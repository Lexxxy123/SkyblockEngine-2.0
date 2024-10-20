/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import org.bson.BsonReader;
import org.bson.BsonTimestamp;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.BSONTimestamp;

public class BSONTimestampCodec
implements Codec<BSONTimestamp> {
    @Override
    public void encode(BsonWriter writer, BSONTimestamp value, EncoderContext encoderContext) {
        writer.writeTimestamp(new BsonTimestamp(value.getTime(), value.getInc()));
    }

    @Override
    public BSONTimestamp decode(BsonReader reader, DecoderContext decoderContext) {
        BsonTimestamp timestamp = reader.readTimestamp();
        return new BSONTimestamp(timestamp.getTime(), timestamp.getInc());
    }

    @Override
    public Class<BSONTimestamp> getEncoderClass() {
        return BSONTimestamp.class;
    }
}

