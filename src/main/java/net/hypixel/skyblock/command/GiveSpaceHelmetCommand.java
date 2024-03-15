package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;

@CommandParameters(description = "bruhbu", aliases = "gsh", permission = PlayerRank.ADMIN)
public class GiveSpaceHelmetCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (!player.isOp()) {
            return;
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        User user = sender.getUser();
        if (0 == args.length) {
            this.send(ChatColor.RED + "Invaild Syntax!");
            return;
        }
        String pgv = args[0];
        String lore = args[1];
        SItem sitem = SItem.of(SMaterial.HIDDEN_DONATOR_HELMET);
        sitem.setDataString("p_given", player.getName());
        if (null != Bukkit.getPlayer(pgv)) {
            sitem.setDataString("p_rcv", pgv);
            if (null != args[1]) {
                sitem.setDataString("lore_d", lore);
            } else {
                sitem.setDataString("lore_d", "null");
            }
            player.getInventory().addItem(sitem.getStack());
            this.send(ChatColor.GREEN + "Done!");
        }
    }
}
