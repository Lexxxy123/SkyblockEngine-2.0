/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.item.armor;

import java.util.Map;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.TickingMaterial;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ObsidianChestplate
implements LeatherArmorStatistics,
TickingMaterial {
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
    public void tick(SItem item, final Player owner) {
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(owner.getUniqueId());
        statistics.getSpeed().zero(9);
        int obsidian = 0;
        for (Map.Entry entry : owner.getInventory().all(Material.OBSIDIAN).entrySet()) {
            obsidian += ((ItemStack)entry.getValue()).getAmount();
        }
        statistics.getSpeed().add(9, (double)obsidian / 20.0 / 100.0);
        new BukkitRunnable(){

            public void run() {
                SItem chestplate = SItem.find(owner.getInventory().getChestplate());
                if (chestplate != null && chestplate.getType() == SMaterial.OBSIDIAN_CHESTPLATE) {
                    return;
                }
                statistics.getSpeed().zero(9);
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 13L);
    }

    @Override
    public long getInterval() {
        return 10L;
    }
}

