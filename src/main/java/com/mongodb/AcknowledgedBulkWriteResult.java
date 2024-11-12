/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BulkWriteResult;
import com.mongodb.BulkWriteUpsert;
import com.mongodb.assertions.Assertions;
import java.util.Collections;
import java.util.List;

class AcknowledgedBulkWriteResult
extends BulkWriteResult {
    private int insertedCount;
    private int matchedCount;
    private int removedCount;
    private int modifiedCount;
    private final List<BulkWriteUpsert> upserts;

    AcknowledgedBulkWriteResult(int insertedCount, int matchedCount, int removedCount, Integer modifiedCount, List<BulkWriteUpsert> upserts) {
        this.insertedCount = insertedCount;
        this.matchedCount = matchedCount;
        this.removedCount = removedCount;
        this.modifiedCount = Assertions.notNull("modifiedCount", modifiedCount);
        this.upserts = Collections.unmodifiableList(Assertions.notNull("upserts", upserts));
    }

    @Override
    public boolean isAcknowledged() {
        return true;
    }

    @Override
    public int getInsertedCount() {
        return this.insertedCount;
    }

    @Override
    public int getMatchedCount() {
        return this.matchedCount;
    }

    @Override
    public int getRemovedCount() {
        return this.removedCount;
    }

    @Override
    @Deprecated
    public boolean isModifiedCountAvailable() {
        return true;
    }

    @Override
    public int getModifiedCount() {
        return this.modifiedCount;
    }

    @Override
    public List<BulkWriteUpsert> getUpserts() {
        return this.upserts;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        AcknowledgedBulkWriteResult that = (AcknowledgedBulkWriteResult)o2;
        if (this.insertedCount != that.insertedCount) {
            return false;
        }
        if (this.matchedCount != that.matchedCount) {
            return false;
        }
        if (this.removedCount != that.removedCount) {
            return false;
        }
        if (this.modifiedCount != that.modifiedCount) {
            return false;
        }
        return this.upserts.equals(that.upserts);
    }

    public int hashCode() {
        int result = this.insertedCount;
        result = 31 * result + this.matchedCount;
        result = 31 * result + this.removedCount;
        result = 31 * result + this.modifiedCount;
        result = 31 * result + this.upserts.hashCode();
        return result;
    }

    public String toString() {
        return "AcknowledgedBulkWriteResult{insertedCount=" + this.insertedCount + ", matchedCount=" + this.matchedCount + ", removedCount=" + this.removedCount + ", modifiedCount=" + this.modifiedCount + ", upserts=" + this.upserts + '}';
    }
}

