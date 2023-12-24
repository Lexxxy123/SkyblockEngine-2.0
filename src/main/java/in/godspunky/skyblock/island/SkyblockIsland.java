package in.godspunky.skyblock.island;

import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import lombok.Data;
import net.swofty.swm.api.SlimePlugin;
import net.swofty.swm.api.exceptions.*;
import net.swofty.swm.api.loaders.SlimeLoader;
import net.swofty.swm.api.world.SlimeWorld;
import net.swofty.swm.api.world.properties.SlimeProperties;
import net.swofty.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class SkyblockIsland {


    private User owner;
    private  UUID uuid;
    private  SlimePlugin slimePlugin;
    private World bukkitWorld;
    private SlimeWorld slimeWorld;
    private Player bukkitPlayer;
    public static final String ISLAND_PREFIX = "island-";


    public static HashMap<UUID, SkyblockIsland> islandRegistry = new HashMap<>();

    public static SkyblockIsland getIsland(UUID uuid) {
        return islandRegistry.computeIfAbsent(uuid, SkyblockIsland::new);
    }

    private SkyblockIsland(UUID uuid){
        this.owner = User.getUser(uuid);
        this.uuid = uuid;
        this.bukkitPlayer = Bukkit.getPlayer(uuid);
        this.slimePlugin = SkySimEngine.getPlugin().getSlimePlugin();
        this.slimeWorld = null;
        this.bukkitWorld = null;

    }

    public static World getWorld(Player player) {
        return Bukkit.getWorld(ISLAND_PREFIX +
                User.getUser(player.getUniqueId()).getSelectedProfileUUID()
        );
    }
    public static World getWorld(UUID uuid){
        return Bukkit.getWorld(ISLAND_PREFIX +
                User.getUser(uuid).getSelectedProfileUUID()
        );
    }



    public void send(){
        owner = User.getUser(uuid);
        final String worldName = ISLAND_PREFIX + owner.getSelectedProfileUUID();
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        try {
            if (!loader.worldExists(worldName)) {
                this.slimeWorld = slimePlugin.createEmptyWorld(
                        loader,
                        worldName,
                        false,
                        properties
                );
                slimePlugin.generateWorld(slimeWorld).thenRun(()->{
                    this.bukkitWorld = Bukkit.getWorld(worldName);
                    SUtil.generate(new Location(Bukkit.getWorld(worldName), 0, 100, 0), "private_island.schematic");
                    Location loc1 = new Location(bukkitWorld, 0, 100 + 4.0, + 35.0);
                    Location loc2 = new Location(bukkitWorld, -2, 100,  35.0);
                    SUtil.setBlocks(loc1, loc2, Material.PORTAL, false);
                    bukkitPlayer.teleport(
                            new Location(Bukkit.getWorld(worldName), 0, 100, 0)
                    );
                    bukkitPlayer.sendMessage(ChatColor.GRAY + "Sending to Player Island...");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    bukkitPlayer.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "          Welcome to Skyblock, " + ChatColor.GRAY + bukkitPlayer.getName() + ChatColor.WHITE + ChatColor.BOLD + "!");
                    bukkitPlayer.sendMessage("");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "               This is your Island, the Skyblock");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "            universe has many lands to discover,");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "         secrets to uncover, and people to meet.");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "            Collect resources, craft items, and");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "         complete objectives to advance your way");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "                       through Skyblock.");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "                             Have fun!");
                    bukkitPlayer.sendMessage("");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                });

            } else if (loader.worldExists(worldName) &&
                    Bukkit.getWorld(worldName) == null) {
                this.slimeWorld = slimePlugin.loadWorld(
                        loader,
                        worldName,
                        false,
                        properties
                );
                slimePlugin.generateWorld(slimeWorld).thenRun(() ->{
                    bukkitPlayer.teleport(
                            new Location(Bukkit.getWorld(worldName), 0, 100, 0)
                    );
                });


            }else if (Bukkit.getWorld(worldName) != null){
                bukkitPlayer.teleport(new Location(Bukkit.getWorld(ISLAND_PREFIX + uuid.toString()), 0, 100, 0));
            }
        }catch (WorldAlreadyExistsException | IOException | CorruptedWorldException | NewerFormatException |
                WorldInUseException | UnknownWorldException ex){
            ex.printStackTrace();
        }
    }


    public boolean exist() {
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        if (loader == null) return false;
        try{
            if (loader.worldExists(ISLAND_PREFIX + uuid.toString())) return true;
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public void delete(){
        if (slimeWorld == null) return;
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        try {
            loader.deleteWorld(ISLAND_PREFIX + uuid.toString());
        }catch (IOException | UnknownWorldException ex){
            ex.printStackTrace();
        }
    }
}