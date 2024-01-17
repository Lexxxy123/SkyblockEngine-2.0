package in.godspunky.skyblock.objectives.starting;


import in.godspunky.skyblock.objectives.Objective;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakLogObjective extends Objective {

    public BreakLogObjective() {
        super("break_log", "Break a log");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!isThisObjective(e.getPlayer())) return;

        if (e.getBlock().getType().equals(Material.LOG)) complete(e.getPlayer());
    }
}
