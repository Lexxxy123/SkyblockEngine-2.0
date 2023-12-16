package vn.giakhanhvn.skysim.dimoon.abilities;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.dimoon.Dimoon;

public class Void implements Ability {
    @Override
    public void activate(final Player player, final Dimoon dimoon) {
    }

    @Override
    public int getCooldown() {
        return 300;
    }
}
