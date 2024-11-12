/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionId;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;

public final class ServerHeartbeatSucceededEvent {
    private final ConnectionId connectionId;
    private final BsonDocument reply;
    private final long elapsedTimeNanos;

    public ServerHeartbeatSucceededEvent(ConnectionId connectionId, BsonDocument reply, long elapsedTimeNanos) {
        this.connectionId = Assertions.notNull("connectionId", connectionId);
        this.reply = Assertions.notNull("reply", reply);
        Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
        this.elapsedTimeNanos = elapsedTimeNanos;
    }

    public ConnectionId getConnectionId() {
        return this.connectionId;
    }

    public BsonDocument getReply() {
        return this.reply;
    }

    public long getElapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
    }

    public String toString() {
        return "ServerHeartbeatSucceededEvent{connectionId=" + this.connectionId + ", reply=" + this.reply + ", elapsedTimeNanos=" + this.elapsedTimeNanos + "} ";
    }
}

