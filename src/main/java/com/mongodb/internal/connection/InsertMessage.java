/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.bulk.InsertRequest;
import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.validator.CollectibleDocumentFieldNameValidator;
import org.bson.io.BsonOutput;

class InsertMessage
extends LegacyMessage {
    private final InsertRequest insertRequest;

    InsertMessage(String collectionName, InsertRequest insertRequest, MessageSettings settings) {
        super(collectionName, OpCode.OP_INSERT, settings);
        this.insertRequest = insertRequest;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput outputStream) {
        this.writeInsertPrologue(outputStream);
        int firstDocumentPosition = outputStream.getPosition();
        this.addCollectibleDocument(this.insertRequest.getDocument(), outputStream, new CollectibleDocumentFieldNameValidator());
        return new RequestMessage.EncodingMetadata(firstDocumentPosition);
    }

    private void writeInsertPrologue(BsonOutput outputStream) {
        outputStream.writeInt32(0);
        outputStream.writeCString(this.getCollectionName());
    }
}

