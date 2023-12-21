package in.godspunky.skyblock.dungeons;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BlessingChest {
    public static final Map<Block, BlessingChest> CHEST_CACHE;
    private boolean opened;
    private boolean locked;
    private final Blessings type;
    private final byte state;
    private final Block chest;
    private final SkySimEngine sse;

    public BlessingChest(final Blessings type, final Block chest, final byte state) {
        this.sse = SkySimEngine.getPlugin();
        this.type = type;
        this.state = state;
        this.locked = false;
        this.opened = false;
        this.chest = chest;
        BlessingChest.CHEST_CACHE.put(chest, this);
        new BukkitRunnable() {
            public void run() {
                if (!BlessingChest.CHEST_CACHE.containsKey(chest)) {
                    this.cancel();
                    return;
                }
                final Collection<Entity> ce = chest.getWorld().getNearbyEntities(chest.getLocation(), 10.0, 10.0, 10.0);
                ce.removeIf(entity -> !(entity instanceof Player));
                if (ce.size() > 0) {
                    BlessingChest.this.show();
                } else {
                    BlessingChest.this.hide();
                }
            }
        }.runTaskTimer(this.sse, 0L, 1L);
    }

    public void open(final Player opener) {
        if (!this.opened && !this.locked) {
            Blessings.openBlessingChest(this.chest, this.type, opener);
            this.opened = true;
            return;
        }
        if (this.locked) {
            opener.sendMessage(Sputnik.trans("&cThat chest is locked!"));
            return;
        }
        if (this.opened) {
            opener.sendMessage(Sputnik.trans("&cThe chest has already been searched!"));
        }
    }

    public void destroy() {
        this.chest.setType(Material.AIR);
        BlessingChest.CHEST_CACHE.remove(this.chest);
    }

    public void hide() {
        this.chest.getLocation().getBlock().setType(Material.AIR);
    }

    public void show() {
        if (this.chest.getType() != Material.CHEST) {
            this.chest.getLocation().getBlock().setType(Material.CHEST);
            this.chest.setData(this.state);
            Location chestLocation = this.chest.getLocation();
            if (this.isOpened()) {
                SUtil.delay(() -> {
                    // todo : fix it
                    BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ());
                    PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 1);
                    for (Player p : chestLocation.getWorld().getPlayers()) {
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                    }
                }, 1L);
            }
        }
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(final boolean opened) {
        this.opened = opened;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    public Blessings getType() {
        return this.type;
    }

    static {
        CHEST_CACHE = new HashMap<Block, BlessingChest>();
    }
}
