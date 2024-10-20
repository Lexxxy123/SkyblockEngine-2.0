/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.session.ClientSession;

@Deprecated
public interface AsyncOperationExecutor {
    public <T> void execute(AsyncReadOperation<T> var1, ReadPreference var2, SingleResultCallback<T> var3);

    public <T> void execute(AsyncReadOperation<T> var1, ReadPreference var2, ClientSession var3, SingleResultCallback<T> var4);

    public <T> void execute(AsyncWriteOperation<T> var1, SingleResultCallback<T> var2);

    public <T> void execute(AsyncWriteOperation<T> var1, ClientSession var2, SingleResultCallback<T> var3);
}

