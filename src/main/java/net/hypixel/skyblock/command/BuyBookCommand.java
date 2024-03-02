package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;

@CommandParameters(description = "", aliases = "purc", permission = "sse.cc")
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
