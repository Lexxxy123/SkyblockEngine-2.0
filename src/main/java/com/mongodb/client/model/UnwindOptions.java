/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.lang.Nullable;

public final class UnwindOptions {
    private Boolean preserveNullAndEmptyArrays;
    private String includeArrayIndex;

    @Nullable
    public Boolean isPreserveNullAndEmptyArrays() {
        return this.preserveNullAndEmptyArrays;
    }

    public UnwindOptions preserveNullAndEmptyArrays(@Nullable Boolean preserveNullAndEmptyArrays) {
        this.preserveNullAndEmptyArrays = preserveNullAndEmptyArrays;
        return this;
    }

    @Nullable
    public String getIncludeArrayIndex() {
        return this.includeArrayIndex;
    }

    public UnwindOptions includeArrayIndex(@Nullable String arrayIndexFieldName) {
        this.includeArrayIndex = arrayIndexFieldName;
        return this;
    }

    public String toString() {
        return "UnwindOptions{preserveNullAndEmptyArrays=" + this.preserveNullAndEmptyArrays + ", includeArrayIndex='" + this.includeArrayIndex + '\'' + '}';
    }
}

