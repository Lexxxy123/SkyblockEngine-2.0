/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import net.sf.cglib.proxy.Callback;

public interface NoOp
extends Callback {
    public static final NoOp INSTANCE = new NoOp(){};
}

