/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.DnsSrvRecordInitializer;
import com.mongodb.internal.connection.DnsSrvRecordMonitor;

public interface DnsSrvRecordMonitorFactory {
    public DnsSrvRecordMonitor create(String var1, DnsSrvRecordInitializer var2);
}

