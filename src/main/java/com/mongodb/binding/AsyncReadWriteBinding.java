/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.ReferenceCounted;

@Deprecated
public interface AsyncReadWriteBinding
extends AsyncReadBinding,
AsyncWriteBinding,
ReferenceCounted {
    @Override
    public AsyncReadWriteBinding retain();
}

