package net.hypixel.skyblock.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.hypixel.skyblock.api.disguise.PlayerDisguise;
import net.hypixel.skyblock.api.reflection.ReflectionsUtils;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.hypixel.skyblock.module.ConfigModule;
import net.hypixel.skyblock.nms.packetevents.*;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.RebootServerCommand;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.DiscordWebhook;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.Sputnik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketListener extends PListener {
    @EventHandler
    public void onBookCrashPacket(PacketReceiveServerSideEvent event) {
        ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInCustomPayload) {
            String packetType = ((PacketPlayInCustomPayload) packet.getPacket()).a();
            if (packetType.toLowerCase().contains("bedit") || packetType.toLowerCase().contains("bsign")) {
                packet.setCancelled(true);
                Player p = SkyBlock.findPlayerByIPAddress(packet.getChannel().getRemoteAddress().toString());
                if (null != p) {
                    this.punish(p);
                }
            }
        }
        if (packet.getPacket() instanceof PacketPlayInSetCreativeSlot) {
            PacketPlayInSetCreativeSlot pks = (PacketPlayInSetCreativeSlot) packet.getPacket();
            Player p = packet.getPlayer();
            if (null != p && GameMode.CREATIVE != p.getGameMode()) {
                packet.setCancelled(true);
                this.punish(p);
            }
        }
    }

    void punish(Player p) {
        p.sendMessage(Sputnik.trans("&cHey kiddo, you want to crash the server huh? Nice try idiot, your IP address has been logged, enjoy!"));
//        new BukkitRunnable() {
//            public void run() {
//                DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/935193761795940404/3IdoSzkoXBU8UQb-X_mfizQgXYZZYiQ61FH9KPgm-gaeuUGjfhoTKvaWUFiQjwh55jKN");
//                webhook.setUsername("GodSpunky Packet Assistant [v0.2.1-BETA]");
//                webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/884749251568082964/935357971368656916/AAAAA.png");
//                webhook.setContent("**We caught a crasher!** His/her IP is `" + p.getAddress() + "` with username `" + p.getName() + "`");
//                try {
//                    webhook.execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.runTaskAsynchronously(SkyBlock.getPlugin());
    }

    @EventHandler
    public void signInputListener(PacketReceiveServerSideEvent event) {
        ReceivedPacket packet = event.getWrappedPacket();
        if (packet.getPacket() instanceof PacketPlayInUpdateSign) {
            if (null == packet.getPlayer()) {
                return;
            }
            UUID p = packet.getPlayer().getUniqueId();
            IChatBaseComponent[] ic = ((PacketPlayInUpdateSign) packet.getPacket()).b();
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
            List<String> sample = new ArrayList<String>();
            pr.setMOTD(Sputnik.trans("             &aHypixel Network &c[1.8-1.20]&r\n       &c&lSERVER UNDER MAINTENANCE"));
            sample.add(Sputnik.trans("&bJoin our &9Discord &bserver for more info"));
            sample.add(Sputnik.trans("&6https://discord.hypixel"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
            return;
        }
        if (!RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
            List<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&cPowered by &6SkyBlock Engine&c"));
            pr.setPlayerSample(sample);
            pr.setProtocolName(ChatColor.DARK_RED + "SkyBlockEngine 1.8.x - 1.20");
        } else {
            pr.setProtocolName(ChatColor.RED + "⚠ Restarting Soon!");
            List<String> sample = new ArrayList<String>();
            sample.add(Sputnik.trans("&4⚠ &cThis server instance is performing a"));
            sample.add(Sputnik.trans("&cscheduled or a server update reboot"));
            sample.add(Sputnik.trans("&cWe do not suggest joining the server right"));
            sample.add(Sputnik.trans("&cnow, please wait until the reboot is complete!"));
            pr.setPlayerSample(sample);
            pr.setProtocolVersion(-1);
        }
        pr.setMOTD(Sputnik.trans(ConfigModule.getServerInfo().getString("motd" , "             &aHypixel Network &c[1.8-1.20]&r\n  &c&lDungeon & Enderman Slayer! &8➜ &a&lNOW LIVE!" )));
        pr.setMaxPlayers(50);
    }


    @EventHandler
    public void onPacketIn(PacketReceiveServerSideEvent event){
//        if (event.getPacket().getClass().getSimpleName().equals("PacketPlayInUseEntity")) {
//            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) event.getPacket();
//            int id = Integer.parseInt(String.valueOf(ReflectionsUtils.getValue(packetPlayInUseEntity, "a")));
//            if (ReflectionsUtils.getValue(packetPlayInUseEntity, "action").toString().equalsIgnoreCase("interact")) {
//                Player player;
//                if (event.getWrappedPacket().getPlayer() != null && event.getWrappedPacket().getPlayer().getUniqueId() != null){
//                  player = Bukkit.getPlayer(event.getWrappedPacket().getPlayer().getUniqueId());
//                } else {
//                    player = null;
//                }
//
//                SkyblockNPCManager.getNPCS().forEach(skyblockNPC -> {
//                    if (skyblockNPC.getEntityID() == id) {
//                        SkyblockPlayerNPCClickEvent npcClickEvent = new SkyblockPlayerNPCClickEvent(player, skyblockNPC);
//                        Bukkit.getPluginManager().callEvent(npcClickEvent);
//
//                        skyblockNPC.getParameters().onInteract(player, skyblockNPC);
//                    }
//                });
//            }
//        }



        PlayerDisguise.packetInManager(event.getPacket());
    }

    @EventHandler
    public void onPacketOut(PacketSentServerSideEvent event){
        PlayerDisguise.packetOutManager(event.getPacket());
    }





}
