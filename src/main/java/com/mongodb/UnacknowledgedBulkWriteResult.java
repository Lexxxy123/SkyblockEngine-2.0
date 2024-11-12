/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BulkWriteResult;
import com.mongodb.BulkWriteUpsert;
import java.util.List;

class UnacknowledgedBulkWriteResult
extends BulkWriteResult {
    UnacknowledgedBulkWriteResult() {
    }

    @Override
    public boolean isAcknowledged() {
        return false;
    }

    @Override
    public int getInsertedCount() {
        throw this.getUnacknowledgedWriteException();
    }

    @Override
    public int getMatchedCount() {
        throw this.getUnacknowledgedWriteException();
    }

    @Override
    public int getRemovedCount() {
        throw this.getUnacknowledgedWriteException();
    }

    @Override
    @Deprecated
    public boolean isModifiedCountAvailable() {
        throw this.getUnacknowledgedWriteException();
    }

    @Override
    public int getModifiedCount() {
        throw this.getUnacknowledgedWriteException();
    }

    @Override
    public List<BulkWriteUpsert> getUpserts() {
        throw this.getUnacknowledgedWriteException();
    }

    private UnsupportedOperationException getUnacknowledgedWriteException() {
        return new UnsupportedOperationException("Can not get information about an unacknowledged write");
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        return o2 != null && this.getClass() == o2.getClass();
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "UnacknowledgedBulkWriteResult{}";
    }
}

