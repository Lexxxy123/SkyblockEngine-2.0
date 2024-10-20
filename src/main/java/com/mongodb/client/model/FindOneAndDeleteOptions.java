/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public class FindOneAndDeleteOptions {
    private Bson projection;
    private Bson sort;
    private long maxTimeMS;
    private Collation collation;

    @Nullable
    public Bson getProjection() {
        return this.projection;
    }

    public FindOneAndDeleteOptions projection(@Nullable Bson projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public Bson getSort() {
        return this.sort;
    }

    public FindOneAndDeleteOptions sort(@Nullable Bson sort) {
        this.sort = sort;
        return this;
    }

    public FindOneAndDeleteOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public FindOneAndDeleteOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public String toString() {
        return "FindOneAndDeleteOptions{projection=" + this.projection + ", sort=" + this.sort + ", maxTimeMS=" + this.maxTimeMS + ", collation=" + this.collation + '}';
    }
}

