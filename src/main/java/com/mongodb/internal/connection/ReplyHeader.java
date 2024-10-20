/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.internal.connection.CompressedHeader;
import com.mongodb.internal.connection.MessageHeader;
import com.mongodb.internal.connection.OpCode;
import org.bson.ByteBuf;

public final class ReplyHeader {
    public static final int REPLY_HEADER_LENGTH = 20;
    public static final int TOTAL_REPLY_HEADER_LENGTH = 36;
    private static final int CURSOR_NOT_FOUND_RESPONSE_FLAG = 1;
    private static final int QUERY_FAILURE_RESPONSE_FLAG = 2;
    private final int messageLength;
    private final int requestId;
    private final int responseTo;
    private final int responseFlags;
    private final long cursorId;
    private final int startingFrom;
    private final int numberReturned;
    private final int opMsgFlagBits;

    ReplyHeader(ByteBuf header, MessageHeader messageHeader) {
        this(messageHeader.getMessageLength(), messageHeader.getOpCode(), messageHeader, header);
    }

    ReplyHeader(ByteBuf header, CompressedHeader compressedHeader) {
        this(compressedHeader.getUncompressedSize() + 16, compressedHeader.getOriginalOpcode(), compressedHeader.getMessageHeader(), header);
    }

    private ReplyHeader(int messageLength, int opCode, MessageHeader messageHeader, ByteBuf header) {
        this.messageLength = messageLength;
        this.requestId = messageHeader.getRequestId();
        this.responseTo = messageHeader.getResponseTo();
        if (opCode == OpCode.OP_MSG.getValue()) {
            this.responseFlags = 0;
            this.cursorId = 0L;
            this.startingFrom = 0;
            this.numberReturned = 1;
            this.opMsgFlagBits = header.getInt();
            header.get();
        } else if (opCode == OpCode.OP_REPLY.getValue()) {
            if (messageLength < 36) {
                throw new MongoInternalException(String.format("The reply message length %d is less than the mimimum message length %d", messageLength, 36));
            }
            this.responseFlags = header.getInt();
            this.cursorId = header.getLong();
            this.startingFrom = header.getInt();
            this.numberReturned = header.getInt();
            this.opMsgFlagBits = 0;
            if (this.numberReturned < 0) {
                throw new MongoInternalException(String.format("The reply message number of returned documents, %d, is less than 0", this.numberReturned));
            }
        } else {
            throw new MongoInternalException(String.format("Unexpected reply message opCode %d", opCode));
        }
    }

    public int getMessageLength() {
        return this.messageLength;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public int getResponseTo() {
        return this.responseTo;
    }

    public int getResponseFlags() {
        return this.responseFlags;
    }

    public long getCursorId() {
        return this.cursorId;
    }

    public int getStartingFrom() {
        return this.startingFrom;
    }

    public int getNumberReturned() {
        return this.numberReturned;
    }

    public boolean isCursorNotFound() {
        return (this.responseFlags & 1) == 1;
    }

    public boolean isQueryFailure() {
        return (this.responseFlags & 2) == 2;
    }

    int getOpMsgFlagBits() {
        return this.opMsgFlagBits;
    }
}

