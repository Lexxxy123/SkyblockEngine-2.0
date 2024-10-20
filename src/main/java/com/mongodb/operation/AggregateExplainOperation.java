/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ReadBinding;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;

class AggregateExplainOperation
implements AsyncReadOperation<BsonDocument>,
ReadOperation<BsonDocument> {
    private final MongoNamespace namespace;
    private final List<BsonDocument> pipeline;
    private boolean retryReads;
    private Boolean allowDiskUse;
    private long maxTimeMS;
    private Collation collation;
    private BsonValue hint;

    AggregateExplainOperation(MongoNamespace namespace, List<BsonDocument> pipeline) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
    }

    public AggregateExplainOperation allowDiskUse(Boolean allowDiskUse) {
        this.allowDiskUse = allowDiskUse;
        return this;
    }

    public AggregateExplainOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public AggregateExplainOperation retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    public AggregateExplainOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public BsonDocument getHint() {
        if (this.hint == null) {
            return null;
        }
        if (!this.hint.isDocument()) {
            throw new IllegalArgumentException("Hint is not a BsonDocument please use the #getHintBsonValue() method. ");
        }
        return this.hint.asDocument();
    }

    public BsonValue getHintBsonValue() {
        return this.hint;
    }

    public AggregateExplainOperation hint(BsonValue hint) {
        Assertions.isTrueArgument("BsonString or BsonDocument", hint == null || hint.isDocument() || hint.isString());
        this.hint = hint;
        return this;
    }

    @Override
    public BsonDocument execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<BsonDocument> callback) {
        SingleResultCallback<BsonDocument> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(), new CommandOperationHelper.IdentityTransformerAsync(), this.retryReads, errHandlingCallback);
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateCollation(connectionDescription, AggregateExplainOperation.this.collation);
                return AggregateExplainOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        BsonDocument commandDocument = new BsonDocument("aggregate", new BsonString(this.namespace.getCollectionName()));
        commandDocument.put("pipeline", new BsonArray(this.pipeline));
        commandDocument.put("explain", BsonBoolean.TRUE);
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.allowDiskUse != null) {
            commandDocument.put("allowDiskUse", BsonBoolean.valueOf(this.allowDiskUse));
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        if (this.hint != null) {
            commandDocument.put("hint", this.hint);
        }
        return commandDocument;
    }
}

