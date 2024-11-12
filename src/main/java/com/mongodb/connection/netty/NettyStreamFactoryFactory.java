/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBufAllocator
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.nio.NioEventLoopGroup
 *  io.netty.channel.socket.SocketChannel
 *  io.netty.channel.socket.nio.NioSocketChannel
 */
package com.mongodb.connection.netty;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.StreamFactory;
import com.mongodb.connection.StreamFactoryFactory;
import com.mongodb.connection.netty.NettyStreamFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyStreamFactoryFactory
implements StreamFactoryFactory {
    private final EventLoopGroup eventLoopGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;

    @Deprecated
    public NettyStreamFactoryFactory() {
        this(NettyStreamFactoryFactory.builder());
    }

    @Deprecated
    public NettyStreamFactoryFactory(EventLoopGroup eventLoopGroup, ByteBufAllocator allocator) {
        this(NettyStreamFactoryFactory.builder().eventLoopGroup(eventLoopGroup).allocator(allocator));
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public StreamFactory create(SocketSettings socketSettings, SslSettings sslSettings) {
        return new NettyStreamFactory(socketSettings, sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator);
    }

    public String toString() {
        return "NettyStreamFactoryFactory{eventLoopGroup=" + this.eventLoopGroup + ", socketChannelClass=" + this.socketChannelClass + ", allocator=" + this.allocator + '}';
    }

    private NettyStreamFactoryFactory(Builder builder) {
        this.allocator = builder.allocator;
        this.socketChannelClass = builder.socketChannelClass;
        this.eventLoopGroup = builder.eventLoopGroup != null ? builder.eventLoopGroup : new NioEventLoopGroup();
    }

    public static final class Builder {
        private ByteBufAllocator allocator;
        private Class<? extends SocketChannel> socketChannelClass;
        private EventLoopGroup eventLoopGroup;

        private Builder() {
            this.allocator(ByteBufAllocator.DEFAULT);
            this.socketChannelClass(NioSocketChannel.class);
        }

        public Builder allocator(ByteBufAllocator allocator) {
            this.allocator = Assertions.notNull("allocator", allocator);
            return this;
        }

        public Builder socketChannelClass(Class<? extends SocketChannel> socketChannelClass) {
            this.socketChannelClass = Assertions.notNull("socketChannelClass", socketChannelClass);
            return this;
        }

        public Builder eventLoopGroup(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = Assertions.notNull("eventLoopGroup", eventLoopGroup);
            return this;
        }

        public NettyStreamFactoryFactory build() {
            return new NettyStreamFactoryFactory(this);
        }
    }
}

