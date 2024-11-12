/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.MongoMappingCursor;
import com.mongodb.lang.Nullable;
import java.util.Collection;

class MappingIterable<U, V>
implements MongoIterable<V> {
    private final MongoIterable<U> iterable;
    private final Function<U, V> mapper;

    MappingIterable(MongoIterable<U> iterable, Function<U, V> mapper) {
        this.iterable = iterable;
        this.mapper = mapper;
    }

    @Override
    public MongoCursor<V> iterator() {
        return new MongoMappingCursor<U, V>(this.iterable.iterator(), this.mapper);
    }

    @Override
    public MongoCursor<V> cursor() {
        return this.iterator();
    }

    @Override
    @Nullable
    public V first() {
        U first = this.iterable.first();
        if (first == null) {
            return null;
        }
        return this.mapper.apply(first);
    }

    @Override
    public void forEach(final Block<? super V> block) {
        this.iterable.forEach(new Block<U>(){

            @Override
            public void apply(U document) {
                block.apply(MappingIterable.this.mapper.apply(document));
            }
        });
    }

    @Override
    public <A extends Collection<? super V>> A into(final A target) {
        this.forEach(new Block<V>(){

            @Override
            public void apply(V v2) {
                target.add(v2);
            }
        });
        return target;
    }

    public MappingIterable<U, V> batchSize(int batchSize) {
        this.iterable.batchSize(batchSize);
        return this;
    }

    @Override
    public <W> MongoIterable<W> map(Function<V, W> newMap) {
        return new MappingIterable<V, W>(this, newMap);
    }

    MongoIterable<U> getMapped() {
        return this.iterable;
    }
}

