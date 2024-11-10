/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.googlearmour;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class GoogleBoots
implements MaterialFunction,
LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 60.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.15;
    }

    @Override
    public double getBaseCritChance() {
        return 0.3;
    }

    @Override
    public double getBaseFerocity() {
        return 5.0;
    }

    @Override
    public double getBaseAttackSpeed() {
        return 4.0;
    }

    @Override
    public double getBaseHealth() {
        return 273.0;
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
        return 160.0;
    }

    @Override
    public int getColor() {
        return 2044954;
    }

    @Override
    public String getDisplayName() {
        return "\u00a79G\u00a7co\u00a7eo\u00a79g\u00a7al\u00a7ce \u00a79B\u00a7eo\u00a7ao\u00a7ct\u00a79s";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public String getLore() {
        return "\u00a78* Soulbound *";
    }
}

