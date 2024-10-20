/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.client.MongoDatabase;
import java.io.Closeable;

public interface SimpleMongoClient
extends Closeable {
    public MongoDatabase getDatabase(String var1);

    @Override
    public void close();
}

