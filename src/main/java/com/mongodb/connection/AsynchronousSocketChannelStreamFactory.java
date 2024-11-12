/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.Stream;
import com.mongodb.connection.StreamFactory;
import com.mongodb.internal.connection.AsynchronousSocketChannelStream;
import com.mongodb.internal.connection.PowerOfTwoBufferPool;
import java.nio.channels.AsynchronousChannelGroup;

public class AsynchronousSocketChannelStreamFactory
implements StreamFactory {
    private final BufferProvider bufferProvider = new PowerOfTwoBufferPool();
    private final SocketSettings settings;
    private final AsynchronousChannelGroup group;

    public AsynchronousSocketChannelStreamFactory(SocketSettings settings, SslSettings sslSettings) {
        this(settings, sslSettings, null);
    }

    public AsynchronousSocketChannelStreamFactory(SocketSettings settings, SslSettings sslSettings, AsynchronousChannelGroup group) {
        if (sslSettings.isEnabled()) {
            throw new UnsupportedOperationException("No SSL support in java.nio.channels.AsynchronousSocketChannel. For SSL support use com.mongodb.connection.netty.NettyStreamFactoryFactory");
        }
        this.settings = Assertions.notNull("settings", settings);
        this.group = group;
    }

    @Override
    public Stream create(ServerAddress serverAddress) {
        return new AsynchronousSocketChannelStream(serverAddress, this.settings, this.bufferProvider, this.group);
    }
}

