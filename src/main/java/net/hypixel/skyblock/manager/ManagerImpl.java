package net.hypixel.skyblock.manager;

public interface ManagerImpl {
    String name();
    void onStart();
    void onStop();
    boolean shouldStart();
}
