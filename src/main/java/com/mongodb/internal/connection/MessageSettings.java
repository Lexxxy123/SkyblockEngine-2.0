/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.connection.ServerType;

@Immutable
public final class MessageSettings {
    private static final int DEFAULT_MAX_DOCUMENT_SIZE = 0x1000000;
    private static final int DEFAULT_MAX_MESSAGE_SIZE = 0x2000000;
    private static final int DEFAULT_MAX_BATCH_COUNT = 1000;
    private final int maxDocumentSize;
    private final int maxMessageSize;
    private final int maxBatchCount;
    private final int maxWireVersion;
    private final ServerType serverType;

    public static Builder builder() {
        return new Builder();
    }

    public int getMaxDocumentSize() {
        return this.maxDocumentSize;
    }

    public int getMaxMessageSize() {
        return this.maxMessageSize;
    }

    public int getMaxBatchCount() {
        return this.maxBatchCount;
    }

    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    private MessageSettings(Builder builder) {
        this.maxDocumentSize = builder.maxDocumentSize;
        this.maxMessageSize = builder.maxMessageSize;
        this.maxBatchCount = builder.maxBatchCount;
        this.maxWireVersion = builder.maxWireVersion;
        this.serverType = builder.serverType;
    }

    @NotThreadSafe
    public static final class Builder {
        private int maxDocumentSize = 0x1000000;
        private int maxMessageSize = 0x2000000;
        private int maxBatchCount = 1000;
        private int maxWireVersion;
        private ServerType serverType;

        public MessageSettings build() {
            return new MessageSettings(this);
        }

        public Builder maxDocumentSize(int maxDocumentSize) {
            this.maxDocumentSize = maxDocumentSize;
            return this;
        }

        public Builder maxMessageSize(int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
            return this;
        }

        public Builder maxBatchCount(int maxBatchCount) {
            this.maxBatchCount = maxBatchCount;
            return this;
        }

        public Builder maxWireVersion(int maxWireVersion) {
            this.maxWireVersion = maxWireVersion;
            return this;
        }

        public Builder serverType(ServerType serverType) {
            this.serverType = serverType;
            return this;
        }
    }
}

