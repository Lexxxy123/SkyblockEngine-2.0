/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBRef;
import com.mongodb.assertions.Assertions;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class DBRefCodec
implements Codec<DBRef> {
    private final CodecRegistry registry;

    public DBRefCodec(CodecRegistry registry) {
        this.registry = Assertions.notNull("registry", registry);
    }

    @Override
    public void encode(BsonWriter writer, DBRef value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("$ref", value.getCollectionName());
        writer.writeName("$id");
        Codec<?> codec = this.registry.get(value.getId().getClass());
        codec.encode(writer, value.getId(), encoderContext);
        if (value.getDatabaseName() != null) {
            writer.writeString("$db", value.getDatabaseName());
        }
        writer.writeEndDocument();
    }

    @Override
    public Class<DBRef> getEncoderClass() {
        return DBRef.class;
    }

    @Override
    public DBRef decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException("DBRefCodec does not support decoding");
    }
}

