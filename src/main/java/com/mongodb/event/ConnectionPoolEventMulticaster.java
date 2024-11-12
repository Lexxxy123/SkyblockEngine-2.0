/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.annotations.Beta;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
@Beta
public final class ConnectionPoolEventMulticaster
implements ConnectionPoolListener {
    private final Set<ConnectionPoolListener> connectionPoolListeners = Collections.newSetFromMap(new ConcurrentHashMap());

    public void add(ConnectionPoolListener connectionPoolListener) {
        this.connectionPoolListeners.add(connectionPoolListener);
    }

    public void remove(ConnectionPoolListener connectionPoolListener) {
        this.connectionPoolListeners.remove(connectionPoolListener);
    }

    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionPoolOpened(event);
        }
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionPoolClosed(event);
        }
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionCheckedOut(event);
        }
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionCheckedIn(event);
        }
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.waitQueueEntered(event);
        }
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.waitQueueExited(event);
        }
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionAdded(event);
        }
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            cur.connectionRemoved(event);
        }
    }
}

