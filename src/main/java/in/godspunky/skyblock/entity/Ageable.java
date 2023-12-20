package in.godspunky.skyblock.entity;

public interface Ageable {
    default boolean isBaby() {
        return false;
    }
}
