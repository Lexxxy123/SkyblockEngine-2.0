/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ReadPreference;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;

@Deprecated
@Immutable
public final class ParallelScanOptions {
    private final int numCursors;
    private final int batchSize;
    private final ReadPreference readPreference;

    public static Builder builder() {
        return new Builder();
    }

    public int getNumCursors() {
        return this.numCursors;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    private ParallelScanOptions(Builder builder) {
        this.numCursors = builder.numCursors;
        this.batchSize = builder.batchSize;
        this.readPreference = builder.readPreference;
    }

    @NotThreadSafe
    public static class Builder {
        private int numCursors = 1;
        private int batchSize;
        private ReadPreference readPreference;

        public Builder numCursors(int numCursors) {
            Assertions.isTrue("numCursors >= 1", numCursors >= 1);
            this.numCursors = numCursors;
            return this;
        }

        public Builder batchSize(int batchSize) {
            Assertions.isTrue("batchSize >= 0", batchSize >= 0);
            this.batchSize = batchSize;
            return this;
        }

        public Builder readPreference(ReadPreference readPreference) {
            this.readPreference = Assertions.notNull("readPreference", readPreference);
            return this;
        }

        public ParallelScanOptions build() {
            return new ParallelScanOptions(this);
        }
    }
}

