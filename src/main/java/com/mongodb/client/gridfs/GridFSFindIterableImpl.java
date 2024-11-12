/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

class GridFSFindIterableImpl
implements GridFSFindIterable {
    private final FindIterable<GridFSFile> underlying;

    GridFSFindIterableImpl(FindIterable<GridFSFile> underlying) {
        this.underlying = underlying;
    }

    @Override
    public GridFSFindIterable sort(@Nullable Bson sort) {
        this.underlying.sort(sort);
        return this;
    }

    @Override
    public GridFSFindIterable skip(int skip) {
        this.underlying.skip(skip);
        return this;
    }

    @Override
    public GridFSFindIterable limit(int limit) {
        this.underlying.limit(limit);
        return this;
    }

    @Override
    public GridFSFindIterable filter(@Nullable Bson filter) {
        this.underlying.filter(filter);
        return this;
    }

    @Override
    public GridFSFindIterable maxTime(long maxTime, TimeUnit timeUnit) {
        this.underlying.maxTime(maxTime, timeUnit);
        return this;
    }

    @Override
    public GridFSFindIterable batchSize(int batchSize) {
        this.underlying.batchSize(batchSize);
        return this;
    }

    @Override
    public GridFSFindIterable collation(@Nullable Collation collation) {
        this.underlying.collation(collation);
        return this;
    }

    @Override
    public GridFSFindIterable noCursorTimeout(boolean noCursorTimeout) {
        this.underlying.noCursorTimeout(noCursorTimeout);
        return this;
    }

    @Override
    public MongoCursor<GridFSFile> iterator() {
        return this.underlying.iterator();
    }

    @Override
    public MongoCursor<GridFSFile> cursor() {
        return this.iterator();
    }

    @Override
    @Nullable
    public GridFSFile first() {
        return (GridFSFile)this.underlying.first();
    }

    @Override
    public <U> MongoIterable<U> map(Function<GridFSFile, U> mapper) {
        return this.underlying.map(mapper);
    }

    @Override
    public void forEach(Block<? super GridFSFile> block) {
        this.underlying.forEach(block);
    }

    @Override
    public <A extends Collection<? super GridFSFile>> A into(A target) {
        return this.underlying.into(target);
    }
}

