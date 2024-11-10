/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.entity.den;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.EntityStatistics;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseSpider
implements EntityStatistics,
EntityFunction {
    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        if (!(damager instanceof Player)) {
            return;
        }
        Player player = (Player)damager;
        User user = User.getUser(player.getUniqueId());
        SlayerQuest quest = user.getSlayerQuest();
        if (quest == null) {
            return;
        }
        if (quest.getSpawned() != 0L) {
            return;
        }
        if (quest.getType().getName() == "Tarantula Broodfather") {
            Location k = killed.getLocation().clone();
            if (SUtil.random(0, 10) == 0 && quest.getType().getTier() >= 3 && quest.getType().getTier() < 4) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.TARANTULA_VERMIN, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (SUtil.random(0, 18) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.TARANTULA_BEAST, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (SUtil.random(0, 50) == 0 && quest.getType().getTier() >= 4) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.MUTANT_TARANTULA, new Object[0]).setTarget((LivingEntity)player), 12L);
            }
        }
    }

    @Override
    public List<EntityDrop> drops() {
        return Arrays.asList(new EntityDrop(SMaterial.STRING, EntityDropType.GUARANTEED, 1.0), new EntityDrop(SMaterial.SPIDER_EYE, EntityDropType.OCCASIONAL, 0.5));
    }
}

