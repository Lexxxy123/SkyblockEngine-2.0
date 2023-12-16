package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.SItem;

@CommandParameters(description = "Modifies the rarity of an item.", aliases = "rar", permission = "spt.item")
public class SpecRarityCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length > 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (!player.isOp()) {
            return;
        }
        final ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) {
            throw new CommandFailException(ChatColor.RED + "You don't have anything in your hand!");
        }
        final SItem sItem = SItem.find(stack);
        if (sItem == null) {
            throw new CommandFailException(ChatColor.RED + "That item is not executable!");
        }
        if (args.length == 0) {
            this.send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " is " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ".");
            return;
        }
        final Rarity prev = sItem.getRarity();
        final String s = args[0];
        switch (s) {
            case "up":
            case "upgrade":
                sItem.upgradeRarity();
                this.send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " has been upgraded. (" + prev.getDisplay() + ChatColor.GRAY + " ➜ " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
                return;
            case "down":
            case "downgrade":
                sItem.downgradeRarity();
                this.send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + " has been downgraded. (" + prev.getDisplay() + ChatColor.GRAY + " ➜ " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
                return;
            default: {
                final Rarity chosen = Rarity.getRarity(args[0]);
                if (chosen == null) {
                    throw new CommandFailException(ChatColor.RED + "That rarity does not exist, sucks to be you!");
                }
                sItem.setRarity(chosen);
                this.send("Your " + sItem.getType().getDisplayName(sItem.getVariant()) + ChatColor.GRAY + "'s rarity has been modified. (" + prev.getDisplay() + ChatColor.GRAY + " ➜ " + sItem.getRarity().getDisplay() + ChatColor.GRAY + ")");
            }
        }
    }
}
