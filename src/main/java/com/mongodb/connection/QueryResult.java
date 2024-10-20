/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import java.util.List;

@Deprecated
public class QueryResult<T> {
    private final MongoNamespace namespace;
    private final List<T> results;
    private final long cursorId;
    private final ServerAddress serverAddress;

    public QueryResult(MongoNamespace namespace, List<T> results, long cursorId, ServerAddress serverAddress) {
        this.namespace = namespace;
        this.results = results;
        this.cursorId = cursorId;
        this.serverAddress = serverAddress;
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public ServerCursor getCursor() {
        return this.cursorId == 0L ? null : new ServerCursor(this.cursorId, this.serverAddress);
    }

    public List<T> getResults() {
        return this.results;
    }

    public ServerAddress getAddress() {
        return this.serverAddress;
    }
}

