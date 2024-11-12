/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.bulk.DeleteRequest;
import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import org.bson.io.BsonOutput;

class DeleteMessage
extends LegacyMessage {
    private final DeleteRequest deleteRequest;

    DeleteMessage(String collectionName, DeleteRequest deleteRequest, MessageSettings settings) {
        super(collectionName, OpCode.OP_DELETE, settings);
        this.deleteRequest = deleteRequest;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput) {
        bsonOutput.writeInt32(0);
        bsonOutput.writeCString(this.getCollectionName());
        if (this.deleteRequest.isMulti()) {
            bsonOutput.writeInt32(0);
        } else {
            bsonOutput.writeInt32(1);
        }
        int firstDocumentStartPosition = bsonOutput.getPosition();
        this.addDocument(this.deleteRequest.getFilter(), bsonOutput, new NoOpFieldNameValidator());
        return new RequestMessage.EncodingMetadata(firstDocumentStartPosition);
    }
}

