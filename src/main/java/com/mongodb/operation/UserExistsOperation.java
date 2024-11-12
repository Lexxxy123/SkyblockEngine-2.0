/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.codecs.BsonDocumentCodec;

@Deprecated
public class UserExistsOperation
implements AsyncReadOperation<Boolean>,
ReadOperation<Boolean> {
    private final String databaseName;
    private final String userName;
    private boolean retryReads;

    public UserExistsOperation(String databaseName, String userName) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.userName = Assertions.notNull("userName", userName);
    }

    public UserExistsOperation retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    @Override
    public Boolean execute(final ReadBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Boolean>(){

            @Override
            public Boolean call(Connection connection) {
                return (Boolean)CommandOperationHelper.executeCommand(binding, UserExistsOperation.this.databaseName, UserExistsOperation.this.getCommandCreator(), UserExistsOperation.this.transformer(), UserExistsOperation.this.retryReads);
            }
        });
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<Boolean> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommandCreator(), new BsonDocumentCodec(), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, Boolean> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, Boolean>(){

            @Override
            public Boolean apply(BsonDocument result, ConnectionSource source, Connection connection) {
                return result.get("users").isArray() && !result.getArray("users").isEmpty();
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, Boolean> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, Boolean>(){

            @Override
            public Boolean apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                return result.get("users").isArray() && !result.getArray("users").isEmpty();
            }
        };
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return UserExistsOperation.this.getCommand();
            }
        };
    }

    private BsonDocument getCommand() {
        return new BsonDocument("usersInfo", new BsonString(this.userName));
    }
}

