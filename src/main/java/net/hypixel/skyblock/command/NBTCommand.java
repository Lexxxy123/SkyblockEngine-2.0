package net.hypixel.skyblock.command;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "Gets the NBT of your current item.", permission = "spt.item")
public class NBTCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (0 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack stack = CraftItemStack.asNMSCopy(inv.getItemInHand());
        if (null == inv.getItemInHand()) {
            throw new CommandFailException(ChatColor.RED + "Air do not have NBT kiddo, get something!");
        }
        NBTTagCompound compound = stack.getTag();
        if (null == compound) {
            throw new CommandFailException(ChatColor.RED + "This item does not have any NBT data!");
        }
        this.send(ChatColor.GREEN + "NBT Explorer >");
        for (String key : compound.c()) {
            this.send(ChatColor.YELLOW + key + ChatColor.GREEN + ": " + ChatColor.RESET + compound.get(key).toString().replaceAll("§", "&"));
        }
    }
}
