/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCompressor;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.StreamFactory;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ServerListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ClusterableServer;
import com.mongodb.internal.connection.ClusterableServerFactory;
import com.mongodb.internal.connection.DefaultConnectionFactory;
import com.mongodb.internal.connection.DefaultConnectionPool;
import com.mongodb.internal.connection.DefaultServer;
import com.mongodb.internal.connection.DefaultServerMonitorFactory;
import com.mongodb.internal.connection.InternalStreamConnectionFactory;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import java.util.Collections;
import java.util.List;

public class DefaultClusterableServerFactory
implements ClusterableServerFactory {
    private final ClusterId clusterId;
    private final ClusterSettings clusterSettings;
    private final ServerSettings serverSettings;
    private final ConnectionPoolSettings connectionPoolSettings;
    private final StreamFactory streamFactory;
    private final List<MongoCredentialWithCache> credentialList;
    private final StreamFactory heartbeatStreamFactory;
    private final CommandListener commandListener;
    private final String applicationName;
    private final MongoDriverInformation mongoDriverInformation;
    private final List<MongoCompressor> compressorList;

    public DefaultClusterableServerFactory(ClusterId clusterId, ClusterSettings clusterSettings, ServerSettings serverSettings, ConnectionPoolSettings connectionPoolSettings, StreamFactory streamFactory, StreamFactory heartbeatStreamFactory, List<MongoCredential> credentialList, CommandListener commandListener, String applicationName, MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList) {
        this.clusterId = clusterId;
        this.clusterSettings = clusterSettings;
        this.serverSettings = serverSettings;
        this.connectionPoolSettings = connectionPoolSettings;
        this.streamFactory = streamFactory;
        this.credentialList = MongoCredentialWithCache.wrapCredentialList(credentialList);
        this.heartbeatStreamFactory = heartbeatStreamFactory;
        this.commandListener = commandListener;
        this.applicationName = applicationName;
        this.mongoDriverInformation = mongoDriverInformation;
        this.compressorList = compressorList;
    }

    @Override
    public ClusterableServer create(ServerAddress serverAddress, ServerListener serverListener, ClusterClock clusterClock) {
        DefaultConnectionPool connectionPool = new DefaultConnectionPool(new ServerId(this.clusterId, serverAddress), new InternalStreamConnectionFactory(this.streamFactory, this.credentialList, this.applicationName, this.mongoDriverInformation, this.compressorList, this.commandListener), this.connectionPoolSettings);
        connectionPool.start();
        DefaultServerMonitorFactory serverMonitorFactory = new DefaultServerMonitorFactory(new ServerId(this.clusterId, serverAddress), this.serverSettings, clusterClock, new InternalStreamConnectionFactory(this.heartbeatStreamFactory, Collections.<MongoCredentialWithCache>emptyList(), this.applicationName, this.mongoDriverInformation, Collections.<MongoCompressor>emptyList(), null), connectionPool);
        return new DefaultServer(new ServerId(this.clusterId, serverAddress), this.clusterSettings.getMode(), connectionPool, new DefaultConnectionFactory(), serverMonitorFactory, serverListener, this.commandListener, clusterClock);
    }

    @Override
    public ServerSettings getSettings() {
        return this.serverSettings;
    }
}

