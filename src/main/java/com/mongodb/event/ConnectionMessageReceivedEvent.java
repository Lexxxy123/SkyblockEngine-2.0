/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.annotations.Beta;
import com.mongodb.connection.ConnectionId;
import org.bson.assertions.Assertions;

@Deprecated
@Beta
public final class ConnectionMessageReceivedEvent {
    private final int responseTo;
    private final int size;
    private final ConnectionId connectionId;

    public ConnectionMessageReceivedEvent(ConnectionId connectionId, int responseTo, int size) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
        this.responseTo = responseTo;
        this.size = size;
    }

    public int getResponseTo() {
        return this.responseTo;
    }

    public int getSize() {
        return this.size;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public String toString() {
        return "ConnectionMessageReceivedEvent{responseTo=" + this.responseTo + ", size=" + this.size + ", connectionId=" + this.connectionId + '}';
    }
}

