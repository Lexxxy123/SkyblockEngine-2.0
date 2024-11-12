/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.changestream;

public enum OperationType {
    INSERT("insert"),
    UPDATE("update"),
    REPLACE("replace"),
    DELETE("delete"),
    INVALIDATE("invalidate"),
    DROP("drop"),
    DROP_DATABASE("dropDatabase"),
    RENAME("rename"),
    OTHER("other");

    private final String value;

    private OperationType(String operationTypeName) {
        this.value = operationTypeName;
    }

    public String getValue() {
        return this.value;
    }

    public static OperationType fromString(String operationTypeName) {
        if (operationTypeName != null) {
            for (OperationType operationType : OperationType.values()) {
                if (!operationTypeName.equals(operationType.value)) continue;
                return operationType;
            }
        }
        return OTHER;
    }

    public String toString() {
        return "OperationType{value='" + this.value + "'}";
    }
}

