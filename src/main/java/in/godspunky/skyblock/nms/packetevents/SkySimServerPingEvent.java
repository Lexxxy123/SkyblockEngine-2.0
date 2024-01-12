package in.godspunky.skyblock.nms.packetevents;

import in.godspunky.skyblock.nms.pingrep.PingEvent;
import in.godspunky.skyblock.nms.pingrep.PingReply;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SkySimServerPingEvent extends Event implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final PingEvent a;

    public SkySimServerPingEvent(final PingEvent event) {
        this.a = event;
    }

    public static HandlerList getHandlerList() {
        return SkySimServerPingEvent.handlers;
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
}
