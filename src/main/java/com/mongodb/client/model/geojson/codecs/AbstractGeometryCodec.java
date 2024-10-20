/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.codecs.GeometryDecoderHelper;
import com.mongodb.client.model.geojson.codecs.GeometryEncoderHelper;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

abstract class AbstractGeometryCodec<T extends Geometry>
implements Codec<T> {
    private final CodecRegistry registry;
    private final Class<T> encoderClass;

    AbstractGeometryCodec(CodecRegistry registry, Class<T> encoderClass) {
        this.registry = registry;
        this.encoderClass = encoderClass;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        GeometryEncoderHelper.encodeGeometry(writer, value, encoderContext, this.registry);
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return GeometryDecoderHelper.decodeGeometry(reader, this.getEncoderClass());
    }

    @Override
    public Class<T> getEncoderClass() {
        return this.encoderClass;
    }
}

