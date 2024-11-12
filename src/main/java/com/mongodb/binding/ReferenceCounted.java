/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

@Deprecated
public interface ReferenceCounted {
    public int getCount();

    public ReferenceCounted retain();

    public void release();
}

