/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.event.CommandEvent;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;

public final class CommandSucceededEvent
extends CommandEvent {
    private final BsonDocument response;
    private final long elapsedTimeNanos;

    public CommandSucceededEvent(int requestId, ConnectionDescription connectionDescription, String commandName, BsonDocument response, long elapsedTimeNanos) {
        super(requestId, connectionDescription, commandName);
        this.response = response;
        Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
        this.elapsedTimeNanos = elapsedTimeNanos;
    }

    public long getElapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
    }

    public BsonDocument getResponse() {
        return this.response;
    }
}

