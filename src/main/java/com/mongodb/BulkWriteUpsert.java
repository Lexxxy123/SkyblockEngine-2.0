/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

public class BulkWriteUpsert {
    private final int index;
    private final Object id;

    public BulkWriteUpsert(int index, Object id) {
        this.index = index;
        this.id = id;
    }

    public int getIndex() {
        return this.index;
    }

    public Object getId() {
        return this.id;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        BulkWriteUpsert that = (BulkWriteUpsert)o2;
        if (this.index != that.index) {
            return false;
        }
        return this.id.equals(that.id);
    }

    public int hashCode() {
        int result = this.index;
        result = 31 * result + this.id.hashCode();
        return result;
    }

    public String toString() {
        return "BulkWriteUpsert{index=" + this.index + ", id=" + this.id + '}';
    }
}

