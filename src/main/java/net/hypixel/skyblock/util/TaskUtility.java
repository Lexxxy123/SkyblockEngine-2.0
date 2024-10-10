package net.hypixel.skyblock.util;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtility {
    private static SkyBlock plugin = SkyBlock.getPlugin();

    public static void delaySync(Runnable task, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLater(plugin, delay);
    }

    public static void delayAsync(Runnable task, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskLaterAsynchronously(plugin, delay);
    }

    public static void runSync(Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTask(plugin);
    }

    public static void runAsync(Runnable task) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

    public static void repeatSync(Runnable task, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskTimer(plugin, delay, period);
    }

    public static void repeatAsync(Runnable task, long delay, long period) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskTimerAsynchronously(plugin, delay, period);
    }

    public static void cancelTask(int taskId) {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }
}
