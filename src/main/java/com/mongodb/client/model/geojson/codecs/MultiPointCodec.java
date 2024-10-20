/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.MultiPoint;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class MultiPointCodec
extends AbstractGeometryCodec<MultiPoint> {
    public MultiPointCodec(CodecRegistry registry) {
        super(registry, MultiPoint.class);
    }
}

