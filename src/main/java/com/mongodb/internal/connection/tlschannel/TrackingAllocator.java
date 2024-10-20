/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import com.mongodb.internal.connection.tlschannel.BufferAllocator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;
import org.bson.ByteBuf;

public class TrackingAllocator
implements BufferAllocator {
    private BufferAllocator impl;
    private LongAdder bytesAllocatedAdder = new LongAdder();
    private LongAdder bytesDeallocatedAdder = new LongAdder();
    private AtomicLong currentAllocationSize = new AtomicLong();
    private LongAccumulator maxAllocationSizeAcc = new LongAccumulator(new LongBinaryOperator(){

        @Override
        public long applyAsLong(long a2, long b2) {
            return Math.max(a2, b2);
        }
    }, 0L);
    private LongAdder buffersAllocatedAdder = new LongAdder();
    private LongAdder buffersDeallocatedAdder = new LongAdder();

    public TrackingAllocator(BufferAllocator impl) {
        this.impl = impl;
    }

    @Override
    public ByteBuf allocate(int size) {
        this.bytesAllocatedAdder.add(size);
        this.currentAllocationSize.addAndGet(size);
        this.buffersAllocatedAdder.increment();
        return this.impl.allocate(size);
    }

    @Override
    public void free(ByteBuf buffer) {
        int size = buffer.capacity();
        this.bytesDeallocatedAdder.add(size);
        this.maxAllocationSizeAcc.accumulate(this.currentAllocationSize.longValue());
        this.currentAllocationSize.addAndGet(-size);
        this.buffersDeallocatedAdder.increment();
        this.impl.free(buffer);
    }

    public long bytesAllocated() {
        return this.bytesAllocatedAdder.longValue();
    }

    public long bytesDeallocated() {
        return this.bytesDeallocatedAdder.longValue();
    }

    public long currentAllocation() {
        return this.currentAllocationSize.longValue();
    }

    public long maxAllocation() {
        return this.maxAllocationSizeAcc.longValue();
    }

    public long buffersAllocated() {
        return this.buffersAllocatedAdder.longValue();
    }

    public long buffersDeallocated() {
        return this.buffersDeallocatedAdder.longValue();
    }
}

