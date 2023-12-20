package in.godspunky.skyblock.skill;

import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class CombatSkill extends Skill {
    public static final CombatSkill INSTANCE;

    @Override
    public String getName() {
        return "Combat";
    }

    @Override
    public String getAlternativeName() {
        return "Warrior";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("Fight mobs and players to earn", "Combat XP!");
    }

    @Override
    public List<String> getLevelUpInformation(final int level, final int lastLevel, final boolean showOld) {
        return Arrays.asList(ChatColor.WHITE + " Deal " + (showOld ? (ChatColor.DARK_GRAY + "" + lastLevel * 4 + "➜") : "") + ChatColor.GREEN + level * 4 + "% " + ChatColor.WHITE + "more damage to mobs.", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + "0.5% " + ChatColor.BLUE + "☣ Crit Chance");
    }

    @Override
    public boolean hasSixtyLevels() {
        return false;
    }

    @Override
    public void onSkillUpdate(final User user, final double previousXP) {
        super.onSkillUpdate(user, previousXP);
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(12);
        statistics.getCritChance().set(12, Double.valueOf(0.005 * getLevel(user.getCombatXP(), false)));
    }

    static {
        INSTANCE = new CombatSkill();
    }
}
