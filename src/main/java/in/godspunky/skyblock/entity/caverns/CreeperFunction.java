package in.godspunky.skyblock.entity.caverns;

import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.event.CreeperIgniteEvent;

public interface CreeperFunction extends EntityFunction {
    default void onCreeperIgnite(final CreeperIgniteEvent e, final SEntity sEntity) {
    }
}
