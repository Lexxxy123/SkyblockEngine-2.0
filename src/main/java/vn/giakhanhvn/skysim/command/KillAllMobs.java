package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "kam", permission = "spt.item")
public class KillAllMobs extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player == null) {
            this.send(ChatColor.RED + "You can't use this command here!");
            return;
        }
        if (player.isOp()) {
            final World world = player.getWorld();
            for (final Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM_FRAME && entity.getType() != EntityType.MINECART && !entity.hasMetadata("pets") && !entity.hasMetadata("NPC") && !entity.hasMetadata("ss_drop") && !entity.hasMetadata("Ire") && !entity.hasMetadata("inv")) {
                    entity.remove();
                }
            }
            this.send(ChatColor.WHITE + "You removed" + ChatColor.YELLOW + " ALL" + ChatColor.RESET + " the mobs in this world.");
        } else {
            this.send(ChatColor.RED + "You can't use this, lol.");
        }
    }
}
