/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class PolygonCodec
extends AbstractGeometryCodec<Polygon> {
    public PolygonCodec(CodecRegistry registry) {
        super(registry, Polygon.class);
    }
}

