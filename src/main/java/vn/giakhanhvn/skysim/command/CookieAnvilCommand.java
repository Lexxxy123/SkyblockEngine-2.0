package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.gui.GUIType;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.user.PlayerUtils;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "av", permission = "sse.cc")
public class CookieAnvilCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (PlayerUtils.getCookieDurationTicks(player) <= 0L) {
            this.send(Sputnik.trans("&cYou need the Cookie Buff active to use this feature!"));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        GUIType.ANVIL.getGUI().open(player);
    }
}
