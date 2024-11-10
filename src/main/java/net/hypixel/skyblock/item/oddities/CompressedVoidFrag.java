/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.enchanted.EnchantedMaterialStatistics;

public class CompressedVoidFrag
implements EnchantedMaterialStatistics,
SkullStatistics,
MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Compressed Ender's Fragment";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public String getLore() {
        return "The entire chunk is... glowing, either?";
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
    public boolean isStackable() {
        return false;
    }

    @Override
    public String getURL() {
        return "6201ae1a8a04df52656f5e4813e1fbcf97877dbbfbc4268d04316d6f9f753";
    }

    @Override
    public SMaterial getCraftingMaterial() {
        return SMaterial.HIDDEN_VOID_FRAGMENT;
    }

    @Override
    public int getCraftingRequiredAmount() {
        return 64;
    }

    @Override
    public MaterialQuantifiable getResult() {
        return new MaterialQuantifiable(SMaterial.HIDDEN_COMPRESSED_VOID_FRAG);
    }
}

