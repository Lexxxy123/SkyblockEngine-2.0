/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 */
package net.hypixel.skyblock.item.armor;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.md_5.bungee.api.ChatColor;

public class CrownOfGreed
implements ToolStatistics {
    @Override
    public String getDisplayName() {
        return "Crown of Greed";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public double getBaseDefense() {
        return 90.0;
    }

    @Override
    public double getBaseHealth() {
        return 130.0;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.04;
    }

    @Override
    public String getLore() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)"Hits have &c+25% &7base damage, but cost the weapon's base damage in &6coins &7from your purse.");
    }
}

