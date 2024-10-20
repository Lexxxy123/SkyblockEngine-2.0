/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package net.hypixel.skyblock.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class EntitySpawner {
    private static final List<EntitySpawner> SPAWNER_CACHE = new ArrayList<EntitySpawner>();
    private static BukkitTask SPAWNER_TASK;
    private final UUID uuid;
    private final SEntityType type;
    private final Location location;
    private SEntity active;

    private EntitySpawner(UUID uuid, SEntityType type, Location location) {
        this.uuid = uuid;
        this.type = type;
        this.location = location;
        this.save();
    }

    public EntitySpawner(SEntityType type, Location location) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.location = location;
        SPAWNER_CACHE.add(this);
        this.save();
    }

    public void delete() {
        Config spawners = SkyBlock.getPlugin().spawners;
        SPAWNER_CACHE.remove(this);
        spawners.set(this.uuid.toString(), null);
        spawners.save();
    }

    public void save() {
        Config spawners = SkyBlock.getPlugin().spawners;
        spawners.set(this.uuid.toString() + ".type", this.type.name());
        spawners.set(this.uuid + ".location", this.location);
        spawners.save();
    }

    public String toString() {
        return "EntitySpawner{uuid=" + this.uuid.toString() + ", type=" + this.type.name() + ", location=" + this.location.toString() + "}";
    }

    public static EntitySpawner deserialize(String key) {
        Config spawners = SkyBlock.getPlugin().spawners;
        return new EntitySpawner(UUID.fromString(key), SEntityType.getEntityType(spawners.getString(key + ".type")), (Location)spawners.get(key + ".location"));
    }

    public static List<EntitySpawner> getSpawners() {
        if (SPAWNER_CACHE.size() == 0) {
            Config spawners = SkyBlock.getPlugin().spawners;
            for (String key : spawners.getKeys(false)) {
                SPAWNER_CACHE.add(EntitySpawner.deserialize(key));
            }
        }
        return SPAWNER_CACHE;
    }

    public static void startSpawnerTask() {
        if (SPAWNER_TASK != null) {
            return;
        }
        SPAWNER_TASK = SkyBlock.getPlugin().getServer().getScheduler().runTaskTimer((Plugin)SkyBlock.getPlugin(), () -> {
            ArrayList<Location> locations = new ArrayList<Location>(Bukkit.getOnlinePlayers().size());
            for (Player player : Bukkit.getOnlinePlayers()) {
                locations.add(player.getLocation());
            }
            for (EntitySpawner spawner : EntitySpawner.getSpawners()) {
                boolean sp = false;
                for (Location location : locations) {
                    if (!location.getWorld().getUID().equals(spawner.location.getWorld().getUID()) || !(location.distance(spawner.location) <= 60.0)) continue;
                    sp = true;
                    break;
                }
                if (!sp) {
                    if (spawner.active == null || spawner.active.getEntity().isDead()) continue;
                    spawner.active.remove();
                    continue;
                }
                if (spawner.active != null && !spawner.active.getEntity().isDead()) continue;
                spawner.active = new SEntity(spawner.location, spawner.type, new Object[0]);
                spawner.active.getEntity().setRemoveWhenFarAway(true);
            }
        }, 0L, 400L);
    }

    public static void stopSpawnerTask() {
        SPAWNER_TASK.cancel();
        SPAWNER_TASK = null;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public SEntityType getType() {
        return this.type;
    }

    public Location getLocation() {
        return this.location;
    }
}

