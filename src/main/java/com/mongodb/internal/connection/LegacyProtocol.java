/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.event.CommandListener;
import com.mongodb.internal.connection.InternalConnection;

public interface LegacyProtocol<T> {
    public T execute(InternalConnection var1);

    public void executeAsync(InternalConnection var1, SingleResultCallback<T> var2);

    public void setCommandListener(CommandListener var1);
}

