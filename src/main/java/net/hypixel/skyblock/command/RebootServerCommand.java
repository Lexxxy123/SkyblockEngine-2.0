package net.hypixel.skyblock.command;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.HashMap;
import java.util.Map;

@CommandParameters(description = "Spec test command.", aliases = "rebootserver", permission = PlayerRank.OWNER)
public class RebootServerCommand extends SCommand {
    public static Map<Server, Integer> secondMap;

    @Override
    public void run(CommandSource sender, String[] args) {
        if (!sender.getPlayer().isOp()) {
            sender.getPlayer().sendMessage(ChatColor.RED + "This command is highly restricted!");
            return;
        }
        if (secondMap.containsKey(Bukkit.getServer())) {
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
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
        }
        String reason = reasonraw;
        Bukkit.broadcastMessage(Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
        Bukkit.broadcastMessage(Sputnik.trans("&eYou have &a30 seconds &eto disconnect to prevent &cdata corruptions &ethat can result to inventories wipes!"));
        secondMap.put(Bukkit.getServer(), 30);
        new BukkitRunnable() {
            public void run() {
                secondMap.put(Bukkit.getServer(), secondMap.get(Bukkit.getServer()) - 1);
                if (5 >= RebootServerCommand.secondMap.get(Bukkit.getServer()) && 0 < RebootServerCommand.secondMap.get(Bukkit.getServer())) {
                    Bukkit.broadcastMessage(Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
                    Bukkit.broadcastMessage(Sputnik.trans("&eServer closing down in &c" + secondMap.get(Bukkit.getServer()) + " &eseconds"));
                } else if (0 >= RebootServerCommand.secondMap.get(Bukkit.getServer())) {
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
        }.runTaskTimer(SkyBlock.getPlugin(), 20L, 20L);
    }

    public static boolean isPrimeNumber(int n) {
        if (2 > n) {
            return false;
        }
        for (int squareRoot = (int) Math.sqrt(n), i = 2; i <= squareRoot; ++i) {
            if (0 == n % i) {
                return false;
            }
        }
        return true;
    }

    static {
        secondMap = new HashMap<Server, Integer>();
    }
}
