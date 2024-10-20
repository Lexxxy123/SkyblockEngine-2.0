/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.binding;

import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.binding.WriteBinding;

@Deprecated
public interface ReadWriteBinding
extends ReadBinding,
WriteBinding,
ReferenceCounted {
    @Override
    public ReadWriteBinding retain();
}

