/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWaitQueueFullException;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerId;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.ConcurrentPool;
import com.mongodb.internal.connection.ConnectionPool;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.InternalConnectionFactory;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.internal.connection.UsageTrackingInternalConnection;
import com.mongodb.internal.event.EventListenerHelper;
import com.mongodb.internal.thread.DaemonThreadFactory;
import com.mongodb.session.SessionContext;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.ByteBuf;
import org.bson.codecs.Decoder;

class DefaultConnectionPool
implements ConnectionPool {
    private static final Logger LOGGER = Loggers.getLogger("connection");
    private final ConcurrentPool<UsageTrackingInternalConnection> pool;
    private final ConnectionPoolSettings settings;
    private final AtomicInteger waitQueueSize = new AtomicInteger(0);
    private final AtomicInteger generation = new AtomicInteger(0);
    private final AtomicInteger lastPrunedGeneration = new AtomicInteger(0);
    private final ScheduledExecutorService sizeMaintenanceTimer;
    private ExecutorService asyncGetter;
    private final Runnable maintenanceTask;
    private final ConnectionPoolListener connectionPoolListener;
    private final ServerId serverId;
    private volatile boolean closed;

    DefaultConnectionPool(ServerId serverId, InternalConnectionFactory internalConnectionFactory, ConnectionPoolSettings settings) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.settings = Assertions.notNull("settings", settings);
        UsageTrackingInternalConnectionItemFactory connectionItemFactory = new UsageTrackingInternalConnectionItemFactory(internalConnectionFactory);
        this.pool = new ConcurrentPool<UsageTrackingInternalConnection>(settings.getMaxSize(), connectionItemFactory);
        this.connectionPoolListener = EventListenerHelper.getConnectionPoolListener(settings);
        this.maintenanceTask = this.createMaintenanceTask();
        this.sizeMaintenanceTimer = this.createMaintenanceTimer();
        this.connectionPoolListener.connectionPoolOpened(new ConnectionPoolOpenedEvent(serverId, settings));
    }

    @Override
    public void start() {
        if (this.sizeMaintenanceTimer != null) {
            this.sizeMaintenanceTimer.scheduleAtFixedRate(this.maintenanceTask, this.settings.getMaintenanceInitialDelay(TimeUnit.MILLISECONDS), this.settings.getMaintenanceFrequency(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public InternalConnection get() {
        return this.get(this.settings.getMaxWaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public InternalConnection get(long timeout, TimeUnit timeUnit) {
        try {
            if (this.waitQueueSize.incrementAndGet() > this.settings.getMaxWaitQueueSize()) {
                throw this.createWaitQueueFullException();
            }
            try {
                this.connectionPoolListener.waitQueueEntered(new ConnectionPoolWaitQueueEnteredEvent(this.serverId));
                PooledConnection pooledConnection = this.getPooledConnection(timeout, timeUnit);
                if (!pooledConnection.opened()) {
                    try {
                        pooledConnection.open();
                    } catch (Throwable t2) {
                        this.pool.release(pooledConnection.wrapped, true);
                        if (t2 instanceof MongoException) {
                            throw (MongoException)t2;
                        }
                        throw new MongoInternalException(t2.toString(), t2);
                    }
                }
                PooledConnection pooledConnection2 = pooledConnection;
                this.connectionPoolListener.waitQueueExited(new ConnectionPoolWaitQueueExitedEvent(this.serverId));
                return pooledConnection2;
            } catch (Throwable throwable) {
                this.connectionPoolListener.waitQueueExited(new ConnectionPoolWaitQueueExitedEvent(this.serverId));
                throw throwable;
            }
        } finally {
            this.waitQueueSize.decrementAndGet();
        }
    }

    @Override
    public void getAsync(SingleResultCallback<InternalConnection> callback) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Asynchronously getting a connection from the pool for server %s", this.serverId));
        }
        final SingleResultCallback<InternalConnection> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);
        PooledConnection connection = null;
        try {
            connection = this.getPooledConnection(0L, TimeUnit.MILLISECONDS);
        } catch (MongoTimeoutException mongoTimeoutException) {
        } catch (Throwable t2) {
            callback.onResult(null, t2);
            return;
        }
        if (connection != null) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Asynchronously opening pooled connection %s to server %s", connection.getDescription().getConnectionId(), this.serverId));
            }
            this.openAsync(connection, errHandlingCallback);
        } else if (this.waitQueueSize.incrementAndGet() > this.settings.getMaxWaitQueueSize()) {
            this.waitQueueSize.decrementAndGet();
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Asynchronously failing to get a pooled connection to %s because the wait queue is full", this.serverId));
            }
            callback.onResult(null, this.createWaitQueueFullException());
        } else {
            final long startTimeMillis = System.currentTimeMillis();
            this.connectionPoolListener.waitQueueEntered(new ConnectionPoolWaitQueueEnteredEvent(this.serverId));
            this.getAsyncGetter().submit(new Runnable(){

                @Override
                public void run() {
                    try {
                        if (this.getRemainingWaitTime() <= 0L) {
                            errHandlingCallback.onResult(null, DefaultConnectionPool.this.createTimeoutException());
                        } else {
                            PooledConnection connection = DefaultConnectionPool.this.getPooledConnection(this.getRemainingWaitTime(), TimeUnit.MILLISECONDS);
                            DefaultConnectionPool.this.openAsync(connection, errHandlingCallback);
                        }
                    } catch (Throwable t2) {
                        errHandlingCallback.onResult(null, t2);
                    } finally {
                        DefaultConnectionPool.this.waitQueueSize.decrementAndGet();
                        DefaultConnectionPool.this.connectionPoolListener.waitQueueExited(new ConnectionPoolWaitQueueExitedEvent(DefaultConnectionPool.this.serverId));
                    }
                }

                private long getRemainingWaitTime() {
                    return startTimeMillis + DefaultConnectionPool.this.settings.getMaxWaitTime(TimeUnit.MILLISECONDS) - System.currentTimeMillis();
                }
            });
        }
    }

    private void openAsync(final PooledConnection pooledConnection, final SingleResultCallback<InternalConnection> callback) {
        if (pooledConnection.opened()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Pooled connection %s to server %s is already open", pooledConnection.getDescription().getConnectionId(), this.serverId));
            }
            callback.onResult(pooledConnection, null);
        } else {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Pooled connection %s to server %s is not yet open", pooledConnection.getDescription().getConnectionId(), this.serverId));
            }
            pooledConnection.openAsync(new SingleResultCallback<Void>(){

                @Override
                public void onResult(Void result, Throwable t2) {
                    if (t2 != null) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace(String.format("Pooled connection %s to server %s failed to open", pooledConnection.getDescription().getConnectionId(), DefaultConnectionPool.this.serverId));
                        }
                        callback.onResult(null, t2);
                        DefaultConnectionPool.this.pool.release(pooledConnection.wrapped, true);
                    } else {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace(String.format("Pooled connection %s to server %s is now open", pooledConnection.getDescription().getConnectionId(), DefaultConnectionPool.this.serverId));
                        }
                        callback.onResult(pooledConnection, null);
                    }
                }
            });
        }
    }

    private synchronized ExecutorService getAsyncGetter() {
        if (this.asyncGetter == null) {
            this.asyncGetter = Executors.newSingleThreadExecutor(new DaemonThreadFactory("AsyncGetter"));
        }
        return this.asyncGetter;
    }

    private synchronized void shutdownAsyncGetter() {
        if (this.asyncGetter != null) {
            this.asyncGetter.shutdownNow();
        }
    }

    @Override
    public void invalidate() {
        LOGGER.debug("Invalidating the connection pool");
        this.generation.incrementAndGet();
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.pool.close();
            if (this.sizeMaintenanceTimer != null) {
                this.sizeMaintenanceTimer.shutdownNow();
            }
            this.shutdownAsyncGetter();
            this.closed = true;
            this.connectionPoolListener.connectionPoolClosed(new ConnectionPoolClosedEvent(this.serverId));
        }
    }

    public void doMaintenance() {
        if (this.maintenanceTask != null) {
            this.maintenanceTask.run();
        }
    }

    private PooledConnection getPooledConnection(long timeout, TimeUnit timeUnit) {
        UsageTrackingInternalConnection internalConnection = this.pool.get(timeout, timeUnit);
        while (this.shouldPrune(internalConnection)) {
            this.pool.release(internalConnection, true);
            internalConnection = this.pool.get(timeout, timeUnit);
        }
        this.connectionPoolListener.connectionCheckedOut(new ConnectionCheckedOutEvent(internalConnection.getDescription().getConnectionId()));
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Checked out connection [%s] to server %s", this.getId(internalConnection), this.serverId.getAddress()));
        }
        return new PooledConnection(internalConnection);
    }

    private MongoTimeoutException createTimeoutException() {
        return new MongoTimeoutException(String.format("Timed out after %d ms while waiting for a connection to server %s.", this.settings.getMaxWaitTime(TimeUnit.MILLISECONDS), this.serverId.getAddress()));
    }

    private MongoWaitQueueFullException createWaitQueueFullException() {
        return new MongoWaitQueueFullException(String.format("Too many operations are already waiting for a connection. Max number of operations (maxWaitQueueSize) of %d has been exceeded.", this.settings.getMaxWaitQueueSize()));
    }

    ConcurrentPool<UsageTrackingInternalConnection> getPool() {
        return this.pool;
    }

    private Runnable createMaintenanceTask() {
        Runnable newMaintenanceTask = null;
        if (this.shouldPrune() || this.shouldEnsureMinSize()) {
            newMaintenanceTask = new Runnable(){

                @Override
                public synchronized void run() {
                    try {
                        int curGeneration = DefaultConnectionPool.this.generation.get();
                        if (DefaultConnectionPool.this.shouldPrune() || curGeneration > DefaultConnectionPool.this.lastPrunedGeneration.get()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(String.format("Pruning pooled connections to %s", DefaultConnectionPool.this.serverId.getAddress()));
                            }
                            DefaultConnectionPool.this.pool.prune();
                        }
                        DefaultConnectionPool.this.lastPrunedGeneration.set(curGeneration);
                        if (DefaultConnectionPool.this.shouldEnsureMinSize()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug(String.format("Ensuring minimum pooled connections to %s", DefaultConnectionPool.this.serverId.getAddress()));
                            }
                            DefaultConnectionPool.this.pool.ensureMinSize(DefaultConnectionPool.this.settings.getMinSize(), true);
                        }
                    } catch (MongoInterruptedException curGeneration) {
                    } catch (Exception e2) {
                        LOGGER.warn("Exception thrown during connection pool background maintenance task", e2);
                    }
                }
            };
        }
        return newMaintenanceTask;
    }

    private ScheduledExecutorService createMaintenanceTimer() {
        if (this.maintenanceTask == null) {
            return null;
        }
        return Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("MaintenanceTimer"));
    }

    private boolean shouldEnsureMinSize() {
        return this.settings.getMinSize() > 0;
    }

    private boolean shouldPrune() {
        return this.settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS) > 0L || this.settings.getMaxConnectionLifeTime(TimeUnit.MILLISECONDS) > 0L;
    }

    private boolean shouldPrune(UsageTrackingInternalConnection connection) {
        return this.fromPreviousGeneration(connection) || this.pastMaxLifeTime(connection) || this.pastMaxIdleTime(connection);
    }

    private boolean pastMaxIdleTime(UsageTrackingInternalConnection connection) {
        return this.expired(connection.getLastUsedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS));
    }

    private boolean pastMaxLifeTime(UsageTrackingInternalConnection connection) {
        return this.expired(connection.getOpenedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionLifeTime(TimeUnit.MILLISECONDS));
    }

    private boolean fromPreviousGeneration(UsageTrackingInternalConnection connection) {
        return this.generation.get() > connection.getGeneration();
    }

    private boolean expired(long startTime, long curTime, long maxTime) {
        return maxTime != 0L && curTime - startTime > maxTime;
    }

    private void incrementGenerationOnSocketException(InternalConnection connection, Throwable t2) {
        if (t2 instanceof MongoSocketException && !(t2 instanceof MongoSocketReadTimeoutException)) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("Got socket exception on connection [%s] to %s. All connections to %s will be closed.", this.getId(connection), this.serverId.getAddress(), this.serverId.getAddress()));
            }
            this.invalidate();
        }
    }

    private ConnectionId getId(InternalConnection internalConnection) {
        return internalConnection.getDescription().getConnectionId();
    }

    private class UsageTrackingInternalConnectionItemFactory
    implements ConcurrentPool.ItemFactory<UsageTrackingInternalConnection> {
        private final InternalConnectionFactory internalConnectionFactory;

        UsageTrackingInternalConnectionItemFactory(InternalConnectionFactory internalConnectionFactory) {
            this.internalConnectionFactory = internalConnectionFactory;
        }

        @Override
        public UsageTrackingInternalConnection create(boolean initialize) {
            UsageTrackingInternalConnection internalConnection = new UsageTrackingInternalConnection(this.internalConnectionFactory.create(DefaultConnectionPool.this.serverId), DefaultConnectionPool.this.generation.get());
            if (initialize) {
                internalConnection.open();
            }
            DefaultConnectionPool.this.connectionPoolListener.connectionAdded(new ConnectionAddedEvent(DefaultConnectionPool.this.getId(internalConnection)));
            return internalConnection;
        }

        @Override
        public void close(UsageTrackingInternalConnection connection) {
            DefaultConnectionPool.this.connectionPoolListener.connectionRemoved(new ConnectionRemovedEvent(DefaultConnectionPool.this.getId(connection), this.getReasonForClosing(connection)));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Closed connection [%s] to %s because %s.", DefaultConnectionPool.this.getId(connection), DefaultConnectionPool.this.serverId.getAddress(), this.getReasonStringForClosing(connection)));
            }
            connection.close();
        }

        private String getReasonStringForClosing(UsageTrackingInternalConnection connection) {
            String reason = connection.isClosed() ? "there was a socket exception raised by this connection" : (DefaultConnectionPool.this.fromPreviousGeneration(connection) ? "there was a socket exception raised on another connection from this pool" : (DefaultConnectionPool.this.pastMaxLifeTime(connection) ? "it is past its maximum allowed life time" : (DefaultConnectionPool.this.pastMaxIdleTime(connection) ? "it is past its maximum allowed idle time" : "the pool has been closed")));
            return reason;
        }

        private ConnectionRemovedEvent.Reason getReasonForClosing(UsageTrackingInternalConnection connection) {
            ConnectionRemovedEvent.Reason reason = connection.isClosed() ? ConnectionRemovedEvent.Reason.ERROR : (DefaultConnectionPool.this.fromPreviousGeneration(connection) ? ConnectionRemovedEvent.Reason.STALE : (DefaultConnectionPool.this.pastMaxLifeTime(connection) ? ConnectionRemovedEvent.Reason.MAX_LIFE_TIME_EXCEEDED : (DefaultConnectionPool.this.pastMaxIdleTime(connection) ? ConnectionRemovedEvent.Reason.MAX_IDLE_TIME_EXCEEDED : ConnectionRemovedEvent.Reason.POOL_CLOSED)));
            return reason;
        }

        @Override
        public ConcurrentPool.Prune shouldPrune(UsageTrackingInternalConnection usageTrackingConnection) {
            return DefaultConnectionPool.this.shouldPrune(usageTrackingConnection) ? ConcurrentPool.Prune.YES : ConcurrentPool.Prune.NO;
        }
    }

    private class PooledConnection
    implements InternalConnection {
        private final UsageTrackingInternalConnection wrapped;
        private final AtomicBoolean isClosed = new AtomicBoolean();

        PooledConnection(UsageTrackingInternalConnection wrapped) {
            this.wrapped = Assertions.notNull("wrapped", wrapped);
        }

        @Override
        public void open() {
            Assertions.isTrue("open", !this.isClosed.get());
            this.wrapped.open();
        }

        @Override
        public void openAsync(SingleResultCallback<Void> callback) {
            Assertions.isTrue("open", !this.isClosed.get());
            this.wrapped.openAsync(callback);
        }

        @Override
        public void close() {
            if (!this.isClosed.getAndSet(true)) {
                DefaultConnectionPool.this.connectionPoolListener.connectionCheckedIn(new ConnectionCheckedInEvent(DefaultConnectionPool.this.getId(this.wrapped)));
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(String.format("Checked in connection [%s] to server %s", DefaultConnectionPool.this.getId(this.wrapped), DefaultConnectionPool.this.serverId.getAddress()));
                }
                DefaultConnectionPool.this.pool.release(this.wrapped, this.wrapped.isClosed() || DefaultConnectionPool.this.shouldPrune(this.wrapped));
            }
        }

        @Override
        public boolean opened() {
            Assertions.isTrue("open", !this.isClosed.get());
            return this.wrapped.opened();
        }

        @Override
        public boolean isClosed() {
            return this.isClosed.get() || this.wrapped.isClosed();
        }

        @Override
        public ByteBuf getBuffer(int capacity) {
            return this.wrapped.getBuffer(capacity);
        }

        @Override
        public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
            Assertions.isTrue("open", !this.isClosed.get());
            try {
                this.wrapped.sendMessage(byteBuffers, lastRequestId);
            } catch (MongoException e2) {
                DefaultConnectionPool.this.incrementGenerationOnSocketException(this, e2);
                throw e2;
            }
        }

        @Override
        public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
            Assertions.isTrue("open", !this.isClosed.get());
            try {
                return this.wrapped.sendAndReceive(message, decoder, sessionContext);
            } catch (MongoException e2) {
                DefaultConnectionPool.this.incrementGenerationOnSocketException(this, e2);
                throw e2;
            }
        }

        @Override
        public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, final SingleResultCallback<T> callback) {
            Assertions.isTrue("open", !this.isClosed.get());
            this.wrapped.sendAndReceiveAsync(message, decoder, sessionContext, new SingleResultCallback<T>(){

                @Override
                public void onResult(T result, Throwable t2) {
                    if (t2 != null) {
                        DefaultConnectionPool.this.incrementGenerationOnSocketException(PooledConnection.this, t2);
                    }
                    callback.onResult(result, t2);
                }
            });
        }

        @Override
        public ResponseBuffers receiveMessage(int responseTo) {
            Assertions.isTrue("open", !this.isClosed.get());
            try {
                return this.wrapped.receiveMessage(responseTo);
            } catch (MongoException e2) {
                DefaultConnectionPool.this.incrementGenerationOnSocketException(this, e2);
                throw e2;
            }
        }

        @Override
        public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, final SingleResultCallback<Void> callback) {
            Assertions.isTrue("open", !this.isClosed.get());
            this.wrapped.sendMessageAsync(byteBuffers, lastRequestId, new SingleResultCallback<Void>(){

                @Override
                public void onResult(Void result, Throwable t2) {
                    if (t2 != null) {
                        DefaultConnectionPool.this.incrementGenerationOnSocketException(PooledConnection.this, t2);
                    }
                    callback.onResult(null, t2);
                }
            });
        }

        @Override
        public void receiveMessageAsync(int responseTo, final SingleResultCallback<ResponseBuffers> callback) {
            Assertions.isTrue("open", !this.isClosed.get());
            this.wrapped.receiveMessageAsync(responseTo, new SingleResultCallback<ResponseBuffers>(){

                @Override
                public void onResult(ResponseBuffers result, Throwable t2) {
                    if (t2 != null) {
                        DefaultConnectionPool.this.incrementGenerationOnSocketException(PooledConnection.this, t2);
                    }
                    callback.onResult(result, t2);
                }
            });
        }

        @Override
        public ConnectionDescription getDescription() {
            return this.wrapped.getDescription();
        }
    }
}

