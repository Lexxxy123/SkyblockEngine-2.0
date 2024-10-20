/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ReadPreference;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;

@Deprecated
public interface OperationExecutor {
    public <T> T execute(ReadOperation<T> var1, ReadPreference var2);

    public <T> T execute(WriteOperation<T> var1);
}

