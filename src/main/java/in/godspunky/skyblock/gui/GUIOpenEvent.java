package in.godspunky.skyblock.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;

public class GUIOpenEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    private final GUI opened;
    private final Inventory inventory;
    private String title;
    private boolean cancelled;

    public GUIOpenEvent(final Player player, final GUI opened, final Inventory inventory) {
        super(player);
        this.opened = opened;
        this.title = opened.getTitle();
        this.inventory = inventory;
    }

    public static HandlerList getHandlerList() {
        return GUIOpenEvent.handlers;
    }

    public HandlerList getHandlers() {
        return GUIOpenEvent.handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    public GUI getOpened() {
        return this.opened;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
