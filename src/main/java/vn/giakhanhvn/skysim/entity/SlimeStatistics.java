package vn.giakhanhvn.skysim.entity;

public interface SlimeStatistics extends EntityStatistics {
    default int getSize() {
        return 1;
    }

    default boolean split() {
        return false;
    }
}
