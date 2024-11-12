/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

public interface ConnectionPoolStatisticsMBean {
    public String getHost();

    public int getPort();

    public int getMinSize();

    public int getMaxSize();

    public int getSize();

    public int getCheckedOutCount();

    @Deprecated
    public int getWaitQueueSize();
}

