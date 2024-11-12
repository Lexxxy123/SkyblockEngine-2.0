/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.inventory.ItemStack;

public class ShapedRecipe
extends Recipe<ShapedRecipe> {
    public static final List<ShapedRecipe> CACHED_RECIPES = new ArrayList<ShapedRecipe>();
    protected String[] shape;
    private boolean isVanilla;
    private final Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<Character, MaterialQuantifiable>();

    public ShapedRecipe(SItem result, boolean usesExchangeables) {
        super(result, usesExchangeables);
        CACHED_RECIPES.add(this);
    }

    public ShapedRecipe(SItem result) {
        this(result, false);
    }

    public ShapedRecipe(SMaterial material, int amount) {
        this(SUtil.setSItemAmount(SItem.of(material), amount));
    }

    public ShapedRecipe(SMaterial material) {
        this(SItem.of(material));
    }

    public ShapedRecipe shape(String ... lines) {
        this.shape = lines;
        return this;
    }

    @Override
    public ShapedRecipe setResult(SItem result) {
        this.result = result;
        return this;
    }

    @Override
    public List<MaterialQuantifiable> getIngredients() {
        return new ArrayList<MaterialQuantifiable>(this.ingredientMap.values());
    }

    public ShapedRecipe set(char k2, MaterialQuantifiable material, boolean isVanilla) {
        this.ingredientMap.put(Character.valueOf(k2), material.clone());
        this.isVanilla = isVanilla;
        return this;
    }

    public ShapedRecipe set(char k2, SMaterial material, int amount) {
        return this.set(k2, new MaterialQuantifiable(material, amount), false);
    }

    public ShapedRecipe set(char k2, SMaterial material, int amount, boolean isVanilla) {
        return this.set(k2, new MaterialQuantifiable(material, amount), isVanilla);
    }

    public boolean isVanilla() {
        return this.isVanilla;
    }

    public ShapedRecipe set(char k2, SMaterial material) {
        return this.set(k2, new MaterialQuantifiable(material), false);
    }

    public MaterialQuantifiable[][] toMQ2DArray() {
        MaterialQuantifiable[][] materials = new MaterialQuantifiable[3][3];
        String l1 = SUtil.pad(SUtil.getOrDefault(this.shape, 0, "   "), 3);
        String l2 = SUtil.pad(SUtil.getOrDefault(this.shape, 1, "   "), 3);
        String l3 = SUtil.pad(SUtil.getOrDefault(this.shape, 2, "   "), 3);
        String[] ls = new String[]{l1, l2, l3};
        for (int i2 = 0; i2 < ls.length; ++i2) {
            String[] lps = ls[i2].split("");
            for (int j2 = 0; j2 < lps.length; ++j2) {
                materials[i2][j2] = this.ingredientMap.getOrDefault(Character.valueOf(lps[j2].charAt(0)), new MaterialQuantifiable(SMaterial.AIR, 1));
            }
        }
        return materials;
    }

    protected static ShapedRecipe parseShapedRecipe(ItemStack[] stacks) {
        if (9 != stacks.length) {
            throw new UnsupportedOperationException("Recipe parsing requires a 9 element array!");
        }
        MaterialQuantifiable[] l1 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 0, 3));
        MaterialQuantifiable[] l2 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 3, 6));
        MaterialQuantifiable[] l3 = MaterialQuantifiable.of(Arrays.copyOfRange(stacks, 6, 9));
        MaterialQuantifiable[][] grid = Recipe.airless(new MaterialQuantifiable[][]{l1, l2, l3});
        MaterialQuantifiable[] seg = ShapedRecipe.segment(MaterialQuantifiable.of(stacks));
        for (ShapedRecipe recipe : CACHED_RECIPES) {
            MaterialQuantifiable[][] airRecipeGrid = recipe.toMQ2DArray();
            MaterialQuantifiable[][] recipeGrid = Recipe.airless(airRecipeGrid);
            MaterialQuantifiable[] recipeSeg = ShapedRecipe.segment(SUtil.unnest(airRecipeGrid, MaterialQuantifiable.class));
            if (!ShapedRecipe.recipeAccepted(recipe.useExchangeables, grid, recipeGrid) || !ShapedRecipe.recipeAccepted(recipe.useExchangeables, seg, recipeSeg)) continue;
            return recipe;
        }
        return null;
    }

    private static <T> boolean deepSameLength(T[][] a1, T[][] a2) {
        int c1 = 0;
        int c2 = 0;
        for (T[] a3 : a1) {
            c1 += a3.length;
        }
        for (T[] a3 : a2) {
            c2 += a3.length;
        }
        return c1 == c2;
    }

    private static MaterialQuantifiable[] segment(MaterialQuantifiable[] materials) {
        int firstNonAir = -1;
        int lastNonAir = -1;
        for (int i2 = 0; i2 < materials.length; ++i2) {
            MaterialQuantifiable material = materials[i2];
            if (-1 == firstNonAir && SMaterial.AIR != material.getMaterial()) {
                firstNonAir = i2;
            }
            if (SMaterial.AIR == material.getMaterial()) continue;
            lastNonAir = i2;
        }
        if (-1 == firstNonAir || -1 == lastNonAir) {
            return new MaterialQuantifiable[0];
        }
        return Arrays.copyOfRange(materials, firstNonAir, lastNonAir + 1);
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[][] grid, MaterialQuantifiable[][] recipeGrid) {
        if (!ShapedRecipe.deepSameLength(grid, recipeGrid)) {
            return false;
        }
        boolean found = true;
        try {
            for (int i2 = 0; i2 < grid.length; ++i2) {
                for (int j2 = 0; j2 < grid[i2].length; ++j2) {
                    MaterialQuantifiable m1 = grid[i2][j2];
                    MaterialQuantifiable m2 = recipeGrid[i2][j2];
                    List<SMaterial> exchangeables = Recipe.getExchangeablesOf(m2.getMaterial());
                    if (usesExchangeables && null != exchangeables && exchangeables.contains((Object)m1.getMaterial()) && m1.getAmount() >= m2.getAmount() || m1.getMaterial() == m2.getMaterial() && m1.getAmount() >= m2.getAmount()) continue;
                    found = false;
                    break;
                }
                if (found) {
                    continue;
                }
                break;
            }
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        return found;
    }

    private static boolean recipeAccepted(boolean usesExchangeables, MaterialQuantifiable[] grid1d, MaterialQuantifiable[] recipeGrid1d) {
        if (grid1d.length != recipeGrid1d.length) {
            return false;
        }
        boolean found = true;
        for (int i2 = 0; i2 < grid1d.length; ++i2) {
            MaterialQuantifiable m1 = grid1d[i2];
            MaterialQuantifiable m2 = recipeGrid1d[i2];
            List<SMaterial> exchangeables = Recipe.getExchangeablesOf(m2.getMaterial());
            if (usesExchangeables && null != exchangeables && exchangeables.contains((Object)m1.getMaterial()) && m1.getAmount() >= m2.getAmount() || m1.getMaterial() == m2.getMaterial() && m1.getAmount() >= m2.getAmount()) continue;
            found = false;
            break;
        }
        return found;
    }

    public String[] getShape() {
        return this.shape;
    }

    public Map<Character, MaterialQuantifiable> getIngredientMap() {
        return this.ingredientMap;
    }
}

