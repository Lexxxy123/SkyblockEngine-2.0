/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.NamedCoordinateReferenceSystem;
import com.mongodb.client.model.geojson.codecs.GeometryDecoderHelper;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;

public class NamedCoordinateReferenceSystemCodec
implements Codec<NamedCoordinateReferenceSystem> {
    @Override
    public void encode(BsonWriter writer, NamedCoordinateReferenceSystem value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("type", value.getType().getTypeName());
        writer.writeStartDocument("properties");
        writer.writeString("name", value.getName());
        writer.writeEndDocument();
        writer.writeEndDocument();
    }

    @Override
    public Class<NamedCoordinateReferenceSystem> getEncoderClass() {
        return NamedCoordinateReferenceSystem.class;
    }

    @Override
    public NamedCoordinateReferenceSystem decode(BsonReader reader, DecoderContext decoderContext) {
        CoordinateReferenceSystem crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
        if (crs == null || !(crs instanceof NamedCoordinateReferenceSystem)) {
            throw new CodecConfigurationException("Invalid NamedCoordinateReferenceSystem.");
        }
        return (NamedCoordinateReferenceSystem)crs;
    }
}

