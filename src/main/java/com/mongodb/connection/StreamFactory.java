/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ServerAddress;
import com.mongodb.connection.Stream;

public interface StreamFactory {
    public Stream create(ServerAddress var1);
}

