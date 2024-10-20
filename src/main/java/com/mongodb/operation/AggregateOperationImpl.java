/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.AggregateResponseBatchCursor;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.Decoder;

class AggregateOperationImpl<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private static final String RESULT = "result";
    private static final String CURSOR = "cursor";
    private static final String FIRST_BATCH = "firstBatch";
    private static final List<String> FIELD_NAMES_WITH_RESULT = Arrays.asList("result", "firstBatch");
    private final MongoNamespace namespace;
    private final List<BsonDocument> pipeline;
    private final Decoder<T> decoder;
    private final AggregateTarget aggregateTarget;
    private final PipelineCreator pipelineCreator;
    private boolean retryReads;
    private Boolean allowDiskUse;
    private Integer batchSize;
    private Collation collation;
    private String comment;
    private BsonValue hint;
    private long maxAwaitTimeMS;
    private long maxTimeMS;
    private Boolean useCursor;

    AggregateOperationImpl(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregationLevel aggregationLevel) {
        this(namespace, pipeline, decoder, AggregateOperationImpl.defaultAggregateTarget(Assertions.notNull("aggregationLevel", aggregationLevel), Assertions.notNull("namespace", namespace).getCollectionName()), AggregateOperationImpl.defaultPipelineCreator(pipeline));
    }

    AggregateOperationImpl(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregateTarget aggregateTarget, PipelineCreator pipelineCreator) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.decoder = Assertions.notNull("decoder", decoder);
        this.aggregateTarget = Assertions.notNull("aggregateTarget", aggregateTarget);
        this.pipelineCreator = Assertions.notNull("pipelineCreator", pipelineCreator);
    }

    MongoNamespace getNamespace() {
        return this.namespace;
    }

    List<BsonDocument> getPipeline() {
        return this.pipeline;
    }

    Decoder<T> getDecoder() {
        return this.decoder;
    }

    Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }

    AggregateOperationImpl<T> allowDiskUse(Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    Integer getBatchSize() {
        return this.batchSize;
    }

    AggregateOperationImpl<T> batchSize(Integer batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    long getMaxAwaitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }

    AggregateOperationImpl<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    AggregateOperationImpl<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    Boolean getUseCursor() {
        return this.useCursor;
    }

    AggregateOperationImpl<T> useCursor(Boolean useCursor) {
        this.useCursor = useCursor;
        return this;
    }

    Collation getCollation() {
        return this.collation;
    }

    AggregateOperationImpl<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    String getComment() {
        return this.comment;
    }

    AggregateOperationImpl<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    AggregateOperationImpl<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    boolean getRetryReads() {
        return this.retryReads;
    }

    BsonValue getHint() {
        return this.hint;
    }

    AggregateOperationImpl<T> hint(BsonValue hint) {
        Assertions.isTrueArgument("BsonString or BsonDocument", hint == null || hint.isDocument() || hint.isString());
        this.hint = hint;
        return this;
    }

    @Override
    public BatchCursor<T> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, FIELD_NAMES_WITH_RESULT), this.transformer(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
        SingleResultCallback<AsyncBatchCursor<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, FIELD_NAMES_WITH_RESULT), this.asyncTransformer(), this.retryReads, errHandlingCallback);
    }

    private boolean isInline(ConnectionDescription description) {
        return !ServerVersionHelper.serverIsAtLeastVersionThreeDotSix(description) && this.useCursor != null && this.useCursor == false;
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcernAndCollation(connectionDescription, sessionContext.getReadConcern(), AggregateOperationImpl.this.collation);
                return AggregateOperationImpl.this.getCommand(connectionDescription, sessionContext);
            }
        };
    }

    private BsonDocument getCommand(ConnectionDescription description, SessionContext sessionContext) {
        BsonDocument commandDocument = new BsonDocument("aggregate", this.aggregateTarget.create());
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, commandDocument);
        commandDocument.put("pipeline", this.pipelineCreator.create(description, sessionContext));
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", this.maxTimeMS > Integer.MAX_VALUE ? new BsonInt64(this.maxTimeMS) : new BsonInt32((int)this.maxTimeMS));
        }
        if (!this.isInline(description)) {
            BsonDocument cursor = new BsonDocument();
            if (this.batchSize != null) {
                cursor.put("batchSize", new BsonInt32(this.batchSize));
            }
            commandDocument.put(CURSOR, cursor);
        }
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        if (this.comment != null) {
            commandDocument.put("comment", new BsonString(this.comment));
        }
        if (this.hint != null) {
            commandDocument.put("hint", this.hint);
        }
        return commandDocument;
    }

    private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
        if (!this.isInline(description) || result.containsKey(CURSOR)) {
            return OperationHelper.cursorDocumentToQueryResult(result.getDocument(CURSOR), description.getServerAddress());
        }
        return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, RESULT), 0L, description.getServerAddress());
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, AggregateResponseBatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, AggregateResponseBatchCursor<T>>(){

            @Override
            public AggregateResponseBatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                QueryResult queryResult = AggregateOperationImpl.this.createQueryResult(result, connection.getDescription());
                return new QueryBatchCursor(queryResult, 0, AggregateOperationImpl.this.batchSize != null ? AggregateOperationImpl.this.batchSize : 0, AggregateOperationImpl.this.maxAwaitTimeMS, AggregateOperationImpl.this.decoder, source, connection, result);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                QueryResult queryResult = AggregateOperationImpl.this.createQueryResult(result, connection.getDescription());
                return new AsyncQueryBatchCursor(queryResult, 0, AggregateOperationImpl.this.batchSize != null ? AggregateOperationImpl.this.batchSize : 0, AggregateOperationImpl.this.maxAwaitTimeMS, AggregateOperationImpl.this.decoder, source, connection, result);
            }
        };
    }

    private static AggregateTarget defaultAggregateTarget(final AggregationLevel aggregationLevel, final String collectionName) {
        return new AggregateTarget(){

            @Override
            public BsonValue create() {
                if (aggregationLevel == AggregationLevel.DATABASE) {
                    return new BsonInt32(1);
                }
                return new BsonString(collectionName);
            }
        };
    }

    private static PipelineCreator defaultPipelineCreator(final List<BsonDocument> pipeline) {
        return new PipelineCreator(){

            @Override
            public BsonArray create(ConnectionDescription connectionDescription, SessionContext sessionContext) {
                return new BsonArray(pipeline);
            }
        };
    }

    static interface PipelineCreator {
        public BsonArray create(ConnectionDescription var1, SessionContext var2);
    }

    static interface AggregateTarget {
        public BsonValue create();
    }
}

