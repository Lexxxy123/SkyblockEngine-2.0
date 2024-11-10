/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.entity.end;

import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.end.EndermanStatistics;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public abstract class BaseEnderman
implements EndermanStatistics,
EntityFunction {
    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        if (!(damager instanceof Player)) {
            return;
        }
        Player player = (Player)damager;
        User user = User.getUser(player.getUniqueId());
        SlayerQuest quest = user.getSlayerQuest();
        if (null == quest) {
            return;
        }
        if (0L != quest.getSpawned()) {
            return;
        }
        if ("Voidgloom Seraph" == quest.getType().getName()) {
            Location k = killed.getLocation().clone();
            if (0 == SUtil.random(0, 8) && 3 == quest.getType().getTier()) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.VOIDLING_DEVOTEE, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (0 == SUtil.random(0, 16) && 4 == quest.getType().getTier()) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.VOIDLING_RADICAL, new Object[0]).setTarget((LivingEntity)player), 12L);
                return;
            }
            if (0 == SUtil.random(0, 45) && 4 == quest.getType().getTier()) {
                SlayerQuest.playMinibossSpawn(k, (Entity)player);
                SUtil.delay(() -> new SEntity(k, SEntityType.VOIDCRAZED_MANIAC, new Object[0]).setTarget((LivingEntity)player), 12L);
            }
        }
    }
}

