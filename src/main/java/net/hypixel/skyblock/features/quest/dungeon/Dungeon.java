/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.features.quest.dungeon;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.quest.dungeon.SadanBoss;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;

public class Dungeon
extends QuestLine {
    public Dungeon() {
        super("dungeon_run", "Explore Dungeons", new SadanBoss());
    }

    @Override
    protected boolean hasCompletionMessage() {
        return true;
    }

    @Override
    protected List<String> getRewards() {
        return Arrays.asList(ChatColor.DARK_GRAY + "+" + ChatColor.GOLD + "1000 coins", ChatColor.DARK_GRAY + "+" + ChatColor.DARK_AQUA + "15 Combat Exp", ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.RED + "Diamond Sadan Trophy", ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.LIGHT_PURPLE + "Golden Sadan Trophy", ChatColor.GOLD + "" + ChatColor.BOLD + "UNLOCKED" + ChatColor.RESET + " " + ChatColor.AQUA + "Soul Whip");
    }

    @Override
    protected void reward(User player) {
        player.addCoins(1000L);
        Skill.reward(CombatSkill.INSTANCE, 15.0, player.toBukkitPlayer());
    }
}

