/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.BlockPosition
 *  net.minecraft.server.v1_8_R3.Blocks
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.features.dungeons.chest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.gui.DungeonsLootGUI;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemChest {
    public static final Map<org.bukkit.block.Block, ItemChest> ITEM_CHEST_CACHE = new HashMap<org.bukkit.block.Block, ItemChest>();
    private boolean opened;
    private boolean locked;
    private final ItemStack type;
    private final byte state;
    private final org.bukkit.block.Block chest;
    private final SkyBlock sse = SkyBlock.getPlugin();

    public ItemChest(ItemStack type, final org.bukkit.block.Block chest, byte state) {
        this.type = type;
        this.state = state;
        this.locked = false;
        this.opened = false;
        this.chest = chest;
        ITEM_CHEST_CACHE.put(chest, this);
        new BukkitRunnable(){

            public void run() {
                if (!ITEM_CHEST_CACHE.containsKey(chest)) {
                    this.cancel();
                    return;
                }
                Collection ce = chest.getWorld().getNearbyEntities(chest.getLocation(), 10.0, 10.0, 10.0);
                ce.removeIf(entity -> !(entity instanceof Player));
                if (ce.size() > 0) {
                    ItemChest.this.show();
                } else {
                    ItemChest.this.hide();
                }
            }
        }.runTaskTimer((Plugin)this.sse, 0L, 1L);
    }

    public void open(Player opener) {
        if (!this.opened && !this.locked) {
            new DungeonsLootGUI(this.type, this.chest).open(opener);
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
        ITEM_CHEST_CACHE.remove(this.chest);
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
                    BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ());
                    PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, (Block)Blocks.CHEST, 1, 1);
                    for (Player p2 : chestLocation.getWorld().getPlayers()) {
                        ((CraftPlayer)p2).getHandle().playerConnection.sendPacket((Packet)packet);
                    }
                }, 1L);
            }
        }
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public ItemStack getType() {
        return this.type;
    }
}

