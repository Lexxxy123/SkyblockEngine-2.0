package in.godspunky.skyblock.user;

public interface PlayerStatistic<T> {
    int HELMET = 0;
    int CHESTPLATE = 1;
    int LEGGINGS = 2;
    int BOOTS = 3;
    int HAND = 4;
    int SET = 5;
    int BOOST = 6;
    int PET = 7;
    int MINER_BUFF = 8;
    int OBSIDIAN_CHESTPLATE = 9;
    int FARMING = 10;
    int MINING = 11;
    int COMBAT = 12;
    int ENCHANTING = 14;
    int FORAGING = 13;
    int ADD_FOR_INVENTORY = 15;
    int ADD_FOR_POTION_EFFECTS = 52;
    int CRYSTALBUFF = 150;
    int COOKIE_BUFF = 151;
    int TEMPORARY_STATS = 152;
    int FATAL_SLOT = 153;

    T addAll();

    void add(final int p0, final T p1);

    void sub(final int p0, final T p1);

    void zero(final int p0);

    boolean contains(final int p0);

    T safeGet(final int p0);

    void set(final int p0, final T p1);

    int next();

    T getFor(final int p0);
}
