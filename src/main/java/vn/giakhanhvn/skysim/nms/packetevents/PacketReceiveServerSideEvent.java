package vn.giakhanhvn.skysim.nms.packetevents;

import net.minecraft.server.v1_8_R3.Packet;
import vn.giakhanhvn.skysim.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PacketReceiveServerSideEvent extends Event implements Cancellable {
    private static final HandlerList handlers;
    private final ReceivedPacket a;

    public PacketReceiveServerSideEvent(final ReceivedPacket b) {
        this.a = b;
    }

    public Packet getPacket() {
        return (Packet) this.a.getPacket();
    }

    public ReceivedPacket getWrappedPacket() {
        return this.a;
    }

    public boolean isCancelled() {
        return this.a.isCancelled();
    }

    public void setCancelled(final boolean cancel) {
        this.a.setCancelled(cancel);
    }

    public HandlerList getHandlers() {
        return PacketReceiveServerSideEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketReceiveServerSideEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
