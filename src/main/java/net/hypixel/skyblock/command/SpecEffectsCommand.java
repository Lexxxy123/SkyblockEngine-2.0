package net.hypixel.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.util.SUtil;

@CommandParameters(description = "Get your current active potion effects.", aliases = "effect", permission = "spt.item")
public class SpecEffectsCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length > 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
            sender.getUser().clearPotionEffects();
            this.send("Cleared active effects.");
            return;
        }
        this.send("Current active effects:");
        for (final ActivePotionEffect effect : sender.getUser().getEffects()) {
            this.send(" - " + effect.getEffect().getType().getName() + " " + SUtil.toRomanNumeral(effect.getEffect().getLevel()) + ChatColor.GRAY + " (" + effect.getRemainingDisplay() + ")");
        }
    }
}
