package net.hypixel.skyblock.item.enchanted;

import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapelessRecipe;

public interface EnchantedMaterialStatistics extends MaterialStatistics {
    SMaterial getCraftingMaterial();

    MaterialQuantifiable getResult();

    default SMaterial getBlockCraftingMaterial() {
        return null;
    }

    default MaterialQuantifiable getBlockResult() {
        return null;
    }

    default int getCraftingRequiredAmount() {
        return 32;
    }

    default void load() {
        if (null != this.getBlockCraftingMaterial() && null != this.getBlockResult()) {
            createRecipe(new MaterialQuantifiable(this.getBlockCraftingMaterial(), this.getCraftingRequiredAmount()), this.getBlockResult());
        }
        createRecipe(new MaterialQuantifiable(this.getCraftingMaterial(), this.getCraftingRequiredAmount()), this.getResult());
    }

    default void createRecipe(MaterialQuantifiable material, MaterialQuantifiable result) {
        new ShapelessRecipe(result.getMaterial(), result.getAmount()).add(material).add(material).add(material).add(material).add(material);
    }
}
