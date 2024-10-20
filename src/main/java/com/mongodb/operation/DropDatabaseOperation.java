/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

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
import org.bson.BsonInt32;

@Deprecated
public class DropDatabaseOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private static final BsonDocument DROP_DATABASE = new BsonDocument("dropDatabase", new BsonInt32(1));
    private final String databaseName;
    private final WriteConcern writeConcern;

    @Deprecated
    public DropDatabaseOperation(String databaseName) {
        this(databaseName, null);
    }

    public DropDatabaseOperation(String databaseName, WriteConcern writeConcern) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
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
                CommandOperationHelper.executeCommand(binding, DropDatabaseOperation.this.databaseName, DropDatabaseOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
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
                    CommandOperationHelper.executeCommandAsync(binding, DropDatabaseOperation.this.databaseName, DropDatabaseOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), OperationHelper.releasingCallback(errHandlingCallback, connection));
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument commandDocument = new BsonDocument("dropDatabase", new BsonInt32(1));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        return commandDocument;
    }
}

