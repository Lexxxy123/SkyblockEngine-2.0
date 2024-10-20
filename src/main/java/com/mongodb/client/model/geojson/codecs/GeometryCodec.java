/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public final class GeometryCodec
extends AbstractGeometryCodec<Geometry> {
    public GeometryCodec(CodecRegistry registry) {
        super(registry, Geometry.class);
    }
}

