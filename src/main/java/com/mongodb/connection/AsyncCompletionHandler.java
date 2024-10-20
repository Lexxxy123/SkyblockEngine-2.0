/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

public interface AsyncCompletionHandler<T> {
    public void completed(T var1);

    public void failed(Throwable var1);
}

