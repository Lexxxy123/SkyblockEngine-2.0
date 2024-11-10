/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.ShapedRecipe
 *  org.bukkit.inventory.ShapelessRecipe
 */
package net.hypixel.skyblock.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.item.ShapelessRecipe;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Groups;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Recipe<T> {
    protected static final List<List<SMaterial>> EXCHANGEABLES = new ArrayList<List>(Arrays.asList(Arrays.asList(SMaterial.OAK_WOOD, SMaterial.SPRUCE_WOOD, SMaterial.BIRCH_WOOD, SMaterial.JUNGLE_WOOD, SMaterial.ACACIA_WOOD, SMaterial.DARK_OAK_WOOD), Arrays.asList(SMaterial.OAK_WOOD_PLANKS, SMaterial.SPRUCE_WOOD_PLANKS, SMaterial.BIRCH_WOOD_PLANKS, SMaterial.JUNGLE_WOOD_PLANKS, SMaterial.ACACIA_WOOD_PLANKS, SMaterial.DARK_OAK_WOOD_PLANKS)));
    protected SItem result;
    protected boolean useExchangeables;

    protected Recipe(SItem result, boolean useExchangeables) {
        this.result = result;
        this.useExchangeables = useExchangeables;
    }

    protected Recipe(SItem result) {
        this(result, false);
    }

    public abstract T setResult(SItem var1);

    public abstract List<MaterialQuantifiable> getIngredients();

    public static Recipe<?> parseRecipe(ItemStack[] stacks) {
        ShapedRecipe shaped = ShapedRecipe.parseShapedRecipe(stacks);
        if (shaped != null) {
            return shaped;
        }
        return ShapelessRecipe.parseShapelessRecipe(stacks);
    }

    public boolean isUnlockedForPlayer(User user) {
        try {
            return user.getUnlockedRecipes().contains(this.result.getDisplayName());
        } catch (NullPointerException ex) {
            System.out.println("Result is null");
            return false;
        }
    }

    protected static MaterialQuantifiable[][] airless(MaterialQuantifiable[][] grid) {
        ArrayList<Integer> excluded = new ArrayList<Integer>(0);
        for (int i = 0; i < grid.length; ++i) {
            boolean ex = true;
            for (MaterialQuantifiable material : grid[i]) {
                if (material.getMaterial() == SMaterial.AIR) continue;
                ex = false;
                break;
            }
            if (!ex) continue;
            excluded.add(i);
        }
        int amountExcluded = excluded.size();
        MaterialQuantifiable[][] g = new MaterialQuantifiable[grid.length - amountExcluded][];
        int b = 0;
        for (int j = 0; j < grid.length; ++j) {
            if (excluded.contains(j)) {
                ++b;
                continue;
            }
            MaterialQuantifiable[] line = grid[j];
            int remaining = (int)Arrays.stream(line).filter(mat -> mat.getMaterial() != SMaterial.AIR).count();
            g[j - b] = new MaterialQuantifiable[remaining];
            int r = 0;
            for (int k = 0; k < line.length; ++k) {
                if (line[k].getMaterial() == SMaterial.AIR) continue;
                g[j - b][r] = line[k];
                ++r;
            }
        }
        return g;
    }

    public static List<SMaterial> getExchangeablesOf(SMaterial material) {
        for (List<SMaterial> materials : EXCHANGEABLES) {
            int f = Collections.binarySearch(materials, material);
            if (f < 0) continue;
            return materials;
        }
        return null;
    }

    public static void loadRecipes() {
        Iterator iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            org.bukkit.inventory.Recipe recipe = (org.bukkit.inventory.Recipe)iter.next();
            if (recipe.getResult() == null) continue;
            Material result = recipe.getResult().getType();
            if (recipe instanceof org.bukkit.inventory.ShapedRecipe) {
                org.bukkit.inventory.ShapedRecipe shaped = (org.bukkit.inventory.ShapedRecipe)recipe;
                ShapedRecipe specShaped = new ShapedRecipe(SItem.convert(shaped.getResult()), Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result)).shape(shaped.getShape());
                for (Map.Entry entry : shaped.getIngredientMap().entrySet()) {
                    if (entry.getValue() == null) continue;
                    ItemStack stack = (ItemStack)entry.getValue();
                    specShaped.set(((Character)entry.getKey()).charValue(), SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()), stack.getAmount(), true);
                }
            }
            if (!(recipe instanceof org.bukkit.inventory.ShapelessRecipe)) continue;
            org.bukkit.inventory.ShapelessRecipe shapeless = (org.bukkit.inventory.ShapelessRecipe)recipe;
            ShapelessRecipe specShapeless = new ShapelessRecipe(SItem.convert(shapeless.getResult()), Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result));
            for (ItemStack itemStack : shapeless.getIngredientList()) {
                specShapeless.add(SMaterial.getSpecEquivalent(itemStack.getType(), itemStack.getDurability()), itemStack.getAmount(), true);
            }
        }
    }

    public SItem getResult() {
        return this.result;
    }

    public boolean isUseExchangeables() {
        return this.useExchangeables;
    }

    public void setUseExchangeables(boolean useExchangeables) {
        this.useExchangeables = useExchangeables;
    }
}

