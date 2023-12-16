package vn.giakhanhvn.skysim.nms.packetevents;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import vn.giakhanhvn.skysim.nms.nmsutil.packetlistener.handler.SentPacket;

public class PacketSentServerSideEvent extends Event implements Cancellable {
    private static final HandlerList handlers;
    private final SentPacket a;

    public PacketSentServerSideEvent(final SentPacket b) {
        this.a = b;
    }

    public Packet getPacket() {
        return (Packet) this.a.getPacket();
    }

    public SentPacket getWrappedPacket() {
        return this.a;
    }

    public boolean isCancelled() {
        return this.a.isCancelled();
    }

    public void setCancelled(final boolean cancel) {
        this.a.setCancelled(cancel);
    }

    public HandlerList getHandlers() {
        return PacketSentServerSideEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketSentServerSideEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
