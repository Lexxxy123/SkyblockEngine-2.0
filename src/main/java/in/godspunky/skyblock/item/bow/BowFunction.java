package in.godspunky.skyblock.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.SItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;

public interface BowFunction extends MaterialFunction {
    default void onBowShoot(final SItem bow, final EntityShootBowEvent e) {
    }

    default void onBowHit(final Entity hit, final Player shooter, final Arrow arrow, final SItem weapon, final AtomicDouble damage) {
    }
}
