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
import java.util.Collections;
import java.util.List;

public final class LineString
extends Geometry {
    private final List<Position> coordinates;

    public LineString(List<Position> coordinates) {
        this(null, coordinates);
    }

    public LineString(@Nullable CoordinateReferenceSystem coordinateReferenceSystem, List<Position> coordinates) {
        super(coordinateReferenceSystem);
        Assertions.notNull("coordinates", coordinates);
        Assertions.isTrueArgument("coordinates must contain at least two positions", coordinates.size() >= 2);
        Assertions.doesNotContainNull("coordinates", coordinates);
        this.coordinates = Collections.unmodifiableList(coordinates);
    }

    @Override
    public GeoJsonObjectType getType() {
        return GeoJsonObjectType.LINE_STRING;
    }

    public List<Position> getCoordinates() {
        return this.coordinates;
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
        LineString lineString = (LineString)o2;
        return this.coordinates.equals(lineString.coordinates);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.coordinates.hashCode();
    }

    public String toString() {
        return "LineString{coordinates=" + this.coordinates + (this.getCoordinateReferenceSystem() == null ? "" : ", coordinateReferenceSystem=" + this.getCoordinateReferenceSystem()) + '}';
    }
}

