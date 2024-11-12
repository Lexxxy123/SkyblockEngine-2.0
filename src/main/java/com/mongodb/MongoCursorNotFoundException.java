/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoQueryException;
import com.mongodb.ServerAddress;

public class MongoCursorNotFoundException
extends MongoQueryException {
    private static final long serialVersionUID = -4415279469780082174L;
    private final long cursorId;
    private final ServerAddress serverAddress;

    public MongoCursorNotFoundException(long cursorId, ServerAddress serverAddress) {
        super(serverAddress, -5, "Cursor " + cursorId + " not found on server " + serverAddress);
        this.cursorId = cursorId;
        this.serverAddress = serverAddress;
    }

    public long getCursorId() {
        return this.cursorId;
    }

    @Override
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
}

