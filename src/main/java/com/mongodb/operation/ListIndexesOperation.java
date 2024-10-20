/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
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
import com.mongodb.connection.ServerType;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.CursorHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;

@Deprecated
public class ListIndexesOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private int batchSize;
    private long maxTimeMS;

    public ListIndexesOperation(MongoNamespace namespace, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public Integer getBatchSize() {
        return this.batchSize;
    }

    public ListIndexesOperation<T> batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public ListIndexesOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public ListIndexesOperation<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return (BatchCursor)OperationHelper.withReadConnectionSource(binding, new OperationHelper.CallableWithSource<BatchCursor<T>>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public BatchCursor<T> call(ConnectionSource source) {
                Connection connection = source.getConnection();
                if (ServerVersionHelper.serverIsAtLeastVersionThreeDotZero(connection.getDescription())) {
                    try {
                        return (BatchCursor)CommandOperationHelper.executeCommandWithConnection(binding, source, ListIndexesOperation.this.namespace.getDatabaseName(), ListIndexesOperation.this.getCommandCreator(), ListIndexesOperation.this.createCommandDecoder(), ListIndexesOperation.this.transformer(), ListIndexesOperation.this.retryReads, connection);
                    } catch (MongoCommandException e2) {
                        return CommandOperationHelper.rethrowIfNotNamespaceError(e2, OperationHelper.createEmptyBatchCursor(ListIndexesOperation.this.namespace, ListIndexesOperation.this.decoder, source.getServerDescription().getAddress(), ListIndexesOperation.this.batchSize));
                    }
                }
                try {
                    QueryBatchCursor queryBatchCursor = new QueryBatchCursor(connection.query(ListIndexesOperation.this.getIndexNamespace(), ListIndexesOperation.this.asQueryDocument(connection.getDescription(), binding.getReadPreference()), null, 0, 0, ListIndexesOperation.this.batchSize, binding.getReadPreference().isSlaveOk(), false, false, false, false, false, ListIndexesOperation.this.decoder), 0, ListIndexesOperation.this.batchSize, ListIndexesOperation.this.decoder, source);
                    return queryBatchCursor;
                } finally {
                    connection.release();
                }
            }
        });
    }

    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        OperationHelper.withAsyncReadConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

            @Override
            public void call(final AsyncConnectionSource source, final AsyncConnection connection, Throwable t2) {
                final SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else if (ServerVersionHelper.serverIsAtLeastVersionThreeDotZero(connection.getDescription())) {
                    CommandOperationHelper.executeCommandAsyncWithConnection(binding, source, ListIndexesOperation.this.namespace.getDatabaseName(), ListIndexesOperation.this.getCommandCreator(), ListIndexesOperation.this.createCommandDecoder(), ListIndexesOperation.this.asyncTransformer(), ListIndexesOperation.this.retryReads, connection, new SingleResultCallback<AsyncBatchCursor<T>>(){

                        @Override
                        public void onResult(AsyncBatchCursor<T> result, Throwable t2) {
                            if (t2 != null && !CommandOperationHelper.isNamespaceError(t2)) {
                                errHandlingCallback.onResult(null, t2);
                            } else {
                                errHandlingCallback.onResult(result != null ? result : ListIndexesOperation.this.emptyAsyncCursor(source), null);
                            }
                        }
                    });
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, source, connection);
                    connection.queryAsync(ListIndexesOperation.this.getIndexNamespace(), ListIndexesOperation.this.asQueryDocument(connection.getDescription(), binding.getReadPreference()), null, 0, 0, ListIndexesOperation.this.batchSize, binding.getReadPreference().isSlaveOk(), false, false, false, false, false, ListIndexesOperation.this.decoder, new SingleResultCallback<QueryResult<T>>(){

                        @Override
                        public void onResult(QueryResult<T> result, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                wrappedCallback.onResult(new AsyncQueryBatchCursor(result, 0, ListIndexesOperation.this.batchSize, 0L, ListIndexesOperation.this.decoder, source, connection), null);
                            }
                        }
                    });
                }
            }
        });
    }

    private AsyncBatchCursor<T> emptyAsyncCursor(AsyncConnectionSource source) {
        return OperationHelper.createEmptyAsyncBatchCursor(this.namespace, source.getServerDescription().getAddress());
    }

    private BsonDocument asQueryDocument(ConnectionDescription connectionDescription, ReadPreference readPreference) {
        BsonDocument document = new BsonDocument("$query", new BsonDocument("ns", new BsonString(this.namespace.getFullName())));
        if (this.maxTimeMS > 0L) {
            document.put("$maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (connectionDescription.getServerType() == ServerType.SHARD_ROUTER && !readPreference.equals(ReadPreference.primary())) {
            document.put("$readPreference", readPreference.toDocument());
        }
        return document;
    }

    private MongoNamespace getIndexNamespace() {
        return new MongoNamespace(this.namespace.getDatabaseName(), "system.indexes");
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return ListIndexesOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        BsonDocument command = new BsonDocument("listIndexes", new BsonString(this.namespace.getCollectionName())).append("cursor", CursorHelper.getCursorDocumentFromBatchSize(this.batchSize == 0 ? null : Integer.valueOf(this.batchSize)));
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        return command;
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>(){

            @Override
            public BatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return OperationHelper.cursorDocumentToBatchCursor(result.getDocument("cursor"), ListIndexesOperation.this.decoder, source, ListIndexesOperation.this.batchSize);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return OperationHelper.cursorDocumentToAsyncBatchCursor(result.getDocument("cursor"), ListIndexesOperation.this.decoder, source, connection, ListIndexesOperation.this.batchSize);
            }
        };
    }

    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, "firstBatch");
    }
}

