package vn.giakhanhvn.skysim.nms.nmsutil.apihelper;

import com.google.common.io.ByteArrayDataOutput;
import me.skysim.SLog;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class SkySimBungee
{
    private String channel;
    private List<String> servers;
    
    public SkySimBungee(final String channel) {
        this.servers = new ArrayList<String>();
        this.channel = channel;
    }
    
    public static SkySimBungee getNewBungee() {
        return new SkySimBungee("BungeeCord");
    }
    
    public void sendData(final Player p, final String subchannel, final String args) {
        Player sender = null;
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        if (args != null) {
            out.writeUTF(args);
        }
        if (p == null) {
            sender = (Player)Iterables.getFirst((Iterable)Bukkit.getOnlinePlayers(), (Object)null);
        }
        else {
            sender = p;
        }
        if (sender != null) {
            sender.sendPluginMessage((Plugin)SkySimEngine.getPlugin(), this.channel, out.toByteArray());
        }
        else {
            SLog.warn((Object)"Player object mustn't be null!");
        }
    }
}
