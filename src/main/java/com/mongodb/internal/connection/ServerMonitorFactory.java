/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.connection.ChangeListener;
import com.mongodb.internal.connection.ServerMonitor;

interface ServerMonitorFactory {
    public ServerMonitor create(ChangeListener<ServerDescription> var1);
}

