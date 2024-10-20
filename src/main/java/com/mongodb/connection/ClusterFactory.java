/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoCredential;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.StreamFactory;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ConnectionListener;
import com.mongodb.event.ConnectionPoolListener;
import java.util.List;

@Deprecated
public interface ClusterFactory {
    public Cluster create(ClusterSettings var1, ServerSettings var2, ConnectionPoolSettings var3, StreamFactory var4, StreamFactory var5, List<MongoCredential> var6, ClusterListener var7, ConnectionPoolListener var8, ConnectionListener var9);
}

