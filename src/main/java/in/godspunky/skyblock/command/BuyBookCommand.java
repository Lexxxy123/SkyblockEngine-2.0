package in.godspunky.skyblock.command;

import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description = "", aliases = "purc", permission = PlayerRank.DEFAULT)
public class BuyBookCommand extends SCommand {
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
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
