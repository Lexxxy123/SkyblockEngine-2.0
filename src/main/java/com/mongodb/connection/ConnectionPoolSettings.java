/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ConnectionString;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.event.ConnectionPoolListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Immutable
public class ConnectionPoolSettings {
    private final List<ConnectionPoolListener> connectionPoolListeners;
    private final int maxSize;
    private final int minSize;
    private final int maxWaitQueueSize;
    private final long maxWaitTimeMS;
    private final long maxConnectionLifeTimeMS;
    private final long maxConnectionIdleTimeMS;
    private final long maintenanceInitialDelayMS;
    private final long maintenanceFrequencyMS;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ConnectionPoolSettings connectionPoolSettings) {
        return ConnectionPoolSettings.builder().applySettings(connectionPoolSettings);
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public int getMinSize() {
        return this.minSize;
    }

    @Deprecated
    public int getMaxWaitQueueSize() {
        return this.maxWaitQueueSize;
    }

    public long getMaxWaitTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxWaitTimeMS, TimeUnit.MILLISECONDS);
    }

    public long getMaxConnectionLifeTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxConnectionLifeTimeMS, TimeUnit.MILLISECONDS);
    }

    public long getMaxConnectionIdleTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxConnectionIdleTimeMS, TimeUnit.MILLISECONDS);
    }

    public long getMaintenanceInitialDelay(TimeUnit timeUnit) {
        return timeUnit.convert(this.maintenanceInitialDelayMS, TimeUnit.MILLISECONDS);
    }

    public long getMaintenanceFrequency(TimeUnit timeUnit) {
        return timeUnit.convert(this.maintenanceFrequencyMS, TimeUnit.MILLISECONDS);
    }

    public List<ConnectionPoolListener> getConnectionPoolListeners() {
        return this.connectionPoolListeners;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ConnectionPoolSettings that = (ConnectionPoolSettings)o2;
        if (this.maxConnectionIdleTimeMS != that.maxConnectionIdleTimeMS) {
            return false;
        }
        if (this.maxConnectionLifeTimeMS != that.maxConnectionLifeTimeMS) {
            return false;
        }
        if (this.maxSize != that.maxSize) {
            return false;
        }
        if (this.minSize != that.minSize) {
            return false;
        }
        if (this.maintenanceInitialDelayMS != that.maintenanceInitialDelayMS) {
            return false;
        }
        if (this.maintenanceFrequencyMS != that.maintenanceFrequencyMS) {
            return false;
        }
        if (this.maxWaitQueueSize != that.maxWaitQueueSize) {
            return false;
        }
        if (this.maxWaitTimeMS != that.maxWaitTimeMS) {
            return false;
        }
        return this.connectionPoolListeners.equals(that.connectionPoolListeners);
    }

    public int hashCode() {
        int result = this.maxSize;
        result = 31 * result + this.minSize;
        result = 31 * result + this.maxWaitQueueSize;
        result = 31 * result + (int)(this.maxWaitTimeMS ^ this.maxWaitTimeMS >>> 32);
        result = 31 * result + (int)(this.maxConnectionLifeTimeMS ^ this.maxConnectionLifeTimeMS >>> 32);
        result = 31 * result + (int)(this.maxConnectionIdleTimeMS ^ this.maxConnectionIdleTimeMS >>> 32);
        result = 31 * result + (int)(this.maintenanceInitialDelayMS ^ this.maintenanceInitialDelayMS >>> 32);
        result = 31 * result + (int)(this.maintenanceFrequencyMS ^ this.maintenanceFrequencyMS >>> 32);
        result = 31 * result + this.connectionPoolListeners.hashCode();
        return result;
    }

    public String toString() {
        return "ConnectionPoolSettings{maxSize=" + this.maxSize + ", minSize=" + this.minSize + ", maxWaitQueueSize=" + this.maxWaitQueueSize + ", maxWaitTimeMS=" + this.maxWaitTimeMS + ", maxConnectionLifeTimeMS=" + this.maxConnectionLifeTimeMS + ", maxConnectionIdleTimeMS=" + this.maxConnectionIdleTimeMS + ", maintenanceInitialDelayMS=" + this.maintenanceInitialDelayMS + ", maintenanceFrequencyMS=" + this.maintenanceFrequencyMS + ", connectionPoolListeners=" + this.connectionPoolListeners + '}';
    }

    ConnectionPoolSettings(Builder builder) {
        Assertions.isTrue("maxSize > 0", builder.maxSize > 0);
        Assertions.isTrue("minSize >= 0", builder.minSize >= 0);
        Assertions.isTrue("maxWaitQueueSize >= 0", builder.maxWaitQueueSize >= 0);
        Assertions.isTrue("maintenanceInitialDelayMS >= 0", builder.maintenanceInitialDelayMS >= 0L);
        Assertions.isTrue("maxConnectionLifeTime >= 0", builder.maxConnectionLifeTimeMS >= 0L);
        Assertions.isTrue("maxConnectionIdleTime >= 0", builder.maxConnectionIdleTimeMS >= 0L);
        Assertions.isTrue("sizeMaintenanceFrequency > 0", builder.maintenanceFrequencyMS > 0L);
        Assertions.isTrue("maxSize >= minSize", builder.maxSize >= builder.minSize);
        this.maxSize = builder.maxSize;
        this.minSize = builder.minSize;
        this.maxWaitQueueSize = builder.maxWaitQueueSize;
        this.maxWaitTimeMS = builder.maxWaitTimeMS;
        this.maxConnectionLifeTimeMS = builder.maxConnectionLifeTimeMS;
        this.maxConnectionIdleTimeMS = builder.maxConnectionIdleTimeMS;
        this.maintenanceInitialDelayMS = builder.maintenanceInitialDelayMS;
        this.maintenanceFrequencyMS = builder.maintenanceFrequencyMS;
        this.connectionPoolListeners = Collections.unmodifiableList(builder.connectionPoolListeners);
    }

    @NotThreadSafe
    public static final class Builder {
        private List<ConnectionPoolListener> connectionPoolListeners = new ArrayList<ConnectionPoolListener>();
        private int maxSize = 100;
        private int minSize;
        private int maxWaitQueueSize = 500;
        private long maxWaitTimeMS = 120000L;
        private long maxConnectionLifeTimeMS;
        private long maxConnectionIdleTimeMS;
        private long maintenanceInitialDelayMS;
        private long maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.MINUTES);

        Builder() {
        }

        public Builder applySettings(ConnectionPoolSettings connectionPoolSettings) {
            Assertions.notNull("connectionPoolSettings", connectionPoolSettings);
            this.connectionPoolListeners = new ArrayList<ConnectionPoolListener>(connectionPoolSettings.connectionPoolListeners);
            this.maxSize = connectionPoolSettings.maxSize;
            this.minSize = connectionPoolSettings.minSize;
            this.maxWaitQueueSize = connectionPoolSettings.maxWaitQueueSize;
            this.maxWaitTimeMS = connectionPoolSettings.maxWaitTimeMS;
            this.maxConnectionLifeTimeMS = connectionPoolSettings.maxConnectionLifeTimeMS;
            this.maxConnectionIdleTimeMS = connectionPoolSettings.maxConnectionIdleTimeMS;
            this.maintenanceInitialDelayMS = connectionPoolSettings.maintenanceInitialDelayMS;
            this.maintenanceFrequencyMS = connectionPoolSettings.maintenanceFrequencyMS;
            return this;
        }

        public Builder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder minSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        @Deprecated
        public Builder maxWaitQueueSize(int maxWaitQueueSize) {
            this.maxWaitQueueSize = maxWaitQueueSize;
            return this;
        }

        public Builder maxWaitTime(long maxWaitTime, TimeUnit timeUnit) {
            this.maxWaitTimeMS = TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit);
            return this;
        }

        public Builder maxConnectionLifeTime(long maxConnectionLifeTime, TimeUnit timeUnit) {
            this.maxConnectionLifeTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionLifeTime, timeUnit);
            return this;
        }

        public Builder maxConnectionIdleTime(long maxConnectionIdleTime, TimeUnit timeUnit) {
            this.maxConnectionIdleTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionIdleTime, timeUnit);
            return this;
        }

        public Builder maintenanceInitialDelay(long maintenanceInitialDelay, TimeUnit timeUnit) {
            this.maintenanceInitialDelayMS = TimeUnit.MILLISECONDS.convert(maintenanceInitialDelay, timeUnit);
            return this;
        }

        public Builder maintenanceFrequency(long maintenanceFrequency, TimeUnit timeUnit) {
            this.maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(maintenanceFrequency, timeUnit);
            return this;
        }

        public Builder addConnectionPoolListener(ConnectionPoolListener connectionPoolListener) {
            this.connectionPoolListeners.add(Assertions.notNull("connectionPoolListener", connectionPoolListener));
            return this;
        }

        public ConnectionPoolSettings build() {
            return new ConnectionPoolSettings(this);
        }

        public Builder applyConnectionString(ConnectionString connectionString) {
            Integer threadsAllowedToBlockForConnectionMultiplier;
            Integer maxConnectionLifeTime;
            Integer maxConnectionIdleTime;
            Integer maxWaitTime;
            Integer minConnectionPoolSize;
            Integer maxConnectionPoolSize = connectionString.getMaxConnectionPoolSize();
            if (maxConnectionPoolSize != null) {
                this.maxSize(maxConnectionPoolSize);
            }
            if ((minConnectionPoolSize = connectionString.getMinConnectionPoolSize()) != null) {
                this.minSize(minConnectionPoolSize);
            }
            if ((maxWaitTime = connectionString.getMaxWaitTime()) != null) {
                this.maxWaitTime(maxWaitTime.intValue(), TimeUnit.MILLISECONDS);
            }
            if ((maxConnectionIdleTime = connectionString.getMaxConnectionIdleTime()) != null) {
                this.maxConnectionIdleTime(maxConnectionIdleTime.intValue(), TimeUnit.MILLISECONDS);
            }
            if ((maxConnectionLifeTime = connectionString.getMaxConnectionLifeTime()) != null) {
                this.maxConnectionLifeTime(maxConnectionLifeTime.intValue(), TimeUnit.MILLISECONDS);
            }
            if ((threadsAllowedToBlockForConnectionMultiplier = connectionString.getThreadsAllowedToBlockForConnectionMultiplier()) != null) {
                this.maxWaitQueueSize(threadsAllowedToBlockForConnectionMultiplier * this.maxSize);
            }
            return this;
        }
    }
}

