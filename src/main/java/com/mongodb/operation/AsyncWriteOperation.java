/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;

@Deprecated
public interface AsyncWriteOperation<T> {
    public void executeAsync(AsyncWriteBinding var1, SingleResultCallback<T> var2);
}

