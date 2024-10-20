/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.LineString;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class LineStringCodec
extends AbstractGeometryCodec<LineString> {
    public LineStringCodec(CodecRegistry registry) {
        super(registry, LineString.class);
    }
}

