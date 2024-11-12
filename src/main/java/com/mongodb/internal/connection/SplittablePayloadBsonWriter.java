/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.BsonWriterHelper;
import com.mongodb.internal.connection.LevelCountingBsonWriter;
import com.mongodb.internal.connection.MessageSettings;
import org.bson.BsonBinaryWriter;
import org.bson.BsonWriter;
import org.bson.io.BsonOutput;

public class SplittablePayloadBsonWriter
extends LevelCountingBsonWriter {
    private final BsonWriter writer;
    private final BsonOutput bsonOutput;
    private final SplittablePayload payload;
    private int maxSplittableDocumentSize;
    private final MessageSettings settings;
    private final int messageStartPosition;

    public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, MessageSettings settings, SplittablePayload payload) {
        this(writer, bsonOutput, settings, payload, settings.getMaxDocumentSize());
    }

    public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, MessageSettings settings, SplittablePayload payload, int maxSplittableDocumentSize) {
        this(writer, bsonOutput, 0, settings, payload, maxSplittableDocumentSize);
    }

    public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, int messageStartPosition, MessageSettings settings, SplittablePayload payload) {
        this(writer, bsonOutput, messageStartPosition, settings, payload, settings.getMaxDocumentSize());
    }

    public SplittablePayloadBsonWriter(BsonBinaryWriter writer, BsonOutput bsonOutput, int messageStartPosition, MessageSettings settings, SplittablePayload payload, int maxSplittableDocumentSize) {
        super(writer);
        this.writer = writer;
        this.bsonOutput = bsonOutput;
        this.messageStartPosition = messageStartPosition;
        this.settings = settings;
        this.payload = payload;
        this.maxSplittableDocumentSize = maxSplittableDocumentSize;
    }

    @Override
    public void writeStartDocument() {
        super.writeStartDocument();
    }

    @Override
    public void writeEndDocument() {
        if (this.getCurrentLevel() == 0 && this.payload.getPayload().size() > 0) {
            BsonWriterHelper.writePayloadArray(this.writer, this.bsonOutput, this.settings, this.messageStartPosition, this.payload, this.maxSplittableDocumentSize);
        }
        super.writeEndDocument();
    }
}

