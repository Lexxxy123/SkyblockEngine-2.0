/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.geojson;

public enum GeoJsonObjectType {
    GEOMETRY_COLLECTION("GeometryCollection"),
    LINE_STRING("LineString"),
    MULTI_LINE_STRING("MultiLineString"),
    MULTI_POINT("MultiPoint"),
    MULTI_POLYGON("MultiPolygon"),
    POINT("Point"),
    POLYGON("Polygon");

    private final String typeName;

    public String getTypeName() {
        return this.typeName;
    }

    private GeoJsonObjectType(String typeName) {
        this.typeName = typeName;
    }
}

