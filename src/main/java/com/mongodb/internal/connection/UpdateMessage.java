/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.validator.CollectibleDocumentFieldNameValidator;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.internal.validator.UpdateFieldNameValidator;
import org.bson.BsonValue;
import org.bson.io.BsonOutput;

class UpdateMessage
extends LegacyMessage {
    private final UpdateRequest updateRequest;

    UpdateMessage(String collectionName, UpdateRequest updateRequest, MessageSettings settings) {
        super(collectionName, OpCode.OP_UPDATE, settings);
        this.updateRequest = updateRequest;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput) {
        bsonOutput.writeInt32(0);
        bsonOutput.writeCString(this.getCollectionName());
        int flags = 0;
        if (this.updateRequest.isUpsert()) {
            flags |= 1;
        }
        if (this.updateRequest.isMulti()) {
            flags |= 2;
        }
        bsonOutput.writeInt32(flags);
        int firstDocumentStartPosition = bsonOutput.getPosition();
        this.addDocument(this.updateRequest.getFilter(), bsonOutput, new NoOpFieldNameValidator());
        if (this.updateRequest.getType() == WriteRequest.Type.REPLACE && this.updateRequest.getUpdateValue().isDocument()) {
            this.addDocument(this.updateRequest.getUpdateValue().asDocument(), bsonOutput, new CollectibleDocumentFieldNameValidator());
        } else {
            int bufferPosition = bsonOutput.getPosition();
            BsonValue update = this.updateRequest.getUpdateValue();
            if (!update.isDocument()) {
                throw new IllegalArgumentException("Invalid update filter in update request. The filter must be a document.");
            }
            this.addDocument(update.asDocument(), bsonOutput, new UpdateFieldNameValidator());
            if (bsonOutput.getPosition() == bufferPosition + 5) {
                throw new IllegalArgumentException("Invalid BSON document for an update");
            }
        }
        return new RequestMessage.EncodingMetadata(firstDocumentStartPosition);
    }
}

