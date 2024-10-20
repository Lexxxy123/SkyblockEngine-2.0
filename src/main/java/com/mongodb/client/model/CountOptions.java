/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public class CountOptions {
    private Bson hint;
    private String hintString;
    private int limit;
    private int skip;
    private long maxTimeMS;
    private Collation collation;

    @Nullable
    public Bson getHint() {
        return this.hint;
    }

    @Nullable
    public String getHintString() {
        return this.hintString;
    }

    public CountOptions hint(@Nullable Bson hint) {
        this.hint = hint;
        return this;
    }

    public CountOptions hintString(@Nullable String hint) {
        this.hintString = hint;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public CountOptions limit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getSkip() {
        return this.skip;
    }

    public CountOptions skip(int skip) {
        this.skip = skip;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public CountOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public CountOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public String toString() {
        return "CountOptions{hint=" + this.hint + ", hintString='" + this.hintString + '\'' + ", limit=" + this.limit + ", skip=" + this.skip + ", maxTimeMS=" + this.maxTimeMS + ", collation=" + this.collation + '}';
    }
}

