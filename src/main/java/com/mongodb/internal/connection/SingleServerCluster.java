/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ServerDescription;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListenerAdapter;
import com.mongodb.internal.connection.BaseCluster;
import com.mongodb.internal.connection.ClusterableServer;
import com.mongodb.internal.connection.ClusterableServerFactory;
import java.util.Collections;

public final class SingleServerCluster
extends BaseCluster {
    private static final Logger LOGGER = Loggers.getLogger("cluster");
    private final ClusterableServer server;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SingleServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
        super(clusterId, settings, serverFactory);
        Assertions.isTrue("one server in a direct cluster", settings.getHosts().size() == 1);
        Assertions.isTrue("connection mode is single", settings.getMode() == ClusterConnectionMode.SINGLE);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("Cluster created with settings %s", settings.getShortDescription()));
        }
        SingleServerCluster singleServerCluster = this;
        synchronized (singleServerCluster) {
            this.server = this.createServer(settings.getHosts().get(0), new DefaultServerStateListener());
            this.publishDescription(this.server.getDescription());
        }
    }

    @Override
    protected void connect() {
        this.server.connect();
    }

    @Override
    protected ClusterableServer getServer(ServerAddress serverAddress) {
        Assertions.isTrue("open", !this.isClosed());
        return this.server;
    }

    @Override
    public void close() {
        if (!this.isClosed()) {
            this.server.close();
            super.close();
        }
    }

    private void publishDescription(ServerDescription serverDescription) {
        ClusterType clusterType = this.getSettings().getRequiredClusterType();
        if (clusterType == ClusterType.UNKNOWN && serverDescription != null) {
            clusterType = serverDescription.getClusterType();
        }
        ClusterDescription currentDescription = this.getCurrentDescription();
        ClusterDescription description = new ClusterDescription(ClusterConnectionMode.SINGLE, clusterType, serverDescription == null ? Collections.emptyList() : Collections.singletonList(serverDescription), this.getSettings(), this.getServerFactory().getSettings());
        this.updateDescription(description);
        this.fireChangeEvent(new ClusterDescriptionChangedEvent(this.getClusterId(), description, currentDescription));
    }

    private class DefaultServerStateListener
    extends ServerListenerAdapter {
        private DefaultServerStateListener() {
        }

        @Override
        public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
            ServerDescription descriptionToPublish = event.getNewDescription();
            if (event.getNewDescription().isOk()) {
                if (SingleServerCluster.this.getSettings().getRequiredClusterType() != ClusterType.UNKNOWN && SingleServerCluster.this.getSettings().getRequiredClusterType() != event.getNewDescription().getClusterType()) {
                    descriptionToPublish = null;
                } else if (SingleServerCluster.this.getSettings().getRequiredClusterType() == ClusterType.REPLICA_SET && SingleServerCluster.this.getSettings().getRequiredReplicaSetName() != null && !SingleServerCluster.this.getSettings().getRequiredReplicaSetName().equals(event.getNewDescription().getSetName())) {
                    descriptionToPublish = null;
                }
            }
            SingleServerCluster.this.publishDescription(descriptionToPublish);
        }
    }
}

