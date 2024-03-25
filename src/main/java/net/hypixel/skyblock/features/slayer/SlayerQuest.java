package net.hypixel.skyblock.features.slayer;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Setter;
import net.hypixel.skyblock.features.sequence.SoundSequenceType;
import org.bson.Document;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;

import java.util.HashMap;
import java.util.Map;

public class SlayerQuest implements ConfigurationSerializable {
    private final SlayerBossType type;
    private final long started;
    @Setter
    private double xp;
    @Setter
    private long spawned;
    @Setter
    private long killed;
    @Setter
    private long died;
    @Setter
    private SEntityType lastKilled;
    @Setter
    private SEntity entity;

    public SlayerQuest(final SlayerBossType type, final long started) {
        this.type = type;
        this.started = started;
        this.entity = null;
    }

    private SlayerQuest(final SlayerBossType type, final long started, final double xp, final long spawned, final long killed, final long died, final SEntityType lastKilled) {
        this.type = type;
        this.started = started;
        this.xp = xp;
        this.spawned = spawned;
        this.killed = killed;
        this.died = died;
        this.lastKilled = null;
        this.entity = null;
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", this.type.getNamespace());
        map.put("started", this.started);
        map.put("xp", this.xp);
        map.put("spawned", this.spawned);
        map.put("killed", this.killed);
        map.put("died", this.died);
        map.put("lastKilled", null);
        return map;
    }



    public static SlayerQuest deserialize(Map<String, Object> map) {
        return new SlayerQuest(SlayerBossType.getByNamespace(String.valueOf(map.get("type"))), ((Number) map.get("started")).longValue(), ((Number) map.get("xp")).doubleValue(), ((Number) map.get("spawned")).longValue(), ((Number) map.get("killed")).longValue(), ((Number) map.get("died")).longValue(), null);
    }



    public static SlayerQuest deserializeSlayerQuest(Document document) {
        SlayerBossType type = SlayerBossType.getByNamespace(document.getString("type"));
        long started = document.getLong("started");
        double xp = document.getDouble("xp");
        long spawned = document.getLong("spawned");
        long killed = document.getLong("killed");
        long died = document.getLong("died");
        return new SlayerQuest(type, started, xp, spawned, killed, died, null);
    }

    public static void playMinibossSpawn(final Location location, final Entity sound) {
        final Location clone = location.clone();
        final World world = location.getWorld();
        if (sound != null) {
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(sound);
        } else {
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(clone);
        }
        final AtomicDouble additive = new AtomicDouble();
        SUtil.runIntervalForTicks(() -> world.spigot().playEffect(clone.clone().add(0.0, additive.getAndAdd(0.5), 0.0), Effect.EXPLOSION_LARGE, 1, 0, 0.0f, 0.0f, 0.0f, 0.0f, 1, 16), 3L, 12L);
    }

    public static void playBossSpawn(final Location location, final Entity sound) {
        final Location clone = location.clone();
        final World world = location.getWorld();
        if (sound != null) {
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(sound);
        } else {
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(clone);
        }
        SUtil.runIntervalForTicks(() -> {
            for (int i = 0; i < 50; ++i) {
                world.playEffect(clone.clone().add(0.0, -0.2, 0.0), Effect.WITCH_MAGIC, (Object) Effect.SPELL.getData());
                world.playEffect(clone, Effect.SPELL, (Object) Effect.SPELL.getData());
                world.playEffect(clone, Effect.FLYING_GLYPH, (Object) Effect.FLYING_GLYPH.getData());
                world.playEffect(clone.clone().add(0.0, -0.2, 0.0), Effect.FLYING_GLYPH, (Object) Effect.FLYING_GLYPH.getData());
                world.playEffect(clone, Effect.WITCH_MAGIC, (Object) Effect.WITCH_MAGIC.getData());
            }
        }, 5L, 28L);
        new BukkitRunnable() {
            public void run() {
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object) Effect.EXPLOSION_HUGE.getData());
            }
        }.runTaskLater(SkyBlock.getPlugin(), 28L);
    }

    public SlayerBossType getType() {
        return this.type;
    }

    public long getStarted() {
        return this.started;
    }

    public double getXp() {
        return this.xp;
    }

    public long getSpawned() {
        return this.spawned;
    }

    public long getKilled() {
        return this.killed;
    }

    public long getDied() {
        return this.died;
    }

    public SEntityType getLastKilled() {
        return this.lastKilled;
    }

    public SEntity getEntity() {
        return this.entity;
    }

}
