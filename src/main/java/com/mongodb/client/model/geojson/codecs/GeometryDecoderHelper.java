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
import com.mongodb.client.model.geojson.NamedCoordinateReferenceSystem;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.PolygonCoordinates;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.codecs.configuration.CodecConfigurationException;

final class GeometryDecoderHelper {
    static <T extends Geometry> T decodeGeometry(BsonReader reader, Class<T> clazz) {
        if (clazz.equals(Point.class)) {
            return (T)GeometryDecoderHelper.decodePoint(reader);
        }
        if (clazz.equals(MultiPoint.class)) {
            return (T)GeometryDecoderHelper.decodeMultiPoint(reader);
        }
        if (clazz.equals(Polygon.class)) {
            return (T)GeometryDecoderHelper.decodePolygon(reader);
        }
        if (clazz.equals(MultiPolygon.class)) {
            return (T)GeometryDecoderHelper.decodeMultiPolygon(reader);
        }
        if (clazz.equals(LineString.class)) {
            return (T)GeometryDecoderHelper.decodeLineString(reader);
        }
        if (clazz.equals(MultiLineString.class)) {
            return (T)GeometryDecoderHelper.decodeMultiLineString(reader);
        }
        if (clazz.equals(GeometryCollection.class)) {
            return (T)GeometryDecoderHelper.decodeGeometryCollection(reader);
        }
        if (clazz.equals(Geometry.class)) {
            return (T)GeometryDecoderHelper.decodeGeometry(reader);
        }
        throw new CodecConfigurationException(String.format("Unsupported Geometry: %s", clazz));
    }

    private static Point decodePoint(BsonReader reader) {
        String type = null;
        Position position = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                position = GeometryDecoderHelper.decodePosition(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON point", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid Point, document contained no type information.");
        }
        if (!type.equals("Point")) {
            throw new CodecConfigurationException(String.format("Invalid Point, found type '%s'.", type));
        }
        if (position == null) {
            throw new CodecConfigurationException("Invalid Point, missing position coordinates.");
        }
        return crs != null ? new Point(crs, position) : new Point(position);
    }

    private static MultiPoint decodeMultiPoint(BsonReader reader) {
        String type = null;
        List<Position> coordinates = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                coordinates = GeometryDecoderHelper.decodeCoordinates(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON point", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiPoint, document contained no type information.");
        }
        if (!type.equals("MultiPoint")) {
            throw new CodecConfigurationException(String.format("Invalid MultiPoint, found type '%s'.", type));
        }
        if (coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiPoint, missing position coordinates.");
        }
        return crs != null ? new MultiPoint(crs, coordinates) : new MultiPoint(coordinates);
    }

    private static Polygon decodePolygon(BsonReader reader) {
        String type = null;
        PolygonCoordinates coordinates = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                coordinates = GeometryDecoderHelper.decodePolygonCoordinates(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid Polygon, document contained no type information.");
        }
        if (!type.equals("Polygon")) {
            throw new CodecConfigurationException(String.format("Invalid Polygon, found type '%s'.", type));
        }
        if (coordinates == null) {
            throw new CodecConfigurationException("Invalid Polygon, missing coordinates.");
        }
        return crs != null ? new Polygon(crs, coordinates) : new Polygon(coordinates);
    }

    private static MultiPolygon decodeMultiPolygon(BsonReader reader) {
        String type = null;
        List<PolygonCoordinates> coordinates = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                coordinates = GeometryDecoderHelper.decodeMultiPolygonCoordinates(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiPolygon, document contained no type information.");
        }
        if (!type.equals("MultiPolygon")) {
            throw new CodecConfigurationException(String.format("Invalid MultiPolygon, found type '%s'.", type));
        }
        if (coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiPolygon, missing coordinates.");
        }
        return crs != null ? new MultiPolygon(crs, coordinates) : new MultiPolygon(coordinates);
    }

    private static LineString decodeLineString(BsonReader reader) {
        String type = null;
        List<Position> coordinates = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                coordinates = GeometryDecoderHelper.decodeCoordinates(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid LineString, document contained no type information.");
        }
        if (!type.equals("LineString")) {
            throw new CodecConfigurationException(String.format("Invalid LineString, found type '%s'.", type));
        }
        if (coordinates == null) {
            throw new CodecConfigurationException("Invalid LineString, missing coordinates.");
        }
        return crs != null ? new LineString(crs, coordinates) : new LineString(coordinates);
    }

    private static MultiLineString decodeMultiLineString(BsonReader reader) {
        String type = null;
        List<List<Position>> coordinates = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("coordinates")) {
                coordinates = GeometryDecoderHelper.decodeMultiCoordinates(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid MultiLineString, document contained no type information.");
        }
        if (!type.equals("MultiLineString")) {
            throw new CodecConfigurationException(String.format("Invalid MultiLineString, found type '%s'.", type));
        }
        if (coordinates == null) {
            throw new CodecConfigurationException("Invalid MultiLineString, missing coordinates.");
        }
        return crs != null ? new MultiLineString(crs, coordinates) : new MultiLineString(coordinates);
    }

    private static GeometryCollection decodeGeometryCollection(BsonReader reader) {
        String type = null;
        List<? extends Geometry> geometries = null;
        CoordinateReferenceSystem crs = null;
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                continue;
            }
            if (key.equals("geometries")) {
                geometries = GeometryDecoderHelper.decodeGeometries(reader);
                continue;
            }
            if (key.equals("crs")) {
                crs = GeometryDecoderHelper.decodeCoordinateReferenceSystem(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Unexpected key '%s' found when decoding a GeoJSON Polygon", key));
        }
        reader.readEndDocument();
        if (type == null) {
            throw new CodecConfigurationException("Invalid GeometryCollection, document contained no type information.");
        }
        if (!type.equals("GeometryCollection")) {
            throw new CodecConfigurationException(String.format("Invalid GeometryCollection, found type '%s'.", type));
        }
        if (geometries == null) {
            throw new CodecConfigurationException("Invalid GeometryCollection, missing geometries.");
        }
        return crs != null ? new GeometryCollection(crs, geometries) : new GeometryCollection(geometries);
    }

    private static List<? extends Geometry> decodeGeometries(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<Geometry> values = new ArrayList<Geometry>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            Geometry geometry = GeometryDecoderHelper.decodeGeometry(reader);
            values.add(geometry);
        }
        reader.readEndArray();
        return values;
    }

    private static Geometry decodeGeometry(BsonReader reader) {
        String type = null;
        BsonReaderMark mark = reader.getMark();
        GeometryDecoderHelper.validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = reader.readName();
            if (key.equals("type")) {
                type = reader.readString();
                break;
            }
            reader.skipValue();
        }
        mark.reset();
        if (type == null) {
            throw new CodecConfigurationException("Invalid Geometry item, document contained no type information.");
        }
        Geometry geometry = null;
        if (type.equals("Point")) {
            geometry = GeometryDecoderHelper.decodePoint(reader);
        } else if (type.equals("MultiPoint")) {
            geometry = GeometryDecoderHelper.decodeMultiPoint(reader);
        } else if (type.equals("Polygon")) {
            geometry = GeometryDecoderHelper.decodePolygon(reader);
        } else if (type.equals("MultiPolygon")) {
            geometry = GeometryDecoderHelper.decodeMultiPolygon(reader);
        } else if (type.equals("LineString")) {
            geometry = GeometryDecoderHelper.decodeLineString(reader);
        } else if (type.equals("MultiLineString")) {
            geometry = GeometryDecoderHelper.decodeMultiLineString(reader);
        } else if (type.equals("GeometryCollection")) {
            geometry = GeometryDecoderHelper.decodeGeometryCollection(reader);
        } else {
            throw new CodecConfigurationException(String.format("Invalid Geometry item, found type '%s'.", type));
        }
        return geometry;
    }

    private static PolygonCoordinates decodePolygonCoordinates(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<List<Position>> values = new ArrayList<List<Position>>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(GeometryDecoderHelper.decodeCoordinates(reader));
        }
        reader.readEndArray();
        if (values.isEmpty()) {
            throw new CodecConfigurationException("Invalid Polygon no coordinates.");
        }
        List exterior = (List)values.remove(0);
        ArrayList[] holes = values.toArray(new ArrayList[values.size()]);
        try {
            return new PolygonCoordinates(exterior, holes);
        } catch (IllegalArgumentException e2) {
            throw new CodecConfigurationException(String.format("Invalid Polygon: %s", e2.getMessage()));
        }
    }

    private static List<PolygonCoordinates> decodeMultiPolygonCoordinates(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<PolygonCoordinates> values = new ArrayList<PolygonCoordinates>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(GeometryDecoderHelper.decodePolygonCoordinates(reader));
        }
        reader.readEndArray();
        if (values.isEmpty()) {
            throw new CodecConfigurationException("Invalid MultiPolygon no coordinates.");
        }
        return values;
    }

    private static List<Position> decodeCoordinates(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<Position> values = new ArrayList<Position>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(GeometryDecoderHelper.decodePosition(reader));
        }
        reader.readEndArray();
        return values;
    }

    private static List<List<Position>> decodeMultiCoordinates(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<List<Position>> values = new ArrayList<List<Position>>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(GeometryDecoderHelper.decodeCoordinates(reader));
        }
        reader.readEndArray();
        return values;
    }

    private static Position decodePosition(BsonReader reader) {
        GeometryDecoderHelper.validateIsArray(reader);
        reader.readStartArray();
        ArrayList<Double> values = new ArrayList<Double>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            values.add(GeometryDecoderHelper.readAsDouble(reader));
        }
        reader.readEndArray();
        try {
            return new Position(values);
        } catch (IllegalArgumentException e2) {
            throw new CodecConfigurationException(String.format("Invalid Position: %s", e2.getMessage()));
        }
    }

    private static double readAsDouble(BsonReader reader) {
        if (reader.getCurrentBsonType() == BsonType.DOUBLE) {
            return reader.readDouble();
        }
        if (reader.getCurrentBsonType() == BsonType.INT32) {
            return reader.readInt32();
        }
        if (reader.getCurrentBsonType() == BsonType.INT64) {
            return reader.readInt64();
        }
        throw new CodecConfigurationException("A GeoJSON position value must be a numerical type, but the value is of type " + (Object)((Object)reader.getCurrentBsonType()));
    }

    @Nullable
    static CoordinateReferenceSystem decodeCoordinateReferenceSystem(BsonReader reader) {
        String crsName = null;
        GeometryDecoderHelper.validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals("type")) {
                String type = reader.readString();
                if (type.equals("name")) continue;
                throw new CodecConfigurationException(String.format("Unsupported CoordinateReferenceSystem '%s'.", type));
            }
            if (name.equals("properties")) {
                crsName = GeometryDecoderHelper.decodeCoordinateReferenceSystemProperties(reader);
                continue;
            }
            throw new CodecConfigurationException(String.format("Found invalid key '%s' in the CoordinateReferenceSystem.", name));
        }
        reader.readEndDocument();
        return crsName != null ? new NamedCoordinateReferenceSystem(crsName) : null;
    }

    private static String decodeCoordinateReferenceSystemProperties(BsonReader reader) {
        String crsName = null;
        GeometryDecoderHelper.validateIsDocument(reader);
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals("name")) {
                crsName = reader.readString();
                continue;
            }
            throw new CodecConfigurationException(String.format("Found invalid key '%s' in the CoordinateReferenceSystem.", name));
        }
        reader.readEndDocument();
        if (crsName == null) {
            throw new CodecConfigurationException("Found invalid properties in the CoordinateReferenceSystem.");
        }
        return crsName;
    }

    private static void validateIsDocument(BsonReader reader) {
        BsonType currentType = reader.getCurrentBsonType();
        if (currentType == null) {
            currentType = reader.readBsonType();
        }
        if (!currentType.equals((Object)BsonType.DOCUMENT)) {
            throw new CodecConfigurationException("Invalid BsonType expecting a Document");
        }
    }

    private static void validateIsArray(BsonReader reader) {
        if (reader.getCurrentBsonType() != BsonType.ARRAY) {
            throw new CodecConfigurationException("Invalid BsonType expecting an Array");
        }
    }

    private GeometryDecoderHelper() {
    }
}

