package vn.giakhanhvn.skysim.nms.nmsutil.packetlistener.handler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.minecraft.Minecraft;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.FieldResolver;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.MethodResolver;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.resolver.minecraft.NMSClassResolver;
import vn.giakhanhvn.skysim.nms.nmsutil.reflection.util.AccessUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class PacketHandler {
    private static final List<PacketHandler> handlers;
    private boolean hasSendOptions;
    private boolean forcePlayerSend;
    private boolean forceServerSend;
    private boolean hasReceiveOptions;
    private boolean forcePlayerReceive;
    private boolean forceServerReceive;
    static NMSClassResolver nmsClassResolver;
    static FieldResolver EntityPlayerFieldResolver;
    static MethodResolver PlayerConnectionMethodResolver;
    private Plugin plugin;

    public static boolean addHandler(final PacketHandler handler) {
        final boolean b = PacketHandler.handlers.contains(handler);
        if (!b) {
            try {
                final PacketOptions options = handler.getClass().getMethod("onSend", SentPacket.class).getAnnotation(PacketOptions.class);
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
            } catch (final Exception e) {
                throw new RuntimeException("Failed to register handler (onSend)", e);
            }
            try {
                final PacketOptions options = handler.getClass().getMethod("onReceive", ReceivedPacket.class).getAnnotation(PacketOptions.class);
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
            } catch (final Exception e) {
                throw new RuntimeException("Failed to register handler (onReceive)", e);
            }
        }
        PacketHandler.handlers.add(handler);
        return !b;
    }

    public static boolean removeHandler(final PacketHandler handler) {
        return PacketHandler.handlers.remove(handler);
    }

    public static void notifyHandlers(final SentPacket packet) {
        for (final PacketHandler handler : getHandlers()) {
            try {
                if (handler.hasSendOptions) {
                    if (handler.forcePlayerSend) {
                        if (!packet.hasPlayer()) {
                            continue;
                        }
                    } else if (handler.forceServerSend && !packet.hasChannel()) {
                        continue;
                    }
                }
                handler.onSend(packet);
            } catch (final Exception e) {
                System.err.println("[SkySim Protocol Injector] An exception occured while trying to execute 'onSend'" + ((handler.plugin != null) ? (" in plugin " + handler.plugin.getName()) : "") + ": " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    public static void notifyHandlers(final ReceivedPacket packet) {
        for (final PacketHandler handler : getHandlers()) {
            try {
                if (handler.hasReceiveOptions) {
                    if (handler.forcePlayerReceive) {
                        if (!packet.hasPlayer()) {
                            continue;
                        }
                    } else if (handler.forceServerReceive && !packet.hasChannel()) {
                        continue;
                    }
                }
                handler.onReceive(packet);
            } catch (final Exception e) {
                System.err.println("[SkySim Protocol Injector] An exception occured while trying to execute 'onReceive'" + ((handler.plugin != null) ? (" in plugin " + handler.plugin.getName()) : "") + ": " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final PacketHandler that = (PacketHandler) object;
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
        } else return that.plugin == null;
    }

    @Override
    public int hashCode() {
        int result = this.hasSendOptions ? 1 : 0;
        result = 31 * result + (this.forcePlayerSend ? 1 : 0);
        result = 31 * result + (this.forceServerSend ? 1 : 0);
        result = 31 * result + (this.hasReceiveOptions ? 1 : 0);
        result = 31 * result + (this.forcePlayerReceive ? 1 : 0);
        result = 31 * result + (this.forceServerReceive ? 1 : 0);
        result = 31 * result + ((this.plugin != null) ? this.plugin.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PacketHandler{hasSendOptions=" + this.hasSendOptions + ", forcePlayerSend=" + this.forcePlayerSend + ", forceServerSend=" + this.forceServerSend + ", hasReceiveOptions=" + this.hasReceiveOptions + ", forcePlayerReceive=" + this.forcePlayerReceive + ", forceServerReceive=" + this.forceServerReceive + ", plugin=" + this.plugin + '}';
    }

    public static List<PacketHandler> getHandlers() {
        return new ArrayList<PacketHandler>(PacketHandler.handlers);
    }

    public static List<PacketHandler> getForPlugin(final Plugin plugin) {
        final List<PacketHandler> handlers = new ArrayList<PacketHandler>();
        if (plugin == null) {
            return handlers;
        }
        for (final PacketHandler h : getHandlers()) {
            if (plugin.equals(h.getPlugin())) {
                handlers.add(h);
            }
        }
        return handlers;
    }

    public void sendPacket(final Player p, final Object packet) {
        if (p == null || packet == null) {
            throw new NullPointerException();
        }
        try {
            final Object handle = Minecraft.getHandle(p);
            final Object connection = PacketHandler.EntityPlayerFieldResolver.resolve("playerConnection").get(handle);
            PacketHandler.PlayerConnectionMethodResolver.resolve("sendPacket").invoke(connection, packet);
        } catch (final Exception e) {
            System.err.println("[SkySim Protocol Injector] Exception while sending " + packet + " to " + p);
            e.printStackTrace();
        }
    }

    public Object cloneObject(final Object obj) throws Exception {
        if (obj == null) {
            return obj;
        }
        final Object clone = obj.getClass().newInstance();
        for (Field f : obj.getClass().getDeclaredFields()) {
            f = AccessUtil.setAccessible(f);
            f.set(clone, f.get(obj));
        }
        return clone;
    }

    public PacketHandler() {
    }

    public PacketHandler(final Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public abstract void onSend(final SentPacket p0);

    public abstract void onReceive(final ReceivedPacket p0);

    static {
        handlers = new ArrayList<PacketHandler>();
        PacketHandler.nmsClassResolver = new NMSClassResolver();
        PacketHandler.EntityPlayerFieldResolver = new FieldResolver(PacketHandler.nmsClassResolver.resolveSilent("EntityPlayer"));
        PacketHandler.PlayerConnectionMethodResolver = new MethodResolver(PacketHandler.nmsClassResolver.resolveSilent("PlayerConnection"));
    }
}
