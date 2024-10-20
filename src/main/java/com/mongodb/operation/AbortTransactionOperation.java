/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.Function;
import com.mongodb.WriteConcern;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.TransactionOperation;
import org.bson.BsonDocument;

@Deprecated
public class AbortTransactionOperation
extends TransactionOperation {
    private BsonDocument recoveryToken;

    public AbortTransactionOperation(WriteConcern writeConcern) {
        super(writeConcern);
    }

    public AbortTransactionOperation recoveryToken(BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
        return this;
    }

    @Override
    protected String getCommandName() {
        return "abortTransaction";
    }

    @Override
    CommandOperationHelper.CommandCreator getCommandCreator() {
        final CommandOperationHelper.CommandCreator creator = super.getCommandCreator();
        if (this.recoveryToken != null) {
            return new CommandOperationHelper.CommandCreator(){

                @Override
                public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                    return creator.create(serverDescription, connectionDescription).append("recoveryToken", AbortTransactionOperation.this.recoveryToken);
                }
            };
        }
        return creator;
    }

    @Override
    protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
        return CommandOperationHelper.noOpRetryCommandModifier();
    }
}

