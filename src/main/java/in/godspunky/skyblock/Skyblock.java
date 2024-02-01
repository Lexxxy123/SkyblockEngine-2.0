package in.godspunky.skyblock;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.io.Files;
import de.slikey.effectlib.EffectManager;
import in.godspunky.skyblock.auction.AuctionBid;
import in.godspunky.skyblock.auction.AuctionEscrow;
import in.godspunky.skyblock.auction.AuctionItem;
import in.godspunky.skyblock.command.*;
import in.godspunky.skyblock.config.Config;
import in.godspunky.skyblock.dimoon.*;
import in.godspunky.skyblock.dimoon.listeners.BlockListener;
import in.godspunky.skyblock.dimoon.listeners.EntityListener;
import in.godspunky.skyblock.dimoon.listeners.PlayerListener;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.EntityPopulator;
import in.godspunky.skyblock.entity.EntitySpawner;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.entity.StaticDragonManager;
import in.godspunky.skyblock.entity.nms.VoidgloomSeraph;
import in.godspunky.skyblock.features.npc.NPC;
import in.godspunky.skyblock.features.npc.NPCHandler;
import in.godspunky.skyblock.gui.GUIListener;
import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.item.ItemListener;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.armor.VoidlingsWardenHelmet;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.listener.PacketListener;
import in.godspunky.skyblock.listener.ServerPingListener;
import in.godspunky.skyblock.listener.WorldListener;
import in.godspunky.skyblock.merchant.MerchantItemHandler;
import in.godspunky.skyblock.minion.MinionListener;
import in.godspunky.skyblock.nms.nmsutil.apihelper.APIManager;
import in.godspunky.skyblock.nms.nmsutil.apihelper.SkySimBungee;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.PacketHelper;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.PacketHandler;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.metrics.Metrics;
import in.godspunky.skyblock.nms.packetevents.*;
import in.godspunky.skyblock.nms.pingrep.PingAPI;
import in.godspunky.skyblock.nms.pingrep.PingEvent;
import in.godspunky.skyblock.nms.pingrep.PingListener;
import in.godspunky.skyblock.npc.SkyblockNPC;
import in.godspunky.skyblock.npc.SkyblockNPCManager;
import in.godspunky.skyblock.npc.hub.Auction.*;
import in.godspunky.skyblock.npc.hub.Banker;
import in.godspunky.skyblock.npc.hub.merchants.AdventurerMerchant;
import in.godspunky.skyblock.npc.hub.merchants.FarmMerchant;
import in.godspunky.skyblock.objectives.QuestLine;
import in.godspunky.skyblock.objectives.QuestLineHandler;
import in.godspunky.skyblock.objectives.hub.IntroduceYourselfQuest;
import in.godspunky.skyblock.ranks.PlayerChatListener;
import in.godspunky.skyblock.ranks.PlayerJoinQuitListener;
import in.godspunky.skyblock.ranks.SetRankCommand;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.sql.SQLDatabase;
import in.godspunky.skyblock.sql.SQLRegionData;
import in.godspunky.skyblock.sql.SQLWorldData;
import in.godspunky.skyblock.user.AuctionSettings;
import in.godspunky.skyblock.user.DatabaseManager;
import in.godspunky.skyblock.user.SMongoLoader;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.*;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.swofty.swm.api.SlimePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import static in.godspunky.skyblock.util.SUtil.sendDelayedMessages;

@SuppressWarnings("deprecation")
public class Skyblock extends JavaPlugin implements PluginMessageListener, BungeeChannel.ForwardConsumer {
    public static EffectManager effectManager;
    // public static MultiverseCore core;
    private static ProtocolManager protocolManager;
    private static Economy econ;
    private static Skyblock plugin;
    private static Skyblock instance;

    static {
        //  Skyblock.core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        Skyblock.econ = null;
    }

    private final PacketHelper packetInj;
    @Getter
    private final ServerVersion serverVersion;
    public Arena arena;
    public Dimoon dimoon;
    public SummoningSequence sq;
    public boolean altarCooldown;
    public Config config;
    public Config heads;
    public Config blocks;
    public Config spawners;
    public CommandMap commandMap;
    @Getter
    public SMongoLoader dataLoader;
    public SQLDatabase sql;
    public SQLRegionData regionData;
    public SQLWorldData worldData;
    public CommandLoader cl;
    public Repeater repeater;
    public List<String> bannedUUID;

    @Getter
    private SlimePlugin slimePlugin;
    @Getter
    @Setter
    private int onlinePlayerAcrossServers;
    @Getter
    private BungeeChannel bc;

    @Getter
    private NPCHandler npcHandler;

    @Getter
    private QuestLineHandler questLineHandler;
    @Getter
    @Setter
    private String serverName;

    public Skyblock() {
        this.packetInj = new PacketHelper();
        this.arena = null;
        this.dimoon = null;
        this.sq = null;
        this.altarCooldown = false;
        this.serverVersion = new ServerVersion("beta", 0, 7, 2, 0);
        this.serverName = "Loading...";
        this.bannedUUID = Collections.singletonList("");
    }

    public static Skyblock getPlugin() {
        return Skyblock.plugin;
    }

    public static ProtocolManager getPTC() {
        return Skyblock.protocolManager;
    }

    public static Skyblock getInstance() {
        return Skyblock.instance;
    }

    public static Player findPlayerByIPAddress(final String ip) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getAddress().toString().contains(ip)) {
                return p;
            }
        }
        return null;
    }

    public static Economy getEconomy() {
        return Skyblock.econ;
    }

    public void onLoad() {
        SLog.info("Loading Bukkit-serializable classes...");
        this.loadSerializableClasses();
    }

    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.bc = new BungeeChannel(this);

        this.setupEconomy();

        Skyblock.plugin = this;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc remove all");
        SLog.info("Performing world regeneration...");
        this.fixTheEnd();
        SLog.info("Loading YAML data from disk...");
        this.config = new Config("config.yml");
        this.heads = new Config("heads.yml");
        this.blocks = new Config("blocks.yml");
        this.spawners = new Config("spawners.yml");
        SLog.info("Loading Command map...");
        try {
            final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            this.commandMap = (CommandMap) f.get(Bukkit.getServer());
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            SLog.severe("Couldn't load command map: ");
            e.printStackTrace();
        }
        SLog.info("Loading SQL database...");
        DatabaseManager.connectToDatabase("mongodb://admin:admin@88.99.150.153:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+1.6.2", "Godspunky");
        //DatabaseManager.connectToDatabase("mongodb://localhost:27017", "Godspunky");
        SLog.info("Loading NPCS...");
        registerNpcs();
        this.npcHandler.spawnAll();
        this.sql = new SQLDatabase();
        this.dataLoader = new SMongoLoader();
        this.regionData = new SQLRegionData();
        this.worldData = new SQLWorldData();
        this.cl = new CommandLoader();
        SLog.info("Begin Protocol injection... (GodspunkyProtocol v0.6.2)");
        APIManager.registerAPI(this.packetInj, this);
        if (!this.packetInj.injected) {
            this.getLogger().warning("[FATAL ERROR] Protocol Injection failed. Disabling the plugin for safety...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SwoftyWorldManager");
        loadWorlds();
        SLog.info("Injecting...");
        PingAPI.register();
        new Metrics(this);
        APIManager.initAPI(PacketHelper.class);
        SLog.info("Starting server loop...");
        this.repeater = new Repeater();
        VoidlingsWardenHelmet.startCounting();
        SLog.info("Loading commands...");
        this.loadCommands();
        SLog.info("Loading listeners...");
        this.loadListeners();
        SLog.info("Injecting Packet/Ping Listener into the core...");
        this.registerPacketListener();
        this.registerPingListener();
        SLog.info("Starting entity spawners...");
        EntitySpawner.startSpawnerTask();
        SLog.info("Establishing player regions...");
        Region.cacheRegions();
        SLog.info("Loading auction items from disk...");
        Skyblock.effectManager = new EffectManager(this);
        AuctionItem.loadAuctionsFromDisk();
        SLog.info("Loading Quest!");
        initializeQuests();
        SLog.info("Loading merchants prices...");
        MerchantItemHandler.init();
        SkyBlockCalendar.ELAPSED = Skyblock.plugin.config.getLong("timeElapsed");
        SLog.info("Synchronizing world time with calendar time and removing world entities...");
        for (final World world : Bukkit.getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) {
                    continue;
                }
                entity.remove();
            }
            int time = (int) (SkyBlockCalendar.ELAPSED % 24000L - 6000L);
            if (time < 0) {
                time += 24000;
            }
            world.setTime(time);
        }
        SLog.info("Loading items...");
        try {
            Class.forName("in.godspunky.skyblock.item.SMaterial");
        } catch (final ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        for (final SMaterial material : SMaterial.values()) {
            if (material.hasClass()) {
                Objects.requireNonNull(material.getStatistics()).load();
            }
        }
        SLog.info("Converting CraftRecipes into custom recipes...");
        final Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            final Recipe recipe = iter.next();
            if (recipe.getResult() == null) {
                continue;
            }
            final Material result = recipe.getResult().getType();
            if (recipe instanceof ShapedRecipe) {
                final ShapedRecipe shaped = (ShapedRecipe) recipe;
                final in.godspunky.skyblock.item.ShapedRecipe specShaped = new in.godspunky.skyblock.item.ShapedRecipe(SItem.convert(shaped.getResult()), Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result)).shape(shaped.getShape());
                for (final Map.Entry<Character, ItemStack> entry : shaped.getIngredientMap().entrySet()) {
                    if (entry.getValue() == null) {
                        continue;
                    }
                    final ItemStack stack = entry.getValue();
                    specShaped.set(entry.getKey(), SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()), stack.getAmount(), true);
                }
            }
            if (!(recipe instanceof ShapelessRecipe)) {
                continue;
            }
            final ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
            final in.godspunky.skyblock.item.ShapelessRecipe specShapeless = new in.godspunky.skyblock.item.ShapelessRecipe(SItem.convert(shapeless.getResult()), Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result));
            for (final ItemStack stack2 : shapeless.getIngredientList()) {
                specShapeless.add(SMaterial.getSpecEquivalent(stack2.getType(), stack2.getDurability()), stack2.getAmount(), true);
            }
        }
        SLog.info("Hooking Skyblock to PlaceholderAPI and registering...");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new placeholding().register();
            SLog.info("Hooked to PAPI successfully!");
        } else {
            SLog.info("ERROR! PlaceholderAPI plugin does not exist, disabing placeholder request!");
        }
        Skyblock.protocolManager = ProtocolLibrary.getProtocolManager();
        this.npcHandler.spawnAll();
        this.beginLoopA();
        WorldListener.blb.add(Material.BEDROCK);
        WorldListener.blb.add(Material.COMMAND);
        WorldListener.blb.add(Material.BARRIER);
        WorldListener.blb.add(Material.ENDER_PORTAL_FRAME);
        WorldListener.blb.add(Material.ENDER_PORTAL);
        WorldListener.c();
        SLog.info("Successfully enabled " + this.getDescription().getFullName());
        SLog.info("===================================");
        SLog.info("GODSPUNKY SKYBLOCK - MADE BY HAMZA & EPICPORTAL");
        SLog.info("PLUGIN ENABLED! HOOKED INTO SSS!");
        SLog.info(" ");
        SLog.info("This plugin provide SKYBLOCK most functions!");
        SLog.info("Originally made by super (Slayers code used)");
        SLog.info("Made by Hamza And Epicportal (C) 2023");
        SLog.info("Any illegal usage will be suppressed! DO NOT LEAK IT!");
        SLog.info("===================================");
        this.sq = new SummoningSequence(Bukkit.getWorld("arena"));
        //Bukkit.getWorld("arena").setAutoSave(false);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        final File file = new File(this.getDataFolder(), "parkours");
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                file.mkdir();
            } catch (final IOException e3) {
                throw new RuntimeException(e3);
            }
        }
        final SItem gtigerm = SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022);
        gtigerm.setRarity(Rarity.MYTHIC);
        final SItem lucki8 = SItem.of(SMaterial.ENCHANTED_BOOK);
        lucki8.addEnchantment(EnchantmentType.LUCKINESS, 8);
        final SItem vicious15 = SItem.of(SMaterial.ENCHANTED_BOOK);
        vicious15.addEnchantment(EnchantmentType.VICIOUS, 15);
        final SItem chimera6 = SItem.of(SMaterial.ENCHANTED_BOOK);
        chimera6.addEnchantment(EnchantmentType.CHIMERA, 6);
        final SItem tbits = SItem.of(SMaterial.ENCHANTED_BOOK);
        tbits.addEnchantment(EnchantmentType.TURBO_GEM, 1);
        DimoonLootTable.highQualitylootTable = new ArrayList<DimoonLootItem>(Arrays.asList(new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOONIZARY_DAGGER), 400, 1100), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_EXCRARION), 310, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_HELMET), 290, 700), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_CHESTPLATE), 340, 900), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_LEGGINGS), 330, 800), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_BOOTS), 220, 500), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_QUANTUMFLUX_POWER_ORB), 310, 900), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_ARCHIVY), 370, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_MAGICIVY), 370, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022), 320, 900), new DimoonLootItem(gtigerm, 300, 1000), new DimoonLootItem(lucki8, 170, 700), new DimoonLootItem(vicious15, 100, 600), new DimoonLootItem(chimera6, 260, 700), new DimoonLootItem(tbits, 210, 700)));
        final SItem lucki9 = SItem.of(SMaterial.ENCHANTED_BOOK);
        lucki9.addEnchantment(EnchantmentType.LUCKINESS, 6);
        DimoonLootTable.lowQualitylootTable = new ArrayList<DimoonLootItem>(Arrays.asList(new DimoonLootItem(lucki9, 20, 150), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOON_GEM), 20, 100), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOON_FRAG), 1, 1, 0, true)));
        Arena.cleanArena();
        getCommand("setrank").setExecutor(new SetRankCommand());
        SMongoLoader.startQueueTask();
    }

    private void loadWorlds() {
        new BlankWorldCreator("f6").createWorld();
        new BlankWorldCreator("arena").createWorld();
        //new BlankWorldCreator("f1");
    }

    private void initializeQuests() {
        SLog.info("Initializing quests...");
        long start = System.currentTimeMillis();

        this.questLineHandler = new QuestLineHandler();

        SLog.info("Successfully registered " + ChatColor.GREEN + this.questLineHandler.getQuests().size() + ChatColor.WHITE + " quests [" + SUtil.getTimeDifferenceAndColor(start, System.currentTimeMillis()) + ChatColor.WHITE + "]");
    }

    public void onDisable() {
        SLog.info("Killing all non-human entities...");
        for (final World world : Bukkit.getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) {
                    continue;
                }
                entity.remove();
            }
        }
        if (this.repeater != null) {
            SLog.info("Stopping server loop...");
            this.repeater.stop();
            SLog.info("Unloading ores from Dwarven Mines...");
            this.unloadBlocks();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "npc remove all");

            File file = new File("plugins/Citizens/saves.yml");

            if (file.exists()) {
                file.delete();
                SLog.info("Deleted citizens file");
            }
            SLog.info("Ejecting protocol channel...");
            APIManager.disableAPI(PacketHelper.class);
            SLog.info("Cleaning HashSets...");
            for (final Map.Entry<Entity, Block> entry : VoidgloomSeraph.CACHED_BLOCK.entrySet()) {
                final Entity stand = entry.getKey();
                if (stand != null && VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                    VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                }
            }
            this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
            this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
            SLog.info("Stopping entity spawners...");
            EntitySpawner.stopSpawnerTask();
            SLog.info("Ending Dragons fight... (If one is currently active)");
            StaticDragonManager.endFight();
            SLog.info("Saving calendar time...");
            SkyBlockCalendar.saveElapsed();
            SLog.info("Saving auction data...");
            for (final AuctionItem item : AuctionItem.getAuctions()) {
                item.save();
            }
            Skyblock.plugin = null;
        }
        SLog.info("Disabled " + this.getDescription().getFullName());
        SLog.info("===================================");
        SLog.info("SKYSIM ENGINE - MADE BY GIAKHANHVN");
        SLog.info("PLUGIN DISABLED!");
        SLog.info("===================================");
    }

    public void registerNpcs() {
        SLog.info("Registering NPCs...");
        long start = System.currentTimeMillis();
        
        World world = Bukkit.getWorld("world");

        this.npcHandler = new NPCHandler();

        this.npcHandler.registerNPC(
                "blacksmith",
                new NPC(
                        "Blacksmith",
                        true,
                        false,
                        true,
                        Villager.Profession.BLACKSMITH,
                        new Location(world, -28, 69, -125),
                        (player) -> player.performCommand("sbnpc reforge"),
                        "",
                        ""
                )
        );

        this.npcHandler.registerNPC("bazaar", new NPC(
                "Bazaar",
                true,
                true,
                false,
                null,
                new Location(world, -32.0, 71.0, -76.0),
                (player -> player.sendMessage(ChatColor.RED + "Not Yet")),
                "ewogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJpZCIgOiAiYmE0ODUzODFjNzI5NDhiY2E0NzY1NjJjNzRlZmE0NTkiLAogICAgICAidHlwZSIgOiAiU0tJTiIsCiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIzMmUzODIwODk3NDI5MTU3NjE5YjBlZTA5OWZlYzA2MjhmNjAyZmZmMTJiNjk1ZGU1NGFlZjExZDkyM2FkNyIsCiAgICAgICJwcm9maWxlSWQiIDogIjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwKICAgICAgInRleHR1cmVJZCIgOiAiYzIzMmUzODIwODk3NDI5MTU3NjE5YjBlZTA5OWZlYzA2MjhmNjAyZmZmMTJiNjk1ZGU1NGFlZjExZDkyM2FkNyIKICAgIH0KICB9LAogICJza2luIiA6IHsKICAgICJpZCIgOiAiYmE0ODUzODFjNzI5NDhiY2E0NzY1NjJjNzRlZmE0NTkiLAogICAgInR5cGUiIDogIlNLSU4iLAogICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMjMyZTM4MjA4OTc0MjkxNTc2MTliMGVlMDk5ZmVjMDYyOGY2MDJmZmYxMmI2OTVkZTU0YWVmMTFkOTIzYWQ3IiwKICAgICJwcm9maWxlSWQiIDogIjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwKICAgICJ0ZXh0dXJlSWQiIDogImMyMzJlMzgyMDg5NzQyOTE1NzYxOWIwZWUwOTlmZWMwNjI4ZjYwMmZmZjEyYjY5NWRlNTRhZWYxMWQ5MjNhZDciCiAgfSwKICAiY2FwZSIgOiBudWxsCn0=",
                "B+1Zq16nwoRYl4Ptbsc0vk6juz0fXnSj96JELezDpgrf1rIypSJ68uKaCKvX0XcVCH7eom/9CZTgpgUPoSH+QjG8I9017YKt3SVjKPq9KLxbM5kjKqJEx4Png2mPvkC+c5TF7Qw7FxjKA4AdSs/7XCeHXSZDgebTKhh6D4WH+XARBsLmNmG/EFo5zP7gUn+EkiGUaOjRPizYuRwOg1zNFkoPsrDGIovnW4itbu2BjdJ80yiTRn4bwGMm93NC/pCG7jbgsxO+YP1GkZnLUaAiIIQcKumC1nrlk3GTFAnHi+JFX562EKNUUMAtWrS/0v7QyeDYfDDtXLB8gNrzXHmaAKkvC1AU5/dk/YLZGq4lY6ukkZVOe+XLNbdXE4QNdA8ewPgPKUBwYEO6UW4jDOV+nN1WRana+rTREJUycFcxs2u6zinKV7lRtMbZ6CzpL+gvPlzhldvovvhX273Fdm7vpoPIRT6vVyO50dWH4qpM7n38Mgyxulot63NvLHzHG7Db8Z7ruZenVLE5cdjBAYHKHOjoOxzR81nPlGp7x3fdlF1usWSx5WRjpdw8Eyim7xrc2iNHRzyE1Lbywz7BTeT5ADHqsFkshrkG2bA11MnnZUyfjdPt4VNoUNX53KgWv14vmgtu8quu2xCW8JQ0J3HANoDgqPINKzCqn6oGdDz3Gy8="
        ));

        this.npcHandler.registerNPC(
                "tia_the_fairy",
                new NPC(ChatColor.LIGHT_PURPLE + "Tia the Fairy",
                        true,
                        true,
                        false,
                        null,
                        new Location(world, 129.5, 66, 137.5),
                        (player) -> player.sendMessage(ChatColor.RED + "Not Yet!"),
                        "ewogICJ0aW1lc3RhbXAiIDogMTYxMzg0ODAyNjE2NywKICAicHJvZmlsZUlkIiA6ICJjMDI0Y2M0YTQwMzc0YWFjYTk2ZTE2Y2MwODM1ZDE4MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZW9yZ2VOb0ZvdW5kIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdlNTk3YjE3NTk1OTAyN2U2NDM5MzZjYTJiOGQ3YmZiMjFiMWRjNjgzYTRhNTdmYjBjMDk5YTdkNGE5Njk1MTAiCiAgICB9CiAgfQp9",
                        "TgEp0zMP+3e+782xvYsMcTYtkBfTq6XZpW1Z0mVb0BaDWjjVmQQer64ykJ8lthJj0Z+BjQhotwc8gIMuzxfBlaAPi7TDnODAm73wWRNs4n/qXj4a+++gkk4NeS1KswLzZDgQ0Nkp1kyM3lOja87zgcUCkpFXrzJbcsUX2N+rABmQIT8swmDRFmwoGvK4r0Cjf8nmLDj3+1fX4Kk+0o1ynLDDhI8c1nq4cqPRaRoTqNy0xYUeX22UaSo5tzlxAKrnabQi/I+P1Z33AqjvO6AdclXAfPIBsD7himNluqvKJjyWwpN0tb48703JMCixhs2Wq1j0cmEjVAqZKSLc+3jNkCp46V6NRIvcJ8xi/dijBR5SPgU8Kb7YUaVT6FUFJsAAVpNOBlJmnI+0L9Esqp9SMhMy8SNO/vo8Gk1zF2BENzuKBD6w5zQlWNIQt4E7MRG1fnh0VZMiS8s+dz9NuCC5oGMFIBNz67J2z6VQR+BhXGCSwDgw9gsKDxYSxpzASa6iFUv1gQpi8x+eQMn4VM16d0mwVDNnd6h1HdCmxextKzkf9mkwBaycz9AOcun8GWOqvhZDv2nyzmUAzFBU0mO1Ys6nYSHQEwXBXqURho5L0Fvu3Wb15YqwATsO//Mg+6L+f/kb5l2B1/Z1I/wzxzOeDYtME2TsTMAaK00ob/6e0Hg="));

        this.npcHandler.registerNPC(
                "maddox_the_slayer",
                new NPC(ChatColor.DARK_PURPLE + "Maddox the Slayer",
                        true,
                        true,
                        false,
                        null,
                        new Location(world, -75, 66.0, -55.0),
                        (player) -> player.performCommand("sbnpc batphone"),
                        "ewogICJ0aW1lc3RhbXAiIDogMTU5NjQ5MTIwODg4NCwKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMzNmQ3Y2M5NWNiZjY2ODlmNWU4Yzk1NDI5NGVjOGQxZWZjNDk0YTQwMzEzMjViYjQyN2JjODFkNTZhNDg0ZCIKICAgIH0KICB9Cn0=",
                        "NeIGEVhTQsg+GfcmtVhCCXdWX6tQpI/iUjPixUKaxea5q8xTpCKFSGqnIhSgG0CjPpxw9UKwC1yr4gIDsM5zPGjnIsD3PDP4F6Jaicx0YsiJGr861zxQDSlxpkcbGXrHRuNq92TT4/zojNMk6qPGtGeFApro7dXxU5Fq7HpyikHR2S4iaTZAF2L65rXqdogmQIBcTI5UVO2cZ3xNSr3j9y/nKGUx0SwVaIryt1sMHj2cO5Lknb9eiiG+vfw/LTlgwOmc9PXHhQB045SoBgGondcBZYBWVGCP9dTCNrvDBp963rzEkJMOfLfL+M2P+BT318BCBQzQ6JGJuILqhdY/Ph7qZJW2P9g8At9chbfnBdwMnHvjTshGN3XMzVg8BdxFAKydJMSocfF4j9KvPCtP1Hilk0pylqRAPe1cn0JpTZ1e/xzorzgqHdo0kXmf8gzLXHXDz8fYanZpQQCemwL3aOHy6nvAFFk/+j6kGLEaetTZgw8WAMJiyAxcpN/elfG9fxoX+pXMFtM9ItRA2Sf6EHdRKJTc4gB+yclkuCd3MgCiRDZU5NwpH8AhTmFZsjd0nHzHLXvpNPmSLAZiYi7EqG9SySEu7pJ4PXHZ0F80jKknNqh0CnnnqH4iKdMIUau33ENPKTLiuxwqxj9bv6ZtsCUZXn/mHWeCOiB6IBPjaR0="));

        this.npcHandler.registerNPC("taylor",
                new NPC("Taylor",
                        true,
                        true,
                        false,
                        null,
                        new Location(world, 27, 71, -43),
                        (p) -> sendDelayedMessages(p, "Taylor", "Hello!", "You look dashing today!"),
                        "ewogICJ0aW1lc3RhbXAiIDogMTU5MDg4MzYxNjg5NCwKICAicHJvZmlsZUlkIiA6ICI3ZGEyYWIzYTkzY2E0OGVlODMwNDhhZmMzYjgwZTY4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJHb2xkYXBmZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMThjYTBjODliZWY4YzRlN2U1MjFiMTEzODIxZmZkZjAwZTdiNWViNTUyYTM3OGY5MDM3Y2JlYzY1NWRkMjdjOCIKICAgIH0KICB9Cn0=",
                        "r6GGjK7Rb0X3mhJQFyMYn+5OU0JvzBYnxti3hGQfomrDMrI5NdWe/+huGw22UqSFt7pMoZH+04J0z0gFFFFmdY6p0gbcls/9W1qIxgjSFYbDM5y/v1xu3+o9sX5P4WY9WVLOV/RYilfTNIRHCu6+rb/X+x+4ftttaf+zyVvF/bMQq+EwgcRo0UNkXIOrzvH0CKNbEIF9D/9mHk2kBgElq6ucs5ohVSVkU9R4XWTbzs2v4LYmSumRSzKd4eB6L66R3CbCqt7nk8DLnsEmexcxR8u9k6BR1P2Itf2yGZPf1hdconNZKqKdv/sKgtPZo5wcIk5h5cGfezN1b2wHXbKBXV/EN2ZTvC5seNJlXDHy0vGhlThwqHPvd24E+aSHnLu+Gfujw72NR10VsL5cnxmLjmGQQh12ohus5ZO45nE8+UZEBq4gr1wdgMfTRF8XYYRXclKuFcPwKMFMjsGtvV4jVxEunuZitygHOzcjUF7dKYbBv8yFtP0UxWA9r2Phcf1JwqHWf0lSE9l2Qi+AKoz3HCBhKdxPvuIPET/VKCB4xQkrjm8pCQYyD9FOLz0/t6yLf4KOHEhSfcWM2ObzACAxH3Euz8KCFdYUQShVejAvahA2sJM/twqZdvDgVed4Apmhx/LAJjhGRTzIvg0D7HzZVyfArEEgQMi8ZDttE/5Lyk4="
                ));

        this.npcHandler.registerNPC("seymour",
                new NPC("Seymour",
                        true,
                        true,
                        false,
                        null,
                        new Location(world, 24, 64, -41),
                        (p) -> {
                            p.sendMessage(ChatColor.RED + "Not Yet!");
                        },
                        "ewogICJ0aW1lc3RhbXAiIDogMTU5OTQ2MDQ5OTc1NCwKICAicHJvZmlsZUlkIiA6ICI3NTE0NDQ4MTkxZTY0NTQ2OGM5NzM5YTZlMzk1N2JlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGFua3NNb2phbmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhhYWM0ZTYzMTM5OGMwZjQ3ZmU4NTYxNWU3ZjQ5MjE1NTAwMDQwYmI5MDgxOWQ1NjhlODI0YWNmOTRiZmE0ZSIKICAgIH0KICB9Cn0=",
                        "hGGbYl4OuItrGiGXYYh6mCy1a5xknDipoYay1DFARt03+YJ6ebFagIMH7JgEY3CFed8WIxFcNivfpZ3q47e2KhNugR+6/X4KBrHtyz9fWVte2HGW7p2ShiYipcUL+8wBjvJ2ssEsWTUeGgqnBgo/sA3UdsWhB6E9iP34x4nm5lPfnKT2Jl9QyhsqXSOQYmidDUY5z0kGhmy0IXRoh92vF/lrzmdpS4TamfogRLGV1BivxZ8C71ImVczEm/JHWTGCdjwFBTdoZzkUOJ9IE+tbBUlOWWFMvjW+TY4Y3pBM5lzY3TMTpvG+rHZ0042E2SNfp2RmHaEAqMNb9JI57qfXKZ8zJB1/8gU+pFjuuXRsWuV0tWKLIUGSH3nIho/BidPBoe6YUsWCe9ySSrFprocKU96Ct6z5l8bsoJ5xtiOGSn/5JdUexc4IUF9ICFh7Xeu8rvGufH7s1BIyLgUBQQSvVpj31VharFkV0IVnwG/4c/YBYaaUUH07CW0woj577fd5nCVEs8pfJ7KNrChtna0LzDZQuELzDwmQO5mdOxWEwGurPvPx1uFm3tCDVBRUrj+CCVCQqIflg9s3nVTRSPZhl3ZlNW+L8/wVdjuXtGTXGWT9ou+nfGRT8c0ENrsVE3dkWe4o2BaokIIdCJ1isO+GS6oMNP6I07EGgxUZFe2kk8A="
                ));

        this.npcHandler.registerNPC("auction_master", new NPC(
                "Auction Master",
                true,
                true,
                false,
                null,
                new Location(world, -46, 73, -90),
                (player -> player.performCommand("ah")),
                "ewogICJ0aW1lc3RhbXAiIDogMTU5NjExOTEwOTUyOCwKICAicHJvZmlsZUlkIiA6ICJlM2I0NDVjODQ3ZjU0OGZiOGM4ZmEzZjFmN2VmYmE4ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5pRGlnZ2VyVGVzdCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hMTgzYzNlOGJhOGI2ZTIwNmQ0YzFhMjQ4M2UzYjNhYmE3MTY2YjM5NjczODA4YmU5MmJlMzIwMzI3MGIyNDhjIgogICAgfQogIH0KfQ==",
                "e4r8fhl+jz4F1KxHQCggSnYAm5h+2BIDBumRdNY+qKdC82PeyFUrhSpn9rCwJ2QT1jYMQaZ61I8MUOsNOP+8Qj7xcD7CkhJM9NxwTK2ba948BpR1w3ZIPpj7OPLmAIMZl0Ux8OFXmiK355gYdKzRyr2+37fvRAj1KkO6ov2dG8y1uybVUyKgedUB5jWHjM3idKENIjCf5at9tktW+sjyAtT3TDqkoF78Fe7rDzhSaVYGW81YaNJ3C08FBDZST7vxlDhMG4ADwmQdnlhFdGcRCbh4FPeV8T35eEBXHtynZ40iJctiJAjCIGLCWZI4UeG/Hh+vtmdrZBofosNsR8RuujvQ5SsHZqd1rXOiZ2PC3j0EmrOiZKoyBd9yO3MG8EBwx57qco+LFAurUg5JBMZn4TI4oI+pYh2y8aN68X1JEO4M6kMMCDTgijztFK969f37cyx4xjFdrZA91CIIRUqyDKt4x9dxkQ72vdfGplkiHZyEeUIWlaFCQIky3zsUa63tvVRNQ455/rBNTQthUIsq1Aq5Qewtps32IBryUGvHliZ1E9OH8CBaXX26P67Dx+JJwHxpKpMRdeohlk4MojpjWD+wjH3cIp79lzfGozll7605EfUwkSRKnRYZnFZF7+8Q46CJcmBWz51eXOTrGesbpbFlRQMp6N4deeKuU7KIkrE="
        ));

        /*
        Admin houses
         */

        this.npcHandler.registerNPC("sylent_",
                new NPC(
                        ChatColor.RED + "Sylent_",
                        true,
                        true,
                        false,
                        null,
                        new Location(world, -67.5, 69, -93.5),
                        (p) -> sendDelayedMessages(p, ChatColor.RED + "Sylent_", "You can hold as many talismans as you want in your inventory. They will always work."),
                        "ewogICJ0aW1lc3RhbXAiIDogMTY3MzAzNTIyNDg4MSwKICAicHJvZmlsZUlkIiA6ICI1MWU4OTE1ZGJlM2E0ZDU4OTQ2OTNhMTZmM2M2MGM3MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlbnRfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNGU2ZmVjZTVhNTY0MzNiYWFlYmE3ODc4NjYyYjIzNjJiNGZmYjdkNTIzOWVhZDgzZmZjOTUyMjFmOGViYzUiCiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIzNDBjMGUwM2RkMjRhMTFiMTVhOGIzM2MyYTdlOWUzMmFiYjIwNTFiMjQ4MWQwYmE3ZGVmZDYzNWNhN2E5MzMiCiAgICB9CiAgfQp9",
                        "NniVgyJmfWI5OK/7t9q4hWalHgVMO/VR5HqTod6DY+00qkCZgPkEGBxWbAdxvfE4yrVK/TaPRsMS7cJcSP3k74WYEPIPnXthNfNbzsGPz17Ns3fsc/w4GaLp688zo0OG8TRKRJA2ECcoD1B0kaAuRLKsO7NyBBj8XLL+pHv5QwNeMXg53x2OxsoTf25/BdcJJ9bNVv84V+bCAs1gspnbYBWaODcKj3qUUkUPXrm7nHeafi7snXCIRN1L088Qv3Lu7HTor3sojn9rH1FaFCLGIuVimvW+HWGRdkq3soXJrvFPyIZzstKQtXGUjOSG6V1cNPYXCVO/A6VtW1VmU2Lio9/Q7xvjLF9U3fcD4rXHZz6YUy5iZz6YbznjMAdRzYiFgg7lpRd8Au4Gcykf6873IX0YXucwcB4GiPUkRVnoCLI8j2UbdbHy8MO0ydvP1u1QamhWbRjorSJfUBbk6o2NYeYIfuZ+Gcki3xgWtrc1R0Q6FXN+K5Cir2GvXlQXjKrz2W0tAb8kkl3/4ITRZwoJN22i0CqoVq/FawFJNsW7lG/W7t29uxsaySj34gyDYsQag19DWAwUaazq207JNGVMcJCji/55qT4OxItFrjg2EvfpdJ+DbWLY2BlOABXhGKEjuYCFsABZcO9tI7gwW/+rkLIlGOqHfprtKGC/sHG63PE="
                ));

        SkyblockNPCManager.registerNPC(new FarmMerchant());
        SkyblockNPCManager.registerNPC(new Banker());
        SkyblockNPCManager.registerNPC(new AdventurerMerchant());
        SkyblockNPCManager.registerNPC(new AuctionAgentNPC1());
        SkyblockNPCManager.registerNPC(new AuctionAgentNPC2());
        SkyblockNPCManager.registerNPC(new AuctionAgentNPC3());
        SkyblockNPCManager.registerNPC(new AuctionAgentNPC4());

        SLog.info("Successfully registered NPCs [" + SUtil.getTimeDifferenceAndColor(start, System.currentTimeMillis()) + ChatColor.WHITE + "]");
    }

    public void unloadBlocks() {
        if (WorldListener.changed_blocks.isEmpty()) {
            return;
        }
        for (final Block block : WorldListener.changed_blocks) {
            if (WorldListener.CACHED_BLOCK_ID.containsKey(block) && WorldListener.CACHED_BLOCK_BYTE.containsKey(block)) {
                final int id = WorldListener.CACHED_BLOCK_ID.get(block);
                final byte data = WorldListener.CACHED_BLOCK_BYTE.get(block);
                block.setTypeIdAndData(id, data, true);
                if (!WorldListener.changed_blocks.contains(block)) {
                    continue;
                }
                WorldListener.changed_blocks.remove(block);
            }
        }
    }

    private void loadCommands() {
        this.cl.register(new JoinFloorCommand());
        this.cl.register(new NpcCommand());
        this.cl.register(new SkySimEngineCommand());
        this.cl.register(new RegionCommand());
        this.cl.register(new PlayEnumSoundCommand());
        this.cl.register(new PlayEnumEffectCommand());
        this.cl.register(new SpawnSpecCommand());
        this.cl.register(new ItemCommand());
        this.cl.register(new SpecEnchantmentCommand());
        this.cl.register(new SpecPotionCommand());
        this.cl.register(new SpecEffectsCommand());
        this.cl.register(new SpecReforgeCommand());
        this.cl.register(new ManaCommand());
        this.cl.register(new CoinsCommand());
        this.cl.register(new GUICommand());
        this.cl.register(new ItemBrowseCommand());
        this.cl.register(new SpecRarityCommand());
        this.cl.register(new RecombobulateCommand());
        this.cl.register(new NBTCommand());
        this.cl.register(new IslandCommand());
        this.cl.register(new DataCommand());
        this.cl.register(new SpecTestCommand());
        this.cl.register(new SoundSequenceCommand());
        this.cl.register(new BatphoneCommand());
        this.cl.register(new AbsorptionCommand());
        this.cl.register(new SkillsCommand());
        this.cl.register(new CollectionsCommand());
        this.cl.register(new MaterialDataCommand());
        this.cl.register(new EntitySpawnersCommand());
        this.cl.register(new AuctionHouseCommand());
        this.cl.register(new RebootServerCommand());
        this.cl.register(new HotPotatoBookCommand());
        this.cl.register(new RemoveEnchantCommand());
        this.cl.register(new EndCommand());
        this.cl.register(new EndDragonFightCommand());
        this.cl.register(new ToggleSBACommand());
        this.cl.register(new MembersEnchantCommand());
        this.cl.register(new ToggleRepeatingCommand());
        this.cl.register(new HubCommand());
        this.cl.register(new KillAllMobs());
        this.cl.register(new KillAllHostileMobs());
        this.cl.register(new CookieAHCommand());
        this.cl.register(new CookieAnvilCommand());
        this.cl.register(new CookieOpenBinCommand());
        this.cl.register(new CookieMerchantCommand());
        this.cl.register(new ResetCookieCommand());
        this.cl.register(new SkySimMenuCommand());
        this.cl.register(new BuyCookieCommand());
        this.cl.register(new SaveDataCommand());
        this.cl.register(new GiveSpaceHelmetCommand());
        this.cl.register(new SSTest());
        this.cl.register(new BuyBookCommand());
        this.cl.register(new BuyEPetCommand());
        this.cl.register(new InvRecovery());
        this.cl.register(new BuyItemCommand());
        this.cl.register(new BuyCommand());
        this.cl.register(new TradeCommand());
        this.cl.register(new AccessTimedCommand());
        this.cl.register(new ServerInfoCommand());
        this.cl.register(new APICommand());
        this.cl.register(new PickupStashCommand());
        this.cl.register(new StackMyDimoon());
        cl.register(new AdminItemCommand());
        cl.register(new ProfileCommand());
        cl.register(new HexCommand());
        cl.register(new Quest());
        // todo use reflection!
    }

    private void loadListeners() {
        new in.godspunky.skyblock.listener.BlockListener();
        new in.godspunky.skyblock.listener.PlayerListener();
        new ServerPingListener();
        new ItemListener();
        new MinionListener();
        new GUIListener();
        new PacketListener();
        new WorldListener();
        new PlayerJoinQuitListener();
        new PlayerChatListener();
    }

    private void startPopulators() {
        new EntityPopulator(5, 10, 200L, SEntityType.ENCHANTED_DIAMOND_SKELETON, RegionType.OBSIDIAN_SANCTUARY).start();
        new EntityPopulator(5, 10, 200L, SEntityType.ENCHANTED_DIAMOND_ZOMBIE, RegionType.OBSIDIAN_SANCTUARY).start();
        new EntityPopulator(5, 10, 200L, SEntityType.DIAMOND_ZOMBIE, RegionType.DIAMOND_RESERVE).start();
        new EntityPopulator(5, 10, 200L, SEntityType.DIAMOND_SKELETON, RegionType.DIAMOND_RESERVE).start();
        new EntityPopulator(5, 15, 200L, SEntityType.SMALL_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 10, 200L, SEntityType.MEDIUM_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 5, 400L, SEntityType.LARGE_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 30, 400L, SEntityType.PIGMAN, RegionType.PIGMENS_DEN).start();
        new EntityPopulator(5, 30, 400L, SEntityType.LAPIS_ZOMBIE, RegionType.LAPIS_QUARRY).start();
        new EntityPopulator(5, 10, 400L, SEntityType.SNEAKY_CREEPER, RegionType.GUNPOWDER_MINES).start();
        new EntityPopulator(6, 20, 300L, SEntityType.WEAK_ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(6, 20, 300L, SEntityType.ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(6, 20, 300L, SEntityType.STRONG_ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(10, 30, 200L, SEntityType.ZEALOT, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(1, 5, 1200L, SEntityType.ENDER_CHEST_ZEALOT, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(5, 20, 200L, SEntityType.WATCHER, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(5, 10, 200L, SEntityType.OBSIDIAN_DEFENDER, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(5, 20, 300L, SEntityType.SPLITTER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300L, SEntityType.WEAVER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300L, SEntityType.VORACIOUS_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300L, SEntityType.SPIDER_JOCKEY, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300L, SEntityType.DASHER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 10, 300L, SEntityType.HIGH_LEVEL_SKELETON, RegionType.HIGH_LEVEL, world -> world.getTime() >= 13188L && world.getTime() <= 22812L).start();
        new EntityPopulator(5, 15, 200L, SEntityType.ZOMBIE, RegionType.GRAVEYARD).start();
        new EntityPopulator(5, 15, 200L, SEntityType.ZOMBIE_VILLAGER, RegionType.GRAVEYARD).start();
        new EntityPopulator(5, 20, 200L, SEntityType.WOLF, RegionType.RUINS).start();
        new EntityPopulator(2, 4, 200L, SEntityType.OLD_WOLF, RegionType.RUINS).start();
        new EntityPopulator(5, 30, 200L, SEntityType.CRYPT_GHOUL, RegionType.COAL_MINE_CAVES).start();
        new EntityPopulator(1, 1, 200L, SEntityType.GOLDEN_GHOUL, RegionType.COAL_MINE_CAVES).start();
        new EntityPopulator(4, 4, 200L, SEntityType.SOUL_OF_THE_ALPHA, RegionType.HOWLING_CAVE).start();
        new EntityPopulator(5, 15, 200L, SEntityType.HOWLING_SPIRIT, RegionType.HOWLING_CAVE).start();
        new EntityPopulator(5, 15, 200L, SEntityType.PACK_SPIRIT, RegionType.HOWLING_CAVE).start();
    }

    private void loadSerializableClasses() {
        ConfigurationSerialization.registerClass(SlayerQuest.class, "SlayerQuest");
        ConfigurationSerialization.registerClass(Pet.PetItem.class, "PetItem");
        ConfigurationSerialization.registerClass(SItem.class, "SItem");
        ConfigurationSerialization.registerClass(AuctionSettings.class, "AuctionSettings");
        ConfigurationSerialization.registerClass(AuctionEscrow.class, "AuctionEscrow");
        ConfigurationSerialization.registerClass(SerialNBTTagCompound.class, "SerialNBTTagCompound");
        ConfigurationSerialization.registerClass(AuctionBid.class, "AuctionBid");
    }

    public void fixTheEnd() {
        SLog.info("No Tasks");
    }

    public void beginLoopA() {
        new BukkitRunnable() {
            public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getWorld().getName().startsWith(SkyblockIsland.ISLAND_PREFIX)) {
                        if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                            p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        }
                    } else {
                        if (!p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000, 255));
                        }
                    }
                }
            }
        }.runTaskTimer(Skyblock.plugin, 0L, 5L);
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>) this.getServer().getServicesManager().getRegistration((Class) Economy.class);
        if (rsp == null) {
            return false;
        }
        Skyblock.econ = rsp.getProvider();
        return Skyblock.econ != null;
    }

    private void registerPacketListener() {
        PacketHelper.addPacketHandler(new PacketHandler() {
            @Override
            public void onReceive(final ReceivedPacket packet) {
                final PacketReceiveServerSideEvent ev = new PacketReceiveServerSideEvent(packet);
                Bukkit.getPluginManager().callEvent(ev);
            }

            @Override
            public void onSend(final SentPacket packet) {
                final PacketSentServerSideEvent ev = new PacketSentServerSideEvent(packet);
                Bukkit.getPluginManager().callEvent(ev);
            }
        });
    }


    private void registerPingListener() {
        PingAPI.registerListener(new PingListener() {
            @Override
            public void onPing(final PingEvent event) {
                final SkySimServerPingEvent e = new SkySimServerPingEvent(event);
                Bukkit.getPluginManager().callEvent(e);
            }
        });
    }

    public void async(final Runnable runnable) {
        new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(Skyblock.plugin);
    }

    public BukkitTask syncLoop(final Runnable runnable, final int i0, final int i1) {
        return new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskTimer(Skyblock.plugin, i0, i1);
    }

    public BukkitTask asyncLoop(final Runnable runnable, final int i0, final int i1) {
        return new BukkitRunnable() {
            public void run() {
                runnable.run();
            }
        }.runTaskTimerAsynchronously(Skyblock.plugin, i0, i1);
    }

    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        final PluginMessageReceived e = new PluginMessageReceived(new WrappedPluginMessage(channel, player, message));
        Bukkit.getPluginManager().callEvent(e);
    }

    public void updateServerName(final Player player) {
        SkySimBungee.getNewBungee().sendData(player, "GetServer", null);
    }

    public void updateServerPlayerCount() {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            SkySimBungee.getNewBungee().sendData(null, "PlayerCount", "ALL");
        }
    }

    public void accept(final String channel, final Player player, final byte[] data) {
        if (Objects.equals(channel, "savePlayerData")) {
            SLog.info("YES IT WORK");
            for (final Player p : Bukkit.getOnlinePlayers()) {
                final User u = User.getUser(p.getUniqueId());
                u.syncSavingData();
            }
        }
    }
}
