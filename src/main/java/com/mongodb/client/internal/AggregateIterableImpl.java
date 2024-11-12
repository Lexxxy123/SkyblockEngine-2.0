/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.FindOptions;
import com.mongodb.internal.operation.SyncOperations;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class AggregateIterableImpl<TDocument, TResult>
extends MongoIterableImpl<TResult>
implements AggregateIterable<TResult> {
    private SyncOperations<TDocument> operations;
    private final MongoNamespace namespace;
    private final Class<TDocument> documentClass;
    private final Class<TResult> resultClass;
    private final CodecRegistry codecRegistry;
    private final List<? extends Bson> pipeline;
    private final AggregationLevel aggregationLevel;
    private Boolean allowDiskUse;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private Boolean useCursor;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private String comment;
    private Bson hint;

    AggregateIterableImpl(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel) {
        this(clientSession, new MongoNamespace(databaseName, "ignored"), documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, true);
    }

    AggregateIterableImpl(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        this(clientSession, new MongoNamespace(databaseName, "ignored"), documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    AggregateIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        super(clientSession, executor, readConcern, readPreference, retryReads);
        this.operations = new SyncOperations<TDocument>(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, true, retryReads);
        this.namespace = Assertions.notNull("namespace", namespace);
        this.documentClass = Assertions.notNull("documentClass", documentClass);
        this.resultClass = Assertions.notNull("resultClass", resultClass);
        this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.aggregationLevel = Assertions.notNull("aggregationLevel", aggregationLevel);
    }

    @Override
    public void toCollection() {
        if (this.getOutNamespace() == null) {
            throw new IllegalStateException("The last stage of the aggregation pipeline must be $out or $merge");
        }
        this.getExecutor().execute(this.operations.aggregateToCollection(this.pipeline, this.maxTimeMS, this.allowDiskUse, this.bypassDocumentValidation, this.collation, this.hint, this.comment, this.aggregationLevel), this.getReadConcern(), this.getClientSession());
    }

    @Override
    public AggregateIterable<TResult> allowDiskUse(@Nullable Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    @Override
    public AggregateIterable<TResult> batchSize(int batchSize) {
        super.batchSize(batchSize);
        return this;
    }

    @Override
    public AggregateIterable<TResult> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public AggregateIterable<TResult> useCursor(@Nullable Boolean useCursor) {
        this.useCursor = useCursor;
        return this;
    }

    @Override
    public AggregateIterable<TResult> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    @Override
    public AggregateIterable<TResult> bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Override
    public AggregateIterable<TResult> collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public AggregateIterable<TResult> comment(@Nullable String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public AggregateIterable<TResult> hint(@Nullable Bson hint) {
        this.hint = hint;
        return this;
    }

    @Override
    public ReadOperation<BatchCursor<TResult>> asReadOperation() {
        MongoNamespace outNamespace = this.getOutNamespace();
        if (outNamespace != null) {
            this.getExecutor().execute(this.operations.aggregateToCollection(this.pipeline, this.maxTimeMS, this.allowDiskUse, this.bypassDocumentValidation, this.collation, this.hint, this.comment, this.aggregationLevel), this.getReadConcern(), this.getClientSession());
            FindOptions findOptions = new FindOptions().collation(this.collation);
            Integer batchSize = this.getBatchSize();
            if (batchSize != null) {
                findOptions.batchSize(batchSize);
            }
            return this.operations.find(outNamespace, new BsonDocument(), this.resultClass, findOptions);
        }
        return this.operations.aggregate(this.pipeline, this.resultClass, this.maxTimeMS, this.maxAwaitTimeMS, this.getBatchSize(), this.collation, this.hint, this.comment, this.allowDiskUse, this.useCursor, this.aggregationLevel);
    }

    @Nullable
    private MongoNamespace getOutNamespace() {
        if (this.pipeline.size() == 0) {
            return null;
        }
        Bson lastStage = Assertions.notNull("last stage", this.pipeline.get(this.pipeline.size() - 1));
        BsonDocument lastStageDocument = lastStage.toBsonDocument(this.documentClass, this.codecRegistry);
        if (lastStageDocument.containsKey("$out")) {
            return new MongoNamespace(this.namespace.getDatabaseName(), lastStageDocument.getString("$out").getValue());
        }
        if (lastStageDocument.containsKey("$merge")) {
            if (lastStageDocument.isString("$merge")) {
                return new MongoNamespace(this.namespace.getDatabaseName(), lastStageDocument.getString("$merge").getValue());
            }
            if (lastStageDocument.isDocument("$merge")) {
                BsonDocument mergeDocument = lastStageDocument.getDocument("$merge");
                if (mergeDocument.isDocument("into")) {
                    BsonDocument intoDocument = mergeDocument.getDocument("into");
                    return new MongoNamespace(intoDocument.getString("db", new BsonString(this.namespace.getDatabaseName())).getValue(), intoDocument.getString("coll").getValue());
                }
                if (mergeDocument.isString("into")) {
                    return new MongoNamespace(this.namespace.getDatabaseName(), mergeDocument.getString("into").getValue());
                }
            } else {
                throw new IllegalStateException("Cannot return a cursor when the value for $merge stage is not a string or a document");
            }
        }
        return null;
    }
}

