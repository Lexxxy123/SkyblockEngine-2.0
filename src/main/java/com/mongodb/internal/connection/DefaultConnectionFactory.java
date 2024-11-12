/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.Connection;
import com.mongodb.internal.connection.ConnectionFactory;
import com.mongodb.internal.connection.DefaultServerConnection;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.ProtocolExecutor;

class DefaultConnectionFactory
implements ConnectionFactory {
    DefaultConnectionFactory() {
    }

    @Override
    public Connection create(InternalConnection internalConnection, ProtocolExecutor executor, ClusterConnectionMode clusterConnectionMode) {
        return new DefaultServerConnection(internalConnection, executor, clusterConnectionMode);
    }

    @Override
    public AsyncConnection createAsync(InternalConnection internalConnection, ProtocolExecutor executor, ClusterConnectionMode clusterConnectionMode) {
        return new DefaultServerConnection(internalConnection, executor, clusterConnectionMode);
    }
}

