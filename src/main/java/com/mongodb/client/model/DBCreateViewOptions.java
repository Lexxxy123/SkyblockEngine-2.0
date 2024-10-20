/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.Collation;

public class DBCreateViewOptions {
    private Collation collation;

    public Collation getCollation() {
        return this.collation;
    }

    public DBCreateViewOptions collation(Collation collation) {
        this.collation = collation;
        return this;
    }
}

