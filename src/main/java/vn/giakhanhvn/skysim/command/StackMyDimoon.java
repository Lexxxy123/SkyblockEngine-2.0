package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.item.SItem;

@CommandParameters(description = "", aliases = "smd", permission = "sse.cc")
public class StackMyDimoon extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        int stg = 0;
        final Player player = sender.getPlayer();
        final ItemStack[] iss = player.getInventory().getContents();
        for (int i = 0; i < player.getInventory().getContents().length; ++i) {
            final ItemStack is = iss[i];
            if (SItem.find(is) != null && SItem.find(is).getType() == SMaterial.HIDDEN_DIMOON_FRAG) {
                stg += is.getAmount();
                player.getInventory().setItem(i, null);
            }
        }
        if (stg > 0) {
            final ItemStack is2 = SItem.of(SMaterial.HIDDEN_DIMOON_FRAG).getStack();
            is2.setAmount(stg);
            Sputnik.smartGiveItem(is2, player);
            player.sendMessage(Sputnik.trans("&aStacked all your fragments which have been broken before! Have fun!"));
        }
    }
}
