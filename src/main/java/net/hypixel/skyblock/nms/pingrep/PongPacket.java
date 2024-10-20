/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.pingrep;

import net.hypixel.skyblock.nms.pingrep.PingEvent;

public abstract class PongPacket {
    private PingEvent event;

    public PongPacket(PingEvent event) {
        this.event = event;
    }

    public abstract void send();

    public PingEvent getEvent() {
        return this.event;
    }

    public void setEvent(PingEvent event) {
        this.event = event;
    }
}

