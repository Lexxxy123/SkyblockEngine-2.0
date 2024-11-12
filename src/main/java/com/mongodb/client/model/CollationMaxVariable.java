/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

public enum CollationMaxVariable {
    PUNCT("punct"),
    SPACE("space");

    private final String value;

    private CollationMaxVariable(String caseFirst) {
        this.value = caseFirst;
    }

    public String getValue() {
        return this.value;
    }

    public static CollationMaxVariable fromString(String collationMaxVariable) {
        if (collationMaxVariable != null) {
            for (CollationMaxVariable maxVariable : CollationMaxVariable.values()) {
                if (!collationMaxVariable.equals(maxVariable.value)) continue;
                return maxVariable;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid collationMaxVariable", collationMaxVariable));
    }
}

