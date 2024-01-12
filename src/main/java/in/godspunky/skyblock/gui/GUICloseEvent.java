package in.godspunky.skyblock.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class GUICloseEvent extends PlayerEvent {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final GUI closed;

    public GUICloseEvent(final Player player, final GUI opened) {
        super(player);
        this.closed = opened;
    }

    public static HandlerList getHandlerList() {
        return GUICloseEvent.handlers;
    }

    public HandlerList getHandlers() {
        return GUICloseEvent.handlers;
    }

    public GUI getClosed() {
        return this.closed;
    }
}
