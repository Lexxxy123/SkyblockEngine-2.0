/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.session;

import com.mongodb.ReadConcern;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

@Deprecated
public interface SessionContext {
    public boolean hasSession();

    public boolean isImplicitSession();

    public BsonDocument getSessionId();

    public boolean isCausallyConsistent();

    public long getTransactionNumber();

    public long advanceTransactionNumber();

    public boolean notifyMessageSent();

    public BsonTimestamp getOperationTime();

    public void advanceOperationTime(BsonTimestamp var1);

    public BsonDocument getClusterTime();

    public void advanceClusterTime(BsonDocument var1);

    public boolean hasActiveTransaction();

    public ReadConcern getReadConcern();

    public void setRecoveryToken(BsonDocument var1);

    public void unpinServerAddress();

    public void markSessionDirty();

    public boolean isSessionMarkedDirty();
}

