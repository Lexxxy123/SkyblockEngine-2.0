package vn.giakhanhvn.skysim.dimoon.abilities;

import vn.giakhanhvn.skysim.dimoon.Dimoon;
import org.bukkit.entity.Player;

public interface Ability
{
    void activate(final Player p0, final Dimoon p1);
    
    int getCooldown();
}
