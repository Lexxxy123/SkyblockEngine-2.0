/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import org.bson.io.BsonOutput;

class GetMoreMessage
extends LegacyMessage {
    private final long cursorId;
    private final int numberToReturn;

    GetMoreMessage(String collectionName, long cursorId, int numberToReturn) {
        super(collectionName, OpCode.OP_GETMORE, MessageSettings.builder().build());
        this.cursorId = cursorId;
        this.numberToReturn = numberToReturn;
    }

    public long getCursorId() {
        return this.cursorId;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput) {
        this.writeGetMore(bsonOutput);
        return new RequestMessage.EncodingMetadata(bsonOutput.getPosition());
    }

    private void writeGetMore(BsonOutput buffer) {
        buffer.writeInt32(0);
        buffer.writeCString(this.getCollectionName());
        buffer.writeInt32(this.numberToReturn);
        buffer.writeInt64(this.cursorId);
    }
}

