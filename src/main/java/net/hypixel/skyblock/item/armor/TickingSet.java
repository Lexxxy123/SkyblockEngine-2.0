package net.hypixel.skyblock.item.armor;

import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.SItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface TickingSet extends ArmorSet {
    void tick(final Player p0, final SItem p1, final SItem p2, final SItem p3, final SItem p4, final List<AtomicInteger> p5);
}
