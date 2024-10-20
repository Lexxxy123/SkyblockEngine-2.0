/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public class FindOneAndReplaceOptions {
    private Bson projection;
    private Bson sort;
    private boolean upsert;
    private ReturnDocument returnDocument = ReturnDocument.BEFORE;
    private long maxTimeMS;
    private Boolean bypassDocumentValidation;
    private Collation collation;

    @Nullable
    public Bson getProjection() {
        return this.projection;
    }

    public FindOneAndReplaceOptions projection(@Nullable Bson projection) {
        this.projection = projection;
        return this;
    }

    @Nullable
    public Bson getSort() {
        return this.sort;
    }

    public FindOneAndReplaceOptions sort(@Nullable Bson sort) {
        this.sort = sort;
        return this;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public FindOneAndReplaceOptions upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public ReturnDocument getReturnDocument() {
        return this.returnDocument;
    }

    public FindOneAndReplaceOptions returnDocument(ReturnDocument returnDocument) {
        this.returnDocument = Assertions.notNull("returnDocument", returnDocument);
        return this;
    }

    public FindOneAndReplaceOptions maxTime(long maxTime, TimeUnit timeUnit) {
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

    public FindOneAndReplaceOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public FindOneAndReplaceOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public String toString() {
        return "FindOneAndReplaceOptions{projection=" + this.projection + ", sort=" + this.sort + ", upsert=" + this.upsert + ", returnDocument=" + (Object)((Object)this.returnDocument) + ", maxTimeMS=" + this.maxTimeMS + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + '}';
    }
}

