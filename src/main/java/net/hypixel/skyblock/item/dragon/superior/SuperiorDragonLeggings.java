/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.dragon.superior;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class SuperiorDragonLeggings
implements MaterialFunction,
LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 10.0;
    }

    @Override
    public double getBaseCritChance() {
        return 0.02;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.08;
    }

    @Override
    public double getBaseIntelligence() {
        return 25.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.03;
    }

    @Override
    public double getBaseHealth() {
        return 130.0;
    }

    @Override
    public double getBaseDefense() {
        return 170.0;
    }

    @Override
    public int getColor() {
        return 15916817;
    }

    @Override
    public String getDisplayName() {
        return "Superior Dragon Leggings";
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
        return null;
    }
}

