/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

import com.mongodb.management.MBeanServer;

@Deprecated
public class NullMBeanServer
implements MBeanServer {
    @Override
    public void unregisterMBean(String mBeanName) {
    }

    @Override
    public void registerMBean(Object mBean, String mBeanName) {
    }
}

