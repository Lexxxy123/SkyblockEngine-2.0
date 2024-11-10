/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.module;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.database.SQLDatabase;
import net.hypixel.skyblock.database.SQLRegionData;
import net.hypixel.skyblock.database.SQLWorldData;
import net.hypixel.skyblock.module.ConfigModule;
import net.hypixel.skyblock.module.impl.ModuleImpl;

public class DatabaseModule
implements ModuleImpl {
    public static SQLDatabase sqlInstance;
    public static SQLRegionData regionData;
    public static SQLWorldData worldData;

    @Override
    public String name() {
        return "Database";
    }

    @Override
    public void onStart() {
        SkyBlock.getPlugin().sendMessage("&aLoading SQL database...");
        sqlInstance = new SQLDatabase();
        SkyBlock.getPlugin().sendMessage("&aConnecting to MongoDB...");
        DatabaseManager.connectToDatabase(ConfigModule.getDatabaseInfo().getString("mongodb.uri"), ConfigModule.getDatabaseInfo().getString("mongodb.name"));
        regionData = new SQLRegionData();
        worldData = new SQLWorldData();
    }

    @Override
    public void onStop() {
    }

    @Override
    public boolean shouldStart() {
        return true;
    }

    public static SQLDatabase getSqlInstance() {
        return sqlInstance;
    }

    public static SQLRegionData getRegionData() {
        return regionData;
    }

    public static SQLWorldData getWorldData() {
        return worldData;
    }
}

