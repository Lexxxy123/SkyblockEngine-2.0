package vn.giakhanhvn.skysim.command;

import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "bruhbu", aliases = "resetcb")
public class ResetCookieCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final User user = sender.getUser();
        if (!player.isOp()) {
            this.send(ChatColor.RED + "No permission to execute this command!");
            return;
        }
        if (args.length == 0) {
            this.send(ChatColor.RED + "Invaild Syntax! You need to provide a player");
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target != null) {
            PlayerUtils.setCookieDurationTicks(target, 0L);
            this.send(Sputnik.trans("&aReseted " + target.getName() + "'s &dCookie Buff&a."));
            target.sendMessage(Sputnik.trans("&e[WARNING] ") + ChatColor.RED + player.getName() + " have reseted your Cookie Buff. If you believe this is an error, contact Admins.");
            return;
        }
        this.send(ChatColor.RED + "Invaild Syntax! You need to provide a vaild player");
    }
}
