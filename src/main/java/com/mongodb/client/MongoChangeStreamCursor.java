/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoCursor;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;

public interface MongoChangeStreamCursor<TResult>
extends MongoCursor<TResult> {
    @Nullable
    public BsonDocument getResumeToken();
}

