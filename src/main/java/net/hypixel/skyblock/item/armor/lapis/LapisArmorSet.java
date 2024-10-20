/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor.lapis;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.armor.lapis.LapisArmorBoots;
import net.hypixel.skyblock.item.armor.lapis.LapisArmorChestplate;
import net.hypixel.skyblock.item.armor.lapis.LapisArmorHelmet;
import net.hypixel.skyblock.item.armor.lapis.LapisArmorLeggings;
import org.bukkit.entity.Player;

public class LapisArmorSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Health";
    }

    @Override
    public String getDescription() {
        return "Increases the wearer's maximum Health by 60.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return LapisArmorHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return LapisArmorChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return LapisArmorLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return LapisArmorBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(Player player) {
        return new PlayerBoostStatistics(){

            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public Rarity getRarity() {
                return null;
            }

            @Override
            public GenericItemType getType() {
                return null;
            }

            @Override
            public double getBaseHealth() {
                return 60.0;
            }
        };
    }
}

