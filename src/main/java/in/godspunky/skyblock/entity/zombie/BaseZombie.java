package in.godspunky.skyblock.entity.zombie;

import in.godspunky.skyblock.slayer.SlayerQuest;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.entity.ZombieStatistics;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;

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
