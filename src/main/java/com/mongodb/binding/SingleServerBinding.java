/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadWriteBinding;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.Connection;
import com.mongodb.connection.Server;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.AbstractReferenceCounted;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.selector.ServerAddressSelector;
import com.mongodb.session.SessionContext;

@Deprecated
public class SingleServerBinding
extends AbstractReferenceCounted
implements ReadWriteBinding {
    private final Cluster cluster;
    private final ServerAddress serverAddress;
    private final ReadPreference readPreference;

    public SingleServerBinding(Cluster cluster, ServerAddress serverAddress) {
        this(cluster, serverAddress, ReadPreference.primary());
    }

    public SingleServerBinding(Cluster cluster, ServerAddress serverAddress, ReadPreference readPreference) {
        this.cluster = Assertions.notNull("cluster", cluster);
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
    }

    @Override
    public ConnectionSource getWriteConnectionSource() {
        return new SingleServerBindingConnectionSource();
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public ConnectionSource getReadConnectionSource() {
        return new SingleServerBindingConnectionSource();
    }

    @Override
    public SessionContext getSessionContext() {
        return NoOpSessionContext.INSTANCE;
    }

    @Override
    public SingleServerBinding retain() {
        super.retain();
        return this;
    }

    private final class SingleServerBindingConnectionSource
    extends AbstractReferenceCounted
    implements ConnectionSource {
        private final Server server;

        private SingleServerBindingConnectionSource() {
            SingleServerBinding.this.retain();
            this.server = SingleServerBinding.this.cluster.selectServer(new ServerAddressSelector(SingleServerBinding.this.serverAddress));
        }

        @Override
        public ServerDescription getServerDescription() {
            return this.server.getDescription();
        }

        @Override
        public SessionContext getSessionContext() {
            return NoOpSessionContext.INSTANCE;
        }

        @Override
        public Connection getConnection() {
            return SingleServerBinding.this.cluster.selectServer(new ServerAddressSelector(SingleServerBinding.this.serverAddress)).getConnection();
        }

        @Override
        public ConnectionSource retain() {
            super.retain();
            return this;
        }

        @Override
        public void release() {
            super.release();
            if (super.getCount() == 0) {
                SingleServerBinding.this.release();
            }
        }
    }
}

