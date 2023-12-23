package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description = "Recombobulate an item from Spec.", aliases = "recom", permission = PlayerRank.ADMIN)
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
