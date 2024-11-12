/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

@Deprecated
public enum OrderBy {
    ASC(1),
    DESC(-1);

    private final int intRepresentation;

    private OrderBy(int intRepresentation) {
        this.intRepresentation = intRepresentation;
    }

    public int getIntRepresentation() {
        return this.intRepresentation;
    }

    public static OrderBy fromInt(int intRepresentation) {
        switch (intRepresentation) {
            case 1: {
                return ASC;
            }
            case -1: {
                return DESC;
            }
        }
        throw new IllegalArgumentException(intRepresentation + " is not a valid index Order");
    }
}

