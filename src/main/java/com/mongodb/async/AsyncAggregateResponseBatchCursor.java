/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.async;

import com.mongodb.async.AsyncBatchCursor;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

@Deprecated
public interface AsyncAggregateResponseBatchCursor<T>
extends AsyncBatchCursor<T> {
    public BsonDocument getPostBatchResumeToken();

    public BsonTimestamp getOperationTime();

    public boolean isFirstBatchEmpty();
}

