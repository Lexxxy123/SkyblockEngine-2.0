/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

public enum BucketGranularity {
    R5,
    R10,
    R20,
    R40,
    R80,
    SERIES_125("1-2-5"),
    E6,
    E12,
    E24,
    E48,
    E96,
    E192,
    POWERSOF2;

    private final String value;

    private BucketGranularity() {
        this.value = this.name();
    }

    private BucketGranularity(String name) {
        this.value = name;
    }

    public static BucketGranularity fromString(String value) {
        for (BucketGranularity granularity : BucketGranularity.values()) {
            if (!granularity.getValue().equals(value)) continue;
            return granularity;
        }
        throw new IllegalArgumentException("No Granularity exists for the value " + value);
    }

    public String getValue() {
        return this.value;
    }
}

