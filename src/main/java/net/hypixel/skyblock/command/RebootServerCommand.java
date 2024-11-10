/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

@CommandParameters(description="Spec test command.", aliases="rebootserver", permission=PlayerRank.OWNER)
public class RebootServerCommand
extends SCommand {
    public static Map<Server, Integer> secondMap = new HashMap<Server, Integer>();

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
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"ptitle2");
            reasonraw = "Scheduled Reboot";
        } else if (args[0].contains("update")) {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"ptitle1");
            reasonraw = "For a game update";
        } else {
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"ptitle3");
            reasonraw = "Unknown Reason!";
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
        }
        final String reason = reasonraw;
        Bukkit.broadcastMessage((String)Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
        Bukkit.broadcastMessage((String)Sputnik.trans("&eYou have &a30 seconds &eto disconnect to prevent &cdata corruptions &ethat can result to inventories wipes!"));
        secondMap.put(Bukkit.getServer(), 30);
        new BukkitRunnable(){

            public void run() {
                secondMap.put(Bukkit.getServer(), secondMap.get(Bukkit.getServer()) - 1);
                if (5 >= secondMap.get(Bukkit.getServer()) && 0 < secondMap.get(Bukkit.getServer())) {
                    Bukkit.broadcastMessage((String)Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
                    Bukkit.broadcastMessage((String)Sputnik.trans("&eServer closing down in &c" + secondMap.get(Bukkit.getServer()) + " &eseconds"));
                } else if (0 >= secondMap.get(Bukkit.getServer())) {
                    Bukkit.broadcastMessage((String)Sputnik.trans("&c[Important] &eThe server will restart soon: &b" + reason));
                    Bukkit.broadcastMessage((String)Sputnik.trans("&eServer is &cshutting down&e!"));
                    this.cancel();
                    SUtil.delay(() -> {
                        Bukkit.broadcastMessage((String)Sputnik.trans("&7Saving..."));
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"fsd");
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"kickall &cThe Server is restarting!");
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)"stop");
                    }, 10L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 20L, 20L);
    }

    public static boolean isPrimeNumber(int n) {
        if (2 > n) {
            return false;
        }
        int squareRoot = (int)Math.sqrt(n);
        for (int i = 2; i <= squareRoot; ++i) {
            if (0 != n % i) continue;
            return false;
        }
        return true;
    }
}

