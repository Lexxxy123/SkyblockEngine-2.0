package net.hypixel.skyblock.features.quest.dungeon;

import net.hypixel.skyblock.features.quest.Objective;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class Dungeon extends QuestLine {
    public Dungeon() {
        super("dungeon_run","Explore Dungeons", new TalkToMort(), new SadanBoss());
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    @Override
    protected List<String> getRewards() {
        return Arrays.asList(
                ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + "100 Bits",
                ChatColor.DARK_GRAY + "+" + ChatColor.DARK_AQUA + "15 Combat Exp",
                ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.RED + "Diamond Sadan Trophy",
                ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.LIGHT_PURPLE + "Golden Sadan Trophy",
                ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.AQUA + "Soul Whip"
        );
    }

    protected void reward(User player) {
        player.addBits(100);
        Skill.reward(CombatSkill.INSTANCE, 15, player.toBukkitPlayer());
    }
}
