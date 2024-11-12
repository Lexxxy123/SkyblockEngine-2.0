/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

interface ServerMonitor {
    public void start();

    public void connect();

    public void close();
}

