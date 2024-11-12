/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ServerAddress;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.ServerType;
import com.mongodb.connection.ServerVersion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Immutable
public class ConnectionDescription {
    private final ConnectionId connectionId;
    private final ServerVersion serverVersion;
    private final int maxWireVersion;
    private final ServerType serverType;
    private final int maxBatchCount;
    private final int maxDocumentSize;
    private final int maxMessageSize;
    private final List<String> compressors;
    private static final int DEFAULT_MAX_MESSAGE_SIZE = 0x2000000;
    private static final int DEFAULT_MAX_WRITE_BATCH_SIZE = 512;

    public ConnectionDescription(ServerId serverId) {
        this(new ConnectionId(serverId), new ServerVersion(), 0, ServerType.UNKNOWN, 512, ServerDescription.getDefaultMaxDocumentSize(), 0x2000000, Collections.emptyList());
    }

    @Deprecated
    public ConnectionDescription(ConnectionId connectionId, ServerVersion serverVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize) {
        this(connectionId, serverVersion, 0, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, Collections.emptyList());
    }

    @Deprecated
    public ConnectionDescription(ConnectionId connectionId, ServerVersion serverVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors) {
        this(connectionId, serverVersion, 0, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors);
    }

    @Deprecated
    public ConnectionDescription(ConnectionId connectionId, ServerVersion serverVersion, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors) {
        this.connectionId = connectionId;
        this.serverType = serverType;
        this.maxBatchCount = maxBatchCount;
        this.maxDocumentSize = maxDocumentSize;
        this.maxMessageSize = maxMessageSize;
        this.serverVersion = serverVersion;
        this.maxWireVersion = maxWireVersion;
        this.compressors = Assertions.notNull("compressors", Collections.unmodifiableList(new ArrayList<String>(compressors)));
    }

    public ConnectionDescription(ConnectionId connectionId, int maxWireVersion, ServerType serverType, int maxBatchCount, int maxDocumentSize, int maxMessageSize, List<String> compressors) {
        this(connectionId, new ServerVersion(), maxWireVersion, serverType, maxBatchCount, maxDocumentSize, maxMessageSize, compressors);
    }

    public ConnectionDescription withConnectionId(ConnectionId connectionId) {
        Assertions.notNull("connectionId", connectionId);
        return new ConnectionDescription(connectionId, this.serverVersion, this.maxWireVersion, this.serverType, this.maxBatchCount, this.maxDocumentSize, this.maxMessageSize, this.compressors);
    }

    public ServerAddress getServerAddress() {
        return this.connectionId.getServerId().getAddress();
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    @Deprecated
    public ServerVersion getServerVersion() {
        return this.serverVersion;
    }

    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    public int getMaxBatchCount() {
        return this.maxBatchCount;
    }

    public int getMaxDocumentSize() {
        return this.maxDocumentSize;
    }

    public int getMaxMessageSize() {
        return this.maxMessageSize;
    }

    public List<String> getCompressors() {
        return this.compressors;
    }

    public static int getDefaultMaxMessageSize() {
        return 0x2000000;
    }

    public static int getDefaultMaxWriteBatchSize() {
        return 512;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ConnectionDescription that = (ConnectionDescription)o2;
        if (this.maxBatchCount != that.maxBatchCount) {
            return false;
        }
        if (this.maxDocumentSize != that.maxDocumentSize) {
            return false;
        }
        if (this.maxMessageSize != that.maxMessageSize) {
            return false;
        }
        if (!this.connectionId.equals(that.connectionId)) {
            return false;
        }
        if (this.serverType != that.serverType) {
            return false;
        }
        if (!this.serverVersion.equals(that.serverVersion)) {
            return false;
        }
        if (this.maxWireVersion != that.maxWireVersion) {
            return false;
        }
        return this.compressors.equals(that.compressors);
    }

    public int hashCode() {
        int result = this.connectionId.hashCode();
        result = 31 * result + this.serverVersion.hashCode();
        result = 31 * result + this.maxBatchCount;
        result = 31 * result + this.serverType.hashCode();
        result = 31 * result + this.maxBatchCount;
        result = 31 * result + this.maxDocumentSize;
        result = 31 * result + this.maxMessageSize;
        result = 31 * result + this.compressors.hashCode();
        return result;
    }

    public String toString() {
        return "ConnectionDescription{connectionId=" + this.connectionId + ", serverVersion=" + this.serverVersion + ", maxWireVersion=" + this.maxWireVersion + ", serverType=" + (Object)((Object)this.serverType) + ", maxBatchCount=" + this.maxBatchCount + ", maxDocumentSize=" + this.maxDocumentSize + ", maxMessageSize=" + this.maxMessageSize + ", compressors=" + this.compressors + '}';
    }
}

