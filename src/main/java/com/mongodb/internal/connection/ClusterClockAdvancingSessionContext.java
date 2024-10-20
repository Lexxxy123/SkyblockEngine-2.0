/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ReadConcern;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public final class ClusterClockAdvancingSessionContext
implements SessionContext {
    private final SessionContext wrapped;
    private final ClusterClock clusterClock;

    public ClusterClockAdvancingSessionContext(SessionContext wrapped, ClusterClock clusterClock) {
        this.wrapped = wrapped;
        this.clusterClock = clusterClock;
    }

    @Override
    public boolean hasSession() {
        return this.wrapped.hasSession();
    }

    @Override
    public boolean isImplicitSession() {
        return this.wrapped.isImplicitSession();
    }

    @Override
    public BsonDocument getSessionId() {
        return this.wrapped.getSessionId();
    }

    @Override
    public boolean isCausallyConsistent() {
        return this.wrapped.isCausallyConsistent();
    }

    @Override
    public long getTransactionNumber() {
        return this.wrapped.getTransactionNumber();
    }

    @Override
    public long advanceTransactionNumber() {
        return this.wrapped.advanceTransactionNumber();
    }

    @Override
    public boolean notifyMessageSent() {
        return this.wrapped.notifyMessageSent();
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return this.wrapped.getOperationTime();
    }

    @Override
    public void advanceOperationTime(BsonTimestamp operationTime) {
        this.wrapped.advanceOperationTime(operationTime);
    }

    @Override
    public BsonDocument getClusterTime() {
        return this.clusterClock.greaterOf(this.wrapped.getClusterTime());
    }

    @Override
    public void advanceClusterTime(BsonDocument clusterTime) {
        this.wrapped.advanceClusterTime(clusterTime);
        this.clusterClock.advance(clusterTime);
    }

    @Override
    public boolean hasActiveTransaction() {
        return this.wrapped.hasActiveTransaction();
    }

    @Override
    public ReadConcern getReadConcern() {
        return this.wrapped.getReadConcern();
    }

    @Override
    public void setRecoveryToken(BsonDocument recoveryToken) {
        this.wrapped.setRecoveryToken(recoveryToken);
    }

    @Override
    public void unpinServerAddress() {
        this.wrapped.unpinServerAddress();
    }

    @Override
    public void markSessionDirty() {
        this.wrapped.markSessionDirty();
    }

    @Override
    public boolean isSessionMarkedDirty() {
        return this.wrapped.isSessionMarkedDirty();
    }
}

