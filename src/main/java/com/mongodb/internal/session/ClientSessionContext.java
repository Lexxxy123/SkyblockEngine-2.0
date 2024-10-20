/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.session;

import com.mongodb.assertions.Assertions;
import com.mongodb.session.ClientSession;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public abstract class ClientSessionContext
implements SessionContext {
    private ClientSession clientSession;

    public ClientSessionContext(ClientSession clientSession) {
        this.clientSession = Assertions.notNull("clientSession", clientSession);
    }

    public ClientSession getClientSession() {
        return this.clientSession;
    }

    @Override
    public boolean hasSession() {
        return true;
    }

    @Override
    public BsonDocument getSessionId() {
        return this.clientSession.getServerSession().getIdentifier();
    }

    @Override
    public boolean isCausallyConsistent() {
        return this.clientSession.isCausallyConsistent();
    }

    @Override
    public long getTransactionNumber() {
        return this.clientSession.getServerSession().getTransactionNumber();
    }

    @Override
    public long advanceTransactionNumber() {
        return this.clientSession.getServerSession().advanceTransactionNumber();
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return this.clientSession.getOperationTime();
    }

    @Override
    public void advanceOperationTime(BsonTimestamp operationTime) {
        this.clientSession.advanceOperationTime(operationTime);
    }

    @Override
    public BsonDocument getClusterTime() {
        return this.clientSession.getClusterTime();
    }

    @Override
    public void advanceClusterTime(BsonDocument clusterTime) {
        this.clientSession.advanceClusterTime(clusterTime);
    }

    @Override
    public void setRecoveryToken(BsonDocument recoveryToken) {
        this.clientSession.setRecoveryToken(recoveryToken);
    }

    @Override
    public void unpinServerAddress() {
        this.clientSession.setPinnedServerAddress(null);
    }

    @Override
    public void markSessionDirty() {
        this.clientSession.getServerSession().markDirty();
    }

    @Override
    public boolean isSessionMarkedDirty() {
        return this.clientSession.getServerSession().isMarkedDirty();
    }
}

