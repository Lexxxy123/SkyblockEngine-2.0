/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.lang.Nullable;

public final class InsertOneOptions {
    private Boolean bypassDocumentValidation;

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public InsertOneOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public String toString() {
        return "InsertOneOptions{bypassDocumentValidation=" + this.bypassDocumentValidation + '}';
    }
}

