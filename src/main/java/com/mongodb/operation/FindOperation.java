/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.CursorType;
import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoQueryException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.AsyncSingleConnectionReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.SingleConnectionReadBinding;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerType;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.AggregateResponseBatchCursor;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

@Deprecated
public class FindOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private static final String FIRST_BATCH = "firstBatch";
    private final MongoNamespace namespace;
    private final Decoder<T> decoder;
    private boolean retryReads;
    private BsonDocument filter;
    private int batchSize;
    private int limit;
    private BsonDocument modifiers;
    private BsonDocument projection;
    private long maxTimeMS;
    private long maxAwaitTimeMS;
    private int skip;
    private BsonDocument sort;
    private CursorType cursorType = CursorType.NonTailable;
    private boolean slaveOk;
    private boolean oplogReplay;
    private boolean noCursorTimeout;
    private boolean partial;
    private Collation collation;
    private String comment;
    private BsonValue hint;
    private BsonDocument max;
    private BsonDocument min;
    private long maxScan;
    private boolean returnKey;
    private boolean showRecordId;
    private boolean snapshot;
    private static final Map<String, String> META_OPERATOR_TO_COMMAND_FIELD_MAP = new HashMap<String, String>();

    public FindOperation(MongoNamespace namespace, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public Decoder<T> getDecoder() {
        return this.decoder;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public FindOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public FindOperation<T> batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public FindOperation<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public BsonDocument getModifiers() {
        return this.modifiers;
    }

    @Deprecated
    public FindOperation<T> modifiers(BsonDocument modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public BsonDocument getProjection() {
        return this.projection;
    }

    public FindOperation<T> projection(BsonDocument projection) {
        this.projection = projection;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public long getMaxAwaitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxAwaitTime >= 0", maxAwaitTime >= 0L);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    public int getSkip() {
        return this.skip;
    }

    public FindOperation<T> skip(int skip) {
        this.skip = skip;
        return this;
    }

    public BsonDocument getSort() {
        return this.sort;
    }

    public FindOperation<T> sort(BsonDocument sort) {
        this.sort = sort;
        return this;
    }

    public CursorType getCursorType() {
        return this.cursorType;
    }

    public FindOperation<T> cursorType(CursorType cursorType) {
        this.cursorType = Assertions.notNull("cursorType", cursorType);
        return this;
    }

    public boolean isSlaveOk() {
        return this.slaveOk;
    }

    public FindOperation<T> slaveOk(boolean slaveOk) {
        this.slaveOk = slaveOk;
        return this;
    }

    public boolean isOplogReplay() {
        return this.oplogReplay;
    }

    public FindOperation<T> oplogReplay(boolean oplogReplay) {
        this.oplogReplay = oplogReplay;
        return this;
    }

    public boolean isNoCursorTimeout() {
        return this.noCursorTimeout;
    }

    public FindOperation<T> noCursorTimeout(boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public FindOperation<T> partial(boolean partial) {
        this.partial = partial;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public FindOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public String getComment() {
        return this.comment;
    }

    public FindOperation<T> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public BsonValue getHint() {
        return this.hint;
    }

    public FindOperation<T> hint(BsonValue hint) {
        this.hint = hint;
        return this;
    }

    public BsonDocument getMax() {
        return this.max;
    }

    public FindOperation<T> max(BsonDocument max) {
        this.max = max;
        return this;
    }

    public BsonDocument getMin() {
        return this.min;
    }

    public FindOperation<T> min(BsonDocument min) {
        this.min = min;
        return this;
    }

    @Deprecated
    public long getMaxScan() {
        return this.maxScan;
    }

    @Deprecated
    public FindOperation<T> maxScan(long maxScan) {
        this.maxScan = maxScan;
        return this;
    }

    public boolean isReturnKey() {
        return this.returnKey;
    }

    public FindOperation<T> returnKey(boolean returnKey) {
        this.returnKey = returnKey;
        return this;
    }

    public boolean isShowRecordId() {
        return this.showRecordId;
    }

    public FindOperation<T> showRecordId(boolean showRecordId) {
        this.showRecordId = showRecordId;
        return this;
    }

    @Deprecated
    public boolean isSnapshot() {
        return this.snapshot;
    }

    @Deprecated
    public FindOperation<T> snapshot(boolean snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public FindOperation<T> retryReads(boolean retryReads) {
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
                if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                    try {
                        return (BatchCursor)CommandOperationHelper.executeCommandWithConnection(binding, source, FindOperation.this.namespace.getDatabaseName(), FindOperation.this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(FindOperation.this.decoder, FindOperation.FIRST_BATCH), FindOperation.this.transformer(), FindOperation.this.retryReads, connection);
                    } catch (MongoCommandException e2) {
                        throw new MongoQueryException(e2);
                    }
                }
                try {
                    OperationHelper.validateReadConcernAndCollation(connection, binding.getSessionContext().getReadConcern(), FindOperation.this.collation);
                    QueryResult queryResult = connection.query(FindOperation.this.namespace, FindOperation.this.asDocument(connection.getDescription(), binding.getReadPreference()), FindOperation.this.projection, FindOperation.this.skip, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.isSlaveOk() || binding.getReadPreference().isSlaveOk(), FindOperation.this.isTailableCursor(), FindOperation.this.isAwaitData(), FindOperation.this.isNoCursorTimeout(), FindOperation.this.isPartial(), FindOperation.this.isOplogReplay(), FindOperation.this.decoder);
                    QueryBatchCursor queryBatchCursor = new QueryBatchCursor(queryResult, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.getMaxTimeForCursor(), FindOperation.this.decoder, source, connection);
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
            public void call(AsyncConnectionSource source, AsyncConnection connection, Throwable t2) {
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                    SingleResultCallback wrappedCallback = FindOperation.exceptionTransformingCallback(errHandlingCallback);
                    CommandOperationHelper.executeCommandAsyncWithConnection(binding, source, FindOperation.this.namespace.getDatabaseName(), FindOperation.this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(FindOperation.this.decoder, FindOperation.FIRST_BATCH), FindOperation.this.asyncTransformer(), FindOperation.this.retryReads, connection, wrappedCallback);
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, source, connection);
                    OperationHelper.validateReadConcernAndCollation(source, connection, binding.getSessionContext().getReadConcern(), FindOperation.this.collation, new OperationHelper.AsyncCallableWithConnectionAndSource(){

                        @Override
                        public void call(final AsyncConnectionSource source, final AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                connection.queryAsync(FindOperation.this.namespace, FindOperation.this.asDocument(connection.getDescription(), binding.getReadPreference()), FindOperation.this.projection, FindOperation.this.skip, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.isSlaveOk() || binding.getReadPreference().isSlaveOk(), FindOperation.this.isTailableCursor(), FindOperation.this.isAwaitData(), FindOperation.this.isNoCursorTimeout(), FindOperation.this.isPartial(), FindOperation.this.isOplogReplay(), FindOperation.this.decoder, new SingleResultCallback<QueryResult<T>>(){

                                    @Override
                                    public void onResult(QueryResult<T> result, Throwable t2) {
                                        if (t2 != null) {
                                            wrappedCallback.onResult(null, t2);
                                        } else {
                                            wrappedCallback.onResult(new AsyncQueryBatchCursor(result, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.getMaxTimeForCursor(), FindOperation.this.decoder, source, connection), null);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private static <T> SingleResultCallback<T> exceptionTransformingCallback(final SingleResultCallback<T> callback) {
        return new SingleResultCallback<T>(){

            @Override
            public void onResult(T result, Throwable t2) {
                if (t2 != null) {
                    if (t2 instanceof MongoCommandException) {
                        MongoCommandException commandException = (MongoCommandException)t2;
                        callback.onResult(result, new MongoQueryException(commandException.getServerAddress(), commandException.getErrorCode(), commandException.getErrorMessage()));
                    } else {
                        callback.onResult(result, t2);
                    }
                } else {
                    callback.onResult(result, null);
                }
            }
        };
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        Assertions.notNull("explainVerbosity", explainVerbosity);
        return new ReadOperation<BsonDocument>(){

            @Override
            public BsonDocument execute(final ReadBinding binding) {
                return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnectionAndSource<BsonDocument>(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public BsonDocument call(ConnectionSource connectionSource, Connection connection) {
                        SingleConnectionReadBinding singleConnectionBinding = new SingleConnectionReadBinding(binding.getReadPreference(), connectionSource.getServerDescription(), connection);
                        if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                            try {
                                BsonDocument bsonDocument = new CommandReadOperation<BsonDocument>(FindOperation.this.getNamespace().getDatabaseName(), new BsonDocument("explain", FindOperation.this.getCommand(binding.getSessionContext())), new BsonDocumentCodec()).execute(singleConnectionBinding);
                                return bsonDocument;
                            } catch (MongoCommandException e2) {
                                throw new MongoQueryException(e2);
                            }
                        }
                        Object cursor = FindOperation.this.createExplainableQueryOperation().execute(singleConnectionBinding);
                        try {
                            BsonDocument bsonDocument = (BsonDocument)cursor.next().iterator().next();
                            cursor.close();
                            return bsonDocument;
                        } catch (Throwable throwable) {
                            cursor.close();
                            throw throwable;
                        } finally {
                            singleConnectionBinding.release();
                        }
                    }
                });
            }
        };
    }

    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
        Assertions.notNull("explainVerbosity", explainVerbosity);
        return new AsyncReadOperation<BsonDocument>(){

            @Override
            public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<BsonDocument> callback) {
                OperationHelper.withAsyncReadConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

                    @Override
                    public void call(AsyncConnectionSource connectionSource, AsyncConnection connection, Throwable t2) {
                        SingleResultCallback<BsonDocument> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                        if (t2 != null) {
                            errHandlingCallback.onResult(null, t2);
                        } else {
                            AsyncSingleConnectionReadBinding singleConnectionReadBinding = new AsyncSingleConnectionReadBinding(binding.getReadPreference(), connectionSource.getServerDescription(), connection);
                            if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                                new CommandReadOperation<BsonDocument>(FindOperation.this.namespace.getDatabaseName(), new BsonDocument("explain", FindOperation.this.getCommand(binding.getSessionContext())), new BsonDocumentCodec()).executeAsync(singleConnectionReadBinding, OperationHelper.releasingCallback(FindOperation.exceptionTransformingCallback(errHandlingCallback), singleConnectionReadBinding, connectionSource, connection));
                            } else {
                                FindOperation.this.createExplainableQueryOperation().executeAsync((AsyncReadBinding)singleConnectionReadBinding, OperationHelper.releasingCallback(new ExplainResultCallback(errHandlingCallback), singleConnectionReadBinding, connectionSource, connection));
                            }
                        }
                    }
                });
            }
        };
    }

    private FindOperation<BsonDocument> createExplainableQueryOperation() {
        FindOperation<BsonDocument> explainFindOperation = new FindOperation<BsonDocument>(this.namespace, new BsonDocumentCodec());
        BsonDocument explainModifiers = new BsonDocument();
        if (this.modifiers != null) {
            explainModifiers.putAll(this.modifiers);
        }
        explainModifiers.append("$explain", BsonBoolean.TRUE);
        return explainFindOperation.filter(this.filter).projection(this.projection).sort(this.sort).skip(this.skip).limit(Math.abs(this.limit) * -1).hint(this.hint).min(this.min).max(this.max).modifiers(explainModifiers);
    }

    private BsonDocument asDocument(ConnectionDescription connectionDescription, ReadPreference readPreference) {
        BsonDocument document = new BsonDocument();
        if (this.modifiers != null) {
            document.putAll(this.modifiers);
        }
        if (this.sort != null) {
            document.put("$orderby", this.sort);
        }
        if (this.maxTimeMS > 0L) {
            document.put("$maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (connectionDescription.getServerType() == ServerType.SHARD_ROUTER && !readPreference.equals(ReadPreference.primary())) {
            document.put("$readPreference", readPreference.toDocument());
        }
        if (this.comment != null) {
            document.put("$comment", new BsonString(this.comment));
        }
        if (this.hint != null) {
            document.put("$hint", this.hint);
        }
        if (this.max != null) {
            document.put("$max", this.max);
        }
        if (this.min != null) {
            document.put("$min", this.min);
        }
        if (this.maxScan > 0L) {
            document.put("$maxScan", new BsonInt64(this.maxScan));
        }
        if (this.returnKey) {
            document.put("$returnKey", BsonBoolean.TRUE);
        }
        if (this.showRecordId) {
            document.put("$showDiskLoc", BsonBoolean.TRUE);
        }
        if (this.snapshot) {
            document.put("$snapshot", BsonBoolean.TRUE);
        }
        if (document.isEmpty()) {
            document = this.filter != null ? this.filter : new BsonDocument();
        } else if (this.filter != null) {
            document.put("$query", this.filter);
        } else if (!document.containsKey("$query")) {
            document.put("$query", new BsonDocument());
        }
        return document;
    }

    private BsonDocument getCommand(SessionContext sessionContext) {
        BsonDocument commandDocument = new BsonDocument("find", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, commandDocument);
        if (this.modifiers != null) {
            for (Map.Entry<String, BsonValue> cur : this.modifiers.entrySet()) {
                String commandFieldName = META_OPERATOR_TO_COMMAND_FIELD_MAP.get(cur.getKey());
                if (commandFieldName == null) continue;
                commandDocument.append(commandFieldName, cur.getValue());
            }
        }
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "filter", this.filter);
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "sort", this.sort);
        DocumentHelper.putIfNotNullOrEmpty(commandDocument, "projection", this.projection);
        if (this.skip > 0) {
            commandDocument.put("skip", new BsonInt32(this.skip));
        }
        if (this.limit != 0) {
            commandDocument.put("limit", new BsonInt32(Math.abs(this.limit)));
        }
        if (this.limit >= 0) {
            if (this.batchSize < 0 && Math.abs(this.batchSize) < this.limit) {
                commandDocument.put("limit", new BsonInt32(Math.abs(this.batchSize)));
            } else if (this.batchSize != 0) {
                commandDocument.put("batchSize", new BsonInt32(Math.abs(this.batchSize)));
            }
        }
        if (this.limit < 0 || this.batchSize < 0) {
            commandDocument.put("singleBatch", BsonBoolean.TRUE);
        }
        if (this.maxTimeMS > 0L) {
            commandDocument.put("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        if (this.isTailableCursor()) {
            commandDocument.put("tailable", BsonBoolean.TRUE);
        }
        if (this.isAwaitData()) {
            commandDocument.put("awaitData", BsonBoolean.TRUE);
        }
        if (this.oplogReplay) {
            commandDocument.put("oplogReplay", BsonBoolean.TRUE);
        }
        if (this.noCursorTimeout) {
            commandDocument.put("noCursorTimeout", BsonBoolean.TRUE);
        }
        if (this.partial) {
            commandDocument.put("allowPartialResults", BsonBoolean.TRUE);
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
        if (this.max != null) {
            commandDocument.put("max", this.max);
        }
        if (this.min != null) {
            commandDocument.put("min", this.min);
        }
        if (this.maxScan > 0L) {
            commandDocument.put("maxScan", new BsonInt64(this.maxScan));
        }
        if (this.returnKey) {
            commandDocument.put("returnKey", BsonBoolean.TRUE);
        }
        if (this.showRecordId) {
            commandDocument.put("showRecordId", BsonBoolean.TRUE);
        }
        if (this.snapshot) {
            commandDocument.put("snapshot", BsonBoolean.TRUE);
        }
        return commandDocument;
    }

    private BsonDocument wrapInExplainIfNecessary(BsonDocument commandDocument) {
        if (this.isExplain()) {
            return new BsonDocument("explain", commandDocument);
        }
        return commandDocument;
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcernAndCollation(connectionDescription, sessionContext.getReadConcern(), FindOperation.this.collation);
                return FindOperation.this.wrapInExplainIfNecessary(FindOperation.this.getCommand(sessionContext));
            }
        };
    }

    private boolean isExplain() {
        return this.modifiers != null && this.modifiers.get("$explain", BsonBoolean.FALSE).equals(BsonBoolean.TRUE);
    }

    private boolean isTailableCursor() {
        return this.cursorType.isTailable();
    }

    private boolean isAwaitData() {
        return this.cursorType == CursorType.TailableAwait;
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, AggregateResponseBatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, AggregateResponseBatchCursor<T>>(){

            @Override
            public AggregateResponseBatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                QueryResult queryResult = FindOperation.this.documentToQueryResult(result, connection.getDescription().getServerAddress());
                return new QueryBatchCursor(queryResult, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.getMaxTimeForCursor(), FindOperation.this.decoder, source, connection, result);
            }
        };
    }

    private long getMaxTimeForCursor() {
        return this.cursorType == CursorType.TailableAwait ? this.maxAwaitTimeMS : 0L;
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, AsyncBatchCursor<T>>(){

            @Override
            public AsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                QueryResult queryResult = FindOperation.this.documentToQueryResult(result, connection.getDescription().getServerAddress());
                return new AsyncQueryBatchCursor(queryResult, FindOperation.this.limit, FindOperation.this.batchSize, FindOperation.this.getMaxTimeForCursor(), FindOperation.this.decoder, source, connection, result);
            }
        };
    }

    private QueryResult<T> documentToQueryResult(BsonDocument result, ServerAddress serverAddress) {
        QueryResult queryResult;
        if (this.isExplain()) {
            T decodedDocument = this.decoder.decode(new BsonDocumentReader(result), DecoderContext.builder().build());
            queryResult = new QueryResult<T>(this.getNamespace(), Collections.singletonList(decodedDocument), 0L, serverAddress);
        } else {
            queryResult = OperationHelper.cursorDocumentToQueryResult(result.getDocument("cursor"), serverAddress);
        }
        return queryResult;
    }

    static {
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$query", "filter");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$orderby", "sort");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$hint", "hint");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$comment", "comment");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$maxScan", "maxScan");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$maxTimeMS", "maxTimeMS");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$max", "max");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$min", "min");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$returnKey", "returnKey");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$showDiskLoc", "showRecordId");
        META_OPERATOR_TO_COMMAND_FIELD_MAP.put("$snapshot", "snapshot");
    }

    private static class ExplainResultCallback
    implements SingleResultCallback<AsyncBatchCursor<BsonDocument>> {
        private final SingleResultCallback<BsonDocument> callback;

        ExplainResultCallback(SingleResultCallback<BsonDocument> callback) {
            this.callback = callback;
        }

        @Override
        public void onResult(final AsyncBatchCursor<BsonDocument> cursor, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
            } else {
                cursor.next(new SingleResultCallback<List<BsonDocument>>(){

                    @Override
                    public void onResult(List<BsonDocument> result, Throwable t2) {
                        cursor.close();
                        if (t2 != null) {
                            ExplainResultCallback.this.callback.onResult(null, t2);
                        } else if (result == null || result.size() == 0) {
                            ExplainResultCallback.this.callback.onResult(null, new MongoInternalException("Expected explain result"));
                        } else {
                            ExplainResultCallback.this.callback.onResult(result.get(0), null);
                        }
                    }
                });
            }
        }
    }
}

