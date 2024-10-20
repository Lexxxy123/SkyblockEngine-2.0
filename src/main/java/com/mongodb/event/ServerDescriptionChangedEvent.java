/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerId;

public final class ServerDescriptionChangedEvent {
    private final ServerId serverId;
    private final ServerDescription newDescription;
    private final ServerDescription previousDescription;

    public ServerDescriptionChangedEvent(ServerId serverId, ServerDescription newDescription, ServerDescription previousDescription) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.newDescription = Assertions.notNull("newDescription", newDescription);
        this.previousDescription = Assertions.notNull("previousDescription", previousDescription);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public ServerDescription getNewDescription() {
        return this.newDescription;
    }

    public ServerDescription getPreviousDescription() {
        return this.previousDescription;
    }

    public String toString() {
        return "ServerDescriptionChangedEvent{serverId=" + this.serverId + ", newDescription=" + this.newDescription + ", previousDescription=" + this.previousDescription + '}';
    }
}

