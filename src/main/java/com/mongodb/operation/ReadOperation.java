/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.binding.ReadBinding;

@Deprecated
public interface ReadOperation<T> {
    public T execute(ReadBinding var1);
}

