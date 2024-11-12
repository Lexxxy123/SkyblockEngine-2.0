/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.operation.CommandResultArrayCodec;
import com.mongodb.operation.CommandResultCodecProvider;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

class CommandResultDocumentCodec<T>
extends BsonDocumentCodec {
    private final Decoder<T> payloadDecoder;
    private final List<String> fieldsContainingPayload;

    CommandResultDocumentCodec(CodecRegistry registry, Decoder<T> payloadDecoder, List<String> fieldsContainingPayload) {
        super(registry);
        this.payloadDecoder = payloadDecoder;
        this.fieldsContainingPayload = fieldsContainingPayload;
    }

    static <P> Codec<BsonDocument> create(Decoder<P> decoder, String fieldContainingPayload) {
        return CommandResultDocumentCodec.create(decoder, Collections.singletonList(fieldContainingPayload));
    }

    static <P> Codec<BsonDocument> create(Decoder<P> decoder, List<String> fieldsContainingPayload) {
        CodecRegistry registry = CodecRegistries.fromProviders(new CommandResultCodecProvider<P>(decoder, fieldsContainingPayload));
        return registry.get(BsonDocument.class);
    }

    @Override
    protected BsonValue readValue(BsonReader reader, DecoderContext decoderContext) {
        if (this.fieldsContainingPayload.contains(reader.getCurrentName())) {
            if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
                return new BsonDocumentWrapper<T>(this.payloadDecoder.decode(reader, decoderContext), null);
            }
            if (reader.getCurrentBsonType() == BsonType.ARRAY) {
                return new CommandResultArrayCodec<T>(this.getCodecRegistry(), this.payloadDecoder).decode(reader, decoderContext);
            }
        }
        return super.readValue(reader, decoderContext);
    }
}

