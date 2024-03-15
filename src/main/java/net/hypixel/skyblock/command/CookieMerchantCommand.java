package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.Sputnik;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "fm", permission = PlayerRank.DEFAULT)
public class CookieMerchantCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (0L >= PlayerUtils.getCookieDurationTicks(player)) {
            this.send(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        GUIType.FARM_MERCHANT.getGUI().open(player);
    }
}
