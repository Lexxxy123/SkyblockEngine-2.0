/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload
 *  net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot
 *  net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package net.hypixel.skyblock.listener;

import java.util.ArrayList;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.disguise.PlayerDisguise;
import net.hypixel.skyblock.command.RebootServerCommand;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.module.ConfigModule;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import net.hypixel.skyblock.nms.packetevents.PacketReceiveServerSideEvent;
import net.hypixel.skyblock.nms.packetevents.PacketSentServerSideEvent;
import net.hypixel.skyblock.nms.packetevents.SkySimServerPingEvent;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PacketListener
extends PListener {
    @EventHandler
    public void onBookCrashPacket(PacketReceiveServerSideEvent event) {
        Player p;
        String packetType;
        ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInCustomPayload && ((packetType = ((PacketPlayInCustomPayload)packet.getPacket()).a()).toLowerCase().contains("bedit") || packetType.toLowerCase().contains("bsign"))) {
            packet.setCancelled(true);
            p = SkyBlock.findPlayerByIPAddress(packet.getChannel().getRemoteAddress().toString());
            if (null != p) {
                this.punish(p);
            }
        }
        if (packet.getPacket() instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot pks = (PacketPlayInSetCreativeSlot)packet.getPacket();
            p = packet.getPlayer();
            if (null != p && GameMode.CREATIVE != p.getGameMode()) {
                packet.setCancelled(true);
                this.punish(p);
            }
        }
    }

    void punish(Player p) {
        p.sendMessage(Sputnik.trans("&cHey kiddo, you want to crash the server huh? Nice try idiot, your IP address has been logged, enjoy!"));
    }

    @EventHandler
    public void signInputListener(PacketReceiveServerSideEvent event) {
        ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInUpdateSign) {
            if (null == packet.getPlayer()) {
                return;
            }
            UUID p = packet.getPlayer().getUniqueId();
            IChatBaseComponent[] ic = ((PacketPlayInUpdateSign)packet.getPacket()).b();
            User u = User.getUser(p);
            if (null != u && u.isWaitingForSign() && !u.isCompletedSign()) {
                u.setSignContent(ic[0].getText());
                u.setCompletedSign(true);
            }
        }
    }

    @EventHandler
    void onPing(SkySimServerPingEvent e) {
        PingReply pr = e.getPingReply();
        if (Bukkit.getServer().hasWhitelist()) {
            pr.setProtocolName(ChatColor.RED + "Maintenance");
            ArrayList<String> sample = new ArrayList<String>();
            pr.setMOTD(Sputnik.trans("             &aHypixel Network &c[1.8-1.20]&r\n       &c&lSERVER UNDER MAINTENANCE"));
            sample.add(Sputnik.trans("&bJoin our &9Discord &bserver for more info"));
            sample.add(Sputnik.trans("&6https://discord.hypixel"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
            return;
        }
        if (!RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
            ArrayList<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&cPowered by &6SkyBlock Engine&c"));
            pr.setPlayerSample(sample);
            pr.setProtocolName(ChatColor.DARK_RED + "SkyBlockEngine 1.8.x - 1.20");
        } else {
            pr.setProtocolName(ChatColor.RED + "\u26a0 Restarting Soon!");
            ArrayList<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&4\u26a0 &cThis server instance is performing a"));
            sample.add(Sputnik.trans("&cscheduled or a server update reboot"));
            sample.add(Sputnik.trans("&cWe do not suggest joining the server right"));
            sample.add(Sputnik.trans("&cnow, please wait until the reboot is complete!"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
        }
        pr.setMOTD(Sputnik.trans(ConfigModule.getServerInfo().getString("motd", "             &aHypixel Network &c[1.8-1.20]&r\n  &c&lDungeon & Enderman Slayer! &8\u279c &a&lNOW LIVE!")));
        pr.setMaxPlayers(50);
    }

    @EventHandler
    public void onPacketIn(PacketReceiveServerSideEvent event) {
        PlayerDisguise.packetInManager(event.getPacket());
    }

    @EventHandler
    public void onPacketOut(PacketSentServerSideEvent event) {
        PlayerDisguise.packetOutManager(event.getPacket());
    }
}

