/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import org.bson.ByteBuf;

public interface BufferAllocator {
    public ByteBuf allocate(int var1);

    public void free(ByteBuf var1);
}

