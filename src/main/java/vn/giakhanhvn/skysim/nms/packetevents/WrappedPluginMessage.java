package vn.giakhanhvn.skysim.nms.packetevents;

import org.bukkit.entity.Player;

public class WrappedPluginMessage
{
    private String channelName;
    private Player player;
    private byte[] message;
    
    public WrappedPluginMessage(final String cn, final Player p, final byte[] msg) {
        this.channelName = cn;
        this.player = p;
        this.message = msg;
    }
    
    public String getChannelName() {
        return this.channelName;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public byte[] getMessage() {
        return this.message;
    }
}
