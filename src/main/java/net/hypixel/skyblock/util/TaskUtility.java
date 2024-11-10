/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.util;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtility {
    private static SkyBlock plugin = SkyBlock.getPlugin();

    public static void delaySync(final Runnable task, long delay) {
        new BukkitRunnable(){

            public void run() {
                task.run();
            }
        }.runTaskLater((Plugin)plugin, delay);
    }

    public static void delayAsync(final Runnable task, long delay) {
        new BukkitRunnable(){

            public void run() {
                task.run();
            }
        }.runTaskLaterAsynchronously((Plugin)plugin, delay);
    }

    public static void runSync(final Runnable task) {
        new BukkitRunnable(){

            public void run() {
                task.run();
            }
        }.runTask((Plugin)plugin);
    }

    public static void runAsync(Runnable task) {
        plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)plugin, task);
    }

    public static void repeatSync(final Runnable task, long delay, long period) {
        new BukkitRunnable(){

            public void run() {
                task.run();
            }
        }.runTaskTimer((Plugin)plugin, delay, period);
    }

    public static void repeatAsync(final Runnable task, long delay, long period) {
        new BukkitRunnable(){

            public void run() {
                task.run();
            }
        }.runTaskTimerAsynchronously((Plugin)plugin, delay, period);
    }

    public static void cancelTask(int taskId) {
        plugin.getServer().getScheduler().cancelTask(taskId);
    }
}

