package vn.giakhanhvn.skysim.command;

import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "bruhbu", aliases = "gsh")
public class GiveSpaceHelmetCommand extends SCommand
{
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
            }
            else {
                sitem.setDataString("lore_d", "null");
            }
            player.getInventory().addItem(new ItemStack[] { sitem.getStack() });
            this.send(ChatColor.GREEN + "Done!");
        }
    }
}
