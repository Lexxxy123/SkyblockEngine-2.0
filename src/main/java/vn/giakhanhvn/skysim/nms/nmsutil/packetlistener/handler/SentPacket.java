package vn.giakhanhvn.skysim.nms.nmsutil.packetlistener.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import vn.giakhanhvn.skysim.nms.nmsutil.packetlistener.channel.ChannelWrapper;

public class SentPacket extends PacketAbstract {
    public SentPacket(final Object packet, final Cancellable cancellable, final Player player) {
        super(packet, cancellable, player);
    }

    public SentPacket(final Object packet, final Cancellable cancellable, final ChannelWrapper channelWrapper) {
        super(packet, cancellable, channelWrapper);
    }
}
