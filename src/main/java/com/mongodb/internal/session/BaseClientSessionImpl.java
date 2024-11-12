/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.session;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.internal.session.ServerSessionPool;
import com.mongodb.lang.Nullable;
import com.mongodb.session.ClientSession;
import com.mongodb.session.ServerSession;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public class BaseClientSessionImpl
implements ClientSession {
    private static final String CLUSTER_TIME_KEY = "clusterTime";
    private final ServerSessionPool serverSessionPool;
    private final ServerSession serverSession;
    private final Object originator;
    private final ClientSessionOptions options;
    private BsonDocument clusterTime;
    private BsonTimestamp operationTime;
    private ServerAddress pinnedServerAddress;
    private BsonDocument recoveryToken;
    private volatile boolean closed;

    public BaseClientSessionImpl(ServerSessionPool serverSessionPool, Object originator, ClientSessionOptions options) {
        this.serverSessionPool = serverSessionPool;
        this.serverSession = serverSessionPool.get();
        this.originator = originator;
        this.options = options;
        this.pinnedServerAddress = null;
        this.closed = false;
    }

    @Override
    @Nullable
    public ServerAddress getPinnedServerAddress() {
        return this.pinnedServerAddress;
    }

    @Override
    public void setPinnedServerAddress(@Nullable ServerAddress address) {
        Assertions.isTrue("pinned mongos null check", address == null || this.pinnedServerAddress == null);
        this.pinnedServerAddress = address;
    }

    @Override
    public BsonDocument getRecoveryToken() {
        return this.recoveryToken;
    }

    @Override
    public void setRecoveryToken(BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
    }

    @Override
    public ClientSessionOptions getOptions() {
        return this.options;
    }

    @Override
    public boolean isCausallyConsistent() {
        Boolean causallyConsistent = this.options.isCausallyConsistent();
        return causallyConsistent == null ? true : causallyConsistent;
    }

    @Override
    public Object getOriginator() {
        return this.originator;
    }

    @Override
    public BsonDocument getClusterTime() {
        return this.clusterTime;
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return this.operationTime;
    }

    @Override
    public ServerSession getServerSession() {
        Assertions.isTrue("open", !this.closed);
        return this.serverSession;
    }

    @Override
    public void advanceOperationTime(BsonTimestamp newOperationTime) {
        Assertions.isTrue("open", !this.closed);
        this.operationTime = this.greaterOf(newOperationTime);
    }

    @Override
    public void advanceClusterTime(BsonDocument newClusterTime) {
        Assertions.isTrue("open", !this.closed);
        this.clusterTime = this.greaterOf(newClusterTime);
    }

    private BsonDocument greaterOf(BsonDocument newClusterTime) {
        if (newClusterTime == null) {
            return this.clusterTime;
        }
        if (this.clusterTime == null) {
            return newClusterTime;
        }
        return newClusterTime.getTimestamp(CLUSTER_TIME_KEY).compareTo(this.clusterTime.getTimestamp(CLUSTER_TIME_KEY)) > 0 ? newClusterTime : this.clusterTime;
    }

    private BsonTimestamp greaterOf(BsonTimestamp newOperationTime) {
        if (newOperationTime == null) {
            return this.operationTime;
        }
        if (this.operationTime == null) {
            return newOperationTime;
        }
        return newOperationTime.compareTo(this.operationTime) > 0 ? newOperationTime : this.operationTime;
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;
            this.serverSessionPool.release(this.serverSession);
            this.pinnedServerAddress = null;
        }
    }
}

