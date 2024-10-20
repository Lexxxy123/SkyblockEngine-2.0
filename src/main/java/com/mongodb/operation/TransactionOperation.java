/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.Function;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.FieldNameValidator;
import org.bson.codecs.BsonDocumentCodec;

@Deprecated
public abstract class TransactionOperation
implements WriteOperation<Void>,
AsyncWriteOperation<Void> {
    private final WriteConcern writeConcern;

    TransactionOperation(WriteConcern writeConcern) {
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    @Override
    public Void execute(WriteBinding binding) {
        Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
        return CommandOperationHelper.executeRetryableCommand(binding, "admin", null, (FieldNameValidator)new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), CommandOperationHelper.writeConcernErrorTransformer(), this.getRetryCommandModifier());
    }

    @Override
    public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
        Assertions.isTrue("in transaction", binding.getSessionContext().hasActiveTransaction());
        CommandOperationHelper.executeRetryableCommand(binding, "admin", null, new NoOpFieldNameValidator(), new BsonDocumentCodec(), this.getCommandCreator(), CommandOperationHelper.writeConcernErrorTransformerAsync(), this.getRetryCommandModifier(), ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                BsonDocument command = new BsonDocument(TransactionOperation.this.getCommandName(), new BsonInt32(1));
                if (!TransactionOperation.this.writeConcern.isServerDefault()) {
                    command.put("writeConcern", TransactionOperation.this.writeConcern.asDocument());
                }
                return command;
            }
        };
    }

    protected abstract String getCommandName();

    protected abstract Function<BsonDocument, BsonDocument> getRetryCommandModifier();
}

