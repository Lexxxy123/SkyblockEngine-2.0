/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

public class ShapelessRecipe
extends Recipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<ShapelessRecipe>();
    private final List<MaterialQuantifiable> ingredientList = new ArrayList<MaterialQuantifiable>();
    private boolean isVanilla;

    public ShapelessRecipe(SItem result, boolean usesExchangeables) {
        super(result, usesExchangeables);
        CACHED_RECIPES.add(this);
    }

    public ShapelessRecipe(SItem result) {
        this(result, false);
    }

    public ShapelessRecipe(SMaterial material, int amount) {
        this(SUtil.setSItemAmount(SItem.of(material), amount));
    }

    public ShapelessRecipe(SMaterial material) {
        this(SItem.of(material));
    }

    @Override
    public ShapelessRecipe setResult(SItem result) {
        this.result = result;
        return this;
    }

    public ShapelessRecipe add(MaterialQuantifiable material, boolean isVanilla) {
        this.ingredientList.add(material.clone());
        this.isVanilla = isVanilla;
        return this;
    }

    public ShapelessRecipe add(MaterialQuantifiable material) {
        this.ingredientList.add(material.clone());
        return this;
    }

    public ShapelessRecipe add(SMaterial material, int amount) {
        return this.add(new MaterialQuantifiable(material, amount), false);
    }

    public ShapelessRecipe add(SMaterial material, int amount, boolean isVanilla) {
        return this.add(new MaterialQuantifiable(material, amount), isVanilla);
    }

    @Override
    public List<MaterialQuantifiable> getIngredients() {
        return this.ingredientList;
    }

    public String toString() {
        return "ShapelessRecipe{" + this.ingredientList.toString() + "}";
    }

    protected static ShapelessRecipe parseShapelessRecipe(ItemStack[] stacks) {
        if (9 != stacks.length) {
            throw new UnsupportedOperationException("Recipe parsing requires a 9 element array!");
        }
        MaterialQuantifiable[] materials = SUtil.unnest(Recipe.airless(new MaterialQuantifiable[][]{MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 0, 3)), MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 3, 6)), MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 6, 9))}), MaterialQuantifiable.class);
        for (ShapelessRecipe recipe : CACHED_RECIPES) {
            List<MaterialQuantifiable> ingredients = recipe.getIngredientList();
            if (materials.length != ingredients.size()) continue;
            boolean found = true;
            MaterialQuantifiable[] copy = Arrays.copyOf(materials, materials.length);
            for (MaterialQuantifiable ingredient : ingredients) {
                if (ShapelessRecipe.contains(recipe.useExchangeables, copy, ingredient)) continue;
                found = false;
                break;
            }
            if (!found) continue;
            return recipe;
        }
        return null;
    }

    private static boolean contains(boolean usesExchangeables, MaterialQuantifiable[] grid, MaterialQuantifiable test) {
        List<SMaterial> exchangeables = Recipe.getExchangeablesOf(test.getMaterial());
        for (int i2 = 0; i2 < grid.length; ++i2) {
            MaterialQuantifiable material = grid[i2];
            if (null == material) continue;
            if (usesExchangeables && null != exchangeables && exchangeables.contains((Object)material.getMaterial()) && material.getAmount() >= test.getAmount()) {
                grid[i2] = null;
                return true;
            }
            if (material.getMaterial() != test.getMaterial() || material.getAmount() < test.getAmount()) continue;
            grid[i2] = null;
            return true;
        }
        return false;
    }

    public static void fromDocument(Document doc) {
        String resultType = doc.getString("result");
        int resultAmount = doc.getInteger("amount");
        List ingredientList = (List)doc.get("ingredientList");
        SItem resultItem = SItem.of(SMaterial.valueOf(resultType));
        resultItem.setAmount(resultAmount);
        ArrayList<MaterialQuantifiable> ingredients = new ArrayList<MaterialQuantifiable>();
        for (Document ingredientDoc : ingredientList) {
            String materialType = ingredientDoc.getString("material");
            int amount = ingredientDoc.getInteger("amount");
            SMaterial material = SMaterial.valueOf(materialType);
            MaterialQuantifiable mq = new MaterialQuantifiable(material, amount);
            ingredients.add(mq);
        }
        ShapelessRecipe recipe = new ShapelessRecipe(resultItem);
        for (MaterialQuantifiable ingredient : ingredients) {
            recipe.add(ingredient);
        }
    }

    public boolean isVanilla() {
        return this.isVanilla;
    }

    public List<MaterialQuantifiable> getIngredientList() {
        return this.ingredientList;
    }
}

