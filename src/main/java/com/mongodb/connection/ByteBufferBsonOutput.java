/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.BufferProvider;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.io.OutputBuffer;

@Deprecated
public class ByteBufferBsonOutput
extends OutputBuffer {
    private static final int MAX_SHIFT = 31;
    private static final int INITIAL_SHIFT = 10;
    public static final int INITIAL_BUFFER_SIZE = 1024;
    public static final int MAX_BUFFER_SIZE = 0x1000000;
    private final BufferProvider bufferProvider;
    private final List<ByteBuf> bufferList = new ArrayList<ByteBuf>();
    private int curBufferIndex = 0;
    private int position = 0;
    private boolean closed;

    public ByteBufferBsonOutput(BufferProvider bufferProvider) {
        this.bufferProvider = Assertions.notNull("bufferProvider", bufferProvider);
    }

    @Override
    public void writeBytes(byte[] bytes, int offset, int length) {
        this.ensureOpen();
        int currentOffset = offset;
        int remainingLen = length;
        while (remainingLen > 0) {
            ByteBuf buf = this.getCurrentByteBuffer();
            int bytesToPutInCurrentBuffer = Math.min(buf.remaining(), remainingLen);
            buf.put(bytes, currentOffset, bytesToPutInCurrentBuffer);
            remainingLen -= bytesToPutInCurrentBuffer;
            currentOffset += bytesToPutInCurrentBuffer;
        }
        this.position += length;
    }

    @Override
    public void writeByte(int value) {
        this.ensureOpen();
        this.getCurrentByteBuffer().put((byte)value);
        ++this.position;
    }

    private ByteBuf getCurrentByteBuffer() {
        ByteBuf curByteBuffer = this.getByteBufferAtIndex(this.curBufferIndex);
        if (curByteBuffer.hasRemaining()) {
            return curByteBuffer;
        }
        ++this.curBufferIndex;
        return this.getByteBufferAtIndex(this.curBufferIndex);
    }

    private ByteBuf getByteBufferAtIndex(int index) {
        if (this.bufferList.size() < index + 1) {
            this.bufferList.add(this.bufferProvider.getBuffer(index >= 21 ? 0x1000000 : Math.min(1024 << index, 0x1000000)));
        }
        return this.bufferList.get(index);
    }

    @Override
    public int getPosition() {
        this.ensureOpen();
        return this.position;
    }

    @Override
    public int getSize() {
        this.ensureOpen();
        return this.position;
    }

    @Override
    protected void write(int absolutePosition, int value) {
        this.ensureOpen();
        if (absolutePosition < 0) {
            throw new IllegalArgumentException(String.format("position must be >= 0 but was %d", absolutePosition));
        }
        if (absolutePosition > this.position - 1) {
            throw new IllegalArgumentException(String.format("position must be <= %d but was %d", this.position - 1, absolutePosition));
        }
        BufferPositionPair bufferPositionPair = this.getBufferPositionPair(absolutePosition);
        ByteBuf byteBuffer = this.getByteBufferAtIndex(bufferPositionPair.bufferIndex);
        byteBuffer.put(bufferPositionPair.position++, (byte)value);
    }

    @Override
    public List<ByteBuf> getByteBuffers() {
        this.ensureOpen();
        ArrayList<ByteBuf> buffers = new ArrayList<ByteBuf>(this.bufferList.size());
        for (ByteBuf cur : this.bufferList) {
            buffers.add(cur.duplicate().order(ByteOrder.LITTLE_ENDIAN).flip());
        }
        return buffers;
    }

    @Override
    public int pipe(OutputStream out) throws IOException {
        this.ensureOpen();
        byte[] tmp = new byte[1024];
        int total = 0;
        for (ByteBuf cur : this.getByteBuffers()) {
            ByteBuf dup = cur.duplicate();
            while (dup.hasRemaining()) {
                int numBytesToCopy = Math.min(dup.remaining(), tmp.length);
                dup.get(tmp, 0, numBytesToCopy);
                out.write(tmp, 0, numBytesToCopy);
            }
            total += dup.limit();
        }
        return total;
    }

    @Override
    public void truncateToPosition(int newPosition) {
        this.ensureOpen();
        if (newPosition > this.position || newPosition < 0) {
            throw new IllegalArgumentException();
        }
        BufferPositionPair bufferPositionPair = this.getBufferPositionPair(newPosition);
        this.bufferList.get(bufferPositionPair.bufferIndex).position(bufferPositionPair.position);
        while (this.bufferList.size() > bufferPositionPair.bufferIndex + 1) {
            ByteBuf buffer = this.bufferList.remove(this.bufferList.size() - 1);
            buffer.release();
        }
        this.curBufferIndex = bufferPositionPair.bufferIndex;
        this.position = newPosition;
    }

    @Override
    public void close() {
        for (ByteBuf cur : this.bufferList) {
            cur.release();
        }
        this.bufferList.clear();
        this.closed = true;
    }

    private BufferPositionPair getBufferPositionPair(int absolutePosition) {
        int positionInBuffer = absolutePosition;
        int bufferIndex = 0;
        int bufferSize = 1024;
        int startPositionOfBuffer = 0;
        while (startPositionOfBuffer + bufferSize <= absolutePosition) {
            startPositionOfBuffer += bufferSize;
            positionInBuffer -= bufferSize;
            bufferSize = this.bufferList.get(++bufferIndex).limit();
        }
        return new BufferPositionPair(bufferIndex, positionInBuffer);
    }

    private void ensureOpen() {
        if (this.closed) {
            throw new IllegalStateException("The output is closed");
        }
    }

    private static final class BufferPositionPair {
        private final int bufferIndex;
        private int position;

        BufferPositionPair(int bufferIndex, int position) {
            this.bufferIndex = bufferIndex;
            this.position = position;
        }
    }
}

