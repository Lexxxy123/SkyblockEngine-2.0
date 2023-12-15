package vn.giakhanhvn.skysim.command;

import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Change the material data of an item.", aliases = "mdata,matdata,md", permission = "spt.item")
public class MaterialDataCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final short data = Short.parseShort(args[0]);
        final ItemStack stack = player.getItemInHand();
        if (stack == null) {
            throw new CommandFailException(ChatColor.RED + "You are not holding anything!");
        }
        stack.setDurability(data);
        this.send("This item's material data value has been updated to be " + data + ".");
    }
}
