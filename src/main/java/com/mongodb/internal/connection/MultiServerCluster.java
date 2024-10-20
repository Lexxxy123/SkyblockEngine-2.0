/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.internal.connection.AbstractMultiServerCluster;
import com.mongodb.internal.connection.ClusterableServerFactory;

public final class MultiServerCluster
extends AbstractMultiServerCluster {
    public MultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory) {
        super(clusterId, settings, serverFactory);
        Assertions.isTrue("srvHost is null", settings.getSrvHost() == null);
        this.initialize(settings.getHosts());
    }
}

