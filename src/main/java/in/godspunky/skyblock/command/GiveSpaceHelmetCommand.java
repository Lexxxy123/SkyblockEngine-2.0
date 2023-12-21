package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "bruhbu", aliases = "gsh")
public class GiveSpaceHelmetCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final User user = sender.getUser();
        if (args.length == 0) {
            this.send(ChatColor.RED + "Invaild Syntax!");
            return;
        }
        final String pgv = args[0];
        final String lore = args[1];
        final SItem sitem = SItem.of(SMaterial.HIDDEN_DONATOR_HELMET);
        sitem.setDataString("p_given", player.getName());
        if (Bukkit.getPlayer(pgv) != null) {
            sitem.setDataString("p_rcv", pgv);
            if (args[1] != null) {
                sitem.setDataString("lore_d", lore);
            } else {
                sitem.setDataString("lore_d", "null");
            }
            player.getInventory().addItem(sitem.getStack());
            this.send(ChatColor.GREEN + "Done!");
        }
    }
}
