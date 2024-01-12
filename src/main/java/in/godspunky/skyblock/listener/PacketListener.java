package in.godspunky.skyblock.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import in.godspunky.skyblock.Skyblock;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.command.RebootServerCommand;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import in.godspunky.skyblock.nms.packetevents.PacketReceiveServerSideEvent;
import in.godspunky.skyblock.nms.packetevents.PluginMessageReceived;
import in.godspunky.skyblock.nms.packetevents.SkySimServerPingEvent;
import in.godspunky.skyblock.nms.packetevents.WrappedPluginMessage;
import in.godspunky.skyblock.nms.pingrep.PingReply;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.DiscordWebhook;
import in.godspunky.skyblock.util.SLog;
import in.godspunky.skyblock.util.Sputnik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketListener extends PListener {
    @EventHandler
    public void onBookCrashPacket(final PacketReceiveServerSideEvent event) {
        final ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInCustomPayload) {
            final String packetType = ((PacketPlayInCustomPayload) packet.getPacket()).a();
            if (packetType.toLowerCase().contains("bedit") || packetType.toLowerCase().contains("bsign")) {
                packet.setCancelled(true);
                final Player p = Skyblock.findPlayerByIPAddress(packet.getChannel().getRemoteAddress().toString());
                if (p != null) {
                    this.punish(p);
                }
            }
        }
        if (packet.getPacket() instanceof PacketPlayInSetCreativeSlot) {
            final PacketPlayInSetCreativeSlot pks = (PacketPlayInSetCreativeSlot) packet.getPacket();
            final Player p = packet.getPlayer();
            if (p != null && p.getGameMode() != GameMode.CREATIVE) {
                packet.setCancelled(true);
                this.punish(p);
            }
        }
    }

    void punish(final Player p) {
        p.sendMessage(Sputnik.trans("&cHey kiddo, you want to crash the server huh? Nice try idiot, your IP address has been logged, enjoy!"));
        new BukkitRunnable() {
            public void run() {
                final DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/935193761795940404/3IdoSzkoXBU8UQb-X_mfizQgXYZZYiQ61FH9KPgm-gaeuUGjfhoTKvaWUFiQjwh55jKN");
                webhook.setUsername("SkySim Packet Assistant [v0.2.1-BETA]");
                webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/884749251568082964/935357971368656916/AAAAA.png");
                webhook.setContent("**We caught a crasher!** His/her IP is `" + p.getAddress() + "` with username `" + p.getName() + "`");
                try {
                    webhook.execute();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Skyblock.getPlugin());
    }

    @EventHandler
    public void signInputListener(final PacketReceiveServerSideEvent event) {
        final ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInUpdateSign) {
            if (packet.getPlayer() == null) {
                return;
            }
            final UUID p = packet.getPlayer().getUniqueId();
            final IChatBaseComponent[] ic = ((PacketPlayInUpdateSign) packet.getPacket()).b();
            final User u = User.getUser(p);
            if (u != null && u.isWaitingForSign() && !u.isCompletedSign()) {
                u.setSignContent(ic[0].getText());
                u.setCompletedSign(true);
            }
        }
    }

    @EventHandler
    void onPing(final SkySimServerPingEvent e) {
        final PingReply pr = e.getPingReply();
        if (!Bukkit.getServer().getOnlineMode() && Bukkit.getOnlinePlayers().size() > 0) {
            Skyblock.getPlugin().updateServerPlayerCount();
        }
        if (Bukkit.getServer().hasWhitelist()) {
            pr.setProtocolName(ChatColor.RED + "Maintenance");
            final List<String> sample = new ArrayList<String>();
            pr.setMOTD(Sputnik.trans("             &aSkySim Network &c[1.8-1.20]&r\n       &c&lSERVER UNDER MAINTENANCE"));
            sample.add(Sputnik.trans("&bJoin our &9Discord &bserver for more info"));
            sample.add(Sputnik.trans("&6https://discord.skysim.sbs/"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
            return;
        }
        if (!RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
            final List<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&cPowered by &6Skyblock Engine&c"));
            pr.setPlayerSample(sample);
            pr.setProtocolName(ChatColor.DARK_RED + "Skyblock 1.8.x - 1.20");
        } else {
            pr.setProtocolName(ChatColor.RED + "⚠ Restarting Soon!");
            final List<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&4⚠ &cThis server instance is performing a"));
            sample.add(Sputnik.trans("&cscheduled or a server update reboot"));
            sample.add(Sputnik.trans("&cWe do not suggest joining the server right"));
            sample.add(Sputnik.trans("&cnow, please wait until the reboot is complete!"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
        }
        pr.setMOTD(Sputnik.trans("             &aGodspunky Network &c[1.8-1.20]&r\n  &c&lDIMOON & GIANTS ISLAND! &8➜ &a&lNOW LIVE!"));
        pr.setMaxPlayers(50);
    }

    @EventHandler
    public void onPluginChannel2(final PluginMessageReceived e) {
        final WrappedPluginMessage w = e.getWrappedPluginMessage();
        final String channel = w.getChannelName();
        final byte[] message = w.getMessage();
        if (!channel.equals("BungeeCord")) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            in.readInt();
        }
    }

    @EventHandler
    public void onPluginChannel(final PluginMessageReceived e) {
        final WrappedPluginMessage w = e.getWrappedPluginMessage();
        final String channel = w.getChannelName();
        final byte[] message = w.getMessage();
        if (!channel.equals("BungeeCord")) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();
        if (subchannel.equals("GetServer")) {
            final String name = in.readUTF();
            if (Skyblock.getPlugin().getServerName().contains("Loading...")) {
                SLog.info("Registered server instance name as " + name + " for this session!");
            }
            Skyblock.getPlugin().setServerName(name);
        }
    }
}
