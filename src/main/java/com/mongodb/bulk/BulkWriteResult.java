/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

import com.mongodb.bulk.BulkWriteUpsert;
import com.mongodb.bulk.WriteRequest;
import java.util.List;

public abstract class BulkWriteResult {
    public abstract boolean wasAcknowledged();

    public abstract int getInsertedCount();

    public abstract int getMatchedCount();

    public abstract int getDeletedCount();

    @Deprecated
    public abstract boolean isModifiedCountAvailable();

    public abstract int getModifiedCount();

    public abstract List<BulkWriteUpsert> getUpserts();

    public static BulkWriteResult acknowledged(WriteRequest.Type type, int count, List<BulkWriteUpsert> upserts) {
        return BulkWriteResult.acknowledged(type, count, 0, upserts);
    }

    public static BulkWriteResult acknowledged(WriteRequest.Type type, int count, Integer modifiedCount, List<BulkWriteUpsert> upserts) {
        return BulkWriteResult.acknowledged(type == WriteRequest.Type.INSERT ? count : 0, type == WriteRequest.Type.UPDATE || type == WriteRequest.Type.REPLACE ? count : 0, type == WriteRequest.Type.DELETE ? count : 0, modifiedCount, upserts);
    }

    public static BulkWriteResult acknowledged(final int insertedCount, final int matchedCount, final int removedCount, final Integer modifiedCount, final List<BulkWriteUpsert> upserts) {
        return new BulkWriteResult(){

            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            @Override
            public int getInsertedCount() {
                return insertedCount;
            }

            @Override
            public int getMatchedCount() {
                return matchedCount;
            }

            @Override
            public int getDeletedCount() {
                return removedCount;
            }

            @Override
            @Deprecated
            public boolean isModifiedCountAvailable() {
                return true;
            }

            @Override
            public int getModifiedCount() {
                return modifiedCount;
            }

            @Override
            public List<BulkWriteUpsert> getUpserts() {
                return upserts;
            }

            public boolean equals(Object o2) {
                if (this == o2) {
                    return true;
                }
                if (o2 == null || this.getClass() != o2.getClass()) {
                    return false;
                }
                BulkWriteResult that = (BulkWriteResult)o2;
                if (!that.wasAcknowledged()) {
                    return false;
                }
                if (insertedCount != that.getInsertedCount()) {
                    return false;
                }
                if (modifiedCount != null && !modifiedCount.equals(that.getModifiedCount())) {
                    return false;
                }
                if (removedCount != that.getDeletedCount()) {
                    return false;
                }
                if (matchedCount != that.getMatchedCount()) {
                    return false;
                }
                return upserts.equals(that.getUpserts());
            }

            public int hashCode() {
                int result = upserts.hashCode();
                result = 31 * result + insertedCount;
                result = 31 * result + matchedCount;
                result = 31 * result + removedCount;
                result = 31 * result + (modifiedCount != null ? modifiedCount.hashCode() : 0);
                return result;
            }

            public String toString() {
                return "AcknowledgedBulkWriteResult{insertedCount=" + insertedCount + ", matchedCount=" + matchedCount + ", removedCount=" + removedCount + ", modifiedCount=" + modifiedCount + ", upserts=" + upserts + '}';
            }
        };
    }

    public static BulkWriteResult unacknowledged() {
        return new BulkWriteResult(){

            @Override
            public boolean wasAcknowledged() {
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
            public int getDeletedCount() {
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

            public boolean equals(Object o2) {
                if (this == o2) {
                    return true;
                }
                if (o2 == null || this.getClass() != o2.getClass()) {
                    return false;
                }
                BulkWriteResult that = (BulkWriteResult)o2;
                return !that.wasAcknowledged();
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "UnacknowledgedBulkWriteResult{}";
            }

            private UnsupportedOperationException getUnacknowledgedWriteException() {
                return new UnsupportedOperationException("Cannot get information about an unacknowledged write");
            }
        };
    }
}

