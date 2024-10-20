/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.event.CommandEvent;
import java.util.concurrent.TimeUnit;

public final class CommandFailedEvent
extends CommandEvent {
    private final long elapsedTimeNanos;
    private final Throwable throwable;

    public CommandFailedEvent(int requestId, ConnectionDescription connectionDescription, String commandName, long elapsedTimeNanos, Throwable throwable) {
        super(requestId, connectionDescription, commandName);
        Assertions.isTrueArgument("elapsed time is not negative", elapsedTimeNanos >= 0L);
        this.elapsedTimeNanos = elapsedTimeNanos;
        this.throwable = throwable;
    }

    public long getElapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.elapsedTimeNanos, TimeUnit.NANOSECONDS);
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}

