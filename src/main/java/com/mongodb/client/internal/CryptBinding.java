/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadWriteBinding;
import com.mongodb.client.internal.Crypt;
import com.mongodb.client.internal.CryptConnection;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import com.mongodb.session.SessionContext;

class CryptBinding
implements ClusterAwareReadWriteBinding {
    private final ClusterAwareReadWriteBinding wrapped;
    private final Crypt crypt;

    CryptBinding(ClusterAwareReadWriteBinding wrapped, Crypt crypt) {
        this.crypt = crypt;
        this.wrapped = wrapped;
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.wrapped.getReadPreference();
    }

    @Override
    public ConnectionSource getReadConnectionSource() {
        return new CryptConnectionSource(this.wrapped.getReadConnectionSource());
    }

    @Override
    public ConnectionSource getWriteConnectionSource() {
        return new CryptConnectionSource(this.wrapped.getWriteConnectionSource());
    }

    @Override
    public ConnectionSource getConnectionSource(ServerAddress serverAddress) {
        return new CryptConnectionSource(this.wrapped.getConnectionSource(serverAddress));
    }

    @Override
    public SessionContext getSessionContext() {
        return this.wrapped.getSessionContext();
    }

    @Override
    public int getCount() {
        return this.wrapped.getCount();
    }

    @Override
    public ReadWriteBinding retain() {
        this.wrapped.retain();
        return this;
    }

    @Override
    public void release() {
        this.wrapped.release();
    }

    @Override
    public Cluster getCluster() {
        return this.wrapped.getCluster();
    }

    private class CryptConnectionSource
    implements ConnectionSource {
        private final ConnectionSource wrapped;

        CryptConnectionSource(ConnectionSource wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public ServerDescription getServerDescription() {
            return this.wrapped.getServerDescription();
        }

        @Override
        public SessionContext getSessionContext() {
            return this.wrapped.getSessionContext();
        }

        @Override
        public Connection getConnection() {
            return new CryptConnection(this.wrapped.getConnection(), CryptBinding.this.crypt);
        }

        @Override
        public int getCount() {
            return this.wrapped.getCount();
        }

        @Override
        public ConnectionSource retain() {
            this.wrapped.retain();
            return this;
        }

        @Override
        public void release() {
            this.wrapped.release();
        }
    }
}

