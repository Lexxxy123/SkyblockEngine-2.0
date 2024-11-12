/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 */
package net.hypixel.skyblock.features.calendar;

import java.util.Arrays;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

public final class SkyBlockCalendar {
    public static final List<String> MONTH_NAMES = Arrays.asList("Early Spring", "Spring", "Late Spring", "Early Summer", "Summer", "Late Summer", "Early Autumn", "Autumn", "Late Autumn", "Early Winter", "Winter", "Late Winter");
    public static long ELAPSED = 0L;
    public static final int YEAR = 8928000;
    public static final int MONTH = 744000;
    public static final int DAY = 24000;

    private SkyBlockCalendar() {
    }

    public static int getYear() {
        return (int)(ELAPSED / 8928000L);
    }

    public static int getMonth() {
        return (int)(ELAPSED / 744000L) % 12 + 1;
    }

    public static int getDay() {
        return (int)(ELAPSED / 24000L) % 31 + 1;
    }

    public static String getMonthName(int month) {
        if (month < 1 || month > 12) {
            return "Unknown Month";
        }
        return MONTH_NAMES.get(month - 1);
    }

    public static String getMonthName() {
        return SkyBlockCalendar.getMonthName(SkyBlockCalendar.getMonth());
    }

    public static void saveElapsed() {
        SkyBlock plugin = SkyBlock.getPlugin();
        plugin.config.set("timeElapsed", ELAPSED);
        plugin.config.save();
    }

    public static void synchronize() {
        ELAPSED = SkyBlock.getPlugin().config.getLong("timeElapsed");
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) continue;
                entity.remove();
            }
            int time = (int)(ELAPSED % 24000L - 6000L);
            if (time < 0) {
                time += 24000;
            }
            world.setTime((long)time);
        }
    }
}

