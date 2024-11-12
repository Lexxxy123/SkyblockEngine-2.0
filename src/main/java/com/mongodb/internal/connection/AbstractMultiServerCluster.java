/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerType;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListenerAdapter;
import com.mongodb.internal.connection.BaseCluster;
import com.mongodb.internal.connection.ClusterableServer;
import com.mongodb.internal.connection.ClusterableServerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;

public abstract class AbstractMultiServerCluster
extends BaseCluster {
    private static final Logger LOGGER = Loggers.getLogger("cluster");
    private ClusterType clusterType;
    private String replicaSetName;
    private ObjectId maxElectionId;
    private Integer maxSetVersion;
    private final ConcurrentMap<ServerAddress, ServerTuple> addressToServerTupleMap = new ConcurrentHashMap<ServerAddress, ServerTuple>();

    AbstractMultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
        super(clusterId, settings, serverFactory);
        Assertions.isTrue("connection mode is multiple", settings.getMode() == ClusterConnectionMode.MULTIPLE);
        this.clusterType = settings.getRequiredClusterType();
        this.replicaSetName = settings.getRequiredReplicaSetName();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Cluster created with settings %s", settings.getShortDescription()));
        }
    }

    ClusterType getClusterType() {
        return this.clusterType;
    }

    MongoException getSrvResolutionException() {
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void initialize(Collection<ServerAddress> serverAddresses) {
        ClusterDescription newDescription;
        ClusterDescription currentDescription = this.getCurrentDescription();
        AbstractMultiServerCluster abstractMultiServerCluster = this;
        synchronized (abstractMultiServerCluster) {
            for (ServerAddress serverAddress : serverAddresses) {
                this.addServer(serverAddress);
            }
            newDescription = this.updateDescription();
        }
        this.fireChangeEvent(new ClusterDescriptionChangedEvent(this.getClusterId(), newDescription, currentDescription));
    }

    @Override
    protected void connect() {
        for (ServerTuple cur : this.addressToServerTupleMap.values()) {
            cur.server.connect();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() {
        AbstractMultiServerCluster abstractMultiServerCluster = this;
        synchronized (abstractMultiServerCluster) {
            if (!this.isClosed()) {
                for (ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
                    serverTuple.server.close();
                }
            }
            super.close();
        }
    }

    @Override
    protected ClusterableServer getServer(ServerAddress serverAddress) {
        Assertions.isTrue("is open", !this.isClosed());
        ServerTuple serverTuple = (ServerTuple)this.addressToServerTupleMap.get(serverAddress);
        if (serverTuple == null) {
            return null;
        }
        return serverTuple.server;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void onChange(Collection<ServerAddress> newHosts) {
        AbstractMultiServerCluster abstractMultiServerCluster = this;
        synchronized (abstractMultiServerCluster) {
            if (this.isClosed()) {
                return;
            }
            for (ServerAddress serverAddress : newHosts) {
                this.addServer(serverAddress);
            }
            Iterator iterator = this.addressToServerTupleMap.values().iterator();
            while (iterator.hasNext()) {
                ServerTuple serverTuple = (ServerTuple)iterator.next();
                if (newHosts.contains(serverTuple.description.getAddress())) continue;
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("Removing %s from client view of cluster.", serverTuple.description.getAddress()));
                }
                iterator.remove();
                serverTuple.server.close();
            }
            ClusterDescription oldClusterDescription = this.getCurrentDescription();
            ClusterDescription clusterDescription = this.updateDescription();
            this.fireChangeEvent(new ClusterDescriptionChangedEvent(this.getClusterId(), clusterDescription, oldClusterDescription));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void onChange(ServerDescriptionChangedEvent event) {
        ClusterDescription oldClusterDescription = null;
        ClusterDescription newClusterDescription = null;
        boolean shouldUpdateDescription = true;
        AbstractMultiServerCluster abstractMultiServerCluster = this;
        synchronized (abstractMultiServerCluster) {
            ServerTuple serverTuple;
            if (this.isClosed()) {
                return;
            }
            ServerDescription newDescription = event.getNewDescription();
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Handling description changed event for server %s with description %s", newDescription.getAddress(), newDescription));
            }
            if ((serverTuple = (ServerTuple)this.addressToServerTupleMap.get(newDescription.getAddress())) == null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(String.format("Ignoring description changed event for removed server %s", newDescription.getAddress()));
                }
                return;
            }
            if (event.getNewDescription().isOk()) {
                if (this.clusterType == ClusterType.UNKNOWN && newDescription.getType() != ServerType.REPLICA_SET_GHOST) {
                    this.clusterType = newDescription.getClusterType();
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("Discovered cluster type of %s", new Object[]{this.clusterType}));
                    }
                }
                switch (this.clusterType) {
                    case REPLICA_SET: {
                        shouldUpdateDescription = this.handleReplicaSetMemberChanged(newDescription);
                        break;
                    }
                    case SHARDED: {
                        shouldUpdateDescription = this.handleShardRouterChanged(newDescription);
                        break;
                    }
                    case STANDALONE: {
                        shouldUpdateDescription = this.handleStandAloneChanged(newDescription);
                        break;
                    }
                }
            }
            if (shouldUpdateDescription) {
                serverTuple.description = newDescription;
                oldClusterDescription = this.getCurrentDescription();
                newClusterDescription = this.updateDescription();
            }
        }
        if (shouldUpdateDescription) {
            this.fireChangeEvent(new ClusterDescriptionChangedEvent(this.getClusterId(), newClusterDescription, oldClusterDescription));
        }
    }

    private boolean handleReplicaSetMemberChanged(ServerDescription newDescription) {
        if (!newDescription.isReplicaSetMember()) {
            LOGGER.error(String.format("Expecting replica set member, but found a %s.  Removing %s from client view of cluster.", new Object[]{newDescription.getType(), newDescription.getAddress()}));
            this.removeServer(newDescription.getAddress());
            return true;
        }
        if (newDescription.getType() == ServerType.REPLICA_SET_GHOST) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Server %s does not appear to be a member of an initiated replica set.", newDescription.getAddress()));
            }
            return true;
        }
        if (this.replicaSetName == null) {
            this.replicaSetName = newDescription.getSetName();
        }
        if (!this.replicaSetName.equals(newDescription.getSetName())) {
            LOGGER.error(String.format("Expecting replica set member from set '%s', but found one from set '%s'.  Removing %s from client view of cluster.", this.replicaSetName, newDescription.getSetName(), newDescription.getAddress()));
            this.removeServer(newDescription.getAddress());
            return true;
        }
        this.ensureServers(newDescription);
        if (newDescription.getCanonicalAddress() != null && !newDescription.getAddress().equals(new ServerAddress(newDescription.getCanonicalAddress()))) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Canonical address %s does not match server address.  Removing %s from client view of cluster", newDescription.getCanonicalAddress(), newDescription.getAddress()));
            }
            this.removeServer(newDescription.getAddress());
            return true;
        }
        if (newDescription.isPrimary()) {
            if (newDescription.getSetVersion() != null && newDescription.getElectionId() != null) {
                if (this.isStalePrimary(newDescription)) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("Invalidating potential primary %s whose (set version, election id) tuple of (%d, %s) is less than one already seen of (%d, %s)", newDescription.getAddress(), newDescription.getSetVersion(), newDescription.getElectionId(), this.maxSetVersion, this.maxElectionId));
                    }
                    ((ServerTuple)this.addressToServerTupleMap.get(newDescription.getAddress())).server.invalidate();
                    return false;
                }
                if (!newDescription.getElectionId().equals(this.maxElectionId)) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("Setting max election id to %s from replica set primary %s", newDescription.getElectionId(), newDescription.getAddress()));
                    }
                    this.maxElectionId = newDescription.getElectionId();
                }
            }
            if (newDescription.getSetVersion() != null && (this.maxSetVersion == null || newDescription.getSetVersion().compareTo(this.maxSetVersion) > 0)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("Setting max set version to %d from replica set primary %s", newDescription.getSetVersion(), newDescription.getAddress()));
                }
                this.maxSetVersion = newDescription.getSetVersion();
            }
            if (this.isNotAlreadyPrimary(newDescription.getAddress())) {
                LOGGER.info(String.format("Discovered replica set primary %s", newDescription.getAddress()));
            }
            this.invalidateOldPrimaries(newDescription.getAddress());
        }
        return true;
    }

    private boolean isStalePrimary(ServerDescription newDescription) {
        if (this.maxSetVersion == null || this.maxElectionId == null) {
            return false;
        }
        return this.maxSetVersion.compareTo(newDescription.getSetVersion()) > 0 || this.maxSetVersion.equals(newDescription.getSetVersion()) && this.maxElectionId.compareTo(newDescription.getElectionId()) > 0;
    }

    private boolean isNotAlreadyPrimary(ServerAddress address) {
        ServerTuple serverTuple = (ServerTuple)this.addressToServerTupleMap.get(address);
        return serverTuple == null || !serverTuple.description.isPrimary();
    }

    private boolean handleShardRouterChanged(ServerDescription newDescription) {
        if (!newDescription.isShardRouter()) {
            LOGGER.error(String.format("Expecting a %s, but found a %s.  Removing %s from client view of cluster.", new Object[]{ServerType.SHARD_ROUTER, newDescription.getType(), newDescription.getAddress()}));
            this.removeServer(newDescription.getAddress());
        }
        return true;
    }

    private boolean handleStandAloneChanged(ServerDescription newDescription) {
        if (this.getSettings().getHosts().size() > 1) {
            LOGGER.error(String.format("Expecting a single %s, but found more than one.  Removing %s from client view of cluster.", new Object[]{ServerType.STANDALONE, newDescription.getAddress()}));
            this.clusterType = ClusterType.UNKNOWN;
            this.removeServer(newDescription.getAddress());
        }
        return true;
    }

    private void addServer(ServerAddress serverAddress) {
        if (!this.addressToServerTupleMap.containsKey(serverAddress)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Adding discovered server %s to client view of cluster", serverAddress));
            }
            ClusterableServer server = this.createServer(serverAddress, new DefaultServerStateListener());
            this.addressToServerTupleMap.put(serverAddress, new ServerTuple(server, this.getConnectingServerDescription(serverAddress)));
        }
    }

    private void removeServer(ServerAddress serverAddress) {
        ServerTuple removed = (ServerTuple)this.addressToServerTupleMap.remove(serverAddress);
        if (removed != null) {
            removed.server.close();
        }
    }

    private void invalidateOldPrimaries(ServerAddress newPrimary) {
        for (ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
            if (serverTuple.description.getAddress().equals(newPrimary) || !serverTuple.description.isPrimary()) continue;
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Rediscovering type of existing primary %s", serverTuple.description.getAddress()));
            }
            serverTuple.server.invalidate(new MongoNotPrimaryException(new BsonDocument(), serverTuple.description.getAddress()));
        }
    }

    private ServerDescription getConnectingServerDescription(ServerAddress serverAddress) {
        return ServerDescription.builder().state(ServerConnectionState.CONNECTING).address(serverAddress).build();
    }

    private ClusterDescription updateDescription() {
        ClusterDescription newDescription = new ClusterDescription(ClusterConnectionMode.MULTIPLE, this.clusterType, this.getSrvResolutionException(), this.getNewServerDescriptionList(), this.getSettings(), this.getServerFactory().getSettings());
        this.updateDescription(newDescription);
        return newDescription;
    }

    private List<ServerDescription> getNewServerDescriptionList() {
        ArrayList<ServerDescription> serverDescriptions = new ArrayList<ServerDescription>();
        for (ServerTuple cur : this.addressToServerTupleMap.values()) {
            serverDescriptions.add(cur.description);
        }
        return serverDescriptions;
    }

    private void ensureServers(ServerDescription description) {
        if (description.isPrimary() || !this.hasPrimary()) {
            this.addNewHosts(description.getHosts());
            this.addNewHosts(description.getPassives());
            this.addNewHosts(description.getArbiters());
        }
        if (description.isPrimary()) {
            this.removeExtraHosts(description);
        }
    }

    private boolean hasPrimary() {
        for (ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
            if (!serverTuple.description.isPrimary()) continue;
            return true;
        }
        return false;
    }

    private void addNewHosts(Set<String> hosts) {
        for (String cur : hosts) {
            this.addServer(new ServerAddress(cur));
        }
    }

    private void removeExtraHosts(ServerDescription serverDescription) {
        Set<ServerAddress> allServerAddresses = this.getAllServerAddresses(serverDescription);
        Iterator iterator = this.addressToServerTupleMap.values().iterator();
        while (iterator.hasNext()) {
            ServerTuple cur = (ServerTuple)iterator.next();
            if (allServerAddresses.contains(cur.description.getAddress())) continue;
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Server %s is no longer a member of the replica set.  Removing from client view of cluster.", cur.description.getAddress()));
            }
            iterator.remove();
            cur.server.close();
        }
    }

    private Set<ServerAddress> getAllServerAddresses(ServerDescription serverDescription) {
        HashSet<ServerAddress> retVal = new HashSet<ServerAddress>();
        this.addHostsToSet(serverDescription.getHosts(), retVal);
        this.addHostsToSet(serverDescription.getPassives(), retVal);
        this.addHostsToSet(serverDescription.getArbiters(), retVal);
        return retVal;
    }

    private void addHostsToSet(Set<String> hosts, Set<ServerAddress> retVal) {
        for (String host : hosts) {
            retVal.add(new ServerAddress(host));
        }
    }

    private final class DefaultServerStateListener
    extends ServerListenerAdapter {
        private DefaultServerStateListener() {
        }

        @Override
        public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
            AbstractMultiServerCluster.this.onChange(event);
        }
    }

    private static final class ServerTuple {
        private final ClusterableServer server;
        private ServerDescription description;

        private ServerTuple(ClusterableServer server, ServerDescription description) {
            this.server = server;
            this.description = description;
        }
    }
}

