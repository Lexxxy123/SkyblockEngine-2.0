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
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;

@Deprecated
public class DistinctOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private static final String VALUES = "values";
    private final MongoNamespace namespace;
    private final String fieldName;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private long maxTimeMS;
    private Collation collation;

    public DistinctOperation(MongoNamespace namespace, String fieldName, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.fieldName = Assertions.notNull("fieldName", fieldName);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public DistinctOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public DistinctOperation<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public DistinctOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public DistinctOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public BatchCursor<T> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), this.transformer(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), this.createCommandDecoder(), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, VALUES);
    }

    private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
        return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, VALUES), 0L, description.getServerAddress());
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>(){

            @Override
            public BatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                QueryResult queryResult = DistinctOperation.this.createQueryResult(result, connection.getDescription());
                return new QueryBatchCursor(queryResult, 0, 0, DistinctOperation.this.decoder, source);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                QueryResult queryResult = DistinctOperation.this.createQueryResult(result, connection.getDescription());
                return new AsyncSingleBatchQueryCursor(queryResult);
            }
        };
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcernAndCollation(connectionDescription, sessionContext.getReadConcern(), DistinctOperation.this.collation);
                return DistinctOperation.this.getCommand(sessionContext);
            }
        };
    }

    private BsonDocument getCommand(SessionContext sessionContext) {
        BsonDocument commandDocument = new BsonDocument("distinct", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, commandDocument);
        commandDocument.put("key", new BsonString(this.fieldName));
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "query", this.filter);
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.maxTimeMS);
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        return commandDocument;
    }
}

