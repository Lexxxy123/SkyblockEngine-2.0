package net.hypixel.skyblock.entity.dimoon.abilities;

import org.bukkit.entity.Player;
import net.hypixel.skyblock.entity.dimoon.Dimoon;

public interface Ability {
    void activate(final Player p0, final Dimoon p1);

    int getCooldown();
}
