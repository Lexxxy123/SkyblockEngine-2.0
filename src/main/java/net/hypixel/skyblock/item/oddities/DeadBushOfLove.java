/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.md_5.bungee.api.ChatColor;

public class DeadBushOfLove
implements MaterialStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Dead Bush of Love";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public String getLore() {
        return "This item was given to the kind souls who helped so much testing SkyBlock on Beta! Much love " + ChatColor.RED + "\u2764";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}

