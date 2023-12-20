package in.godspunky.skyblock.command;

import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "", aliases = "bcb", permission = PlayerRank.ADMIN)
public class BuyItemCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            final int amount = Integer.parseInt(args[0]);
            final PlayerInventory inv = player.getInventory();
            for (int i = 0; i < amount; ++i) {
                final ItemStack stack = SItem.of(SMaterial.HIDDEN_COMPRESSED_BITS).getStack();
                Sputnik.smartGiveItem(stack, player);
            }
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
