/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonRegularExpression;
import org.bson.BsonString;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

@Deprecated
public class ListCollectionsOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private final String databaseName;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private int batchSize;
    private long maxTimeMS;
    private boolean nameOnly;

    public ListCollectionsOperation(String databaseName, Decoder<T> decoder) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public boolean isNameOnly() {
        return this.nameOnly;
    }

    public ListCollectionsOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public ListCollectionsOperation<T> nameOnly(boolean nameOnly) {
        this.nameOnly = nameOnly;
        return this;
    }

    public Integer getBatchSize() {
        return this.batchSize;
    }

    public ListCollectionsOperation<T> batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public ListCollectionsOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public ListCollectionsOperation<T> retryReads(boolean retryReads) {
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
                        return (BatchCursor)CommandOperationHelper.executeCommandWithConnection(binding, source, ListCollectionsOperation.this.databaseName, ListCollectionsOperation.this.getCommandCreator(), ListCollectionsOperation.this.createCommandDecoder(), ListCollectionsOperation.this.commandTransformer(), ListCollectionsOperation.this.retryReads, connection);
                    } catch (MongoCommandException e2) {
                        return CommandOperationHelper.rethrowIfNotNamespaceError(e2, OperationHelper.createEmptyBatchCursor(ListCollectionsOperation.this.createNamespace(), ListCollectionsOperation.this.decoder, source.getServerDescription().getAddress(), ListCollectionsOperation.this.batchSize));
                    }
                }
                try {
                    ProjectingBatchCursor projectingBatchCursor = new ProjectingBatchCursor(new QueryBatchCursor<BsonDocument>(connection.query(ListCollectionsOperation.this.getNamespace(), ListCollectionsOperation.this.asQueryDocument(connection.getDescription(), binding.getReadPreference()), null, 0, 0, ListCollectionsOperation.this.batchSize, binding.getReadPreference().isSlaveOk(), false, false, false, false, false, new BsonDocumentCodec()), 0, ListCollectionsOperation.this.batchSize, new BsonDocumentCodec(), source));
                    return projectingBatchCursor;
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
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, source, connection);
                    if (ServerVersionHelper.serverIsAtLeastVersionThreeDotZero(connection.getDescription())) {
                        CommandOperationHelper.executeCommandAsync(binding, ListCollectionsOperation.this.databaseName, ListCollectionsOperation.this.getCommandCreator(), ListCollectionsOperation.this.createCommandDecoder(), ListCollectionsOperation.this.asyncTransformer(), ListCollectionsOperation.this.retryReads, new SingleResultCallback<AsyncBatchCursor<T>>(){

                            @Override
                            public void onResult(AsyncBatchCursor<T> result, Throwable t2) {
                                if (t2 != null && !CommandOperationHelper.isNamespaceError(t2)) {
                                    wrappedCallback.onResult(null, t2);
                                } else {
                                    wrappedCallback.onResult(result != null ? result : ListCollectionsOperation.this.emptyAsyncCursor(source), null);
                                }
                            }
                        });
                    } else {
                        connection.queryAsync(ListCollectionsOperation.this.getNamespace(), ListCollectionsOperation.this.asQueryDocument(connection.getDescription(), binding.getReadPreference()), null, 0, 0, ListCollectionsOperation.this.batchSize, binding.getReadPreference().isSlaveOk(), false, false, false, false, false, new BsonDocumentCodec(), new SingleResultCallback<QueryResult<BsonDocument>>(){

                            @Override
                            public void onResult(QueryResult<BsonDocument> result, Throwable t2) {
                                if (t2 != null) {
                                    wrappedCallback.onResult(null, t2);
                                } else {
                                    wrappedCallback.onResult(new ProjectingAsyncBatchCursor(new AsyncQueryBatchCursor<BsonDocument>(result, 0, ListCollectionsOperation.this.batchSize, 0L, new BsonDocumentCodec(), source, connection)), null);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private AsyncBatchCursor<T> emptyAsyncCursor(AsyncConnectionSource source) {
        return OperationHelper.createEmptyAsyncBatchCursor(this.createNamespace(), source.getServerDescription().getAddress());
    }

    private MongoNamespace createNamespace() {
        return new MongoNamespace(this.databaseName, "$cmd.listCollections");
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return OperationHelper.cursorDocumentToAsyncBatchCursor(result.getDocument("cursor"), ListCollectionsOperation.this.decoder, source, connection, ListCollectionsOperation.this.batchSize);
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>> commandTransformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, BatchCursor<T>>(){

            @Override
            public BatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return OperationHelper.cursorDocumentToBatchCursor(result.getDocument("cursor"), ListCollectionsOperation.this.decoder, source, ListCollectionsOperation.this.batchSize);
            }
        };
    }

    private MongoNamespace getNamespace() {
        return new MongoNamespace(this.databaseName, "system.namespaces");
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return ListCollectionsOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        BsonDocument command = new BsonDocument("listCollections", new BsonInt32(1)).append("cursor", CursorHelper.getCursorDocumentFromBatchSize(this.batchSize == 0 ? null : Integer.valueOf(this.batchSize)));
        if (this.filter != null) {
            command.append("filter", this.filter);
        }
        if (this.nameOnly) {
            command.append("nameOnly", BsonBoolean.TRUE);
        }
        if (this.maxTimeMS > 0L) {
            command.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        return command;
    }

    private BsonDocument asQueryDocument(ConnectionDescription connectionDescription, ReadPreference readPreference) {
        BsonDocument document = new BsonDocument();
        BsonDocument transformedFilter = null;
        if (this.filter != null) {
            if (this.filter.containsKey("name")) {
                if (!this.filter.isString("name")) {
                    throw new IllegalArgumentException("When filtering collections on MongoDB versions < 3.0 the name field must be a string");
                }
                transformedFilter = new BsonDocument();
                transformedFilter.putAll(this.filter);
                transformedFilter.put("name", new BsonString(String.format("%s.%s", this.databaseName, this.filter.getString("name").getValue())));
            } else {
                transformedFilter = this.filter;
            }
        }
        BsonDocument indexExcludingRegex = new BsonDocument("name", new BsonRegularExpression("^[^$]*$"));
        BsonDocument query = transformedFilter == null ? indexExcludingRegex : new BsonDocument("$and", new BsonArray(Arrays.asList(indexExcludingRegex, transformedFilter)));
        document.put("$query", query);
        if (connectionDescription.getServerType() == ServerType.SHARD_ROUTER && !readPreference.equals(ReadPreference.primary())) {
            document.put("$readPreference", readPreference.toDocument());
        }
        if (this.maxTimeMS > 0L) {
            document.put("$maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        return document;
    }

    private Codec<BsonDocument> createCommandDecoder() {
        return CommandResultDocumentCodec.create(this.decoder, "firstBatch");
    }

    private List<T> projectFromFullNamespaceToCollectionName(List<BsonDocument> unstripped) {
        if (unstripped == null) {
            return null;
        }
        ArrayList<T> stripped = new ArrayList<T>(unstripped.size());
        String prefix = this.databaseName + ".";
        for (BsonDocument cur : unstripped) {
            String name = cur.getString("name").getValue();
            String collectionName = name.substring(prefix.length());
            cur.put("name", new BsonString(collectionName));
            stripped.add(this.decoder.decode(new BsonDocumentReader(cur), DecoderContext.builder().build()));
        }
        return stripped;
    }

    private final class ProjectingAsyncBatchCursor
    implements AsyncBatchCursor<T> {
        private final AsyncBatchCursor<BsonDocument> delegate;

        private ProjectingAsyncBatchCursor(AsyncBatchCursor<BsonDocument> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void next(final SingleResultCallback<List<T>> callback) {
            this.delegate.next(new SingleResultCallback<List<BsonDocument>>(){

                @Override
                public void onResult(List<BsonDocument> result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        callback.onResult(ListCollectionsOperation.this.projectFromFullNamespaceToCollectionName(result), null);
                    }
                }
            });
        }

        @Override
        public void tryNext(final SingleResultCallback<List<T>> callback) {
            this.delegate.tryNext(new SingleResultCallback<List<BsonDocument>>(){

                @Override
                public void onResult(List<BsonDocument> result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        callback.onResult(ListCollectionsOperation.this.projectFromFullNamespaceToCollectionName(result), null);
                    }
                }
            });
        }

        @Override
        public void setBatchSize(int batchSize) {
            this.delegate.setBatchSize(batchSize);
        }

        @Override
        public int getBatchSize() {
            return this.delegate.getBatchSize();
        }

        @Override
        public boolean isClosed() {
            return this.delegate.isClosed();
        }

        @Override
        public void close() {
            this.delegate.close();
        }
    }

    private final class ProjectingBatchCursor
    implements BatchCursor<T> {
        private final BatchCursor<BsonDocument> delegate;

        private ProjectingBatchCursor(BatchCursor<BsonDocument> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void remove() {
            this.delegate.remove();
        }

        @Override
        public void close() {
            this.delegate.close();
        }

        @Override
        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        @Override
        public List<T> next() {
            return ListCollectionsOperation.this.projectFromFullNamespaceToCollectionName(this.delegate.next());
        }

        @Override
        public void setBatchSize(int batchSize) {
            this.delegate.setBatchSize(batchSize);
        }

        @Override
        public int getBatchSize() {
            return this.delegate.getBatchSize();
        }

        @Override
        public List<T> tryNext() {
            return ListCollectionsOperation.this.projectFromFullNamespaceToCollectionName(this.delegate.tryNext());
        }

        @Override
        public ServerCursor getServerCursor() {
            return this.delegate.getServerCursor();
        }

        @Override
        public ServerAddress getServerAddress() {
            return this.delegate.getServerAddress();
        }
    }
}

