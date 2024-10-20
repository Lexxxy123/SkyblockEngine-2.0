/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.item.enchanted;

import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapelessRecipe;

public interface EnchantedMaterialStatistics
extends MaterialStatistics {
    public SMaterial getCraftingMaterial();

    public MaterialQuantifiable getResult();

    default public SMaterial getBlockCraftingMaterial() {
        return null;
    }

    default public MaterialQuantifiable getBlockResult() {
        return null;
    }

    default public int getCraftingRequiredAmount() {
        return 32;
    }

    @Override
    default public void load() {
        if (null != this.getBlockCraftingMaterial() && null != this.getBlockResult()) {
            this.createRecipe(new MaterialQuantifiable(this.getBlockCraftingMaterial(), this.getCraftingRequiredAmount()), this.getBlockResult());
        }
        this.createRecipe(new MaterialQuantifiable(this.getCraftingMaterial(), this.getCraftingRequiredAmount()), this.getResult());
    }

    default public void createRecipe(MaterialQuantifiable material, MaterialQuantifiable result) {
        new ShapelessRecipe(result.getMaterial(), result.getAmount()).add(material).add(material).add(material).add(material).add(material);
    }
}

