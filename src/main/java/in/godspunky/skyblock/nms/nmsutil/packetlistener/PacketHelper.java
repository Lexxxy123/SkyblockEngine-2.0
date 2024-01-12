package in.godspunky.skyblock.nms.nmsutil.packetlistener;

import in.godspunky.skyblock.nms.nmsutil.apihelper.API;
import in.godspunky.skyblock.nms.nmsutil.apihelper.APIManager;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.channel.ChannelWrapper;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.PacketHandler;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import in.godspunky.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PacketHelper implements IPacketListener, Listener, API {
    public boolean injected;
    private ChannelInjector channelInjector;

    public PacketHelper() {
        this.injected = false;
    }

    public static boolean addPacketHandler(final PacketHandler handler) {
        return PacketHandler.addHandler(handler);
    }

    public static boolean removePacketHandler(final PacketHandler handler) {
        return PacketHandler.removeHandler(handler);
    }

    public void load() {
        this.channelInjector = new ChannelInjector();
        final boolean inject = this.channelInjector.inject(this);
        this.injected = inject;
        if (inject) {
            this.channelInjector.addServerChannel();
            SLog.info("Injected custom channel handlers.");
        } else {
            SLog.severe("Failed to inject channel handlers");
        }
    }

    public void init(final Plugin plugin) {
        APIManager.registerEvents(this, this);
        SLog.info("Adding channels for online players...");
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.channelInjector.addChannel(player);
        }
    }

    public void disable(final Plugin plugin) {
        if (!this.injected) {
            return;
        }
        SLog.info("Removing channels for online players...");
        for (final Player player : Bukkit.getOnlinePlayers()) {
            this.channelInjector.removeChannel(player);
        }
        SLog.info("Removing packet handlers (" + PacketHandler.getHandlers().size() + ")...");
        while (!PacketHandler.getHandlers().isEmpty()) {
            PacketHandler.removeHandler(PacketHandler.getHandlers().get(0));
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        this.channelInjector.addChannel(e.getPlayer());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        this.channelInjector.removeChannel(e.getPlayer());
    }

    @Override
    public Object onPacketReceive(final Object sender, final Object packet, final Cancellable cancellable) {
        ReceivedPacket receivedPacket;
        if (sender instanceof Player) {
            receivedPacket = new ReceivedPacket(packet, cancellable, (Player) sender);
        } else {
            receivedPacket = new ReceivedPacket(packet, cancellable, (ChannelWrapper) sender);
        }
        PacketHandler.notifyHandlers(receivedPacket);
        if (receivedPacket.getPacket() != null) {
            return receivedPacket.getPacket();
        }
        return packet;
    }

    @Override
    public Object onPacketSend(final Object receiver, final Object packet, final Cancellable cancellable) {
        SentPacket sentPacket;
        if (receiver instanceof Player) {
            sentPacket = new SentPacket(packet, cancellable, (Player) receiver);
        } else {
            sentPacket = new SentPacket(packet, cancellable, (ChannelWrapper) receiver);
        }
        PacketHandler.notifyHandlers(sentPacket);
        if (sentPacket.getPacket() != null) {
            return sentPacket.getPacket();
        }
        return packet;
    }
}
