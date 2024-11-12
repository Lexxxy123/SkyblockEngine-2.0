/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ServerDescription;
import com.mongodb.session.SessionContext;

@Deprecated
public interface AsyncConnectionSource
extends ReferenceCounted {
    public ServerDescription getServerDescription();

    public SessionContext getSessionContext();

    public void getConnection(SingleResultCallback<AsyncConnection> var1);

    @Override
    public AsyncConnectionSource retain();
}

