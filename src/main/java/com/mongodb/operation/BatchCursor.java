/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.annotations.NotThreadSafe;
import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

@NotThreadSafe
@Deprecated
public interface BatchCursor<T>
extends Iterator<List<T>>,
Closeable {
    @Override
    public void close();

    @Override
    public boolean hasNext();

    @Override
    public List<T> next();

    public void setBatchSize(int var1);

    public int getBatchSize();

    public List<T> tryNext();

    public ServerCursor getServerCursor();

    public ServerAddress getServerAddress();
}

