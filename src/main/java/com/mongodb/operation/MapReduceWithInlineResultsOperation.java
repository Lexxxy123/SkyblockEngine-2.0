/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
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
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.ExplainHelper;
import com.mongodb.operation.MapReduceAsyncBatchCursor;
import com.mongodb.operation.MapReduceBatchCursor;
import com.mongodb.operation.MapReduceHelper;
import com.mongodb.operation.MapReduceInlineResultsAsyncCursor;
import com.mongodb.operation.MapReduceInlineResultsCursor;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonJavaScript;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;

@Deprecated
public class MapReduceWithInlineResultsOperation<T>
implements AsyncReadOperation<MapReduceAsyncBatchCursor<T>>,
ReadOperation<MapReduceBatchCursor<T>> {
    private final MongoNamespace namespace;
    private final BsonJavaScript mapFunction;
    private final BsonJavaScript reduceFunction;
    private final Decoder<T> decoder;
    private BsonJavaScript finalizeFunction;
    private BsonDocument scope;
    private BsonDocument filter;
    private BsonDocument sort;
    private int limit;
    private boolean jsMode;
    private boolean verbose;
    private long maxTimeMS;
    private Collation collation;

    public MapReduceWithInlineResultsOperation(MongoNamespace namespace, BsonJavaScript mapFunction, BsonJavaScript reduceFunction, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.mapFunction = Assertions.notNull("mapFunction", mapFunction);
        this.reduceFunction = Assertions.notNull("reduceFunction", reduceFunction);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public Decoder<T> getDecoder() {
        return this.decoder;
    }

    public BsonJavaScript getMapFunction() {
        return this.mapFunction;
    }

    public BsonJavaScript getReduceFunction() {
        return this.reduceFunction;
    }

    public BsonJavaScript getFinalizeFunction() {
        return this.finalizeFunction;
    }

    public MapReduceWithInlineResultsOperation<T> finalizeFunction(BsonJavaScript finalizeFunction) {
        this.finalizeFunction = finalizeFunction;
        return this;
    }

    public BsonDocument getScope() {
        return this.scope;
    }

    public MapReduceWithInlineResultsOperation<T> scope(BsonDocument scope) {
        this.scope = scope;
        return this;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public MapReduceWithInlineResultsOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonDocument getSort() {
        return this.sort;
    }

    public MapReduceWithInlineResultsOperation<T> sort(BsonDocument sort) {
        this.sort = sort;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public MapReduceWithInlineResultsOperation<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public boolean isJsMode() {
        return this.jsMode;
    }

    public MapReduceWithInlineResultsOperation<T> jsMode(boolean jsMode) {
        this.jsMode = jsMode;
        return this;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public MapReduceWithInlineResultsOperation<T> verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public MapReduceWithInlineResultsOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public MapReduceWithInlineResultsOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public MapReduceBatchCursor<T> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "results"), this.transformer(), false);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<MapReduceAsyncBatchCursor<T>> callback) {
        SingleResultCallback<MapReduceAsyncBatchCursor<T>> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "results"), this.asyncTransformer(), false, errHandlingCallback);
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }

    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
        return this.createExplainableOperation(explainVerbosity);
    }

    private CommandReadOperation<BsonDocument> createExplainableOperation(ExplainVerbosity explainVerbosity) {
        return new CommandReadOperation<BsonDocument>(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE), explainVerbosity), new BsonDocumentCodec());
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, MapReduceBatchCursor<T>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, MapReduceBatchCursor<T>>(){

            @Override
            public MapReduceBatchCursor<T> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return new MapReduceInlineResultsCursor(MapReduceWithInlineResultsOperation.this.createQueryResult(result, connection.getDescription()), MapReduceWithInlineResultsOperation.this.decoder, source, MapReduceHelper.createStatistics(result));
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, MapReduceAsyncBatchCursor<T>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, MapReduceAsyncBatchCursor<T>>(){

            @Override
            public MapReduceAsyncBatchCursor<T> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return new MapReduceInlineResultsAsyncCursor(MapReduceWithInlineResultsOperation.this.createQueryResult(result, connection.getDescription()), MapReduceHelper.createStatistics(result));
            }
        };
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcernAndCollation(connectionDescription, sessionContext.getReadConcern(), MapReduceWithInlineResultsOperation.this.collation);
                return MapReduceWithInlineResultsOperation.this.getCommand(sessionContext);
            }
        };
    }

    private BsonDocument getCommand(SessionContext sessionContext) {
        BsonDocument commandDocument = new BsonDocument("mapreduce", new BsonString(this.namespace.getCollectionName())).append("map", this.getMapFunction()).append("reduce", this.getReduceFunction()).append("out", new BsonDocument("inline", new BsonInt32(1)));
        DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
        DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
        DocumentHelper.putIfNotNull(commandDocument, "finalize", this.getFinalizeFunction());
        DocumentHelper.putIfNotNull(commandDocument, "scope", this.getScope());
        DocumentHelper.putIfTrue(commandDocument, "verbose", this.isVerbose());
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, commandDocument);
        DocumentHelper.putIfNotZero(commandDocument, "limit", this.getLimit());
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
        DocumentHelper.putIfTrue(commandDocument, "jsMode", this.isJsMode());
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        return commandDocument;
    }

    private QueryResult<T> createQueryResult(BsonDocument result, ConnectionDescription description) {
        return new QueryResult(this.namespace, BsonDocumentWrapperHelper.toList(result, "results"), 0L, description.getServerAddress());
    }

    private static BsonValue asValueOrNull(BsonValue value) {
        return value == null ? BsonNull.VALUE : value;
    }
}

