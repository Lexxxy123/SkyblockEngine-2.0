package in.godspunky.skyblock.entity;

public interface SkeletonStatistics extends EntityStatistics {
    default boolean isWither() {
        return false;
    }
}
