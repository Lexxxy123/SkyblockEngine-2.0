/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.hypixel.skyblock.config;

import java.io.File;
import net.hypixel.skyblock.SkyBlock;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
extends YamlConfiguration {
    private final SkyBlock plugin = SkyBlock.getPlugin();
    private final File file;

    public Config(File parent, String name) {
        this.file = new File(parent, name);
        if (!this.file.exists()) {
            this.options().copyDefaults(true);
            this.plugin.saveResource(name, false);
        }
        this.load();
    }

    public Config(String name) {
        this(SkyBlock.getPlugin().getDataFolder(), name);
    }

    public void load() {
        try {
            super.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

