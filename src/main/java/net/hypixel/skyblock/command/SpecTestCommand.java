package net.hypixel.skyblock.command;


import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;


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
