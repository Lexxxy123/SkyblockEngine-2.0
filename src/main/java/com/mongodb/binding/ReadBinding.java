/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadPreference;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.session.SessionContext;

@Deprecated
public interface ReadBinding
extends ReferenceCounted {
    public ReadPreference getReadPreference();

    public ConnectionSource getReadConnectionSource();

    public SessionContext getSessionContext();

    @Override
    public ReadBinding retain();
}

