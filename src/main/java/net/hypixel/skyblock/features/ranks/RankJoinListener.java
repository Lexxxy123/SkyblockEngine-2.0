/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package net.hypixel.skyblock.features.ranks;

import java.io.File;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankJoinListener
implements Listener {
    private File rankFile = new File("plugins/SkyblockEngine/ranks.yml");
    private FileConfiguration rankConfig = YamlConfiguration.loadConfiguration((File)this.rankFile);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerId = player.getUniqueId().toString();
        if (this.rankConfig.contains(playerId + ".rank")) {
            String rankName = this.rankConfig.getString(playerId + ".rank");
            PlayerRank rank = PlayerRank.valueOf(rankName);
            User.getUser(player).setRank(rank);
        }
    }
}

