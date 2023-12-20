package in.godspunky.skyblock.entity;

public interface ZombieStatistics extends EntityStatistics, Ageable {
    default boolean isVillager() {
        return false;
    }
}
