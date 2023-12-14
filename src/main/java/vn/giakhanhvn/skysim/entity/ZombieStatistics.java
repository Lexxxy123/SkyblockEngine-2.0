package vn.giakhanhvn.skysim.entity;

public interface ZombieStatistics extends EntityStatistics, Ageable
{
    default boolean isVillager() {
        return false;
    }
}
