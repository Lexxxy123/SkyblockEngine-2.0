package net.hypixel.skyblock.command;


import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "kick player", aliases = "kick", permission = PlayerRank.HELPER)
public class Kick extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
            if (args.length >= 2) {
                String reason = "";
                for (int i = 1; i < args.length; i++) {
                    reason = String.valueOf(reason) + args[i] + " ";
                }
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target == null) {
                    sender.send("§cPlayer does not exist or offline.");
                }
                sender.send("§aKicked player " + Bukkit.getPlayer(args[0]).getName() + " for " + reason);
                target.kickPlayer("§cYou have been kicked!\n\n§7Reason: §f" + reason + "\n§7Find out more: §b§n" + "https://discord.gg/godspunky");

            }
            sender.send("§cInvalid syntax. Correct: /kick <name> <reason>");
    }
}
