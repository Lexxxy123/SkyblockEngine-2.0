/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoCompressor;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterFactory;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.StreamFactory;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionListener;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.internal.connection.DefaultClusterableServerFactory;
import com.mongodb.internal.connection.DefaultDnsSrvRecordMonitorFactory;
import com.mongodb.internal.connection.DnsMultiServerCluster;
import com.mongodb.internal.connection.MultiServerCluster;
import com.mongodb.internal.connection.SingleServerCluster;
import java.util.Collections;
import java.util.List;

@Deprecated
public final class DefaultClusterFactory
implements ClusterFactory {
    @Override
    public Cluster create(ClusterSettings settings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, ClusterListener clusterListener, ConnectionPoolListener connectionPoolListener, ConnectionListener connectionListener) {
        return this.createCluster(this.getClusterSettings(settings, clusterListener), serverSettings, this.getConnectionPoolSettings(connectionPoolSettings, connectionPoolListener), streamFactory, heartbeatStreamFactory, credentialList, null, null, null, Collections.<MongoCompressor>emptyList());
    }

    @Deprecated
    public Cluster create(ClusterSettings settings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, ClusterListener clusterListener, ConnectionPoolListener connectionPoolListener, ConnectionListener connectionListener, CommandListener commandListener) {
        return this.createCluster(this.getClusterSettings(settings, clusterListener), serverSettings, this.getConnectionPoolSettings(connectionPoolSettings, connectionPoolListener), streamFactory, heartbeatStreamFactory, credentialList, commandListener, null, null, Collections.<MongoCompressor>emptyList());
    }

    @Deprecated
    public Cluster create(ClusterSettings settings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, ClusterListener clusterListener, ConnectionPoolListener connectionPoolListener, ConnectionListener connectionListener, CommandListener commandListener, String applicationName, MongoDriverInformation mongoDriverInformation) {
        return this.createCluster(this.getClusterSettings(settings, clusterListener), serverSettings, this.getConnectionPoolSettings(connectionPoolSettings, connectionPoolListener), streamFactory, heartbeatStreamFactory, credentialList, commandListener, applicationName, mongoDriverInformation, Collections.<MongoCompressor>emptyList());
    }

    @Deprecated
    public Cluster createCluster(ClusterSettings clusterSettings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, CommandListener commandListener, String applicationName, MongoDriverInformation mongoDriverInformation) {
        return this.createCluster(clusterSettings, serverSettings, connectionPoolSettings, streamFactory, heartbeatStreamFactory, credentialList, commandListener, applicationName, mongoDriverInformation, Collections.<MongoCompressor>emptyList());
    }

    public Cluster createCluster(ClusterSettings clusterSettings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, CommandListener commandListener, String applicationName, MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList) {
        ClusterId clusterId = new ClusterId(clusterSettings.getDescription());
        DefaultClusterableServerFactory serverFactory = new DefaultClusterableServerFactory(clusterId, clusterSettings, serverSettings, connectionPoolSettings, streamFactory, heartbeatStreamFactory, credentialList, commandListener, applicationName, mongoDriverInformation != null ? mongoDriverInformation : MongoDriverInformation.builder().build(), compressorList);
        DefaultDnsSrvRecordMonitorFactory dnsSrvRecordMonitorFactory = new DefaultDnsSrvRecordMonitorFactory(clusterId, serverSettings);
        if (clusterSettings.getMode() == ClusterConnectionMode.SINGLE) {
            return new SingleServerCluster(clusterId, clusterSettings, serverFactory);
        }
        if (clusterSettings.getMode() == ClusterConnectionMode.MULTIPLE) {
            if (clusterSettings.getSrvHost() == null) {
                return new MultiServerCluster(clusterId, clusterSettings, serverFactory);
            }
            return new DnsMultiServerCluster(clusterId, clusterSettings, serverFactory, dnsSrvRecordMonitorFactory);
        }
        throw new UnsupportedOperationException("Unsupported cluster mode: " + (Object)((Object)clusterSettings.getMode()));
    }

    private ClusterSettings getClusterSettings(ClusterSettings settings, ClusterListener clusterListener) {
        return ClusterSettings.builder(settings).addClusterListener(clusterListener).build();
    }

    private ConnectionPoolSettings getConnectionPoolSettings(ConnectionPoolSettings connPoolSettings, ConnectionPoolListener connPoolListener) {
        return ConnectionPoolSettings.builder(connPoolSettings).addConnectionPoolListener(connPoolListener).build();
    }
}

