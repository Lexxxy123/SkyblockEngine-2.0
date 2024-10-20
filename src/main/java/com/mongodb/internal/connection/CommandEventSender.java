/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.ResponseBuffers;

interface CommandEventSender {
    public void sendStartedEvent();

    public void sendFailedEvent(Throwable var1);

    public void sendSucceededEvent(ResponseBuffers var1);

    public void sendSucceededEventForOneWayCommand();
}

