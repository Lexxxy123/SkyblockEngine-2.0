/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.AbstractReferenceCounted;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.session.SessionContext;

@Deprecated
public class AsyncSingleConnectionReadBinding
extends AbstractReferenceCounted
implements AsyncReadBinding {
    private final ReadPreference readPreference;
    private final ServerDescription serverDescription;
    private final AsyncConnection connection;

    public AsyncSingleConnectionReadBinding(ReadPreference readPreference, ServerDescription serverDescription, AsyncConnection connection) {
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.serverDescription = Assertions.notNull("serverDescription", serverDescription);
        this.connection = Assertions.notNull("connection", connection).retain();
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public SessionContext getSessionContext() {
        return NoOpSessionContext.INSTANCE;
    }

    @Override
    public void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> callback) {
        callback.onResult(new AsyncSingleConnectionSource(), null);
    }

    @Override
    public AsyncReadBinding retain() {
        super.retain();
        return this;
    }

    @Override
    public void release() {
        super.release();
        if (this.getCount() == 0) {
            this.connection.release();
        }
    }

    private class AsyncSingleConnectionSource
    extends AbstractReferenceCounted
    implements AsyncConnectionSource {
        AsyncSingleConnectionSource() {
            AsyncSingleConnectionReadBinding.this.retain();
        }

        @Override
        public ServerDescription getServerDescription() {
            return AsyncSingleConnectionReadBinding.this.serverDescription;
        }

        @Override
        public SessionContext getSessionContext() {
            return NoOpSessionContext.INSTANCE;
        }

        @Override
        public void getConnection(SingleResultCallback<AsyncConnection> callback) {
            callback.onResult(AsyncSingleConnectionReadBinding.this.connection.retain(), null);
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
                AsyncSingleConnectionReadBinding.this.release();
            }
        }
    }
}

