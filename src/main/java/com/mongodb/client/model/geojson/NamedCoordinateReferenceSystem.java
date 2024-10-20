/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson;

import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.CoordinateReferenceSystemType;

@Immutable
public final class NamedCoordinateReferenceSystem
extends CoordinateReferenceSystem {
    public static final NamedCoordinateReferenceSystem EPSG_4326 = new NamedCoordinateReferenceSystem("EPSG:4326");
    public static final NamedCoordinateReferenceSystem CRS_84 = new NamedCoordinateReferenceSystem("urn:ogc:def:crs:OGC:1.3:CRS84");
    public static final NamedCoordinateReferenceSystem EPSG_4326_STRICT_WINDING = new NamedCoordinateReferenceSystem("urn:x-mongodb:crs:strictwinding:EPSG:4326");
    private final String name;

    public NamedCoordinateReferenceSystem(String name) {
        this.name = Assertions.notNull("name", name);
    }

    @Override
    public CoordinateReferenceSystemType getType() {
        return CoordinateReferenceSystemType.NAME;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        NamedCoordinateReferenceSystem that = (NamedCoordinateReferenceSystem)o2;
        return this.name.equals(that.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        return "NamedCoordinateReferenceSystem{name='" + this.name + '\'' + '}';
    }
}

