/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

public enum CollationAlternate {
    NON_IGNORABLE("non-ignorable"),
    SHIFTED("shifted");

    private final String value;

    private CollationAlternate(String caseFirst) {
        this.value = caseFirst;
    }

    public String getValue() {
        return this.value;
    }

    public static CollationAlternate fromString(String collationAlternate) {
        if (collationAlternate != null) {
            for (CollationAlternate alternate : CollationAlternate.values()) {
                if (!collationAlternate.equals(alternate.value)) continue;
                return alternate;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid collationAlternate", collationAlternate));
    }
}

