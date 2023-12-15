package vn.giakhanhvn.skysim.command;

import java.util.Iterator;

import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import vn.giakhanhvn.skysim.util.SLog;
import org.bukkit.ChatColor;

@CommandParameters(description = "Spec test command.", aliases = "fsd")
public class SaveDataCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player != null) {
            if (player.isOp()) {
                this.send(ChatColor.GRAY + "Performing save action, please wait...");
                SLog.info("[SYSTEM] Saving players data, this action was performed by " + player.getName() + "...");
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    final User user = User.getUser(p.getUniqueId());
                    if (user != null) {
                        user.saveCookie();
                        user.save();
                        user.saveAllVanillaInstances();
                    }
                }
                Bukkit.broadcastMessage(Sputnik.trans("&b[SKYSIM D.C] &aAll players data have been saved! Action performed by " + player.getDisplayName() + "&a!"));
            }
        } else {
            SLog.info("[SYSTEM] Saving players data, this action was performed by CONSOLE...");
            for (final Player p : Bukkit.getOnlinePlayers()) {
                final User user = User.getUser(p.getUniqueId());
                if (user != null) {
                    user.saveCookie();
                    user.save();
                    user.saveAllVanillaInstances();
                }
            }
            Bukkit.broadcastMessage(Sputnik.trans("&b[SKYSIM D.C] &aAll players data have been saved! Action performed by &cCONSOLE&a!"));
        }
    }
}
