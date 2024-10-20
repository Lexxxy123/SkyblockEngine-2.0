/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.GeoJsonObjectType;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.lang.Nullable;

public final class Point
extends Geometry {
    private final Position coordinate;

    public Point(Position coordinate) {
        this(null, coordinate);
    }

    public Point(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, Position coordinate) {
        super(coordinateReferenceSystem);
        this.coordinate = Assertions.notNull("coordinates", coordinate);
    }

    @Override
    public GeoJsonObjectType getType() {
        return GeoJsonObjectType.POINT;
    }

    public Position getCoordinates() {
        return this.coordinate;
    }

    public Position getPosition() {
        return this.coordinate;
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
        Point point = (Point)o2;
        return this.coordinate.equals(point.coordinate);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.coordinate.hashCode();
    }

    public String toString() {
        return "Point{coordinate=" + this.coordinate + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
    }
}

