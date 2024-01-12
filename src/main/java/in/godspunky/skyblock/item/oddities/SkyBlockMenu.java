package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.item.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SkyBlockMenu implements MaterialStatistics, MaterialFunction, Untradeable {
    @Override
    public String getDisplayName() {
        return ChatColor.GREEN + "SkySim Menu " + ChatColor.GRAY + "(Right Click)";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore() {
        return "View all of your progress, including your Skills, Collections, Recipes, and more!";
    }

    @Override
    public boolean displayRarity() {
        return false;
    }

    @Override
    public void onInteraction(final PlayerInteractEvent e) {
        GUIType.SKYBLOCK_MENU.getGUI().open(e.getPlayer());
    }

    @Override
    public void onInventoryClick(final SItem instance, final InventoryClickEvent e) {
        e.setCancelled(true);
        GUIType.SKYBLOCK_MENU.getGUI().open((Player) e.getWhoClicked());
    }
}
