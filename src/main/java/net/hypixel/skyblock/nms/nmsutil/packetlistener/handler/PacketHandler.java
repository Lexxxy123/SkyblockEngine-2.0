/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.PacketOptions;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import net.hypixel.skyblock.nms.nmsutil.reflection.minecraft.Minecraft;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.FieldResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.MethodResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.minecraft.NMSClassResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.util.AccessUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class PacketHandler {
    private static final List<PacketHandler> handlers = new ArrayList<PacketHandler>();
    private boolean hasSendOptions;
    private boolean forcePlayerSend;
    private boolean forceServerSend;
    private boolean hasReceiveOptions;
    private boolean forcePlayerReceive;
    private boolean forceServerReceive;
    static NMSClassResolver nmsClassResolver = new NMSClassResolver();
    static FieldResolver EntityPlayerFieldResolver = new FieldResolver(nmsClassResolver.resolveSilent("EntityPlayer"));
    static MethodResolver PlayerConnectionMethodResolver = new MethodResolver(nmsClassResolver.resolveSilent("PlayerConnection"));
    private Plugin plugin;

    public static boolean addHandler(PacketHandler handler) {
        boolean b2 = handlers.contains(handler);
        if (!b2) {
            PacketOptions options;
            try {
                options = handler.getClass().getMethod("onSend", SentPacket.class).getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.hasSendOptions = true;
                    if (options.forcePlayer() && options.forceServer()) {
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
                    }
                    if (options.forcePlayer()) {
                        handler.forcePlayerSend = true;
                    } else if (options.forceServer()) {
                        handler.forceServerSend = true;
                    }
                }
            } catch (Exception e2) {
                throw new RuntimeException("Failed to register handler (onSend)", e2);
            }
            try {
                options = handler.getClass().getMethod("onReceive", ReceivedPacket.class).getAnnotation(PacketOptions.class);
                if (options != null) {
                    handler.hasReceiveOptions = true;
                    if (options.forcePlayer() && options.forceServer()) {
                        throw new IllegalArgumentException("Cannot force player and server packets at the same time!");
                    }
                    if (options.forcePlayer()) {
                        handler.forcePlayerReceive = true;
                    } else if (options.forceServer()) {
                        handler.forceServerReceive = true;
                    }
                }
            } catch (Exception e3) {
                throw new RuntimeException("Failed to register handler (onReceive)", e3);
            }
        }
        handlers.add(handler);
        return !b2;
    }

    public static boolean removeHandler(PacketHandler handler) {
        return handlers.remove(handler);
    }

    public static void notifyHandlers(SentPacket packet) {
        for (PacketHandler handler : PacketHandler.getHandlers()) {
            try {
                if (handler.hasSendOptions && (handler.forcePlayerSend ? !packet.hasPlayer() : handler.forceServerSend && !packet.hasChannel())) continue;
                handler.onSend(packet);
            } catch (Exception e2) {
                System.err.println("[GodSpunky Protocol Injector] An exception occured while trying to execute 'onSend'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e2.getMessage());
                e2.printStackTrace(System.err);
            }
        }
    }

    public static void notifyHandlers(ReceivedPacket packet) {
        for (PacketHandler handler : PacketHandler.getHandlers()) {
            try {
                if (handler.hasReceiveOptions && (handler.forcePlayerReceive ? !packet.hasPlayer() : handler.forceServerReceive && !packet.hasChannel())) continue;
                handler.onReceive(packet);
            } catch (Exception e2) {
                System.err.println("[GodSpunky Protocol Injector] An exception occured while trying to execute 'onReceive'" + (handler.plugin != null ? " in plugin " + handler.plugin.getName() : "") + ": " + e2.getMessage());
                e2.printStackTrace(System.err);
            }
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        PacketHandler that = (PacketHandler)object;
        if (this.hasSendOptions != that.hasSendOptions) {
            return false;
        }
        if (this.forcePlayerSend != that.forcePlayerSend) {
            return false;
        }
        if (this.forceServerSend != that.forceServerSend) {
            return false;
        }
        if (this.hasReceiveOptions != that.hasReceiveOptions) {
            return false;
        }
        if (this.forcePlayerReceive != that.forcePlayerReceive) {
            return false;
        }
        if (this.forceServerReceive != that.forceServerReceive) {
            return false;
        }
        if (this.plugin != null) {
            return this.plugin.equals(that.plugin);
        }
        return that.plugin == null;
    }

    public int hashCode() {
        int result = this.hasSendOptions ? 1 : 0;
        result = 31 * result + (this.forcePlayerSend ? 1 : 0);
        result = 31 * result + (this.forceServerSend ? 1 : 0);
        result = 31 * result + (this.hasReceiveOptions ? 1 : 0);
        result = 31 * result + (this.forcePlayerReceive ? 1 : 0);
        result = 31 * result + (this.forceServerReceive ? 1 : 0);
        result = 31 * result + (this.plugin != null ? this.plugin.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "PacketHandler{hasSendOptions=" + this.hasSendOptions + ", forcePlayerSend=" + this.forcePlayerSend + ", forceServerSend=" + this.forceServerSend + ", hasReceiveOptions=" + this.hasReceiveOptions + ", forcePlayerReceive=" + this.forcePlayerReceive + ", forceServerReceive=" + this.forceServerReceive + ", plugin=" + this.plugin + '}';
    }

    public static List<PacketHandler> getHandlers() {
        return new ArrayList<PacketHandler>(handlers);
    }

    public static List<PacketHandler> getForPlugin(Plugin plugin) {
        ArrayList<PacketHandler> handlers = new ArrayList<PacketHandler>();
        if (plugin == null) {
            return handlers;
        }
        for (PacketHandler h2 : PacketHandler.getHandlers()) {
            if (!plugin.equals(h2.getPlugin())) continue;
            handlers.add(h2);
        }
        return handlers;
    }

    public void sendPacket(Player p2, Object packet) {
        if (p2 == null || packet == null) {
            throw new NullPointerException();
        }
        try {
            Object handle = Minecraft.getHandle(p2);
            Object connection = EntityPlayerFieldResolver.resolve("playerConnection").get(handle);
            PlayerConnectionMethodResolver.resolve("sendPacket").invoke(connection, packet);
        } catch (Exception e2) {
            System.err.println("[SkyBlock Protocol Injector] Exception while sending " + packet + " to " + p2);
            e2.printStackTrace();
        }
    }

    public Object cloneObject(Object obj) throws Exception {
        if (obj == null) {
            return obj;
        }
        Object clone = obj.getClass().newInstance();
        for (Field f2 : obj.getClass().getDeclaredFields()) {
            f2 = AccessUtil.setAccessible(f2);
            f2.set(clone, f2.get(obj));
        }
        return clone;
    }

    public PacketHandler() {
    }

    public PacketHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public abstract void onSend(SentPacket var1);

    public abstract void onReceive(ReceivedPacket var1);
}

