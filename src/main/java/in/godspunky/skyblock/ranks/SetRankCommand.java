package in.godspunky.skyblock.ranks;

import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        GodspunkyPlayer user = GodspunkyPlayer.getUser(player);
        if (sender.isOp() || Objects.requireNonNull(user).rank.isAboveOrEqual(PlayerRank.ADMIN)) {
            if (args.length >= 2) {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    PlayerRank newRank = PlayerRank.valueOf(args[1].toUpperCase().replace("+", "P"));
                    GodspunkyPlayer.getUser(target).setRank(newRank);
                    String prefix = newRank == PlayerRank.DEFAULT ? "&7Default" : newRank.getPrefix().replace("[", "").replace("]", "");
                    sender.sendMessage(Sputnik.trans("&aSet " + args[0] + "'s Rank To " + prefix + "&a!"));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(Sputnik.trans("&cUsage: /setrank <player> <rank>"));
                return false;
            }
        } else {
            sender.sendMessage(Sputnik.trans("&cYou need ADMIN rank to use this command."));
            return false;
        }
        return false;
    }
}