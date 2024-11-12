/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoException;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.QueryResult;
import java.util.List;

class AsyncSingleBatchQueryCursor<T>
implements AsyncBatchCursor<T> {
    private volatile QueryResult<T> firstBatch;
    private volatile boolean closed;

    AsyncSingleBatchQueryCursor(QueryResult<T> firstBatch) {
        this.firstBatch = firstBatch;
        Assertions.isTrue("Empty Cursor", firstBatch.getCursor() == null);
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public void next(SingleResultCallback<List<T>> callback) {
        if (this.closed) {
            callback.onResult(null, new MongoException("next() called after the cursor was closed."));
        } else if (this.firstBatch != null && !this.firstBatch.getResults().isEmpty()) {
            List<T> results = this.firstBatch.getResults();
            this.firstBatch = null;
            callback.onResult(results, null);
        } else {
            this.closed = true;
            callback.onResult(null, null);
        }
    }

    @Override
    public void tryNext(SingleResultCallback<List<T>> callback) {
        this.next(callback);
    }

    @Override
    public void setBatchSize(int batchSize) {
    }

    @Override
    public int getBatchSize() {
        return 0;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }
}

