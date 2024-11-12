/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.annotations.Beta;
import com.mongodb.event.ConnectionClosedEvent;
import com.mongodb.event.ConnectionMessageReceivedEvent;
import com.mongodb.event.ConnectionMessagesSentEvent;
import com.mongodb.event.ConnectionOpenedEvent;
import java.util.EventListener;

@Deprecated
@Beta
public interface ConnectionListener
extends EventListener {
    public void connectionOpened(ConnectionOpenedEvent var1);

    public void connectionClosed(ConnectionClosedEvent var1);

    public void messagesSent(ConnectionMessagesSentEvent var1);

    public void messageReceived(ConnectionMessageReceivedEvent var1);
}

