/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.lang.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public class FindOneAndUpdateOptions {
    private Bson projection;
    private Bson sort;
    private boolean upsert;
    private ReturnDocument returnDocument = ReturnDocument.BEFORE;
    private long maxTimeMS;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private List<? extends Bson> arrayFilters;

    @Nullable
    public Bson getProjection() {
        return this.projection;
    }

    public FindOneAndUpdateOptions projection(@Nullable Bson projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public Bson getSort() {
        return this.sort;
    }

    public FindOneAndUpdateOptions sort(@Nullable Bson sort) {
        this.sort = sort;
        return this;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public FindOneAndUpdateOptions upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public ReturnDocument getReturnDocument() {
        return this.returnDocument;
    }

    public FindOneAndUpdateOptions returnDocument(ReturnDocument returnDocument) {
        this.returnDocument = Assertions.notNull("returnDocument", returnDocument);
        return this;
    }

    public FindOneAndUpdateOptions maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public FindOneAndUpdateOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public FindOneAndUpdateOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public FindOneAndUpdateOptions arrayFilters(@Nullable List<? extends Bson> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    @Nullable
    public List<? extends Bson> getArrayFilters() {
        return this.arrayFilters;
    }

    public String toString() {
        return "FindOneAndUpdateOptions{projection=" + this.projection + ", sort=" + this.sort + ", upsert=" + this.upsert + ", returnDocument=" + (Object)((Object)this.returnDocument) + ", maxTimeMS=" + this.maxTimeMS + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + ", arrayFilters=" + this.arrayFilters + '}';
    }
}

