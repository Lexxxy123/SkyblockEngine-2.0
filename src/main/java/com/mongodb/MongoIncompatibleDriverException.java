/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;
import com.mongodb.connection.ClusterDescription;

public class MongoIncompatibleDriverException
extends MongoException {
    private static final long serialVersionUID = -5213381354402601890L;
    private ClusterDescription clusterDescription;

    public MongoIncompatibleDriverException(String message, ClusterDescription clusterDescription) {
        super(message);
        this.clusterDescription = clusterDescription;
    }

    public ClusterDescription getClusterDescription() {
        return this.clusterDescription;
    }
}

