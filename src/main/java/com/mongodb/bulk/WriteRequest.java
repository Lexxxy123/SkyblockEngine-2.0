/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

@Deprecated
public abstract class WriteRequest {
    WriteRequest() {
    }

    public abstract Type getType();

    public static enum Type {
        INSERT,
        UPDATE,
        REPLACE,
        DELETE;

    }
}

