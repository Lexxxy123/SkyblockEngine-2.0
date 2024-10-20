/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.changestream;

public enum FullDocument {
    DEFAULT("default"),
    UPDATE_LOOKUP("updateLookup");

    private final String value;

    private FullDocument(String caseFirst) {
        this.value = caseFirst;
    }

    public String getValue() {
        return this.value;
    }

    public static FullDocument fromString(String changeStreamFullDocument) {
        if (changeStreamFullDocument != null) {
            for (FullDocument fullDocument : FullDocument.values()) {
                if (!changeStreamFullDocument.equals(fullDocument.value)) continue;
                return fullDocument;
            }
        }
        throw new IllegalArgumentException(String.format("'%s' is not a valid ChangeStreamFullDocument", changeStreamFullDocument));
    }
}

