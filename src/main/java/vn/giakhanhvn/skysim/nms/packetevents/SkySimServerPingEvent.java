package vn.giakhanhvn.skysim.nms.packetevents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import vn.giakhanhvn.skysim.nms.pingrep.PingEvent;
import vn.giakhanhvn.skysim.nms.pingrep.PingReply;

public class SkySimServerPingEvent extends Event implements Cancellable {
    private static final HandlerList handlers;
    private final PingEvent a;

    public SkySimServerPingEvent(final PingEvent event) {
        this.a = event;
    }

    public PingReply getPingReply() {
        return this.a.getReply();
    }

    public boolean isCancelled() {
        return this.a.isCancelled();
    }

    public void setCancelled(final boolean cancel) {
        this.a.setCancelled(cancel);
    }

    public HandlerList getHandlers() {
        return SkySimServerPingEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return SkySimServerPingEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }
}
