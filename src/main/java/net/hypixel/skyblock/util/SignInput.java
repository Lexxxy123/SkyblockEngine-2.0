/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_8_R3.BlockPosition
 *  net.minecraft.server.v1_8_R3.Blocks
 *  net.minecraft.server.v1_8_R3.ChatComponentText
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange
 *  net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor
 *  net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.GUI;
import net.hypixel.skyblock.gui.GUISignItem;
import net.hypixel.skyblock.gui.TradeMenu;
import net.hypixel.skyblock.user.User;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateSign;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SignInput {
    private final Player player;
    private Location signLoc;
    private final int timeoutSec;
    private final UUID tradeUUID;
    private final User user;
    public static final Map<UUID, GUISignItem> SIGN_INPUT_QUERY = new HashMap<UUID, GUISignItem>();

    public SignInput(Player p2, String[] text, int timeoutSec, UUID tradeUUID) {
        this.player = p2;
        this.timeoutSec = timeoutSec;
        this.tradeUUID = tradeUUID;
        this.user = User.getUser(p2.getUniqueId());
        this.openSign(text);
    }

    public void openSign(String[] strings) {
        this.signLoc = this.player.getLocation();
        this.signLoc.setY(1.0);
        BlockPosition p2 = new BlockPosition(this.signLoc.getBlockX(), this.signLoc.getBlockY(), this.signLoc.getBlockZ());
        PacketPlayOutBlockChange blockPacket = new PacketPlayOutBlockChange((World)((CraftWorld)this.signLoc.getWorld()).getHandle(), p2);
        blockPacket.block = Blocks.STANDING_SIGN.getBlockData();
        IChatBaseComponent[] lines = new IChatBaseComponent[4];
        for (int i2 = 0; i2 < 4; ++i2) {
            lines[i2] = new ChatComponentText(strings[i2]);
        }
        PacketPlayOutUpdateSign sign = new PacketPlayOutUpdateSign((World)((CraftWorld)this.signLoc.getWorld()).getHandle(), p2, lines);
        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(p2);
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)blockPacket);
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)sign);
        ((CraftPlayer)this.player).getHandle().playerConnection.sendPacket((Packet)packet);
        this.user.setWaitingForSign(true);
        this.user.setCompletedSign(false);
        this.user.setSignContent(null);
        new BukkitRunnable(){
            int i = 0;

            public void run() {
                ++this.i;
                if (TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                    this.cancel();
                    SignInput.this.signLoc.getBlock().getState().update();
                    SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
                    SignInput.this.player.closeInventory();
                    SignInput.this.user.setWaitingForSign(false);
                    SignInput.this.user.setSignContent(null);
                    SignInput.this.user.setCompletedSign(false);
                    return;
                }
                if (SignInput.this.user.isWaitingForSign() && SignInput.this.user.isCompletedSign()) {
                    this.cancel();
                    SignInput.this.signLoc.getBlock().getState().update();
                    GUI gui = SIGN_INPUT_QUERY.get(SignInput.this.player.getUniqueId()).onSignClose(SignInput.this.user.getSignContent(), SignInput.this.user.toBukkitPlayer());
                    if (gui != null && !TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                        gui.open(SignInput.this.player);
                    }
                    SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
                    SignInput.this.user.setWaitingForSign(false);
                    SignInput.this.user.setSignContent(null);
                    SignInput.this.user.setCompletedSign(false);
                    return;
                }
                if (!SignInput.this.player.isOnline() || this.i >= SignInput.this.timeoutSec * 20) {
                    SignInput.this.player.playSound(SignInput.this.player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                    SignInput.this.player.sendMessage(ChatColor.RED + "Timeout exceeded! You only have " + SignInput.this.timeoutSec + "s to type in the input!");
                    this.cancel();
                    SignInput.this.signLoc.getBlock().getState().update();
                    GUI gui = SIGN_INPUT_QUERY.get(SignInput.this.player.getUniqueId()).onSignClose("$canc", SignInput.this.user.toBukkitPlayer());
                    if (gui != null && !TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                        gui.open(SignInput.this.player);
                    }
                    SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
                    SignInput.this.user.setWaitingForSign(false);
                    SignInput.this.user.setSignContent(null);
                    SignInput.this.user.setCompletedSign(false);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }
}

