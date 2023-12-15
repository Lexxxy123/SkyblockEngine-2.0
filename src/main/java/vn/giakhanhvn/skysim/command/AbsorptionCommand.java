package vn.giakhanhvn.skysim.command;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.SputnikPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Modify your absorption amount.", permission = "spt.player")
public class AbsorptionCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final EntityHuman human = ((CraftHumanEntity) player).getHandle();
        final float f = Float.parseFloat(args[0]);
        SputnikPlayer.setCustomAbsorptionHP(player, f);
        this.send(ChatColor.GREEN + "You now have " + ChatColor.GOLD + f + ChatColor.GREEN + " absorption hearts.");
    }
}
