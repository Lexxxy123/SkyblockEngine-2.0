/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.armor.minichad;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.LeatherArmorStatistics;

public class MinichadChestplate
implements MaterialFunction,
LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 60.0;
    }

    @Override
    public double getBaseCritChance() {
        return 0.05;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.25;
    }

    @Override
    public double getBaseIntelligence() {
        return 2.0;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.05;
    }

    @Override
    public double getBaseSpeed() {
        return 0.02;
    }

    @Override
    public double getBaseHealth() {
        return 120.0;
    }

    @Override
    public double getBaseDefense() {
        return 80.0;
    }

    @Override
    public int getColor() {
        return 0x323232;
    }

    @Override
    public String getDisplayName() {
        return "Minichad Chestplate";
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
        return null;
    }

    @Override
    public void load() {
        ShapedRecipe recipe = new ShapedRecipe(SMaterial.HIDDEN_MINICHAD_CHESTPLATE);
        recipe.shape("123", "456", "789");
        recipe.set('1', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('2', SMaterial.HIDDEN_SHARD_DIAMOND, 1);
        recipe.set('3', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('4', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('5', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('6', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('7', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('8', SMaterial.HIDDEN_DIMOON_FRAG, 24);
        recipe.set('9', SMaterial.HIDDEN_DIMOON_FRAG, 24);
    }
}

