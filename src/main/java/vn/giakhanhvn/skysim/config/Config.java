package vn.giakhanhvn.skysim.config;

import org.bukkit.configuration.file.YamlConfiguration;
import vn.giakhanhvn.skysim.SkySimEngine;

import java.io.File;

public class Config extends YamlConfiguration {
    private final SkySimEngine plugin;
    private final File file;

    public Config(final File parent, final String name) {
        this.plugin = SkySimEngine.getPlugin();
        this.file = new File(parent, name);
        if (!this.file.exists()) {
            this.options().copyDefaults(true);
            this.plugin.saveResource(name, false);
        }
        this.load();
    }

    public Config(final String name) {
        this(SkySimEngine.getPlugin().getDataFolder(), name);
    }

    public void load() {
        try {
            super.load(this.file);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.file);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
