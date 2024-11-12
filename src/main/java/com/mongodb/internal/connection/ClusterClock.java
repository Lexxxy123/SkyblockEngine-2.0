/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public class ClusterClock {
    private static final String CLUSTER_TIME_KEY = "clusterTime";
    private BsonDocument clusterTime;

    public synchronized BsonDocument getCurrent() {
        return this.clusterTime;
    }

    public synchronized BsonTimestamp getClusterTime() {
        return this.clusterTime != null ? this.clusterTime.getTimestamp(CLUSTER_TIME_KEY) : null;
    }

    public synchronized void advance(BsonDocument other) {
        this.clusterTime = this.greaterOf(other);
    }

    public synchronized BsonDocument greaterOf(BsonDocument other) {
        if (other == null) {
            return this.clusterTime;
        }
        if (this.clusterTime == null) {
            return other;
        }
        return other.getTimestamp(CLUSTER_TIME_KEY).compareTo(this.clusterTime.getTimestamp(CLUSTER_TIME_KEY)) > 0 ? other : this.clusterTime;
    }
}

