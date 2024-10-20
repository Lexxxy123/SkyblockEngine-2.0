/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.enchanted;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.enchanted.EnchantedMaterialStatistics;

public class EnchantedAcaciaWood
implements EnchantedMaterialStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Enchanted Acacia Wood";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public SMaterial getCraftingMaterial() {
        return SMaterial.ACACIA_WOOD;
    }

    @Override
    public MaterialQuantifiable getResult() {
        return new MaterialQuantifiable(SMaterial.ENCHANTED_ACACIA_WOOD);
    }
}

