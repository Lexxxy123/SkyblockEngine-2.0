/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.util.CachedServerIcon
 */
package net.hypixel.skyblock.nms.pingrep;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

public class PingReply {
    private final Object ctx;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    private int protocolVersion;
    private String protocolName;
    private List<String> playerSample;
    private boolean hidePlayerSample = false;
    private CachedServerIcon icon = Bukkit.getServerIcon();

    public PingReply(Object ctx, String motd, int onlinePlayers, int maxPlayers, int protocolVersion, String protocolName, List<String> playerSample) {
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

    public void setMOTD(String motd) {
        this.motd = motd;
    }

    public void hidePlayerSample(boolean hidePlayerSample) {
        this.hidePlayerSample = hidePlayerSample;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setPlayerSample(List<String> playerSample) {
        this.playerSample = playerSample;
    }

    public List<String> getPlayerSample() {
        return this.playerSample;
    }

    public void setIcon(CachedServerIcon icon) {
        this.icon = icon;
    }

    public CachedServerIcon getIcon() {
        return this.icon;
    }
}

