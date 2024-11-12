/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.ReadOperation;
import org.bson.BsonDocument;
import org.bson.codecs.Decoder;

@Deprecated
public class CommandReadOperation<T>
implements AsyncReadOperation<T>,
ReadOperation<T> {
    private final String databaseName;
    private final BsonDocument command;
    private final Decoder<T> decoder;

    public CommandReadOperation(String databaseName, BsonDocument command, Decoder<T> decoder) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.command = Assertions.notNull("command", command);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    @Override
    public T execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.databaseName, this.getCommandCreator(), this.decoder, false);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.databaseName, this.getCommandCreator(), this.decoder, false, callback);
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return CommandReadOperation.this.command;
            }
        };
    }
}

