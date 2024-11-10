/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.IPacketListener;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.channel.ChannelAbstract;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.ClassResolver;
import net.hypixel.skyblock.nms.nmsutil.reflection.resolver.ConstructorResolver;
import org.bukkit.entity.Player;

public class ChannelInjector {
    private static final ClassResolver CLASS_RESOLVER = new ClassResolver();
    private ChannelAbstract channel;

    public boolean inject(IPacketListener iPacketListener) {
        ArrayList<Exception> exceptions = new ArrayList<Exception>();
        try {
            Class.forName("io.netty.channel.Channel");
            this.channel = this.newChannelInstance(iPacketListener, "net.hypixel.skyblock.nms.nmsutil.packetlistener.channel.INCChannel");
            System.out.println("[SkyBlock Protocol Injector] Using INChannel");
            return true;
        } catch (Exception e1) {
            exceptions.add(e1);
            for (Exception e2 : exceptions) {
                e2.printStackTrace();
            }
            return false;
        }
    }

    protected ChannelAbstract newChannelInstance(IPacketListener iPacketListener, String clazzName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (ChannelAbstract)new ConstructorResolver(CLASS_RESOLVER.resolve(clazzName)).resolve(new Class[][]{{IPacketListener.class}}).newInstance(iPacketListener);
    }

    public void addChannel(Player p) {
        this.channel.addChannel(p);
    }

    public void removeChannel(Player p) {
        this.channel.removeChannel(p);
    }

    public void addServerChannel() {
        this.channel.addServerChannel();
    }
}

