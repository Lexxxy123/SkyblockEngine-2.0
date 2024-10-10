package net.hypixel.skyblock.nms.pingrep;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import java.util.List;

public class PingReply {
    private final Object ctx;
    private String motd;
    @Setter
    @Getter
    private int onlinePlayers;
    @Setter
    @Getter
    private int maxPlayers;
    @Setter
    @Getter
    private int protocolVersion;
    @Setter
    @Getter
    private String protocolName;
    @Setter
    @Getter
    private List<String> playerSample;
    private boolean hidePlayerSample;
    @Setter
    @Getter
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

    public String getMOTD() {
        return this.motd;
    }

    public boolean isPlayerSampleHidden() {
        return this.hidePlayerSample;
    }

    public void setMOTD(final String motd) {
        this.motd = motd;
    }

    public void hidePlayerSample(final boolean hidePlayerSample) {
        this.hidePlayerSample = hidePlayerSample;
    }

}
