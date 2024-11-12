/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.connection.ConnectionDescription;
import com.mongodb.event.CommandEvent;
import org.bson.BsonDocument;

public final class CommandStartedEvent
extends CommandEvent {
    private final String databaseName;
    private final BsonDocument command;

    public CommandStartedEvent(int requestId, ConnectionDescription connectionDescription, String databaseName, String commandName, BsonDocument command) {
        super(requestId, connectionDescription, commandName);
        this.command = command;
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public BsonDocument getCommand() {
        return this.command;
    }
}

