/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;

public class CreateViewOptions {
    private Collation collation;

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public CreateViewOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public String toString() {
        return "CreateViewOptions{collation=" + this.collation + '}';
    }
}

