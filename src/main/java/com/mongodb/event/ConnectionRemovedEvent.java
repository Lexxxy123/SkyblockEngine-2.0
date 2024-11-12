/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.connection.ConnectionId;
import org.bson.assertions.Assertions;

public final class ConnectionRemovedEvent {
    private final ConnectionId connectionId;
    private final Reason reason;

    @Deprecated
    public ConnectionRemovedEvent(ConnectionId connectionId) {
        this(connectionId, Reason.UNKNOWN);
    }

    public ConnectionRemovedEvent(ConnectionId connectionId, Reason reason) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
        this.reason = Assertions.notNull("reason", reason);
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public Reason getReason() {
        return this.reason;
    }

    public String toString() {
        return "ConnectionRemovedEvent{connectionId=" + this.connectionId + ", reason=" + (Object)((Object)this.reason) + '}';
    }

    public static enum Reason {
        UNKNOWN,
        STALE,
        MAX_IDLE_TIME_EXCEEDED,
        MAX_LIFE_TIME_EXCEEDED,
        ERROR,
        POOL_CLOSED;

    }
}

