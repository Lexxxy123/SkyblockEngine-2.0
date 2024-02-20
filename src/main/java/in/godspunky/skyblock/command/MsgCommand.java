package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CommandParameters(description = "Message a player", aliases = "msg", permission = PlayerRank.DEFAULT)
public class MsgCommand extends SCommand {

    private static final Map<UUID, UUID> replyMap = new HashMap<>();

    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender.getPlayer() != null) {
            Player player = sender.getPlayer();
            if (args.length >= 2) {
                String targetName = args[0];
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                Player target = Bukkit.getPlayer(targetName);
                if (target != null && target.isOnline()) {
                    replyMap.put(player.getUniqueId(), target.getUniqueId());
                    replyMap.put(target.getUniqueId(), player.getUniqueId());

                    sender.send(Sputnik.trans("&dFrom Me to " + ChatColor.RESET + target.getDisplayName() + "&f: " + message));
                    target.sendMessage(Sputnik.trans("&dFrom " + ChatColor.RESET + player.getDisplayName()  +" &dto Me &7: " + message));
                } else {
                    sender.send(ChatColor.RED + "Player " + targetName + " is not online.");
                }
            } else {
                sender.send(ChatColor.RED + "Usage: /msg <player> <message>");
            }
        }else {
            sender.send("You can't Execute this!");
        }
    }

    public static UUID getReplyTarget(UUID playerUUID) {
        return replyMap.get(playerUUID);
    }
}