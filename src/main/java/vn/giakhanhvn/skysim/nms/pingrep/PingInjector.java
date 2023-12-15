package vn.giakhanhvn.skysim.nms.pingrep;

import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

import io.netty.channel.ChannelHandler;
import vn.giakhanhvn.skysim.nms.pingrep.reflect.ReflectUtils;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.ServerConnection;

import java.lang.reflect.Field;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.List;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.event.Listener;

public class PingInjector implements Listener {
    private MinecraftServer server;
    private List<?> networkManagers;

    public PingInjector() {
        try {
            final CraftServer craftserver = (CraftServer) Bukkit.getServer();
            final Field console = craftserver.getClass().getDeclaredField("console");
            console.setAccessible(true);
            this.server = (MinecraftServer) console.get(craftserver);
            final ServerConnection conn = this.server.aq();
            this.networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void injectOpenConnections() {
        try {
            final Field field = ReflectUtils.getFirstFieldByType(NetworkManager.class, Channel.class);
            field.setAccessible(true);
            for (final Object manager : this.networkManagers.toArray()) {
                final Channel channel = (Channel) field.get(manager);
                if (channel.pipeline().context("pingapi_handler") == null && channel.pipeline().context("packet_handler") != null) {
                    channel.pipeline().addBefore("packet_handler", "pingapi_handler", new DuplexHandler());
                }
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final NullPointerException | IllegalArgumentException | NoSuchElementException ex) {
        }
    }

    public Object getNetworkManagerList(final ServerConnection conn) {
        try {
            for (final Method method : conn.getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.getReturnType() == List.class) {
                    final Object object = method.invoke(null, conn);
                    return object;
                }
            }
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventHandler
    public void serverListPing(final ServerListPingEvent event) {
        this.injectOpenConnections();
    }
}
