/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.entity.zombie;

import java.util.Objects;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.ZombieStatistics;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseZombie
implements ZombieStatistics,
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
        if (Objects.equals(quest.getType().getName(), "Revenant Horror") || Objects.equals(quest.getType().getName(), "Atoned Horror")) {
            Location k2 = killed.getLocation().clone();
            if (SUtil.random(0, 8) == 0 && quest.getType().getTier() == 3) {
                SlayerQuest.playMinibossSpawn(k2, (Entity)player);
                SUtil.delay(() -> new SEntity(k2, SEntityType.REVENANT_SYCOPHANT, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (SUtil.random(0, 16) == 0 && quest.getType().getTier() == 4) {
                SlayerQuest.playMinibossSpawn(k2, (Entity)player);
                SUtil.delay(() -> new SEntity(k2, SEntityType.REVENANT_CHAMPION, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (SUtil.random(0, 45) == 0 && quest.getType().getTier() == 4) {
                SlayerQuest.playMinibossSpawn(k2, (Entity)player);
                SUtil.delay(() -> new SEntity(k2, SEntityType.DEFORMED_REVENANT, new Object[0]).setTarget((LivingEntity)player), 12L);
            }
            if (SUtil.random(0, 16) == 0 && quest.getType().getTier() == 5) {
                SlayerQuest.playMinibossSpawn(k2, (Entity)player);
                SUtil.delay(() -> new SEntity(k2, SEntityType.ATONED_CHAMPION, new Object[0]).setTarget((LivingEntity)player), 12L);
            }
            if (SUtil.random(0, 40) == 0 && quest.getType().getTier() == 5) {
                SlayerQuest.playMinibossSpawn(k2, (Entity)player);
                SUtil.delay(() -> new SEntity(k2, SEntityType.ATONED_REVENANT, new Object[0]).setTarget((LivingEntity)player), 12L);
            }
        }
    }
}

