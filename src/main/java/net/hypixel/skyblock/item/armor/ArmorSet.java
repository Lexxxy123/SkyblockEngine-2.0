/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import org.bukkit.entity.Player;

public interface ArmorSet {
    public String getName();

    public String getDescription();

    public Class<? extends MaterialStatistics> getHelmet();

    public Class<? extends MaterialStatistics> getChestplate();

    public Class<? extends MaterialStatistics> getLeggings();

    public Class<? extends MaterialStatistics> getBoots();

    default public PlayerBoostStatistics whileHasFullSet(Player player) {
        return null;
    }
}

