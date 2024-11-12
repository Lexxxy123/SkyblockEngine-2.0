/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.ProtocolManager
 *  de.slikey.effectlib.EffectManager
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandMap
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.skyblock.skyblock.reflections.Reflections;
import com.skyblock.skyblock.reflections.scanners.Scanner;
import de.slikey.effectlib.EffectManager;
import dev.demeng.sentinel.wrapper.SentinelClient;
import dev.demeng.sentinel.wrapper.exception.BlacklistedLicenseException;
import dev.demeng.sentinel.wrapper.exception.ConnectionMismatchException;
import dev.demeng.sentinel.wrapper.exception.ExcessiveIpsException;
import dev.demeng.sentinel.wrapper.exception.ExcessiveServersException;
import dev.demeng.sentinel.wrapper.exception.ExpiredLicenseException;
import dev.demeng.sentinel.wrapper.exception.InvalidLicenseException;
import dev.demeng.sentinel.wrapper.exception.InvalidPlatformException;
import dev.demeng.sentinel.wrapper.exception.InvalidProductException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.api.placeholder.SkyblockPlaceholder;
import net.hypixel.skyblock.api.worldmanager.SkyBlockWorldManager;
import net.hypixel.skyblock.command.CommandLoader;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.database.SQLDatabase;
import net.hypixel.skyblock.database.SQLRegionData;
import net.hypixel.skyblock.database.SQLWorldData;
import net.hypixel.skyblock.entity.EntityPopulator;
import net.hypixel.skyblock.entity.EntitySpawner;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.punishment.JoinLeaveEvent;
import net.hypixel.skyblock.features.punishment.PlayerChat;
import net.hypixel.skyblock.features.quest.QuestLineHandler;
import net.hypixel.skyblock.features.ranks.SetRankCommand;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.gui.GUIListener;
import net.hypixel.skyblock.item.ItemListener;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.listener.BlockListener;
import net.hypixel.skyblock.listener.PacketListener;
import net.hypixel.skyblock.listener.PlayerChatListener;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.listener.ServerPingListener;
import net.hypixel.skyblock.listener.WorldListener;
import net.hypixel.skyblock.nms.nmsutil.apihelper.APIManager;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.PacketHelper;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.PacketHandler;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.ReceivedPacket;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.handler.SentPacket;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.metrics.Metrics;
import net.hypixel.skyblock.nms.packetevents.PacketReceiveServerSideEvent;
import net.hypixel.skyblock.nms.packetevents.PacketSentServerSideEvent;
import net.hypixel.skyblock.nms.packetevents.PluginMessageReceived;
import net.hypixel.skyblock.nms.packetevents.SkySimServerPingEvent;
import net.hypixel.skyblock.nms.packetevents.WrappedPluginMessage;
import net.hypixel.skyblock.nms.pingrep.PingAPI;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.user.AuctionSettings;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.BungeeChannel;
import net.hypixel.skyblock.util.CC;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.SerialNBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyBlock
extends JavaPlugin
implements PluginMessageListener {
    private static ProtocolManager protocolManager;
    private static SkyBlock plugin;
    private final PacketHelper packetInj = new PacketHelper();
    private boolean authenticated;
    public static final String[] DEVELOPERS;
    public boolean altarCooldown = false;
    public static EffectManager effectManager;
    public Config config;
    public Config heads;
    public Config blocks;
    public Config spawners;
    private Config databaseInfo;
    private QuestLineHandler questLineHandler;
    private int onlinePlayerAcrossServers;
    public CommandMap commandMap;
    public SQLDatabase sql;
    public SQLRegionData regionData;
    public SQLWorldData worldData;
    public CommandLoader cl;
    public Repeater repeater;
    private BungeeChannel bc;
    private String serverName = "dev";
    public List<String> bannedUUID = Collections.singletonList("");

    public void onLoad() {
        SLog.info("Loading Bukkit-serializable classes...");
        this.loadSerializableClasses();
    }

    public void onEnable() {
        plugin = this;
        this.sendMessage("&aEnabling Skyblock Core. Made by " + this.getDevelopersName());
        long start = System.currentTimeMillis();
        new BukkitRunnable(){

            public void run() {
                SkyBlock.this.sendMessage("Cleaning up the JVM (This may cause a short lag spike!)");
                long before = System.currentTimeMillis();
                System.gc();
                long after = System.currentTimeMillis();
                SkyBlock.this.sendMessage("It took " + (after - before) + "ms to cleanup the JVM heap");
            }
        }.runTaskTimer((Plugin)this, 1L, 12000L);
        this.sendMessage("&aLoading SkyBlock worlds...");
        SkyBlockWorldManager.loadWorlds();
        this.sendMessage("&aLoading YAML data from disk...");
        this.config = new Config("config.yml");
        this.heads = new Config("heads.yml");
        this.blocks = new Config("blocks.yml");
        this.spawners = new Config("spawners.yml");
        this.databaseInfo = new Config("database.yml");
        this.sendMessage("&aLoading Command map...");
        try {
            Field f2 = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f2.setAccessible(true);
            this.commandMap = (CommandMap)f2.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e2) {
            SLog.severe("Couldn't load command map: ");
            e2.printStackTrace();
        }
        this.sendMessage("&aLoading SQL database...");
        this.sql = new SQLDatabase();
        if (!this.config.getBoolean("Config")) {
            try {
                DatabaseManager.connectToDatabase(this.databaseInfo.getString("uri"), this.databaseInfo.getString("name"));
            } catch (Exception ex) {
                SLog.warn("An error occurred while connecting to mongodb with uri : " + this.databaseInfo.getString("uri"));
            }
        }
        this.regionData = new SQLRegionData();
        this.worldData = new SQLWorldData();
        this.cl = new CommandLoader();
        this.sendMessage("&aBegin Protocol injection... (SkyBlockProtocol v0.6.2)");
        APIManager.registerAPI(this.packetInj, (Plugin)this);
        if (!this.packetInj.injected) {
            this.getLogger().warning("[FATAL ERROR] Protocol Injection failed. Disabling the plugin for safety...");
            Bukkit.getPluginManager().disablePlugin((Plugin)this);
            return;
        }
        this.sendMessage("&aInjecting...");
        PingAPI.register();
        new Metrics(this);
        APIManager.initAPI(PacketHelper.class);
        this.sendMessage("&aStarting server loop...");
        this.repeater = new Repeater();
        this.sendMessage("&aLoading commands...");
        this.loadCommands();
        this.sendMessage("&aLoading listeners...");
        this.loadListeners();
        this.sendMessage("&aInjecting Packet/Ping Listener into the core...");
        this.registerPacketListener();
        this.registerPingListener();
        this.sendMessage("&aStarting entity spawners...");
        EntitySpawner.startSpawnerTask();
        this.sendMessage("&aEstablishing player regions...");
        Region.cacheRegions();
        this.sendMessage("&aLoading NPCS...");
        this.registerNPCS();
        this.sendMessage("&aLoading auction items from disk...");
        effectManager = new EffectManager((Plugin)this);
        AuctionItem.loadAuctionsFromDisk();
        this.sendMessage("&aLoading Quest!");
        this.initializeQuests();
        this.sendMessage("&aLoading merchants prices...");
        MerchantItemHandler.init();
        this.sendMessage("&aSynchronizing world time with calendar time and removing world entities...");
        SkyBlockCalendar.synchronize();
        this.sendMessage("&aLoading items...");
        SMaterial.loadItems();
        this.sendMessage("&aConverting CraftRecipes into custom recipes...");
        Recipe.loadRecipes();
        if (!this.config.getBoolean("Config")) {
            this.sendMessage("&aLoading recipes from database...");
            RecipeDatabase.loadRecipes();
        }
        this.sendMessage("&aHooking SkyBlockEngine to PlaceholderAPI and registering...");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SkyblockPlaceholder().register();
            this.sendMessage("&aHooked to PAPI successfully!");
        } else {
            this.sendMessage("&aERROR! PlaceholderAPI plugin does not exist, disabing placeholder request!");
        }
        protocolManager = ProtocolLibrary.getProtocolManager();
        WorldListener.init();
        this.sendMessage("&aSuccessfully enabled " + this.getDescription().getFullName());
        this.sendMessage("&a===================================");
        this.sendMessage("&aSkyBlock ENGINE - MADE BY " + this.getDevelopersName());
        this.sendMessage("&aPLUGIN ENABLED! HOOKED INTO SkyBlock!");
        this.sendMessage("&a ");
        this.sendMessage("&aThis plugin provide most of SkyBlock functions!");
        this.sendMessage("&aOriginally was made by super");
        this.sendMessage("&aContinued by GodSpunky (C) 2024");
        this.sendMessage("&aAny illegal usage will be suppressed! DO NOT LEAK IT!");
        this.sendMessage("&a===================================");
        this.startPopulators();
        this.getCommand("setrank").setExecutor((CommandExecutor)new SetRankCommand());
        long end = System.currentTimeMillis();
        this.sendMessage("&aSuccessfully enabled Hub Core in " + CC.getTimeDifferenceAndColor(start, end) + "&a.");
    }

    public static SkyBlock getInstance() {
        return plugin;
    }

    private void initializeQuests() {
        this.sendMessage("&aInitializing quests...");
        long start = System.currentTimeMillis();
        this.questLineHandler = new QuestLineHandler();
        this.sendMessage("&aSuccessfully registered " + ChatColor.GREEN + this.questLineHandler.getQuests().size() + ChatColor.WHITE + " quests [" + SUtil.getTimeDifferenceAndColor(start, System.currentTimeMillis()) + ChatColor.WHITE + "]");
    }

    public void onDisable() {
        this.sendMessage("&aSaving Player data...");
        for (User user : User.getCachedUsers()) {
            if (user == null || user.getUuid() == null) continue;
            if (this.config.getBoolean("Config")) {
                user.configsave();
                continue;
            }
            user.save().thenRun(user::kick);
        }
        this.sendMessage("&aKilling all non-human entities...");
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) continue;
                entity.remove();
            }
        }
        if (this.repeater != null && EntitySpawner.class != null && EntitySpawner.class != null && StaticDragonManager.class != null && SkyBlockCalendar.class != null) {
            this.sendMessage("&aStopping server loop...");
            this.repeater.stop();
            this.sendMessage("&aUnloading ores from Dwarven Mines...");
            WorldListener.unloadBlocks();
            this.sendMessage("&aEjecting protocol channel...");
            APIManager.disableAPI(PacketHelper.class);
            this.sendMessage("&aCleaning HashSets...");
            for (Map.Entry entry : VoidgloomSeraph.CACHED_BLOCK.entrySet()) {
                Entity stand = (Entity)entry.getKey();
                if (stand == null || !VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) || !VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) || !VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) continue;
                VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand).intValue(), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand).byteValue(), true);
            }
            this.sendMessage("&aStopping entity spawners...");
            EntitySpawner.stopSpawnerTask();
            this.sendMessage("&aEnding Dragons fight... (If one is currently active)");
            StaticDragonManager.endFight();
            this.sendMessage("&aSaving calendar time...");
            SkyBlockCalendar.saveElapsed();
            this.sendMessage("&aSaving auction data...");
            for (AuctionItem auctionItem : AuctionItem.getAuctions()) {
                auctionItem.save();
            }
            plugin = null;
        }
        this.sendMessage("&aDisabled " + this.getDescription().getFullName());
        this.sendMessage("&a===================================");
        this.sendMessage("&aSkyBlock ENGINE - MADE BY " + this.getDevelopersName());
        this.sendMessage("&aPLUGIN DISABLED!");
        this.sendMessage("&a===================================");
    }

    private void registerNPCS() {
        Reflections reflections = new Reflections("net.hypixel.skyblock.npc", new Scanner[0]);
        for (Class<SkyBlockNPC> npcClazz : reflections.getSubTypesOf(SkyBlockNPC.class)) {
            try {
                npcClazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.sendMessage("&aSuccessfully loaded &e" + SkyblockNPCManager.getNPCS().size() + "&a NPCs");
    }

    private void loadCommands() {
        Reflections reflections = new Reflections("net.hypixel.skyblock.command", new Scanner[0]);
        this.sendMessage("&eRegistering commands...");
        int count = 0;
        for (Class<SCommand> command : reflections.getSubTypesOf(SCommand.class)) {
            try {
                this.cl.register(command.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                ++count;
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
                SLog.severe("An exception occured when loading " + command.getSimpleName());
                SLog.severe(exception.getMessage());
            }
        }
        this.sendMessage("&eRegistered " + count + " commands");
    }

    private void loadListeners() {
        new BlockListener();
        new PlayerListener();
        new ServerPingListener();
        new ItemListener();
        new GUIListener();
        new PacketListener();
        new WorldListener();
        new PlayerChatListener();
        new PlayerChat();
        new JoinLeaveEvent();
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
        ConfigurationSerialization.registerClass(SlayerQuest.class, (String)"SlayerQuest");
        ConfigurationSerialization.registerClass(Pet.PetItem.class, (String)"PetItem");
        ConfigurationSerialization.registerClass(SItem.class, (String)"SItem");
        ConfigurationSerialization.registerClass(AuctionSettings.class, (String)"AuctionSettings");
        ConfigurationSerialization.registerClass(AuctionEscrow.class, (String)"AuctionEscrow");
        ConfigurationSerialization.registerClass(SerialNBTTagCompound.class, (String)"SerialNBTTagCompound");
        ConfigurationSerialization.registerClass(AuctionBid.class, (String)"AuctionBid");
    }

    private void registerPacketListener() {
        PacketHelper.addPacketHandler(new PacketHandler(){

            @Override
            public void onReceive(ReceivedPacket packet) {
                PacketReceiveServerSideEvent ev = new PacketReceiveServerSideEvent(packet);
                Bukkit.getPluginManager().callEvent((Event)ev);
            }

            @Override
            public void onSend(SentPacket packet) {
                PacketSentServerSideEvent ev = new PacketSentServerSideEvent(packet);
                Bukkit.getPluginManager().callEvent((Event)ev);
            }
        });
    }

    private void registerPingListener() {
        PingAPI.registerListener(event -> {
            SkySimServerPingEvent e2 = new SkySimServerPingEvent(event);
            Bukkit.getPluginManager().callEvent((Event)e2);
        });
    }

    public static Player findPlayerByIPAddress(String ip) {
        for (Player p2 : Bukkit.getOnlinePlayers()) {
            if (!p2.getAddress().toString().contains(ip)) continue;
            return p2;
        }
        return null;
    }

    public String getDevelopersName() {
        StringBuilder builder = new StringBuilder();
        for (String name : DEVELOPERS) {
            builder.append(name).append(" , ");
        }
        return builder.toString().substring(0, builder.length() - 2);
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PluginMessageReceived e2 = new PluginMessageReceived(new WrappedPluginMessage(channel, player, message));
        Bukkit.getPluginManager().callEvent((Event)e2);
    }

    private void authenticate() {
        String licenseKey = this.config.getString("licenseKey");
        SentinelClient client = new SentinelClient("http://authentication.gsdevelopments.in:2222/api/v1", "rhvs43epk4onhk9atpqrvivli5", "GYCZMI3M4rNYap1Q0JuSmM3b2MR0vaE+U2Kf7KWH1rg=");
        this.authenticated = false;
        try {
            client.getLicenseController().auth(licenseKey, "SkyblockCore", null, null, SentinelClient.getCurrentHwid(), SentinelClient.getCurrentIp());
            this.authenticated = true;
        } catch (InvalidLicenseException e2) {
            System.out.println("Invalid license key.");
        } catch (ExpiredLicenseException e3) {
            System.out.println("Expired.");
        } catch (BlacklistedLicenseException e4) {
            System.out.println("Blacklisted.");
        } catch (ConnectionMismatchException e5) {
            System.out.println("Provided connection does not match.");
        } catch (ExcessiveServersException e6) {
            System.out.println("Too many servers. (Max: " + e6.getMaxServers() + ")");
        } catch (ExcessiveIpsException e7) {
            System.out.println("Too many IPs. (Max: " + e7.getMaxIps() + ")");
        } catch (InvalidProductException e8) {
            System.out.println("License is for different product.");
        } catch (InvalidPlatformException e9) {
            System.out.println("Provided connection platform is invalid.");
        } catch (IOException e10) {
            System.out.println("An unexpected error occurred.");
        }
        if (this.authenticated) {
            System.out.println("Successfully authenticated.");
        }
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)"&7[&aHypixel&bSkyblock&dCore&7] &f");
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(this.getPrefix() + CC.translate(message));
    }

    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public static SkyBlock getPlugin() {
        return plugin;
    }

    public Config getDatabaseInfo() {
        return this.databaseInfo;
    }

    public QuestLineHandler getQuestLineHandler() {
        return this.questLineHandler;
    }

    public void setOnlinePlayerAcrossServers(int onlinePlayerAcrossServers) {
        this.onlinePlayerAcrossServers = onlinePlayerAcrossServers;
    }

    public int getOnlinePlayerAcrossServers() {
        return this.onlinePlayerAcrossServers;
    }

    public BungeeChannel getBc() {
        return this.bc;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return this.serverName;
    }

    static {
        DEVELOPERS = new String[]{"Hamza", "EpicPortal", "Dumbo"};
    }
}

