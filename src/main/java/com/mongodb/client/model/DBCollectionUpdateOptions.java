/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.DBEncoder;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.List;

public class DBCollectionUpdateOptions {
    private boolean upsert;
    private Boolean bypassDocumentValidation;
    private boolean multi;
    private Collation collation;
    private List<? extends DBObject> arrayFilters;
    private WriteConcern writeConcern;
    private DBEncoder encoder;

    public boolean isUpsert() {
        return this.upsert;
    }

    public DBCollectionUpdateOptions upsert(boolean isUpsert) {
        this.upsert = isUpsert;
        return this;
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public DBCollectionUpdateOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public DBCollectionUpdateOptions multi(boolean multi) {
        this.multi = multi;
        return this;
    }

    public boolean isMulti() {
        return this.multi;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionUpdateOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public DBCollectionUpdateOptions arrayFilters(@Nullable List<? extends DBObject> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    @Nullable
    public List<? extends DBObject> getArrayFilters() {
        return this.arrayFilters;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public DBCollectionUpdateOptions writeConcern(@Nullable WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    @Nullable
    public DBEncoder getEncoder() {
        return this.encoder;
    }

    public DBCollectionUpdateOptions encoder(@Nullable DBEncoder encoder) {
        this.encoder = encoder;
        return this;
    }
}

