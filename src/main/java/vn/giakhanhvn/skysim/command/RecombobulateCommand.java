package vn.giakhanhvn.skysim.command;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.GenericItemType;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Recombobulate an item from Spec.", aliases = "recom", permission = "spt.item")
public class RecombobulateCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 0) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) {
            throw new CommandFailException("You don't have anything in your hand!");
        }
        final SItem sItem = SItem.find(stack);
        if (sItem == null) {
            throw new CommandFailException(ChatColor.RED + "You cannot recombobulate that, lol!");
        }
        if (sItem.getType().getStatistics().getType() == GenericItemType.PET) {
            this.send(ChatColor.RED + "You cannot recombobulate that, lol!");
            return;
        }
        sItem.setRecombobulated(!sItem.isRecombobulated());
        this.send(ChatColor.YELLOW + "Your " + sItem.getFullName() + ChatColor.YELLOW + " is no" + (sItem.isRecombobulated() ? "w" : " longer") + " recombobulated.");
    }
}
