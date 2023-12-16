package vn.giakhanhvn.skysim.dimoon.abilities;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.dimoon.Dimoon;

public interface Ability {
    void activate(final Player p0, final Dimoon p1);

    int getCooldown();
}
