package in.godspunky.skyblock.entity;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EntitySpawner {
    private static final List<EntitySpawner> SPAWNER_CACHE;
    private static BukkitTask SPAWNER_TASK;

    static {
        SPAWNER_CACHE = new ArrayList<EntitySpawner>();
    }

    private final UUID uuid;
    private final SEntityType type;
    private final Location location;
    private SEntity active;

    private EntitySpawner(final UUID uuid, final SEntityType type, final Location location) {
        this.uuid = uuid;
        this.type = type;
        this.location = location;
        this.save();
    }

    public EntitySpawner(final SEntityType type, final Location location) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.location = location;
        EntitySpawner.SPAWNER_CACHE.add(this);
        this.save();
    }

    public static EntitySpawner deserialize(final String key) {
        final Config spawners = Skyblock.getPlugin().spawners;
        return new EntitySpawner(UUID.fromString(key), SEntityType.getEntityType(spawners.getString(key + ".type")), (Location) spawners.get(key + ".location"));
    }

    public static List<EntitySpawner> getSpawners() {
        if (EntitySpawner.SPAWNER_CACHE.size() == 0) {
            final Config spawners = Skyblock.getPlugin().spawners;
            for (final String key : spawners.getKeys(false)) {
                EntitySpawner.SPAWNER_CACHE.add(deserialize(key));
            }
        }
        return EntitySpawner.SPAWNER_CACHE;
    }

    public static void startSpawnerTask() {
        if (EntitySpawner.SPAWNER_TASK != null) {
            return;
        }
        EntitySpawner.SPAWNER_TASK = Skyblock.getPlugin().getServer().getScheduler().runTaskTimer(Skyblock.getPlugin(), () -> {
            final ArrayList<Location> locations = new ArrayList<Location>(Bukkit.getOnlinePlayers().size());

            final Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
            while (iterator.hasNext()) {
                final Player player = iterator.next();
                locations.add(player.getLocation());
            }

            final Iterator<EntitySpawner> iterator2 = getSpawners().iterator();
            while (iterator2.hasNext()) {
                final EntitySpawner spawner = iterator2.next();
                boolean sp = false;

                final Iterator<Location> iterator3 = locations.iterator();
                while (iterator3.hasNext()) {
                    final Location location = iterator3.next();
                    if (!location.getWorld().getUID().equals(spawner.location.getWorld().getUID())) {
                        continue;
                    } else if (location.distance(spawner.location) <= 60.0) {
                        sp = true;
                        break;
                    } else {
                        continue;
                    }
                }
                if (!sp) {
                    if (spawner.active != null && !spawner.active.getEntity().isDead()) {
                        spawner.active.remove();
                    } else {
                        continue;
                    }
                } else if (spawner.active == null || spawner.active.getEntity().isDead()) {
                    spawner.active = new SEntity(spawner.location, spawner.type);
                    spawner.active.getEntity().setRemoveWhenFarAway(true);
                } else {
                    continue;
                }
            }
        }, 0L, 400L);
    }

    public static void stopSpawnerTask() {
        EntitySpawner.SPAWNER_TASK.cancel();
        EntitySpawner.SPAWNER_TASK = null;
    }

    public void delete() {
        final Config spawners = Skyblock.getPlugin().spawners;
        EntitySpawner.SPAWNER_CACHE.remove(this);
        spawners.set(this.uuid.toString(), null);
        spawners.save();
    }

    public void save() {
        final Config spawners = Skyblock.getPlugin().spawners;
        spawners.set(this.uuid.toString() + ".type", this.type.name());
        spawners.set(this.uuid + ".location", this.location);
        spawners.save();
    }

    @Override
    public String toString() {
        return "EntitySpawner{uuid=" + this.uuid.toString() + ", type=" + this.type.name() + ", location=" + this.location.toString() + "}";
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
