/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import net.sf.cglib.proxy.Callback;

public interface LazyLoader
extends Callback {
    public Object loadObject() throws Exception;
}

