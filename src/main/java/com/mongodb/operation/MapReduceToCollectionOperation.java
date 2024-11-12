/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.ExplainHelper;
import com.mongodb.operation.MapReduceHelper;
import com.mongodb.operation.MapReduceStatistics;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonJavaScript;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;

@Deprecated
public class MapReduceToCollectionOperation
implements AsyncWriteOperation<MapReduceStatistics>,
WriteOperation<MapReduceStatistics> {
    private final MongoNamespace namespace;
    private final BsonJavaScript mapFunction;
    private final BsonJavaScript reduceFunction;
    private final String collectionName;
    private final WriteConcern writeConcern;
    private BsonJavaScript finalizeFunction;
    private BsonDocument scope;
    private BsonDocument filter;
    private BsonDocument sort;
    private int limit;
    private boolean jsMode;
    private boolean verbose;
    private long maxTimeMS;
    private String action = "replace";
    private String databaseName;
    private boolean sharded;
    private boolean nonAtomic;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private static final List<String> VALID_ACTIONS = Arrays.asList("replace", "merge", "reduce");

    public MapReduceToCollectionOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, String collectionName) {
        this(namespace, mapFunction, reduceFunction, collectionName, null);
    }

    public MapReduceToCollectionOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, String collectionName, WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.mapFunction = Assertions.notNull("mapFunction", mapFunction);
        this.reduceFunction = Assertions.notNull("reduceFunction", reduceFunction);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.writeConcern = writeConcern;
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public BsonJavaScript getMapFunction() {
        return this.mapFunction;
    }

    public BsonJavaScript getReduceFunction() {
        return this.reduceFunction;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public BsonJavaScript getFinalizeFunction() {
        return this.finalizeFunction;
    }

    public MapReduceToCollectionOperation finalizeFunction(BsonJavaScript finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }

    public BsonDocument getScope() {
        return this.scope;
    }

    public MapReduceToCollectionOperation scope(BsonDocument scope) {
        this.scope = scope;
        return this;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public MapReduceToCollectionOperation filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonDocument getSort() {
        return this.sort;
    }

    public MapReduceToCollectionOperation sort(BsonDocument sort) {
        this.sort = sort;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public MapReduceToCollectionOperation limit(int limit) {
        this.limit = limit;
        return this;
    }

    public boolean isJsMode() {
        return this.jsMode;
    }

    public MapReduceToCollectionOperation jsMode(boolean jsMode) {
        this.jsMode = jsMode;
        return this;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public MapReduceToCollectionOperation verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public MapReduceToCollectionOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public String getAction() {
        return this.action;
    }

    public MapReduceToCollectionOperation action(String action) {
        Assertions.notNull("action", action);
        Assertions.isTrue("action must be one of: \"replace\", \"merge\", \"reduce\"", VALID_ACTIONS.contains(action));
        this.action = action;
        return this;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public MapReduceToCollectionOperation databaseName(String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    public boolean isSharded() {
        return this.sharded;
    }

    public MapReduceToCollectionOperation sharded(boolean sharded) {
        this.sharded = sharded;
        return this;
    }

    public boolean isNonAtomic() {
        return this.nonAtomic;
    }

    public MapReduceToCollectionOperation nonAtomic(boolean nonAtomic) {
        this.nonAtomic = nonAtomic;
        return this;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public MapReduceToCollectionOperation bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public MapReduceToCollectionOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public MapReduceStatistics execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<MapReduceStatistics>(){

            @Override
            public MapReduceStatistics call(Connection connection) {
                OperationHelper.validateCollation(connection, MapReduceToCollectionOperation.this.collation);
                return (MapReduceStatistics)CommandOperationHelper.executeCommand(binding, MapReduceToCollectionOperation.this.namespace.getDatabaseName(), MapReduceToCollectionOperation.this.getCommand(connection.getDescription()), connection, MapReduceToCollectionOperation.this.transformer());
            }
        });
    }

    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<MapReduceStatistics> callback) {
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, connection);
                    OperationHelper.validateCollation(connection, MapReduceToCollectionOperation.this.collation, new OperationHelper.AsyncCallableWithConnection(){

                        @Override
                        public void call(AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                CommandOperationHelper.executeCommandAsync(binding, MapReduceToCollectionOperation.this.namespace.getDatabaseName(), MapReduceToCollectionOperation.this.getCommand(connection.getDescription()), connection, MapReduceToCollectionOperation.this.transformerAsync(), wrappedCallback);
                            }
                        }
                    });
                }
            }
        });
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }

    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }

    private CommandReadOperation<BsonDocument> createExplainableOperation(ExplainVerbosity explainVerbosity) {
        return new CommandReadOperation<BsonDocument>(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(null), explainVerbosity), new BsonDocumentCodec());
    }

    private CommandOperationHelper.CommandWriteTransformer<BsonDocument, MapReduceStatistics> transformer() {
        return new CommandOperationHelper.CommandWriteTransformer<BsonDocument, MapReduceStatistics>(){

            @Override
            public MapReduceStatistics apply(BsonDocument result, Connection connection) {
                WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress());
                return MapReduceHelper.createStatistics(result);
            }
        };
    }

    private CommandOperationHelper.CommandWriteTransformerAsync<BsonDocument, MapReduceStatistics> transformerAsync() {
        return new CommandOperationHelper.CommandWriteTransformerAsync<BsonDocument, MapReduceStatistics>(){

            @Override
            public MapReduceStatistics apply(BsonDocument result, AsyncConnection connection) {
                WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress());
                return MapReduceHelper.createStatistics(result);
            }
        };
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument outputDocument = new BsonDocument(this.getAction(), new BsonString(this.getCollectionName()));
        DocumentHelper.putIfTrue(outputDocument, "sharded", this.isSharded());
        DocumentHelper.putIfTrue(outputDocument, "nonAtomic", this.isNonAtomic());
        if (this.getDatabaseName() != null) {
            outputDocument.put("db", new BsonString(this.getDatabaseName()));
        }
        BsonDocument commandDocument = new BsonDocument("mapreduce", new BsonString(this.namespace.getCollectionName())).append("map", this.getMapFunction()).append("reduce", this.getReduceFunction()).append("out", outputDocument);
        DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
        DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
        DocumentHelper.putIfNotNull(commandDocument, "finalize", this.getFinalizeFunction());
        DocumentHelper.putIfNotNull(commandDocument, "scope", this.getScope());
        DocumentHelper.putIfTrue(commandDocument, "verbose", this.isVerbose());
        DocumentHelper.putIfNotZero(commandDocument, "limit", this.getLimit());
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
        DocumentHelper.putIfTrue(commandDocument, "jsMode", this.isJsMode());
        if (this.bypassDocumentValidation != null && description != null && ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(description)) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        if (description != null) {
            WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        }
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        return commandDocument;
    }

    private static BsonValue asValueOrNull(BsonValue value) {
        return value == null ? BsonNull.VALUE : value;
    }
}

