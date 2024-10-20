/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

public class PushOptions {
    private Integer position;
    private Integer slice;
    private Integer sort;
    private Bson sortDocument;

    @Nullable
    public Integer getPosition() {
        return this.position;
    }

    public PushOptions position(@Nullable Integer position) {
        this.position = position;
        return this;
    }

    @Nullable
    public Integer getSlice() {
        return this.slice;
    }

    public PushOptions slice(@Nullable Integer slice) {
        this.slice = slice;
        return this;
    }

    @Nullable
    public Integer getSort() {
        return this.sort;
    }

    public PushOptions sort(@Nullable Integer sort) {
        if (this.sortDocument != null) {
            throw new IllegalStateException("sort can not be set if sortDocument already is");
        }
        this.sort = sort;
        return this;
    }

    @Nullable
    public Bson getSortDocument() {
        return this.sortDocument;
    }

    public PushOptions sortDocument(@Nullable Bson sortDocument) {
        if (this.sort != null) {
            throw new IllegalStateException("sortDocument can not be set if sort already is");
        }
        this.sortDocument = sortDocument;
        return this;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        PushOptions that = (PushOptions)o2;
        if (this.position != null ? !this.position.equals(that.position) : that.position != null) {
            return false;
        }
        if (this.slice != null ? !this.slice.equals(that.slice) : that.slice != null) {
            return false;
        }
        if (this.sort != null ? !this.sort.equals(that.sort) : that.sort != null) {
            return false;
        }
        return this.sortDocument != null ? this.sortDocument.equals(that.sortDocument) : that.sortDocument == null;
    }

    public int hashCode() {
        int result = this.position != null ? this.position.hashCode() : 0;
        result = 31 * result + (this.slice != null ? this.slice.hashCode() : 0);
        result = 31 * result + (this.sort != null ? this.sort.hashCode() : 0);
        result = 31 * result + (this.sortDocument != null ? this.sortDocument.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Push Options{position=" + this.position + ", slice=" + this.slice + (this.sort == null ? "" : ", sort=" + this.sort) + (this.sortDocument == null ? "" : ", sortDocument=" + this.sortDocument) + '}';
    }
}

