/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcernResult;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.ByteBufBsonDocument;
import com.mongodb.internal.connection.DeleteMessage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.WriteProtocol;
import java.util.Collections;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

class DeleteProtocol
extends WriteProtocol {
    private static final Logger LOGGER = Loggers.getLogger("protocol.delete");
    private final DeleteRequest deleteRequest;

    DeleteProtocol(MongoNamespace namespace, boolean ordered, DeleteRequest deleteRequest) {
        super(namespace, ordered);
        this.deleteRequest = deleteRequest;
    }

    @Override
    public WriteConcernResult execute(InternalConnection connection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Deleting documents from namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
        }
        WriteConcernResult writeConcernResult = super.execute(connection);
        LOGGER.debug("Delete completed");
        return writeConcernResult;
    }

    @Override
    public void executeAsync(InternalConnection connection, final SingleResultCallback<WriteConcernResult> callback) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Asynchronously deleting documents in namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
            }
            super.executeAsync(connection, new SingleResultCallback<WriteConcernResult>(){

                @Override
                public void onResult(WriteConcernResult result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        LOGGER.debug("Asynchronous delete completed");
                        callback.onResult(result, null);
                    }
                }
            });
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    @Override
    protected BsonDocument getAsWriteCommand(ByteBufferBsonOutput bsonOutput, int firstDocumentPosition) {
        BsonDocument deleteDocument = new BsonDocument("q", ByteBufBsonDocument.createOne(bsonOutput, firstDocumentPosition)).append("limit", this.deleteRequest.isMulti() ? new BsonInt32(0) : new BsonInt32(1));
        return this.getBaseCommandDocument("delete").append("deletes", new BsonArray(Collections.singletonList(deleteDocument)));
    }

    @Override
    protected RequestMessage createRequestMessage(MessageSettings settings) {
        return new DeleteMessage(this.getNamespace().getFullName(), this.deleteRequest, settings);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}

