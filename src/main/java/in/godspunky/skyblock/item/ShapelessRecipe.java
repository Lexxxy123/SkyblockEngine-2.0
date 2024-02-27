package in.godspunky.skyblock.item;

import in.godspunky.skyblock.util.SUtil;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapelessRecipe extends Recipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES;
    private final List<MaterialQuantifiable> ingredientList;
    private boolean isVanilla;

    public ShapelessRecipe(SItem result, boolean usesExchangeables) {
        super(result, usesExchangeables);
        this.ingredientList = new ArrayList<MaterialQuantifiable>();
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

    public ShapelessRecipe add(MaterialQuantifiable material, final boolean isVanilla) {
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

    public ShapelessRecipe add(SMaterial material, int amount, final boolean isVanilla) {
        return this.add(new MaterialQuantifiable(material, amount), isVanilla);
    }

    @Override
    public List<MaterialQuantifiable> getIngredients() {
        return this.ingredientList;
    }

    @Override
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
            if (materials.length != ingredients.size()) {
                continue;
            }
            boolean found = true;
            MaterialQuantifiable[] copy = Arrays.copyOf(materials, materials.length);
            for (MaterialQuantifiable ingredient : ingredients) {
                if (!contains(recipe.useExchangeables, copy, ingredient)) {
                    found = false;
                    break;
                }
            }
            if (!found) {
                continue;
            }
            return recipe;
        }
        return null;
    }

    private static boolean contains(boolean usesExchangeables, MaterialQuantifiable[] grid, MaterialQuantifiable test) {
        List<SMaterial> exchangeables = Recipe.getExchangeablesOf(test.getMaterial());
        for (int i = 0; i < grid.length; ++i) {
            MaterialQuantifiable material = grid[i];
            if (null != material) {
                if (usesExchangeables && null != exchangeables && exchangeables.contains(material.getMaterial()) && material.getAmount() >= test.getAmount()) {
                    grid[i] = null;
                    return true;
                }
                if (material.getMaterial() == test.getMaterial() && material.getAmount() >= test.getAmount()) {
                    grid[i] = null;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isVanilla() {
        return isVanilla;
    }

    public List<MaterialQuantifiable> getIngredientList() {
        return this.ingredientList;
    }

    static {
        CACHED_RECIPES = new ArrayList<ShapelessRecipe>();
    }
}
