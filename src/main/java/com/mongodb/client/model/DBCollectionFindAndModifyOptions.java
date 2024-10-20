/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class DBCollectionFindAndModifyOptions {
    private DBObject projection;
    private DBObject sort;
    private boolean remove;
    private DBObject update;
    private boolean upsert;
    private boolean returnNew;
    private Boolean bypassDocumentValidation;
    private long maxTimeMS;
    private WriteConcern writeConcern;
    private Collation collation;
    private List<? extends DBObject> arrayFilters;

    @Nullable
    public DBObject getProjection() {
        return this.projection;
    }

    public DBCollectionFindAndModifyOptions projection(@Nullable DBObject projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public DBObject getSort() {
        return this.sort;
    }

    public DBCollectionFindAndModifyOptions sort(@Nullable DBObject sort) {
        this.sort = sort;
        return this;
    }

    public boolean isRemove() {
        return this.remove;
    }

    public DBCollectionFindAndModifyOptions remove(boolean remove) {
        this.remove = remove;
        return this;
    }

    @Nullable
    public DBObject getUpdate() {
        return this.update;
    }

    public DBCollectionFindAndModifyOptions update(@Nullable DBObject update) {
        this.update = update;
        return this;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public DBCollectionFindAndModifyOptions upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public boolean returnNew() {
        return this.returnNew;
    }

    public DBCollectionFindAndModifyOptions returnNew(boolean returnNew) {
        this.returnNew = returnNew;
        return this;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public DBCollectionFindAndModifyOptions bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public DBCollectionFindAndModifyOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime > = 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public DBCollectionFindAndModifyOptions writeConcern(@Nullable WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionFindAndModifyOptions collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public DBCollectionFindAndModifyOptions arrayFilters(List<? extends DBObject> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    public List<? extends DBObject> getArrayFilters() {
        return this.arrayFilters;
    }
}

