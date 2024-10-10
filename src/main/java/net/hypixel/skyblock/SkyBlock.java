package net.hypixel.skyblock;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.slikey.effectlib.EffectManager;
import lombok.Getter;
import lombok.Setter;
import net.hypixel.skyblock.api.placeholder.SkyblockPlaceholder;
import net.hypixel.skyblock.api.worldmanager.SkyBlockWorldManager;
import net.hypixel.skyblock.database.RecipeDatabase;
import net.hypixel.skyblock.entity.EntitySpawner;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.features.merchant.MerchantItemHandler;
import net.hypixel.skyblock.features.quest.QuestLineHandler;
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
import net.hypixel.skyblock.nms.packetevents.*;
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
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SkyBlock extends JavaPlugin implements PluginMessageListener {
    @Getter
    private static ProtocolManager protocolManager;
    @Getter
    private static SkyBlock plugin;
    private final PacketHelper packetInj;

    public static final String[] DEVELOPERS = {"Hamza", "EpicPortal", "Dumbo"};


    public boolean altarCooldown;

    public static EffectManager effectManager;
    @Getter
    private static SkyBlock instance;

    @Getter
    private QuestLineHandler questLineHandler;
    @Setter
    @Getter
    private int onlinePlayerAcrossServers;

    public Repeater repeater;
    @Setter
    @Getter
    private String serverName;

    public List<String> bannedUUID;

    public SkyBlock() {
        this.packetInj = new PacketHelper();
        this.altarCooldown = false;
        this.serverName = "dev";
        this.bannedUUID = Collections.singletonList("");
    }

    public void onLoad() {
        SLog.info("Loading Bukkit-serializable classes...");
        this.loadSerializableClasses();
    }

    public void onEnable() {
        plugin = this;
        instance = this;
        sendMessage("&aEnabling Skyblock Core. Made by " + getDevelopersName());
        long start = System.currentTimeMillis();

        sendMessage("&aLoading SkyBlock worlds...");
        SkyBlockWorldManager.loadWorlds();

        sendMessage("&aLoading SkyBlock modules...");

        SkyBlockModuleManager.initModules();


        sendMessage("&aBegin Protocol injection... (SkyBlockProtocol v0.6.2)");
        APIManager.registerAPI(this.packetInj, this);
        if (!this.packetInj.injected) {
            this.getLogger().warning("[FATAL ERROR] Protocol Injection failed. Disabling the plugin for safety...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        sendMessage("&aInjecting...");
        PingAPI.register();
        new Metrics(this);
        APIManager.initAPI(PacketHelper.class);
        sendMessage("&aStarting server loop...");
        this.repeater = new Repeater();
        VoidlingsWardenHelmet.startCounting();
        sendMessage("&aInjecting Packet/Ping Listener into the core...");
        this.registerPacketListener();
        this.registerPingListener();
        sendMessage("&aStarting entity spawners...");
        EntitySpawner.startSpawnerTask();
        sendMessage("&aEstablishing player regions...");
        Region.cacheRegions();
        sendMessage("&aLoading auction items from disk...");
        effectManager = new EffectManager(this);
        AuctionItem.loadAuctionsFromDisk();
        sendMessage("&aLoading Quest!");
        initializeQuests();
        sendMessage("&aLoading merchants prices...");
        MerchantItemHandler.init();
        sendMessage("&aSynchronizing world time with calendar time and removing world entities...");
        SkyBlockCalendar.synchronize();
        sendMessage("&aLoading items...");
        SMaterial.loadItems();
        sendMessage("&aConverting CraftRecipes into custom recipes...");
        Recipe.loadRecipes();
        sendMessage("&aLoading recipes from database...");
        RecipeDatabase.loadRecipes();
        sendMessage("&aHooking SkyBlockEngine to PlaceholderAPI and registering...");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new SkyblockPlaceholder().register();
            sendMessage("&aHooked to PAPI successfully!");
        } else {
            sendMessage("&aERROR! PlaceholderAPI plugin does not exist, disabing placeholder request!");
        }
        protocolManager = ProtocolLibrary.getProtocolManager();
        WorldListener.init();

        sendMessage("&aSuccessfully enabled " + this.getDescription().getFullName());
        sendMessage("&a===================================");
        sendMessage("&aSkyBlock ENGINE - MADE BY " + getDevelopersName());
        sendMessage("&aPLUGIN ENABLED! HOOKED INTO SkyBlock!");
        sendMessage("&a ");
        sendMessage("&aThis plugin provide most of SkyBlock functions!");
        sendMessage("&aOriginally was made by super");
        sendMessage("&aContinued by GodSpunky (C) 2024");
        sendMessage("&aAny illegal usage will be suppressed! DO NOT LEAK IT!");
        sendMessage("&a===================================");
        this.getCommand("setrank").setExecutor(new SetRankCommand());

        long end = System.currentTimeMillis();
        sendMessage("&aSuccessfully enabled Hub Core in " + CC.getTimeDifferenceAndColor(start, end) + "&a.");

    }

    private void initializeQuests() {
        sendMessage("&aInitializing quests...");
        long start = System.currentTimeMillis();

        this.questLineHandler = new QuestLineHandler();

        sendMessage("&aSuccessfully registered " + ChatColor.GREEN + this.questLineHandler.getQuests().size() + ChatColor.WHITE + " quests [" + SUtil.getTimeDifferenceAndColor(start, System.currentTimeMillis()) + ChatColor.WHITE + "]");
    }


    public void onDisable() {
        sendMessage("&aSaving Player data...");
        SkyBlockModuleManager.stopManagers();

        for (User user : User.getCachedUsers()) {
            if (user == null) continue;
            if (user.getUuid() == null) continue;
            user.save().thenRun(user::kick);
        }


        sendMessage("&aKilling all non-human entities...");
        for (final World world : Bukkit.getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity instanceof HumanEntity) {
                    continue;
                }
                entity.remove();
            }
        }
        if (this.repeater != null) {
            sendMessage("&aStopping server loop...");
            this.repeater.stop();
            sendMessage("&aUnloading ores from Dwarven Mines...");
            WorldListener.unloadBlocks();
            sendMessage("&aEjecting protocol channel...");
            APIManager.disableAPI(PacketHelper.class);
            sendMessage("&aCleaning HashSets...");
            for (final Map.Entry<Entity, Block> entry : VoidgloomSeraph.CACHED_BLOCK.entrySet()) {
                final Entity stand = entry.getKey();
                if (stand != null && VoidgloomSeraph.CACHED_BLOCK.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_ID.containsKey(stand) && VoidgloomSeraph.CACHED_BLOCK_DATA.containsKey(stand)) {
                    VoidgloomSeraph.CACHED_BLOCK.get(stand).getLocation().getBlock().setTypeIdAndData(VoidgloomSeraph.CACHED_BLOCK_ID.get(stand), VoidgloomSeraph.CACHED_BLOCK_DATA.get(stand), true);
                }
            }
            //this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
            //this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
            sendMessage("&aStopping entity spawners...");
            EntitySpawner.stopSpawnerTask();
            sendMessage("&aEnding Dragons fight... (If one is currently active)");
            StaticDragonManager.endFight();
            sendMessage("&aSaving calendar time...");
            SkyBlockCalendar.saveElapsed();
            sendMessage("&aSaving auction data...");
            for (final AuctionItem item : AuctionItem.getAuctions()) {
                item.save();
            }
            plugin = null;
        }
        sendMessage("&aDisabled " + this.getDescription().getFullName());
        sendMessage("&a===================================");
        sendMessage("&aSkyBlock ENGINE - MADE BY " + getDevelopersName());
        sendMessage("&aPLUGIN DISABLED!");
        sendMessage("&a===================================");
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
        PingAPI.registerListener(event -> {
            final SkySimServerPingEvent e = new SkySimServerPingEvent(event);
            Bukkit.getPluginManager().callEvent(e);
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


    public String getDevelopersName() {
        StringBuilder builder = new StringBuilder();
        for (String name : DEVELOPERS) {
            builder.append(name).append(" , ");
        }
        return builder.substring(0, builder.length() - 2);
    }


    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        final PluginMessageReceived e = new PluginMessageReceived(new WrappedPluginMessage(channel, player, message));
        Bukkit.getPluginManager().callEvent(e);
    }


    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', "&7[&aSkyblock&dCore&7] &f");
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(getPrefix() + CC.translate(message));
    }
}
