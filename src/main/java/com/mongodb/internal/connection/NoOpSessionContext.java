/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ReadConcern;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public class NoOpSessionContext
implements SessionContext {
    public static final NoOpSessionContext INSTANCE = new NoOpSessionContext();

    @Override
    public boolean hasSession() {
        return false;
    }

    @Override
    public boolean isImplicitSession() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BsonDocument getSessionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCausallyConsistent() {
        return false;
    }

    @Override
    public long getTransactionNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long advanceTransactionNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean notifyMessageSent() {
        return false;
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return null;
    }

    @Override
    public void advanceOperationTime(BsonTimestamp operationTime) {
    }

    @Override
    public BsonDocument getClusterTime() {
        return null;
    }

    @Override
    public void advanceClusterTime(BsonDocument clusterTime) {
    }

    @Override
    public boolean hasActiveTransaction() {
        return false;
    }

    @Override
    public ReadConcern getReadConcern() {
        return ReadConcern.DEFAULT;
    }

    @Override
    public void setRecoveryToken(BsonDocument recoveryToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unpinServerAddress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void markSessionDirty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSessionMarkedDirty() {
        return false;
    }
}

