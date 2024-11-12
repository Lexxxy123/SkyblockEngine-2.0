/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.GeoJsonObjectType;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.PolygonCoordinates;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.lang.Nullable;
import java.util.List;

public final class Polygon
extends Geometry {
    private final PolygonCoordinates coordinates;

    public Polygon(List<Position> exterior, List<Position> ... holes) {
        this(new PolygonCoordinates(exterior, holes));
    }

    public Polygon(PolygonCoordinates coordinates) {
        this(null, coordinates);
    }

    public Polygon(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, PolygonCoordinates coordinates) {
        super(coordinateReferenceSystem);
        this.coordinates = Assertions.notNull("coordinates", coordinates);
    }

    @Override
    public GeoJsonObjectType getType() {
        return GeoJsonObjectType.POLYGON;
    }

    public PolygonCoordinates getCoordinates() {
        return this.coordinates;
    }

    public List<Position> getExterior() {
        return this.coordinates.getExterior();
    }

    public List<List<Position>> getHoles() {
        return this.coordinates.getHoles();
    }

    @Override
    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        if (!super.equals(o2)) {
            return false;
        }
        Polygon polygon = (Polygon)o2;
        return this.coordinates.equals(polygon.coordinates);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.coordinates.hashCode();
        return result;
    }

    public String toString() {
        return "Polygon{exterior=" + this.coordinates.getExterior() + (this.coordinates.getHoles().isEmpty() ? "" : ", holes=" + this.coordinates.getHoles()) + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
    }
}

