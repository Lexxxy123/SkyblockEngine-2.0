/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import java.util.EventListener;

public interface ConnectionPoolListener
extends EventListener {
    public void connectionPoolOpened(ConnectionPoolOpenedEvent var1);

    public void connectionPoolClosed(ConnectionPoolClosedEvent var1);

    public void connectionCheckedOut(ConnectionCheckedOutEvent var1);

    public void connectionCheckedIn(ConnectionCheckedInEvent var1);

    @Deprecated
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent var1);

    @Deprecated
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent var1);

    public void connectionAdded(ConnectionAddedEvent var1);

    public void connectionRemoved(ConnectionRemovedEvent var1);
}

