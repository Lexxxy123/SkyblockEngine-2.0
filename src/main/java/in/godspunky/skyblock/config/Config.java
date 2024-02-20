package in.godspunky.skyblock.config;

import in.godspunky.skyblock.SkyBlock;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {
    private final SkyBlock plugin;
    private final File file;

    public Config(final File parent, final String name) {
        this.plugin = SkyBlock.getPlugin();
        this.file = new File(parent, name);
        if (!this.file.exists()) {
            this.options().copyDefaults(true);
            this.plugin.saveResource(name, false);
        }
        this.load();
    }

    public Config(final String name) {
        this(SkyBlock.getPlugin().getDataFolder(), name);
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
