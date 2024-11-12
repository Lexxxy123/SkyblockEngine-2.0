/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.client.model.Collation;
import java.util.concurrent.TimeUnit;

public class AggregationOptions {
    private final Integer batchSize;
    private final Boolean allowDiskUse;
    private final OutputMode outputMode;
    private final long maxTimeMS;
    private final Boolean bypassDocumentValidation;
    private final Collation collation;

    AggregationOptions(Builder builder) {
        this.batchSize = builder.batchSize;
        this.allowDiskUse = builder.allowDiskUse;
        this.outputMode = builder.outputMode;
        this.maxTimeMS = builder.maxTimeMS;
        this.bypassDocumentValidation = builder.bypassDocumentValidation;
        this.collation = builder.collation;
    }

    public Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }

    public Integer getBatchSize() {
        return this.batchSize;
    }

    @Deprecated
    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public String toString() {
        return "AggregationOptions{batchSize=" + this.batchSize + ", allowDiskUse=" + this.allowDiskUse + ", outputMode=" + (Object)((Object)this.outputMode) + ", maxTimeMS=" + this.maxTimeMS + ", bypassDocumentValidation=" + this.bypassDocumentValidation + ", collation=" + this.collation + "}";
    }

    public static Builder builder() {
        return new Builder();
    }

    @NotThreadSafe
    public static class Builder {
        private Integer batchSize;
        private Boolean allowDiskUse;
        private OutputMode outputMode = OutputMode.CURSOR;
        private long maxTimeMS;
        private Boolean bypassDocumentValidation;
        private Collation collation;

        private Builder() {
        }

        public Builder batchSize(Integer size) {
            this.batchSize = size;
            return this;
        }

        public Builder allowDiskUse(Boolean allowDiskUse) {
            this.allowDiskUse = allowDiskUse;
            return this;
        }

        @Deprecated
        public Builder outputMode(OutputMode mode) {
            this.outputMode = mode;
            return this;
        }

        public Builder maxTime(long maxTime, TimeUnit timeUnit) {
            this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
            return this;
        }

        public Builder bypassDocumentValidation(Boolean bypassDocumentValidation) {
            this.bypassDocumentValidation = bypassDocumentValidation;
            return this;
        }

        public Builder collation(Collation collation) {
            this.collation = collation;
            return this;
        }

        public AggregationOptions build() {
            return new AggregationOptions(this);
        }
    }

    @Deprecated
    public static enum OutputMode {
        INLINE,
        CURSOR;

    }
}

