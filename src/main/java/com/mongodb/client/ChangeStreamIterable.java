/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

public interface ChangeStreamIterable<TResult>
extends MongoIterable<ChangeStreamDocument<TResult>> {
    @Override
    public MongoChangeStreamCursor<ChangeStreamDocument<TResult>> cursor();

    public ChangeStreamIterable<TResult> fullDocument(FullDocument var1);

    public ChangeStreamIterable<TResult> resumeAfter(BsonDocument var1);

    @Override
    public ChangeStreamIterable<TResult> batchSize(int var1);

    public ChangeStreamIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

    public ChangeStreamIterable<TResult> collation(@Nullable Collation var1);

    public <TDocument> MongoIterable<TDocument> withDocumentClass(Class<TDocument> var1);

    public ChangeStreamIterable<TResult> startAtOperationTime(BsonTimestamp var1);

    public ChangeStreamIterable<TResult> startAfter(BsonDocument var1);
}

