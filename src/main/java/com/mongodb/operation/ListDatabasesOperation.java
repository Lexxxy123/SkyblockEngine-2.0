/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.concurrent.TimeUnit;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.codecs.Decoder;

@Deprecated
public class ListDatabasesOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private final Decoder<T> decoder;
    private boolean retryReads;
    private long maxTimeMS;
    private BsonDocument filter;
    private Boolean nameOnly;

    public ListDatabasesOperation(Decoder<T> decoder) {
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public ListDatabasesOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public ListDatabasesOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public ListDatabasesOperation<T> nameOnly(Boolean nameOnly) {
        this.nameOnly = nameOnly;
        return this;
    }

    public ListDatabasesOperation<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    public Boolean getNameOnly() {
        return this.nameOnly;
    }

    @Override
    public BatchCursor<T> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), this.transformer(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
        CommandOperationHelper.executeCommandAsync(binding, "admin", this.getCommandCreator(), CommandResultDocumentCodec.create(this.decoder, "databases"), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>(){

            @Override
            public BatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return new QueryBatchCursor(ListDatabasesOperation.this.createQueryResult(result, connection.getDescription()), 0, 0, ListDatabasesOperation.this.decoder, source);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return new AsyncQueryBatchCursor(ListDatabasesOperation.this.createQueryResult(result, connection.getDescription()), 0, 0, 0L, ListDatabasesOperation.this.decoder, source, connection, result);
            }
        };
    }

    private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
        return new QueryResult(null, BsonDocumentWrapperHelper.toList(result, "databases"), 0L, description.getServerAddress());
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return ListDatabasesOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        BsonDocument command = new BsonDocument("listDatabases", new BsonInt32(1));
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.filter != null) {
            command.put("filter", this.filter);
        }
        if (this.nameOnly != null) {
            command.put("nameOnly", new BsonBoolean(this.nameOnly));
        }
        return command;
    }
}

