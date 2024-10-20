/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MongoCompressor {
    public static final String LEVEL = "LEVEL";
    private final String name;
    private final Map<String, Object> properties;

    public static MongoCompressor createSnappyCompressor() {
        return new MongoCompressor("snappy");
    }

    public static MongoCompressor createZlibCompressor() {
        return new MongoCompressor("zlib");
    }

    public static MongoCompressor createZstdCompressor() {
        return new MongoCompressor("zstd");
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public <T> T getProperty(String key, T defaultValue) {
        Assertions.notNull("key", key);
        Object value = this.properties.get(key.toLowerCase());
        return (T)(value == null && !this.properties.containsKey(key) ? defaultValue : value);
    }

    public <T> T getPropertyNonNull(String key, T defaultValue) {
        T value = this.getProperty(key, defaultValue);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    public <T> MongoCompressor withProperty(String key, T value) {
        return new MongoCompressor(this, key, value);
    }

    private MongoCompressor(String name) {
        this.name = name;
        this.properties = Collections.emptyMap();
    }

    private <T> MongoCompressor(MongoCompressor from, String propertyKey, T propertyValue) {
        Assertions.notNull("propertyKey", propertyKey);
        this.name = from.name;
        this.properties = new HashMap<String, Object>(from.properties);
        this.properties.put(propertyKey.toLowerCase(), propertyValue);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoCompressor that = (MongoCompressor)o2;
        if (!this.name.equals(that.name)) {
            return false;
        }
        return this.properties.equals(that.properties);
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.properties.hashCode();
        return result;
    }

    public String toString() {
        return "MongoCompressor{name='" + this.name + '\'' + ", properties=" + this.properties + '}';
    }
}

