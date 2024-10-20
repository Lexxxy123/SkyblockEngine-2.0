/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoIterable;
import java.util.concurrent.TimeUnit;

public interface ListIndexesIterable<TResult>
extends MongoIterable<TResult> {
    public ListIndexesIterable<TResult> maxTime(long var1, TimeUnit var3);

    @Override
    public ListIndexesIterable<TResult> batchSize(int var1);
}

