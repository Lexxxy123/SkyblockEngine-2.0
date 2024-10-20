/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.BasicDBObject;
import com.mongodb.CursorType;
import com.mongodb.DBObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;

public final class DBCollectionFindOptions {
    private int batchSize;
    private int limit;
    private DBObject modifiers = new BasicDBObject();
    private DBObject projection;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private int skip;
    private DBObject sort;
    private CursorType cursorType = CursorType.NonTailable;
    private boolean noCursorTimeout;
    private boolean oplogReplay;
    private boolean partial;
    private ReadPreference readPreference;
    private ReadConcern readConcern;
    private Collation collation;
    private String comment;
    private DBObject hint;
    private DBObject max;
    private DBObject min;
    private boolean returnKey;
    private boolean showRecordId;

    public DBCollectionFindOptions copy() {
        DBCollectionFindOptions copiedOptions = new DBCollectionFindOptions();
        copiedOptions.batchSize(this.batchSize);
        copiedOptions.limit(this.limit);
        copiedOptions.modifiers(this.modifiers);
        copiedOptions.projection(this.projection);
        copiedOptions.maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
        copiedOptions.maxAwaitTime(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
        copiedOptions.skip(this.skip);
        copiedOptions.sort(this.sort);
        copiedOptions.cursorType(this.cursorType);
        copiedOptions.noCursorTimeout(this.noCursorTimeout);
        copiedOptions.oplogReplay(this.oplogReplay);
        copiedOptions.partial(this.partial);
        copiedOptions.readPreference(this.readPreference);
        copiedOptions.readConcern(this.readConcern);
        copiedOptions.collation(this.collation);
        copiedOptions.comment(this.comment);
        copiedOptions.hint(this.hint);
        copiedOptions.max(this.max);
        copiedOptions.min(this.min);
        copiedOptions.returnKey(this.returnKey);
        copiedOptions.showRecordId(this.showRecordId);
        return copiedOptions;
    }

    public int getLimit() {
        return this.limit;
    }

    public DBCollectionFindOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getSkip() {
        return this.skip;
    }

    public DBCollectionFindOptions skip(int skip) {
        this.skip = skip;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public DBCollectionFindOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime > = 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxAwaitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }

    public DBCollectionFindOptions maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime > = 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public DBCollectionFindOptions batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Deprecated
    public DBObject getModifiers() {
        return this.modifiers;
    }

    @Deprecated
    public DBCollectionFindOptions modifiers(@Nullable DBObject modifiers) {
        this.modifiers = Assertions.notNull("modifiers", modifiers);
        return this;
    }

    @Nullable
    public DBObject getProjection() {
        return this.projection;
    }

    public DBCollectionFindOptions projection(@Nullable DBObject projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public DBObject getSort() {
        return this.sort;
    }

    public DBCollectionFindOptions sort(@Nullable DBObject sort) {
        this.sort = sort;
        return this;
    }

    public boolean isNoCursorTimeout() {
        return this.noCursorTimeout;
    }

    public DBCollectionFindOptions noCursorTimeout(boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }

    public boolean isOplogReplay() {
        return this.oplogReplay;
    }

    public DBCollectionFindOptions oplogReplay(boolean oplogReplay) {
        this.oplogReplay = oplogReplay;
        return this;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public DBCollectionFindOptions partial(boolean partial) {
        this.partial = partial;
        return this;
    }

    public CursorType getCursorType() {
        return this.cursorType;
    }

    public DBCollectionFindOptions cursorType(CursorType cursorType) {
        this.cursorType = Assertions.notNull("cursorType", cursorType);
        return this;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public DBCollectionFindOptions readPreference(@Nullable ReadPreference readPreference) {
        this.readPreference = readPreference;
        return this;
    }

    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    public DBCollectionFindOptions readConcern(@Nullable ReadConcern readConcern) {
        this.readConcern = readConcern;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public DBCollectionFindOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    public DBCollectionFindOptions comment(@Nullable String comment) {
        this.comment = comment;
        return this;
    }

    @Nullable
    public DBObject getHint() {
        return this.hint;
    }

    public DBCollectionFindOptions hint(@Nullable DBObject hint) {
        this.hint = hint;
        return this;
    }

    @Nullable
    public DBObject getMax() {
        return this.max;
    }

    public DBCollectionFindOptions max(@Nullable DBObject max) {
        this.max = max;
        return this;
    }

    @Nullable
    public DBObject getMin() {
        return this.min;
    }

    public DBCollectionFindOptions min(@Nullable DBObject min) {
        this.min = min;
        return this;
    }

    public boolean isReturnKey() {
        return this.returnKey;
    }

    public DBCollectionFindOptions returnKey(boolean returnKey) {
        this.returnKey = returnKey;
        return this;
    }

    public boolean isShowRecordId() {
        return this.showRecordId;
    }

    public DBCollectionFindOptions showRecordId(boolean showRecordId) {
        this.showRecordId = showRecordId;
        return this;
    }
}

