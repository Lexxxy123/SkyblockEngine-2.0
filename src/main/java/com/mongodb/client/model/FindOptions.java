/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.CursorType;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

@Deprecated
public final class FindOptions {
    private int batchSize;
    private int limit;
    private Bson modifiers;
    private Bson projection;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private int skip;
    private Bson sort;
    private CursorType cursorType = CursorType.NonTailable;
    private boolean noCursorTimeout;
    private boolean oplogReplay;
    private boolean partial;
    private Collation collation;
    private String comment;
    private Bson hint;
    private String hintString;
    private Bson max;
    private Bson min;
    private long maxScan;
    private boolean returnKey;
    private boolean showRecordId;
    private boolean snapshot;

    public FindOptions() {
    }

    @Deprecated
    public FindOptions(FindOptions from) {
        this.batchSize = from.batchSize;
        this.limit = from.limit;
        this.modifiers = from.modifiers;
        this.projection = from.projection;
        this.maxTimeMS = from.maxTimeMS;
        this.maxAwaitTimeMS = from.maxAwaitTimeMS;
        this.skip = from.skip;
        this.sort = from.sort;
        this.cursorType = from.cursorType;
        this.noCursorTimeout = from.noCursorTimeout;
        this.oplogReplay = from.oplogReplay;
        this.partial = from.partial;
        this.comment = from.comment;
        this.hint = from.hint;
        this.hintString = from.hintString;
        this.max = from.max;
        this.min = from.min;
        this.maxScan = from.maxScan;
        this.returnKey = from.returnKey;
        this.showRecordId = from.showRecordId;
        this.snapshot = from.snapshot;
    }

    public int getLimit() {
        return this.limit;
    }

    public FindOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getSkip() {
        return this.skip;
    }

    public FindOptions skip(int skip) {
        this.skip = skip;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime > = 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxAwaitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindOptions maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime > = 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public FindOptions batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Deprecated
    @Nullable
    public Bson getModifiers() {
        return this.modifiers;
    }

    @Deprecated
    public FindOptions modifiers(@Nullable Bson modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    @Nullable
    public Bson getProjection() {
        return this.projection;
    }

    public FindOptions projection(@Nullable Bson projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public Bson getSort() {
        return this.sort;
    }

    public FindOptions sort(@Nullable Bson sort) {
        this.sort = sort;
        return this;
    }

    public boolean isNoCursorTimeout() {
        return this.noCursorTimeout;
    }

    public FindOptions noCursorTimeout(boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }

    public boolean isOplogReplay() {
        return this.oplogReplay;
    }

    public FindOptions oplogReplay(boolean oplogReplay) {
        this.oplogReplay = oplogReplay;
        return this;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public FindOptions partial(boolean partial) {
        this.partial = partial;
        return this;
    }

    public CursorType getCursorType() {
        return this.cursorType;
    }

    public FindOptions cursorType(CursorType cursorType) {
        this.cursorType = Assertions.notNull("cursorType", cursorType);
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public FindOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    public FindOptions comment(@Nullable String comment) {
        this.comment = comment;
        return this;
    }

    @Nullable
    public Bson getHint() {
        return this.hint;
    }

    public FindOptions hint(@Nullable Bson hint) {
        this.hint = hint;
        return this;
    }

    @Nullable
    public String getHintString() {
        return this.hintString;
    }

    public FindOptions hintString(@Nullable String hint) {
        this.hintString = hint;
        return this;
    }

    @Nullable
    public Bson getMax() {
        return this.max;
    }

    public FindOptions max(@Nullable Bson max) {
        this.max = max;
        return this;
    }

    @Nullable
    public Bson getMin() {
        return this.min;
    }

    public FindOptions min(@Nullable Bson min) {
        this.min = min;
        return this;
    }

    @Deprecated
    public long getMaxScan() {
        return this.maxScan;
    }

    @Deprecated
    public FindOptions maxScan(long maxScan) {
        this.maxScan = maxScan;
        return this;
    }

    public boolean isReturnKey() {
        return this.returnKey;
    }

    public FindOptions returnKey(boolean returnKey) {
        this.returnKey = returnKey;
        return this;
    }

    public boolean isShowRecordId() {
        return this.showRecordId;
    }

    public FindOptions showRecordId(boolean showRecordId) {
        this.showRecordId = showRecordId;
        return this;
    }

    @Deprecated
    public boolean isSnapshot() {
        return this.snapshot;
    }

    @Deprecated
    public FindOptions snapshot(boolean snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public String toString() {
        return "FindOptions{batchSize=" + this.batchSize + ", limit=" + this.limit + ", modifiers=" + this.modifiers + ", projection=" + this.projection + ", maxTimeMS=" + this.maxTimeMS + ", maxAwaitTimeMS=" + this.maxAwaitTimeMS + ", skip=" + this.skip + ", sort=" + this.sort + ", cursorType=" + (Object)((Object)this.cursorType) + ", noCursorTimeout=" + this.noCursorTimeout + ", oplogReplay=" + this.oplogReplay + ", partial=" + this.partial + ", collation=" + this.collation + ", comment='" + this.comment + "', hint=" + this.hint + ", max=" + this.max + ", min=" + this.min + ", maxScan=" + this.maxScan + ", returnKey=" + this.returnKey + ", showRecordId=" + this.showRecordId + ", snapshot=" + this.snapshot + "}";
    }
}

