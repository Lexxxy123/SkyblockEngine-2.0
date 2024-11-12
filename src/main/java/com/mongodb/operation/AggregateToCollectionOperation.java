/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.AggregateExplainOperation;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;

@Deprecated
public class AggregateToCollectionOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final MongoNamespace namespace;
    private final List<BsonDocument> pipeline;
    private final WriteConcern writeConcern;
    private final ReadConcern readConcern;
    private final AggregationLevel aggregationLevel;
    private Boolean allowDiskUse;
    private long maxTimeMS;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private String comment;
    private BsonDocument hint;

    @Deprecated
    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline) {
        this(namespace, pipeline, null, null, AggregationLevel.COLLECTION);
    }

    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, WriteConcern writeConcern) {
        this(namespace, pipeline, null, writeConcern, AggregationLevel.COLLECTION);
    }

    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, ReadConcern readConcern) {
        this(namespace, pipeline, readConcern, null, AggregationLevel.COLLECTION);
    }

    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, ReadConcern readConcern, WriteConcern writeConcern) {
        this(namespace, pipeline, readConcern, writeConcern, AggregationLevel.COLLECTION);
    }

    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, WriteConcern writeConcern, AggregationLevel aggregationLevel) {
        this(namespace, pipeline, ReadConcern.DEFAULT, writeConcern, aggregationLevel);
    }

    public AggregateToCollectionOperation(MongoNamespace namespace, List<BsonDocument> pipeline, ReadConcern readConcern, WriteConcern writeConcern, AggregationLevel aggregationLevel) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.writeConcern = writeConcern;
        this.readConcern = readConcern;
        this.aggregationLevel = Assertions.notNull("aggregationLevel", aggregationLevel);
        Assertions.isTrueArgument("pipeline is not empty", !pipeline.isEmpty());
    }

    public List<BsonDocument> getPipeline() {
        return this.pipeline;
    }

    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }

    public AggregateToCollectionOperation allowDiskUse(Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public AggregateToCollectionOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public AggregateToCollectionOperation bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public AggregateToCollectionOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public String getComment() {
        return this.comment;
    }

    public AggregateToCollectionOperation comment(String comment) {
        this.comment = comment;
        return this;
    }

    public BsonDocument getHint() {
        return this.hint;
    }

    public AggregateToCollectionOperation hint(BsonDocument hint) {
        this.hint = hint;
        return this;
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        return new AggregateExplainOperation(this.namespace, this.pipeline).allowDiskUse(this.allowDiskUse).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS).hint(this.hint);
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                OperationHelper.validateCollation(connection, AggregateToCollectionOperation.this.collation);
                return CommandOperationHelper.executeCommand(binding, AggregateToCollectionOperation.this.namespace.getDatabaseName(), AggregateToCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
            }
        });
    }

    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, connection);
                    OperationHelper.validateCollation(connection, AggregateToCollectionOperation.this.collation, new OperationHelper.AsyncCallableWithConnection(){

                        @Override
                        public void call(AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                CommandOperationHelper.executeCommandAsync(binding, AggregateToCollectionOperation.this.namespace.getDatabaseName(), AggregateToCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), wrappedCallback);
                            }
                        }
                    });
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonValue aggregationTarget = this.aggregationLevel == AggregationLevel.DATABASE ? new BsonInt32(1) : new BsonString(this.namespace.getCollectionName());
        BsonDocument commandDocument = new BsonDocument("aggregate", aggregationTarget);
        commandDocument.put("pipeline", new BsonArray(this.pipeline));
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
        }
        if (this.bypassDocumentValidation != null && ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(description)) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        if (ServerVersionHelper.serverIsAtLeastVersionThreeDotSix(description)) {
            commandDocument.put("cursor", new BsonDocument());
        }
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        if (this.readConcern != null && !this.readConcern.isServerDefault() && ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(description)) {
            commandDocument.put("readConcern", this.readConcern.asDocument());
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
}

