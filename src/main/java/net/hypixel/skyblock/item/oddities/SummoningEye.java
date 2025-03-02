/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;
import org.bukkit.ChatColor;

public class SummoningEye
implements SkullStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Summoning Eye";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public String getLore() {
        return "Use this at the " + ChatColor.DARK_PURPLE + "Ender Altar" + ChatColor.GRAY + " in the " + ChatColor.DARK_PURPLE + "Dragon's Nest" + ChatColor.GRAY + " to summon Ender Dragons!";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public String getURL() {
        return "12c318157f5c9daf8a7677a3669f9798940aff014a674ee0af2a574cbb21b8c3";
    }
}

