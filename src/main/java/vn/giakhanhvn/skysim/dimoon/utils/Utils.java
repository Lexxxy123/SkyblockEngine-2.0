package vn.giakhanhvn.skysim.dimoon.utils;

import java.util.Iterator;

import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.dimoon.Dimoon;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.ChatColor;

public class Utils {
    public static String format(final String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void bossMessage(final String message) {
        if (SkySimEngine.getPlugin().dimoon == null) {
            return;
        }
        for (final Player p : SkySimEngine.getPlugin().dimoon.getEntity().getWorld().getPlayers()) {
            p.playSound(p.getLocation(), Sound.WITHER_IDLE, 0.5f, 1.0f);
            p.sendMessage(format("&4[BOSS] Dimoon&f: " + message));
        }
        Dimoon.a.setCustomNameVisible(true);
        Dimoon.a.setCustomName(Sputnik.trans("&f&l" + message));
        SUtil.delay(() -> {
            if (Dimoon.a.getCustomName().contains(message)) {
                Dimoon.a.setCustomNameVisible(false);
                Dimoon.a.setCustomName(Sputnik.trans("&f&l"));
            }
        }, 40L);
    }

    public static String commaInt(final int num) {
        String str = String.valueOf(num);
        for (int i = str.length() - 3; i > 0; i -= 3) {
            str = str.substring(0, i) + "," + str.substring(i);
        }
        return str;
    }
}
