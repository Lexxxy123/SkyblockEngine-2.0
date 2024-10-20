/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.binding;

import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadWriteBinding;
import com.mongodb.connection.Cluster;

public interface AsyncClusterAwareReadWriteBinding
extends AsyncReadWriteBinding {
    public Cluster getCluster();

    public void getConnectionSource(ServerAddress var1, SingleResultCallback<AsyncConnectionSource> var2);
}

