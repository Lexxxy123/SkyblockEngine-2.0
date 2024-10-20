/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.WriteOperation;
import org.bson.BsonDocument;
import org.bson.codecs.Decoder;

@Deprecated
public class CommandWriteOperation<T>
implements AsyncWriteOperation<T>,
WriteOperation<T> {
    private final String databaseName;
    private final BsonDocument command;
    private final Decoder<T> decoder;

    public CommandWriteOperation(String databaseName, BsonDocument command, Decoder<T> decoder) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.command = Assertions.notNull("command", command);
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    @Override
    public T execute(WriteBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.databaseName, this.command, this.decoder);
    }

    @Override
    public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.databaseName, this.command, this.decoder, callback);
    }
}

