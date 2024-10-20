/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

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
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;

@Deprecated
public class RenameCollectionOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final MongoNamespace originalNamespace;
    private final MongoNamespace newNamespace;
    private final WriteConcern writeConcern;
    private boolean dropTarget;

    @Deprecated
    public RenameCollectionOperation(MongoNamespace originalNamespace, MongoNamespace newNamespace) {
        this(originalNamespace, newNamespace, null);
    }

    public RenameCollectionOperation(MongoNamespace originalNamespace, MongoNamespace newNamespace, WriteConcern writeConcern) {
        this.originalNamespace = Assertions.notNull("originalNamespace", originalNamespace);
        this.newNamespace = Assertions.notNull("newNamespace", newNamespace);
        this.writeConcern = writeConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean isDropTarget() {
        return this.dropTarget;
    }

    public RenameCollectionOperation dropTarget(boolean dropTarget) {
        this.dropTarget = dropTarget;
        return this;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                return CommandOperationHelper.executeCommand(binding, "admin", RenameCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
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
                    CommandOperationHelper.executeCommandAsync(binding, "admin", RenameCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), OperationHelper.releasingCallback(errHandlingCallback, connection));
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument commandDocument = new BsonDocument("renameCollection", new BsonString(this.originalNamespace.getFullName())).append("to", new BsonString(this.newNamespace.getFullName())).append("dropTarget", BsonBoolean.valueOf(this.dropTarget));
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        return commandDocument;
    }
}

