/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.Collation;
import com.mongodb.internal.operation.SyncOperations;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.concurrent.TimeUnit;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class DistinctIterableImpl<TDocument, TResult>
extends MongoIterableImpl<TResult>
implements DistinctIterable<TResult> {
    private final SyncOperations<TDocument> operations;
    private final Class<TResult> resultClass;
    private final String fieldName;
    private Bson filter;
    private long maxTimeMS;
    private Collation collation;

    DistinctIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter) {
        this(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, true);
    }

    DistinctIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
        super(clientSession, executor, readConcern, readPreference, retryReads);
        this.operations = new SyncOperations<TDocument>(namespace, documentClass, readPreference, codecRegistry, retryReads);
        this.resultClass = Assertions.notNull("resultClass", resultClass);
        this.fieldName = Assertions.notNull("mapFunction", fieldName);
        this.filter = filter;
    }

    @Override
    public DistinctIterable<TResult> filter(@Nullable Bson filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public DistinctIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public DistinctIterable<TResult> batchSize(int batchSize) {
        super.batchSize(batchSize);
        return this;
    }

    @Override
    public DistinctIterable<TResult> collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public ReadOperation<BatchCursor<TResult>> asReadOperation() {
        return this.operations.distinct(this.fieldName, this.filter, this.resultClass, this.maxTimeMS, this.collation);
    }
}

