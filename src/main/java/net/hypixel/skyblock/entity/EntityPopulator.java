/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.hypixel.skyblock.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EntityPopulator {
    private static final List<EntityPopulator> POPULATORS = new ArrayList<EntityPopulator>();
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

    public EntityPopulator(int amount, int max, long delay, SEntityType type, RegionType regionType, Predicate<World> condition) {
        this.amount = amount;
        this.max = max;
        this.delay = delay;
        this.type = type;
        this.regionType = regionType;
        this.spawned = new ArrayList<SEntity>();
        this.condition = condition;
        POPULATORS.add(this);
    }

    public EntityPopulator(int amount, int max, long delay, SEntityType type, RegionType regionType) {
        this(amount, max, delay, type, regionType, null);
    }

    public void start() {
        this.task = new BukkitRunnable(){

            public void run() {
                EntityPopulator.this.spawned.removeIf(sEntity -> sEntity.getEntity().isDead());
                List<Region> regions = Region.getRegionsOfType(EntityPopulator.this.regionType);
                if (regions.isEmpty()) {
                    return;
                }
                if (Region.getPlayersWithinRegionType(EntityPopulator.this.regionType).isEmpty()) {
                    for (SEntity s2 : EntityPopulator.this.spawned) {
                        s2.remove();
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
                for (int i2 = 0; i2 < EntityPopulator.this.amount; ++i2) {
                    Location available;
                    int attempts = 0;
                    while ((available = SUtil.getRandom(regions).getRandomAvailableLocation()) == null && ++attempts <= 150) {
                    }
                    if (available == null) continue;
                    SEntity sEntity2 = new SEntity(available.clone().add(0.5, 0.0, 0.5), EntityPopulator.this.type, new Object[0]);
                    EntityPopulator.this.spawned.add(sEntity2);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, this.delay);
        new BukkitRunnable(){

            public void run() {
                if (EntityPopulator.this.spawned.isEmpty()) {
                    return;
                }
                if (Region.getPlayersWithinRegionType(EntityPopulator.this.regionType).isEmpty()) {
                    for (SEntity s2 : EntityPopulator.this.spawned) {
                        s2.remove();
                    }
                    EntityPopulator.this.spawned.clear();
                }
            }
        }.runTaskTimerAsynchronously((Plugin)SkyBlock.getPlugin(), 0L, 20L);
    }

    public void stop() {
        if (this.task == null) {
            return;
        }
        this.task.cancel();
    }

    public static void stopAll() {
        for (EntityPopulator populator : POPULATORS) {
            populator.stop();
        }
    }

    public RegionType getRegionType() {
        return this.regionType;
    }
}

