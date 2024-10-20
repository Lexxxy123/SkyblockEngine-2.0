/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.annotations.NotThreadSafe;
import java.io.OutputStream;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

@NotThreadSafe
public abstract class GridFSUploadStream
extends OutputStream {
    @Deprecated
    public abstract ObjectId getFileId();

    public abstract ObjectId getObjectId();

    public abstract BsonValue getId();

    public abstract void abort();

    @Override
    public abstract void write(int var1);

    @Override
    public abstract void write(byte[] var1);

    @Override
    public abstract void write(byte[] var1, int var2, int var3);

    @Override
    public void flush() {
    }

    @Override
    public abstract void close();
}

