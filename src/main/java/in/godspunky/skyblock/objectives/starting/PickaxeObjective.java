package in.godspunky.skyblock.objectives.starting;


import in.godspunky.skyblock.event.CraftEvent;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.objectives.Objective;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;

public class PickaxeObjective extends Objective {

    public PickaxeObjective() {
        super("craft_pickaxe", "Craft a wood pickaxe");
    }

    @EventHandler
    public void onCraft(CraftEvent e) {
        if (!isThisObjective(e.getPlayer())) return;

        if (e.getRecipe().getResult().getType().equals(SMaterial.CRAFTING_TABLE)) complete(e.getPlayer());
    }
}
