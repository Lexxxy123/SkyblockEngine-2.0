package net.hypixel.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;

@CommandParameters(description = "", aliases = "bbc", permission = "sse.cc")
public class BuyCookieCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player.isOp()) {
            int amount = Integer.parseInt(args[0]);
            PlayerInventory inv = player.getInventory();
            for (int i = 0; i < amount; ++i) {
                ItemStack stack = SItem.of(SMaterial.HIDDEN_BOOSTER_COOKIE).getStack();
                Sputnik.smartGiveItem(stack, player);
            }
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
