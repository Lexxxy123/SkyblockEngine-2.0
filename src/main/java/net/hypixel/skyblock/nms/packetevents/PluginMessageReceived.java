/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.hypixel.skyblock.nms.packetevents;

import net.hypixel.skyblock.nms.packetevents.WrappedPluginMessage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginMessageReceived
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final WrappedPluginMessage a;

    public PluginMessageReceived(WrappedPluginMessage b) {
        this.a = b;
    }

    public WrappedPluginMessage getWrappedPluginMessage() {
        return this.a;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

