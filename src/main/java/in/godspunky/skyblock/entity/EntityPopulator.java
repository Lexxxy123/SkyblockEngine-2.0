package in.godspunky.skyblock.entity;

import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.util.SUtil;

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
        return EntityPopulator.POPULATORS;
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
                EntityPopulator.this.spawned.removeIf(sEntity -> sEntity.getEntity().isDead());
                final List<Region> regions = Region.getRegionsOfType(EntityPopulator.this.regionType);
                if (regions.isEmpty()) {
                    return;
                }
                if (Region.getPlayersWithinRegionType(EntityPopulator.this.regionType).isEmpty()) {
                    for (final SEntity s : EntityPopulator.this.spawned) {
                        s.remove();
                    }
                    EntityPopulator.this.spawned.clear();
                    return;
                }
                if (EntityPopulator.this.condition != null && !EntityPopulator.this.condition.test(SUtil.getRandom(regions).getFirstLocation().getWorld())) {
                    return;
                }
                if (EntityPopulator.this.spawned.size() >= EntityPopulator.this.max) {
                    return;
                }
                for (int i = 0; i < EntityPopulator.this.amount; ++i) {
                    int attempts = 0;
                    Location available;
                    do {
                        available = SUtil.getRandom(regions).getRandomAvailableLocation();
                        ++attempts;
                    } while (available == null && attempts <= 150);
                    if (available != null) {
                        EntityPopulator.this.spawned.add(new SEntity(available.clone().add(0.5, 0.0, 0.5), EntityPopulator.this.type));
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, this.delay);
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
