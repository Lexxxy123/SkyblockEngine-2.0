/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel.impl;

import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.tlschannel.BufferAllocator;
import java.nio.ByteBuffer;
import org.bson.ByteBuf;

public class BufferHolder {
    private static final Logger LOGGER = Loggers.getLogger("connection.tls");
    private static final byte[] ZEROS = new byte[BufferHolder.roundUpToNextHighestPowerOfTwo(17408)];
    public final String name;
    public final BufferAllocator allocator;
    public final boolean plainData;
    public final int maxSize;
    public final boolean opportunisticDispose;
    private ByteBuf byteBuf;
    public ByteBuffer buffer;
    public int lastSize;

    public BufferHolder(String name, BufferAllocator allocator, int initialSize, int maxSize, boolean plainData, boolean opportunisticDispose) {
        this.name = name;
        this.allocator = allocator;
        this.buffer = null;
        this.maxSize = maxSize;
        this.plainData = plainData;
        this.opportunisticDispose = opportunisticDispose;
        this.lastSize = initialSize;
    }

    public void prepare() {
        if (this.buffer == null) {
            this.byteBuf = this.allocator.allocate(this.lastSize);
            this.buffer = this.byteBuf.asNIO();
        }
    }

    public boolean release() {
        if (this.opportunisticDispose && this.buffer.position() == 0) {
            return this.dispose();
        }
        return false;
    }

    public boolean dispose() {
        if (this.buffer != null) {
            this.allocator.free(this.byteBuf);
            this.buffer = null;
            return true;
        }
        return false;
    }

    public void resize(int newCapacity) {
        if (newCapacity > this.maxSize) {
            throw new IllegalArgumentException(String.format("new capacity (%s) bigger than absolute max size (%s)", newCapacity, this.maxSize));
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("resizing buffer %s, increasing from %s to %s (manual sizing)", this.name, this.buffer.capacity(), newCapacity));
        }
        this.resizeImpl(newCapacity);
    }

    public void enlarge() {
        if (this.buffer.capacity() >= this.maxSize) {
            throw new IllegalStateException(String.format("%s buffer insufficient despite having capacity of %d", this.name, this.buffer.capacity()));
        }
        int newCapacity = Math.min(this.lastSize * 2, this.maxSize);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("enlarging buffer %s, increasing from %s to %s (automatic enlarge)", this.name, this.buffer.capacity(), newCapacity));
        }
        this.resizeImpl(newCapacity);
    }

    private void resizeImpl(int newCapacity) {
        ByteBuf newByteBuf = this.allocator.allocate(newCapacity);
        ByteBuffer newBuffer = newByteBuf.asNIO();
        this.buffer.flip();
        newBuffer.put(this.buffer);
        if (this.plainData) {
            this.zero();
        }
        this.allocator.free(this.byteBuf);
        this.byteBuf = newByteBuf;
        this.buffer = newBuffer;
        this.lastSize = newCapacity;
    }

    public void zeroRemaining() {
        this.buffer.mark();
        this.buffer.put(ZEROS, 0, this.buffer.remaining());
        this.buffer.reset();
    }

    public void zero() {
        this.buffer.mark();
        this.buffer.position(0);
        this.buffer.put(ZEROS, 0, this.buffer.remaining());
        this.buffer.reset();
    }

    public boolean nullOrEmpty() {
        return this.buffer == null || this.buffer.position() == 0;
    }

    private static int roundUpToNextHighestPowerOfTwo(int size) {
        int v2 = size;
        --v2;
        v2 |= v2 >> 1;
        v2 |= v2 >> 2;
        v2 |= v2 >> 4;
        v2 |= v2 >> 8;
        v2 |= v2 >> 16;
        return ++v2;
    }
}

