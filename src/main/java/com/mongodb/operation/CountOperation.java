/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
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
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.client.model.CountStrategy;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.operation.AggregateOperation;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.ExplainHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;

@Deprecated
public class CountOperation
implements AsyncReadOperation<Long>,
ReadOperation<Long> {
    private static final Decoder<BsonDocument> DECODER = new BsonDocumentCodec();
    private final MongoNamespace namespace;
    private final CountStrategy countStrategy;
    private boolean retryReads;
    private BsonDocument filter;
    private BsonValue hint;
    private long skip;
    private long limit;
    private long maxTimeMS;
    private Collation collation;

    public CountOperation(MongoNamespace namespace) {
        this(namespace, CountStrategy.COMMAND);
    }

    public CountOperation(MongoNamespace namespace, CountStrategy countStrategy) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.countStrategy = Assertions.notNull("countStrategy", countStrategy);
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public CountOperation filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public CountOperation retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    public BsonValue getHint() {
        return this.hint;
    }

    public CountOperation hint(BsonValue hint) {
        this.hint = hint;
        return this;
    }

    public long getLimit() {
        return this.limit;
    }

    public CountOperation limit(long limit) {
        this.limit = limit;
        return this;
    }

    public long getSkip() {
        return this.skip;
    }

    public CountOperation skip(long skip) {
        this.skip = skip;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public CountOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public CountOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public Long execute(ReadBinding binding) {
        if (this.countStrategy.equals((Object)CountStrategy.COMMAND)) {
            return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), DECODER, this.transformer(), this.retryReads);
        }
        Object cursor = this.getAggregateOperation().execute(binding);
        return cursor.hasNext() ? this.getCountFromAggregateResults(cursor.next()) : 0L;
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, final SingleResultCallback<Long> callback) {
        if (this.countStrategy.equals((Object)CountStrategy.COMMAND)) {
            CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), DECODER, this.asyncTransformer(), this.retryReads, callback);
        } else {
            this.getAggregateOperation().executeAsync(binding, new SingleResultCallback<AsyncBatchCursor<BsonDocument>>(){

                @Override
                public void onResult(AsyncBatchCursor<BsonDocument> result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        result.next(new SingleResultCallback<List<BsonDocument>>(){

                            @Override
                            public void onResult(List<BsonDocument> result, Throwable t2) {
                                if (t2 != null) {
                                    callback.onResult(null, t2);
                                } else {
                                    callback.onResult(CountOperation.this.getCountFromAggregateResults(result), null);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        if (this.countStrategy.equals((Object)CountStrategy.COMMAND)) {
            return this.createExplainableOperation(explainVerbosity);
        }
        return this.getAggregateOperation().asExplainableOperation(explainVerbosity);
    }

    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
        if (this.countStrategy.equals((Object)CountStrategy.COMMAND)) {
            return this.createExplainableOperation(explainVerbosity);
        }
        return this.getAggregateOperation().asExplainableOperationAsync(explainVerbosity);
    }

    private CommandReadOperation<BsonDocument> createExplainableOperation(ExplainVerbosity explainVerbosity) {
        return new CommandReadOperation<BsonDocument>(this.namespace.getDatabaseName(), ExplainHelper.asExplainCommand(this.getCommand(NoOpSessionContext.INSTANCE), explainVerbosity), new BsonDocumentCodec());
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, Long> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, Long>(){

            @Override
            public Long apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return result.getNumber("n").longValue();
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, Long> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, Long>(){

            @Override
            public Long apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return result.getNumber("n").longValue();
            }
        };
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcernAndCollation(connectionDescription, sessionContext.getReadConcern(), CountOperation.this.collation);
                return CountOperation.this.getCommand(sessionContext);
            }
        };
    }

    private BsonDocument getCommand(SessionContext sessionContext) {
        BsonDocument document = new BsonDocument("count", new BsonString(this.namespace.getCollectionName()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, document);
        DocumentHelper.putIfNotNull(document, "query", this.filter);
        DocumentHelper.putIfNotZero(document, "limit", this.limit);
        DocumentHelper.putIfNotZero(document, "skip", this.skip);
        DocumentHelper.putIfNotNull(document, "hint", this.hint);
        DocumentHelper.putIfNotZero(document, "maxTimeMS", this.maxTimeMS);
        if (this.collation != null) {
            document.put("collation", this.collation.asDocument());
        }
        return document;
    }

    private AggregateOperation<BsonDocument> getAggregateOperation() {
        return new AggregateOperation<BsonDocument>(this.namespace, this.getPipeline(), DECODER).retryReads(this.retryReads).collation(this.collation).hint(this.hint).maxTime(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    private List<BsonDocument> getPipeline() {
        ArrayList<BsonDocument> pipeline = new ArrayList<BsonDocument>();
        pipeline.add(new BsonDocument("$match", this.filter != null ? this.filter : new BsonDocument()));
        if (this.skip > 0L) {
            pipeline.add(new BsonDocument("$skip", new BsonInt64(this.skip)));
        }
        if (this.limit > 0L) {
            pipeline.add(new BsonDocument("$limit", new BsonInt64(this.limit)));
        }
        pipeline.add(new BsonDocument("$group", new BsonDocument("_id", new BsonInt32(1)).append("n", new BsonDocument("$sum", new BsonInt32(1)))));
        return pipeline;
    }

    private Long getCountFromAggregateResults(List<BsonDocument> results) {
        if (results == null || results.isEmpty()) {
            return 0L;
        }
        return results.get(0).getNumber("n").longValue();
    }
}

