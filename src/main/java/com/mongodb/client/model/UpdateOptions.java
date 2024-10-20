/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.conversions.Bson;

public class UpdateOptions {
    private boolean upsert;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private List<? extends Bson> arrayFilters;

    public boolean isUpsert() {
        return this.upsert;
    }

    public UpdateOptions upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public UpdateOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public UpdateOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public UpdateOptions arrayFilters(@Nullable List<? extends Bson> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    @Nullable
    public List<? extends Bson> getArrayFilters() {
        return this.arrayFilters;
    }

    public String toString() {
        return "UpdateOptions{upsert=" + this.upsert + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + ", arrayFilters=" + this.arrayFilters + '}';
    }
}

