/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.BufferProvider;
import com.mongodb.internal.connection.ConcurrentPool;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import org.bson.ByteBuf;
import org.bson.ByteBufNIO;

public class PowerOfTwoBufferPool
implements BufferProvider {
    private final Map<Integer, ConcurrentPool<ByteBuffer>> powerOfTwoToPoolMap = new HashMap<Integer, ConcurrentPool<ByteBuffer>>();

    public PowerOfTwoBufferPool() {
        this(24);
    }

    public PowerOfTwoBufferPool(int highestPowerOfTwo) {
        int powerOfTwo = 1;
        for (int i2 = 0; i2 <= highestPowerOfTwo; ++i2) {
            final int size = powerOfTwo;
            this.powerOfTwoToPoolMap.put(i2, new ConcurrentPool<ByteBuffer>(Integer.MAX_VALUE, new ConcurrentPool.ItemFactory<ByteBuffer>(){

                @Override
                public ByteBuffer create(boolean initialize) {
                    return PowerOfTwoBufferPool.this.createNew(size);
                }

                @Override
                public void close(ByteBuffer byteBuffer) {
                }

                @Override
                public ConcurrentPool.Prune shouldPrune(ByteBuffer byteBuffer) {
                    return ConcurrentPool.Prune.STOP;
                }
            }));
            powerOfTwo <<= 1;
        }
    }

    @Override
    public ByteBuf getBuffer(int size) {
        ConcurrentPool<ByteBuffer> pool = this.powerOfTwoToPoolMap.get(PowerOfTwoBufferPool.log2(PowerOfTwoBufferPool.roundUpToNextHighestPowerOfTwo(size)));
        ByteBuffer byteBuffer = pool == null ? this.createNew(size) : pool.get();
        byteBuffer.clear();
        byteBuffer.limit(size);
        return new PooledByteBufNIO(byteBuffer);
    }

    private ByteBuffer createNew(int size) {
        ByteBuffer buf = ByteBuffer.allocate(size);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf;
    }

    private void release(ByteBuffer buffer) {
        ConcurrentPool<ByteBuffer> pool = this.powerOfTwoToPoolMap.get(PowerOfTwoBufferPool.log2(PowerOfTwoBufferPool.roundUpToNextHighestPowerOfTwo(buffer.capacity())));
        if (pool != null) {
            pool.release(buffer);
        }
    }

    static int log2(int powerOfTwo) {
        return 31 - Integer.numberOfLeadingZeros(powerOfTwo);
    }

    static int roundUpToNextHighestPowerOfTwo(int size) {
        int v2 = size;
        --v2;
        v2 |= v2 >> 1;
        v2 |= v2 >> 2;
        v2 |= v2 >> 4;
        v2 |= v2 >> 8;
        v2 |= v2 >> 16;
        return ++v2;
    }

    private class PooledByteBufNIO
    extends ByteBufNIO {
        PooledByteBufNIO(ByteBuffer buf) {
            super(buf);
        }

        @Override
        public void release() {
            ByteBuffer wrapped = this.asNIO();
            super.release();
            if (this.getReferenceCount() == 0) {
                PowerOfTwoBufferPool.this.release(wrapped);
            }
        }
    }
}

