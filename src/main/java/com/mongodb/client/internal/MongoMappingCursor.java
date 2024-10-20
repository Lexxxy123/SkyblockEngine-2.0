/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.Function;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.MongoCursor;
import com.mongodb.lang.Nullable;

class MongoMappingCursor<T, U>
implements MongoCursor<U> {
    private final MongoCursor<T> proxied;
    private final Function<T, U> mapper;

    MongoMappingCursor(MongoCursor<T> proxied, Function<T, U> mapper) {
        this.proxied = Assertions.notNull("proxied", proxied);
        this.mapper = Assertions.notNull("mapper", mapper);
    }

    @Override
    public void close() {
        this.proxied.close();
    }

    @Override
    public boolean hasNext() {
        return this.proxied.hasNext();
    }

    @Override
    public U next() {
        return this.mapper.apply(this.proxied.next());
    }

    @Override
    @Nullable
    public U tryNext() {
        T proxiedNext = this.proxied.tryNext();
        if (proxiedNext == null) {
            return null;
        }
        return this.mapper.apply(proxiedNext);
    }

    @Override
    public void remove() {
        this.proxied.remove();
    }

    @Override
    @Nullable
    public ServerCursor getServerCursor() {
        return this.proxied.getServerCursor();
    }

    @Override
    public ServerAddress getServerAddress() {
        return this.proxied.getServerAddress();
    }
}

