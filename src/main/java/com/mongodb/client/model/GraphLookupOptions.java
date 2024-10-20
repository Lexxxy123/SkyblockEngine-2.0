/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

public final class GraphLookupOptions {
    private Integer maxDepth;
    private String depthField;
    private Bson restrictSearchWithMatch;

    public GraphLookupOptions depthField(@Nullable String field) {
        this.depthField = field;
        return this;
    }

    @Nullable
    public String getDepthField() {
        return this.depthField;
    }

    public GraphLookupOptions maxDepth(@Nullable Integer max) {
        this.maxDepth = max;
        return this;
    }

    @Nullable
    public Integer getMaxDepth() {
        return this.maxDepth;
    }

    public GraphLookupOptions restrictSearchWithMatch(@Nullable Bson filter) {
        this.restrictSearchWithMatch = filter;
        return this;
    }

    @Nullable
    public Bson getRestrictSearchWithMatch() {
        return this.restrictSearchWithMatch;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        GraphLookupOptions that = (GraphLookupOptions)o2;
        if (this.maxDepth != null ? !this.maxDepth.equals(that.maxDepth) : that.maxDepth != null) {
            return false;
        }
        if (this.depthField != null ? !this.depthField.equals(that.depthField) : that.depthField != null) {
            return false;
        }
        return this.restrictSearchWithMatch != null ? this.restrictSearchWithMatch.equals(that.restrictSearchWithMatch) : that.restrictSearchWithMatch == null;
    }

    public int hashCode() {
        int result = this.maxDepth != null ? this.maxDepth.hashCode() : 0;
        result = 31 * result + (this.depthField != null ? this.depthField.hashCode() : 0);
        result = 31 * result + (this.restrictSearchWithMatch != null ? this.restrictSearchWithMatch.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("GraphLookupOptions{");
        if (this.depthField != null) {
            stringBuilder.append("depthField='").append(this.depthField).append('\'');
            if (this.maxDepth != null) {
                stringBuilder.append(", ");
            }
        }
        if (this.maxDepth != null) {
            stringBuilder.append("maxDepth=").append(this.maxDepth);
            if (this.restrictSearchWithMatch != null) {
                stringBuilder.append(", ");
            }
        }
        if (this.restrictSearchWithMatch != null) {
            stringBuilder.append("restrictSearchWithMatch=").append(this.restrictSearchWithMatch);
        }
        return stringBuilder.append('}').toString();
    }
}

