/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.GeometryCollection;
import com.mongodb.client.model.geojson.LineString;
import com.mongodb.client.model.geojson.MultiLineString;
import com.mongodb.client.model.geojson.MultiPoint;
import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.NamedCoordinateReferenceSystem;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.codecs.GeometryCodec;
import com.mongodb.client.model.geojson.codecs.GeometryCollectionCodec;
import com.mongodb.client.model.geojson.codecs.LineStringCodec;
import com.mongodb.client.model.geojson.codecs.MultiLineStringCodec;
import com.mongodb.client.model.geojson.codecs.MultiPointCodec;
import com.mongodb.client.model.geojson.codecs.MultiPolygonCodec;
import com.mongodb.client.model.geojson.codecs.NamedCoordinateReferenceSystemCodec;
import com.mongodb.client.model.geojson.codecs.PointCodec;
import com.mongodb.client.model.geojson.codecs.PolygonCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class GeoJsonCodecProvider
implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Polygon.class)) {
            return new PolygonCodec(registry);
        }
        if (clazz.equals(Point.class)) {
            return new PointCodec(registry);
        }
        if (clazz.equals(LineString.class)) {
            return new LineStringCodec(registry);
        }
        if (clazz.equals(MultiPoint.class)) {
            return new MultiPointCodec(registry);
        }
        if (clazz.equals(MultiLineString.class)) {
            return new MultiLineStringCodec(registry);
        }
        if (clazz.equals(MultiPolygon.class)) {
            return new MultiPolygonCodec(registry);
        }
        if (clazz.equals(GeometryCollection.class)) {
            return new GeometryCollectionCodec(registry);
        }
        if (clazz.equals(NamedCoordinateReferenceSystem.class)) {
            return new NamedCoordinateReferenceSystemCodec();
        }
        if (clazz.equals(Geometry.class)) {
            return new GeometryCodec(registry);
        }
        return null;
    }
}

