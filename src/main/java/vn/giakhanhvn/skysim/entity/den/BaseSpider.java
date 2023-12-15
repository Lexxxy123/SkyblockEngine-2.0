package vn.giakhanhvn.skysim.entity.den;

import java.util.Arrays;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.entity.EntityDrop;

import java.util.List;

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
import vn.giakhanhvn.skysim.entity.EntityStatistics;

public abstract class BaseSpider implements EntityStatistics, EntityFunction {
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
        if (quest.getType().getName() == "Tarantula Broodfather") {
            final Location k = killed.getLocation().clone();
            if (SUtil.random(0, 10) == 0 && quest.getType().getTier() >= 3 && quest.getType().getTier() < 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.TARANTULA_VERMIN).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 18) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.TARANTULA_BEAST).setTarget(player), 12L);
                return;
            }
            if (SUtil.random(0, 50) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, player);
                SUtil.delay(() -> new SEntity(k, SEntityType.MUTANT_TARANTULA).setTarget(player), 12L);
            }
        }
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(SMaterial.STRING, EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.SPIDER_EYE, EntityDropType.OCCASIONAL, 0.5));
    }
}
