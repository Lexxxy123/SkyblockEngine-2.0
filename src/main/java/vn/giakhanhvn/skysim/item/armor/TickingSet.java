package vn.giakhanhvn.skysim.item.armor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;

public interface TickingSet extends ArmorSet {
    void tick(final Player p0, final SItem p1, final SItem p2, final SItem p3, final SItem p4, final List<AtomicInteger> p5);
}
