/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.client.MongoIterable;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface ListDatabasesIterable<TResult>
extends MongoIterable<TResult> {
    public ListDatabasesIterable<TResult> maxTime(long var1, TimeUnit var3);

    @Override
    public ListDatabasesIterable<TResult> batchSize(int var1);

    public ListDatabasesIterable<TResult> filter(@Nullable Bson var1);

    public ListDatabasesIterable<TResult> nameOnly(@Nullable Boolean var1);
}

