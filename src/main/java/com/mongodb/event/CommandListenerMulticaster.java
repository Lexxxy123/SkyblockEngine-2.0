/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.annotations.Immutable;
import com.mongodb.event.CommandEventMulticaster;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import java.util.List;

@Immutable
@Deprecated
public class CommandListenerMulticaster
implements CommandListener {
    private final CommandEventMulticaster wrapped;

    public CommandListenerMulticaster(List<CommandListener> commandListeners) {
        this.wrapped = new CommandEventMulticaster(commandListeners);
    }

    public List<CommandListener> getCommandListeners() {
        return this.wrapped.getCommandListeners();
    }

    @Override
    public void commandStarted(CommandStartedEvent event) {
        this.wrapped.commandStarted(event);
    }

    @Override
    public void commandSucceeded(CommandSucceededEvent event) {
        this.wrapped.commandSucceeded(event);
    }

    @Override
    public void commandFailed(CommandFailedEvent event) {
        this.wrapped.commandFailed(event);
    }
}

