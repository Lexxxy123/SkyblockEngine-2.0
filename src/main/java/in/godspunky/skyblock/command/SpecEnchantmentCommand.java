package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import in.godspunky.skyblock.enchantment.EnchantmentType;

@CommandParameters(description = "Adds an enchantment from Spec to the specified item.", aliases = "sench", permission = PlayerRank.ADMIN)
public class SpecEnchantmentCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 2) {
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
            throw new CommandFailException(ChatColor.RED + "That item is not executable!");
        }
        final EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
        if (type == null) {
            throw new CommandFailException(ChatColor.RED + "Invalid enchantment type!");
        }
        final int i = Integer.parseInt(args[1]);
        if (i <= 0) {
            this.send(ChatColor.RED + "Are you serious? If you want to remove enchantments, use /re");
            return;
        }
        if (i > 10000000) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i > 50 && type == EnchantmentType.CHIMERA) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i > 100 && type == EnchantmentType.VICIOUS) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i > 100 && type == EnchantmentType.LUCKINESS) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i > 10 && type == EnchantmentType.LEGION) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        sItem.addEnchantment(type, i);
        this.send(ChatColor.GREEN + "Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + type.getName() + " " + i + " on it.");
    }
}
