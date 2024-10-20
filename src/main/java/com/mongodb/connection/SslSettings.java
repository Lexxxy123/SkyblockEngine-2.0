/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ConnectionString;
import com.mongodb.MongoInternalException;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import javax.net.ssl.SSLContext;

@Immutable
public class SslSettings {
    private final boolean enabled;
    private final boolean invalidHostNameAllowed;
    private final SSLContext context;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SslSettings sslSettings) {
        return SslSettings.builder().applySettings(sslSettings);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isInvalidHostNameAllowed() {
        return this.invalidHostNameAllowed;
    }

    public SSLContext getContext() {
        return this.context;
    }

    SslSettings(Builder builder) {
        this.enabled = builder.enabled;
        this.invalidHostNameAllowed = builder.invalidHostNameAllowed;
        if (this.enabled && !this.invalidHostNameAllowed && System.getProperty("java.version").startsWith("1.6.")) {
            throw new MongoInternalException("By default, SSL connections are only supported on Java 7 or later.  If the application must run on Java 6, you must set the SslSettings.invalidHostNameAllowed property to true");
        }
        this.context = builder.context;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        SslSettings that = (SslSettings)o2;
        if (this.enabled != that.enabled) {
            return false;
        }
        if (this.invalidHostNameAllowed != that.invalidHostNameAllowed) {
            return false;
        }
        return this.context != null ? this.context.equals(that.context) : that.context == null;
    }

    public int hashCode() {
        int result = this.enabled ? 1 : 0;
        result = 31 * result + (this.invalidHostNameAllowed ? 1 : 0);
        result = 31 * result + (this.context != null ? this.context.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SslSettings{enabled=" + this.enabled + ", invalidHostNameAllowed=" + this.invalidHostNameAllowed + ", context=" + this.context + '}';
    }

    @NotThreadSafe
    public static final class Builder {
        private boolean enabled;
        private boolean invalidHostNameAllowed;
        private SSLContext context;

        private Builder() {
        }

        public Builder applySettings(SslSettings sslSettings) {
            Assertions.notNull("sslSettings", sslSettings);
            this.enabled = sslSettings.enabled;
            this.invalidHostNameAllowed = sslSettings.invalidHostNameAllowed;
            this.context = sslSettings.context;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder invalidHostNameAllowed(boolean invalidHostNameAllowed) {
            this.invalidHostNameAllowed = invalidHostNameAllowed;
            return this;
        }

        public Builder context(SSLContext context) {
            this.context = context;
            return this;
        }

        public Builder applyConnectionString(ConnectionString connectionString) {
            Boolean sslInvalidHostnameAllowed;
            Boolean sslEnabled = connectionString.getSslEnabled();
            if (sslEnabled != null) {
                this.enabled = sslEnabled;
            }
            if ((sslInvalidHostnameAllowed = connectionString.getSslInvalidHostnameAllowed()) != null) {
                this.invalidHostNameAllowed = sslInvalidHostnameAllowed;
            }
            return this;
        }

        public SslSettings build() {
            return new SslSettings(this);
        }
    }
}

