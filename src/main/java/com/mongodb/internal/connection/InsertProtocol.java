/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcernResult;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.ByteBufBsonDocument;
import com.mongodb.internal.connection.InsertMessage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.WriteProtocol;
import org.bson.BsonArray;
import org.bson.BsonDocument;

class InsertProtocol
extends WriteProtocol {
    private static final Logger LOGGER = Loggers.getLogger("protocol.insert");
    private final InsertRequest insertRequest;

    InsertProtocol(MongoNamespace namespace, boolean ordered, InsertRequest insertRequest) {
        super(namespace, ordered);
        this.insertRequest = insertRequest;
    }

    @Override
    public WriteConcernResult execute(InternalConnection connection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Inserting 1 document into namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
        }
        WriteConcernResult writeConcernResult = super.execute(connection);
        LOGGER.debug("Insert completed");
        return writeConcernResult;
    }

    @Override
    public void executeAsync(InternalConnection connection, final SingleResultCallback<WriteConcernResult> callback) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Asynchronously inserting 1 document into namespace %s on connection [%s] to server %s", this.getNamespace(), connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
            }
            super.executeAsync(connection, new SingleResultCallback<WriteConcernResult>(){

                @Override
                public void onResult(WriteConcernResult result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        LOGGER.debug("Asynchronous insert completed");
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
        return this.getBaseCommandDocument("insert").append("documents", new BsonArray(ByteBufBsonDocument.createList(bsonOutput, firstDocumentPosition)));
    }

    @Override
    protected RequestMessage createRequestMessage(MessageSettings settings) {
        return new InsertMessage(this.getNamespace().getFullName(), this.insertRequest, settings);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}

