package in.godspunky.skyblock.item.accessory;

import com.google.common.util.concurrent.AtomicDouble;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface AccessoryFunction extends MaterialFunction {
    default void onDamageInInventory(final SItem weapon, final Player damager, final Entity damaged, final SItem accessory, final AtomicDouble damage) {
    }

    default void update(final SItem instance, final Player player, final int accessorySlot) {
    }
}
