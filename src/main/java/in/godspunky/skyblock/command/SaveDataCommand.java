package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.user.SMongoLoader;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SLog;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spec test command.", aliases = "fsd", permission = PlayerRank.ADMIN)
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
                        SMongoLoader.save(p.getUniqueId());
                    }
                }
                Bukkit.broadcastMessage(Sputnik.trans("&b[SKYSIM D.C] &aAll players data have been saved! Action performed by " + player.getDisplayName() + "&a!"));
            }
        } else {
            SLog.info("[SYSTEM] Saving players data, this action was performed by CONSOLE...");
            for (final Player p : Bukkit.getOnlinePlayers()) {
                final User user = User.getUser(p.getUniqueId());
                if (user != null) {
                    SMongoLoader.save(p.getUniqueId());
                }
            }
            Bukkit.broadcastMessage(Sputnik.trans("&b[SKYSIM D.C] &aAll players data have been saved! Action performed by &cCONSOLE&a!"));
        }
    }
}
