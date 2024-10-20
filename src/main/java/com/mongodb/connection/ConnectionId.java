/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ServerId;
import java.util.concurrent.atomic.AtomicInteger;

@Immutable
public final class ConnectionId {
    private static final AtomicInteger INCREMENTING_ID = new AtomicInteger();
    private final ServerId serverId;
    private final int localValue;
    private final Integer serverValue;
    private final String stringValue;

    public ConnectionId(ServerId serverId) {
        this(serverId, INCREMENTING_ID.incrementAndGet(), null);
    }

    public ConnectionId(ServerId serverId, int localValue, Integer serverValue) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.localValue = localValue;
        this.serverValue = serverValue;
        this.stringValue = serverValue == null ? String.format("connectionId{localValue:%s}", localValue) : String.format("connectionId{localValue:%s, serverValue:%s}", localValue, serverValue);
    }

    public ConnectionId withServerValue(int serverValue) {
        Assertions.isTrue("server value is null", this.serverValue == null);
        return new ConnectionId(this.serverId, this.localValue, serverValue);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public int getLocalValue() {
        return this.localValue;
    }

    public Integer getServerValue() {
        return this.serverValue;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ConnectionId that = (ConnectionId)o2;
        if (this.localValue != that.localValue) {
            return false;
        }
        if (!this.serverId.equals(that.serverId)) {
            return false;
        }
        return !(this.serverValue != null ? !this.serverValue.equals(that.serverValue) : that.serverValue != null);
    }

    public int hashCode() {
        int result = this.serverId.hashCode();
        result = 31 * result + this.localValue;
        result = 31 * result + (this.serverValue != null ? this.serverValue.hashCode() : 0);
        return result;
    }

    public String toString() {
        return this.stringValue;
    }
}

