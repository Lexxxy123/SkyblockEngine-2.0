package vn.giakhanhvn.skysim.item.armor;

import java.util.Iterator;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.inventory.ItemStack;
import java.util.Map;
import org.bukkit.Material;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.PlayerStatistics;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.TickingMaterial;

public class ObsidianChestplate implements LeatherArmorStatistics, TickingMaterial
{
    @Override
    public int getColor() {
        return 0;
    }
    
    @Override
    public String getDisplayName() {
        return "Obsidian Chestplate";
    }
    
    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }
    
    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }
    
    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.CHESTPLATE;
    }
    
    @Override
    public double getBaseDefense() {
        return 250.0;
    }
    
    @Override
    public void tick(final SItem item, final Player owner) {
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(owner.getUniqueId());
        statistics.getSpeed().zero(9);
        int obsidian = 0;
        for (final Map.Entry<Integer, ? extends ItemStack> entry : owner.getInventory().all(Material.OBSIDIAN).entrySet()) {
            obsidian += ((ItemStack)entry.getValue()).getAmount();
        }
        statistics.getSpeed().add(9, Double.valueOf(obsidian / 20.0 / 100.0));
        new BukkitRunnable() {
            public void run() {
                final SItem chestplate = SItem.find(owner.getInventory().getChestplate());
                if (chestplate != null && chestplate.getType() == SMaterial.OBSIDIAN_CHESTPLATE) {
                    return;
                }
                statistics.getSpeed().zero(9);
            }
        }.runTaskLater((Plugin)SkySimEngine.getPlugin(), 13L);
    }
    
    @Override
    public long getInterval() {
        return 10L;
    }
}
