/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ServerAddress;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.BufferProvider;
import java.io.IOException;
import java.util.List;
import org.bson.ByteBuf;

public interface Stream
extends BufferProvider {
    public void open() throws IOException;

    public void openAsync(AsyncCompletionHandler<Void> var1);

    public void write(List<ByteBuf> var1) throws IOException;

    public ByteBuf read(int var1) throws IOException;

    public void writeAsync(List<ByteBuf> var1, AsyncCompletionHandler<Void> var2);

    public void readAsync(int var1, AsyncCompletionHandler<ByteBuf> var2);

    public ServerAddress getAddress();

    public void close();

    public boolean isClosed();
}

