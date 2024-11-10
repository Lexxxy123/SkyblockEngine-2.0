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
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package net.hypixel.skyblock;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.slikey.effectlib.EffectManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.api.placeholder.SkyblockPlaceholder;
import net.hypixel.skyblock.api.worldmanager.SkyBlockWorldManager;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.entity.EntitySpawner;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.features.launchpads.LaunchPadHandler;
import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.quest.QuestLineHandler;
import net.hypixel.skyblock.features.ranks.RankJoinListener;
import net.hypixel.skyblock.features.ranks.SetRankCommand;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.item.Recipe;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.armor.VoidlingsWardenHelmet;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.listener.WorldListener;
import net.hypixel.skyblock.module.impl.SkyBlockModuleManager;
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
import net.hypixel.skyblock.user.AuctionSettings;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.CC;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.SerialNBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class SkyBlock
extends JavaPlugin
implements PluginMessageListener {
    private static ProtocolManager protocolManager;
    private static SkyBlock plugin;
    private final PacketHelper packetInj = new PacketHelper();
    public static final String[] DEVELOPERS;
    public boolean altarCooldown = false;
    public static EffectManager effectManager;
    private static SkyBlock instance;
    private QuestLineHandler questLineHandler;
    private LaunchPadHandler launchPadHandler;
    private int onlinePlayerAcrossServers;
    public Repeater repeater;
    private String serverName = "M3A1E";
    public Config config;
    public List<String> bannedUUID = Collections.singletonList("");

    public void onLoad() {
        SLog.info("Loading Bukkit-serializable classes...");
        this.loadSerializableClasses();
    }

    public void onEnable() {
        plugin = this;
        instance = this;
        this.sendMessage("&aEnabling Skyblock Core. Made by " + this.getDevelopersName());
        long start = System.currentTimeMillis();
        this.getServer().getPluginManager().registerEvents((Listener)new RankJoinListener(), (Plugin)this);
        this.sendMessage("&aLoading SkyBlock worlds...");
        SkyBlockWorldManager.loadWorlds();
        this.sendMessage("&aLoading SkyBlock modules...");
        SkyBlockModuleManager.initModules();
        this.sendMessage("&aLoading config...");
        this.config = new Config("config.yml");
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
        VoidlingsWardenHelmet.startCounting();
        this.sendMessage("&aInjecting Packet/Ping Listener into the core...");
        this.registerPacketListener();
        this.registerPingListener();
        this.sendMessage("&aLoading Launchpads from config...");
        this.registerLaunchPads();
        this.sendMessage("&aStarting entity spawners...");
        EntitySpawner.startSpawnerTask();
        this.sendMessage("&aEstablishing player regions...");
        Region.cacheRegions();
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
        this.sendMessage("&aContinued by Funpixel (C) 2024");
        this.sendMessage("&aAny illegal usage will be suppressed! DO NOT LEAK IT!");
        this.sendMessage("&a===================================");
        this.getCommand("setrank").setExecutor((CommandExecutor)new SetRankCommand());
        long end = System.currentTimeMillis();
        this.sendMessage("&aSuccessfully enabled Hub Core in " + CC.getTimeDifferenceAndColor(start, end) + "&a.");
    }

    private void initializeQuests() {
        this.sendMessage("&aInitializing quests...");
        long start = System.currentTimeMillis();
        this.questLineHandler = new QuestLineHandler();
        this.sendMessage("&aSuccessfully registered " + ChatColor.GREEN + this.questLineHandler.getQuests().size() + ChatColor.WHITE + " quests [" + SUtil.getTimeDifferenceAndColor(start, System.currentTimeMillis()) + ChatColor.WHITE + "]");
    }

    public void onDisable() {
        this.sendMessage("&aSaving Player data...");
        SkyBlockModuleManager.stopManagers();
        for (User user : User.getCachedUsers()) {
            if (user == null || user.getUuid() == null) continue;
            user.save().thenRun(user::kick);
        }
        this.sendMessage("&aKilling all non-human entities...");
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) continue;
                entity.remove();
            }
        }
        if (this.repeater != null) {
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
            SkySimServerPingEvent e = new SkySimServerPingEvent(event);
            Bukkit.getPluginManager().callEvent((Event)e);
        });
    }

    public void registerLaunchPads() {
        this.launchPadHandler = new LaunchPadHandler();
    }

    public static Player findPlayerByIPAddress(String ip) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getAddress().toString().contains(ip)) continue;
            return p;
        }
        return null;
    }

    public String getDevelopersName() {
        StringBuilder builder = new StringBuilder();
        for (String name : DEVELOPERS) {
            builder.append(name).append(" , ");
        }
        return builder.substring(0, builder.length() - 2);
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PluginMessageReceived e = new PluginMessageReceived(new WrappedPluginMessage(channel, player, message));
        Bukkit.getPluginManager().callEvent((Event)e);
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)"&7[&aSkyblock&dCore&7] &f");
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

    public static SkyBlock getInstance() {
        return instance;
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

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return this.serverName;
    }

    static {
        DEVELOPERS = new String[]{"Lexxxy"};
    }
}

