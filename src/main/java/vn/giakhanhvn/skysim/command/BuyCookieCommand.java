package vn.giakhanhvn.skysim.command;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;

@CommandParameters(description = "", aliases = "bbc", permission = "sse.cc")
public class BuyCookieCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            final int amount = Integer.parseInt(args[0]);
            final PlayerInventory inv = player.getInventory();
            for (int i = 0; i < amount; ++i) {
                final ItemStack stack = SItem.of(SMaterial.HIDDEN_BOOSTER_COOKIE).getStack();
                Sputnik.smartGiveItem(stack, player);
            }
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
