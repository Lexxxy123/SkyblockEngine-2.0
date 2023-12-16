package vn.giakhanhvn.skysim.util;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.gui.GUI;
import vn.giakhanhvn.skysim.gui.GUISignItem;
import vn.giakhanhvn.skysim.gui.TradeMenu;
import vn.giakhanhvn.skysim.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignInput {
    private final Player player;
    private Location signLoc;
    private final int timeoutSec;
    private final UUID tradeUUID;
    private final User user;
    public static final Map<UUID, GUISignItem> SIGN_INPUT_QUERY;

    public SignInput(final Player p, final String[] text, final int timeoutSec, final UUID tradeUUID) {
        this.player = p;
        this.timeoutSec = timeoutSec;
        this.tradeUUID = tradeUUID;
        this.user = User.getUser(p.getUniqueId());
        this.openSign(text);
    }

    public void openSign(final String[] strings) {
        (this.signLoc = this.player.getLocation()).setY(1.0);
        final BlockPosition p = new BlockPosition(this.signLoc.getBlockX(), this.signLoc.getBlockY(), this.signLoc.getBlockZ());
        final PacketPlayOutBlockChange blockPacket = new PacketPlayOutBlockChange(((CraftWorld) this.signLoc.getWorld()).getHandle(), p);
        blockPacket.block = Blocks.STANDING_SIGN.getBlockData();
        final IChatBaseComponent[] lines = new IChatBaseComponent[4];
        for (int i = 0; i < 4; ++i) {
            lines[i] = new ChatComponentText(strings[i]);
        }
        final PacketPlayOutUpdateSign sign = new PacketPlayOutUpdateSign(((CraftWorld) this.signLoc.getWorld()).getHandle(), p, lines);
        final PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(p);
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(blockPacket);
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(sign);
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
        this.user.setWaitingForSign(true);
        this.user.setCompletedSign(false);
        this.user.setSignContent(null);
        new BukkitRunnable() {
            int i = 0;

            public void run() {
                ++this.i;
                if (TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                    this.cancel();
                    SignInput.this.signLoc.getBlock().getState().update();
                    SignInput.SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
                    SignInput.this.player.closeInventory();
                    SignInput.this.user.setWaitingForSign(false);
                    SignInput.this.user.setSignContent(null);
                    SignInput.this.user.setCompletedSign(false);
                    return;
                }
                if (SignInput.this.user.isWaitingForSign() && SignInput.this.user.isCompletedSign()) {
                    this.cancel();
                    SignInput.this.signLoc.getBlock().getState().update();
                    final GUI gui = SignInput.SIGN_INPUT_QUERY.get(SignInput.this.player.getUniqueId()).onSignClose(SignInput.this.user.getSignContent(), SignInput.this.user.toBukkitPlayer());
                    if (gui != null && !TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                        gui.open(SignInput.this.player);
                    }
                    SignInput.SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
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
                    final GUI gui = SignInput.SIGN_INPUT_QUERY.get(SignInput.this.player.getUniqueId()).onSignClose("$canc", SignInput.this.user.toBukkitPlayer());
                    if (gui != null && !TradeMenu.tradeClose.containsKey(SignInput.this.tradeUUID)) {
                        gui.open(SignInput.this.player);
                    }
                    SignInput.SIGN_INPUT_QUERY.remove(SignInput.this.player.getUniqueId());
                    SignInput.this.user.setWaitingForSign(false);
                    SignInput.this.user.setSignContent(null);
                    SignInput.this.user.setCompletedSign(false);
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    static {
        SIGN_INPUT_QUERY = new HashMap<UUID, GUISignItem>();
    }
}
