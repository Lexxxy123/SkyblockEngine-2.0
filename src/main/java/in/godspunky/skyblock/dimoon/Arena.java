package in.godspunky.skyblock.dimoon;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.World;
import in.godspunky.skyblock.Skyblock;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import in.godspunky.skyblock.dimoon.utils.Utils;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

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
        final File parkour = new File("plugins/dimoon/parkours/parkour" + this.currentParkour + ".schematic");
        final Dimoon dimoon = Skyblock.getPlugin().dimoon;
        if (dimoon == null) {
            return;
        }
        for (final Player p : dimoon.getEntity().getWorld().getPlayers()) {
            p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            p.sendMessage(Utils.format("&c&lALERT! &eParkour level &a" + this.currentParkour + " &espawned in! Try your best, &dyou can do it!!"));
            p.sendMessage(Sputnik.trans("&eHint: &6Reach the &chighest point &6of the parkour to complete it!"));
        }
        final Location location = dimoon.getEntity().getLocation();
        final World world = new BukkitWorld(location.getWorld());
        final Closer closer = Closer.create();
        final FileInputStream fis = (FileInputStream) closer.register((Closeable) new FileInputStream(parkour));
        final BufferedInputStream bis = (BufferedInputStream) closer.register((Closeable) new BufferedInputStream(fis));
        final ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
        final Clipboard clipboard = reader.read(world.getWorldData());
        int highestYC = this.highestY;
        Location minYLoc = null;
        int minY = Integer.MAX_VALUE;
        for (int x = 0; x < clipboard.getRegion().getWidth(); ++x) {
            for (int y = 0; y < clipboard.getRegion().getHeight(); ++y) {
                for (int z = 0; z < clipboard.getRegion().getLength(); ++z) {
                    final Vector minimumPoint = clipboard.getMinimumPoint();
                    final Vector clipboardLoc = new Vector(minimumPoint.getBlockX() + x, minimumPoint.getBlockY() + y, minimumPoint.getBlockZ() + z);
                    final BaseBlock baseBlock = clipboard.getBlock(clipboardLoc);
                    if (baseBlock.getId() != 0) {
                        final Location newLocation = location.clone().subtract(57.0, 0.0, 57.0).add(x, y, z);
                        final Vector loc = new Vector(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        try {
                            world.setBlock(loc, baseBlock);
                            if (y < minY && baseBlock.getId() != 165 && clipboard.getBlock(clipboardLoc.add(0, 1, 0)).getId() == 0 && newLocation.getBlock().getType().isSolid()) {
                                minY = y;
                                minYLoc = newLocation;
                            }
                            this.parkourBlocks.add(location.getWorld().getBlockAt(newLocation));
                            if (this.currentParkour == 1 && newLocation.getBlock().getType() == Material.BARRIER) {
                                newLocation.getBlock().setType(Material.AIR);
                            }
                            highestYC = Math.max(highestYC, loc.getBlockY());
                        } catch (final WorldEditException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        closer.close();
        final List<Block> parkourList = new ArrayList<Block>(this.parkourBlocks);
        this.parkourBlocks.clear();
        for (final Block b : parkourList) {
            if (!b.getType().isSolid()) {
                this.parkourBlocks.add(b);
            }
        }
        for (final Block b : parkourList) {
            if (b.getType().isSolid()) {
                this.parkourBlocks.add(b);
            }
        }
        if (this.currentParkour != 1) {
            for (final Player p2 : dimoon.getEntity().getWorld().getPlayers()) {
                p2.teleport(minYLoc.add(0.0, 1.0, 0.0));
            }
        } else {
            for (final Player p2 : dimoon.getEntity().getWorld().getPlayers()) {
                p2.teleport(new Location(dimoon.getEntity().getWorld(), 234673.5, 155.0, 236425.5));
            }
        }
        final int hc = highestYC;
        this.currentParkour = this.currentParkour % 3 + 1;
        SUtil.delay(() -> this.highestY = hc, 1L);
        final Skyblock plugin = Skyblock.getPlugin();
        this.parkourTask = new BukkitRunnable() {
            public void run() {
                Arena.this.collapseParkour();
                Arena.this.collapseFloor();
                for (final Player p : Skyblock.getPlugin().dimoon.getEntity().getWorld().getPlayers()) {
                    p.sendMessage(Utils.format("&cYou couldn't complete the parkour in time, the boss regained 5,000 HP that sucks!"));
                }
                Skyblock.getPlugin().arena.highestY = -1;
                dimoon.getTasks().add(new BukkitRunnable() {
                    public void run() {
                        try {
                            Skyblock.getPlugin().arena.pasteParkour();
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.runTaskLater(Skyblock.getPlugin(), 1200L));
                if (plugin.dimoon != null) {
                    plugin.dimoon.heal(5000);
                }
            }
        }.runTaskLater(Skyblock.getPlugin(), 12000L);
    }

    public void collapseParkour() {
        if (this.isCollapsing) {
            return;
        }
        this.isCollapsing = true;
        final Iterator<Block> iterator = this.parkourBlocks.iterator();
        final Skyblock plugin = Skyblock.getPlugin();
        new BukkitRunnable() {
            public void run() {
                if (!iterator.hasNext()) {
                    Arena.this.isCollapsing = false;
                    this.cancel();
                    Arena.this.parkourBlocks.clear();
                    return;
                }
                final Block block = iterator.next();
                if (block.getType().isSolid()) {
                    block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.0, 0.5), block.getType(), block.getData());
                    block.getWorld().playSound(block.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.5f, 1.0f);
                }
                block.setType(Material.AIR);
            }
        }.runTaskTimer(Skyblock.getPlugin(), 1L, 1L);
    }

    public void collapseFloor() {
        final Dimoon dimoon = Skyblock.getPlugin().dimoon;
        if (dimoon == null) {
        }
    }

    public void spawnDimoonaize(final Entity entity) {
        Utils.bossMessage("DIMOONAIZE! GO!");
        final List<Location> locList = new ArrayList<Location>();
        final Dimoon dimoon = Skyblock.getPlugin().dimoon;
        if (dimoon == null) {
            return;
        }
        for (int x = 0; x < 120; ++x) {
            for (int z = 0; z < 120; ++z) {
                final Location location = dimoon.getEntity().getLocation().clone().add(60.0, 0.0, 60.0).subtract(x, 2.0, z);
                final Block block = location.getBlock();
                if (block.getType() == Material.BEACON) {
                    locList.add(block.getLocation().add(0.5, 3.0, 0.5));
                }
            }
        }
        int i = 0;
        for (final Location loc : locList) {
            ++i;
            SUtil.delay(() -> {
                final org.bukkit.World world = entity.getWorld();
                final org.bukkit.util.Vector[] velocity = {loc.toVector().subtract(entity.getLocation().toVector()).normalize()};
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
                        if (this.particleLocation.distance(loc) < 1.5 || this.particleLocation.distance(loc) < 1.5) {
                            loc.getWorld().playEffect(loc.clone().add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 0);
                            loc.getWorld().playSound(loc.clone().add(0.0, -1.0, 0.0), Sound.EXPLODE, 1.0f, 1.0f);
                            loc.getWorld().strikeLightningEffect(loc.clone().add(0.0, -1.0, 0.0));
                            new SEntity(loc.clone().add(0.0, -1.0, 0.0), SEntityType.DIMOON_MINIBOSS);
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Skyblock.getPlugin(), 0L, 1L);
            }, i * 5L);
        }
    }

    public void fixFloor() {
        final Dimoon dimoon = Skyblock.getPlugin().dimoon;
        if (dimoon == null) {
            return;
        }
        for (int x = 0; x < 120; ++x) {
            for (int z = 0; z < 120; ++z) {
                final Location location = dimoon.getEntity().getLocation().clone().add(60.0, 0.0, 60.0).subtract(x, 2.0, z);
                final Block block = location.getBlock();
                if (block.getType() == Material.STAINED_CLAY) {
                    block.setType(Material.SMOOTH_BRICK);
                }
            }
        }
    }

    public static void cleanArena() {
        for (int i = 1; i < 4; ++i) {
            try {
                rp(i);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        Sputnik.pasteSchematicRep("egg3", true, 234666.0f, 155.0f, 236479.0f, Bukkit.getWorld("arena")).forEach(block -> block.setType(Material.AIR));
        final Block[] a = Altar.altarList;
        for (int j = 0; j < a.length; ++j) {
            final Block c = Bukkit.getWorld("arena").getBlockAt(a[j].getLocation().clone().add(0.0, 1.0, 0.0));
            c.setType(Material.AIR);
        }
    }

    private static void rp(final int s) throws IOException {
        final File parkour = new File("plugins/dimoon/parkours/parkour" + s + ".schematic");
        final Location location = new Location(Bukkit.getWorld("arena"), 234668.5, 155.0, 236481.5);
        final World world = new BukkitWorld(location.getWorld());
        final Closer closer = Closer.create();
        final FileInputStream fis = (FileInputStream) closer.register((Closeable) new FileInputStream(parkour));
        final BufferedInputStream bis = (BufferedInputStream) closer.register((Closeable) new BufferedInputStream(fis));
        final ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(bis);
        final Clipboard clipboard = reader.read(world.getWorldData());
        for (int x = 0; x < clipboard.getRegion().getWidth(); ++x) {
            for (int y = 0; y < clipboard.getRegion().getHeight(); ++y) {
                for (int z = 0; z < clipboard.getRegion().getLength(); ++z) {
                    final Vector minimumPoint = clipboard.getMinimumPoint();
                    final Vector clipboardLoc = new Vector(minimumPoint.getBlockX() + x, minimumPoint.getBlockY() + y, minimumPoint.getBlockZ() + z);
                    final BaseBlock baseBlock = clipboard.getBlock(clipboardLoc);
                    if (baseBlock.getId() != 0) {
                        final Location newLocation = location.clone().subtract(57.0, 0.0, 57.0).add(x, y, z);
                        final Vector loc = new Vector(newLocation.getBlockX(), newLocation.getBlockY(), newLocation.getBlockZ());
                        try {
                            world.setBlock(loc, baseBlock);
                            location.getWorld().getBlockAt(newLocation).setType(Material.AIR);
                        } catch (final WorldEditException e) {
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
