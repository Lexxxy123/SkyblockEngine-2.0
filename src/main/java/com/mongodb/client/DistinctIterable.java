/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface DistinctIterable<TResult>
extends MongoIterable<TResult> {
    public DistinctIterable<TResult> filter(@Nullable Bson var1);

    public DistinctIterable<TResult> maxTime(long var1, TimeUnit var3);

    @Override
    public DistinctIterable<TResult> batchSize(int var1);

    public DistinctIterable<TResult> collation(@Nullable Collation var1);
}

