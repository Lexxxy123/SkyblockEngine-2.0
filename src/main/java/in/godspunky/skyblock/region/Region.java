package in.godspunky.skyblock.region;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.util.SUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Region {
    private static final Map<String, Region> REGION_CACHE;
    protected static final SkySimEngine plugin;
    protected final String name;
    protected Location firstLocation;
    protected Location secondLocation;
    protected RegionType type;
    private List<BlockState> capture;

    public Region(final String name, final Location firstLocation, final Location secondLocation, final RegionType type) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.capture = null;
        Region.REGION_CACHE.put(this.name, this);
    }

    public void save() {
        Region.plugin.regionData.save(this);
    }

    public void delete() {
        Region.REGION_CACHE.remove(this.name);
        Region.plugin.regionData.delete(this);
    }

    public static List<Entity> getPlayersWithinRegionType(final RegionType type) {
        final List<Entity> players = new ArrayList<Entity>();
        for (final Region region : getRegionsOfType(type)) {
            players.addAll(region.getPlayersWithinRegion());
        }
        return players;
    }

    public static Region getRegionOfEntity(final Entity entity) {
        final List<Region> possible = new ArrayList<Region>();
        for (final Region region : getRegions()) {
            if (region.insideRegion(entity)) {
                possible.add(region);
            }
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return (possible.size() != 0) ? possible.get(0) : null;
    }

    public static Region getQuickRegionOfEntity(final Entity entity) {
        for (final Region region : getRegions()) {
            if (region.insideRegion(entity)) {
                return region;
            }
        }
        return null;
    }

    public static Region getRegionOfBlock(final Block block) {
        final List<Region> possible = new ArrayList<Region>();
        for (final Region region : getRegions()) {
            if (region.insideRegion(block)) {
                possible.add(region);
            }
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse(possible);
        return (possible.size() != 0) ? possible.get(0) : null;
    }

    public boolean insideRegion(final Entity entity) {
        final List<Integer> bounds = this.getBounds();
        final Location location = entity.getLocation();
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        return this.firstLocation != null && this.firstLocation.getWorld() != null && this.firstLocation.getWorld().getUID().equals(location.getWorld().getUID()) && x >= bounds.get(0) && x <= bounds.get(1) && y >= bounds.get(2) && y <= bounds.get(3) && z >= bounds.get(4) && z <= bounds.get(5);
    }

    public boolean insideRegion(final Block block) {
        final List<Integer> bounds = this.getBounds();
        final Location location = block.getLocation();
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        return this.firstLocation != null && this.firstLocation.getWorld() != null && this.firstLocation.getWorld().getUID().equals(location.getWorld().getUID()) && x >= bounds.get(0) && x <= bounds.get(1) && y >= bounds.get(2) && y <= bounds.get(3) && z >= bounds.get(4) && z <= bounds.get(5);
    }

    public List<Location> getAvailableTeleportLocations() {
        final List<Location> locations = new ArrayList<Location>();
        for (final Location location : this.getLocations()) {
            final Block block = location.getBlock();
            if (block.getType() != Material.AIR) {
                if (block.getType() == Material.WATER) {
                    continue;
                }
                final Block above = location.clone().add(0.0, 1.0, 0.0).getBlock();
                final Block top = location.clone().add(0.0, 2.0, 0.0).getBlock();
                if (above.getType() != Material.AIR && above.getType() != Material.WATER) {
                    continue;
                }
                if (top.getType() != Material.AIR && top.getType() != Material.WATER) {
                    continue;
                }
                locations.add(above.getLocation());
            }
        }
        return locations;
    }

    public List<Location> getLocations() {
        if (!this.firstLocation.getWorld().getName().equals(this.secondLocation.getWorld().getName())) {
            return null;
        }
        final List<Integer> bounds = this.getBounds();
        final World world = this.firstLocation.getWorld();
        final List<Location> locations = new ArrayList<Location>();
        for (int y = bounds.get(2); y <= bounds.get(3); ++y) {
            for (int x = bounds.get(0); x <= bounds.get(1); ++x) {
                for (int z = bounds.get(4); z <= bounds.get(5); ++z) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

    public void captureRegion() {
        if (!this.firstLocation.getWorld().getName().equals(this.secondLocation.getWorld().getName())) {
            return;
        }
        final List<Integer> bounds = this.getBounds();
        final World world = this.firstLocation.getWorld();
        final List<BlockState> states = new ArrayList<BlockState>();
        for (int y = bounds.get(2); y <= bounds.get(3); ++y) {
            for (int x = bounds.get(0); x <= bounds.get(1); ++x) {
                for (int z = bounds.get(4); z <= bounds.get(5); ++z) {
                    states.add(new Location(world, x, y, z).getBlock().getState());
                }
            }
        }
        this.capture = states;
    }

    public void pasteRegionCapture() {
        if (this.capture == null) {
            return;
        }
        for (final BlockState state : this.capture) {
            state.getBlock().setType(state.getType());
            state.setRawData(state.getRawData());
            state.update();
        }
        this.capture = null;
    }

    public Location getRandomLocation() {
        final List<Integer> bounds = this.getBounds();
        return new Location(this.firstLocation.getWorld(), SUtil.random(bounds.get(0), bounds.get(1)), SUtil.random(bounds.get(2), bounds.get(3)), SUtil.random(bounds.get(4), bounds.get(5)));
    }

    public Location getRandomAvailableLocation() {
        final Location r = this.getRandomLocation();
        final List<Location> possible = new ArrayList<Location>();
        for (int y = this.getBounds().get(3); y >= this.getBounds().get(2); --y) {
            final Block test = this.firstLocation.getWorld().getBlockAt(r.getBlockX(), y, r.getBlockZ());
            if (test.getType() != Material.AIR && test.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR && test.getLocation().clone().add(0.0, 2.0, 0.0).getBlock().getType() == Material.AIR) {
                possible.add(test.getLocation().clone().add(0.0, 1.0, 0.0));
            }
        }
        return possible.isEmpty() ? null : SUtil.getRandom(possible);
    }

    public List<Integer> getBounds() {
        final int sx = Math.min(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        final int ex = Math.max(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        final int sy = Math.min(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        final int ey = Math.max(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        final int sz = Math.min(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        final int ez = Math.max(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        return Arrays.asList(sx, ex, sy, ey, sz, ez);
    }

    public List<Entity> getPlayersWithinRegion() {
        final List<Entity> entities = new ArrayList<Entity>(this.firstLocation.getWorld().getEntitiesByClasses(Player.class));
        return entities.stream().filter((this::insideRegion)).collect(Collectors.toList());
    }

    public static Region create(final String name, final Location firstLocation, final Location secondLocation, final RegionType type) {
        return Region.plugin.regionData.create(name, firstLocation, secondLocation, type);
    }

    public static Region get(final String name) {
        if (Region.REGION_CACHE.containsKey(name)) {
            return Region.REGION_CACHE.get(name);
        }
        return Region.plugin.regionData.get(name);
    }

    public static List<Region> getRegions() {
        return new ArrayList<Region>(Region.REGION_CACHE.values());
    }

    public static List<Region> getRegionsOfType(final RegionType type) {
        return getRegions().stream().filter(region -> region.getType() == type).collect(Collectors.toList());
    }

    public static void cacheRegions() {
        for (final Region region : Region.plugin.regionData.getAll()) {
            Region.REGION_CACHE.put(region.getName(), region);
        }
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

    public void setFirstLocation(final Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public void setSecondLocation(final Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public void setType(final RegionType type) {
        this.type = type;
    }

    public List<BlockState> getCapture() {
        return this.capture;
    }

    static {
        REGION_CACHE = new HashMap<String, Region>();
        plugin = SkySimEngine.getPlugin();
    }
}
