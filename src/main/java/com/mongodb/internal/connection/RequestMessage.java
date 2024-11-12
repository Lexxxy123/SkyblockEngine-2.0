/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.assertions.Assertions;
import com.mongodb.internal.connection.ElementExtendingBsonWriter;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.session.SessionContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.BsonBinaryWriter;
import org.bson.BsonBinaryWriterSettings;
import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.BsonWriter;
import org.bson.BsonWriterSettings;
import org.bson.FieldNameValidator;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BsonOutput;

abstract class RequestMessage {
    static final AtomicInteger REQUEST_ID = new AtomicInteger(1);
    static final int MESSAGE_PROLOGUE_LENGTH = 16;
    private static final int DOCUMENT_HEADROOM = 16384;
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
    private final String collectionName;
    private final MessageSettings settings;
    private final int id;
    private final OpCode opCode;
    private EncodingMetadata encodingMetadata;

    public static int getCurrentGlobalId() {
        return REQUEST_ID.get();
    }

    RequestMessage(OpCode opCode, MessageSettings settings) {
        this(null, opCode, settings);
    }

    RequestMessage(OpCode opCode, int requestId, MessageSettings settings) {
        this(null, opCode, requestId, settings);
    }

    RequestMessage(String collectionName, OpCode opCode, MessageSettings settings) {
        this(collectionName, opCode, REQUEST_ID.getAndIncrement(), settings);
    }

    private RequestMessage(String collectionName, OpCode opCode, int requestId, MessageSettings settings) {
        this.collectionName = collectionName;
        this.settings = settings;
        this.id = requestId;
        this.opCode = opCode;
    }

    public int getId() {
        return this.id;
    }

    public OpCode getOpCode() {
        return this.opCode;
    }

    public MessageSettings getSettings() {
        return this.settings;
    }

    public void encode(BsonOutput bsonOutput, SessionContext sessionContext) {
        Assertions.notNull("sessionContext", sessionContext);
        int messageStartPosition = bsonOutput.getPosition();
        this.writeMessagePrologue(bsonOutput);
        EncodingMetadata encodingMetadata = this.encodeMessageBodyWithMetadata(bsonOutput, sessionContext);
        this.backpatchMessageLength(messageStartPosition, bsonOutput);
        this.encodingMetadata = encodingMetadata;
    }

    public EncodingMetadata getEncodingMetadata() {
        return this.encodingMetadata;
    }

    protected void writeMessagePrologue(BsonOutput bsonOutput) {
        bsonOutput.writeInt32(0);
        bsonOutput.writeInt32(this.id);
        bsonOutput.writeInt32(0);
        bsonOutput.writeInt32(this.opCode.getValue());
    }

    protected abstract EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput var1, SessionContext var2);

    protected void addDocument(BsonDocument document, BsonOutput bsonOutput, FieldNameValidator validator) {
        this.addDocument(document, this.getCodec(document), EncoderContext.builder().build(), bsonOutput, validator, this.settings.getMaxDocumentSize() + 16384, null);
    }

    protected void addDocument(BsonDocument document, BsonOutput bsonOutput, FieldNameValidator validator, List<BsonElement> extraElements) {
        this.addDocument(document, this.getCodec(document), EncoderContext.builder().build(), bsonOutput, validator, this.settings.getMaxDocumentSize() + 16384, extraElements);
    }

    protected void addCollectibleDocument(BsonDocument document, BsonOutput bsonOutput, FieldNameValidator validator) {
        this.addDocument(document, this.getCodec(document), EncoderContext.builder().isEncodingCollectibleDocument(true).build(), bsonOutput, validator, this.settings.getMaxDocumentSize(), null);
    }

    protected void backpatchMessageLength(int startPosition, BsonOutput bsonOutput) {
        int messageLength = bsonOutput.getPosition() - startPosition;
        bsonOutput.writeInt32(bsonOutput.getPosition() - messageLength, messageLength);
    }

    protected String getCollectionName() {
        return this.collectionName;
    }

    Codec<BsonDocument> getCodec(BsonDocument document) {
        return REGISTRY.get(document.getClass());
    }

    private <T> void addDocument(T obj, Encoder<T> encoder, EncoderContext encoderContext, BsonOutput bsonOutput, FieldNameValidator validator, int maxDocumentSize, List<BsonElement> extraElements) {
        BsonBinaryWriter bsonBinaryWriter = new BsonBinaryWriter(new BsonWriterSettings(), new BsonBinaryWriterSettings(maxDocumentSize), bsonOutput, validator);
        BsonWriter bsonWriter = extraElements == null ? bsonBinaryWriter : new ElementExtendingBsonWriter(bsonBinaryWriter, extraElements);
        encoder.encode(bsonWriter, obj, encoderContext);
    }

    static class EncodingMetadata {
        private final int firstDocumentPosition;

        EncodingMetadata(int firstDocumentPosition) {
            this.firstDocumentPosition = firstDocumentPosition;
        }

        public int getFirstDocumentPosition() {
            return this.firstDocumentPosition;
        }
    }
}

