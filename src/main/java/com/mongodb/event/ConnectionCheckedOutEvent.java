/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionId;

public final class ConnectionCheckedOutEvent {
    private final ConnectionId connectionId;

    public ConnectionCheckedOutEvent(ConnectionId connectionId) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public String toString() {
        return "ConnectionCheckedOutEvent{connectionId=" + this.connectionId + '}';
    }
}

