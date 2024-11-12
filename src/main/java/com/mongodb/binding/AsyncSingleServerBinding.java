/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

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
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.selector.ServerAddressSelector;
import com.mongodb.selector.ServerSelector;
import com.mongodb.session.SessionContext;

@Deprecated
public class AsyncSingleServerBinding
extends AbstractReferenceCounted
implements AsyncReadWriteBinding {
    private final Cluster cluster;
    private final ServerAddress serverAddress;
    private final ReadPreference readPreference;

    public AsyncSingleServerBinding(Cluster cluster, ServerAddress serverAddress) {
        this(cluster, serverAddress, ReadPreference.primary());
    }

    public AsyncSingleServerBinding(Cluster cluster, ServerAddress serverAddress, ReadPreference readPreference) {
        this.cluster = Assertions.notNull("cluster", cluster);
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> callback) {
        this.getAsyncSingleServerBindingConnectionSource(new ServerAddressSelector(this.serverAddress), callback);
    }

    @Override
    public void getWriteConnectionSource(SingleResultCallback<AsyncConnectionSource> callback) {
        this.getAsyncSingleServerBindingConnectionSource(new ServerAddressSelector(this.serverAddress), callback);
    }

    private void getAsyncSingleServerBindingConnectionSource(ServerSelector serverSelector, final SingleResultCallback<AsyncConnectionSource> callback) {
        this.cluster.selectServerAsync(serverSelector, new SingleResultCallback<Server>(){

            @Override
            public void onResult(Server result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    callback.onResult(new AsyncSingleServerBindingConnectionSource(result), null);
                }
            }
        });
    }

    @Override
    public SessionContext getSessionContext() {
        return NoOpSessionContext.INSTANCE;
    }

    @Override
    public AsyncSingleServerBinding retain() {
        super.retain();
        return this;
    }

    private final class AsyncSingleServerBindingConnectionSource
    extends AbstractReferenceCounted
    implements AsyncConnectionSource {
        private final Server server;

        private AsyncSingleServerBindingConnectionSource(Server server) {
            AsyncSingleServerBinding.this.retain();
            this.server = server;
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
        public void getConnection(SingleResultCallback<AsyncConnection> callback) {
            this.server.getConnectionAsync(callback);
        }

        @Override
        public AsyncConnectionSource retain() {
            super.retain();
            return this;
        }

        @Override
        public void release() {
            super.release();
            if (super.getCount() == 0) {
                AsyncSingleServerBinding.this.release();
            }
        }
    }
}

