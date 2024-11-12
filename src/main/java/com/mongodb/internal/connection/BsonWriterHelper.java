/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.MessageSettings;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.BsonMaximumSizeExceededException;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BsonOutput;

final class BsonWriterHelper {
    private static final int DOCUMENT_HEADROOM = 16384;
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
    private static final EncoderContext ENCODER_CONTEXT = EncoderContext.builder().build();

    static void writeElements(BsonWriter writer, List<BsonElement> bsonElements) {
        for (BsonElement bsonElement : bsonElements) {
            writer.writeName(bsonElement.getName());
            BsonWriterHelper.getCodec(bsonElement.getValue()).encode(writer, bsonElement.getValue(), ENCODER_CONTEXT);
        }
    }

    static void writePayloadArray(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, int messageStartPosition, SplittablePayload payload, int maxSplittableDocumentSize) {
        writer.writeStartArray(payload.getPayloadName());
        BsonWriterHelper.writePayload(writer, bsonOutput, BsonWriterHelper.getDocumentMessageSettings(settings), messageStartPosition, payload, maxSplittableDocumentSize);
        writer.writeEndArray();
    }

    static void writePayload(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, int messageStartPosition, SplittablePayload payload, int maxSplittableDocumentSize) {
        MessageSettings payloadSettings = BsonWriterHelper.getPayloadMessageSettings(payload.getPayloadType(), settings);
        for (int i2 = 0; i2 < payload.getPayload().size() && BsonWriterHelper.writeDocument(writer, bsonOutput, payloadSettings, payload.getPayload().get(i2), messageStartPosition, i2 + 1, maxSplittableDocumentSize); ++i2) {
            payload.setPosition(i2 + 1);
        }
        if (payload.getPosition() == 0) {
            throw new BsonMaximumSizeExceededException(String.format("Payload document size is larger than maximum of %d.", payloadSettings.getMaxDocumentSize()));
        }
    }

    private static boolean writeDocument(BsonWriter writer, BsonOutput bsonOutput, MessageSettings settings, BsonDocument document, int messageStartPosition, int batchItemCount, int maxSplittableDocumentSize) {
        int currentPosition = bsonOutput.getPosition();
        BsonWriterHelper.getCodec(document).encode(writer, document, ENCODER_CONTEXT);
        int messageSize = bsonOutput.getPosition() - messageStartPosition;
        int documentSize = bsonOutput.getPosition() - currentPosition;
        if (BsonWriterHelper.exceedsLimits(settings, messageSize, documentSize, batchItemCount) || batchItemCount > 1 && bsonOutput.getPosition() - messageStartPosition > maxSplittableDocumentSize) {
            bsonOutput.truncateToPosition(currentPosition);
            return false;
        }
        return true;
    }

    private static Codec<BsonValue> getCodec(BsonValue bsonValue) {
        return REGISTRY.get(bsonValue.getClass());
    }

    private static MessageSettings getPayloadMessageSettings(SplittablePayload.Type type, MessageSettings settings) {
        MessageSettings payloadMessageSettings = settings;
        if (type != SplittablePayload.Type.INSERT) {
            payloadMessageSettings = BsonWriterHelper.createMessageSettingsBuilder(settings).maxDocumentSize(settings.getMaxDocumentSize() + 16384).build();
        }
        return payloadMessageSettings;
    }

    private static MessageSettings getDocumentMessageSettings(MessageSettings settings) {
        return BsonWriterHelper.createMessageSettingsBuilder(settings).maxMessageSize(settings.getMaxDocumentSize() + 16384).build();
    }

    private static MessageSettings.Builder createMessageSettingsBuilder(MessageSettings settings) {
        return MessageSettings.builder().maxBatchCount(settings.getMaxBatchCount()).maxMessageSize(settings.getMaxMessageSize()).maxDocumentSize(settings.getMaxDocumentSize()).maxWireVersion(settings.getMaxWireVersion());
    }

    private static boolean exceedsLimits(MessageSettings settings, int messageSize, int documentSize, int batchItemCount) {
        if (batchItemCount > settings.getMaxBatchCount()) {
            return true;
        }
        if (messageSize > settings.getMaxMessageSize()) {
            return true;
        }
        return documentSize > settings.getMaxDocumentSize();
    }

    private BsonWriterHelper() {
    }
}

