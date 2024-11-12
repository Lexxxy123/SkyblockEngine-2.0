/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;

public enum ValidationLevel {
    OFF("off"),
    STRICT("strict"),
    MODERATE("moderate");

    private final String value;

    private ValidationLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ValidationLevel fromString(String validationLevel) {
        Assertions.notNull("ValidationLevel", validationLevel);
        for (ValidationLevel action : ValidationLevel.values()) {
            if (!validationLevel.equalsIgnoreCase(action.value)) continue;
            return action;
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid ValidationLevel", validationLevel));
    }
}

