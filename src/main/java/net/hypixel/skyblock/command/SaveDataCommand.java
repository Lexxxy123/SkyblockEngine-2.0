/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description="Spec test command.", aliases="fsd", permission=PlayerRank.ADMIN)
public class SaveDataCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player != null) {
            if (player.isOp()) {
                this.send(ChatColor.GRAY + "Performing save action, please wait...");
                SLog.info("[SYSTEM] Saving players data, this action was performed by " + player.getName() + "...");
                for (Player p2 : Bukkit.getOnlinePlayers()) {
                    User user = User.getUser(p2.getUniqueId());
                    if (user == null) continue;
                    if (SkyBlock.getPlugin().config.getBoolean("Config")) {
                        user.configsave();
                        continue;
                    }
                    user.save();
                }
                Bukkit.broadcastMessage((String)Sputnik.trans("&b[SkyBlock D.C] &aAll players data have been saved! Action performed by " + player.getDisplayName() + "&a!"));
            }
        } else {
            SLog.info("[SYSTEM] Saving players data, this action was performed by CONSOLE...");
            for (Player p3 : Bukkit.getOnlinePlayers()) {
                User user = User.getUser(p3.getUniqueId());
                if (user == null) continue;
                user.save();
            }
            Bukkit.broadcastMessage((String)Sputnik.trans("&b[SkyBlock D.C] &aAll players data have been saved! Action performed by &cCONSOLE&a!"));
        }
    }
}

