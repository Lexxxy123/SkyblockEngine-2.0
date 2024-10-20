/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel.impl;

import com.mongodb.internal.connection.tlschannel.impl.ByteBufferUtil;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteBufferSet {
    public final ByteBuffer[] array;
    public final int offset;
    public final int length;

    public ByteBufferSet(ByteBuffer[] array, int offset, int length) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (array.length < offset) {
            throw new IndexOutOfBoundsException();
        }
        if (array.length < offset + length) {
            throw new IndexOutOfBoundsException();
        }
        for (int i2 = offset; i2 < offset + length; ++i2) {
            if (array[i2] != null) continue;
            throw new NullPointerException();
        }
        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    public ByteBufferSet(ByteBuffer[] array) {
        this(array, 0, array.length);
    }

    public ByteBufferSet(ByteBuffer buffer) {
        this(new ByteBuffer[]{buffer});
    }

    public long remaining() {
        long ret = 0L;
        for (int i2 = this.offset; i2 < this.offset + this.length; ++i2) {
            ret += (long)this.array[i2].remaining();
        }
        return ret;
    }

    public int putRemaining(ByteBuffer from) {
        int totalBytes = 0;
        for (int i2 = this.offset; i2 < this.offset + this.length && from.hasRemaining(); ++i2) {
            ByteBuffer dstBuffer = this.array[i2];
            int bytes = Math.min(from.remaining(), dstBuffer.remaining());
            ByteBufferUtil.copy(from, dstBuffer, bytes);
            totalBytes += bytes;
        }
        return totalBytes;
    }

    public ByteBufferSet put(ByteBuffer from, int length) {
        int pending;
        if (from.remaining() < length) {
            throw new IllegalArgumentException();
        }
        if (this.remaining() < (long)length) {
            throw new IllegalArgumentException();
        }
        int totalBytes = 0;
        for (int i2 = this.offset; i2 < this.offset + this.length && (pending = length - totalBytes) != 0; ++i2) {
            int bytes = Math.min(pending, (int)this.remaining());
            ByteBuffer dstBuffer = this.array[i2];
            ByteBufferUtil.copy(from, dstBuffer, bytes);
            totalBytes += bytes;
        }
        return this;
    }

    public int getRemaining(ByteBuffer dst) {
        int totalBytes = 0;
        for (int i2 = this.offset; i2 < this.offset + this.length && dst.hasRemaining(); ++i2) {
            ByteBuffer srcBuffer = this.array[i2];
            int bytes = Math.min(dst.remaining(), srcBuffer.remaining());
            ByteBufferUtil.copy(srcBuffer, dst, bytes);
            totalBytes += bytes;
        }
        return totalBytes;
    }

    public ByteBufferSet get(ByteBuffer dst, int length) {
        int pending;
        if (this.remaining() < (long)length) {
            throw new IllegalArgumentException();
        }
        if (dst.remaining() < length) {
            throw new IllegalArgumentException();
        }
        int totalBytes = 0;
        for (int i2 = this.offset; i2 < this.offset + this.length && (pending = length - totalBytes) != 0; ++i2) {
            ByteBuffer srcBuffer = this.array[i2];
            int bytes = Math.min(pending, srcBuffer.remaining());
            ByteBufferUtil.copy(srcBuffer, dst, bytes);
            totalBytes += bytes;
        }
        return this;
    }

    public boolean hasRemaining() {
        return this.remaining() > 0L;
    }

    public boolean isReadOnly() {
        for (int i2 = this.offset; i2 < this.offset + this.length; ++i2) {
            if (!this.array[i2].isReadOnly()) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        return "ByteBufferSet[array=" + Arrays.toString(this.array) + ", offset=" + this.offset + ", length=" + this.length + "]";
    }
}

