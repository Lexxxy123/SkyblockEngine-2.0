/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBufAllocator
 *  io.netty.buffer.PooledByteBufAllocator
 */
package com.mongodb.connection.netty;

import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.netty.NettyByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.bson.ByteBuf;

final class NettyBufferProvider
implements BufferProvider {
    private final ByteBufAllocator allocator;

    NettyBufferProvider() {
        this.allocator = PooledByteBufAllocator.DEFAULT;
    }

    NettyBufferProvider(ByteBufAllocator allocator) {
        this.allocator = allocator;
    }

    @Override
    public ByteBuf getBuffer(int size) {
        return new NettyByteBuf(this.allocator.directBuffer(size, size));
    }
}

