/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.connection.ServerSettings;
import com.mongodb.event.ServerListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ClusterableServer;

public interface ClusterableServerFactory {
    public ClusterableServer create(ServerAddress var1, ServerListener var2, ClusterClock var3);

    public ServerSettings getSettings();
}

