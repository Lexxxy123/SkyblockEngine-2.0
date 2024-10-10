package net.hypixel.skyblock.module.impl;

public interface ModuleImpl {
    String name();
    void onStart();
    void onStop();
    boolean shouldStart();
}
