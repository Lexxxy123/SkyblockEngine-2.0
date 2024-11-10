/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.event;

import net.hypixel.skyblock.event.SkyblockEvent;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import org.bukkit.entity.Player;

public class SkyblockPlayerNPCClickEvent
extends SkyblockEvent {
    private Player player;
    private SkyblockNPC npc;

    public Player getPlayer() {
        return this.player;
    }

    public SkyblockNPC getNpc() {
        return this.npc;
    }

    public SkyblockPlayerNPCClickEvent(Player player, SkyblockNPC npc) {
        this.player = player;
        this.npc = npc;
    }
}

