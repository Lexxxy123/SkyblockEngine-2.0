package in.godspunky.skyblock.command;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@CommandParameters(description = "Spec test command.", aliases = "rebootserver", permission = PlayerRank.ADMIN)
public class RebootServerCommand extends SCommand {
    public static Map<Server, Integer> secondMap;

    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
            this.send(ChatColor.RED + "You cannot schedule more than 1 server reboot at a time");
            return;
        }
        String reasonraw = "";
        if (args[0].contains("time")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ptitle2");
            reasonraw = "Scheduled Reboot";
        } else if (args[0].contains("update")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ptitle1");
            reasonraw = "For a game update";
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ptitle3");
            reasonraw = "Unknown Reason!";
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
        }
        final String reason = reasonraw;
        Bukkit.broadcastMessage(Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
        Bukkit.broadcastMessage(Sputnik.trans("&eYou have &a30 seconds &eto disconnect to prevent &cdata corruptions &ethat can result to inventories wipes!"));
        RebootServerCommand.secondMap.put(Bukkit.getServer(), 30);
        new BukkitRunnable() {
            public void run() {
                RebootServerCommand.secondMap.put(Bukkit.getServer(), RebootServerCommand.secondMap.get(Bukkit.getServer()) - 1);
                if (RebootServerCommand.secondMap.get(Bukkit.getServer()) <= 5 && RebootServerCommand.secondMap.get(Bukkit.getServer()) > 0) {
                    Bukkit.broadcastMessage(Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
                    Bukkit.broadcastMessage(Sputnik.trans("&eServer closing down in &c" + RebootServerCommand.secondMap.get(Bukkit.getServer()) + " &eseconds"));
                } else if (RebootServerCommand.secondMap.get(Bukkit.getServer()) <= 0) {
                    Bukkit.broadcastMessage(Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
                    Bukkit.broadcastMessage(Sputnik.trans("&eServer is &cshutting down&e!"));
                    this.cancel();
                    SUtil.delay(() -> {
                        Bukkit.broadcastMessage(Sputnik.trans("&7Saving..."));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fsd");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kickall &cThe Server is restarting!");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }, 10L);
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 20L, 20L);
    }

    public static boolean isPrimeNumber(final int n) {
        if (n < 2) {
            return false;
        }
        for (int squareRoot = (int) Math.sqrt(n), i = 2; i <= squareRoot; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    static {
        RebootServerCommand.secondMap = new HashMap<Server, Integer>();
    }
}
