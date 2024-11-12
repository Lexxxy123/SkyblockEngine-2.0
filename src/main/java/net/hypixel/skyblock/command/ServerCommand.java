/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.entity.Player;

@CommandParameters(description="Modify your coin amount.", usage="", aliases="ss", permission=PlayerRank.ADMIN)
public class ServerCommand
extends SCommand {
    public Map<UUID, List<String>> servers = new HashMap<UUID, List<String>>();

    @Override
    public void run(CommandSource sender, String[] args) {
        UUID runUUID = UUID.randomUUID();
        if (SkyBlock.getPlugin().getBc() == null) {
            this.send("&cThis is not a BungeeCord based server!");
            return;
        }
        if (sender.getPlayer() == null) {
            this.send("&cConsole Sender cannot execute Proxy commands!");
            return;
        }
        Player p2 = sender.getPlayer();
        if (!p2.hasPermission("sse.proxy.sserver")) {
            this.send("&cThis command is restricted!");
            return;
        }
        if (args.length != 1) {
            this.send("&cCorrect Command Usage: /ss <server name>");
            return;
        }
        SkyBlock.getPlugin().getBc().getServers().whenComplete((result, error) -> this.servers.put(runUUID, (List<String>)result));
        boolean isExist = false;
        String targetServer = null;
        for (String sv : this.servers.get(runUUID)) {
            if (!sv.equalsIgnoreCase(args[0])) continue;
            targetServer = sv;
            isExist = true;
        }
        if (!isExist && targetServer == null) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < this.servers.get(runUUID).size(); ++i2) {
                String server = this.servers.get(runUUID).get(i2);
                if (i2 == this.servers.size() - 1) {
                    sb.append(server);
                    continue;
                }
                sb.append(server + ", ");
            }
            this.send("&cThat server doesn't exist! &aYou may send players to these following servers: &f" + sb);
            this.servers.remove(runUUID);
            return;
        }
        String finalTarget = targetServer;
        if (SkyBlock.getPlugin().getServerName().equalsIgnoreCase(args[0])) {
            this.servers.remove(runUUID);
            this.send("&cYou're already playing on this server");
            return;
        }
        this.send("&7Hooking up request...");
        User u2 = User.getUser(p2.getUniqueId());
        u2.asyncSavingData();
        SUtil.delay(() -> {
            this.send("&7Sending you to " + finalTarget + "...");
            SkyBlock.getPlugin().getBc().connect(p2, finalTarget);
        }, 8L);
        this.servers.remove(runUUID);
    }
}

