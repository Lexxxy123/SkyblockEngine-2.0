/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

import com.mongodb.internal.management.jmx.JMXMBeanServer;
import com.mongodb.management.MBeanServer;
import com.mongodb.management.NullMBeanServer;

@Deprecated
public final class MBeanServerFactory {
    private static final MBeanServer M_BEAN_SERVER;

    private MBeanServerFactory() {
    }

    public static MBeanServer getMBeanServer() {
        return M_BEAN_SERVER;
    }

    static {
        MBeanServer tmp;
        try {
            tmp = new JMXMBeanServer();
        } catch (Throwable e2) {
            tmp = new NullMBeanServer();
        }
        M_BEAN_SERVER = tmp;
    }
}

