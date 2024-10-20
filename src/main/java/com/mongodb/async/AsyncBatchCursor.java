/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.async;

import com.mongodb.async.SingleResultCallback;
import java.io.Closeable;
import java.util.List;

@Deprecated
public interface AsyncBatchCursor<T>
extends Closeable {
    public void next(SingleResultCallback<List<T>> var1);

    public void tryNext(SingleResultCallback<List<T>> var1);

    public void setBatchSize(int var1);

    public int getBatchSize();

    public boolean isClosed();

    @Override
    public void close();
}

