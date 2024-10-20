/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.ByteBuf;
import org.bson.assertions.Assertions;

class CompositeByteBuf
implements ByteBuf {
    private final List<Component> components;
    private final AtomicInteger referenceCount = new AtomicInteger(1);
    private int position;
    private int limit;

    CompositeByteBuf(List<ByteBuf> buffers) {
        Assertions.notNull("buffers", buffers);
        Assertions.isTrueArgument("buffer list not empty", !buffers.isEmpty());
        this.components = new ArrayList<Component>(buffers.size());
        int offset = 0;
        for (ByteBuf cur : buffers) {
            Component component = new Component(cur.asReadOnly().order(ByteOrder.LITTLE_ENDIAN), offset);
            this.components.add(component);
            offset = component.endOffset;
        }
        this.limit = this.components.get(this.components.size() - 1).endOffset;
    }

    CompositeByteBuf(CompositeByteBuf from) {
        this.components = from.components;
        this.position = from.position();
        this.limit = from.limit();
    }

    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            throw new UnsupportedOperationException(String.format("Only %s is supported", ByteOrder.BIG_ENDIAN));
        }
        return this;
    }

    @Override
    public int capacity() {
        return this.components.get(this.components.size() - 1).endOffset;
    }

    @Override
    public int remaining() {
        return this.limit() - this.position();
    }

    @Override
    public boolean hasRemaining() {
        return this.remaining() > 0;
    }

    @Override
    public int position() {
        return this.position;
    }

    @Override
    public ByteBuf position(int newPosition) {
        if (newPosition < 0 || newPosition > this.limit) {
            throw new IndexOutOfBoundsException(String.format("%d is out of bounds", newPosition));
        }
        this.position = newPosition;
        return this;
    }

    @Override
    public ByteBuf clear() {
        this.position = 0;
        this.limit = this.capacity();
        return this;
    }

    @Override
    public int limit() {
        return this.limit;
    }

    @Override
    public byte get() {
        this.checkIndex(this.position);
        ++this.position;
        return this.get(this.position - 1);
    }

    @Override
    public byte get(int index) {
        this.checkIndex(index);
        Component component = this.findComponent(index);
        return component.buffer.get(index - component.offset);
    }

    @Override
    public ByteBuf get(byte[] bytes) {
        this.checkIndex(this.position, bytes.length);
        this.position += bytes.length;
        return this.get(this.position - bytes.length, bytes);
    }

    @Override
    public ByteBuf get(int index, byte[] bytes) {
        return this.get(index, bytes, 0, bytes.length);
    }

    @Override
    public ByteBuf get(byte[] bytes, int offset, int length) {
        this.checkIndex(this.position, length);
        this.position += length;
        return this.get(this.position - length, bytes, offset, length);
    }

    @Override
    public ByteBuf get(int index, byte[] bytes, int offset, int length) {
        this.checkDstIndex(index, length, offset, bytes.length);
        int i2 = this.findComponentIndex(index);
        int curIndex = index;
        int curOffset = offset;
        int curLength = length;
        while (curLength > 0) {
            Component c2 = this.components.get(i2);
            int localLength = Math.min(curLength, c2.buffer.capacity() - (curIndex - c2.offset));
            c2.buffer.get(curIndex - c2.offset, bytes, curOffset, localLength);
            curIndex += localLength;
            curOffset += localLength;
            curLength -= localLength;
            ++i2;
        }
        return this;
    }

    @Override
    public long getLong() {
        this.position += 8;
        return this.getLong(this.position - 8);
    }

    @Override
    public long getLong(int index) {
        this.checkIndex(index, 8);
        Component component = this.findComponent(index);
        if (index + 8 <= component.endOffset) {
            return component.buffer.getLong(index - component.offset);
        }
        return (long)this.getInt(index) & 0xFFFFFFFFL | ((long)this.getInt(index + 4) & 0xFFFFFFFFL) << 32;
    }

    @Override
    public double getDouble() {
        this.position += 8;
        return this.getDouble(this.position - 8);
    }

    @Override
    public double getDouble(int index) {
        return Double.longBitsToDouble(this.getLong(index));
    }

    @Override
    public int getInt() {
        this.position += 4;
        return this.getInt(this.position - 4);
    }

    @Override
    public int getInt(int index) {
        this.checkIndex(index, 4);
        Component component = this.findComponent(index);
        if (index + 4 <= component.endOffset) {
            return component.buffer.getInt(index - component.offset);
        }
        return this.getShort(index) & 0xFFFF | (this.getShort(index + 2) & 0xFFFF) << 16;
    }

    private int getShort(int index) {
        this.checkIndex(index, 2);
        return (short)(this.get(index) & 0xFF | (this.get(index + 1) & 0xFF) << 8);
    }

    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public ByteBuf limit(int newLimit) {
        if (newLimit < 0 || newLimit > this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("%d is out of bounds", newLimit));
        }
        this.limit = newLimit;
        return this;
    }

    @Override
    public ByteBuf put(int index, byte b2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf put(byte[] src, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf put(byte b2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf flip() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf asReadOnly() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuf duplicate() {
        return new CompositeByteBuf(this);
    }

    @Override
    public ByteBuffer asNIO() {
        if (this.components.size() == 1) {
            ByteBuffer byteBuffer = this.components.get(0).buffer.asNIO().duplicate();
            byteBuffer.position(this.position).limit(this.limit);
            return byteBuffer;
        }
        byte[] bytes = new byte[this.remaining()];
        this.get(this.position, bytes, 0, bytes.length);
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public int getReferenceCount() {
        return this.referenceCount.get();
    }

    @Override
    public ByteBuf retain() {
        if (this.referenceCount.incrementAndGet() == 1) {
            this.referenceCount.decrementAndGet();
            throw new IllegalStateException("Attempted to increment the reference count when it is already 0");
        }
        return this;
    }

    @Override
    public void release() {
        if (this.referenceCount.decrementAndGet() < 0) {
            this.referenceCount.incrementAndGet();
            throw new IllegalStateException("Attempted to decrement the reference count below 0");
        }
    }

    private Component findComponent(int index) {
        return this.components.get(this.findComponentIndex(index));
    }

    private int findComponentIndex(int index) {
        for (int i2 = this.components.size() - 1; i2 >= 0; --i2) {
            Component cur = this.components.get(i2);
            if (index < cur.offset) continue;
            return i2;
        }
        throw new IndexOutOfBoundsException(String.format("%d is out of bounds", index));
    }

    private void checkIndex(int index) {
        this.ensureAccessible();
        if (index < 0 || index >= this.capacity()) {
            throw new IndexOutOfBoundsException(String.format("index: %d (expected: range(0, %d))", index, this.capacity()));
        }
    }

    private void checkIndex(int index, int fieldLength) {
        this.ensureAccessible();
        if (index < 0 || index > this.capacity() - fieldLength) {
            throw new IndexOutOfBoundsException(String.format("index: %d, length: %d (expected: range(0, %d))", index, fieldLength, this.capacity()));
        }
    }

    private void checkDstIndex(int index, int length, int dstIndex, int dstCapacity) {
        this.checkIndex(index, length);
        if (dstIndex < 0 || dstIndex > dstCapacity - length) {
            throw new IndexOutOfBoundsException(String.format("dstIndex: %d, length: %d (expected: range(0, %d))", dstIndex, length, dstCapacity));
        }
    }

    private void ensureAccessible() {
        if (this.referenceCount.get() == 0) {
            throw new IllegalStateException("Reference count is 0");
        }
    }

    private static final class Component {
        private final ByteBuf buffer;
        private final int length;
        private final int offset;
        private final int endOffset;

        Component(ByteBuf buffer, int offset) {
            this.buffer = buffer;
            this.length = buffer.limit() - buffer.position();
            this.offset = offset;
            this.endOffset = offset + this.length;
        }
    }
}

