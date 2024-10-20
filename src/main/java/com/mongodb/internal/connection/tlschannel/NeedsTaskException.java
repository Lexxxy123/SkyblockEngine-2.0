/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import com.mongodb.internal.connection.tlschannel.TlsChannelFlowControlException;

public class NeedsTaskException
extends TlsChannelFlowControlException {
    private static final long serialVersionUID = -7869448883241411803L;
    private Runnable task;

    public NeedsTaskException(Runnable task) {
        this.task = task;
    }

    public Runnable getTask() {
        return this.task;
    }
}

