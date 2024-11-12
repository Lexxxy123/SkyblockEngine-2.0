/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.binding.WriteBinding;

@Deprecated
public interface WriteOperation<T> {
    public T execute(WriteBinding var1);
}

