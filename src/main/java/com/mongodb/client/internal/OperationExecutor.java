/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;

public interface OperationExecutor {
    public <T> T execute(ReadOperation<T> var1, ReadPreference var2, ReadConcern var3);

    public <T> T execute(WriteOperation<T> var1, ReadConcern var2);

    public <T> T execute(ReadOperation<T> var1, ReadPreference var2, ReadConcern var3, @Nullable ClientSession var4);

    public <T> T execute(WriteOperation<T> var1, ReadConcern var2, @Nullable ClientSession var3);
}

