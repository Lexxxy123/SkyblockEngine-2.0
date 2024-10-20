/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.skill.Skill;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description="Shows your skills.", aliases="skill", permission=PlayerRank.DEFAULT)
public class SkillsCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (0 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        this.send(ChatColor.AQUA + "Skills:");
        for (Skill skill : Skill.getSkills()) {
            this.send(" - " + skill.getName() + ": LVL " + Skill.getLevel(sender.getUser().getSkillXP(skill), skill.hasSixtyLevels()) + ", " + sender.getUser().getSkillXP(skill) + " XP");
        }
    }
}

