/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.Compressor;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.io.BsonOutput;

class CompressedMessage
extends RequestMessage {
    private final OpCode wrappedOpcode;
    private final List<ByteBuf> wrappedMessageBuffers;
    private final Compressor compressor;

    CompressedMessage(OpCode wrappedOpcode, List<ByteBuf> wrappedMessageBuffers, Compressor compressor, MessageSettings settings) {
        super(OpCode.OP_COMPRESSED, CompressedMessage.getWrappedMessageRequestId(wrappedMessageBuffers), settings);
        this.wrappedOpcode = wrappedOpcode;
        this.wrappedMessageBuffers = wrappedMessageBuffers;
        this.compressor = compressor;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext) {
        bsonOutput.writeInt32(this.wrappedOpcode.getValue());
        bsonOutput.writeInt32(CompressedMessage.getWrappedMessageSize(this.wrappedMessageBuffers) - 16);
        bsonOutput.writeByte(this.compressor.getId());
        CompressedMessage.getFirstWrappedMessageBuffer(this.wrappedMessageBuffers).position(CompressedMessage.getFirstWrappedMessageBuffer(this.wrappedMessageBuffers).position() + 16);
        this.compressor.compress(this.wrappedMessageBuffers, bsonOutput);
        return new RequestMessage.EncodingMetadata(0);
    }

    private static int getWrappedMessageSize(List<ByteBuf> wrappedMessageBuffers) {
        ByteBuf first = CompressedMessage.getFirstWrappedMessageBuffer(wrappedMessageBuffers);
        return first.getInt(0);
    }

    private static int getWrappedMessageRequestId(List<ByteBuf> wrappedMessageBuffers) {
        ByteBuf first = CompressedMessage.getFirstWrappedMessageBuffer(wrappedMessageBuffers);
        return first.getInt(4);
    }

    private static ByteBuf getFirstWrappedMessageBuffer(List<ByteBuf> wrappedMessageBuffers) {
        return wrappedMessageBuffers.get(0);
    }
}

