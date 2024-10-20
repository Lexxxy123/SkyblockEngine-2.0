/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public interface ExtendedAsynchronousByteChannel
extends AsynchronousByteChannel {
    public <A> void read(ByteBuffer var1, long var2, TimeUnit var4, A var5, CompletionHandler<Integer, ? super A> var6);

    public <A> void read(ByteBuffer[] var1, int var2, int var3, long var4, TimeUnit var6, A var7, CompletionHandler<Long, ? super A> var8);

    public <A> void write(ByteBuffer var1, long var2, TimeUnit var4, A var5, CompletionHandler<Integer, ? super A> var6);

    public <A> void write(ByteBuffer[] var1, int var2, int var3, long var4, TimeUnit var6, A var7, CompletionHandler<Long, ? super A> var8);
}

