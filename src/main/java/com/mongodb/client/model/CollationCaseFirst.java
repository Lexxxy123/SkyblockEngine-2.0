/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

public enum CollationCaseFirst {
    UPPER("upper"),
    LOWER("lower"),
    OFF("off");

    private final String value;

    private CollationCaseFirst(String caseFirst) {
        this.value = caseFirst;
    }

    public String getValue() {
        return this.value;
    }

    public static CollationCaseFirst fromString(String collationCaseFirst) {
        if (collationCaseFirst != null) {
            for (CollationCaseFirst caseFirst : CollationCaseFirst.values()) {
                if (!collationCaseFirst.equals(caseFirst.value)) continue;
                return caseFirst;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid collationCaseFirst", collationCaseFirst));
    }
}

