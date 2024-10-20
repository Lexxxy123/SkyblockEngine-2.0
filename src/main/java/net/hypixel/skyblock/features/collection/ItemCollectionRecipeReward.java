/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.collection;

import net.hypixel.skyblock.features.collection.ItemCollectionReward;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.ShapedRecipe;
import net.hypixel.skyblock.item.ShapelessRecipe;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ItemCollectionRecipeReward
extends ItemCollectionReward {
    private final SMaterial material;

    public ItemCollectionRecipeReward(SMaterial material) {
        super(ItemCollectionReward.Type.RECIPE);
        this.material = material;
    }

    @Override
    public void onAchieve(Player player) {
        for (ShapedRecipe shapedRecipe : ShapedRecipe.CACHED_RECIPES) {
            if (!this.recipeMatchesMaterial(shapedRecipe, this.material) || shapedRecipe.isVanilla()) continue;
            this.unlockRecipe(player, shapedRecipe);
        }
        for (ShapelessRecipe shapelessRecipe : ShapelessRecipe.CACHED_RECIPES) {
            if (!this.recipeMatchesMaterial(shapelessRecipe, this.material) || shapelessRecipe.isVanilla()) continue;
            this.unlockRecipe(player, shapelessRecipe);
        }
    }

    private boolean recipeMatchesMaterial(Recipe recipe, SMaterial material) {
        SItem sItem = SItem.of(material);
        return recipe.getResult().getDisplayName().equalsIgnoreCase(sItem.getDisplayName());
    }

    private void unlockRecipe(Player player, Recipe recipe) {
        SItem sItem = SItem.of(this.material);
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

