package in.godspunky.skyblock.item.armor;

import in.godspunky.skyblock.item.SItem;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface TickingSet extends ArmorSet {
    void tick(final Player p0, final SItem p1, final SItem p2, final SItem p3, final SItem p4, final List<AtomicInteger> p5);
}
