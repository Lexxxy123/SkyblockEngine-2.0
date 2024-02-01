package in.godspunky.skyblock.island;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.features.npc.NPC;
import in.godspunky.skyblock.features.npc.NPCHandler;
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
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class SkyblockIsland {


    public static final String ISLAND_PREFIX = "island-";
    public static HashMap<UUID, SkyblockIsland> islandRegistry = new HashMap<>();
    private User owner;
    private UUID uuid;
    private SlimePlugin slimePlugin;
    private World bukkitWorld;
    private SlimeWorld slimeWorld;
    private Player bukkitPlayer;

    private SkyblockIsland(UUID uuid) {
        this.owner = User.getUser(uuid);
        this.uuid = uuid;
        this.bukkitPlayer = Bukkit.getPlayer(uuid);
        this.slimePlugin = Skyblock.getPlugin().getSlimePlugin();
        this.slimeWorld = null;
        this.bukkitWorld = null;

    }

    public static SkyblockIsland getIsland(UUID uuid) {
        return islandRegistry.computeIfAbsent(uuid, SkyblockIsland::new);
    }

    public static World getWorld(Player player) {
        return Bukkit.getWorld(ISLAND_PREFIX +
                User.getUser(player.getUniqueId()).getSelectedProfileUUID()
        );
    }

    public static World getWorld(UUID uuid) {
        return Bukkit.getWorld(ISLAND_PREFIX +
                User.getUser(uuid).getSelectedProfileUUID()
        );
    }


    public void send(UUID uuid1) {
        owner = User.getUser(uuid);
        final String worldName = ISLAND_PREFIX + uuid1;
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
                slimePlugin.generateWorld(slimeWorld).thenRun(() -> {
                    this.bukkitWorld = Bukkit.getWorld(worldName);
                    SUtil.generate(new Location(Bukkit.getWorld(worldName), 0, 100, 0), "private_island.schematic");
                    Location loc1 = new Location(bukkitWorld, 0, 100 + 4.0, +35.0);
                    Location loc2 = new Location(bukkitWorld, -2, 100, 35.0);
                    SUtil.setBlocks(loc1, loc2, Material.PORTAL, false);
                    addJerry(uuid1.toString());
                    bukkitPlayer.teleport(
                            new Location(Bukkit.getWorld(worldName), 0, 100, 0)
                    );
                    bukkitPlayer.sendMessage(ChatColor.GRAY + "Sending to Player Island...");
                    bukkitPlayer.sendMessage(ChatColor.YELLOW + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                    bukkitPlayer.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "          Welcome to Skyblock, " +  bukkitPlayer.getDisplayName() + ChatColor.WHITE + ChatColor.BOLD + "!");
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
                slimePlugin.generateWorld(slimeWorld).thenRun(() -> {
                    addJerry(uuid1.toString());
                    bukkitPlayer.teleport(
                            new Location(Bukkit.getWorld(worldName), 0, 100, 0)
                    );
                });


            } else if (Bukkit.getWorld(worldName) != null) {
                bukkitPlayer.teleport(new Location(Bukkit.getWorld(ISLAND_PREFIX + owner.getSelectedProfileUUID().toString()), 0, 100, 0));
            }
        } catch (WorldAlreadyExistsException | IOException | CorruptedWorldException | NewerFormatException |
                 WorldInUseException | UnknownWorldException ex) {
            ex.printStackTrace();
        }
    }


    public boolean exist() {
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        if (loader == null) return false;
        try {
            if (loader.worldExists(ISLAND_PREFIX + uuid.toString())) return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void addJerry(String uuid) {
        NPCHandler npcs = Skyblock.getPlugin().getNpcHandler();
        if (!npcs.getNPCs().containsKey("jerry_" + uuid) && getWorld(UUID.fromString(uuid)) != null) {
            NPC jerry = new NPC("Jerry", true, false, true, Villager.Profession.FARMER,
                    new Location(Bukkit.getWorld(ISLAND_PREFIX + uuid), 2.5, 100, 26.5),
                    (p) -> {
                        if (p.getUniqueId().equals(bukkitPlayer.getUniqueId())) {
                            if (owner.getCompletedObjectives().contains("jerry")) {
                            } else {
                                NPC.sendMessages(p, "Jerry",
                                        "Your Skyblock island is part of a much larger universe",
                                        "The Skyblock universe is full of islands to explore and resources to discover!",
                                        "Use the Portal to warp to the first of those islands - The Skyblock Hub!");
                            }
                        } else {
                            NPC.sendMessage(p, "Jerry", "Jerry doesn't speak to strangers!", false);
                            p.playSound(p.getLocation(), Sound.VILLAGER_NO, 10, 1);
                        }
                    }, "", "");

            npcs.registerNPC("jerry_" + uuid, jerry);
            jerry.spawn();
        }
    }

    public void delete() {
        if (slimeWorld == null) return;
        SlimeLoader loader = slimePlugin.getLoader("mongodb");
        try {
            loader.deleteWorld(ISLAND_PREFIX + uuid.toString());
        } catch (IOException | UnknownWorldException ex) {
            ex.printStackTrace();
        }
    }
}