/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.event;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

public interface CommandListener {
    public void commandStarted(CommandStartedEvent var1);

    public void commandSucceeded(CommandSucceededEvent var1);

    public void commandFailed(CommandFailedEvent var1);
}

