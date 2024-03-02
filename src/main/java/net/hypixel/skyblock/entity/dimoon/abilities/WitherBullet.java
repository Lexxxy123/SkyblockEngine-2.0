package net.hypixel.skyblock.entity.dimoon.abilities;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.dimoon.Dimoon;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;

public class WitherBullet implements Ability {
    @Override
    public void activate(final Player player, final Dimoon dimoon) {
        final Entity entity = dimoon.getEntity();
        player.getWorld().playSound(dimoon.getEntity().getLocation(), Sound.WITHER_SHOOT, 5.0f, 1.0f);
        final World world = entity.getWorld();
        final Vector[] velocity = {player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize()};
        new BukkitRunnable() {
            private final Location particleLocation = entity.getLocation().add(0.0, 2.0, 0.0).clone();
            double multiplier = 4.0;

            public void run() {
                this.particleLocation.add(velocity[0]);
                velocity[0] = player.getLocation().toVector().subtract(this.particleLocation.toVector()).normalize().multiply(this.multiplier);
                if (velocity[0].length() < 1.0 || this.multiplier == 1.0) {
                    this.cancel();
                    return;
                }
                this.multiplier -= 0.05;
                world.spigot().playEffect(this.particleLocation, Effect.LARGE_SMOKE, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                world.spigot().playEffect(this.particleLocation, Effect.LARGE_SMOKE, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                world.spigot().playEffect(this.particleLocation, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                world.spigot().playEffect(this.particleLocation, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                if (SkyBlock.getPlugin().dimoon == null) {
                    this.cancel();
                    return;
                }
                if (this.particleLocation.distance(player.getEyeLocation()) < 1.5 || this.particleLocation.distance(player.getLocation()) < 1.5) {
                    player.getWorld().playEffect(this.particleLocation, Effect.EXPLOSION_HUGE, 0);
                    player.getWorld().playSound(this.particleLocation, Sound.EXPLODE, 1.0f, 1.0f);
                    User.getUser(player.getUniqueId()).send("&7Wither's Bullet have hit you for &c" + SUtil.commaify(player.getMaxHealth() * 2.0 / 100.0) + " &7true damage.");
                    User.getUser(player.getUniqueId()).damage(player.getMaxHealth() * 2.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, SkyBlock.getPlugin().dimoon.getEntity());
                    this.cancel();
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public int getCooldown() {
        return 10;
    }
}
