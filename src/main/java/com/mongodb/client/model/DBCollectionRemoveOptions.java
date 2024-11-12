/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.DBEncoder;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;

public final class DBCollectionRemoveOptions {
    private Collation collation;
    private WriteConcern writeConcern;
    private DBEncoder encoder;

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionRemoveOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public DBCollectionRemoveOptions writeConcern(@Nullable WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Nullable
    public DBEncoder getEncoder() {
        return this.encoder;
    }

    public DBCollectionRemoveOptions encoder(@Nullable DBEncoder encoder) {
        this.encoder = encoder;
        return this;
    }
}

