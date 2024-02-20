package in.godspunky.skyblock.entity.wolf;

import in.godspunky.skyblock.slayer.SlayerQuest;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

import java.util.List;
import java.util.stream.Collectors;

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
        }.runTaskTimer(SkyBlock.getPlugin(), 20L, 20L);
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
