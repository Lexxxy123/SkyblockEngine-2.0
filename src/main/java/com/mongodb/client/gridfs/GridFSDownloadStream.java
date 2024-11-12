/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.InputStream;

@NotThreadSafe
public abstract class GridFSDownloadStream
extends InputStream {
    public abstract GridFSFile getGridFSFile();

    public abstract GridFSDownloadStream batchSize(int var1);

    @Override
    public abstract int read();

    @Override
    public abstract int read(byte[] var1);

    @Override
    public abstract int read(byte[] var1, int var2, int var3);

    @Override
    public abstract long skip(long var1);

    @Override
    public abstract int available();

    public abstract void mark();

    @Override
    public abstract void reset();

    @Override
    public abstract void close();
}

