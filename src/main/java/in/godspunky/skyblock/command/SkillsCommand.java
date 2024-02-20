package in.godspunky.skyblock.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import in.godspunky.skyblock.skill.Skill;

@CommandParameters(description = "Shows your skills.", aliases = "skill", permission = "spt.skills")
public class SkillsCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 0) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        this.send(ChatColor.AQUA + "Skills:");
        for (final Skill skill : Skill.getSkills()) {
            this.send(" - " + skill.getName() + ": LVL " + Skill.getLevel(sender.getUser().getSkillXP(skill), skill.hasSixtyLevels()) + ", " + sender.getUser().getSkillXP(skill) + " XP");
        }
    }
}
