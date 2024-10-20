/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.features.skill;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;

public class FarmingSkill
extends Skill {
    public static final FarmingSkill INSTANCE = new FarmingSkill();

    @Override
    public String getName() {
        return "Farming";
    }

    @Override
    public String getAlternativeName() {
        return "Farmhand";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Harvest crops and shear sheep to", "earn Farming XP!");
    }

    public double getDoubleDropChance(int level) {
        return (double)level * 4.0 / 100.0;
    }

    public double getHealth(int level) {
        int health = level * 2;
        if (level >= 15) {
            health += level - 14;
        }
        if (level >= 20) {
            health += level - 19;
        }
        if (level >= 26) {
            health += level - 25;
        }
        return health;
    }

    @Override
    public List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld) {
        String dropChance = (showOld ? ChatColor.DARK_GRAY + "" + lastLevel * 4 + "\u279c" : "") + ChatColor.GREEN + level * 4;
        int healthPlus = 2;
        if (level >= 15) {
            healthPlus = 3;
        }
        if (level >= 20) {
            healthPlus = 4;
        }
        if (level >= 26) {
            healthPlus = 5;
        }
        return Arrays.asList(ChatColor.WHITE + " Grants " + dropChance + "%" + ChatColor.WHITE + " chance", ChatColor.WHITE + " to drop 2x crops.", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + healthPlus + " " + ChatColor.RED + "\u2764 Health");
    }

    @Override
    public boolean hasSixtyLevels() {
        return true;
    }

    @Override
    public void onSkillUpdate(User user, double previousXP) {
        super.onSkillUpdate(user, previousXP);
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(10);
        statistics.getMaxHealth().set(10, this.getHealth(FarmingSkill.getLevel(user.getSkillXP(this), this.hasSixtyLevels())));
    }
}

