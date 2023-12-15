package vn.giakhanhvn.skysim.entity.wolf;

import vn.giakhanhvn.skysim.entity.Ageable;
import vn.giakhanhvn.skysim.entity.EntityStatistics;

public interface WolfStatistics extends EntityStatistics, Ageable {
    default boolean isAngry() {
        return false;
    }
}
