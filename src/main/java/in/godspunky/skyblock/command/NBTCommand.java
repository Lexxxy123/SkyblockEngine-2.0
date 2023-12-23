package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "Gets the NBT of your current item.", permission = PlayerRank.ADMIN)
public class NBTCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 0) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final PlayerInventory inv = player.getInventory();
        final ItemStack stack = CraftItemStack.asNMSCopy(inv.getItemInHand());
        if (inv.getItemInHand() == null) {
            throw new CommandFailException(ChatColor.RED + "Air do not have NBT kiddo, get something!");
        }
        final NBTTagCompound compound = stack.getTag();
        if (compound == null) {
            throw new CommandFailException(ChatColor.RED + "This item does not have any NBT data!");
        }
        this.send(ChatColor.GREEN + "NBT Explorer >");
        for (final String key : compound.c()) {
            this.send(ChatColor.YELLOW + key + ChatColor.GREEN + ": " + ChatColor.RESET + compound.get(key).toString().replaceAll("§", "&"));
        }
    }
}
