/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.assertions.Assertions;

public enum ReadConcernLevel {
    LOCAL("local"),
    MAJORITY("majority"),
    LINEARIZABLE("linearizable"),
    SNAPSHOT("snapshot"),
    AVAILABLE("available");

    private final String value;

    private ReadConcernLevel(String readConcernLevel) {
        this.value = readConcernLevel;
    }

    public String getValue() {
        return this.value;
    }

    public static ReadConcernLevel fromString(String readConcernLevel) {
        Assertions.notNull("readConcernLevel", readConcernLevel);
        for (ReadConcernLevel level : ReadConcernLevel.values()) {
            if (!readConcernLevel.equalsIgnoreCase(level.value)) continue;
            return level;
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid readConcernLevel", readConcernLevel));
    }
}

