package in.godspunky.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

@CommandParameters(description = "Modify your coin amount.", permission = "spt.balance")
public class CoinsCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 0 && args.length != 2) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final User user = sender.getUser();
        if (args.length == 0) {
            user.setPermanentCoins(!user.isPermanentCoins());
            this.send(ChatColor.GREEN + "Your coins are no" + (user.isPermanentCoins() ? "w" : " longer") + " permanent.");
            return;
        }
        final long coins = Long.parseLong(args[1]);
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "add":
                user.addCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have added " + ChatColor.GOLD + SUtil.commaify(coins) + ChatColor.GREEN + " to your purse.");
                return;
            case "subtract":
            case "sub":
            case "take":
                user.subCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have subtracted " + ChatColor.GOLD + SUtil.commaify(coins) + ChatColor.GREEN + " from your Purse.");
                return;
            case "set":
                user.setCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have set your Purse coins to " + ChatColor.GOLD + SUtil.commaify(coins) + ".");
                return;
            default:
                throw new CommandArgumentException();
        }
    }
}
