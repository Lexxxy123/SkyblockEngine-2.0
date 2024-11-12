/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.result;

public abstract class DeleteResult {
    public abstract boolean wasAcknowledged();

    public abstract long getDeletedCount();

    public static DeleteResult acknowledged(long deletedCount) {
        return new AcknowledgedDeleteResult(deletedCount);
    }

    public static DeleteResult unacknowledged() {
        return new UnacknowledgedDeleteResult();
    }

    private static class UnacknowledgedDeleteResult
    extends DeleteResult {
        private UnacknowledgedDeleteResult() {
        }

        @Override
        public boolean wasAcknowledged() {
            return false;
        }

        @Override
        public long getDeletedCount() {
            throw new UnsupportedOperationException("Cannot get information about an unacknowledged delete");
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
            return "UnacknowledgedDeleteResult{}";
        }
    }

    private static class AcknowledgedDeleteResult
    extends DeleteResult {
        private final long deletedCount;

        AcknowledgedDeleteResult(long deletedCount) {
            this.deletedCount = deletedCount;
        }

        @Override
        public boolean wasAcknowledged() {
            return true;
        }

        @Override
        public long getDeletedCount() {
            return this.deletedCount;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            AcknowledgedDeleteResult that = (AcknowledgedDeleteResult)o2;
            return this.deletedCount == that.deletedCount;
        }

        public int hashCode() {
            return (int)(this.deletedCount ^ this.deletedCount >>> 32);
        }

        public String toString() {
            return "AcknowledgedDeleteResult{deletedCount=" + this.deletedCount + '}';
        }
    }
}

