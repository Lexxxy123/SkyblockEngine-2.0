/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerId;

public final class ConnectionPoolOpenedEvent {
    private final ServerId serverId;
    private final ConnectionPoolSettings settings;

    public ConnectionPoolOpenedEvent(ServerId serverId, ConnectionPoolSettings settings) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.settings = Assertions.notNull("settings", settings);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public ConnectionPoolSettings getSettings() {
        return this.settings;
    }

    public String toString() {
        return "ConnectionPoolOpenedEvent{serverId=" + this.serverId + "settings=" + this.settings + '}';
    }
}

