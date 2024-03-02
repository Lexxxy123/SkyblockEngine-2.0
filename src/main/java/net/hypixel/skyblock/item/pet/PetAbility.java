package net.hypixel.skyblock.item.pet;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import net.hypixel.skyblock.item.SItem;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(final SItem p0);

    default void onHurt(final EntityDamageByEntityEvent e, final Entity damager) {
    }

    default void onDamage(final EntityDamageByEntityEvent e) {
    }

    default void onZealotAttempt(final AtomicDouble chance) {
    }
}
