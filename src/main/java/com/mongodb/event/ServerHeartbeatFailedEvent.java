/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionId;
import java.util.concurrent.TimeUnit;

public final class ServerHeartbeatFailedEvent {
    private final ConnectionId connectionId;
    private final long elapsedTimeNanos;
    private final Throwable throwable;

    public ServerHeartbeatFailedEvent(ConnectionId connectionId, long elapsedTimeNanos, Throwable throwable) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
        Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
        this.elapsedTimeNanos = elapsedTimeNanos;
        this.throwable = Assertions.notNull("throwable", throwable);
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public long getElapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public String toString() {
        return "ServerHeartbeatFailedEvent{connectionId=" + this.connectionId + ", elapsedTimeNanos=" + this.elapsedTimeNanos + ", throwable=" + this.throwable + "} " + super.toString();
    }
}

