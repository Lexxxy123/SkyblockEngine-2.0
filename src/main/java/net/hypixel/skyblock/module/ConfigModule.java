/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.module;

import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.module.impl.ModuleImpl;

public class ConfigModule
implements ModuleImpl {
    private static Config genericConfig;
    private static Config heads;
    private static Config blocks;
    private static Config spawners;
    private static Config databaseInfo;
    private static Config serverInfo;

    @Override
    public String name() {
        return "Configuration";
    }

    @Override
    public void onStart() {
        genericConfig = new Config("config.yml");
        heads = new Config("heads.yml");
        blocks = new Config("blocks.yml");
        spawners = new Config("spawners.yml");
        databaseInfo = new Config("database.yml");
        serverInfo = new Config("serverinfo.yml");
    }

    @Override
    public void onStop() {
    }

    @Override
    public boolean shouldStart() {
        return true;
    }

    public static Config getGenericConfig() {
        return genericConfig;
    }

    public static Config getHeads() {
        return heads;
    }

    public static Config getBlocks() {
        return blocks;
    }

    public static Config getSpawners() {
        return spawners;
    }

    public static Config getDatabaseInfo() {
        return databaseInfo;
    }

    public static Config getServerInfo() {
        return serverInfo;
    }
}

