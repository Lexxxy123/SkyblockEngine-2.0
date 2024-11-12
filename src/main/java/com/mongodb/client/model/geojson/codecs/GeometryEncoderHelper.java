/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.GeometryCollection;
import com.mongodb.client.model.geojson.LineString;
import com.mongodb.client.model.geojson.MultiLineString;
import com.mongodb.client.model.geojson.MultiPoint;
import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.PolygonCoordinates;
import com.mongodb.client.model.geojson.Position;
import java.util.List;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;

final class GeometryEncoderHelper {
    static void encodeGeometry(BsonWriter writer, Geometry value, EncoderContext encoderContext, CodecRegistry registry) {
        writer.writeStartDocument();
        writer.writeString("type", value.getType().getTypeName());
        if (value instanceof GeometryCollection) {
            writer.writeName("geometries");
            GeometryEncoderHelper.encodeGeometryCollection(writer, (GeometryCollection)value, encoderContext, registry);
        } else {
            writer.writeName("coordinates");
            if (value instanceof Point) {
                GeometryEncoderHelper.encodePoint(writer, (Point)value);
            } else if (value instanceof MultiPoint) {
                GeometryEncoderHelper.encodeMultiPoint(writer, (MultiPoint)value);
            } else if (value instanceof Polygon) {
                GeometryEncoderHelper.encodePolygon(writer, (Polygon)value);
            } else if (value instanceof MultiPolygon) {
                GeometryEncoderHelper.encodeMultiPolygon(writer, (MultiPolygon)value);
            } else if (value instanceof LineString) {
                GeometryEncoderHelper.encodeLineString(writer, (LineString)value);
            } else if (value instanceof MultiLineString) {
                GeometryEncoderHelper.encodeMultiLineString(writer, (MultiLineString)value);
            } else {
                throw new CodecConfigurationException(String.format("Unsupported Geometry: %s", value));
            }
        }
        GeometryEncoderHelper.encodeCoordinateReferenceSystem(writer, value, encoderContext, registry);
        writer.writeEndDocument();
    }

    private static void encodePoint(BsonWriter writer, Point value) {
        GeometryEncoderHelper.encodePosition(writer, value.getPosition());
    }

    private static void encodeMultiPoint(BsonWriter writer, MultiPoint value) {
        writer.writeStartArray();
        for (Position position : value.getCoordinates()) {
            GeometryEncoderHelper.encodePosition(writer, position);
        }
        writer.writeEndArray();
    }

    private static void encodePolygon(BsonWriter writer, Polygon value) {
        GeometryEncoderHelper.encodePolygonCoordinates(writer, value.getCoordinates());
    }

    private static void encodeMultiPolygon(BsonWriter writer, MultiPolygon value) {
        writer.writeStartArray();
        for (PolygonCoordinates polygonCoordinates : value.getCoordinates()) {
            GeometryEncoderHelper.encodePolygonCoordinates(writer, polygonCoordinates);
        }
        writer.writeEndArray();
    }

    private static void encodeLineString(BsonWriter writer, LineString value) {
        writer.writeStartArray();
        for (Position position : value.getCoordinates()) {
            GeometryEncoderHelper.encodePosition(writer, position);
        }
        writer.writeEndArray();
    }

    private static void encodeMultiLineString(BsonWriter writer, MultiLineString value) {
        writer.writeStartArray();
        for (List<Position> ring : value.getCoordinates()) {
            writer.writeStartArray();
            for (Position position : ring) {
                GeometryEncoderHelper.encodePosition(writer, position);
            }
            writer.writeEndArray();
        }
        writer.writeEndArray();
    }

    private static void encodeGeometryCollection(BsonWriter writer, GeometryCollection value, EncoderContext encoderContext, CodecRegistry registry) {
        writer.writeStartArray();
        for (Geometry geometry : value.getGeometries()) {
            GeometryEncoderHelper.encodeGeometry(writer, geometry, encoderContext, registry);
        }
        writer.writeEndArray();
    }

    static void encodeCoordinateReferenceSystem(BsonWriter writer, Geometry geometry, EncoderContext encoderContext, CodecRegistry registry) {
        CoordinateReferenceSystem coordinateReferenceSystem = geometry.getCoordinateReferenceSystem();
        if (coordinateReferenceSystem != null) {
            writer.writeName("crs");
            Codec<?> codec = registry.get(coordinateReferenceSystem.getClass());
            encoderContext.encodeWithChildContext(codec, writer, coordinateReferenceSystem);
        }
    }

    static void encodePolygonCoordinates(BsonWriter writer, PolygonCoordinates polygonCoordinates) {
        writer.writeStartArray();
        GeometryEncoderHelper.encodeLinearRing(polygonCoordinates.getExterior(), writer);
        for (List<Position> ring : polygonCoordinates.getHoles()) {
            GeometryEncoderHelper.encodeLinearRing(ring, writer);
        }
        writer.writeEndArray();
    }

    private static void encodeLinearRing(List<Position> ring, BsonWriter writer) {
        writer.writeStartArray();
        for (Position position : ring) {
            GeometryEncoderHelper.encodePosition(writer, position);
        }
        writer.writeEndArray();
    }

    static void encodePosition(BsonWriter writer, Position value) {
        writer.writeStartArray();
        for (double number : value.getValues()) {
            writer.writeDouble(number);
        }
        writer.writeEndArray();
    }

    private GeometryEncoderHelper() {
    }
}

