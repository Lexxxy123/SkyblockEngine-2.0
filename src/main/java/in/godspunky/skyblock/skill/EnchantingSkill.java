package in.godspunky.skyblock.skill;

import org.bukkit.ChatColor;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;

import java.util.Arrays;
import java.util.List;

public class EnchantingSkill extends Skill {
    public static final EnchantingSkill INSTANCE;

    @Override
    public String getName() {
        return "Enchanting";
    }

    @Override
    public String getAlternativeName() {
        return "Conjurer";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Enchant items to earn Enchanting", "XP!");
    }

    @Override
    public List<String> getLevelUpInformation(final int level, final int lastLevel, final boolean showOld) {
        return Arrays.asList(ChatColor.WHITE + " Gain " + (showOld ? (ChatColor.DARK_GRAY + "" + lastLevel * 4 + "➜") : "") + ChatColor.GREEN + level * 4 + "% " + ChatColor.WHITE + "more experience orbs", ChatColor.WHITE + " from any source.", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + "0.5% " + ChatColor.RED + "๑ Ability Damage", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + "2 " + ChatColor.AQUA + "✎ Intelligence");
    }

    @Override
    public boolean hasSixtyLevels() {
        return false;
    }

    @Override
    public void onSkillUpdate(final User user, final double previousXP) {
        super.onSkillUpdate(user, previousXP);
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(14);
        statistics.getAbilityDamage().set(14, Double.valueOf(0.5 * getLevel(user.getEnchantXP(), false)));
        statistics.getIntelligence().set(14, Double.valueOf(2 * getLevel(user.getEnchantXP(), false)));
    }

    static {
        INSTANCE = new EnchantingSkill();
    }
}
