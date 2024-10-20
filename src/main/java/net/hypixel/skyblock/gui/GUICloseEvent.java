/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class GUICloseEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final GUI closed;

    public GUICloseEvent(Player player, GUI opened) {
        super(player);
        this.closed = opened;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public GUI getClosed() {
        return this.closed;
    }
}

