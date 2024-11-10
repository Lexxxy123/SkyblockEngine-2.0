/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.googlearmour;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class GoogleLeggings
implements MaterialFunction,
LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 70.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.17;
    }

    @Override
    public double getBaseCritChance() {
        return 0.6;
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
        return 323.0;
    }

    @Override
    public double getBaseDefense() {
        return 186.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.21;
    }

    @Override
    public double getBaseIntelligence() {
        return 205.0;
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
        return 1589268;
    }

    @Override
    public String getDisplayName() {
        return "\u00a79G\u00a7co\u00a7eo\u00a79g\u00a7al\u00a7ce \u00a79L\u00a7ee\u00a7ag\u00a7cg\u00a79i\u00a7cn\u00a7eg&9s";
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
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public String getLore() {
        return "\u00a78* Soulbound *";
    }
}

