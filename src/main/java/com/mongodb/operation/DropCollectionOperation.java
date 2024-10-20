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
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import org.bson.BsonDocument;
import org.bson.BsonString;

@Deprecated
public class DropCollectionOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final MongoNamespace namespace;
    private final WriteConcern writeConcern;

    @Deprecated
    public DropCollectionOperation(MongoNamespace namespace) {
        this(namespace, null);
    }

    public DropCollectionOperation(MongoNamespace namespace, WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeConcern = writeConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                try {
                    CommandOperationHelper.executeCommand(binding, DropCollectionOperation.this.namespace.getDatabaseName(), DropCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
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
                    CommandOperationHelper.executeCommandAsync(binding, DropCollectionOperation.this.namespace.getDatabaseName(), DropCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), new SingleResultCallback<Void>(){

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
        BsonDocument commandDocument = new BsonDocument("drop", new BsonString(this.namespace.getCollectionName()));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        return commandDocument;
    }
}

