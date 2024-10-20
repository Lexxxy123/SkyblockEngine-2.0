/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.annotations.Beta;
import com.mongodb.connection.ConnectionId;
import org.bson.assertions.Assertions;

@Deprecated
@Beta
public final class ConnectionMessagesSentEvent {
    private final ConnectionId connectionId;
    private final int requestId;
    private final int size;

    public ConnectionMessagesSentEvent(ConnectionId connectionId, int requestId, int size) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
        this.requestId = requestId;
        this.size = size;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public int getSize() {
        return this.size;
    }

    public String toString() {
        return "ConnectionMessagesSentEvent{requestId=" + this.requestId + ", size=" + this.size + ", connectionId=" + this.connectionId + '}';
    }
}

