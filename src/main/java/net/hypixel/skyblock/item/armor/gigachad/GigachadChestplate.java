package net.hypixel.skyblock.item.armor.gigachad;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class GigachadChestplate implements MaterialFunction, LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 100.0;
    }

    @Override
    public double getBaseCritChance() {
        return 0.1;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.08;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.3;
    }

    @Override
    public double getBaseIntelligence() {
        return 50.0;
    }

    @Override
    public double getBaseSpeed() {
        return 0.03;
    }

    @Override
    public double getBaseHealth() {
        return 300.0;
    }

    @Override
    public double getBaseDefense() {
        return 300.0;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Gigachad Chestplate";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
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
        return null;
    }
}
