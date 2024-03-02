package net.hypixel.skyblock.entity.dimoon;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import net.hypixel.skyblock.SkyBlock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.hypixel.skyblock.entity.dimoon.utils.Utils;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {
    private int currentParkour;
    private final List<Block> parkourBlocks;
    private boolean isCollapsing;
    public int highestY;
    private BukkitTask parkourTask;

    public Arena() {
        this.currentParkour = 1;
        this.parkourBlocks = new ArrayList<Block>();
        this.isCollapsing = false;
        this.highestY = -1;
    }

    public void pasteParkour() throws IOException {
        File parkour = new File("plugins/dimoon/parkours/parkour" + this.currentParkour + ".schematic");
        Dimoon dimoon = SkyBlock.getPlugin().dimoon;
        if (null == dimoon) {
            return;
        }
        for (Player p : dimoon.getEntity().getWorld().getPlayers()) {
            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            p.sendMessage(Utils.format("&c&lALERT! &eParkour level &a" + this.currentParkour + " &espawned in! Try your best, &dyou can do it!!"));
            p.sendMessage(Sputnik.trans("&eHint: &6Reach the &chighest point &6of the parkour to complete it!"));
        }
        Location location = dimoon.getEntity().getLocation();
        World world = new BukkitWorld(location.getWorld());
        Closer closer = Closer.create();
        FileInputStream fis = (FileInputStream) closer.register((Closeable) new FileInputStream(parkour));
        BufferedInputStream bis = (BufferedInputStream) closer.register((Closeable) new BufferedInputStream(fis));
        ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
        Clipboard clipboard = reader.read(world.getWorldData());
        int highestYC = this.highestY;
        Location minYLoc = null;
        int minY = Integer.MAX_VALUE;
        for (int x = 0; x < clipboard.getRegion().getWidth(); ++x) {
            for (int y = 0; y < clipboard.getRegion().getHeight(); ++y) {
                for (int z = 0; z < clipboard.getRegion().getLength(); ++z) {
                    Vector minimumPoint = clipboard.getMinimumPoint();
                    Vector clipboardLoc = new Vector(minimumPoint.getBlockX() + x, minimumPoint.getBlockY() + y, minimumPoint.getBlockZ() + z);
                    BaseBlock baseBlock = clipboard.getBlock(clipboardLoc);
                    if (0 != baseBlock.getId()) {
                        Location newLocation = location.clone().subtract(57.0, 0.0, 57.0).add(x, y, z);
                        Vector loc = new Vector(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        try {
                            world.setBlock(loc, baseBlock);
                            if (y < minY && 165 != baseBlock.getId() && 0 == clipboard.getBlock(clipboardLoc.add(0, 1, 0)).getId() && newLocation.getBlock().getType().isSolid()) {
                                minY = y;
                                minYLoc = newLocation;
                            }
                            this.parkourBlocks.add(location.getWorld().getBlockAt(newLocation));
                            if (1 == this.currentParkour && Material.BARRIER == newLocation.getBlock().getType()) {
                                newLocation.getBlock().setType(Material.AIR);
                            }
                            highestYC = Math.max(highestYC, loc.getBlockY());
                        } catch (WorldEditException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        closer.close();
        List<Block> parkourList = new ArrayList<Block>(this.parkourBlocks);
        this.parkourBlocks.clear();
        for (Block b : parkourList) {
            if (!b.getType().isSolid()) {
                this.parkourBlocks.add(b);
            }
        }
        for (Block b : parkourList) {
            if (b.getType().isSolid()) {
                this.parkourBlocks.add(b);
            }
        }
        if (1 != this.currentParkour) {
            for (Player p2 : dimoon.getEntity().getWorld().getPlayers()) {
                p2.teleport(minYLoc.add(0.0, 1.0, 0.0));
            }
        } else {
            for (Player p2 : dimoon.getEntity().getWorld().getPlayers()) {
                p2.teleport(new Location(dimoon.getEntity().getWorld(), 234673.5, 155.0, 236425.5));
            }
        }
        int hc = highestYC;
        this.currentParkour = this.currentParkour % 3 + 1;
        SUtil.delay(() -> this.highestY = hc, 1L);
        SkyBlock plugin = SkyBlock.getPlugin();
        this.parkourTask = new BukkitRunnable() {
            public void run() {
                Arena.this.collapseParkour();
                Arena.this.collapseFloor();
                for (Player p : SkyBlock.getPlugin().dimoon.getEntity().getWorld().getPlayers()) {
                    p.sendMessage(Utils.format("&cYou couldn't complete the parkour in time, the boss regained 5,000 HP that sucks!"));
                }
                SkyBlock.getPlugin().arena.highestY = -1;
                dimoon.getTasks().add(new BukkitRunnable() {
                    public void run() {
                        try {
                            SkyBlock.getPlugin().arena.pasteParkour();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(SkyBlock.getPlugin(), 1200L));
                if (null != plugin.dimoon) {
                    plugin.dimoon.heal(5000);
                }
            }
        }.runTaskLater(SkyBlock.getPlugin(), 12000L);
    }

    public void collapseParkour() {
        if (this.isCollapsing) {
            return;
        }
        this.isCollapsing = true;
        Iterator<Block> iterator = this.parkourBlocks.iterator();
        SkyBlock plugin = SkyBlock.getPlugin();
        new BukkitRunnable() {
            public void run() {
                if (!iterator.hasNext()) {
                    Arena.this.isCollapsing = false;
                    this.cancel();
                    Arena.this.parkourBlocks.clear();
                    return;
                }
                Block block = iterator.next();
                if (block.getType().isSolid()) {
                    block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.0, 0.5), block.getType(), block.getData());
                    block.getWorld().playSound(block.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 1.0f);
                }
                block.setType(Material.AIR);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 1L, 1L);
    }

    public void collapseFloor() {
        Dimoon dimoon = SkyBlock.getPlugin().dimoon;
        if (null == dimoon) {
        }
    }

    public void spawnDimoonaize(Entity entity) {
        Utils.bossMessage("DIMOONAIZE! GO!");
        List<Location> locList = new ArrayList<Location>();
        Dimoon dimoon = SkyBlock.getPlugin().dimoon;
        if (null == dimoon) {
            return;
        }
        for (int x = 0; 120 > x; ++x) {
            for (int z = 0; 120 > z; ++z) {
                Location location = dimoon.getEntity().getLocation().clone().add(60.0, 0.0, 60.0).subtract(x, 2.0, z);
                Block block = location.getBlock();
                if (Material.BEACON == block.getType()) {
                    locList.add(block.getLocation().add(0.5, 3.0, 0.5));
                }
            }
        }
        int i = 0;
        for (Location loc : locList) {
            ++i;
            SUtil.delay(() -> {
                org.bukkit.World world = entity.getWorld();
                org.bukkit.util.Vector[] velocity = {loc.toVector().subtract(entity.getLocation().toVector()).normalize()};
                new BukkitRunnable() {
                    private final Location particleLocation;
                    final double multiplier;

                    {
                        this.particleLocation = entity.getLocation().add(0.0, 2.0, 0.0).clone();
                        this.multiplier = 1.0;
                    }

                    public void run() {
                        this.particleLocation.add(velocity[0]);
                        velocity[0] = loc.toVector().subtract(this.particleLocation.toVector()).normalize().multiply(this.multiplier);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.LARGE_SMOKE, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        loc.getWorld().playEffect(this.particleLocation, Effect.POTION_SWIRL, 0);
                        loc.getWorld().playEffect(this.particleLocation, Effect.FLYING_GLYPH, 0);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.COLOURED_DUST, 0, 1, 0.99607843f, 0.12941177f, 0.003921569f, 1.0f, 0, 64);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.COLOURED_DUST, 0, 1, 0.99607843f, 0.12941177f, 0.003921569f, 1.0f, 0, 64);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.LARGE_SMOKE, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                        loc.getWorld().playEffect(this.particleLocation, Effect.POTION_SWIRL, 0);
                        loc.getWorld().playEffect(this.particleLocation, Effect.FLYING_GLYPH, 0);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.COLOURED_DUST, 0, 1, 0.99607843f, 0.12941177f, 0.003921569f, 1.0f, 0, 64);
                        loc.getWorld().spigot().playEffect(this.particleLocation, Effect.COLOURED_DUST, 0, 1, 0.99607843f, 0.12941177f, 0.003921569f, 1.0f, 0, 64);
                        if (1.5 > this.particleLocation.distance(loc) || 1.5 > this.particleLocation.distance(loc)) {
                            loc.getWorld().playEffect(loc.clone().add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 0);
                            loc.getWorld().playSound(loc.clone().add(0.0, -1.0, 0.0), Sound.EXPLODE, 1.0f, 1.0f);
                            loc.getWorld().strikeLightningEffect(loc.clone().add(0.0, -1.0, 0.0));
                            new SEntity(loc.clone().add(0.0, -1.0, 0.0), SEntityType.DIMOON_MINIBOSS);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
            }, i * 5L);
        }
    }

    public void fixFloor() {
        Dimoon dimoon = SkyBlock.getPlugin().dimoon;
        if (null == dimoon) {
            return;
        }
        for (int x = 0; 120 > x; ++x) {
            for (int z = 0; 120 > z; ++z) {
                Location location = dimoon.getEntity().getLocation().clone().add(60.0, 0.0, 60.0).subtract(x, 2.0, z);
                Block block = location.getBlock();
                if (Material.STAINED_CLAY == block.getType()) {
                    block.setType(Material.SMOOTH_BRICK);
                }
            }
        }
    }

    public static void cleanArena() {
        for (int i = 1; 4 > i; ++i) {
            try {
                rp(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Sputnik.pasteSchematicRep("egg3", true, 234666.0f, 155.0f, 236479.0f, Bukkit.getWorld("arena")).forEach(block -> block.setType(Material.AIR));
        Block[] a = Altar.altarList;
        for (int j = 0; j < a.length; ++j) {
            Block c = Bukkit.getWorld("arena").getBlockAt(a[j].getLocation().clone().add(0.0, 1.0, 0.0));
            c.setType(Material.AIR);
        }
    }

    private static void rp(int s) throws IOException {
        File parkour = new File("plugins/dimoon/parkours/parkour" + s + ".schematic");
        Location location = new Location(Bukkit.getWorld("arena"), 234668.5, 155.0, 236481.5);
        World world = new BukkitWorld(location.getWorld());
        Closer closer = Closer.create();
        FileInputStream fis = (FileInputStream) closer.register((Closeable) new FileInputStream(parkour));
        BufferedInputStream bis = (BufferedInputStream) closer.register((Closeable) new BufferedInputStream(fis));
        ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
        Clipboard clipboard = reader.read(world.getWorldData());
        for (int x = 0; x < clipboard.getRegion().getWidth(); ++x) {
            for (int y = 0; y < clipboard.getRegion().getHeight(); ++y) {
                for (int z = 0; z < clipboard.getRegion().getLength(); ++z) {
                    Vector minimumPoint = clipboard.getMinimumPoint();
                    Vector clipboardLoc = new Vector(minimumPoint.getBlockX() + x, minimumPoint.getBlockY() + y, minimumPoint.getBlockZ() + z);
                    BaseBlock baseBlock = clipboard.getBlock(clipboardLoc);
                    if (0 != baseBlock.getId()) {
                        Location newLocation = location.clone().subtract(57.0, 0.0, 57.0).add(x, y, z);
                        Vector loc = new Vector(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        try {
                            world.setBlock(loc, baseBlock);
                            location.getWorld().getBlockAt(newLocation).setType(Material.AIR);
                        } catch (WorldEditException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        closer.close();
    }

    public BukkitTask getParkourTask() {
        return this.parkourTask;
    }
}
