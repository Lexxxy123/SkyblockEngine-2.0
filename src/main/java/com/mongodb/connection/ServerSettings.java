/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ConnectionString;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerMonitorListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Immutable
public class ServerSettings {
    private final long heartbeatFrequencyMS;
    private final long minHeartbeatFrequencyMS;
    private final List<ServerListener> serverListeners;
    private final List<ServerMonitorListener> serverMonitorListeners;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ServerSettings serverSettings) {
        return ServerSettings.builder().applySettings(serverSettings);
    }

    public long getHeartbeatFrequency(TimeUnit timeUnit) {
        return timeUnit.convert(this.heartbeatFrequencyMS, TimeUnit.MILLISECONDS);
    }

    public long getMinHeartbeatFrequency(TimeUnit timeUnit) {
        return timeUnit.convert(this.minHeartbeatFrequencyMS, TimeUnit.MILLISECONDS);
    }

    public List<ServerListener> getServerListeners() {
        return this.serverListeners;
    }

    public List<ServerMonitorListener> getServerMonitorListeners() {
        return this.serverMonitorListeners;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ServerSettings that = (ServerSettings)o2;
        if (this.heartbeatFrequencyMS != that.heartbeatFrequencyMS) {
            return false;
        }
        if (this.minHeartbeatFrequencyMS != that.minHeartbeatFrequencyMS) {
            return false;
        }
        if (!this.serverListeners.equals(that.serverListeners)) {
            return false;
        }
        return this.serverMonitorListeners.equals(that.serverMonitorListeners);
    }

    public int hashCode() {
        int result = (int)(this.heartbeatFrequencyMS ^ this.heartbeatFrequencyMS >>> 32);
        result = 31 * result + (int)(this.minHeartbeatFrequencyMS ^ this.minHeartbeatFrequencyMS >>> 32);
        result = 31 * result + this.serverListeners.hashCode();
        result = 31 * result + this.serverMonitorListeners.hashCode();
        return result;
    }

    public String toString() {
        return "ServerSettings{heartbeatFrequencyMS=" + this.heartbeatFrequencyMS + ", minHeartbeatFrequencyMS=" + this.minHeartbeatFrequencyMS + ", serverListeners='" + this.serverListeners + '\'' + ", serverMonitorListeners='" + this.serverMonitorListeners + '\'' + '}';
    }

    ServerSettings(Builder builder) {
        this.heartbeatFrequencyMS = builder.heartbeatFrequencyMS;
        this.minHeartbeatFrequencyMS = builder.minHeartbeatFrequencyMS;
        this.serverListeners = Collections.unmodifiableList(builder.serverListeners);
        this.serverMonitorListeners = Collections.unmodifiableList(builder.serverMonitorListeners);
    }

    @NotThreadSafe
    public static final class Builder {
        private long heartbeatFrequencyMS = 10000L;
        private long minHeartbeatFrequencyMS = 500L;
        private List<ServerListener> serverListeners = new ArrayList<ServerListener>();
        private List<ServerMonitorListener> serverMonitorListeners = new ArrayList<ServerMonitorListener>();

        private Builder() {
        }

        public Builder applySettings(ServerSettings serverSettings) {
            Assertions.notNull("serverSettings", serverSettings);
            this.heartbeatFrequencyMS = serverSettings.heartbeatFrequencyMS;
            this.minHeartbeatFrequencyMS = serverSettings.minHeartbeatFrequencyMS;
            this.serverListeners = new ArrayList<ServerListener>(serverSettings.serverListeners);
            this.serverMonitorListeners = new ArrayList<ServerMonitorListener>(serverSettings.serverMonitorListeners);
            return this;
        }

        public Builder heartbeatFrequency(long heartbeatFrequency, TimeUnit timeUnit) {
            this.heartbeatFrequencyMS = TimeUnit.MILLISECONDS.convert(heartbeatFrequency, timeUnit);
            return this;
        }

        public Builder minHeartbeatFrequency(long minHeartbeatFrequency, TimeUnit timeUnit) {
            this.minHeartbeatFrequencyMS = TimeUnit.MILLISECONDS.convert(minHeartbeatFrequency, timeUnit);
            return this;
        }

        public Builder addServerListener(ServerListener serverListener) {
            Assertions.notNull("serverListener", serverListener);
            this.serverListeners.add(serverListener);
            return this;
        }

        public Builder addServerMonitorListener(ServerMonitorListener serverMonitorListener) {
            Assertions.notNull("serverMonitorListener", serverMonitorListener);
            this.serverMonitorListeners.add(serverMonitorListener);
            return this;
        }

        public Builder applyConnectionString(ConnectionString connectionString) {
            Integer heartbeatFrequency = connectionString.getHeartbeatFrequency();
            if (heartbeatFrequency != null) {
                this.heartbeatFrequencyMS = heartbeatFrequency.intValue();
            }
            return this;
        }

        public ServerSettings build() {
            return new ServerSettings(this);
        }
    }
}

