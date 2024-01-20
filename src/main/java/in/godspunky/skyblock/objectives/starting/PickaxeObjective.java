package in.godspunky.skyblock.objectives.starting;


import in.godspunky.skyblock.event.SkyBlockCraftEvent;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.objectives.Objective;
import org.bukkit.event.EventHandler;

public class PickaxeObjective extends Objective {

    public PickaxeObjective() {
        super("craft_pickaxe", "Craft a wood pickaxe");
    }

    @EventHandler
    public void onCraft(SkyBlockCraftEvent e) {
        if (!isThisObjective(e.getPlayer())) return;

        if (e.getRecipe().getResult().getType().equals(SMaterial.WOOD_PICKAXE)) complete(e.getPlayer());
    }
}
