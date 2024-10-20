/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener.handler;

import net.hypixel.skyblock.nms.nmsutil.packetlistener.channel.ChannelWrapper;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.PacketAbstract;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class SentPacket
extends PacketAbstract {
    public SentPacket(Object packet, Cancellable cancellable, Player player) {
        super(packet, cancellable, player);
    }

    public SentPacket(Object packet, Cancellable cancellable, ChannelWrapper channelWrapper) {
        super(packet, cancellable, channelWrapper);
    }
}

