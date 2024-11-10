/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.nms.nmsutil.apihelper;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SkySimBungee {
    private final String channel;
    private final List<String> servers = new ArrayList<String>();

    public SkySimBungee(String channel) {
        this.channel = channel;
    }

    public static SkySimBungee getNewBungee() {
        return new SkySimBungee("BungeeCord");
    }

    public void sendData(Player p, String subchannel, String args) {
        Player sender = null;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        if (args != null) {
            out.writeUTF(args);
        }
        if ((sender = p == null ? (Player)Iterables.getFirst(Bukkit.getOnlinePlayers(), null) : p) != null) {
            sender.sendPluginMessage((Plugin)SkyBlock.getPlugin(), this.channel, out.toByteArray());
        } else {
            SLog.warn("Player object mustn't be null!");
        }
    }
}

