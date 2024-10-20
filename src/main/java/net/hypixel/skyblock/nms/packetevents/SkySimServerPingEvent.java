/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.hypixel.skyblock.nms.packetevents;

import net.hypixel.skyblock.nms.pingrep.PingEvent;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkySimServerPingEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final PingEvent a;

    public SkySimServerPingEvent(PingEvent event) {
        this.a = event;
    }

    public PingReply getPingReply() {
        return this.a.getReply();
    }

    public boolean isCancelled() {
        return this.a.isCancelled();
    }

    public void setCancelled(boolean cancel) {
        this.a.setCancelled(cancel);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

