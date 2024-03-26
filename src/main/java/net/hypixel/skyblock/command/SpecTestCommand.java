package net.hypixel.skyblock.command;

import net.hypixel.skyblock.api.disguise.PlayerDisguise;
import net.hypixel.skyblock.api.hologram.Hologram;
import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.gui.RecipeCreatorGUI;
import net.hypixel.skyblock.util.PacketEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.Iterator;

@CommandParameters(description = "Spec test command.", aliases = "spectest", permission = PlayerRank.ADMIN)
public class SpecTestCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {

        switch (args[0]){
            case "giveRecipe" :
                sender.getUser().getUnlockedRecipes().add(args[1]);
                break;
            case "open":
                GUIType.RECIPE_CREATOR.getGUI().open(sender.getPlayer());
                break;
            case "loadAll":
                RecipeDatabase.loadRecipes();
        }
    }
}
