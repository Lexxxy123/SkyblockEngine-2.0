package net.hypixel.skyblock.entity;

import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityPopulator {
    private static final List<EntityPopulator> POPULATORS;
    private final int amount;
    private final int max;
    private final long delay;
    private final SEntityType type;
    private final Predicate<World> condition;
    private final RegionType regionType;
    private BukkitTask task;
    private final List<SEntity> spawned;

    public static List<EntityPopulator> getPopulators() {
        return POPULATORS;
    }

    public EntityPopulator(final int amount, final int max, final long delay, final SEntityType type, final RegionType regionType, final Predicate<World> condition) {
        this.amount = amount;
        this.max = max;
        this.delay = delay;
        this.type = type;
        this.regionType = regionType;
        this.spawned = new ArrayList<SEntity>();
        this.condition = condition;
        EntityPopulator.POPULATORS.add(this);
    }

    public EntityPopulator(final int amount, final int max, final long delay, final SEntityType type, final RegionType regionType) {
        this(amount, max, delay, type, regionType, null);
    }

    public void start() {
        this.task = new BukkitRunnable() {
            public void run() {
                spawned.removeIf(sEntity -> sEntity.getEntity().isDead());
                final List<Region> regions = Region.getRegionsOfType(regionType);
                if (regions.isEmpty()) {
                    return;
                }

                if (Region.getPlayersWithinRegionType(regionType).isEmpty()) {
                    for (final SEntity s : spawned) {
                        s.remove();
                    }
                    spawned.clear();
                    return;
                }
                if (condition != null && !condition.test(SUtil.getRandom(regions).getFirstLocation().getWorld())) {
                    return;
                }
                if (spawned.size() >= max) {
                    return;
                }
                for (int i = 0; i < amount; ++i) {
                    int attempts = 0;
                    Location available;
                    do {
                        available = SUtil.getRandom(regions).getRandomAvailableLocation();
                        ++attempts;
                    } while (available == null && attempts <= 150);
                    if (available != null) {
                        SEntity sEntity = new SEntity(available.clone().add(0.5, 0.0, 0.5), type);
                        spawned.add(sEntity);
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, this.delay);

        new BukkitRunnable(){
            @Override
            public void run() {
                if (spawned.isEmpty()) return;
                if (Region.getPlayersWithinRegionType(regionType).isEmpty()) {
                    for (final SEntity s : spawned) {
                        s.remove();
                    }
                    spawned.clear();
                }
            }
        }.runTaskTimerAsynchronously(SkyBlock.getPlugin() , 0L , 20);
    }


    public void stop() {
        if (this.task == null) {
            return;
        }
        this.task.cancel();
    }

    public static void stopAll() {
        for (final EntityPopulator populator : EntityPopulator.POPULATORS) {
            populator.stop();
        }
    }

    public RegionType getRegionType() {
        return this.regionType;
    }

    static {
        POPULATORS = new ArrayList<EntityPopulator>();
    }
}
