/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.binding.ReadBinding;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.FindOptions;
import com.mongodb.client.model.MapReduceAction;
import com.mongodb.internal.operation.SyncOperations;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.MapReduceBatchCursor;
import com.mongodb.operation.MapReduceStatistics;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class MapReduceIterableImpl<TDocument, TResult>
extends MongoIterableImpl<TResult>
implements MapReduceIterable<TResult> {
    private final SyncOperations<TDocument> operations;
    private final MongoNamespace namespace;
    private final Class<TResult> resultClass;
    private final String mapFunction;
    private final String reduceFunction;
    private boolean inline = true;
    private String collectionName;
    private String finalizeFunction;
    private Bson scope;
    private Bson filter;
    private Bson sort;
    private int limit;
    private boolean jsMode;
    private boolean verbose = true;
    private long maxTimeMS;
    private MapReduceAction action = MapReduceAction.REPLACE;
    private String databaseName;
    private boolean sharded;
    private boolean nonAtomic;
    private Boolean bypassDocumentValidation;
    private Collation collation;

    MapReduceIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
        super(clientSession, executor, readConcern, readPreference, false);
        this.operations = new SyncOperations<TDocument>(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, false, false);
        this.namespace = Assertions.notNull("namespace", namespace);
        this.resultClass = Assertions.notNull("resultClass", resultClass);
        this.mapFunction = Assertions.notNull("mapFunction", mapFunction);
        this.reduceFunction = Assertions.notNull("reduceFunction", reduceFunction);
    }

    @Override
    public void toCollection() {
        if (this.inline) {
            throw new IllegalStateException("The options must specify a non-inline result");
        }
        this.getExecutor().execute(this.createMapReduceToCollectionOperation(), this.getReadConcern(), this.getClientSession());
    }

    @Override
    public MapReduceIterable<TResult> collectionName(String collectionName) {
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.inline = false;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> finalizeFunction(@Nullable String finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> scope(@Nullable Bson scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> sort(@Nullable Bson sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> filter(@Nullable Bson filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> jsMode(boolean jsMode) {
        this.jsMode = jsMode;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public MapReduceIterable<TResult> action(MapReduceAction action) {
        this.action = action;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> databaseName(@Nullable String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> sharded(boolean sharded) {
        this.sharded = sharded;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> nonAtomic(boolean nonAtomic) {
        this.nonAtomic = nonAtomic;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> batchSize(int batchSize) {
        super.batchSize(batchSize);
        return this;
    }

    @Override
    public MapReduceIterable<TResult> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Override
    public MapReduceIterable<TResult> collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    ReadPreference getReadPreference() {
        if (this.inline) {
            return super.getReadPreference();
        }
        return ReadPreference.primary();
    }

    @Override
    public ReadOperation<BatchCursor<TResult>> asReadOperation() {
        if (this.inline) {
            ReadOperation<MapReduceBatchCursor<TResult>> operation = this.operations.mapReduce(this.mapFunction, this.reduceFunction, this.finalizeFunction, this.resultClass, this.filter, this.limit, this.maxTimeMS, this.jsMode, this.scope, this.sort, this.verbose, this.collation);
            return new WrappedMapReduceReadOperation<TResult>(operation);
        }
        this.getExecutor().execute(this.createMapReduceToCollectionOperation(), this.getReadConcern(), this.getClientSession());
        String dbName = this.databaseName != null ? this.databaseName : this.namespace.getDatabaseName();
        FindOptions findOptions = new FindOptions().collation(this.collation);
        Integer batchSize = this.getBatchSize();
        if (batchSize != null) {
            findOptions.batchSize(batchSize);
        }
        return this.operations.find(new MongoNamespace(dbName, this.collectionName), new BsonDocument(), this.resultClass, findOptions);
    }

    private WriteOperation<MapReduceStatistics> createMapReduceToCollectionOperation() {
        return this.operations.mapReduceToCollection(this.databaseName, this.collectionName, this.mapFunction, this.reduceFunction, this.finalizeFunction, this.filter, this.limit, this.maxTimeMS, this.jsMode, this.scope, this.sort, this.verbose, this.action, this.nonAtomic, this.sharded, this.bypassDocumentValidation, this.collation);
    }

    static class WrappedMapReduceReadOperation<TResult>
    implements ReadOperation<BatchCursor<TResult>> {
        private final ReadOperation<MapReduceBatchCursor<TResult>> operation;

        ReadOperation<MapReduceBatchCursor<TResult>> getOperation() {
            return this.operation;
        }

        WrappedMapReduceReadOperation(ReadOperation<MapReduceBatchCursor<TResult>> operation) {
            this.operation = operation;
        }

        @Override
        public BatchCursor<TResult> execute(ReadBinding binding) {
            return this.operation.execute(binding);
        }
    }
}

