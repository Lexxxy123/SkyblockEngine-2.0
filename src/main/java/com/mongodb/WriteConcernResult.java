/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.lang.Nullable;
import org.bson.BsonValue;

public abstract class WriteConcernResult {
    public abstract boolean wasAcknowledged();

    public abstract int getCount();

    public abstract boolean isUpdateOfExisting();

    @Nullable
    public abstract BsonValue getUpsertedId();

    public static WriteConcernResult acknowledged(final int count, final boolean isUpdateOfExisting, final @Nullable BsonValue upsertedId) {
        return new WriteConcernResult(){

            @Override
            public boolean wasAcknowledged() {
                return true;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public boolean isUpdateOfExisting() {
                return isUpdateOfExisting;
            }

            @Override
            @Nullable
            public BsonValue getUpsertedId() {
                return upsertedId;
            }

            public boolean equals(Object o2) {
                if (this == o2) {
                    return true;
                }
                if (o2 == null || this.getClass() != o2.getClass()) {
                    return false;
                }
                WriteConcernResult that = (WriteConcernResult)o2;
                if (!that.wasAcknowledged()) {
                    return false;
                }
                if (count != that.getCount()) {
                    return false;
                }
                if (isUpdateOfExisting != that.isUpdateOfExisting()) {
                    return false;
                }
                return !(upsertedId != null ? !upsertedId.equals(that.getUpsertedId()) : that.getUpsertedId() != null);
            }

            public int hashCode() {
                int result = count;
                result = 31 * result + (isUpdateOfExisting ? 1 : 0);
                result = 31 * result + (upsertedId != null ? upsertedId.hashCode() : 0);
                return result;
            }

            public String toString() {
                return "AcknowledgedWriteResult{count=" + count + ", isUpdateOfExisting=" + isUpdateOfExisting + ", upsertedId=" + upsertedId + '}';
            }
        };
    }

    public static WriteConcernResult unacknowledged() {
        return new WriteConcernResult(){

            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public int getCount() {
                throw this.getUnacknowledgedWriteException();
            }

            @Override
            public boolean isUpdateOfExisting() {
                throw this.getUnacknowledgedWriteException();
            }

            @Override
            public BsonValue getUpsertedId() {
                throw this.getUnacknowledgedWriteException();
            }

            public boolean equals(Object o2) {
                if (this == o2) {
                    return true;
                }
                if (o2 == null || this.getClass() != o2.getClass()) {
                    return false;
                }
                WriteConcernResult that = (WriteConcernResult)o2;
                return !that.wasAcknowledged();
            }

            public int hashCode() {
                return 1;
            }

            public String toString() {
                return "UnacknowledgedWriteResult{}";
            }

            private UnsupportedOperationException getUnacknowledgedWriteException() {
                return new UnsupportedOperationException("Cannot get information about an unacknowledged write");
            }
        };
    }
}

