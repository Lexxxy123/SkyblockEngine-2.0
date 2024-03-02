package net.hypixel.skyblock.entity.caverns;

import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.event.CreeperIgniteEvent;

public interface CreeperFunction extends EntityFunction {
    default void onCreeperIgnite(final CreeperIgniteEvent e, final SEntity sEntity) {
    }
}
