package in.godspunky.skyblock.user;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserConfig {

    private final File configFile;
    private final FileConfiguration config;

    public UserConfig(UUID uuid) {
        this.configFile = new File(User.USER_FOLDER, uuid.toString() + ".yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public List<String> getCompletedQuests() {
        return config.getStringList("completedQuests");
    }

    public List<String> getCompletedObjectives() {
        return config.getStringList("completedObjectives");
    }

    public void addCompletedQuest(String questName) {
        List<String> completedQuests = getCompletedQuests();
        completedQuests.add(questName);
        config.set("completedQuests", completedQuests);
        saveConfig();
    }

    public void addCompletedObjectives(String objectiveName) {
        List<String> completedObjectives = getCompletedObjectives();
        completedObjectives.add(objectiveName);
        config.set("completedObjectives", completedObjectives);
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

