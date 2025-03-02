/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener.channel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.Cancellable;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.IPacketListener;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.FieldResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.MethodResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.minecraft.NMSClassResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.util.AccessUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class ChannelAbstract {
    protected static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
    static final Class<?> EntityPlayer = nmsClassResolver.resolveSilent("EntityPlayer");
    static final Class<?> PlayerConnection = nmsClassResolver.resolveSilent("PlayerConnection");
    static final Class<?> NetworkManager = nmsClassResolver.resolveSilent("NetworkManager");
    static final Class<?> Packet = nmsClassResolver.resolveSilent("Packet");
    static final Class<?> ServerConnection = nmsClassResolver.resolveSilent("ServerConnection");
    static final Class<?> MinecraftServer = nmsClassResolver.resolveSilent("MinecraftServer");
    protected static final FieldResolver entityPlayerFieldResolver = new FieldResolver(EntityPlayer);
    protected static final FieldResolver playerConnectionFieldResolver = new FieldResolver(PlayerConnection);
    protected static final FieldResolver networkManagerFieldResolver = new FieldResolver(NetworkManager);
    protected static final FieldResolver minecraftServerFieldResolver = new FieldResolver(MinecraftServer);
    protected static final FieldResolver serverConnectionFieldResolver = new FieldResolver(ServerConnection);
    static final Field networkManager = playerConnectionFieldResolver.resolveSilent("networkManager");
    static final Field playerConnection = entityPlayerFieldResolver.resolveSilent("playerConnection");
    static final Field serverConnection = minecraftServerFieldResolver.resolveByFirstTypeSilent(ServerConnection);
    static final Field connectionList = serverConnectionFieldResolver.resolveByLastTypeSilent(List.class);
    protected static final MethodResolver craftServerFieldResolver = new MethodResolver(Bukkit.getServer().getClass());
    static final Method getServer = craftServerFieldResolver.resolveSilent("getServer");
    final Executor addChannelExecutor = Executors.newSingleThreadExecutor();
    final Executor removeChannelExecutor = Executors.newSingleThreadExecutor();
    static final String KEY_HANDLER = "packet_handler";
    static final String KEY_PLAYER = "packet_listener_player";
    static final String KEY_SERVER = "packet_listener_server";
    private final IPacketListener iPacketListener;

    public ChannelAbstract(IPacketListener iPacketListener) {
        this.iPacketListener = iPacketListener;
    }

    public abstract void addChannel(Player var1);

    public abstract void removeChannel(Player var1);

    public void addServerChannel() {
        try {
            Object dedicatedServer = getServer.invoke(Bukkit.getServer(), new Object[0]);
            if (dedicatedServer == null) {
                return;
            }
            Object serverConnection = ChannelAbstract.serverConnection.get(dedicatedServer);
            if (serverConnection == null) {
                return;
            }
            List currentList = (List)connectionList.get(serverConnection);
            Field superListField = AccessUtil.setAccessible(currentList.getClass().getSuperclass().getDeclaredField("list"));
            Object list = superListField.get(currentList);
            if (IListenerList.class.isAssignableFrom(list.getClass())) {
                return;
            }
            List newList = Collections.synchronizedList(this.newListenerList());
            for (Object o2 : currentList) {
                newList.add(o2);
            }
            connectionList.set(serverConnection, newList);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public abstract IListenerList newListenerList();

    protected final Object onPacketSend(Object receiver, Object packet, Cancellable cancellable) {
        return this.iPacketListener.onPacketSend(receiver, packet, cancellable);
    }

    protected final Object onPacketReceive(Object sender, Object packet, Cancellable cancellable) {
        return this.iPacketListener.onPacketReceive(sender, packet, cancellable);
    }

    static interface IChannelHandler {
    }

    static interface IChannelWrapper {
    }

    static interface IListenerList<E>
    extends List<E> {
    }
}

