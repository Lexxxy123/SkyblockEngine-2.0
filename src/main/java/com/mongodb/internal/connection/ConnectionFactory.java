/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.Connection;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.ProtocolExecutor;

interface ConnectionFactory {
    public Connection create(InternalConnection var1, ProtocolExecutor var2, ClusterConnectionMode var3);

    public AsyncConnection createAsync(InternalConnection var1, ProtocolExecutor var2, ClusterConnectionMode var3);
}

