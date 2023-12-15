package vn.giakhanhvn.skysim.entity.wolf;

import org.bukkit.Location;
import vn.giakhanhvn.skysim.entity.SEntityType;
import vn.giakhanhvn.skysim.slayer.SlayerQuest;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import vn.giakhanhvn.skysim.util.SUtil;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bukkit.GameMode;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.entity.SEntity;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.entity.EntityFunction;

public abstract class BaseWolf implements WolfStatistics, EntityFunction {
    private LivingEntity target;

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    return;
                }
                if (BaseWolf.this.target == null || BaseWolf.this.target.isDead()) {
                    BaseWolf.this.target = null;
                }
                if (BaseWolf.this.target != null) {
                    return;
                }
                Player found = (Player) SUtil.getRandom((List) entity.getNearbyEntities(16.0D, 4.0D, 16.0D).stream().filter((e) -> {
                    return e instanceof Player && (((Player) e).getGameMode() == GameMode.SURVIVAL || ((Player) e).getGameMode() == GameMode.ADVENTURE);
                }).collect(Collectors.toList()));
                BaseWolf.this.target = found;
                ((Wolf) entity).setTarget(BaseWolf.this.target);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 20L, 20L);
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        if (!(damager instanceof Player)) {
            return;
        }
        final Player player = (Player) damager;
        final User user = User.getUser(player.getUniqueId());
        final SlayerQuest quest = user.getSlayerQuest();
        if (quest == null) {
            return;
        }
        if (quest.getSpawned() != 0L) {
            return;
        }
        if (quest.getType().getName() == "Sven Packmaster") {
            final Location k = killed.getLocation().clone();
            if (SUtil.random(0, 8) == 0 && quest.getType().getTier() >= 3 && quest.getType().getTier() < 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.SVEN_FOLLOWER).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 12) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.PACK_ENFORCER).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 25) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.SVEN_ALPHA).setTarget(player), 12L);
            }
        }
    }
}
