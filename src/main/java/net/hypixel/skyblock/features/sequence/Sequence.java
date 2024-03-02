package net.hypixel.skyblock.features.sequence;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Sequence {
    void play(final Location p0);

    void play(final Entity p0);
}
