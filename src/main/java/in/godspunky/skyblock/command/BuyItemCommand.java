package in.godspunky.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.Sputnik;

@CommandParameters(description = "", aliases = "bcb", permission = "sse.cc")
public class BuyItemCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player.isOp()) {
            int amount = Integer.parseInt(args[0]);
            PlayerInventory inv = player.getInventory();
            for (int i = 0; i < amount; ++i) {
                ItemStack stack = SItem.of(SMaterial.HIDDEN_COMPRESSED_BITS).getStack();
                Sputnik.smartGiveItem(stack, player);
            }
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
