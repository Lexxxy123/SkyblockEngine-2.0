package vn.giakhanhvn.skysim.command;

import java.util.Iterator;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.SkySimEngine;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@CommandParameters(description = "Modify your coin amount.", usage = "", aliases = "ss")
public class ServerCommand extends SCommand {
    public Map<UUID, List<String>> servers;

    public ServerCommand() {
        this.servers = new HashMap<UUID, List<String>>();
    }

    @Override
    public void run(final CommandSource sender, final String[] args) {
        final UUID runUUID = UUID.randomUUID();
        if (SkySimEngine.getPlugin().getBc() == null) {
            this.send("&cThis is not a BungeeCord based server!");
            return;
        }
        if (sender.getPlayer() == null) {
            this.send("&cConsole Sender cannot execute Proxy commands!");
            return;
        }
        final Player p = sender.getPlayer();
        if (!p.hasPermission("sse.proxy.sserver")) {
            this.send("&cThis command is restricted!");
            return;
        }
        if (args.length != 1) {
            this.send("&cCorrect Command Usage: /ss <server name>");
            return;
        }
        SkySimEngine.getPlugin().getBc().getServers().whenComplete((result, error) -> this.servers.put(runUUID, result));
        boolean isExist = false;
        String targetServer = null;
        for (final String sv : this.servers.get(runUUID)) {
            if (sv.equalsIgnoreCase(args[0])) {
                targetServer = sv;
                isExist = true;
            }
        }
        if (!isExist && targetServer == null) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.servers.get(runUUID).size(); ++i) {
                final String server = this.servers.get(runUUID).get(i);
                if (i == this.servers.size() - 1) {
                    sb.append(server);
                } else {
                    sb.append(server + ", ");
                }
            }
            this.send("&cThat server doesn't exist! &aYou may send players to these following servers: &f" + sb);
            this.servers.remove(runUUID);
            return;
        }
        final String finalTarget = targetServer;
        if (SkySimEngine.getPlugin().getServerName().equalsIgnoreCase(args[0])) {
            this.servers.remove(runUUID);
            this.send("&cYou're already playing on this server");
            return;
        }
        this.send("&7Hooking up request...");
        final User u = User.getUser(p.getUniqueId());
        u.syncSavingData();
        SUtil.delay(() -> {
            this.send("&7Sending you to " + finalTarget + "...");
            SkySimEngine.getPlugin().getBc().connect(p, finalTarget);
        }, 8L);
        this.servers.remove(runUUID);
    }
}
