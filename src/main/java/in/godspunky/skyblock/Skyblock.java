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
import in.godspunky.skyblock.item.ItemListener;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.armor.VoidlingsWardenHelmet;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.listener.PacketListener;
import in.godspunky.skyblock.listener.ServerPingListener;
import in.godspunky.skyblock.listener.WorldListener;
import in.godspunky.skyblock.merchant.MerchantItemHandler;
import in.godspunky.skyblock.minion.MinionListener;
import in.godspunky.skyblock.nms.packetevents.*;
import in.godspunky.skyblock.npc.SkyblockNPC;
import in.godspunky.skyblock.ranks.PlayerChatListener;
import in.godspunky.skyblock.ranks.PlayerJoinQuitListener;
import in.godspunky.skyblock.ranks.SetRankCommand;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.user.AuctionSettings;
import in.godspunky.skyblock.user.DatabaseManager;
import in.godspunky.skyblock.user.SMongoLoader;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.*;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import net.swofty.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
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
import in.godspunky.skyblock.gui.GUIListener;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.nms.nmsutil.apihelper.APIManager;
import in.godspunky.skyblock.nms.nmsutil.apihelper.SkySimBungee;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.PacketHelper;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.PacketHandler;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import in.godspunky.skyblock.nms.nmsutil.packetlistener.metrics.Metrics;
import in.godspunky.skyblock.nms.pingrep.PingAPI;
import in.godspunky.skyblock.nms.pingrep.PingEvent;
import in.godspunky.skyblock.nms.pingrep.PingListener;
import in.godspunky.skyblock.npc.SkyblockNPCManager;
import in.godspunky.skyblock.sql.SQLDatabase;
import in.godspunky.skyblock.sql.SQLRegionData;
import in.godspunky.skyblock.sql.SQLWorldData;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@SuppressWarnings("deprecation")
public class Skyblock extends JavaPlugin implements PluginMessageListener, BungeeChannel.ForwardConsumer {
   // public static MultiverseCore core;
    private static ProtocolManager protocolManager;
    private static Economy econ;
    private static Skyblock plugin;
    private final PacketHelper packetInj;
    public Arena arena;
    public Dimoon dimoon;
    public SummoningSequence sq;
    public boolean altarCooldown;
    @Getter
    private final ServerVersion serverVersion;
    public static EffectManager effectManager;
    private static Skyblock instance;
    public Config config;
    @Getter
    private SlimePlugin slimePlugin;
    public Config heads;
    public Config blocks;
    public Config spawners;
    @Getter
    @Setter
    private int onlinePlayerAcrossServers;
    public CommandMap commandMap;

    @Getter
    public SMongoLoader dataLoader;
    public SQLDatabase sql;
    public SQLRegionData regionData;
    public SQLWorldData worldData;
    public CommandLoader cl;
    public Repeater repeater;
    @Getter
    private BungeeChannel bc;
    @Getter
    @Setter
    private String serverName;

    public List<String> bannedUUID;

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
        this.sql = new SQLDatabase();
        this.dataLoader = new SMongoLoader();
        this.regionData = new SQLRegionData();
        this.worldData = new SQLWorldData();
        this.cl = new CommandLoader();
        SLog.info("Begin Protocol injection... (SkySimProtocol v0.6.2)");
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
        SLog.info("Loading NPCS...");
        registerNPCS();
        SLog.info("Loading auction items from disk...");
        Skyblock.effectManager = new EffectManager(this);
        AuctionItem.loadAuctionsFromDisk();
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

    private void loadWorlds(){
        new BlankWorldCreator("f6").createWorld();
        new BlankWorldCreator("arena").createWorld();
        //new BlankWorldCreator("f1");
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

    private void registerNPCS()
    {
        Reflections reflections = new Reflections("in.godspunky.skyblock.npc");
        for (Class<? extends SkyblockNPC> npcClazz : reflections.getSubTypesOf(SkyblockNPC.class)){
            try {
                npcClazz.getDeclaredConstructor().newInstance();
            }catch (Exception ex){
                ex.printStackTrace();

            }
        }
        SLog.info(ChatColor.GREEN + "Successfully loaded " + ChatColor.YELLOW + SkyblockNPCManager.getNPCS().size() + ChatColor.GREEN + " NPCs");

    }


    public static ProtocolManager getPTC() {
        return Skyblock.protocolManager;
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

    public static Skyblock getInstance() {
        return Skyblock.instance;
    }

    public void fixTheEnd() {
        SLog.info("No Tasks");
    }

    public void beginLoopA() {
        new BukkitRunnable() {
            public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000, 255));
                }
            }
        }.runTaskTimer(Skyblock.plugin, 0L, 1L);
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

    static {
      //  Skyblock.core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        Skyblock.econ = null;
    }
}
