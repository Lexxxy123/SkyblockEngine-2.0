/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;

@Immutable
public final class TransactionOptions {
    private final ReadConcern readConcern;
    private final WriteConcern writeConcern;
    private final ReadPreference readPreference;
    private final Long maxCommitTimeMS;

    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Nullable
    public Long getMaxCommitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        if (this.maxCommitTimeMS == null) {
            return null;
        }
        return timeUnit.convert(this.maxCommitTimeMS, TimeUnit.MILLISECONDS);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TransactionOptions merge(TransactionOptions options, TransactionOptions defaultOptions) {
        Assertions.notNull("options", options);
        Assertions.notNull("defaultOptions", defaultOptions);
        return TransactionOptions.builder().writeConcern(options.getWriteConcern() == null ? defaultOptions.getWriteConcern() : options.getWriteConcern()).readConcern(options.getReadConcern() == null ? defaultOptions.getReadConcern() : options.getReadConcern()).readPreference(options.getReadPreference() == null ? defaultOptions.getReadPreference() : options.getReadPreference()).maxCommitTime(options.getMaxCommitTime(TimeUnit.MILLISECONDS) == null ? defaultOptions.getMaxCommitTime(TimeUnit.MILLISECONDS) : options.getMaxCommitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).build();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        TransactionOptions that = (TransactionOptions)o2;
        if (this.maxCommitTimeMS != null ? !this.maxCommitTimeMS.equals(that.maxCommitTimeMS) : that.maxCommitTimeMS != null) {
            return false;
        }
        if (this.readConcern != null ? !this.readConcern.equals(that.readConcern) : that.readConcern != null) {
            return false;
        }
        if (this.writeConcern != null ? !this.writeConcern.equals(that.writeConcern) : that.writeConcern != null) {
            return false;
        }
        return !(this.readPreference != null ? !this.readPreference.equals(that.readPreference) : that.readPreference != null);
    }

    public int hashCode() {
        int result = this.readConcern != null ? this.readConcern.hashCode() : 0;
        result = 31 * result + (this.writeConcern != null ? this.writeConcern.hashCode() : 0);
        result = 31 * result + (this.readPreference != null ? this.readPreference.hashCode() : 0);
        result = 31 * result + (this.maxCommitTimeMS != null ? this.maxCommitTimeMS.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "TransactionOptions{readConcern=" + this.readConcern + ", writeConcern=" + this.writeConcern + ", readPreference=" + this.readPreference + ", maxCommitTimeMS" + this.maxCommitTimeMS + '}';
    }

    private TransactionOptions(Builder builder) {
        this.readConcern = builder.readConcern;
        this.writeConcern = builder.writeConcern;
        this.readPreference = builder.readPreference;
        this.maxCommitTimeMS = builder.maxCommitTimeMS;
    }

    public static final class Builder {
        private ReadConcern readConcern;
        private WriteConcern writeConcern;
        private ReadPreference readPreference;
        private Long maxCommitTimeMS;

        public Builder readConcern(@Nullable ReadConcern readConcern) {
            this.readConcern = readConcern;
            return this;
        }

        public Builder writeConcern(@Nullable WriteConcern writeConcern) {
            this.writeConcern = writeConcern;
            return this;
        }

        public Builder readPreference(@Nullable ReadPreference readPreference) {
            this.readPreference = readPreference;
            return this;
        }

        public Builder maxCommitTime(@Nullable Long maxCommitTime, TimeUnit timeUnit) {
            if (maxCommitTime == null) {
                this.maxCommitTimeMS = null;
            } else {
                Assertions.notNull("timeUnit", timeUnit);
                Assertions.isTrueArgument("maxCommitTime > 0", maxCommitTime > 0L);
                this.maxCommitTimeMS = TimeUnit.MILLISECONDS.convert(maxCommitTime, timeUnit);
            }
            return this;
        }

        public TransactionOptions build() {
            return new TransactionOptions(this);
        }

        private Builder() {
        }
    }
}

