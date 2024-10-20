/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

public enum MapReduceAction {
    REPLACE("replace"),
    MERGE("merge"),
    REDUCE("reduce");

    private final String value;

    private MapReduceAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

