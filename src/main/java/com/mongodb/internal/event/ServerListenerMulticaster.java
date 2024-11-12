/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;
import java.util.ArrayList;
import java.util.List;

final class ServerListenerMulticaster
implements ServerListener {
    private static final Logger LOGGER = Loggers.getLogger("cluster.event");
    private final List<ServerListener> serverListeners;

    ServerListenerMulticaster(List<ServerListener> serverListeners) {
        Assertions.isTrue("All ServerListener instances are non-null", !serverListeners.contains(null));
        this.serverListeners = new ArrayList<ServerListener>(serverListeners);
    }

    @Override
    public void serverOpening(ServerOpeningEvent event) {
        for (ServerListener cur : this.serverListeners) {
            try {
                cur.serverOpening(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void serverClosed(ServerClosedEvent event) {
        for (ServerListener cur : this.serverListeners) {
            try {
                cur.serverClosed(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising server opening event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void serverDescriptionChanged(ServerDescriptionChangedEvent event) {
        for (ServerListener cur : this.serverListeners) {
            try {
                cur.serverDescriptionChanged(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising server description changed event to listener %s", cur), e2);
            }
        }
    }
}

