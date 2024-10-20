/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

@Deprecated
public interface MBeanServer {
    public void unregisterMBean(String var1);

    public void registerMBean(Object var1, String var2);
}

