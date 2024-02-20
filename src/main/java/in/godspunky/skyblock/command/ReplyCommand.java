package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandParameters(description = "Reply Player", aliases = "reply,r", permission = PlayerRank.DEFAULT)
public class ReplyCommand extends SCommand {


    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender.getPlayer() != null) {
            Player player = sender.getPlayer();
            UUID replyTarget = MsgCommand.getReplyTarget(player.getUniqueId());
            if (replyTarget != null) {
                if (args.length >= 1) {
                    String message = String.join(" ", args);

                    Player target = Bukkit.getPlayer(replyTarget);
                    if (target != null && target.isOnline()) {
                        sender.send(ChatColor.LIGHT_PURPLE + "From me to " + ChatColor.RESET + target.getDisplayName() + ChatColor.WHITE + ": " + message);
                        target.sendMessage(ChatColor.LIGHT_PURPLE + "From " + ChatColor.RESET + player.getDisplayName() + ChatColor.LIGHT_PURPLE + " to Me: " + ChatColor.WHITE + message);
                    } else {
                        sender.send(ChatColor.RED + "Your conversation partner is no longer online.");
                    }
                } else {
                    sender.send(ChatColor.RED + "Usage: /reply <message>");
                }
            } else {
                sender.send(ChatColor.RED + "You have no recent conversation to reply to.");
            }
        }else {
            sender.send("U can't execute this!");
        }
    }
}