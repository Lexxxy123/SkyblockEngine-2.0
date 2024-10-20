/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.internal.operation.SyncOperations;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class ListCollectionsIterableImpl<TResult>
extends MongoIterableImpl<TResult>
implements ListCollectionsIterable<TResult> {
    private final SyncOperations<BsonDocument> operations;
    private final String databaseName;
    private final Class<TResult> resultClass;
    private Bson filter;
    private final boolean collectionNamesOnly;
    private long maxTimeMS;

    ListCollectionsIterableImpl(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor) {
        this(clientSession, databaseName, collectionNamesOnly, resultClass, codecRegistry, readPreference, executor, true);
    }

    ListCollectionsIterableImpl(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        super(clientSession, executor, ReadConcern.DEFAULT, readPreference, retryReads);
        this.collectionNamesOnly = collectionNamesOnly;
        this.operations = new SyncOperations<BsonDocument>(BsonDocument.class, readPreference, codecRegistry, retryReads);
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.resultClass = Assertions.notNull("resultClass", resultClass);
    }

    @Override
    public ListCollectionsIterable<TResult> filter(@Nullable Bson filter) {
        Assertions.notNull("filter", filter);
        this.filter = filter;
        return this;
    }

    @Override
    public ListCollectionsIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public ListCollectionsIterable<TResult> batchSize(int batchSize) {
        super.batchSize(batchSize);
        return this;
    }

    @Override
    public ReadOperation<BatchCursor<TResult>> asReadOperation() {
        return this.operations.listCollections(this.databaseName, this.resultClass, this.filter, this.collectionNamesOnly, this.getBatchSize(), this.maxTimeMS);
    }
}

