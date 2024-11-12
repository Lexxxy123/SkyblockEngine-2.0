/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.MappingIterable;
import com.mongodb.client.internal.MongoBatchCursorAdapter;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.Collection;
import java.util.Iterator;

public abstract class MongoIterableImpl<TResult>
implements MongoIterable<TResult> {
    private final ClientSession clientSession;
    private final ReadConcern readConcern;
    private final OperationExecutor executor;
    private final ReadPreference readPreference;
    private final boolean retryReads;
    private Integer batchSize;

    public MongoIterableImpl(@Nullable ClientSession clientSession, OperationExecutor executor, ReadConcern readConcern, ReadPreference readPreference, boolean retryReads) {
        this.clientSession = clientSession;
        this.executor = Assertions.notNull("executor", executor);
        this.readConcern = Assertions.notNull("readConcern", readConcern);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.retryReads = Assertions.notNull("retryReads", retryReads);
    }

    public abstract ReadOperation<BatchCursor<TResult>> asReadOperation();

    @Nullable
    ClientSession getClientSession() {
        return this.clientSession;
    }

    OperationExecutor getExecutor() {
        return this.executor;
    }

    ReadPreference getReadPreference() {
        return this.readPreference;
    }

    protected ReadConcern getReadConcern() {
        return this.readConcern;
    }

    protected boolean getRetryReads() {
        return this.retryReads;
    }

    @Nullable
    public Integer getBatchSize() {
        return this.batchSize;
    }

    @Override
    public MongoIterable<TResult> batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public MongoCursor<TResult> iterator() {
        return new MongoBatchCursorAdapter<TResult>(this.execute());
    }

    @Override
    public MongoCursor<TResult> cursor() {
        return this.iterator();
    }

    @Override
    @Nullable
    public TResult first() {
        Iterator cursor = this.iterator();
        try {
            if (!cursor.hasNext()) {
                TResult TResult = null;
                return TResult;
            }
            Object TResult = cursor.next();
            return TResult;
        } finally {
            cursor.close();
        }
    }

    @Override
    public <U> MongoIterable<U> map(Function<TResult, U> mapper) {
        return new MappingIterable<TResult, U>(this, mapper);
    }

    @Override
    public void forEach(Block<? super TResult> block) {
        Iterator cursor = this.iterator();
        try {
            while (cursor.hasNext()) {
                block.apply(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public <A extends Collection<? super TResult>> A into(final A target) {
        this.forEach(new Block<TResult>(){

            @Override
            public void apply(TResult t2) {
                target.add(t2);
            }
        });
        return target;
    }

    private BatchCursor<TResult> execute() {
        return this.executor.execute(this.asReadOperation(), this.readPreference, this.readConcern, this.clientSession);
    }
}

