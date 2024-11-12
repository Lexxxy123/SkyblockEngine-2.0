/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoClientException;
import com.mongodb.MongoIncompatibleDriverException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWaitQueueFullException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.Server;
import com.mongodb.connection.ServerDescription;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterOpeningEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ClusterableServer;
import com.mongodb.internal.connection.ClusterableServerFactory;
import com.mongodb.internal.connection.ConcurrentLinkedDeque;
import com.mongodb.internal.event.EventListenerHelper;
import com.mongodb.selector.CompositeServerSelector;
import com.mongodb.selector.ServerSelector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.bson.BsonTimestamp;

abstract class BaseCluster
implements Cluster {
    private static final Logger LOGGER = Loggers.getLogger("cluster");
    private final AtomicReference<CountDownLatch> phase = new AtomicReference<CountDownLatch>(new CountDownLatch(1));
    private final ClusterableServerFactory serverFactory;
    private final ThreadLocal<Random> random = new ThreadLocal();
    private final ClusterId clusterId;
    private final ClusterSettings settings;
    private final ClusterListener clusterListener;
    private final Deque<ServerSelectionRequest> waitQueue = new ConcurrentLinkedDeque<ServerSelectionRequest>();
    private final AtomicInteger waitQueueSize = new AtomicInteger(0);
    private final ClusterClock clusterClock = new ClusterClock();
    private Thread waitQueueHandler;
    private volatile boolean isClosed;
    private volatile ClusterDescription description;

    BaseCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
        this.clusterId = Assertions.notNull("clusterId", clusterId);
        this.settings = Assertions.notNull("settings", settings);
        this.serverFactory = Assertions.notNull("serverFactory", serverFactory);
        this.clusterListener = EventListenerHelper.getClusterListener(settings);
        this.clusterListener.clusterOpening(new ClusterOpeningEvent(clusterId));
        this.description = new ClusterDescription(settings.getMode(), ClusterType.UNKNOWN, Collections.<ServerDescription>emptyList(), settings, serverFactory.getSettings());
    }

    @Override
    public BsonTimestamp getClusterTime() {
        return this.clusterClock.getClusterTime();
    }

    @Override
    public Server selectServer(ServerSelector serverSelector) {
        Assertions.isTrue("open", !this.isClosed());
        try {
            long startTimeNanos;
            CountDownLatch currentPhase = this.phase.get();
            ClusterDescription curDescription = this.description;
            ServerSelector compositeServerSelector = this.getCompositeServerSelector(serverSelector);
            Server server = this.selectRandomServer(compositeServerSelector, curDescription);
            boolean selectionFailureLogged = false;
            long curTimeNanos = startTimeNanos = System.nanoTime();
            long maxWaitTimeNanos = this.getMaxWaitTimeNanos();
            while (true) {
                this.throwIfIncompatible(curDescription);
                if (server != null) {
                    return server;
                }
                if (curTimeNanos - startTimeNanos > maxWaitTimeNanos) {
                    throw this.createTimeoutException(serverSelector, curDescription);
                }
                if (!selectionFailureLogged) {
                    this.logServerSelectionFailure(serverSelector, curDescription);
                    selectionFailureLogged = true;
                }
                this.connect();
                currentPhase.await(Math.min(maxWaitTimeNanos - (curTimeNanos - startTimeNanos), this.getMinWaitTimeNanos()), TimeUnit.NANOSECONDS);
                curTimeNanos = System.nanoTime();
                currentPhase = this.phase.get();
                curDescription = this.description;
                server = this.selectRandomServer(compositeServerSelector, curDescription);
            }
        } catch (InterruptedException e2) {
            throw new MongoInterruptedException(String.format("Interrupted while waiting for a server that matches %s", serverSelector), e2);
        }
    }

    @Override
    public void selectServerAsync(ServerSelector serverSelector, SingleResultCallback<Server> callback) {
        ClusterDescription currentDescription;
        CountDownLatch currentPhase;
        ServerSelectionRequest request;
        Assertions.isTrue("open", !this.isClosed());
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Asynchronously selecting server with selector %s", serverSelector));
        }
        if (!this.handleServerSelectionRequest(request = new ServerSelectionRequest(serverSelector, this.getCompositeServerSelector(serverSelector), this.getMaxWaitTimeNanos(), callback), currentPhase = this.phase.get(), currentDescription = this.description)) {
            this.notifyWaitQueueHandler(request);
        }
    }

    @Override
    public ClusterDescription getDescription() {
        Assertions.isTrue("open", !this.isClosed());
        try {
            long startTimeNanos;
            CountDownLatch currentPhase = this.phase.get();
            ClusterDescription curDescription = this.description;
            boolean selectionFailureLogged = false;
            long curTimeNanos = startTimeNanos = System.nanoTime();
            long maxWaitTimeNanos = this.getMaxWaitTimeNanos();
            while (curDescription.getType() == ClusterType.UNKNOWN) {
                if (curTimeNanos - startTimeNanos > maxWaitTimeNanos) {
                    throw new MongoTimeoutException(String.format("Timed out after %d ms while waiting to connect. Client view of cluster state is %s", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS), curDescription.getShortDescription()));
                }
                if (!selectionFailureLogged) {
                    if (LOGGER.isInfoEnabled()) {
                        if (this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS) < 0L) {
                            LOGGER.info("Cluster description not yet available. Waiting indefinitely.");
                        } else {
                            LOGGER.info(String.format("Cluster description not yet available. Waiting for %d ms before timing out", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS)));
                        }
                    }
                    selectionFailureLogged = true;
                }
                this.connect();
                currentPhase.await(Math.min(maxWaitTimeNanos - (curTimeNanos - startTimeNanos), this.getMinWaitTimeNanos()), TimeUnit.NANOSECONDS);
                curTimeNanos = System.nanoTime();
                currentPhase = this.phase.get();
                curDescription = this.description;
            }
            return curDescription;
        } catch (InterruptedException e2) {
            throw new MongoInterruptedException("Interrupted while waiting to connect", e2);
        }
    }

    protected ClusterId getClusterId() {
        return this.clusterId;
    }

    @Override
    public ClusterSettings getSettings() {
        return this.settings;
    }

    public ClusterableServerFactory getServerFactory() {
        return this.serverFactory;
    }

    protected abstract void connect();

    @Override
    public void close() {
        if (!this.isClosed()) {
            this.isClosed = true;
            this.phase.get().countDown();
            this.clusterListener.clusterClosed(new ClusterClosedEvent(this.clusterId));
            this.stopWaitQueueHandler();
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    protected abstract ClusterableServer getServer(ServerAddress var1);

    protected synchronized void updateDescription(ClusterDescription newDescription) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Updating cluster description to  %s", newDescription.getShortDescription()));
        }
        this.description = newDescription;
        this.updatePhase();
    }

    protected void fireChangeEvent(ClusterDescriptionChangedEvent event) {
        this.clusterListener.clusterDescriptionChanged(event);
    }

    @Override
    public ClusterDescription getCurrentDescription() {
        return this.description;
    }

    private synchronized void updatePhase() {
        this.phase.getAndSet(new CountDownLatch(1)).countDown();
    }

    private long getMaxWaitTimeNanos() {
        if (this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS) < 0L) {
            return Long.MAX_VALUE;
        }
        return this.settings.getServerSelectionTimeout(TimeUnit.NANOSECONDS);
    }

    private long getMinWaitTimeNanos() {
        return this.serverFactory.getSettings().getMinHeartbeatFrequency(TimeUnit.NANOSECONDS);
    }

    private boolean handleServerSelectionRequest(ServerSelectionRequest request, CountDownLatch currentPhase, ClusterDescription description) {
        try {
            if (currentPhase != request.phase) {
                CountDownLatch prevPhase = request.phase;
                request.phase = currentPhase;
                if (!description.isCompatibleWithDriver()) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Asynchronously failed server selection due to driver incompatibility with server");
                    }
                    request.onResult(null, this.createIncompatibleException(description));
                    return true;
                }
                Server server = this.selectRandomServer(request.compositeSelector, description);
                if (server != null) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace(String.format("Asynchronously selected server %s", server.getDescription().getAddress()));
                    }
                    request.onResult(server, null);
                    return true;
                }
                if (prevPhase == null) {
                    this.logServerSelectionFailure(request.originalSelector, description);
                }
            }
            if (request.timedOut()) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Asynchronously failed server selection after timeout");
                }
                request.onResult(null, this.createTimeoutException(request.originalSelector, description));
                return true;
            }
            return false;
        } catch (Exception e2) {
            request.onResult(null, e2);
            return true;
        }
    }

    private void logServerSelectionFailure(ServerSelector serverSelector, ClusterDescription curDescription) {
        if (LOGGER.isInfoEnabled()) {
            if (this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS) < 0L) {
                LOGGER.info(String.format("No server chosen by %s from cluster description %s. Waiting indefinitely.", serverSelector, curDescription));
            } else {
                LOGGER.info(String.format("No server chosen by %s from cluster description %s. Waiting for %d ms before timing out", serverSelector, curDescription, this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS)));
            }
        }
    }

    private Server selectRandomServer(ServerSelector serverSelector, ClusterDescription clusterDescription) {
        List<ServerDescription> serverDescriptions = serverSelector.select(clusterDescription);
        if (!serverDescriptions.isEmpty()) {
            return this.getRandomServer(new ArrayList<ServerDescription>(serverDescriptions));
        }
        return null;
    }

    private ServerSelector getCompositeServerSelector(ServerSelector serverSelector) {
        if (this.settings.getServerSelector() == null) {
            return serverSelector;
        }
        return new CompositeServerSelector(Arrays.asList(serverSelector, this.settings.getServerSelector()));
    }

    private ClusterableServer getRandomServer(List<ServerDescription> serverDescriptions) {
        while (!serverDescriptions.isEmpty()) {
            int serverPos = this.getRandom().nextInt(serverDescriptions.size());
            ClusterableServer server = this.getServer(serverDescriptions.get(serverPos).getAddress());
            if (server != null) {
                return server;
            }
            serverDescriptions.remove(serverPos);
        }
        return null;
    }

    private Random getRandom() {
        Random result = this.random.get();
        if (result == null) {
            result = new Random();
            this.random.set(result);
        }
        return result;
    }

    protected ClusterableServer createServer(ServerAddress serverAddress, ServerListener serverListener) {
        return this.serverFactory.create(serverAddress, EventListenerHelper.createServerListener(this.serverFactory.getSettings(), serverListener), this.clusterClock);
    }

    private void throwIfIncompatible(ClusterDescription curDescription) {
        if (!curDescription.isCompatibleWithDriver()) {
            throw this.createIncompatibleException(curDescription);
        }
    }

    private MongoIncompatibleDriverException createIncompatibleException(ClusterDescription curDescription) {
        String message;
        ServerDescription incompatibleServer = curDescription.findServerIncompatiblyOlderThanDriver();
        if (incompatibleServer != null) {
            message = String.format("Server at %s reports wire version %d, but this version of the driver requires at least %d (MongoDB %s).", incompatibleServer.getAddress(), incompatibleServer.getMaxWireVersion(), 2, "2.6");
        } else {
            incompatibleServer = curDescription.findServerIncompatiblyNewerThanDriver();
            message = String.format("Server at %s requires wire version %d, but this version of the driver only supports up to %d.", incompatibleServer.getAddress(), incompatibleServer.getMinWireVersion(), 15);
        }
        return new MongoIncompatibleDriverException(message, curDescription);
    }

    private MongoTimeoutException createTimeoutException(ServerSelector serverSelector, ClusterDescription curDescription) {
        return new MongoTimeoutException(String.format("Timed out after %d ms while waiting for a server that matches %s. Client view of cluster state is %s", this.settings.getServerSelectionTimeout(TimeUnit.MILLISECONDS), serverSelector, curDescription.getShortDescription()));
    }

    private MongoWaitQueueFullException createWaitQueueFullException() {
        return new MongoWaitQueueFullException(String.format("Too many operations are already waiting for a server. Max number of operations (maxWaitQueueSize) of %d has been exceeded.", this.settings.getMaxWaitQueueSize()));
    }

    private synchronized void notifyWaitQueueHandler(ServerSelectionRequest request) {
        if (this.isClosed) {
            return;
        }
        if (this.waitQueueSize.incrementAndGet() > this.settings.getMaxWaitQueueSize()) {
            this.waitQueueSize.decrementAndGet();
            request.onResult(null, this.createWaitQueueFullException());
        } else {
            this.waitQueue.add(request);
            if (this.waitQueueHandler == null) {
                this.waitQueueHandler = new Thread((Runnable)new WaitQueueHandler(), "cluster-" + this.clusterId.getValue());
                this.waitQueueHandler.setDaemon(true);
                this.waitQueueHandler.start();
            } else {
                this.updatePhase();
            }
        }
    }

    private synchronized void stopWaitQueueHandler() {
        if (this.waitQueueHandler != null) {
            this.waitQueueHandler.interrupt();
        }
    }

    private final class WaitQueueHandler
    implements Runnable {
        private WaitQueueHandler() {
        }

        @Override
        public void run() {
            while (!BaseCluster.this.isClosed) {
                CountDownLatch currentPhase = (CountDownLatch)BaseCluster.this.phase.get();
                ClusterDescription curDescription = BaseCluster.this.description;
                long waitTimeNanos = Long.MAX_VALUE;
                Iterator iter = BaseCluster.this.waitQueue.iterator();
                while (iter.hasNext()) {
                    ServerSelectionRequest nextRequest = (ServerSelectionRequest)iter.next();
                    if (BaseCluster.this.handleServerSelectionRequest(nextRequest, currentPhase, curDescription)) {
                        iter.remove();
                        BaseCluster.this.waitQueueSize.decrementAndGet();
                        continue;
                    }
                    waitTimeNanos = Math.min(nextRequest.getRemainingTime(), Math.min(BaseCluster.this.getMinWaitTimeNanos(), waitTimeNanos));
                }
                if (waitTimeNanos < Long.MAX_VALUE) {
                    BaseCluster.this.connect();
                }
                try {
                    currentPhase.await(waitTimeNanos, TimeUnit.NANOSECONDS);
                } catch (InterruptedException interruptedException) {}
            }
            Iterator iter = BaseCluster.this.waitQueue.iterator();
            while (iter.hasNext()) {
                ((ServerSelectionRequest)iter.next()).onResult(null, new MongoClientException("Shutdown in progress"));
                iter.remove();
            }
        }
    }

    private static final class ServerSelectionRequest {
        private final ServerSelector originalSelector;
        private final ServerSelector compositeSelector;
        private final long maxWaitTimeNanos;
        private final SingleResultCallback<Server> callback;
        private final long startTimeNanos = System.nanoTime();
        private CountDownLatch phase;

        ServerSelectionRequest(ServerSelector serverSelector, ServerSelector compositeSelector, long maxWaitTimeNanos, SingleResultCallback<Server> callback) {
            this.originalSelector = serverSelector;
            this.compositeSelector = compositeSelector;
            this.maxWaitTimeNanos = maxWaitTimeNanos;
            this.callback = callback;
        }

        void onResult(Server server, Throwable t2) {
            try {
                this.callback.onResult(server, t2);
            } catch (Throwable throwable) {
                // empty catch block
            }
        }

        boolean timedOut() {
            return System.nanoTime() - this.startTimeNanos > this.maxWaitTimeNanos;
        }

        long getRemainingTime() {
            return this.startTimeNanos + this.maxWaitTimeNanos - System.nanoTime();
        }
    }
}

