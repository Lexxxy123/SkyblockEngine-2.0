/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.MethodProxy;

public interface MethodInterceptor
extends Callback {
    public Object intercept(Object var1, Method var2, Object[] var3, MethodProxy var4) throws Throwable;
}

