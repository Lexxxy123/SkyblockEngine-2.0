/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.internal.connection.InternalConnection;
import java.io.Closeable;
import java.util.concurrent.TimeUnit;

interface ConnectionPool
extends Closeable {
    public void start();

    public InternalConnection get();

    public InternalConnection get(long var1, TimeUnit var3);

    public void getAsync(SingleResultCallback<InternalConnection> var1);

    public void invalidate();

    @Override
    public void close();
}

