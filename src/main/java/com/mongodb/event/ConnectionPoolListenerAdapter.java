/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;

public abstract class ConnectionPoolListenerAdapter
implements ConnectionPoolListener {
    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent event) {
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent event) {
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
    }
}

