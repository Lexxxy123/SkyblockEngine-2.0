/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;

class MongoCursorAdapter
implements Cursor {
    private final MongoCursor<DBObject> cursor;

    MongoCursorAdapter(MongoCursor<DBObject> cursor) {
        this.cursor = cursor;
    }

    @Override
    public long getCursorId() {
        ServerCursor serverCursor = this.cursor.getServerCursor();
        if (serverCursor == null) {
            return 0L;
        }
        return serverCursor.getId();
    }

    @Override
    public ServerAddress getServerAddress() {
        return this.cursor.getServerAddress();
    }

    @Override
    public void close() {
        this.cursor.close();
    }

    @Override
    public boolean hasNext() {
        return this.cursor.hasNext();
    }

    @Override
    public DBObject next() {
        return this.cursor.next();
    }

    @Override
    public void remove() {
        this.cursor.remove();
    }
}

