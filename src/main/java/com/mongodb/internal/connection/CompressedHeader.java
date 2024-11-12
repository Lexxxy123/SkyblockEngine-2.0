/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.internal.connection.MessageHeader;
import com.mongodb.internal.connection.OpCode;
import org.bson.ByteBuf;

class CompressedHeader {
    public static final int COMPRESSED_HEADER_LENGTH = 9;
    public static final int TOTAL_COMPRESSED_HEADER_LENGTH = 25;
    private final int originalOpcode;
    private final int uncompressedSize;
    private final byte compressorId;
    private final MessageHeader messageHeader;

    CompressedHeader(ByteBuf header, MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
        if (messageHeader.getOpCode() != OpCode.OP_COMPRESSED.getValue()) {
            throw new MongoInternalException(String.format("The reply message opCode %d does not match the expected opCode %d", messageHeader.getOpCode(), OpCode.OP_COMPRESSED.getValue()));
        }
        if (messageHeader.getMessageLength() < 25) {
            throw new MongoInternalException(String.format("The reply message length %d is less than the mimimum message length %d", messageHeader.getMessageLength(), 9));
        }
        this.originalOpcode = header.getInt();
        this.uncompressedSize = header.getInt();
        this.compressorId = header.get();
    }

    public int getOriginalOpcode() {
        return this.originalOpcode;
    }

    public int getUncompressedSize() {
        return this.uncompressedSize;
    }

    public byte getCompressorId() {
        return this.compressorId;
    }

    public int getCompressedSize() {
        return this.messageHeader.getMessageLength() - 9 - 16;
    }

    public MessageHeader getMessageHeader() {
        return this.messageHeader;
    }
}

