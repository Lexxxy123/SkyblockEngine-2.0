/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.changestream;

import com.mongodb.client.model.changestream.OperationType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

final class OperationTypeCodec
implements Codec<OperationType> {
    OperationTypeCodec() {
    }

    @Override
    public OperationType decode(BsonReader reader, DecoderContext decoderContext) {
        return OperationType.fromString(reader.readString());
    }

    @Override
    public void encode(BsonWriter writer, OperationType value, EncoderContext encoderContext) {
        writer.writeString(value.getValue());
    }

    @Override
    public Class<OperationType> getEncoderClass() {
        return OperationType.class;
    }
}

