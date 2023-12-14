package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.enchantment.EnchantmentType;

@CommandParameters(description = "", aliases = "purc", permission = "sse.cc")
public class BuyBookCommand extends SCommand
{
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            final EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
            if (type == null) {
                this.send(ChatColor.RED + "Something wrong, contact admins!");
                return;
            }
            final int i = Integer.parseInt(args[1]);
            final SItem eBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            eBook.addEnchantment(type, i);
            Sputnik.smartGiveItem(eBook.getStack(), player);
        }
        else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
