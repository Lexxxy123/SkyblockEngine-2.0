package net.hypixel.skyblock.manager;

import net.hypixel.skyblock.util.ReflectionUtil;
import net.hypixel.skyblock.util.SLog;

import java.util.ArrayList;
import java.util.List;

public class SkyBlockManager {

    private static final List<ManagerImpl> LOADED_MANAGERS = new ArrayList<>();

    public static void loadManagers() {
        ReflectionUtil.loopThroughPackage("net.hypixel.skyblock.manager", ManagerImpl.class)
                .filter(ManagerImpl::shouldStart)
                .forEach(LOADED_MANAGERS::add);
    }

    public static void startManagers() {
        LOADED_MANAGERS.forEach(manager -> {
            long startTime = System.currentTimeMillis();
            SLog.info("Starting " + manager.name() + " manager...");
            manager.onStart();
            long duration = System.currentTimeMillis() - startTime;
            SLog.info(manager.name() + " manager started in " + duration + " ms.");
        });
    }

    public static void stopManagers() {
        LOADED_MANAGERS.forEach(manager -> {
            long startTime = System.currentTimeMillis();
            SLog.info("Stopping " + manager.name() + " manager...");
            manager.onStop();
            long duration = System.currentTimeMillis() - startTime;
            SLog.info(manager.name() + " manager stopped in " + duration + " ms.");
        });
    }
}