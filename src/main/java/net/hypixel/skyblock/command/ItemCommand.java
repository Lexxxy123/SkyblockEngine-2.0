package net.hypixel.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;

@CommandParameters(description = "Gives an item from Spec.", aliases = "sitem,specitem", permission = "spt.item")
public class ItemCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (!player.isOp()) {
            return;
        }
        final SMaterial material = SMaterial.getMaterial(args[0]);
        if (material == null) {
            throw new CommandFailException(ChatColor.RED + "Invalid material! That's item don't even exist!");
        }
        final PlayerInventory inv = player.getInventory();
        if (inv.firstEmpty() == -1) {
            throw new CommandFailException(ChatColor.RED + "Not enough Inventory space, clean it up buddy.");
        }
        byte variant = 0;
        if (args.length == 2) {
            variant = Byte.parseByte(args[1]);
        }
        inv.setItem(inv.firstEmpty(), SItem.of(material, variant).getStack());
        this.send(ChatColor.GREEN + "You have received " + material.getDisplayName(variant));
    }
}
