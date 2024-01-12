package in.godspunky.skyblock.nms.pingrep;

import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import java.util.List;

public class PingReply {
    private final Object ctx;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    private int protocolVersion;
    private String protocolName;
    private List<String> playerSample;
    private boolean hidePlayerSample;
    private CachedServerIcon icon;

    public PingReply(final Object ctx, final String motd, final int onlinePlayers, final int maxPlayers, final int protocolVersion, final String protocolName, final List<String> playerSample) {
        this.hidePlayerSample = false;
        this.icon = Bukkit.getServerIcon();
        this.ctx = ctx;
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.protocolVersion = protocolVersion;
        this.protocolName = protocolName;
        this.playerSample = playerSample;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public void setOnlinePlayers(final int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMOTD() {
        return this.motd;
    }

    public void setMOTD(final String motd) {
        this.motd = motd;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(final int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setProtocolName(final String protocolName) {
        this.protocolName = protocolName;
    }

    public List<String> getPlayerSample() {
        return this.playerSample;
    }

    public void setPlayerSample(final List<String> playerSample) {
        this.playerSample = playerSample;
    }

    public boolean isPlayerSampleHidden() {
        return this.hidePlayerSample;
    }

    public CachedServerIcon getIcon() {
        return this.icon;
    }

    public void setIcon(final CachedServerIcon icon) {
        this.icon = icon;
    }

    public void hidePlayerSample(final boolean hidePlayerSample) {
        this.hidePlayerSample = hidePlayerSample;
    }
}
