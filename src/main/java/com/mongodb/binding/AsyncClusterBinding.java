/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadWriteBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.Server;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.AbstractReferenceCounted;
import com.mongodb.internal.binding.AsyncClusterAwareReadWriteBinding;
import com.mongodb.internal.connection.ReadConcernAwareNoOpSessionContext;
import com.mongodb.selector.ReadPreferenceServerSelector;
import com.mongodb.selector.ServerAddressSelector;
import com.mongodb.selector.ServerSelector;
import com.mongodb.selector.WritableServerSelector;
import com.mongodb.session.SessionContext;

@Deprecated
public class AsyncClusterBinding
extends AbstractReferenceCounted
implements AsyncClusterAwareReadWriteBinding {
    private final Cluster cluster;
    private final ReadPreference readPreference;
    private final ReadConcern readConcern;

    @Deprecated
    public AsyncClusterBinding(Cluster cluster, ReadPreference readPreference) {
        this(cluster, readPreference, ReadConcern.DEFAULT);
    }

    public AsyncClusterBinding(Cluster cluster, ReadPreference readPreference, ReadConcern readConcern) {
        this.cluster = Assertions.notNull("cluster", cluster);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.readConcern = Assertions.notNull("readConcern", readConcern);
    }

    @Override
    public AsyncReadWriteBinding retain() {
        super.retain();
        return this;
    }

    @Override
    public Cluster getCluster() {
        return this.cluster;
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
    public void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> callback) {
        this.getAsyncClusterBindingConnectionSource(new ReadPreferenceServerSelector(this.readPreference), callback);
    }

    @Override
    public void getWriteConnectionSource(SingleResultCallback<AsyncConnectionSource> callback) {
        this.getAsyncClusterBindingConnectionSource(new WritableServerSelector(), callback);
    }

    @Override
    public void getConnectionSource(ServerAddress serverAddress, SingleResultCallback<AsyncConnectionSource> callback) {
        this.getAsyncClusterBindingConnectionSource(new ServerAddressSelector(serverAddress), callback);
    }

    private void getAsyncClusterBindingConnectionSource(ServerSelector serverSelector, final SingleResultCallback<AsyncConnectionSource> callback) {
        this.cluster.selectServerAsync(serverSelector, new SingleResultCallback<Server>(){

            @Override
            public void onResult(Server result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    callback.onResult(new AsyncClusterBindingConnectionSource(result), null);
                }
            }
        });
    }

    private final class AsyncClusterBindingConnectionSource
    extends AbstractReferenceCounted
    implements AsyncConnectionSource {
        private final Server server;

        private AsyncClusterBindingConnectionSource(Server server) {
            this.server = server;
            AsyncClusterBinding.this.retain();
        }

        @Override
        public ServerDescription getServerDescription() {
            return this.server.getDescription();
        }

        @Override
        public SessionContext getSessionContext() {
            return new ReadConcernAwareNoOpSessionContext(AsyncClusterBinding.this.readConcern);
        }

        @Override
        public void getConnection(SingleResultCallback<AsyncConnection> callback) {
            this.server.getConnectionAsync(callback);
        }

        @Override
        public AsyncConnectionSource retain() {
            super.retain();
            AsyncClusterBinding.this.retain();
            return this;
        }

        @Override
        public void release() {
            super.release();
            AsyncClusterBinding.this.release();
        }
    }
}

