package in.godspunky.skyblock.nms.nmsutil.packetlistener.handler;

import in.godspunky.skyblock.nms.nmsutil.packetlistener.channel.ChannelWrapper;
import in.godspunky.skyblock.nms.nmsutil.reflection.resolver.FieldResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PacketAbstract {
    private Player player;
    private ChannelWrapper channelWrapper;
    private Object packet;
    private final Cancellable cancellable;
    protected FieldResolver fieldResolver;

    public PacketAbstract(final Object packet, final Cancellable cancellable, final Player player) {
        this.player = player;
        this.packet = packet;
        this.cancellable = cancellable;
        this.fieldResolver = new FieldResolver(packet.getClass());
    }

    public PacketAbstract(final Object packet, final Cancellable cancellable, final ChannelWrapper channelWrapper) {
        this.channelWrapper = channelWrapper;
        this.packet = packet;
        this.cancellable = cancellable;
        this.fieldResolver = new FieldResolver(packet.getClass());
    }

    public void writeField(final String field, final Object value) {
        try {
            this.fieldResolver.resolve(field).set(this.getPacket(), value);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPacketValueSilent(final String field, final Object value) {
        try {
            this.fieldResolver.resolve(field).set(this.getPacket(), value);
        } catch (final Exception ex) {
        }
    }

    public void write(final int index, final Object value) {
        try {
            this.fieldResolver.resolveIndex(index).set(this.getPacket(), value);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPacketValueSilent(final int index, final Object value) {
        try {
            this.fieldResolver.resolveIndex(index).set(this.getPacket(), value);
        } catch (final Exception ex) {
        }
    }

    public Object getPacketValue(final String field) {
        try {
            return this.fieldResolver.resolve(field).get(this.getPacket());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getPacketValueSilent(final String field) {
        try {
            return this.fieldResolver.resolve(field).get(this.getPacket());
        } catch (final Exception ex) {
            return null;
        }
    }

    public Object read(final int index) {
        try {
            return this.fieldResolver.resolveIndex(index).get(this.getPacket());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getPacketValueSilent(final int index) {
        try {
            return this.fieldResolver.resolveIndex(index).get(this.getPacket());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public FieldResolver getFieldResolver() {
        return this.fieldResolver;
    }

    public void setCancelled(final boolean b) {
        this.cancellable.setCancelled(b);
    }

    public boolean isCancelled() {
        return this.cancellable.isCancelled();
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean hasPlayer() {
        return this.player != null;
    }

    public ChannelWrapper<?> getChannel() {
        return this.channelWrapper;
    }

    public boolean hasChannel() {
        return this.channelWrapper != null;
    }

    public String getPlayername() {
        if (!this.hasPlayer()) {
            return null;
        }
        return this.player.getName();
    }

    public void setPacket(final Object packet) {
        this.packet = packet;
    }

    public Object getPacket() {
        return this.packet;
    }

    public Player getPlayerInvolved() {
        Collection<? extends Player> q = Bukkit.getOnlinePlayers();
        ArrayList<Player> t = new ArrayList<Player>(q);
        return t.get(0);
    }

    public String getPacketName() {
        return this.packet.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Packet{ " + (this.getClass().equals(SentPacket.class) ? "[> OUT >]" : "[< IN <]") + " " + this.getPacketName() + " " + (this.hasPlayer() ? this.getPlayername() : (this.hasChannel() ? this.getChannel().channel() : "#server#")) + " }";
    }
}
