/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ServerId;

public final class ServerOpeningEvent {
    private final ServerId serverId;

    public ServerOpeningEvent(ServerId serverId) {
        this.serverId = Assertions.notNull("serverId", serverId);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public String toString() {
        return "ServerOpeningEvent{serverId=" + this.serverId + '}';
    }
}

