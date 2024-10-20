/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

public final class IndexOptionDefaults {
    private Bson storageEngine;

    @Nullable
    public Bson getStorageEngine() {
        return this.storageEngine;
    }

    public IndexOptionDefaults storageEngine(@Nullable Bson storageEngine) {
        this.storageEngine = storageEngine;
        return this;
    }

    public String toString() {
        return "IndexOptionDefaults{storageEngine=" + this.storageEngine + '}';
    }
}

