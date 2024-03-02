package net.hypixel.skyblock.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import net.hypixel.skyblock.features.skill.Skill;

@CommandParameters(description = "Shows your skills.", aliases = "skill", permission = "spt.skills")
public class SkillsCommand extends SCommand {
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
