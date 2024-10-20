/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.TransactionOptions;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;

@Immutable
public final class ClientSessionOptions {
    private final Boolean causallyConsistent;
    private final TransactionOptions defaultTransactionOptions;

    @Nullable
    public Boolean isCausallyConsistent() {
        return this.causallyConsistent;
    }

    public TransactionOptions getDefaultTransactionOptions() {
        return this.defaultTransactionOptions;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ClientSessionOptions that = (ClientSessionOptions)o2;
        if (this.causallyConsistent != null ? !this.causallyConsistent.equals(that.causallyConsistent) : that.causallyConsistent != null) {
            return false;
        }
        return !(this.defaultTransactionOptions != null ? !this.defaultTransactionOptions.equals(that.defaultTransactionOptions) : that.defaultTransactionOptions != null);
    }

    public int hashCode() {
        int result = this.causallyConsistent != null ? this.causallyConsistent.hashCode() : 0;
        result = 31 * result + (this.defaultTransactionOptions != null ? this.defaultTransactionOptions.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ClientSessionOptions{causallyConsistent=" + this.causallyConsistent + ", defaultTransactionOptions=" + this.defaultTransactionOptions + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ClientSessionOptions options) {
        Assertions.notNull("options", options);
        Builder builder = new Builder();
        builder.causallyConsistent = options.isCausallyConsistent();
        builder.defaultTransactionOptions = options.getDefaultTransactionOptions();
        return builder;
    }

    private ClientSessionOptions(Builder builder) {
        this.causallyConsistent = builder.causallyConsistent;
        this.defaultTransactionOptions = builder.defaultTransactionOptions;
    }

    @NotThreadSafe
    public static final class Builder {
        private Boolean causallyConsistent;
        private TransactionOptions defaultTransactionOptions = TransactionOptions.builder().build();

        public Builder causallyConsistent(boolean causallyConsistent) {
            this.causallyConsistent = causallyConsistent;
            return this;
        }

        public Builder defaultTransactionOptions(TransactionOptions defaultTransactionOptions) {
            this.defaultTransactionOptions = Assertions.notNull("defaultTransactionOptions", defaultTransactionOptions);
            return this;
        }

        public ClientSessionOptions build() {
            return new ClientSessionOptions(this);
        }

        private Builder() {
        }
    }
}

