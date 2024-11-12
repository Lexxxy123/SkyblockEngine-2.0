/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcernResult;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.ByteBufBsonDocument;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.UpdateMessage;
import com.mongodb.internal.connection.WriteProtocol;
import java.util.Collections;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;

class UpdateProtocol
extends WriteProtocol {
    private static final Logger LOGGER = Loggers.getLogger("protocol.update");
    private final UpdateRequest updateRequest;

    UpdateProtocol(MongoNamespace namespace, boolean ordered, UpdateRequest updateRequest) {
        super(namespace, ordered);
        this.updateRequest = updateRequest;
    }

    @Override
    public WriteConcernResult execute(InternalConnection connection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Updating documents in namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
        }
        WriteConcernResult writeConcernResult = super.execute(connection);
        LOGGER.debug("Update completed");
        return writeConcernResult;
    }

    @Override
    public void executeAsync(InternalConnection connection, final SingleResultCallback<WriteConcernResult> callback) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Asynchronously updating documents in namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
            }
            super.executeAsync(connection, new SingleResultCallback<WriteConcernResult>(){

                @Override
                public void onResult(WriteConcernResult result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        LOGGER.debug("Asynchronous update completed");
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
        List<ByteBufBsonDocument> documents = ByteBufBsonDocument.createList(bsonOutput, firstDocumentPosition);
        BsonDocument updateDocument = new BsonDocument("q", documents.get(0)).append("u", documents.get(1));
        if (this.updateRequest.isMulti()) {
            updateDocument.append("multi", BsonBoolean.TRUE);
        }
        if (this.updateRequest.isUpsert()) {
            updateDocument.append("upsert", BsonBoolean.TRUE);
        }
        return this.getBaseCommandDocument("update").append("updates", new BsonArray(Collections.singletonList(updateDocument)));
    }

    @Override
    protected RequestMessage createRequestMessage(MessageSettings settings) {
        return new UpdateMessage(this.getNamespace().getFullName(), this.updateRequest, settings);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}

