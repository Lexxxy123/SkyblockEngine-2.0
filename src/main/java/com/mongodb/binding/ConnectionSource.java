/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.binding.ReferenceCounted;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ServerDescription;
import com.mongodb.session.SessionContext;

@Deprecated
public interface ConnectionSource
extends ReferenceCounted {
    public ServerDescription getServerDescription();

    public SessionContext getSessionContext();

    public Connection getConnection();

    @Override
    public ConnectionSource retain();
}

