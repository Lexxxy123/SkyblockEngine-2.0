/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.MapReduceAction;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface MapReduceIterable<TResult>
extends MongoIterable<TResult> {
    public void toCollection();

    public MapReduceIterable<TResult> collectionName(String var1);

    public MapReduceIterable<TResult> finalizeFunction(@Nullable String var1);

    public MapReduceIterable<TResult> scope(@Nullable Bson var1);

    public MapReduceIterable<TResult> sort(@Nullable Bson var1);

    public MapReduceIterable<TResult> filter(@Nullable Bson var1);

    public MapReduceIterable<TResult> limit(int var1);

    public MapReduceIterable<TResult> jsMode(boolean var1);

    public MapReduceIterable<TResult> verbose(boolean var1);

    public MapReduceIterable<TResult> maxTime(long var1, TimeUnit var3);

    public MapReduceIterable<TResult> action(MapReduceAction var1);

    public MapReduceIterable<TResult> databaseName(@Nullable String var1);

    public MapReduceIterable<TResult> sharded(boolean var1);

    public MapReduceIterable<TResult> nonAtomic(boolean var1);

    @Override
    public MapReduceIterable<TResult> batchSize(int var1);

    public MapReduceIterable<TResult> bypassDocumentValidation(@Nullable Boolean var1);

    public MapReduceIterable<TResult> collation(@Nullable Collation var1);
}

