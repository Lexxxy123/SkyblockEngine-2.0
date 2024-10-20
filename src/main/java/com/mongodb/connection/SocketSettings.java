/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ConnectionString;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import java.util.concurrent.TimeUnit;

@Immutable
public class SocketSettings {
    private final long connectTimeoutMS;
    private final long readTimeoutMS;
    private final boolean keepAlive;
    private final int receiveBufferSize;
    private final int sendBufferSize;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SocketSettings socketSettings) {
        return SocketSettings.builder().applySettings(socketSettings);
    }

    public int getConnectTimeout(TimeUnit timeUnit) {
        return (int)timeUnit.convert(this.connectTimeoutMS, TimeUnit.MILLISECONDS);
    }

    public int getReadTimeout(TimeUnit timeUnit) {
        return (int)timeUnit.convert(this.readTimeoutMS, TimeUnit.MILLISECONDS);
    }

    @Deprecated
    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    public int getReceiveBufferSize() {
        return this.receiveBufferSize;
    }

    public int getSendBufferSize() {
        return this.sendBufferSize;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        SocketSettings that = (SocketSettings)o2;
        if (this.connectTimeoutMS != that.connectTimeoutMS) {
            return false;
        }
        if (this.keepAlive != that.keepAlive) {
            return false;
        }
        if (this.readTimeoutMS != that.readTimeoutMS) {
            return false;
        }
        if (this.receiveBufferSize != that.receiveBufferSize) {
            return false;
        }
        return this.sendBufferSize == that.sendBufferSize;
    }

    public int hashCode() {
        int result = (int)(this.connectTimeoutMS ^ this.connectTimeoutMS >>> 32);
        result = 31 * result + (int)(this.readTimeoutMS ^ this.readTimeoutMS >>> 32);
        result = 31 * result + (this.keepAlive ? 1 : 0);
        result = 31 * result + this.receiveBufferSize;
        result = 31 * result + this.sendBufferSize;
        return result;
    }

    public String toString() {
        return "SocketSettings{connectTimeoutMS=" + this.connectTimeoutMS + ", readTimeoutMS=" + this.readTimeoutMS + ", keepAlive=" + this.keepAlive + ", receiveBufferSize=" + this.receiveBufferSize + ", sendBufferSize=" + this.sendBufferSize + '}';
    }

    SocketSettings(Builder builder) {
        this.connectTimeoutMS = builder.connectTimeoutMS;
        this.readTimeoutMS = builder.readTimeoutMS;
        this.keepAlive = builder.keepAlive;
        this.receiveBufferSize = builder.receiveBufferSize;
        this.sendBufferSize = builder.sendBufferSize;
    }

    public static final class Builder {
        private long connectTimeoutMS = 10000L;
        private long readTimeoutMS;
        private boolean keepAlive = true;
        private int receiveBufferSize;
        private int sendBufferSize;

        private Builder() {
        }

        public Builder applySettings(SocketSettings socketSettings) {
            Assertions.notNull("socketSettings", socketSettings);
            this.connectTimeoutMS = socketSettings.connectTimeoutMS;
            this.readTimeoutMS = socketSettings.readTimeoutMS;
            this.keepAlive = socketSettings.keepAlive;
            this.receiveBufferSize = socketSettings.receiveBufferSize;
            this.sendBufferSize = socketSettings.sendBufferSize;
            return this;
        }

        public Builder connectTimeout(int connectTimeout, TimeUnit timeUnit) {
            this.connectTimeoutMS = TimeUnit.MILLISECONDS.convert(connectTimeout, timeUnit);
            return this;
        }

        public Builder readTimeout(int readTimeout, TimeUnit timeUnit) {
            this.readTimeoutMS = TimeUnit.MILLISECONDS.convert(readTimeout, timeUnit);
            return this;
        }

        @Deprecated
        public Builder keepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public Builder receiveBufferSize(int receiveBufferSize) {
            this.receiveBufferSize = receiveBufferSize;
            return this;
        }

        public Builder sendBufferSize(int sendBufferSize) {
            this.sendBufferSize = sendBufferSize;
            return this;
        }

        public Builder applyConnectionString(ConnectionString connectionString) {
            Integer socketTimeout;
            Integer connectTimeout = connectionString.getConnectTimeout();
            if (connectTimeout != null) {
                this.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            }
            if ((socketTimeout = connectionString.getSocketTimeout()) != null) {
                this.readTimeout(socketTimeout, TimeUnit.MILLISECONDS);
            }
            return this;
        }

        public SocketSettings build() {
            return new SocketSettings(this);
        }
    }
}

