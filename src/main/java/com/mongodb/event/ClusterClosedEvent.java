/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterId;

public final class ClusterClosedEvent {
    private final ClusterId clusterId;

    public ClusterClosedEvent(ClusterId clusterId) {
        this.clusterId = Assertions.notNull("clusterId", clusterId);
    }

    public ClusterId getClusterId() {
        return this.clusterId;
    }

    public String toString() {
        return "ClusterClosedEvent{clusterId=" + this.clusterId + '}';
    }
}

