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
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.FindAndModifyHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

@Deprecated
public abstract class BaseFindAndModifyOperation<T>
implements AsyncWriteOperation<T>,
WriteOperation<T> {
    private final MongoNamespace namespace;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final Decoder<T> decoder;

    protected BaseFindAndModifyOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    @Override
    public T execute(WriteBinding binding) {
        return CommandOperationHelper.executeRetryableCommand(binding, this.getDatabaseName(), null, this.getFieldNameValidator(), CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.<BsonDocument>transformer());
    }

    @Override
    public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeRetryableCommand(binding, this.getDatabaseName(), null, this.getFieldNameValidator(), CommandResultDocumentCodec.create(this.getDecoder(), "value"), this.getCommandCreator(binding.getSessionContext()), FindAndModifyHelper.<BsonDocument>asyncTransformer(), callback);
    }

    protected abstract String getDatabaseName();

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public Decoder<T> getDecoder() {
        return this.decoder;
    }

    public boolean isRetryWrites() {
        return this.retryWrites;
    }

    protected abstract CommandOperationHelper.CommandCreator getCommandCreator(SessionContext var1);

    protected void addTxnNumberToCommand(ServerDescription serverDescription, ConnectionDescription connectionDescription, BsonDocument commandDocument, SessionContext sessionContext) {
        if (OperationHelper.isRetryableWrite(this.isRetryWrites(), this.getWriteConcern(), serverDescription, connectionDescription, sessionContext)) {
            commandDocument.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
        }
    }

    protected void addWriteConcernToCommand(ConnectionDescription connectionDescription, BsonDocument commandDocument, SessionContext sessionContext) {
        if (this.getWriteConcern().isAcknowledged() && !this.getWriteConcern().isServerDefault() && ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connectionDescription) && !sessionContext.hasActiveTransaction()) {
            commandDocument.put("writeConcern", this.getWriteConcern().asDocument());
        }
    }

    protected abstract FieldNameValidator getFieldNameValidator();
}

