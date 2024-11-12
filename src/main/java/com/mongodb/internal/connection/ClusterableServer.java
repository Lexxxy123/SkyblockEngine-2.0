/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.Server;

interface ClusterableServer
extends Server {
    public void invalidate();

    public void invalidate(Throwable var1);

    public void close();

    public boolean isClosed();

    public void connect();
}

