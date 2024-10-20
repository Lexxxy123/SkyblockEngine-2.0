/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadConcern;
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
import com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import com.mongodb.internal.connection.ReadConcernAwareNoOpSessionContext;
import com.mongodb.selector.ReadPreferenceServerSelector;
import com.mongodb.selector.ServerAddressSelector;
import com.mongodb.selector.ServerSelector;
import com.mongodb.selector.WritableServerSelector;
import com.mongodb.session.SessionContext;

@Deprecated
public class ClusterBinding
extends AbstractReferenceCounted
implements ClusterAwareReadWriteBinding {
    private final Cluster cluster;
    private final ReadPreference readPreference;
    private final ReadConcern readConcern;

    @Deprecated
    public ClusterBinding(Cluster cluster, ReadPreference readPreference) {
        this(cluster, readPreference, ReadConcern.DEFAULT);
    }

    public ClusterBinding(Cluster cluster, ReadPreference readPreference, ReadConcern readConcern) {
        this.cluster = Assertions.notNull("cluster", cluster);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.readConcern = Assertions.notNull("readConcern", readConcern);
    }

    @Override
    public Cluster getCluster() {
        return this.cluster;
    }

    @Override
    public ReadWriteBinding retain() {
        super.retain();
        return this;
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public SessionContext getSessionContext() {
        return new ReadConcernAwareNoOpSessionContext(this.readConcern);
    }

    @Override
    public ConnectionSource getReadConnectionSource() {
        return new ClusterBindingConnectionSource(new ReadPreferenceServerSelector(this.readPreference));
    }

    @Override
    public ConnectionSource getWriteConnectionSource() {
        return new ClusterBindingConnectionSource(new WritableServerSelector());
    }

    @Override
    public ConnectionSource getConnectionSource(ServerAddress serverAddress) {
        return new ClusterBindingConnectionSource(new ServerAddressSelector(serverAddress));
    }

    private final class ClusterBindingConnectionSource
    extends AbstractReferenceCounted
    implements ConnectionSource {
        private final Server server;

        private ClusterBindingConnectionSource(ServerSelector serverSelector) {
            this.server = ClusterBinding.this.cluster.selectServer(serverSelector);
            ClusterBinding.this.retain();
        }

        @Override
        public ServerDescription getServerDescription() {
            return this.server.getDescription();
        }

        @Override
        public SessionContext getSessionContext() {
            return new ReadConcernAwareNoOpSessionContext(ClusterBinding.this.readConcern);
        }

        @Override
        public Connection getConnection() {
            return this.server.getConnection();
        }

        @Override
        public ConnectionSource retain() {
            super.retain();
            ClusterBinding.this.retain();
            return this;
        }

        @Override
        public void release() {
            super.release();
            ClusterBinding.this.release();
        }
    }
}

