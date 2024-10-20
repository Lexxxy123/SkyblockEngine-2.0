/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.MultiPolygon;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class MultiPolygonCodec
extends AbstractGeometryCodec<MultiPolygon> {
    public MultiPolygonCodec(CodecRegistry registry) {
        super(registry, MultiPolygon.class);
    }
}

