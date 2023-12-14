package vn.giakhanhvn.skysim;

import java.util.Iterator;
import java.util.ArrayList;
import vn.giakhanhvn.skysim.user.User;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Collection;
import org.bukkit.Server;

public class SkyServer
{
    private Server server;
    
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
