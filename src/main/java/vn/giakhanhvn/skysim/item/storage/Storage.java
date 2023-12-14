package vn.giakhanhvn.skysim.item.storage;

import java.util.HashMap;
import vn.giakhanhvn.skysim.item.GenericItemType;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.UUID;
import java.util.Map;
import vn.giakhanhvn.skysim.item.Untradeable;
import vn.giakhanhvn.skysim.item.ItemData;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;

public abstract class Storage implements MaterialStatistics, MaterialFunction, ItemData, Untradeable
{
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
