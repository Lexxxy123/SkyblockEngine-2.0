/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.session.SessionContext;

@Deprecated
public interface AsyncReadBinding
extends ReferenceCounted {
    public ReadPreference getReadPreference();

    public SessionContext getSessionContext();

    public void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> var1);

    @Override
    public AsyncReadBinding retain();
}

