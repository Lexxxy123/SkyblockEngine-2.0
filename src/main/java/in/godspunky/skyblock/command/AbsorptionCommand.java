package in.godspunky.skyblock.command;

import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.util.SputnikPlayer;

@CommandParameters(description = "Modify your absorption amount.", permission = "spt.player")
public class AbsorptionCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (1 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        EntityHuman human = ((CraftHumanEntity) player).getHandle();
        float f = Float.parseFloat(args[0]);
        SputnikPlayer.setCustomAbsorptionHP(player, f);
        this.send(ChatColor.GREEN + "You now have " + ChatColor.GOLD + f + ChatColor.GREEN + " absorption hearts.");
    }
}
