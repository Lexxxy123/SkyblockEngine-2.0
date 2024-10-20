/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.operation.MapReduceStatistics;

@Deprecated
public interface MapReduceAsyncBatchCursor<T>
extends AsyncBatchCursor<T> {
    public MapReduceStatistics getStatistics();
}

