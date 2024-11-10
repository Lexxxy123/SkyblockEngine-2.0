/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.googlearmour;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class GoogleChestplate
implements MaterialFunction,
LeatherArmorStatistics {
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
        return 265.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.21;
    }

    @Override
    public double getBaseIntelligence() {
        return 210.0;
    }

    @Override
    public double getBaseTrueDefense() {
        return 5.0;
    }

    @Override
    public double getBaseHealthRegen() {
        return 10.0;
    }

    @Override
    public int getColor() {
        return 0xFF5555;
    }

    @Override
    public String getDisplayName() {
        return "\u00a79G\u00a7co\u00a7eo\u00a79g\u00a7al\u00a7ce \u00a79C\u00a7eh\u00a7ae\u00a7cs\u00a79t\u00a7cp\u00a7el\u00a79a\u00a7ct\u00a7ee";
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
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public String getLore() {
        return "\u00a78* Soulbound *";
    }
}

