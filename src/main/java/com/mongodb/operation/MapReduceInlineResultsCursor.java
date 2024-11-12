/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.binding.ConnectionSource;
import com.mongodb.connection.QueryResult;
import com.mongodb.operation.MapReduceBatchCursor;
import com.mongodb.operation.MapReduceStatistics;
import com.mongodb.operation.QueryBatchCursor;
import org.bson.codecs.Decoder;

class MapReduceInlineResultsCursor<T>
extends QueryBatchCursor<T>
implements MapReduceBatchCursor<T> {
    private final MapReduceStatistics statistics;

    MapReduceInlineResultsCursor(QueryResult<T> queryResult, Decoder<T> decoder, ConnectionSource connectionSource, MapReduceStatistics statistics) {
        super(queryResult, 0, 0, decoder, connectionSource);
        this.statistics = statistics;
    }

    @Override
    public MapReduceStatistics getStatistics() {
        return this.statistics;
    }
}

