package in.godspunky.skyblock.dimoon.abilities;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.dimoon.Dimoon;

public interface Ability {
    void activate(final Player p0, final Dimoon p1);

    int getCooldown();
}
