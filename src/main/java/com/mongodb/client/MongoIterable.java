/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.client.MongoCursor;
import com.mongodb.lang.Nullable;
import java.util.Collection;

public interface MongoIterable<TResult>
extends Iterable<TResult> {
    @Override
    public MongoCursor<TResult> iterator();

    public MongoCursor<TResult> cursor();

    @Nullable
    public TResult first();

    public <U> MongoIterable<U> map(Function<TResult, U> var1);

    @Override
    @Deprecated
    public void forEach(Block<? super TResult> var1);

    public <A extends Collection<? super TResult>> A into(A var1);

    public MongoIterable<TResult> batchSize(int var1);
}

