/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.lang.Nullable;

public class WriteResult {
    private final boolean acknowledged;
    private final int n;
    private final boolean updateOfExisting;
    private final Object upsertedId;

    public static WriteResult unacknowledged() {
        return new WriteResult();
    }

    public WriteResult(int n2, boolean updateOfExisting, @Nullable Object upsertedId) {
        this.acknowledged = true;
        this.n = n2;
        this.updateOfExisting = updateOfExisting;
        this.upsertedId = upsertedId;
    }

    WriteResult() {
        this.acknowledged = false;
        this.n = 0;
        this.updateOfExisting = false;
        this.upsertedId = null;
    }

    public boolean wasAcknowledged() {
        return this.acknowledged;
    }

    public int getN() {
        this.throwIfUnacknowledged("n");
        return this.n;
    }

    @Nullable
    public Object getUpsertedId() {
        this.throwIfUnacknowledged("upsertedId");
        return this.upsertedId;
    }

    public boolean isUpdateOfExisting() {
        this.throwIfUnacknowledged("updateOfExisting");
        return this.updateOfExisting;
    }

    public String toString() {
        if (this.acknowledged) {
            return "WriteResult{n=" + this.n + ", updateOfExisting=" + this.updateOfExisting + ", upsertedId=" + this.upsertedId + '}';
        }
        return "WriteResult{acknowledged=false}";
    }

    private void throwIfUnacknowledged(String property) {
        if (!this.acknowledged) {
            throw new UnsupportedOperationException("Cannot get " + property + " property for an unacknowledged write");
        }
    }
}

