package vn.giakhanhvn.skysim.collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.*;
import vn.giakhanhvn.skysim.user.User;

public class ItemCollectionRecipeReward extends ItemCollectionReward {
    private final SMaterial material;

    public ItemCollectionRecipeReward(final SMaterial material) {
        super(Type.RECIPE);
        this.material = material;
    }


    @Override
    public void onAchieve(final Player player) {
        for (ShapedRecipe shapedRecipe : ShapedRecipe.CACHED_RECIPES) {
            if (recipeMatchesMaterial(shapedRecipe, material) && !shapedRecipe.isVanilla()) {
                unlockRecipe(player, shapedRecipe);
            }
        }
        for (ShapelessRecipe shapelessRecipe : ShapelessRecipe.CACHED_RECIPES) {
            if (recipeMatchesMaterial(shapelessRecipe, material) && !shapelessRecipe.isVanilla()) {
                unlockRecipe(player, shapelessRecipe);
            }
        }
    }

    private boolean recipeMatchesMaterial(Recipe recipe, SMaterial material) {
        SItem sItem = SItem.of(material);
        return recipe.getResult().getDisplayName().equalsIgnoreCase(sItem.getDisplayName());
    }

    private void unlockRecipe(Player player, Recipe recipe) {
        SItem sItem = SItem.of(material);
        User user = User.getUser(player.getUniqueId());
        if (!user.getUnlockedRecipes().contains(sItem.getDisplayName())) {
            user.getUnlockedRecipes().add(sItem.getDisplayName());
        }
    }

    @Override
    public String toRewardString() {
        return ChatColor.GRAY + this.material.getDisplayName(this.material.getData()) + ChatColor.GRAY + " Recipe";
    }
}
