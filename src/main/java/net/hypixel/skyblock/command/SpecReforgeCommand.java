package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.reforge.ReforgeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.hypixel.skyblock.item.SItem;

@CommandParameters(description = "Reforge an item from Spec.", aliases = "sref", permission = PlayerRank.ADMIN)
public class SpecReforgeCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (1 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (null == stack) {
            throw new CommandFailException(ChatColor.RED + "You don't have anything in your hand!");
        }
        SItem sItem = SItem.find(stack);
        if (null == sItem) {
            throw new CommandFailException(ChatColor.RED + "That item is un-reforgeable!");
        }
        Reforge reforge = ReforgeType.getReforgeType(args[0]).getReforge();
        sItem.setReforge(reforge);
        this.send(ChatColor.GREEN + "Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + reforge.getName() + " on it.");
    }
}
