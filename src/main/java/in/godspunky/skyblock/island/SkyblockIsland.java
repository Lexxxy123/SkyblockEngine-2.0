package in.godspunky.skyblock.island;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import lombok.Data;
import net.swofty.swm.api.SlimePlugin;
import net.swofty.swm.api.exceptions.*;
import net.swofty.swm.api.loaders.SlimeLoader;
import net.swofty.swm.api.world.SlimeWorld;
import net.swofty.swm.api.world.properties.SlimeProperties;
import net.swofty.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Data
public class SkyblockIsland {


    private User owner;
    private UUID uuid;
    private SlimePlugin slimePlugin;
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
        this.slimePlugin = SkyBlock.getPlugin().getSlimePlugin();
        this.slimeWorld = null;
        this.bukkitWorld = null;

    }

    public static World getWorld(Player player) {
        return Bukkit.getWorld(ISLAND_PREFIX + player.getUniqueId());
    }
    public static World getWorld(UUID uuid){
        return Bukkit.getWorld(ISLAND_PREFIX + uuid);
    }


    public void send(){
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        try {
            if (!loader.worldExists(ISLAND_PREFIX + uuid.toString())) {
                this.slimeWorld = slimePlugin.createEmptyWorld(
                        loader,
                        ISLAND_PREFIX + uuid.toString(),
                        false,
                        properties
                );
                slimePlugin.generateWorld(slimeWorld).thenRun(()->{
                    this.bukkitWorld = Bukkit.getWorld(ISLAND_PREFIX + uuid);
                    SUtil.generate(new Location(Bukkit.getWorld(ISLAND_PREFIX + uuid.toString()), 0, 100, 0), "private_island.schematic");
                    Location loc1 = new Location(bukkitWorld, 0, 100 + 4.0, + 35.0);
                    Location loc2 = new Location(bukkitWorld, -2, 100,  35.0);
                    SUtil.setBlocks(loc1, loc2, Material.PORTAL, false);
                    bukkitPlayer.teleport(new Location(Bukkit.getWorld(ISLAND_PREFIX + uuid.toString()), 0, 100, 0));

                });

            } else if (loader.worldExists(ISLAND_PREFIX + uuid.toString()) && Bukkit.getWorld(ISLAND_PREFIX+uuid) == null) {
                this.slimeWorld = slimePlugin.loadWorld(
                        loader,
                        ISLAND_PREFIX + uuid.toString(),
                        false,
                        properties
                );
                slimePlugin.generateWorld(slimeWorld).thenRun(() ->{
                    bukkitPlayer.teleport(new Location(Bukkit.getWorld(ISLAND_PREFIX + uuid.toString()), 0, 100, 0));
                });


            }else if (Bukkit.getWorld(ISLAND_PREFIX + uuid) != null){
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