/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoSocketException;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.ServerType;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatStartedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;
import com.mongodb.internal.connection.ChangeEvent;
import com.mongodb.internal.connection.ChangeListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.ConnectionPool;
import com.mongodb.internal.connection.DescriptionHelper;
import com.mongodb.internal.connection.ExponentiallyWeightedMovingAverage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.InternalConnectionFactory;
import com.mongodb.internal.connection.ServerMonitor;
import com.mongodb.internal.event.EventListenerHelper;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

@ThreadSafe
class DefaultServerMonitor
implements ServerMonitor {
    private static final Logger LOGGER = Loggers.getLogger("cluster");
    private final ServerId serverId;
    private final ServerMonitorListener serverMonitorListener;
    private final ClusterClock clusterClock;
    private final ChangeListener<ServerDescription> serverStateListener;
    private final InternalConnectionFactory internalConnectionFactory;
    private final ConnectionPool connectionPool;
    private final ServerSettings serverSettings;
    private final ServerMonitorRunnable monitor;
    private final Thread monitorThread;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = this.lock.newCondition();
    private volatile boolean isClosed;

    DefaultServerMonitor(ServerId serverId, ServerSettings serverSettings, ClusterClock clusterClock, ChangeListener<ServerDescription> serverStateListener, InternalConnectionFactory internalConnectionFactory, ConnectionPool connectionPool) {
        this.serverSettings = Assertions.notNull("serverSettings", serverSettings);
        this.serverId = Assertions.notNull("serverId", serverId);
        this.serverMonitorListener = EventListenerHelper.getServerMonitorListener(serverSettings);
        this.clusterClock = Assertions.notNull("clusterClock", clusterClock);
        this.serverStateListener = serverStateListener;
        this.internalConnectionFactory = Assertions.notNull("internalConnectionFactory", internalConnectionFactory);
        this.connectionPool = connectionPool;
        this.monitor = new ServerMonitorRunnable();
        this.monitorThread = new Thread((Runnable)this.monitor, "cluster-" + this.serverId.getClusterId() + "-" + this.serverId.getAddress());
        this.monitorThread.setDaemon(true);
        this.isClosed = false;
    }

    @Override
    public void start() {
        this.monitorThread.start();
    }

    @Override
    public void connect() {
        this.lock.lock();
        try {
            this.condition.signal();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void close() {
        this.isClosed = true;
        this.monitorThread.interrupt();
    }

    static boolean shouldLogStageChange(ServerDescription previous, ServerDescription current) {
        String thatExceptionMessage;
        Class<?> thatExceptionClass;
        if (previous.isOk() != current.isOk()) {
            return true;
        }
        if (!previous.getAddress().equals(current.getAddress())) {
            return true;
        }
        if (previous.getCanonicalAddress() != null ? !previous.getCanonicalAddress().equals(current.getCanonicalAddress()) : current.getCanonicalAddress() != null) {
            return true;
        }
        if (!previous.getHosts().equals(current.getHosts())) {
            return true;
        }
        if (!previous.getArbiters().equals(current.getArbiters())) {
            return true;
        }
        if (!previous.getPassives().equals(current.getPassives())) {
            return true;
        }
        if (previous.getPrimary() != null ? !previous.getPrimary().equals(current.getPrimary()) : current.getPrimary() != null) {
            return true;
        }
        if (previous.getSetName() != null ? !previous.getSetName().equals(current.getSetName()) : current.getSetName() != null) {
            return true;
        }
        if (previous.getState() != current.getState()) {
            return true;
        }
        if (!previous.getTagSet().equals(current.getTagSet())) {
            return true;
        }
        if (previous.getType() != current.getType()) {
            return true;
        }
        if (!previous.getVersion().equals(current.getVersion())) {
            return true;
        }
        if (previous.getElectionId() != null ? !previous.getElectionId().equals(current.getElectionId()) : current.getElectionId() != null) {
            return true;
        }
        if (previous.getSetVersion() != null ? !previous.getSetVersion().equals(current.getSetVersion()) : current.getSetVersion() != null) {
            return true;
        }
        Class<?> thisExceptionClass = previous.getException() != null ? previous.getException().getClass() : null;
        Class<?> clazz = thatExceptionClass = current.getException() != null ? current.getException().getClass() : null;
        if (thisExceptionClass != null ? !thisExceptionClass.equals(thatExceptionClass) : thatExceptionClass != null) {
            return true;
        }
        String thisExceptionMessage = previous.getException() != null ? previous.getException().getMessage() : null;
        String string = thatExceptionMessage = current.getException() != null ? current.getException().getMessage() : null;
        return thisExceptionMessage != null ? !thisExceptionMessage.equals(thatExceptionMessage) : thatExceptionMessage != null;
    }

    class ServerMonitorRunnable
    implements Runnable {
        private final ExponentiallyWeightedMovingAverage averageRoundTripTime = new ExponentiallyWeightedMovingAverage(0.2);

        ServerMonitorRunnable() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public synchronized void run() {
            InternalConnection connection = null;
            try {
                ServerDescription currentServerDescription = this.getConnectingServerDescription(null);
                while (!DefaultServerMonitor.this.isClosed) {
                    ServerDescription previousServerDescription = currentServerDescription;
                    try {
                        if (connection == null) {
                            connection = DefaultServerMonitor.this.internalConnectionFactory.create(DefaultServerMonitor.this.serverId);
                            try {
                                connection.open();
                            } catch (Throwable t2) {
                                connection = null;
                                throw t2;
                            }
                        }
                        try {
                            currentServerDescription = this.lookupServerDescription(connection);
                        } catch (MongoSocketException e2) {
                            DefaultServerMonitor.this.connectionPool.invalidate();
                            connection.close();
                            connection = null;
                            connection = DefaultServerMonitor.this.internalConnectionFactory.create(DefaultServerMonitor.this.serverId);
                            try {
                                connection.open();
                            } catch (Throwable t3) {
                                connection = null;
                                throw t3;
                            }
                            try {
                                currentServerDescription = this.lookupServerDescription(connection);
                            } catch (MongoSocketException e1) {
                                connection.close();
                                connection = null;
                                throw e1;
                            }
                        }
                    } catch (Throwable t4) {
                        this.averageRoundTripTime.reset();
                        currentServerDescription = this.getConnectingServerDescription(t4);
                    }
                    if (DefaultServerMonitor.this.isClosed) continue;
                    try {
                        this.logStateChange(previousServerDescription, currentServerDescription);
                        DefaultServerMonitor.this.serverStateListener.stateChanged(new ChangeEvent<ServerDescription>(previousServerDescription, currentServerDescription));
                    } catch (Throwable t5) {
                        LOGGER.warn("Exception in monitor thread during notification of server description state change", t5);
                    }
                    this.waitForNext();
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }

        private ServerDescription getConnectingServerDescription(Throwable exception) {
            return ServerDescription.builder().type(ServerType.UNKNOWN).state(ServerConnectionState.CONNECTING).address(DefaultServerMonitor.this.serverId.getAddress()).exception(exception).build();
        }

        private ServerDescription lookupServerDescription(InternalConnection connection) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Checking status of %s", DefaultServerMonitor.this.serverId.getAddress()));
            }
            DefaultServerMonitor.this.serverMonitorListener.serverHearbeatStarted(new ServerHeartbeatStartedEvent(connection.getDescription().getConnectionId()));
            long start = System.nanoTime();
            try {
                BsonDocument isMasterResult = CommandHelper.executeCommand("admin", new BsonDocument("ismaster", new BsonInt32(1)), DefaultServerMonitor.this.clusterClock, connection);
                long elapsedTimeNanos = System.nanoTime() - start;
                this.averageRoundTripTime.addSample(elapsedTimeNanos);
                DefaultServerMonitor.this.serverMonitorListener.serverHeartbeatSucceeded(new ServerHeartbeatSucceededEvent(connection.getDescription().getConnectionId(), isMasterResult, elapsedTimeNanos));
                return DescriptionHelper.createServerDescription(DefaultServerMonitor.this.serverId.getAddress(), isMasterResult, connection.getDescription().getServerVersion(), this.averageRoundTripTime.getAverage());
            } catch (RuntimeException e2) {
                DefaultServerMonitor.this.serverMonitorListener.serverHeartbeatFailed(new ServerHeartbeatFailedEvent(connection.getDescription().getConnectionId(), System.nanoTime() - start, e2));
                throw e2;
            }
        }

        private void logStateChange(ServerDescription previousServerDescription, ServerDescription currentServerDescription) {
            if (DefaultServerMonitor.shouldLogStageChange(previousServerDescription, currentServerDescription)) {
                if (currentServerDescription.getException() != null) {
                    LOGGER.info(String.format("Exception in monitor thread while connecting to server %s", DefaultServerMonitor.this.serverId.getAddress()), currentServerDescription.getException());
                } else {
                    LOGGER.info(String.format("Monitor thread successfully connected to server with description %s", currentServerDescription));
                }
            }
        }

        private void waitForNext() {
            try {
                long millisToSleep;
                long minimumNanosToWait;
                long timeWaiting;
                long timeRemaining = this.waitForSignalOrTimeout();
                if (timeRemaining > 0L && (timeWaiting = DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.NANOSECONDS) - timeRemaining) < (minimumNanosToWait = DefaultServerMonitor.this.serverSettings.getMinHeartbeatFrequency(TimeUnit.NANOSECONDS)) && (millisToSleep = TimeUnit.MILLISECONDS.convert(minimumNanosToWait - timeWaiting, TimeUnit.NANOSECONDS)) > 0L) {
                    Thread.sleep(millisToSleep);
                }
            } catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        private long waitForSignalOrTimeout() throws InterruptedException {
            DefaultServerMonitor.this.lock.lock();
            try {
                long l2 = DefaultServerMonitor.this.condition.awaitNanos(DefaultServerMonitor.this.serverSettings.getHeartbeatFrequency(TimeUnit.NANOSECONDS));
                return l2;
            } finally {
                DefaultServerMonitor.this.lock.unlock();
            }
        }
    }
}

