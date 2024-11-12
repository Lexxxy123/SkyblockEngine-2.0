/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Region {
    private static final Map<String, Region> REGION_CACHE = new HashMap<String, Region>();
    protected static final SkyBlock plugin = SkyBlock.getPlugin();
    protected final String name;
    protected Location firstLocation;
    protected Location secondLocation;
    protected RegionType type;
    private List<BlockState> capture;

    public Region(String name, Location firstLocation, Location secondLocation, RegionType type) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.capture = null;
        REGION_CACHE.put(this.name, this);
    }

    public void save() {
        Region.plugin.regionData.save(this);
    }

    public void delete() {
        REGION_CACHE.remove(this.name);
        Region.plugin.regionData.delete(this);
    }

    public static List<Entity> getPlayersWithinRegionType(RegionType type) {
        ArrayList<Entity> players = new ArrayList<Entity>();
        for (Region region : Region.getRegionsOfType(type)) {
            players.addAll(region.getPlayersWithinRegion());
        }
        return players;
    }

    public static Region getRegionOfEntity(Entity entity) {
        ArrayList<Region> possible = new ArrayList<Region>();
        for (Region region : Region.getRegions()) {
            if (!region.insideRegion(entity)) continue;
            possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r2 -> r2.getType().ordinal()));
        Collections.reverse(possible);
        return !possible.isEmpty() ? (Region)possible.get(0) : null;
    }

    public static Region getQuickRegionOfEntity(Entity entity) {
        for (Region region : Region.getRegions()) {
            if (!region.insideRegion(entity)) continue;
            return region;
        }
        return null;
    }

    public static Region getRegionOfBlock(Block block) {
        ArrayList<Region> possible = new ArrayList<Region>();
        for (Region region : Region.getRegions()) {
            if (!region.insideRegion(block)) continue;
            possible.add(region);
        }
        possible.sort(Comparator.comparingInt(r2 -> r2.getType().ordinal()));
        Collections.reverse(possible);
        return possible.size() != 0 ? (Region)possible.get(0) : null;
    }

    public boolean insideRegion(Entity entity) {
        List<Integer> bounds = this.getBounds();
        Location location = entity.getLocation();
        double x2 = location.getX();
        double y2 = location.getY();
        double z2 = location.getZ();
        if (this.firstLocation == null || this.firstLocation.getWorld() == null) {
            return false;
        }
        return this.firstLocation.getWorld().getUID().equals(location.getWorld().getUID()) && x2 >= (double)bounds.get(0).intValue() && x2 <= (double)bounds.get(1).intValue() && y2 >= (double)bounds.get(2).intValue() && y2 <= (double)bounds.get(3).intValue() && z2 >= (double)bounds.get(4).intValue() && z2 <= (double)bounds.get(5).intValue();
    }

    public boolean insideRegion(Block block) {
        List<Integer> bounds = this.getBounds();
        Location location = block.getLocation();
        double x2 = location.getX();
        double y2 = location.getY();
        double z2 = location.getZ();
        if (this.firstLocation == null || this.firstLocation.getWorld() == null) {
            return false;
        }
        return this.firstLocation.getWorld().getUID().equals(location.getWorld().getUID()) && x2 >= (double)bounds.get(0).intValue() && x2 <= (double)bounds.get(1).intValue() && y2 >= (double)bounds.get(2).intValue() && y2 <= (double)bounds.get(3).intValue() && z2 >= (double)bounds.get(4).intValue() && z2 <= (double)bounds.get(5).intValue();
    }

    public List<Location> getAvailableTeleportLocations() {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (Location location : this.getLocations()) {
            Block block = location.getBlock();
            if (block.getType() == Material.AIR || block.getType() == Material.WATER) continue;
            Block above = location.clone().add(0.0, 1.0, 0.0).getBlock();
            Block top = location.clone().add(0.0, 2.0, 0.0).getBlock();
            if (above.getType() != Material.AIR && above.getType() != Material.WATER || top.getType() != Material.AIR && top.getType() != Material.WATER) continue;
            locations.add(above.getLocation());
        }
        return locations;
    }

    public List<Location> getLocations() {
        if (!this.firstLocation.getWorld().getName().equals(this.secondLocation.getWorld().getName())) {
            return null;
        }
        List<Integer> bounds = this.getBounds();
        World world = this.firstLocation.getWorld();
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int y2 = bounds.get(2).intValue(); y2 <= bounds.get(3); ++y2) {
            for (int x2 = bounds.get(0).intValue(); x2 <= bounds.get(1); ++x2) {
                for (int z2 = bounds.get(4).intValue(); z2 <= bounds.get(5); ++z2) {
                    locations.add(new Location(world, (double)x2, (double)y2, (double)z2));
                }
            }
        }
        return locations;
    }

    public void captureRegion() {
        if (!this.firstLocation.getWorld().getName().equals(this.secondLocation.getWorld().getName())) {
            return;
        }
        List<Integer> bounds = this.getBounds();
        World world = this.firstLocation.getWorld();
        ArrayList<BlockState> states = new ArrayList<BlockState>();
        for (int y2 = bounds.get(2).intValue(); y2 <= bounds.get(3); ++y2) {
            for (int x2 = bounds.get(0).intValue(); x2 <= bounds.get(1); ++x2) {
                for (int z2 = bounds.get(4).intValue(); z2 <= bounds.get(5); ++z2) {
                    states.add(new Location(world, (double)x2, (double)y2, (double)z2).getBlock().getState());
                }
            }
        }
        this.capture = states;
    }

    public void pasteRegionCapture() {
        if (this.capture == null) {
            return;
        }
        for (BlockState state : this.capture) {
            state.getBlock().setType(state.getType());
            state.setRawData(state.getRawData());
            state.update();
        }
        this.capture = null;
    }

    public Location getRandomLocation() {
        List<Integer> bounds = this.getBounds();
        int minX = bounds.get(0);
        int maxX = bounds.get(1);
        int minY = bounds.get(2);
        int maxY = bounds.get(3);
        int minZ = bounds.get(4);
        int maxZ = bounds.get(5);
        int randomX = Region.random(minX, maxX);
        int randomY = Region.random(minY, maxY);
        int randomZ = Region.random(minZ, maxZ);
        return new Location(this.firstLocation.getWorld(), (double)randomX, (double)randomY, (double)randomZ);
    }

    public static int random(int min, int max) {
        if (min < 0) {
            return new Random().nextInt(max + Math.abs(min) + 1) + min;
        }
        return new Random().nextInt(max - min + 1) + min;
    }

    public Location getRandomAvailableLocation() {
        Location r2 = this.getRandomLocation();
        ArrayList<Location> possible = new ArrayList<Location>();
        for (int y2 = this.getBounds().get(3).intValue(); y2 >= this.getBounds().get(2); --y2) {
            Block test = this.firstLocation.getWorld().getBlockAt(r2.getBlockX(), y2, r2.getBlockZ());
            if (test.getType() == Material.AIR || test.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType() != Material.AIR || test.getLocation().clone().add(0.0, 2.0, 0.0).getBlock().getType() != Material.AIR) continue;
            possible.add(test.getLocation().clone().add(0.0, 1.0, 0.0));
        }
        return !possible.isEmpty() ? (Location)SUtil.getRandom(possible) : null;
    }

    public List<Integer> getBounds() {
        int sx = Math.min(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        int ex = Math.max(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        int sy = Math.min(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        int ey = Math.max(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        int sz = Math.min(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        int ez = Math.max(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        return Arrays.asList(sx, ex, sy, ey, sz, ez);
    }

    public List<Entity> getPlayersWithinRegion() {
        ArrayList entities = new ArrayList(this.firstLocation.getWorld().getEntitiesByClasses(new Class[]{Player.class}));
        return entities.stream().filter(this::insideRegion).collect(Collectors.toList());
    }

    public static Region create(String name, Location firstLocation, Location secondLocation, RegionType type) {
        return Region.plugin.regionData.create(name, firstLocation, secondLocation, type);
    }

    public static Region get(String name) {
        if (REGION_CACHE.containsKey(name)) {
            return REGION_CACHE.get(name);
        }
        return Region.plugin.regionData.get(name);
    }

    public static List<Region> getRegions() {
        return new ArrayList<Region>(REGION_CACHE.values());
    }

    public static List<Region> getRegionsOfType(RegionType type) {
        return Region.getRegions().stream().filter(region -> region.getType() == type).collect(Collectors.toList());
    }

    public static void cacheRegions() {
        for (Region region : Region.plugin.regionData.getAll()) {
            REGION_CACHE.put(region.getName(), region);
        }
        World islandWorld = Bukkit.getWorld((String)"island");
        double islandX = 0.0;
        double islandY = 0.0;
        double islandZ = 0.0;
        Location islandFirstLocation = new Location(islandWorld, islandX, islandY, islandZ);
        Location islandSecondLocation = new Location(islandWorld, islandX, islandY, islandZ);
        REGION_CACHE.put("island", new Region("island", islandFirstLocation, islandSecondLocation, RegionType.PRIVATE_ISLAND));
        World dungWorld = Bukkit.getWorld((String)"f6");
        double dungX = 0.0;
        double dungY = 0.0;
        double dungZ = 0.0;
        Location dungLocation = new Location(dungWorld, dungX, dungY, dungZ);
        Location dungSecondLocation = new Location(islandWorld, dungX, dungY, dungZ);
    }

    public static Region getIslandRegion() {
        return REGION_CACHE.get("island");
    }

    public String getName() {
        return this.name;
    }

    public Location getFirstLocation() {
        return this.firstLocation;
    }

    public Location getSecondLocation() {
        return this.secondLocation;
    }

    public RegionType getType() {
        return this.type;
    }

    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public void setType(RegionType type) {
        this.type = type;
    }

    public List<BlockState> getCapture() {
        return this.capture;
    }
}

