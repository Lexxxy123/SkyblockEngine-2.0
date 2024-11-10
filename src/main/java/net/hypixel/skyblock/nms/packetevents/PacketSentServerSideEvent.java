/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Packet
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.hypixel.skyblock.nms.packetevents;

import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketSentServerSideEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final SentPacket a;

    public PacketSentServerSideEvent(SentPacket b) {
        this.a = b;
    }

    public Packet getPacket() {
        return (Packet)this.a.getPacket();
    }

    public SentPacket getWrappedPacket() {
        return this.a;
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

