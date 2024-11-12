/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson.codecs;

import com.mongodb.client.model.geojson.GeometryCollection;
import com.mongodb.client.model.geojson.codecs.AbstractGeometryCodec;
import org.bson.codecs.configuration.CodecRegistry;

public class GeometryCollectionCodec
extends AbstractGeometryCodec<GeometryCollection> {
    public GeometryCollectionCodec(CodecRegistry registry) {
        super(registry, GeometryCollection.class);
    }
}

