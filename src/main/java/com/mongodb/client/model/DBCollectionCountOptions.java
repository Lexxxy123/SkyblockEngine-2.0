/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.DBObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;

public class DBCollectionCountOptions {
    private DBObject hint;
    private String hintString;
    private int limit;
    private int skip;
    private long maxTimeMS;
    private ReadPreference readPreference;
    private ReadConcern readConcern;
    private Collation collation;

    @Nullable
    public DBObject getHint() {
        return this.hint;
    }

    @Nullable
    public String getHintString() {
        return this.hintString;
    }

    public DBCollectionCountOptions hint(@Nullable DBObject hint) {
        this.hint = hint;
        return this;
    }

    public DBCollectionCountOptions hintString(@Nullable String hint) {
        this.hintString = hint;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public DBCollectionCountOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getSkip() {
        return this.skip;
    }

    public DBCollectionCountOptions skip(int skip) {
        this.skip = skip;
        return this;
    }

    public DBCollectionCountOptions limit(long limit) {
        Assertions.isTrue("limit is too large", limit <= Integer.MAX_VALUE);
        this.limit = (int)limit;
        return this;
    }

    public DBCollectionCountOptions skip(long skip) {
        Assertions.isTrue("skip is too large", skip <= Integer.MAX_VALUE);
        this.skip = (int)skip;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public DBCollectionCountOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public DBCollectionCountOptions readPreference(@Nullable ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    public DBCollectionCountOptions readConcern(@Nullable ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionCountOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }
}

