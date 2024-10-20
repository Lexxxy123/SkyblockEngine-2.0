/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.ServerSettings;
import com.mongodb.internal.connection.ChangeListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ConnectionPool;
import com.mongodb.internal.connection.DefaultServerMonitor;
import com.mongodb.internal.connection.InternalConnectionFactory;
import com.mongodb.internal.connection.ServerMonitor;
import com.mongodb.internal.connection.ServerMonitorFactory;

class DefaultServerMonitorFactory
implements ServerMonitorFactory {
    private final ServerId serverId;
    private final ServerSettings settings;
    private final ClusterClock clusterClock;
    private final InternalConnectionFactory internalConnectionFactory;
    private final ConnectionPool connectionPool;

    DefaultServerMonitorFactory(ServerId serverId, ServerSettings settings, ClusterClock clusterClock, InternalConnectionFactory internalConnectionFactory, ConnectionPool connectionPool) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.settings = Assertions.notNull("settings", settings);
        this.clusterClock = Assertions.notNull("clusterClock", clusterClock);
        this.internalConnectionFactory = Assertions.notNull("internalConnectionFactory", internalConnectionFactory);
        this.connectionPool = Assertions.notNull("connectionPool", connectionPool);
    }

    @Override
    public ServerMonitor create(ChangeListener<ServerDescription> serverStateListener) {
        return new DefaultServerMonitor(this.serverId, this.settings, this.clusterClock, serverStateListener, this.internalConnectionFactory, this.connectionPool);
    }
}

