package in.godspunky.skyblock.dimoon.abilities;

import in.godspunky.skyblock.dimoon.Dimoon;
import org.bukkit.entity.Player;

public class Void implements Ability {
    @Override
    public void activate(final Player player, final Dimoon dimoon) {
    }

    @Override
    public int getCooldown() {
        return 300;
    }
}
