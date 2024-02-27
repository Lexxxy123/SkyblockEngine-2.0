package in.godspunky.skyblock.features.skill;

import org.bukkit.ChatColor;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;

import java.util.Arrays;
import java.util.List;

public class FarmingSkill extends Skill {
    public static final FarmingSkill INSTANCE;

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

    public double getDoubleDropChance(final int level) {
        return level * 4.0 / 100.0;
    }

    public double getHealth(final int level) {
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
    public List<String> getLevelUpInformation(final int level, final int lastLevel, final boolean showOld) {
        final String dropChance = (showOld ? (ChatColor.DARK_GRAY + "" + lastLevel * 4 + "➜") : "") + ChatColor.GREEN + level * 4;
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
        return Arrays.asList(ChatColor.WHITE + " Grants " + dropChance + "%" + ChatColor.WHITE + " chance", ChatColor.WHITE + " to drop 2x crops.", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + healthPlus + " " + ChatColor.RED + "❤ Health");
    }

    @Override
    public boolean hasSixtyLevels() {
        return true;
    }

    @Override
    public void onSkillUpdate(final User user, final double previousXP) {
        super.onSkillUpdate(user, previousXP);
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(10);
        statistics.getMaxHealth().set(10, Double.valueOf(this.getHealth(getLevel(user.getSkillXP(this), this.hasSixtyLevels()))));
    }

    static {
        INSTANCE = new FarmingSkill();
    }
}
