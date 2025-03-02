/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor.miner;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.armor.TickingSet;
import net.hypixel.skyblock.item.armor.miner.MinerBoots;
import net.hypixel.skyblock.item.armor.miner.MinerChestplate;
import net.hypixel.skyblock.item.armor.miner.MinerHelmet;
import net.hypixel.skyblock.item.armor.miner.MinerLeggings;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.user.DoublePlayerStatistic;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.Groups;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MinerSet
implements TickingSet {
    @Override
    public String getName() {
        return "Regeneration";
    }

    @Override
    public String getDescription() {
        return "Regenerates 5% of your max Health every second if you have been out of combat for the last 8 seconds.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return MinerHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return MinerChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return MinerLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return MinerBoots.class;
    }

    @Override
    public void tick(Player owner, SItem helmet, SItem chestplate, SItem leggings, SItem boots, List<AtomicInteger> counters) {
        Region region;
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(owner.getUniqueId());
        DoublePlayerStatistic defense = statistics.getDefense();
        PlayerListener.CombatAction action = PlayerListener.getLastCombatAction(owner);
        counters.get(0).incrementAndGet();
        if ((action == null || action.getTimeStamp() + 8000L <= System.currentTimeMillis() && helmet != null && chestplate != null && leggings != null && boots != null) && counters.get(0).get() >= 2) {
            owner.setHealth(Math.min(owner.getMaxHealth(), owner.getHealth() + owner.getMaxHealth() * 0.05));
            counters.get(0).set(0);
        }
        if ((region = Region.getRegionOfEntity((Entity)owner)) == null) {
            return;
        }
        if (!Groups.DEEP_CAVERNS_REGIONS.contains((Object)region.getType())) {
            return;
        }
        if (helmet != null) {
            defense.add(8, 45.0);
        }
        if (chestplate != null) {
            defense.add(8, 95.0);
        }
        if (leggings != null) {
            defense.add(8, 70.0);
        }
        if (boots != null) {
            defense.add(8, 45.0);
        }
    }
}

