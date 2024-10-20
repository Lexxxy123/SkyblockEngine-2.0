/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBufAllocator
 *  io.netty.buffer.PooledByteBufAllocator
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.socket.SocketChannel
 *  io.netty.channel.socket.nio.NioSocketChannel
 */
package com.mongodb.connection.netty;

import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.Stream;
import com.mongodb.connection.StreamFactory;
import com.mongodb.connection.netty.NettyStream;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyStreamFactory
implements StreamFactory {
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final EventLoopGroup eventLoopGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;

    public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup, Class<? extends SocketChannel> socketChannelClass, ByteBufAllocator allocator) {
        this.settings = Assertions.notNull("settings", settings);
        this.sslSettings = Assertions.notNull("sslSettings", sslSettings);
        this.eventLoopGroup = Assertions.notNull("eventLoopGroup", eventLoopGroup);
        this.socketChannelClass = Assertions.notNull("socketChannelClass", socketChannelClass);
        this.allocator = Assertions.notNull("allocator", allocator);
    }

    public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup, ByteBufAllocator allocator) {
        this(settings, sslSettings, eventLoopGroup, NioSocketChannel.class, allocator);
    }

    public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup) {
        this(settings, sslSettings, eventLoopGroup, (ByteBufAllocator)PooledByteBufAllocator.DEFAULT);
    }

    public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings) {
        this(settings, sslSettings, (EventLoopGroup)new NioEventLoopGroup());
    }

    @Override
    public Stream create(ServerAddress serverAddress) {
        return new NettyStream(serverAddress, this.settings, this.sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator);
    }
}

