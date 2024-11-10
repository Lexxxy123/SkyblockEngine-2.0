/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.googlearmour;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;

public class GoogleHelmet
implements MaterialFunction,
SkullStatistics,
ToolStatistics {
    @Override
    public double getBaseStrength() {
        return 110.0;
    }

    @Override
    public double getBaseCritChance() {
        return 0.25;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.5;
    }

    @Override
    public double getBaseFerocity() {
        return 5.0;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 0.04;
    }

    @Override
    public double getBaseHealth() {
        return 343.0;
    }

    @Override
    public double getBaseDefense() {
        return 266.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.21;
    }

    @Override
    public double getBaseIntelligence() {
        return 235.0;
    }

    @Override
    public double getBaseHealthRegen() {
        return 10.0;
    }

    @Override
    public String getURL() {
        return "624f543b17a71dff1fbfc5aabcb02e4e65aa2c1f505c7bd1d7adf667db689b8f";
    }

    @Override
    public String getDisplayName() {
        return "\u00a79G\u00a7co\u00a7eo\u00a79g\u00a7al\u00a7ce \u00a79H\u00a7ee\u00a7al\u00a7cm\u00a79e\u00a7ct";
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
    public String getLore() {
        return "\u00a78* Soulbound *";
    }
}

