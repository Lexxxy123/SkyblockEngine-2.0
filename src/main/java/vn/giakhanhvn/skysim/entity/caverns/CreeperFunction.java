package vn.giakhanhvn.skysim.entity.caverns;

import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.event.CreeperIgniteEvent;
import vn.giakhanhvn.skysim.entity.EntityFunction;

public interface CreeperFunction extends EntityFunction
{
    default void onCreeperIgnite(final CreeperIgniteEvent e, final SEntity sEntity) {
    }
}
