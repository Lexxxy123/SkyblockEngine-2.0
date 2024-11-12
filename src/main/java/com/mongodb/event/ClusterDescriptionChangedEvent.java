/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterId;

public final class ClusterDescriptionChangedEvent {
    private final ClusterId clusterId;
    private final ClusterDescription newDescription;
    private final ClusterDescription previousDescription;

    public ClusterDescriptionChangedEvent(ClusterId clusterId, ClusterDescription newDescription, ClusterDescription previousDescription) {
        this.clusterId = Assertions.notNull("clusterId", clusterId);
        this.newDescription = Assertions.notNull("newDescription", newDescription);
        this.previousDescription = Assertions.notNull("previousDescription", previousDescription);
    }

    public ClusterId getClusterId() {
        return this.clusterId;
    }

    public ClusterDescription getNewDescription() {
        return this.newDescription;
    }

    public ClusterDescription getPreviousDescription() {
        return this.previousDescription;
    }

    public String toString() {
        return "ClusterDescriptionChangedEvent{clusterId=" + this.clusterId + ", newDescription=" + this.newDescription + ", previousDescription=" + this.previousDescription + '}';
    }
}

