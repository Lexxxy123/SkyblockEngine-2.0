package in.godspunky.skyblock.entity.dimoon.abilities;

import org.bukkit.entity.Player;
import in.godspunky.skyblock.entity.dimoon.Dimoon;

public class Void implements Ability {
    @Override
    public void activate(final Player player, final Dimoon dimoon) {
    }

    @Override
    public int getCooldown() {
        return 300;
    }
}
