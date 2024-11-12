/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.event;

import com.mongodb.assertions.Assertions;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import java.util.ArrayList;
import java.util.List;

final class CommandListenerMulticaster
implements CommandListener {
    private static final Logger LOGGER = Loggers.getLogger("protocol.event");
    private final List<CommandListener> commandListeners;

    CommandListenerMulticaster(List<CommandListener> commandListeners) {
        Assertions.isTrue("All CommandListener instances are non-null", !commandListeners.contains(null));
        this.commandListeners = new ArrayList<CommandListener>(commandListeners);
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        for (CommandListener cur : this.commandListeners) {
            try {
                cur.commandStarted(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising command started event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent event) {
        for (CommandListener cur : this.commandListeners) {
            try {
                cur.commandSucceeded(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising command succeeded event to listener %s", cur), e2);
            }
        }
    }

    @Override
    public void commandFailed(CommandFailedEvent event) {
        for (CommandListener cur : this.commandListeners) {
            try {
                cur.commandFailed(event);
            } catch (Exception e2) {
                if (!LOGGER.isWarnEnabled()) continue;
                LOGGER.warn(String.format("Exception thrown raising command failed event to listener %s", cur), e2);
            }
        }
    }
}

