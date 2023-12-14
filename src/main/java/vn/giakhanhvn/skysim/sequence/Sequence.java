package vn.giakhanhvn.skysim.sequence;

import org.bukkit.entity.Entity;
import org.bukkit.Location;

public interface Sequence
{
    void play(final Location p0);
    
    void play(final Entity p0);
}
