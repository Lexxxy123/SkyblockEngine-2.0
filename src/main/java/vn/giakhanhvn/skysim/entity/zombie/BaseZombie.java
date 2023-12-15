package vn.giakhanhvn.skysim.entity.zombie;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import vn.giakhanhvn.skysim.entity.SEntityType;
import vn.giakhanhvn.skysim.slayer.SlayerQuest;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.entity.ZombieStatistics;

public abstract class BaseZombie implements ZombieStatistics, EntityFunction {
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
        if (quest.getType().getName() == "Revenant Horror" || quest.getType().getName() == "Atoned Horror") {
            final Location k = killed.getLocation().clone();
            if (SUtil.random(0, 8) == 0 && quest.getType().getTier() >= 3 && quest.getType().getTier() < 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.REVENANT_SYCOPHANT).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 16) == 0 && quest.getType().getTier() == 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.REVENANT_CHAMPION).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 45) == 0 && quest.getType().getTier() == 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.DEFORMED_REVENANT).setTarget(player), 12L);
            }
            if (SUtil.random(0, 16) == 0 && quest.getType().getTier() == 5) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.ATONED_CHAMPION).setTarget(player), 12L);
            }
            if (SUtil.random(0, 40) == 0 && quest.getType().getTier() == 5) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.ATONED_REVENANT).setTarget(player), 12L);
            }
        }
    }
}
