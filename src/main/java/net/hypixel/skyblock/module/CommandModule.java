/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandMap
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.module;

import java.lang.reflect.Field;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.module.impl.ModuleImpl;
import net.hypixel.skyblock.util.ReflectionUtil;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

public class CommandModule
implements ModuleImpl {
    private static CommandMap commandMap;

    @Override
    public String name() {
        return "Command";
    }

    @Override
    public void onStart() {
        this.loadCommandMap();
        ReflectionUtil.loopThroughPackage("net.hypixel.skyblock.command", SCommand.class).forEach(SCommand::register);
    }

    void loadCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap)field.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException | NullPointerException ex) {
            SLog.severe("Plugin was unable to load CommandMap \n Disabling plugin for safety...");
            Bukkit.getPluginManager().disablePlugin((Plugin)SkyBlock.getPlugin());
        }
    }

    @Override
    public void onStop() {
    }

    @Override
    public boolean shouldStart() {
        return true;
    }

    public static CommandMap getCommandMap() {
        return commandMap;
    }
}

