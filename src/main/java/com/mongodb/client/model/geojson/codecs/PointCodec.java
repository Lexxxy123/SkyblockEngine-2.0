/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class PointCodec
extends AbstractGeometryCodec<Point> {
    public PointCodec(CodecRegistry registry) {
        super(registry, Point.class);
    }
}

