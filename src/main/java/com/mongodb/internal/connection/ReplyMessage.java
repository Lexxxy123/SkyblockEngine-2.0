/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.internal.connection.ReplyHeader;
import com.mongodb.internal.connection.ResponseBuffers;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonBinaryReader;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.io.ByteBufferBsonInput;

public class ReplyMessage<T> {
    private final ReplyHeader replyHeader;
    private final List<T> documents;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ReplyMessage(ResponseBuffers responseBuffers, Decoder<T> decoder, long requestId) {
        this(responseBuffers.getReplyHeader(), requestId);
        if (this.replyHeader.getNumberReturned() > 0) {
            ByteBufferBsonInput bsonInput = new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer().duplicate());
            try {
                while (this.documents.size() < this.replyHeader.getNumberReturned()) {
                    BsonBinaryReader reader = new BsonBinaryReader(bsonInput);
                    try {
                        this.documents.add(decoder.decode(reader, DecoderContext.builder().build()));
                    } finally {
                        reader.close();
                    }
                }
            } finally {
                bsonInput.close();
                responseBuffers.reset();
            }
        }
    }

    ReplyMessage(ReplyHeader replyHeader, long requestId) {
        if (requestId != (long)replyHeader.getResponseTo()) {
            throw new MongoInternalException(String.format("The responseTo (%d) in the response does not match the requestId (%d) in the request", replyHeader.getResponseTo(), requestId));
        }
        this.replyHeader = replyHeader;
        this.documents = new ArrayList<T>(replyHeader.getNumberReturned());
    }

    public ReplyHeader getReplyHeader() {
        return this.replyHeader;
    }

    public List<T> getDocuments() {
        return this.documents;
    }
}

