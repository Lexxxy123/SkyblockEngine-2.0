/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.nms.packetevents;

import org.bukkit.entity.Player;

public class WrappedPluginMessage {
    private final String channelName;
    private final Player player;
    private final byte[] message;

    public WrappedPluginMessage(String cn, Player p, byte[] msg) {
        this.channelName = cn;
        this.player = p;
        this.message = msg;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public Player getPlayer() {
        return this.player;
    }

    public byte[] getMessage() {
        return this.message;
    }
}

