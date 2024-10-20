/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.features.sequence;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.sequence.Sequence;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationSequence
implements Sequence {
    private final List<DelayedAnimation> animations;

    public AnimationSequence(DelayedAnimation ... animations) {
        this.animations = Arrays.asList(animations);
    }

    public void append(DelayedAnimation sound) {
        this.animations.add(sound);
    }

    @Override
    public void play(Location location) {
        for (DelayedAnimation animation : this.animations) {
            animation.play(location);
        }
    }

    @Override
    public void play(Entity entity) {
        for (DelayedAnimation animation : this.animations) {
            animation.play(entity);
        }
    }

    public static class DelayedAnimation {
        private final Effect effect;
        private final int data;
        private final long delay;
        private final float speed;
        private final int particleCount;

        public DelayedAnimation(Effect effect, int data, long delay, float speed, int particleCount) {
            this.effect = effect;
            this.data = data;
            this.delay = delay;
            this.speed = speed;
            this.particleCount = particleCount;
        }

        public void play(final Location location) {
            new BukkitRunnable(){

                public void run() {
                    location.getWorld().spigot().playEffect(location, effect, 1, data, 0.0f, 0.0f, 0.0f, speed, particleCount, 16);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), this.delay);
        }

        public void play(Entity entity) {
            this.play(entity.getLocation());
        }
    }
}

