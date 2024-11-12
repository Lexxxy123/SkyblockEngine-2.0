/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.DBObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;

public class DBCollectionDistinctOptions {
    private DBObject filter;
    private ReadPreference readPreference;
    private ReadConcern readConcern;
    private Collation collation;

    @Nullable
    public DBObject getFilter() {
        return this.filter;
    }

    public DBCollectionDistinctOptions filter(@Nullable DBObject filter) {
        this.filter = filter;
        return this;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public DBCollectionDistinctOptions readPreference(@Nullable ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    public DBCollectionDistinctOptions readConcern(@Nullable ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionDistinctOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }
}

