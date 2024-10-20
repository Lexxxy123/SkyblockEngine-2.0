/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;

class CompoundDBObjectCodec
implements Codec<DBObject> {
    private final Encoder<DBObject> encoder;
    private final Decoder<DBObject> decoder;

    CompoundDBObjectCodec(Encoder<DBObject> encoder, Decoder<DBObject> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    CompoundDBObjectCodec(Codec<DBObject> codec) {
        this(codec, codec);
    }

    @Override
    public DBObject decode(BsonReader reader, DecoderContext decoderContext) {
        return this.decoder.decode(reader, decoderContext);
    }

    @Override
    public void encode(BsonWriter writer, DBObject value, EncoderContext encoderContext) {
        this.encoder.encode(writer, value, encoderContext);
    }

    @Override
    public Class<DBObject> getEncoderClass() {
        return DBObject.class;
    }

    public Encoder<DBObject> getEncoder() {
        return this.encoder;
    }

    public Decoder<DBObject> getDecoder() {
        return this.decoder;
    }
}

