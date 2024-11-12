/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface AggregateIterable<TResult>
extends MongoIterable<TResult> {
    public void toCollection();

    public AggregateIterable<TResult> allowDiskUse(@Nullable Boolean var1);

    @Override
    public AggregateIterable<TResult> batchSize(int var1);

    public AggregateIterable<TResult> maxTime(long var1, TimeUnit var3);

    @Deprecated
    public AggregateIterable<TResult> useCursor(@Nullable Boolean var1);

    public AggregateIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

    public AggregateIterable<TResult> bypassDocumentValidation(@Nullable Boolean var1);

    public AggregateIterable<TResult> collation(@Nullable Collation var1);

    public AggregateIterable<TResult> comment(@Nullable String var1);

    public AggregateIterable<TResult> hint(@Nullable Bson var1);
}

