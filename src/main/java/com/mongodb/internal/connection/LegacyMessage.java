/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.session.SessionContext;
import org.bson.io.BsonOutput;

abstract class LegacyMessage
extends RequestMessage {
    LegacyMessage(String collectionName, OpCode opCode, MessageSettings settings) {
        super(collectionName, opCode, settings);
    }

    LegacyMessage(OpCode opCode, MessageSettings settings) {
        super(opCode, settings);
    }

    abstract RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput var1);

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext) {
        return this.encodeMessageBodyWithMetadata(bsonOutput);
    }
}

