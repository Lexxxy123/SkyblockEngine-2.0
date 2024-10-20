/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.session;

import org.bson.BsonDocument;

public interface ServerSession {
    public BsonDocument getIdentifier();

    public long getTransactionNumber();

    public long advanceTransactionNumber();

    public boolean isClosed();

    public void markDirty();

    public boolean isMarkedDirty();
}

