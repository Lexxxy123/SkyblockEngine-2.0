/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.util.concurrent.TimeUnit;

interface Pool<T> {
    public T get();

    public T get(long var1, TimeUnit var3);

    public void release(T var1);

    public void close();

    public void release(T var1, boolean var2);
}

