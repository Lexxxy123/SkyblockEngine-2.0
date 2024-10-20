/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonString;

@Deprecated
public class DropIndexOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final MongoNamespace namespace;
    private final String indexName;
    private final BsonDocument indexKeys;
    private final WriteConcern writeConcern;
    private long maxTimeMS;

    @Deprecated
    public DropIndexOperation(MongoNamespace namespace, String indexName) {
        this(namespace, indexName, null);
    }

    @Deprecated
    public DropIndexOperation(MongoNamespace namespace, BsonDocument keys) {
        this(namespace, keys, null);
    }

    public DropIndexOperation(MongoNamespace namespace, String indexName, WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.indexName = Assertions.notNull("indexName", indexName);
        this.indexKeys = null;
        this.writeConcern = writeConcern;
    }

    public DropIndexOperation(MongoNamespace namespace, BsonDocument indexKeys, WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.indexKeys = Assertions.notNull("indexKeys", indexKeys);
        this.indexName = null;
        this.writeConcern = writeConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public DropIndexOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                try {
                    CommandOperationHelper.executeCommand(binding, DropIndexOperation.this.namespace.getDatabaseName(), DropIndexOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
                } catch (MongoCommandException e2) {
                    CommandOperationHelper.rethrowIfNotNamespaceError(e2);
                }
                return null;
            }
        });
    }

    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    final SingleResultCallback releasingCallback = OperationHelper.releasingCallback(errHandlingCallback, connection);
                    CommandOperationHelper.executeCommandAsync(binding, DropIndexOperation.this.namespace.getDatabaseName(), DropIndexOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), new SingleResultCallback<Void>(){

                        @Override
                        public void onResult(Void result, Throwable t2) {
                            if (t2 != null && !CommandOperationHelper.isNamespaceError(t2)) {
                                releasingCallback.onResult(null, t2);
                            } else {
                                releasingCallback.onResult(result, null);
                            }
                        }
                    });
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument command = new BsonDocument("dropIndexes", new BsonString(this.namespace.getCollectionName()));
        if (this.indexName != null) {
            command.put("index", new BsonString(this.indexName));
        } else {
            command.put("index", this.indexKeys);
        }
        DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command, description);
        return command;
    }
}

