/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncReadBinding;

@Deprecated
public interface AsyncReadOperation<T> {
    public void executeAsync(AsyncReadBinding var1, SingleResultCallback<T> var2);
}

