/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import org.bson.ByteBuf;

public interface BufferProvider {
    public ByteBuf getBuffer(int var1);
}

