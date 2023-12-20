package in.godspunky.skyblock.entity.wolf;

import in.godspunky.skyblock.entity.Ageable;
import in.godspunky.skyblock.entity.EntityStatistics;

public interface WolfStatistics extends EntityStatistics, Ageable {
    default boolean isAngry() {
        return false;
    }
}
