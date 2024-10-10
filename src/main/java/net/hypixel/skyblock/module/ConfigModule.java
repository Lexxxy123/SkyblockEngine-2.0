package net.hypixel.skyblock.module;

import lombok.Getter;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.module.impl.ModuleImpl;


public class ConfigModule implements ModuleImpl {
    @Getter
    private static Config genericConfig;
    @Getter
    private static Config heads;
    @Getter
    private static Config blocks;
    @Getter
    private static Config spawners;
    @Getter
    private static Config databaseInfo;
    @Getter
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
}
