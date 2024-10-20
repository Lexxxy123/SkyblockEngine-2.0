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

public class CombatSkill
extends Skill {
    public static final CombatSkill INSTANCE = new CombatSkill();

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
    public List<String> getLevelUpInformation(int level, int lastLevel, boolean showOld) {
        return Arrays.asList(ChatColor.WHITE + " Deal " + (showOld ? ChatColor.DARK_GRAY + "" + lastLevel * 4 + "\u279c" : "") + ChatColor.GREEN + level * 4 + "% " + ChatColor.WHITE + "more damage to mobs.", ChatColor.DARK_GRAY + "+" + ChatColor.GREEN + "0.5% " + ChatColor.BLUE + "\u2623 Crit Chance");
    }

    @Override
    public boolean hasSixtyLevels() {
        return false;
    }

    @Override
    public void onSkillUpdate(User user, double previousXP) {
        super.onSkillUpdate(user, previousXP);
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(12);
        statistics.getCritChance().set(12, 0.005 * (double)CombatSkill.getLevel(user.getCombatXP(), false));
    }
}

