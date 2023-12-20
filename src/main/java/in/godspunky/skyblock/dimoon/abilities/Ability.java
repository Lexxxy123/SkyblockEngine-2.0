package in.godspunky.skyblock.dimoon.abilities;

import in.godspunky.skyblock.dimoon.Dimoon;
import org.bukkit.entity.Player;

public interface Ability {
    void activate(final Player p0, final Dimoon p1);

    int getCooldown();
}
