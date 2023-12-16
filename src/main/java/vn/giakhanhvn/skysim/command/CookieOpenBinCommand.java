package vn.giakhanhvn.skysim.command;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.gui.GUIType;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.util.Sputnik;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "bin", permission = "sse.cc")
public class CookieOpenBinCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
            this.send(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        GUIType.TRASH.getGUI().open(player);
    }
}
