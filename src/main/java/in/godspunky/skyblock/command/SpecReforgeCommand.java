package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.reforge.Reforge;
import in.godspunky.skyblock.reforge.ReforgeType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Reforge an item from Spec.", aliases = "sref", permission = PlayerRank.ADMIN)
public class SpecReforgeCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) {
            throw new CommandFailException(ChatColor.RED + "You don't have anything in your hand!");
        }
        final SItem sItem = SItem.find(stack);
        if (sItem == null) {
            throw new CommandFailException(ChatColor.RED + "That item is un-reforgeable!");
        }
        final Reforge reforge = ReforgeType.getReforgeType(args[0]).getReforge();
        sItem.setReforge(reforge);
        this.send(ChatColor.GREEN + "Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + reforge.getName() + " on it.");
    }
}
