/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.CommandEventSender;
import com.mongodb.internal.connection.ResponseBuffers;

class NoOpCommandEventSender
implements CommandEventSender {
    NoOpCommandEventSender() {
    }

    @Override
    public void sendStartedEvent() {
    }

    @Override
    public void sendFailedEvent(Throwable t2) {
    }

    @Override
    public void sendSucceededEvent(ResponseBuffers responseBuffers) {
    }

    @Override
    public void sendSucceededEventForOneWayCommand() {
    }
}

