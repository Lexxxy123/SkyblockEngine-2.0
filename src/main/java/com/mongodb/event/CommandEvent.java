/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.connection.ConnectionDescription;

public abstract class CommandEvent {
    private final int requestId;
    private final ConnectionDescription connectionDescription;
    private final String commandName;

    public CommandEvent(int requestId, ConnectionDescription connectionDescription, String commandName) {
        this.requestId = requestId;
        this.connectionDescription = connectionDescription;
        this.commandName = commandName;
    }

    public int getRequestId() {
        return this.requestId;
    }

    public ConnectionDescription getConnectionDescription() {
        return this.connectionDescription;
    }

    public String getCommandName() {
        return this.commandName;
    }
}

