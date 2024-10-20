/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

@Deprecated
public class JSONParseException
extends RuntimeException {
    private static final long serialVersionUID = -4415279469780082174L;
    final String jsonString;
    final int pos;

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(this.jsonString);
        sb.append("\n");
        for (int i2 = 0; i2 < this.pos; ++i2) {
            sb.append(" ");
        }
        sb.append("^");
        return sb.toString();
    }

    public JSONParseException(String jsonString, int position) {
        this.jsonString = jsonString;
        this.pos = position;
    }

    public JSONParseException(String jsonString, int position, Throwable cause) {
        super(cause);
        this.jsonString = jsonString;
        this.pos = position;
    }
}

