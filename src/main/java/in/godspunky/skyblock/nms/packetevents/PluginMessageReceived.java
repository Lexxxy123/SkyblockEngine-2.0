package in.godspunky.skyblock.nms.packetevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginMessageReceived extends Event {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final WrappedPluginMessage a;

    public PluginMessageReceived(final WrappedPluginMessage b) {
        this.a = b;
    }

    public static HandlerList getHandlerList() {
        return PluginMessageReceived.handlers;
    }

    public WrappedPluginMessage getWrappedPluginMessage() {
        return this.a;
    }

    public HandlerList getHandlers() {
        return PluginMessageReceived.handlers;
    }
}
