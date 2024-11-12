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
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncSingleBatchQueryCursor;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import org.bson.BsonDocument;
import org.bson.BsonJavaScript;
import org.bson.BsonString;
import org.bson.codecs.Decoder;

@Deprecated
public class GroupOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    private final BsonJavaScript reduceFunction;
    private final BsonDocument initial;
    private boolean retryReads;
    private BsonDocument key;
    private BsonJavaScript keyFunction;
    private BsonDocument filter;
    private BsonJavaScript finalizeFunction;
    private Collation collation;

    public GroupOperation(MongoNamespace namespace, BsonJavaScript reduceFunction, BsonDocument initial, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.reduceFunction = Assertions.notNull("reduceFunction", reduceFunction);
        this.initial = Assertions.notNull("initial", initial);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public Decoder<T> getDecoder() {
        return this.decoder;
    }

    public BsonDocument getKey() {
        return this.key;
    }

    public GroupOperation<T> key(BsonDocument key) {
        this.key = key;
        return this;
    }

    public BsonJavaScript getKeyFunction() {
        return this.keyFunction;
    }

    public GroupOperation<T> keyFunction(BsonJavaScript keyFunction) {
        this.keyFunction = keyFunction;
        return this;
    }

    public BsonDocument getInitial() {
        return this.initial;
    }

    public BsonJavaScript getReduceFunction() {
        return this.reduceFunction;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public GroupOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonJavaScript getFinalizeFunction() {
        return this.finalizeFunction;
    }

    public GroupOperation<T> finalizeFunction(BsonJavaScript finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public GroupOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public GroupOperation<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    @Override
    public BatchCursor<T> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "retval"), this.transformer(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "retval"), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateCollation(connectionDescription, GroupOperation.this.collation);
                return GroupOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        BsonDocument commandDocument = new BsonDocument("ns", new BsonString(this.namespace.getCollectionName()));
        if (this.getKey() != null) {
            commandDocument.put("key", this.getKey());
        } else if (this.getKeyFunction() != null) {
            commandDocument.put("$keyf", this.getKeyFunction());
        }
        commandDocument.put("initial", this.getInitial());
        commandDocument.put("$reduce", this.getReduceFunction());
        if (this.getFinalizeFunction() != null) {
            commandDocument.put("finalize", this.getFinalizeFunction());
        }
        if (this.getFilter() != null) {
            commandDocument.put("cond", this.getFilter());
        }
        if (this.getCollation() != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        return new BsonDocument("group", commandDocument);
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>(){

            @Override
            public BatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return new QueryBatchCursor(GroupOperation.this.createQueryResult(result, connection.getDescription()), 0, 0, GroupOperation.this.decoder, source);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return new AsyncSingleBatchQueryCursor(GroupOperation.this.createQueryResult(result, connection.getDescription()));
            }
        };
    }

    private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
        return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, "retval"), 0L, description.getServerAddress());
    }
}

