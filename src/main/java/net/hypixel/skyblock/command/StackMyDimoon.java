package net.hypixel.skyblock.command;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;

@CommandParameters(description = "", aliases = "smd", permission = "sse.cc")
public class StackMyDimoon extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        int stg = 0;
        Player player = sender.getPlayer();
        ItemStack[] iss = player.getInventory().getContents();
        for (int i = 0; i < player.getInventory().getContents().length; ++i) {
            ItemStack is = iss[i];
            if (null != SItem.find(is) && SMaterial.HIDDEN_DIMOON_FRAG == SItem.find(is).getType()) {
                stg += is.getAmount();
                player.getInventory().setItem(i, null);
            }
        }
        if (0 < stg) {
            ItemStack is2 = SItem.of(SMaterial.HIDDEN_DIMOON_FRAG).getStack();
            is2.setAmount(stg);
            Sputnik.smartGiveItem(is2, player);
            player.sendMessage(Sputnik.trans("&aStacked all your fragments which have been broken before! Have fun!"));
        }
    }
}
