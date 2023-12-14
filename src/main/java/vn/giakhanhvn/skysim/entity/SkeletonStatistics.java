package vn.giakhanhvn.skysim.entity;

public interface SkeletonStatistics extends EntityStatistics
{
    default boolean isWither() {
        return false;
    }
}
