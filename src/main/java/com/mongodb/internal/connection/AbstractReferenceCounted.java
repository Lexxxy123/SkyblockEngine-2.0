/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.binding.ReferenceCounted;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AbstractReferenceCounted
implements ReferenceCounted {
    private final AtomicInteger referenceCount = new AtomicInteger(1);

    AbstractReferenceCounted() {
    }

    @Override
    public int getCount() {
        return this.referenceCount.get();
    }

    @Override
    public ReferenceCounted retain() {
        if (this.referenceCount.incrementAndGet() == 1) {
            throw new IllegalStateException("Attempted to increment the reference count when it is already 0");
        }
        return this;
    }

    @Override
    public void release() {
        if (this.referenceCount.decrementAndGet() < 0) {
            throw new IllegalStateException("Attempted to decrement the reference count below 0");
        }
    }
}

