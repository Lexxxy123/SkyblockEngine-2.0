/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.IndexOptions;
import org.bson.conversions.Bson;

public class IndexModel {
    private final Bson keys;
    private final IndexOptions options;

    public IndexModel(Bson keys) {
        this(keys, new IndexOptions());
    }

    public IndexModel(Bson keys, IndexOptions options) {
        this.keys = Assertions.notNull("keys", keys);
        this.options = Assertions.notNull("options", options);
    }

    public Bson getKeys() {
        return this.keys;
    }

    public IndexOptions getOptions() {
        return this.options;
    }

    public String toString() {
        return "IndexModel{keys=" + this.keys + ", options=" + this.options + '}';
    }
}

