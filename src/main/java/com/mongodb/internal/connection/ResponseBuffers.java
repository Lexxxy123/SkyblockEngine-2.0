/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.ReplyHeader;
import com.mongodb.internal.connection.ReplyMessage;
import java.io.Closeable;
import org.bson.BsonDocument;
import org.bson.ByteBuf;
import org.bson.codecs.Decoder;

public class ResponseBuffers
implements Closeable {
    private final ReplyHeader replyHeader;
    private final ByteBuf bodyByteBuffer;
    private final int bodyByteBufferStartPosition;
    private volatile boolean isClosed;

    ResponseBuffers(ReplyHeader replyHeader, ByteBuf bodyByteBuffer) {
        this.replyHeader = replyHeader;
        this.bodyByteBuffer = bodyByteBuffer;
        this.bodyByteBufferStartPosition = bodyByteBuffer == null ? 0 : bodyByteBuffer.position();
    }

    public ReplyHeader getReplyHeader() {
        return this.replyHeader;
    }

    <T extends BsonDocument> T getResponseDocument(int messageId, Decoder<T> decoder) {
        ReplyMessage<T> replyMessage = new ReplyMessage<T>(this, decoder, messageId);
        this.reset();
        return (T)((BsonDocument)replyMessage.getDocuments().get(0));
    }

    public ByteBuf getBodyByteBuffer() {
        return this.bodyByteBuffer.asReadOnly();
    }

    public void reset() {
        this.bodyByteBuffer.position(this.bodyByteBufferStartPosition);
    }

    @Override
    public void close() {
        if (!this.isClosed) {
            if (this.bodyByteBuffer != null) {
                this.bodyByteBuffer.release();
            }
            this.isClosed = true;
        }
    }
}

