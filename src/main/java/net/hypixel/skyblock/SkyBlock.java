package net.hypixel.skyblock;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.io.Files;
import de.slikey.effectlib.EffectManager;
import net.hypixel.skyblock.api.placeholder.SkyblockPlaceholder;
import net.hypixel.skyblock.api.worldmanager.SkyBlockWorldManager;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.command.*;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.entity.dimoon.*;
import net.hypixel.skyblock.entity.dimoon.listeners.BlockListener;
import net.hypixel.skyblock.entity.dimoon.listeners.EntityListener;
import net.hypixel.skyblock.entity.dimoon.listeners.PlayerListener;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.entity.EntityPopulator;
import net.hypixel.skyblock.entity.EntitySpawner;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.item.armor.VoidlingsWardenHelmet;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.listener.PacketListener;
import net.hypixel.skyblock.listener.ServerPingListener;
import net.hypixel.skyblock.listener.WorldListener;
import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.nms.packetevents.*;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.server.ServerVersion;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.user.AuctionSettings;
import net.hypixel.skyblock.util.*;
import lombok.Getter;
import lombok.Setter;
import net.swofty.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.reflections.Reflections;
import net.hypixel.skyblock.gui.GUIListener;
import net.hypixel.skyblock.nms.nmsutil.apihelper.APIManager;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.PacketHelper;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.PacketHandler;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.metrics.Metrics;
import net.hypixel.skyblock.nms.pingrep.PingAPI;
import net.hypixel.skyblock.nms.pingrep.PingEvent;
import net.hypixel.skyblock.nms.pingrep.PingListener;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.sql.SQLDatabase;
import net.hypixel.skyblock.sql.SQLRegionData;
import net.hypixel.skyblock.sql.SQLWorldData;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SkyBlock extends JavaPlugin implements PluginMessageListener {
    @Getter
    private static ProtocolManager protocolManager;
    @Getter
    private static SkyBlock plugin;
    private final PacketHelper packetInj;

    public static final boolean dimoonEnabled = false;

    public static final String[] DEVELOPERS = {"Hamza" , "EpicPortal" , "Dumbo"};

    public Arena arena;
    public Dimoon dimoon;
    public SummoningSequence sq;
    public boolean altarCooldown;
    @Getter
    private final ServerVersion serverVersion;
    public static EffectManager effectManager;
    @Getter
    private static SkyBlock instance;
    public Config config;
    @Getter
    private SlimePlugin slimePlugin;
    public Config heads;
    public Config blocks;
    public Config spawners;
    @Setter
    @Getter
    private int onlinePlayerAcrossServers;
    public CommandMap commandMap;
    public SQLDatabase sql;
    public SQLRegionData regionData;
    public SQLWorldData worldData;
    public CommandLoader cl;
    public Repeater repeater;
    @Getter
    private BungeeChannel bc;
    @Setter
    @Getter
    private String serverName;

    public List<String> bannedUUID;

    public SkyBlock() {
        this.packetInj = new PacketHelper();
        this.arena = null;
        this.dimoon = null;
        this.sq = null;
        this.altarCooldown = false;
        this.serverVersion = new ServerVersion("beta", 0, 7, 2, 0);
        this.serverName = "dev";
        this.bannedUUID = Collections.singletonList("");
    }

    public void onLoad() {
        SLog.info("Loading Bukkit-serializable classes...");
        this.loadSerializableClasses();
    }

    public void onEnable() {
        plugin = this;
        SLog.info("Loading SkyBlock worlds...");
        SkyBlockWorldManager.loadWorlds();
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
        this.sql = new SQLDatabase();
        this.regionData = new SQLRegionData();
        this.worldData = new SQLWorldData();
        this.cl = new CommandLoader();



        SLog.info("Begin Protocol injection... (SkyBlockProtocol v0.6.2)");
        APIManager.registerAPI(this.packetInj, this);
        if (!this.packetInj.injected) {
            this.getLogger().warning("[FATAL ERROR] Protocol Injection failed. Disabling the plugin for safety...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SwoftyWorldManager");
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
        effectManager = new EffectManager(this);
        AuctionItem.loadAuctionsFromDisk();
        SLog.info("Loading merchants prices...");
        MerchantItemHandler.init();
        SLog.info("Synchronizing world time with calendar time and removing world entities...");
        SkyBlockCalendar.synchronize();
        SLog.info("Loading items...");
        SMaterial.loadItems();
        SLog.info("Converting CraftRecipes into custom recipes...");
        Recipe.loadRecipes();
        SLog.info("Hooking SkyBlockEngine to PlaceholderAPI and registering...");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SkyblockPlaceholder().register();
            SLog.info("Hooked to PAPI successfully!");
        } else {
            SLog.info("ERROR! PlaceholderAPI plugin does not exist, disabing placeholder request!");
        }
        protocolManager = ProtocolLibrary.getProtocolManager();
        WorldListener.init();

        SLog.info("Successfully enabled " + this.getDescription().getFullName());
        SLog.info("===================================");
        SLog.info("SkyBlock ENGINE - MADE BY " + getDevelopersName());
        SLog.info("PLUGIN ENABLED! HOOKED INTO SkyBlock!");
        SLog.info(" ");
        SLog.info("This plugin provide most of SkyBlock functions!");
        SLog.info("Originally was made by super");
        SLog.info("Continued by GodSpunky (C) 2024");
        SLog.info("Any illegal usage will be suppressed! DO NOT LEAK IT!");
        SLog.info("===================================");
        if (dimoonEnabled) {
           initDimoon();
        }
        startPopulators();
    }


    private void initDimoon(){
        this.sq = new SummoningSequence(Bukkit.getWorld("arena"));
        Bukkit.getWorld("arena").setAutoSave(false);
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
        DimoonLootTable.highQualitylootTable = new ArrayList<>(Arrays.asList(new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOONIZARY_DAGGER), 400, 1100), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_EXCRARION), 310, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_HELMET), 290, 700), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_CHESTPLATE), 340, 900), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_LEGGINGS), 330, 800), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GIGACHAD_BOOTS), 220, 500), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_QUANTUMFLUX_POWER_ORB), 310, 900), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_ARCHIVY), 370, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_MAGICIVY), 370, 1000), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022), 320, 900), new DimoonLootItem(gtigerm, 300, 1000), new DimoonLootItem(lucki8, 170, 700), new DimoonLootItem(vicious15, 100, 600), new DimoonLootItem(chimera6, 260, 700), new DimoonLootItem(tbits, 210, 700)));
        final SItem lucki9 = SItem.of(SMaterial.ENCHANTED_BOOK);
        lucki9.addEnchantment(EnchantmentType.LUCKINESS, 6);
        DimoonLootTable.lowQualitylootTable = new ArrayList<>(Arrays.asList(new DimoonLootItem(lucki9, 20, 150), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOON_GEM), 20, 100), new DimoonLootItem(SItem.of(SMaterial.HIDDEN_DIMOON_FRAG), 1, 1, 0, true)));
        Arena.cleanArena();
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
        if (this.repeater != null && EntitySpawner.class != null && EntitySpawner.class != null && StaticDragonManager.class != null && SkyBlockCalendar.class != null) {
            SLog.info("Stopping server loop...");
            this.repeater.stop();
            SLog.info("Unloading ores from Dwarven Mines...");
            WorldListener.unloadBlocks();
            SLog.info("Ejecting protocol channel...");
            APIManager.disableAPI(PacketHelper.class);
            SLog.info("Cleaning HashSets...");
            for (final Map.Entry<Entity, Block> entry : VoidgloomSeraph.CACHED_BLOCK.entrySet()) {
                final Entity stand = entry.getKey();
                if (stand != null && VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                    VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                }
            }
            //this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
            //this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
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
            plugin = null;
        }
        SLog.info("Disabled " + this.getDescription().getFullName());
        SLog.info("===================================");
        SLog.info("SkyBlock ENGINE - MADE BY GIAKHANHVN");
        SLog.info("PLUGIN DISABLED!");
        SLog.info("===================================");
    }

    private void registerNPCS()
    {
        Reflections reflections = new Reflections("net.hypixel.skyblock.npc");
        for (Class<? extends SkyblockNPC> npcClazz : reflections.getSubTypesOf(SkyblockNPC.class)){
            try {
                npcClazz.getDeclaredConstructor().newInstance();
            }catch (Exception ex){
                ex.printStackTrace();

            }
        }
        SLog.info(ChatColor.GREEN + "Successfully loaded " + ChatColor.YELLOW + SkyblockNPCManager.getNPCS().size() + ChatColor.GREEN + " NPCs");
    }




    private void loadCommands() {
        Reflections reflections = new Reflections("net.hypixel.skyblock.command");

        for (Class<? extends SCommand> command : reflections.getSubTypesOf(SCommand.class)) {
            try {
                cl.register(command.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException exception) {
                SLog.severe("An exception occured when loading " + command.getSimpleName());
                SLog.severe(exception.getMessage());
            }
        }
    }

    private void loadListeners() {
        new net.hypixel.skyblock.listener.BlockListener();
        new net.hypixel.skyblock.listener.PlayerListener();
        new ServerPingListener();
        new ItemListener();
        new GUIListener();
        new PacketListener();
        new WorldListener();
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


    public String getDevelopersName(){
        StringBuilder builder = new StringBuilder();
        for (String name : DEVELOPERS){
            builder.append(name).append(" , ");
        }
        return builder.toString();
    }


    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        final PluginMessageReceived e = new PluginMessageReceived(new WrappedPluginMessage(channel, player, message));
        Bukkit.getPluginManager().callEvent(e);
    }

}
