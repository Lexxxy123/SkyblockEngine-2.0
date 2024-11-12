/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import java.util.ArrayList;
import java.util.List;

final class ConnectionPoolListenerMulticaster
implements ConnectionPoolListener {
    private static final Logger LOGGER = Loggers.getLogger("protocol.event");
    private final List<ConnectionPoolListener> connectionPoolListeners;

    ConnectionPoolListenerMulticaster(List<ConnectionPoolListener> connectionPoolListeners) {
        Assertions.isTrue("All ConnectionPoolListener instances are non-null", !connectionPoolListeners.contains(null));
        this.connectionPoolListeners = new ArrayList<ConnectionPoolListener>(connectionPoolListeners);
    }

    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolOpened(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool opened event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionPoolClosed(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool closed event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckedOut(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool checked out event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionCheckedIn(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool checked in event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.waitQueueEntered(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool wait queue entered event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.waitQueueExited(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool wait queue exited event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionAdded(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool connection added event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
        for (ConnectionPoolListener cur : this.connectionPoolListeners) {
            try {
                cur.connectionRemoved(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising connection pool connection removed event to listener %s", cur), e2);
            }
        }
    }
}

