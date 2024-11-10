/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelHandler
 *  net.minecraft.server.v1_8_R3.MinecraftServer
 *  net.minecraft.server.v1_8_R3.NetworkManager
 *  net.minecraft.server.v1_8_R3.ServerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftServer
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.ServerListPingEvent
 */
package net.hypixel.skyblock.nms.pingrep;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import net.hypixel.skyblock.nms.pingrep.DuplexHandler;
import net.hypixel.skyblock.nms.pingrep.reflect.ReflectUtils;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.ServerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingInjector
implements Listener {
    private MinecraftServer server;
    private List<?> networkManagers;

    public PingInjector() {
        try {
            CraftServer craftserver = (CraftServer)Bukkit.getServer();
            Field console = craftserver.getClass().getDeclaredField("console");
            console.setAccessible(true);
            this.server = (MinecraftServer)console.get(craftserver);
            ServerConnection conn = this.server.aq();
            this.networkManagers = Collections.synchronizedList((List)this.getNetworkManagerList(conn));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void injectOpenConnections() {
        try {
            Field field = ReflectUtils.getFirstFieldByType(NetworkManager.class, Channel.class);
            field.setAccessible(true);
            for (Object manager : this.networkManagers.toArray()) {
                Channel channel = (Channel)field.get(manager);
                if (channel.pipeline().context("pingapi_handler") != null || channel.pipeline().context("packet_handler") == null) continue;
                channel.pipeline().addBefore("packet_handler", "pingapi_handler", (ChannelHandler)new DuplexHandler());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException runtimeException) {
            // empty catch block
        }
    }

    public Object getNetworkManagerList(ServerConnection serverConnection) {
        Field field = null;
        try {
            field = serverConnection.getClass().getDeclaredField("h");
            field.setAccessible(true);
            return field.get(serverConnection);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    @EventHandler
    public void serverListPing(ServerListPingEvent event) {
        this.injectOpenConnections();
    }
}

