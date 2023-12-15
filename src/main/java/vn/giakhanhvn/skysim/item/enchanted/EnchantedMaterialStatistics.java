package vn.giakhanhvn.skysim.item.enchanted;

import vn.giakhanhvn.skysim.item.ShapelessRecipe;
import vn.giakhanhvn.skysim.item.MaterialQuantifiable;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.item.MaterialStatistics;

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
        if (this.getBlockCraftingMaterial() != null && this.getBlockResult() != null) {
            createRecipe(new MaterialQuantifiable(this.getBlockCraftingMaterial(), this.getCraftingRequiredAmount()), this.getBlockResult());
        }
        createRecipe(new MaterialQuantifiable(this.getCraftingMaterial(), this.getCraftingRequiredAmount()), this.getResult());
    }

    default void createRecipe(final MaterialQuantifiable material, final MaterialQuantifiable result) {
        new ShapelessRecipe(result.getMaterial(), result.getAmount()).add(material).add(material).add(material).add(material).add(material);
    }
}
