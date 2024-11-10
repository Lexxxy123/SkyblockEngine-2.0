/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.module.impl;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.module.impl.ModuleImpl;
import net.hypixel.skyblock.util.ReflectionUtil;
import net.hypixel.skyblock.util.SLog;

public class SkyBlockModuleManager {
    private static final List<ModuleImpl> LOADED_MODULES = new ArrayList<ModuleImpl>();

    public static void initModules() {
        ReflectionUtil.loopThroughPackage("net.hypixel.skyblock.module", ModuleImpl.class).filter(ModuleImpl::shouldStart).forEach(LOADED_MODULES::add);
        LOADED_MODULES.forEach(module -> {
            long startTime = System.currentTimeMillis();
            SLog.info("Starting " + module.name() + " module...");
            module.onStart();
            long duration = System.currentTimeMillis() - startTime;
            SLog.info(module.name() + " module started in " + duration + " ms.");
        });
    }

    public static void stopManagers() {
        LOADED_MODULES.forEach(module -> {
            long startTime = System.currentTimeMillis();
            SLog.info("Stopping " + module.name() + " module...");
            module.onStop();
            long duration = System.currentTimeMillis() - startTime;
            SLog.info(module.name() + " module stopped in " + duration + " ms.");
        });
    }
}

