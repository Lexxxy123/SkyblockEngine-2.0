/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.result;

import com.mongodb.lang.Nullable;
import org.bson.BsonValue;

public abstract class UpdateResult {
    public abstract boolean wasAcknowledged();

    public abstract long getMatchedCount();

    @Deprecated
    public abstract boolean isModifiedCountAvailable();

    public abstract long getModifiedCount();

    @Nullable
    public abstract BsonValue getUpsertedId();

    public static UpdateResult acknowledged(long matchedCount, @Nullable Long modifiedCount, @Nullable BsonValue upsertedId) {
        return new AcknowledgedUpdateResult(matchedCount, modifiedCount, upsertedId);
    }

    public static UpdateResult unacknowledged() {
        return new UnacknowledgedUpdateResult();
    }

    private static class UnacknowledgedUpdateResult
    extends UpdateResult {
        private UnacknowledgedUpdateResult() {
        }

        @Override
        public boolean wasAcknowledged() {
            return false;
        }

        @Override
        public long getMatchedCount() {
            throw this.getUnacknowledgedWriteException();
        }

        @Override
        @Deprecated
        public boolean isModifiedCountAvailable() {
            return false;
        }

        @Override
        public long getModifiedCount() {
            throw this.getUnacknowledgedWriteException();
        }

        @Override
        @Nullable
        public BsonValue getUpsertedId() {
            throw this.getUnacknowledgedWriteException();
        }

        private UnsupportedOperationException getUnacknowledgedWriteException() {
            return new UnsupportedOperationException("Cannot get information about an unacknowledged update");
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
            return "UnacknowledgedUpdateResult{}";
        }
    }

    private static class AcknowledgedUpdateResult
    extends UpdateResult {
        private final long matchedCount;
        private final Long modifiedCount;
        private final BsonValue upsertedId;

        AcknowledgedUpdateResult(long matchedCount, Long modifiedCount, @Nullable BsonValue upsertedId) {
            this.matchedCount = matchedCount;
            this.modifiedCount = modifiedCount;
            this.upsertedId = upsertedId;
        }

        @Override
        public boolean wasAcknowledged() {
            return true;
        }

        @Override
        public long getMatchedCount() {
            return this.matchedCount;
        }

        @Override
        @Deprecated
        public boolean isModifiedCountAvailable() {
            return true;
        }

        @Override
        public long getModifiedCount() {
            return this.modifiedCount;
        }

        @Override
        @Nullable
        public BsonValue getUpsertedId() {
            return this.upsertedId;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            AcknowledgedUpdateResult that = (AcknowledgedUpdateResult)o2;
            if (this.matchedCount != that.matchedCount) {
                return false;
            }
            if (this.modifiedCount != null ? !this.modifiedCount.equals(that.modifiedCount) : that.modifiedCount != null) {
                return false;
            }
            return !(this.upsertedId != null ? !this.upsertedId.equals(that.upsertedId) : that.upsertedId != null);
        }

        public int hashCode() {
            int result = (int)(this.matchedCount ^ this.matchedCount >>> 32);
            result = 31 * result + (this.modifiedCount != null ? this.modifiedCount.hashCode() : 0);
            result = 31 * result + (this.upsertedId != null ? this.upsertedId.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "AcknowledgedUpdateResult{matchedCount=" + this.matchedCount + ", modifiedCount=" + this.modifiedCount + ", upsertedId=" + this.upsertedId + '}';
        }
    }
}

