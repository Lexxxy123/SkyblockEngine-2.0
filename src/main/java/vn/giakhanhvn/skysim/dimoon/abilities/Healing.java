package vn.giakhanhvn.skysim.dimoon.abilities;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.dimoon.Dimoon;

public class Healing implements Ability {
    @Override
    public void activate(final Player player, final Dimoon dimoon) {
        if (dimoon.getHealth() < 5000) {
            dimoon.heal(50);
        }
    }

    @Override
    public int getCooldown() {
        return 1;
    }
}
