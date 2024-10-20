/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;

public enum ValidationAction {
    ERROR("error"),
    WARN("warn");

    private final String value;

    private ValidationAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ValidationAction fromString(String validationAction) {
        Assertions.notNull("validationAction", validationAction);
        for (ValidationAction action : ValidationAction.values()) {
            if (!validationAction.equalsIgnoreCase(action.value)) continue;
            return action;
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid validationAction", validationAction));
    }
}

