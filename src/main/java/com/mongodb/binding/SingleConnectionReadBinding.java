/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.AbstractReferenceCounted;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.session.SessionContext;

@Deprecated
public class SingleConnectionReadBinding
extends AbstractReferenceCounted
implements ReadBinding {
    private final ReadPreference readPreference;
    private final ServerDescription serverDescription;
    private final Connection connection;

    public SingleConnectionReadBinding(ReadPreference readPreference, ServerDescription serverDescription, Connection connection) {
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.serverDescription = Assertions.notNull("serverDescription", serverDescription);
        this.connection = Assertions.notNull("connection", connection).retain();
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public ConnectionSource getReadConnectionSource() {
        return new SingleConnectionSource();
    }

    @Override
    public SessionContext getSessionContext() {
        return NoOpSessionContext.INSTANCE;
    }

    @Override
    public ReadBinding retain() {
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

    private class SingleConnectionSource
    extends AbstractReferenceCounted
    implements ConnectionSource {
        SingleConnectionSource() {
            SingleConnectionReadBinding.this.retain();
        }

        @Override
        public ServerDescription getServerDescription() {
            return SingleConnectionReadBinding.this.serverDescription;
        }

        @Override
        public SessionContext getSessionContext() {
            return NoOpSessionContext.INSTANCE;
        }

        @Override
        public Connection getConnection() {
            return SingleConnectionReadBinding.this.connection.retain();
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
                SingleConnectionReadBinding.this.release();
            }
        }
    }
}

