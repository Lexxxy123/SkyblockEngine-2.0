package in.godspunky.skyblock.item.storage;

import in.godspunky.skyblock.item.*;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import in.godspunky.skyblock.item.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Storage implements MaterialStatistics, MaterialFunction, ItemData, Untradeable {
    private static final Map<UUID, Inventory> OPENED_STORAGE_UNITS;

    public static Inventory getCurrentStorageOpened(final Player player) {
        return Storage.OPENED_STORAGE_UNITS.get(player.getUniqueId());
    }

    public static void closeCurrentStorage(final Player player) {
        Storage.OPENED_STORAGE_UNITS.remove(player.getUniqueId());
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public void onInteraction(final PlayerInteractEvent e) {
        e.getPlayer().sendMessage(ChatColor.RED + "This item is currently disabled due to an exploit! Find out more " + ChatColor.WHITE + "https://bit.ly/skysim");
    }

    @Override
    public NBTTagCompound getData() {
        return new NBTTagCompound();
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    public abstract int getSlots();

    static {
        OPENED_STORAGE_UNITS = new HashMap<UUID, Inventory>();
    }
}
