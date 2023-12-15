package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.gui.GUIType;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.user.PlayerUtils;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "auh", permission = "sse.cc")
public class CookieAHCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 0.0f, 2.0f);
            this.send(Sputnik.trans("&eYou need the &dCookie Buff &eto use this command!"));
            this.send(Sputnik.trans("&eObtain a &6Booster Cookie &efrom the community shop in the hub!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        GUIType.AUCTION_HOUSE.getGUI().open(player);
    }
}
