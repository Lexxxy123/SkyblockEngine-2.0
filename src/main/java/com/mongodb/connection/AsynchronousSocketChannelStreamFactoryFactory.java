/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.connection.AsynchronousSocketChannelStreamFactory;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.StreamFactory;
import com.mongodb.connection.StreamFactoryFactory;
import java.nio.channels.AsynchronousChannelGroup;

public class AsynchronousSocketChannelStreamFactoryFactory
implements StreamFactoryFactory {
    private final AsynchronousChannelGroup group;

    @Deprecated
    public AsynchronousSocketChannelStreamFactoryFactory() {
        this(AsynchronousSocketChannelStreamFactoryFactory.builder());
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public StreamFactory create(SocketSettings socketSettings, SslSettings sslSettings) {
        return new AsynchronousSocketChannelStreamFactory(socketSettings, sslSettings, this.group);
    }

    private AsynchronousSocketChannelStreamFactoryFactory(Builder builder) {
        this.group = builder.group;
    }

    public static final class Builder {
        private AsynchronousChannelGroup group;

        public Builder group(AsynchronousChannelGroup group) {
            this.group = group;
            return this;
        }

        public AsynchronousSocketChannelStreamFactoryFactory build() {
            return new AsynchronousSocketChannelStreamFactoryFactory(this);
        }
    }
}

