/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.session.SessionContext;

@Deprecated
public interface AsyncWriteBinding
extends ReferenceCounted {
    public void getWriteConnectionSource(SingleResultCallback<AsyncConnectionSource> var1);

    public SessionContext getSessionContext();

    @Override
    public AsyncWriteBinding retain();
}

