package vn.giakhanhvn.skysim;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SkyServer {
    private final Server server;

    public SkyServer(final Server s) {
        this.server = s;
    }

    public Collection<? extends Player> getPlayersOnline() {
        return Bukkit.getOnlinePlayers();
    }

    public List<User> getUsers() {
        final List<User> user = new ArrayList<User>();
        for (final Player bukkitPlayer : this.getPlayersOnline()) {
            user.add(User.getUser(bukkitPlayer.getUniqueId()));
        }
        return user;
    }

    public double getTPS() {
        return ReflectionTPS.getTPS();
    }

    public void getPacketListener() {
    }
}
