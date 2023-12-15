package vn.giakhanhvn.skysim.entity;

public interface Ageable {
    default boolean isBaby() {
        return false;
    }
}
