package net.hypixel.skyblock.module;

import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.database.SQLDatabase;
import net.hypixel.skyblock.database.SQLRegionData;
import net.hypixel.skyblock.database.SQLWorldData;
import net.hypixel.skyblock.module.impl.ModuleImpl;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Bukkit;

public class DatabaseModule implements ModuleImpl {
    @Getter
    public static SQLDatabase sqlInstance;
    @Getter
    public static SQLRegionData regionData;
    @Getter
    public static SQLWorldData worldData;

    @Override
    public String name() {
        return "Database";
    }

    @Override
    public void onStart() {
        SkyBlock.getPlugin().sendMessage("&aLoading SQL database...");
        sqlInstance = new SQLDatabase();

        // todo - rewrite database manager
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
}
