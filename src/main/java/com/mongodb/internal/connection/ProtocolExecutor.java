/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.internal.connection.CommandProtocol;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.LegacyProtocol;
import com.mongodb.session.SessionContext;

public interface ProtocolExecutor {
    public <T> T execute(LegacyProtocol<T> var1, InternalConnection var2);

    public <T> void executeAsync(LegacyProtocol<T> var1, InternalConnection var2, SingleResultCallback<T> var3);

    public <T> T execute(CommandProtocol<T> var1, InternalConnection var2, SessionContext var3);

    public <T> void executeAsync(CommandProtocol<T> var1, InternalConnection var2, SessionContext var3, SingleResultCallback<T> var4);
}

