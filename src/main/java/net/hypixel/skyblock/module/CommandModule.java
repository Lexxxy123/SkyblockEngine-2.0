package net.hypixel.skyblock.module;

import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.module.impl.ModuleImpl;
import net.hypixel.skyblock.util.ReflectionUtil;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;

public class CommandModule implements ModuleImpl {
    @Getter
    private static CommandMap commandMap;

    @Override
    public String name() {
        return "Command";
    }

    @Override
    public void onStart() {
        this.loadCommandMap();

        ReflectionUtil.loopThroughPackage("net.hypixel.skyblock.command" , SCommand.class)
                .forEach(SCommand::register);
    }

    void loadCommandMap(){
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
        }catch (NullPointerException | IllegalAccessException | NoSuchFieldException ex){
            SLog.severe("Plugin was unable to load CommandMap \n Disabling plugin for safety...");
            Bukkit.getPluginManager().disablePlugin(SkyBlock.getPlugin());
        }
    }

    @Override
    public void onStop() {
          // nothing yet
    }

    @Override
    public boolean shouldStart() {
        return true;
    }
}
