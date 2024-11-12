/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.lang.Nullable;
import java.io.Closeable;
import java.util.Iterator;

@NotThreadSafe
public interface MongoCursor<TResult>
extends Iterator<TResult>,
Closeable {
    @Override
    public void close();

    @Override
    public boolean hasNext();

    @Override
    public TResult next();

    @Nullable
    public TResult tryNext();

    @Nullable
    public ServerCursor getServerCursor();

    public ServerAddress getServerAddress();
}

