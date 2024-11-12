/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.connection.ServerId;

@Deprecated
public final class ConnectionPoolWaitQueueExitedEvent {
    private final ServerId serverId;

    public ConnectionPoolWaitQueueExitedEvent(ServerId serverId) {
        this.serverId = serverId;
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public String toString() {
        return "ConnectionPoolWaitQueueExitedEvent{serverId=" + this.serverId + '}';
    }
}

