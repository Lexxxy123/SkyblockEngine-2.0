/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

enum OpCode {
    OP_REPLY(1),
    OP_UPDATE(2001),
    OP_INSERT(2002),
    OP_QUERY(2004),
    OP_GETMORE(2005),
    OP_DELETE(2006),
    OP_KILL_CURSORS(2007),
    OP_COMPRESSED(2012),
    OP_MSG(2013);

    private final int value;

    private OpCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

