/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.dragon.young;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.dragon.young.YoungDragonBoots;
import net.hypixel.skyblock.item.dragon.young.YoungDragonChestplate;
import net.hypixel.skyblock.item.dragon.young.YoungDragonHelmet;
import net.hypixel.skyblock.item.dragon.young.YoungDragonLeggings;
import org.bukkit.entity.Player;

public class YoungDragonSet
implements ArmorSet {
    @Override
    public String getName() {
        return "Young Blood";
    }

    @Override
    public String getDescription() {
        return "Gain +70 Speed when you are above 50% Health.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return YoungDragonHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return YoungDragonChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return YoungDragonLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return YoungDragonBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(final Player player) {
        return new PlayerBoostStatistics(){

            @Override
            public double getBaseSpeed() {
                if (player.getHealth() > player.getMaxHealth() / 2.0) {
                    return 0.7;
                }
                return 0.0;
            }

            @Override
            public String getDisplayName() {
                return null;
            }

            @Override
            public Rarity getRarity() {
                return null;
            }

            @Override
            public String getLore() {
                return null;
            }

            @Override
            public GenericItemType getType() {
                return null;
            }
        };
    }
}

