package in.godspunky.skyblock.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.Sputnik;

@CommandParameters(description = "", aliases = "purce", permission = "sse.cc")
public class BuyEPetCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player.isOp()) {
            Sputnik.smartGiveItem(SItem.of(SMaterial.HIDDEN_VOIDLINGS_PET).getStack(), player);
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}
