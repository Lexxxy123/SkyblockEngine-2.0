package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.item.SMaterial;

@CommandParameters(description = "", aliases = "purce", permission = "sse.cc")
public class BuyEPetCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            Sputnik.smartGiveItem(SItem.of(SMaterial.HIDDEN_VOIDLINGS_PET).getStack(), player);
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
