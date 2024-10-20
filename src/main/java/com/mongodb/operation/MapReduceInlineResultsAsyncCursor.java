/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.connection.QueryResult;
import com.mongodb.operation.AsyncSingleBatchQueryCursor;
import com.mongodb.operation.MapReduceAsyncBatchCursor;
import com.mongodb.operation.MapReduceStatistics;

class MapReduceInlineResultsAsyncCursor<T>
extends AsyncSingleBatchQueryCursor<T>
implements MapReduceAsyncBatchCursor<T> {
    private final MapReduceStatistics statistics;

    MapReduceInlineResultsAsyncCursor(QueryResult<T> queryResult, MapReduceStatistics statistics) {
        super(queryResult);
        this.statistics = statistics;
    }

    @Override
    public MapReduceStatistics getStatistics() {
        return this.statistics;
    }
}

