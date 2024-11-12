/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.binding;

import com.mongodb.ServerAddress;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadWriteBinding;
import com.mongodb.connection.Cluster;

public interface ClusterAwareReadWriteBinding
extends ReadWriteBinding {
    public Cluster getCluster();

    public ConnectionSource getConnectionSource(ServerAddress var1);
}

