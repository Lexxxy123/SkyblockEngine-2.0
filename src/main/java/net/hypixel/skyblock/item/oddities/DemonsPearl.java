/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;
import org.bukkit.ChatColor;

public class DemonsPearl
implements MaterialStatistics,
MaterialFunction,
SkullStatistics {
    @Override
    public String getDisplayName() {
        return "Voidlings's Demon Pearl";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return false;
    }

    @Override
    public String getLore() {
        return "A Deep Dark Ender Pearl containing mystical powers from the Voidlings, dropped from the " + ChatColor.DARK_PURPLE + "Voidgloom Seraph " + ChatColor.GRAY + "Use this at the " + ChatColor.DARK_PURPLE + "Ender Altar" + ChatColor.GRAY + " in the " + ChatColor.DARK_PURPLE + "Void Sepulture" + ChatColor.GRAY + " to summon Voidlings Warden!";
    }

    @Override
    public String getURL() {
        return "9e62a317f2e8f349a27e296ce225b9e8b127d88be61aaebf16604bfa2ad81308";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}

