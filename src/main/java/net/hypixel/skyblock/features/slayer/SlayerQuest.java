/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.entity.Entity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.features.slayer;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.sequence.SoundSequenceType;
import net.hypixel.skyblock.features.slayer.SlayerBossType;
import net.hypixel.skyblock.util.SUtil;
import org.bson.Document;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SlayerQuest
implements ConfigurationSerializable {
    private final SlayerBossType type;
    private final long started;
    private double xp;
    private long spawned;
    private long killed;
    private long died;
    private SEntityType lastKilled;
    private SEntity entity;

    public SlayerQuest(SlayerBossType type, long started) {
        this.type = type;
        this.started = started;
        this.entity = null;
    }

    private SlayerQuest(SlayerBossType type, long started, double xp, long spawned, long killed, long died, SEntityType lastKilled) {
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
        HashMap<String, Object> map = new HashMap<String, Object>();
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
        return new SlayerQuest(SlayerBossType.getByNamespace(String.valueOf(map.get("type"))), ((Number)map.get("started")).longValue(), ((Number)map.get("xp")).doubleValue(), ((Number)map.get("spawned")).longValue(), ((Number)map.get("killed")).longValue(), ((Number)map.get("died")).longValue(), null);
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

    public static void playMinibossSpawn(Location location, Entity sound) {
        Location clone = location.clone();
        World world = location.getWorld();
        if (sound != null) {
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(sound);
        } else {
            SoundSequenceType.SLAYER_MINIBOSS_SPAWN.play(clone);
        }
        AtomicDouble additive = new AtomicDouble();
        SUtil.runIntervalForTicks(() -> world.spigot().playEffect(clone.clone().add(0.0, additive.getAndAdd(0.5), 0.0), Effect.EXPLOSION_LARGE, 1, 0, 0.0f, 0.0f, 0.0f, 0.0f, 1, 16), 3L, 12L);
    }

    public static void playBossSpawn(Location location, Entity sound) {
        final Location clone = location.clone();
        final World world = location.getWorld();
        if (sound != null) {
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(sound);
        } else {
            SoundSequenceType.SLAYER_BOSS_SPAWN.play(clone);
        }
        SUtil.runIntervalForTicks(() -> {
            for (int i = 0; i < 50; ++i) {
                world.playEffect(clone.clone().add(0.0, -0.2, 0.0), Effect.WITCH_MAGIC, (Object)Effect.SPELL.getData());
                world.playEffect(clone, Effect.SPELL, (Object)Effect.SPELL.getData());
                world.playEffect(clone, Effect.FLYING_GLYPH, (Object)Effect.FLYING_GLYPH.getData());
                world.playEffect(clone.clone().add(0.0, -0.2, 0.0), Effect.FLYING_GLYPH, (Object)Effect.FLYING_GLYPH.getData());
                world.playEffect(clone, Effect.WITCH_MAGIC, (Object)Effect.WITCH_MAGIC.getData());
            }
        }, 5L, 28L);
        new BukkitRunnable(){

            public void run() {
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
                world.playEffect(clone, Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 28L);
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

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void setSpawned(long spawned) {
        this.spawned = spawned;
    }

    public void setKilled(long killed) {
        this.killed = killed;
    }

    public void setDied(long died) {
        this.died = died;
    }

    public void setLastKilled(SEntityType lastKilled) {
        this.lastKilled = lastKilled;
    }

    public void setEntity(SEntity entity) {
        this.entity = entity;
    }
}

