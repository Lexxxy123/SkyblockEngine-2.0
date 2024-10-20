/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoClientException;
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
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;

@Deprecated
public class CreateViewOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final String databaseName;
    private final String viewName;
    private final String viewOn;
    private final List<BsonDocument> pipeline;
    private final WriteConcern writeConcern;
    private Collation collation;

    public CreateViewOperation(String databaseName, String viewName, String viewOn, List<BsonDocument> pipeline, WriteConcern writeConcern) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.viewName = Assertions.notNull("viewName", viewName);
        this.viewOn = Assertions.notNull("viewOn", viewOn);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public String getViewName() {
        return this.viewName;
    }

    public String getViewOn() {
        return this.viewOn;
    }

    public List<BsonDocument> getPipeline() {
        return this.pipeline;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public CreateViewOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                if (!ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(connection.getDescription())) {
                    throw CreateViewOperation.this.createExceptionForIncompatibleServerVersion();
                }
                CommandOperationHelper.executeCommand(binding, CreateViewOperation.this.databaseName, CreateViewOperation.this.getCommand(connection.getDescription()), CommandOperationHelper.writeConcernErrorTransformer());
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
                    SingleResultCallback<Object> wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, connection);
                    if (!ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(connection.getDescription())) {
                        wrappedCallback.onResult(null, CreateViewOperation.this.createExceptionForIncompatibleServerVersion());
                    }
                    CommandOperationHelper.executeCommandAsync(binding, CreateViewOperation.this.databaseName, CreateViewOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), wrappedCallback);
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument commandDocument = new BsonDocument("create", new BsonString(this.viewName)).append("viewOn", new BsonString(this.viewOn)).append("pipeline", new BsonArray(this.pipeline));
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument, description);
        return commandDocument;
    }

    private MongoClientException createExceptionForIncompatibleServerVersion() {
        return new MongoClientException("Can not create view.  The minimum server version that supports view creation is 3.4");
    }
}

