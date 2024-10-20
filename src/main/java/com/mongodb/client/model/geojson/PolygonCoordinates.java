/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.geojson.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PolygonCoordinates {
    private final List<Position> exterior;
    private final List<List<Position>> holes;

    public PolygonCoordinates(List<Position> exterior, List<Position> ... holes) {
        Assertions.notNull("exteriorRing", exterior);
        Assertions.doesNotContainNull("exterior", exterior);
        Assertions.isTrueArgument("ring must contain at least four positions", exterior.size() >= 4);
        Assertions.isTrueArgument("first and last position must be the same", exterior.get(0).equals(exterior.get(exterior.size() - 1)));
        this.exterior = Collections.unmodifiableList(exterior);
        ArrayList<List<Position>> holesList = new ArrayList<List<Position>>(holes.length);
        for (List<Position> hole : holes) {
            Assertions.notNull("interiorRing", hole);
            Assertions.doesNotContainNull("hole", hole);
            Assertions.isTrueArgument("ring must contain at least four positions", hole.size() >= 4);
            Assertions.isTrueArgument("first and last position must be the same", hole.get(0).equals(hole.get(hole.size() - 1)));
            holesList.add(Collections.unmodifiableList(hole));
        }
        this.holes = Collections.unmodifiableList(holesList);
    }

    public List<Position> getExterior() {
        return this.exterior;
    }

    public List<List<Position>> getHoles() {
        return this.holes;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        PolygonCoordinates that = (PolygonCoordinates)o2;
        if (!this.exterior.equals(that.exterior)) {
            return false;
        }
        return this.holes.equals(that.holes);
    }

    public int hashCode() {
        int result = this.exterior.hashCode();
        result = 31 * result + this.holes.hashCode();
        return result;
    }

    public String toString() {
        return "PolygonCoordinates{exterior=" + this.exterior + (this.holes.isEmpty() ? "" : ", holes=" + this.holes) + '}';
    }
}

