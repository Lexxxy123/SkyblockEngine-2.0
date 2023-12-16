package vn.giakhanhvn.skysim.command;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;

@CommandParameters(description = "Modify your absorption amount.", permission = "spt.player")
public class TradeCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (args.length != 1) {
            this.send(Sputnik.trans("&cWrong usage of command! It must be /trade <PLAYER>"));
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            this.send(Sputnik.trans("&cCannot find player named &f" + args[0] + "&c, maybe they've gone offline?"));
            return;
        }
        Sputnik.tradeIntitize(target, player);
    }
}
