/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutTitle$EnumTitleAction
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.user;

import com.google.common.util.concurrent.AtomicDouble;
import com.mongodb.client.MongoCollection;
import de.tr7zw.nbtapi.NBTItem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.serializer.BukkitSerializeClass;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.StaticWardenManager;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanFunction;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.collection.ItemCollectionReward;
import net.hypixel.skyblock.features.collection.ItemCollectionRewards;
import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.features.requirement.enums.SkillType;
import net.hypixel.skyblock.features.skill.ArcherSkill;
import net.hypixel.skyblock.features.skill.BerserkSkill;
import net.hypixel.skyblock.features.skill.CatacombsSkill;
import net.hypixel.skyblock.features.skill.CombatSkill;
import net.hypixel.skyblock.features.skill.EnchantingSkill;
import net.hypixel.skyblock.features.skill.FarmingSkill;
import net.hypixel.skyblock.features.skill.ForagingSkill;
import net.hypixel.skyblock.features.skill.HealerSkill;
import net.hypixel.skyblock.features.skill.MageSkill;
import net.hypixel.skyblock.features.skill.MiningSkill;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.features.skill.TankSkill;
import net.hypixel.skyblock.features.slayer.SlayerBossType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.gui.PetsGUI;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.user.AuctionSettings;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.UserDatabase;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.hypixel.skyblock.util.SputnikPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class User {
    private static final File USER_FOLDER;
    public static final int ISLAND_SIZE = 125;
    public static final Map<UUID, User> USER_CACHE;
    private static final SkyBlock plugin;
    public final Map<String, Object> dataCache;
    public long sadancollections;
    public long totalfloor6run;
    private UUID uuid;
    private Document dataDocument;
    public final Map<ItemCollection, Integer> collections;
    private long coins;
    private long bits;
    private long bankCoins;
    private List<ItemStack> stashedItems = new ArrayList<ItemStack>();
    private int cooldownAltar = 0;
    private boolean headShot = false;
    private static final boolean multiServer = false;
    private boolean playingSong = false;
    private boolean inDanger = false;
    private Region lastRegion;
    private final Map<SMaterial, Integer> quiver;
    private final List<ActivePotionEffect> effects;
    private List<String> talkedNPCs;
    private List<String> talkedVillagers;
    double farmingXP;
    private boolean boneToZeroDamage;
    private boolean cooldownAPI;
    double miningXP;
    double combatXP;
    private double enchantXP;
    private double archerXP;
    private double cataXP;
    private double berserkXP;
    private double healerXP;
    private double tankXP;
    private double mageXP;
    double foragingXP;
    final int[] highestSlayers;
    final int[] slayerXP;
    private final int[] crystalLVL;
    private boolean saveable;
    private int bonusFerocity;
    private boolean fatalActive;
    private boolean permanentCoins;
    private SlayerQuest slayerQuest;
    List<Pet.PetItem> pets;
    private List<String> unlockedRecipes;
    AuctionSettings auctionSettings;
    private boolean auctionCreationBIN;
    private AuctionEscrow auctionEscrow;
    private boolean voidlingWardenActive;
    private boolean waitingForSign;
    private String signContent;
    private boolean isCompletedSign;
    public List<String> completedQuests;
    public List<String> completedObjectives;
    private Double islandX;
    private Double islandZ;
    public PlayerRank rank;
    public Player player;
    private Config config;
    public static Config sbc;
    public List<String> foundzone;
    public boolean hasIsland;

    private User(UUID uuid) {
        this.dataCache = new HashMap<String, Object>();
        this.player = Bukkit.getPlayer((UUID)uuid);
        this.boneToZeroDamage = false;
        this.rank = PlayerRank.DEFAULT;
        this.cooldownAPI = false;
        this.talkedVillagers = new CopyOnWriteArrayList<String>();
        this.saveable = true;
        this.waitingForSign = false;
        this.signContent = null;
        this.isCompletedSign = false;
        this.uuid = uuid;
        this.hasIsland = false;
        this.foundzone = new ArrayList<String>();
        this.collections = ItemCollection.getDefaultCollections();
        this.totalfloor6run = 0L;
        this.coins = 0L;
        this.bits = 0L;
        this.bankCoins = 0L;
        this.sadancollections = 0L;
        this.lastRegion = null;
        this.talkedNPCs = new CopyOnWriteArrayList<String>();
        this.quiver = new HashMap<SMaterial, Integer>();
        this.effects = new ArrayList<ActivePotionEffect>();
        this.unlockedRecipes = new ArrayList<String>();
        this.completedQuests = new ArrayList<String>();
        this.completedObjectives = new ArrayList<String>();
        this.farmingXP = 0.0;
        this.miningXP = 0.0;
        this.combatXP = 0.0;
        this.foragingXP = 0.0;
        this.enchantXP = 0.0;
        this.highestSlayers = new int[4];
        this.slayerXP = new int[4];
        this.crystalLVL = new int[8];
        this.permanentCoins = false;
        this.pets = new ArrayList<Pet.PetItem>();
        this.auctionSettings = new AuctionSettings();
        this.auctionCreationBIN = false;
        this.auctionEscrow = new AuctionEscrow();
        if (sbc.getBoolean("Config")) {
            String path = uuid.toString() + ".yml";
            if (!USER_FOLDER.exists()) {
                USER_FOLDER.mkdirs();
            }
            File configFile = new File(USER_FOLDER, path);
            boolean save = false;
            try {
                if (!configFile.exists()) {
                    save = true;
                    configFile.createNewFile();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.config = new Config(USER_FOLDER, path);
            if (save) {
                this.configsave();
            }
            this.configload();
        }
        USER_CACHE.put(uuid, this);
    }

    public Config getConfig() {
        return this.config;
    }

    public void unload() {
        USER_CACHE.remove(this.uuid);
    }

    public void configload() {
        this.uuid = UUID.fromString(this.config.getString("uuid"));
        if (this.config.contains("collections")) {
            for (String identifier : this.config.getConfigurationSection("collections").getKeys(false)) {
                this.collections.put(ItemCollection.getByIdentifier(identifier), this.config.getInt("collections." + identifier));
            }
        }
        this.islandX = this.config.contains("island.x") ? Double.valueOf(this.config.getDouble("island.x")) : null;
        this.islandZ = this.config.contains("island.z") ? Double.valueOf(this.config.getDouble("island.z")) : null;
        this.rank = PlayerRank.valueOf(this.config.getString("rank"));
        this.hasIsland = this.config.getBoolean("hasIsland");
        this.coins = this.config.getLong("coins");
        this.bits = this.config.getLong("bits");
        this.bankCoins = this.config.getLong("bankCoins");
        Region region = this.lastRegion = this.config.getString("lastRegion") != null ? Region.get(this.config.getString("lastRegion")) : null;
        if (this.config.contains("quiver")) {
            for (String m2 : this.config.getConfigurationSection("quiver").getKeys(false)) {
                this.quiver.put(SMaterial.getMaterial(m2), this.config.getInt("quiver." + m2));
            }
        }
        if (this.config.contains("effects")) {
            for (String key : this.config.getConfigurationSection("effects").getKeys(false)) {
                this.effects.add(new ActivePotionEffect(new PotionEffect(PotionEffectType.getByNamespace(key), this.config.getInt("effects." + key + ".level"), this.config.getLong("effects." + key + ".duration")), this.config.getLong("effects." + key + ".remaining")));
            }
        }
        this.totalfloor6run = this.config.getLong("dungeons.floor6.run");
        this.sadancollections = this.config.getLong("dungeons.boss.sadan");
        this.farmingXP = this.config.getDouble("xp.farming");
        this.miningXP = this.config.getDouble("xp.mining");
        this.combatXP = this.config.getDouble("xp.combat");
        this.foragingXP = this.config.getDouble("xp.foraging");
        this.enchantXP = this.config.getDouble("xp.enchant");
        this.cataXP = this.config.getDouble("xp.dungeons.cata");
        this.archerXP = this.config.getDouble("xp.dungeons.arch");
        this.mageXP = this.config.getDouble("xp.dungeons.mage");
        this.tankXP = this.config.getDouble("xp.dungeons.tank");
        this.berserkXP = this.config.getDouble("xp.dungeons.bers");
        this.healerXP = this.config.getDouble("xp.dungeons.heal");
        this.highestSlayers[0] = this.config.getInt("slayer.revenantHorror.highest");
        this.highestSlayers[1] = this.config.getInt("slayer.tarantulaBroodfather.highest");
        this.highestSlayers[2] = this.config.getInt("slayer.svenPackmaster.highest");
        this.highestSlayers[3] = this.config.getInt("slayer.voidgloomSeraph.highest");
        this.slayerXP[0] = this.config.getInt("xp.slayer.revenantHorror");
        this.slayerXP[1] = this.config.getInt("xp.slayer.tarantulaBroodfather");
        this.slayerXP[2] = this.config.getInt("xp.slayer.svenPackmaster");
        this.slayerXP[3] = this.config.getInt("xp.slayer.voidgloomSeraph");
        this.permanentCoins = this.config.getBoolean("permanentCoins");
        this.slayerQuest = (SlayerQuest)this.config.get("slayer.quest");
        if (this.config.contains("pets")) {
            this.pets = this.config.getList("pets");
        }
        if (this.config.contains("unlockedRecipes")) {
            this.unlockedRecipes = this.config.getList("unlockedRecipes");
        }
        if (this.config.contains("talked_npcs")) {
            this.talkedNPCs = this.config.getList("talked_npcs");
        }
        if (this.config.contains("talked_villagers")) {
            this.talkedVillagers = this.config.getList("talked_villagers");
        }
        if (this.config.contains("foundzones")) {
            this.foundzone = this.config.getList("foundzones");
        }
        if (this.config.contains("completedQuest")) {
            this.completedQuests = this.config.getList("completedQuest");
        }
        if (this.config.contains("completedObjectives")) {
            this.completedObjectives = this.config.getList("completedObjectives");
        }
        this.auctionSettings = (AuctionSettings)this.config.get("auction.settings");
        if (this.auctionSettings == null) {
            this.auctionSettings = new AuctionSettings();
        }
        this.auctionCreationBIN = this.config.getBoolean("auction.creationBIN");
        this.auctionEscrow = (AuctionEscrow)this.config.get("auction.escrow");
        if (this.auctionEscrow == null) {
            this.auctionEscrow = new AuctionEscrow();
        }
    }

    public void configsave() {
        this.config.set("uuid", this.uuid.toString());
        this.config.set("rank", this.rank.toString());
        this.config.set("collections", null);
        this.config.set("island.x", this.islandX);
        this.config.set("island.z", this.islandZ);
        for (Map.Entry<ItemCollection, Integer> entry : this.collections.entrySet()) {
            this.config.set("collections." + entry.getKey().getIdentifier(), entry.getValue());
        }
        this.config.set("hasIsland", this.hasIsland);
        this.config.set("coins", this.coins);
        this.config.set("bits", this.bits);
        this.config.set("bankCoins", this.bankCoins);
        if (this.lastRegion != null) {
            this.config.set("lastRegion", this.lastRegion.getName());
        }
        this.config.set("quiver", null);
        for (Map.Entry<Object, Integer> entry : this.quiver.entrySet()) {
            this.config.set("quiver." + ((SMaterial)((Object)entry.getKey())).name().toLowerCase(), entry.getValue());
        }
        this.config.set("effects", null);
        for (ActivePotionEffect activePotionEffect : this.effects) {
            PotionEffectType type = activePotionEffect.getEffect().getType();
            this.config.set("effects." + type.getNamespace() + ".level", activePotionEffect.getEffect().getLevel());
            this.config.set("effects." + type.getNamespace() + ".duration", activePotionEffect.getEffect().getDuration());
            this.config.set("effects." + type.getNamespace() + ".remaining", activePotionEffect.getRemaining());
        }
        this.config.set("dungeons.floor6.run", this.totalfloor6run);
        this.config.set("dungeons.boss.sadan", this.sadancollections);
        this.config.set("xp.farming", this.farmingXP);
        this.config.set("xp.mining", this.miningXP);
        this.config.set("xp.combat", this.combatXP);
        this.config.set("xp.foraging", this.foragingXP);
        this.config.set("xp.enchant", this.enchantXP);
        this.config.set("xp.dungeons.cata", this.cataXP);
        this.config.set("xp.dungeons.arch", this.archerXP);
        this.config.set("xp.dungeons.bers", this.berserkXP);
        this.config.set("xp.dungeons.heal", this.healerXP);
        this.config.set("xp.dungeons.mage", this.mageXP);
        this.config.set("xp.dungeons.tank", this.tankXP);
        this.config.set("slayer.revenantHorror.highest", this.highestSlayers[0]);
        this.config.set("slayer.tarantulaBroodfather.highest", this.highestSlayers[1]);
        this.config.set("slayer.svenPackmaster.highest", this.highestSlayers[2]);
        this.config.set("slayer.voidgloomSeraph.highest", this.highestSlayers[3]);
        this.config.set("xp.slayer.revenantHorror", this.slayerXP[0]);
        this.config.set("xp.slayer.tarantulaBroodfather", this.slayerXP[1]);
        this.config.set("xp.slayer.svenPackmaster", this.slayerXP[2]);
        this.config.set("xp.slayer.voidgloomSeraph", this.slayerXP[3]);
        this.config.set("permanentCoins", this.permanentCoins);
        this.config.set("slayer.quest", this.slayerQuest);
        this.config.set("pets", this.pets);
        this.config.set("unlockedRecipes", this.unlockedRecipes);
        this.config.set("talked_npcs", this.talkedNPCs);
        this.config.set("talked_villagers", this.talkedVillagers);
        this.config.set("completedObjectives", this.completedObjectives);
        this.config.set("completedQuest", this.completedQuests);
        this.config.set("unlockedRecipes", this.unlockedRecipes);
        this.config.set("foundzones", this.foundzone);
        this.config.set("auction.settings", this.auctionSettings);
        this.config.set("auction.creationBIN", this.auctionCreationBIN);
        this.config.set("auction.escrow", this.auctionEscrow);
        if (Bukkit.getPlayer((UUID)this.uuid) != null && Bukkit.getPlayer((UUID)this.uuid).isOnline()) {
            this.config.set("configures.showPets", PetsGUI.getShowPet(Bukkit.getPlayer((UUID)this.uuid)));
            this.config.set("configures.autoSlayer", PlayerUtils.isAutoSlayer(Bukkit.getPlayer((UUID)this.uuid)));
        }
        this.config.save();
    }

    public synchronized void addTalkedNPC(String name) {
        if (!this.talkedNPCs.contains(name)) {
            this.talkedNPCs.add(name);
        }
    }

    public synchronized void addTalkedVillager(String name) {
        if (!this.talkedVillagers.contains(name)) {
            this.talkedVillagers.add(name);
        }
    }

    public List<String> getdiscoveredzones() {
        return this.foundzone;
    }

    public void addnewzone(String q2) {
        this.foundzone.add(q2);
    }

    public void addCompletedQuest(String questName) {
        this.completedQuests.add(questName);
    }

    public void addCompletedObjectives(String questName) {
        this.completedObjectives.add(questName);
    }

    public QuestLine getQuestLine() {
        return SkyBlock.getPlugin().getQuestLineHandler().getFromPlayer(this);
    }

    public void send(String message, Object ... args) {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        player.sendMessage(Sputnik.trans(String.format(message, args)));
    }

    public void onNewZone(RegionType zone, String ... features) {
        this.send("");
        this.send("\u00a76\u00a7l NEW AREA DISCOVERED!");
        this.send("\u00a77  \u23e3 " + zone.getColor() + zone.getName());
        this.send("");
        if (features.length > 0) {
            for (String feature : features) {
                this.send("\u00a77   \u2b1b \u00a7f%s", feature);
            }
        } else {
            this.send("\u00a77   \u2b1b \u00a7cNot much yet!");
        }
        this.send("");
        Bukkit.getPlayer((UUID)this.uuid).playSound(Bukkit.getPlayer((UUID)this.uuid).getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        SUtil.sendTypedTitle(player, zone.getColor() + zone.getName(), PacketPlayOutTitle.EnumTitleAction.TITLE);
        SUtil.sendTypedTitle(player, "\u00a76\u00a7lNEW AREA DISCOVERED!", PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
    }

    public Region getRegion() {
        if (this.isOnIsland() || this.isOnUserIsland()) {
            return Region.getIslandRegion();
        }
        String worldName = this.player.getWorld().getName();
        if (worldName.startsWith("f6")) {
            return Region.get("Catacombs (F6)");
        }
        return Region.getRegionOfEntity((Entity)Bukkit.getPlayer((UUID)this.uuid));
    }

    public void asyncSavingData() {
        if (SkyBlock.getPlugin().config.getBoolean("Config")) {
            this.configsave();
        } else {
            this.save();
        }
    }

    public void loadStatic() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        User user = User.getUser(this.uuid);
        PlayerUtils.AUTO_SLAYER.put(player.getUniqueId(), true);
        PetsGUI.setShowPets(player, true);
    }

    public void kick() {
        if (this.toBukkitPlayer().isOnline()) {
            this.toBukkitPlayer().kickPlayer("server restart...");
        }
    }

    public CompletableFuture<Void> save() {
        final CompletableFuture<Void> future = new CompletableFuture<Void>();
        SUtil.runAsync(new Runnable(){

            @Override
            public void run() {
                User.this.setDataProperty("uuid", User.this.uuid.toString());
                User.this.setDataProperty("coins", User.this.coins);
                User.this.setDataProperty("bits", User.this.bits);
                User.this.setDataProperty("bankCoins", User.this.bankCoins);
                User.this.setDataProperty("rank", User.this.rank.toString());
                User.this.setDataProperty("islandX", User.this.islandX);
                User.this.setDataProperty("islandZ", User.this.islandZ);
                User.this.setDataProperty("hasIsland", User.this.hasIsland);
                HashMap<String, Integer> collectionsData = new HashMap<String, Integer>();
                for (ItemCollection collection : ItemCollection.getCollections()) {
                    collectionsData.put(collection.getIdentifier(), User.this.getCollection(collection));
                }
                User.this.setDataProperty("collections", collectionsData);
                if (User.this.lastRegion != null) {
                    User.this.setDataProperty("lastRegion", User.this.getLastRegion().getName());
                }
                HashMap quiverData = new HashMap();
                User.this.getQuiver().forEach((key, value) -> quiverData.put(key.name(), value));
                User.this.setDataProperty("quiver", quiverData);
                ArrayList<Document> effects = new ArrayList<Document>();
                for (ActivePotionEffect effect : User.this.getEffects()) {
                    Document effectDocument = new Document().append("key", effect.getEffect().getType().getNamespace()).append("level", effect.getEffect().getLevel()).append("duration", effect.getEffect().getDuration()).append("remaining", effect.getRemaining());
                    effects.add(effectDocument);
                }
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("armor", BukkitSerializeClass.itemStackArrayToBase64(User.this.player.getInventory().getArmorContents()));
                data.put("enderchest", User.this.getPureListFrom(User.this.player.getEnderChest()));
                data.put("minecraft_xp", Sputnik.getTotalExperience(User.this.player));
                data.put("lastslot", User.this.player.getInventory().getHeldItemSlot());
                data.put("inventory", User.this.getPureListFrom((Inventory)User.this.player.getInventory()));
                ItemStack[] is = new ItemStack[User.this.stashedItems.size()];
                is = User.this.stashedItems.toArray(is);
                data.put("stash", BukkitSerializeClass.itemStackArrayToBase64(is));
                User.this.setDataProperty("data", data);
                HashMap<String, List<String>> quest = new HashMap<String, List<String>>();
                quest.put("talkedvillager", User.this.talkedVillagers);
                quest.put("completedQuests", User.this.completedQuests);
                quest.put("completedObjectives", User.this.completedObjectives);
                User.this.setDataProperty("quest", quest);
                User.this.setDataProperty("totalfloor6run", User.this.totalfloor6run);
                User.this.setDataProperty("sadanCollections", User.this.sadancollections);
                User.this.setDataProperty("effects", effects);
                User.this.setDataProperty("skillFarmingXp", User.this.farmingXP);
                User.this.setDataProperty("skillMiningXp", User.this.miningXP);
                User.this.setDataProperty("skillCombatXp", User.this.combatXP);
                User.this.setDataProperty("skillForagingXp", User.this.foragingXP);
                User.this.setDataProperty("skillEnchantXp", User.this.enchantXP);
                User.this.setDataProperty("slayerRevenantHorrorHighest", User.this.highestSlayers[0]);
                User.this.setDataProperty("slayerTarantulaBroodfatherHighest", User.this.highestSlayers[1]);
                User.this.setDataProperty("slayerSvenPackmasterHighest", User.this.highestSlayers[2]);
                User.this.setDataProperty("slayerVoidgloomSeraphHighest", User.this.highestSlayers[3]);
                User.this.setDataProperty("permanentCoins", User.this.isPermanentCoins());
                User.this.setDataProperty("xpSlayerRevenantHorror", User.this.slayerXP[0]);
                User.this.setDataProperty("xpSlayerTarantulaBroodfather", User.this.slayerXP[1]);
                User.this.setDataProperty("xpSlayerSvenPackmaster", User.this.slayerXP[2]);
                User.this.setDataProperty("xpSlayerVoidgloomSeraph", User.this.slayerXP[3]);
                if (User.this.slayerQuest != null) {
                    User.this.setDataProperty("slayerQuest", User.this.getSlayerQuest().serialize());
                }
                if (!User.this.pets.isEmpty()) {
                    List petsSerialized = User.this.pets.stream().map(Pet.PetItem::serialize).collect(Collectors.toList());
                    User.this.setDataProperty("pets", petsSerialized);
                } else {
                    User.this.setDataProperty("pets", new ArrayList());
                }
                if (User.this.auctionSettings != null) {
                    User.this.setDataProperty("auctionSettings", User.this.auctionSettings.serialize());
                }
                User.this.setDataProperty("auctionCreationBIN", User.this.isAuctionCreationBIN());
                if (User.this.auctionEscrow != null) {
                    User.this.setDataProperty("auctionEscrow", User.this.auctionEscrow.serialize());
                }
                User.this.setDataProperty("unlockedRecipes", User.this.unlockedRecipes);
                User.this.setDataProperty("talkedNPCs", User.this.talkedNPCs);
                User.this.setDataProperty("foundZone", User.this.foundzone);
                if (Bukkit.getPlayer((UUID)User.this.uuid) != null && Bukkit.getPlayer((UUID)User.this.uuid).isOnline()) {
                    User.this.setDataProperty("showPets", PetsGUI.getShowPet(Bukkit.getPlayer((UUID)User.this.uuid)));
                    User.this.setDataProperty("autoSlayer", PlayerUtils.isAutoSlayer(Bukkit.getPlayer((UUID)User.this.uuid)));
                    if (PlayerUtils.COOKIE_DURATION_CACHE.containsKey(User.this.uuid)) {
                        User.this.setDataProperty("cookieDuration", PlayerUtils.getCookieDurationTicks(Bukkit.getPlayer((UUID)User.this.uuid)));
                    }
                }
                Document query = new Document("_id", User.this.uuid.toString());
                UserDatabase.collectionFuture.thenApply(userCollection -> {
                    Document found = (Document)userCollection.find(query).first();
                    if (found != null) {
                        Document updated = new Document(found);
                        User.this.dataCache.forEach(updated::append);
                        userCollection.replaceOne(found, updated);
                    } else {
                        Document newDocument = new Document("_id", User.this.uuid.toString());
                        User.this.dataCache.forEach(newDocument::append);
                        userCollection.insertOne(newDocument);
                    }
                    future.complete(null);
                    return future;
                });
            }
        });
        return future;
    }

    public void loadPlayerData() throws IllegalArgumentException, IOException {
        Document query;
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        MongoCollection<Document> collection = DatabaseManager.getCollection("users").join();
        Document document = (Document)collection.find(query = new Document("_id", this.uuid.toString())).first();
        Document databaseDocument = document.containsKey("data") ? (Document)document.get("data") : document;
        if (databaseDocument.containsKey("inventory")) {
            if (player != null) {
                player.getInventory().setContents(BukkitSerializeClass.itemStackArrayFromBase64(databaseDocument.getString("inventory")));
            }
        } else {
            player.getInventory().setContents(new ItemStack[player.getInventory().getSize()]);
        }
        if (databaseDocument.containsKey("enderchest")) {
            if (player != null) {
                player.getEnderChest().setContents(BukkitSerializeClass.itemStackArrayFromBase64(databaseDocument.getString("enderchest")));
            }
        } else {
            player.getInventory().setContents(new ItemStack[player.getEnderChest().getSize()]);
        }
        if (databaseDocument.containsKey("armor")) {
            if (player != null) {
                player.getInventory().setArmorContents(BukkitSerializeClass.itemStackArrayFromBase64(databaseDocument.getString("armor")));
            }
        } else {
            player.getInventory().setContents(new ItemStack[player.getInventory().getArmorContents().length]);
        }
        if (databaseDocument.containsKey("minecraft_xp") && player != null) {
            Sputnik.setTotalExperience(player, databaseDocument.getInteger("minecraft_xp"));
        }
        if (document.containsKey("stash")) {
            if (player != null) {
                ItemStack[] arr = new ItemStack[]{};
                try {
                    arr = BukkitSerializeClass.itemStackArrayFromBase64(databaseDocument.getString("stash"));
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
                this.stashedItems = Arrays.asList(arr);
            }
        } else {
            this.stashedItems = new ArrayList<ItemStack>();
        }
        if (document.containsKey("lastslot") && player != null) {
            player.getInventory().setHeldItemSlot(document.getInteger("lastslot").intValue());
        }
    }

    public String getPureListFrom(Inventory piv) {
        ItemStack[] ist = piv.getContents();
        List<ItemStack> arraylist = Arrays.asList(ist);
        for (int i2 = 0; i2 < ist.length; ++i2) {
            NBTItem nbti;
            ItemStack stack = ist[i2];
            if (stack == null || !(nbti = new NBTItem(stack)).hasKey("dontSaveToProfile").booleanValue()) continue;
            arraylist.remove(i2);
        }
        ItemStack[] arrl = (ItemStack[])arraylist.toArray();
        return BukkitSerializeClass.itemStackArrayToBase64(arrl);
    }

    public void load() {
        SUtil.runAsync(new Runnable(){

            @Override
            public void run() {
                User.this.loadDocument();
                User.this.uuid = UUID.fromString(User.this.getString("uuid", null));
                User.this.coins = User.this.getLong("coins", 0);
                User.this.bits = User.this.getLong("bits", 0);
                User.this.bankCoins = User.this.getLong("bankCoins", 0);
                Map coll = (Map)User.this.get("collections", new HashMap());
                coll.forEach((key, value) -> User.this.collections.put(ItemCollection.getByIdentifier(key), (Integer)value));
                User.this.setRank(PlayerRank.valueOf(User.this.getString("rank", "DEFAULT")));
                User.this.islandX = User.this.getDouble("islandX", 0);
                User.this.islandZ = User.this.getDouble("islandZ", 0);
                User.this.hasIsland = User.this.getBoolean("hasIsland", false);
                if (User.this.getString("lastRegion", "none").equals("none")) {
                    User.this.setLastRegion(null);
                } else {
                    User.this.setLastRegion(Region.get(User.this.getString("lastRegion", "none")));
                }
                User.this.totalfloor6run = User.this.getLong("totalfloor6run", 0);
                User.this.sadancollections = User.this.getLong("sadanCollections", 0);
                User.this.farmingXP = User.this.getDouble("skillFarmingXp", 0.0);
                User.this.miningXP = User.this.getDouble("skillMiningXp", 0.0);
                User.this.combatXP = User.this.getDouble("skillCombatXp", 0.0);
                User.this.foragingXP = User.this.getDouble("skillForagingXp", 0.0);
                User.this.enchantXP = User.this.getDouble("skillEnchantXp", 0.0);
                User.this.highestSlayers[0] = User.this.getInt("slayerRevenantHorrorHighest", 0);
                User.this.highestSlayers[1] = User.this.getInt("slayerTarantulaBroodfatherHighest", 0);
                User.this.highestSlayers[2] = User.this.getInt("slayerSvenPackmasterHighest", 0);
                User.this.highestSlayers[3] = User.this.getInt("slayerVoidgloomSeraphHighest", 0);
                User.this.slayerXP[0] = User.this.getInt("xpSlayerRevenantHorror", 0);
                User.this.slayerXP[1] = User.this.getInt("xpSlayerTarantulaBroodfather", 0);
                User.this.slayerXP[2] = User.this.getInt("xpSlayerSvenPackmaster", 0);
                User.this.slayerXP[3] = User.this.getInt("xpSlayerVoidgloomSeraph", 0);
                User.this.talkedNPCs = User.this.getStringList("talkedNPCs", new ArrayList<String>());
                User.this.unlockedRecipes = User.this.getStringList("unlockedRecipes", new ArrayList<String>());
                User.this.foundzone = User.this.getStringList("foundZone", new ArrayList<String>());
                User.this.setPermanentCoins(User.this.getBoolean("permanentCoins", false));
                Map quiv = (Map)User.this.get("quiver", new HashMap());
                quiv.forEach((key, value) -> User.this.quiver.put(SMaterial.getMaterial(key), value));
                try {
                    SlayerQuest quest = SlayerQuest.deserialize((Map)User.this.get("slayerQuest", new HashMap()));
                    User.this.setSlayerQuest(quest);
                } catch (Exception ignored) {
                    User.this.setSlayerQuest(null);
                }
                ArrayList listOfPetObjects = new ArrayList((List)User.this.get("pets", new ArrayList()));
                listOfPetObjects.forEach(item -> {
                    Pet.PetItem petitem = Pet.PetItem.deserialize((Map)item);
                    User.this.pets.add(petitem);
                });
                try {
                    User.this.auctionSettings = AuctionSettings.deserialize((Map)User.this.get("auctionSettings", new HashMap()));
                } catch (Exception ignored) {
                    User.this.auctionSettings = new AuctionSettings();
                }
                try {
                    User.this.setAuctionEscrow(AuctionEscrow.deserialize((Map)User.this.get("auctionEscrow", new HashMap())));
                } catch (Exception ignored) {
                    User.this.setAuctionEscrow(new AuctionEscrow());
                }
                Document questData = (Document)User.this.get("quests", new Document());
                List<String> completedQuests = questData.getList("completedQuests", String.class, new ArrayList());
                List<String> completedObjectives = questData.getList("completedObjectives", String.class, new ArrayList());
                List<String> talkedto = questData.getList("talkedto", String.class, new ArrayList());
                User.this.setCompletedQuests(completedQuests);
                User.this.setCompletedObjectives(completedObjectives);
                User.this.talkedVillagers = talkedto;
                User.this.setAuctionCreationBIN(User.this.getBoolean("auctionCreationBIN", false));
                List<Document> effectsDocuments = User.this.dataDocument.getList("effects", Document.class);
                for (Document effectData : effectsDocuments) {
                    PotionEffectType potionEffectType;
                    String key2 = effectData.getString("key");
                    Integer level = effectData.getInteger("level");
                    Long duration = effectData.getLong("duration");
                    Long remaining = effectData.getLong("remaining");
                    if (key2 == null || level == null || duration == null || remaining == null || (potionEffectType = PotionEffectType.getByNamespace(key2)) == null) continue;
                    User.this.getEffects().add(new ActivePotionEffect(new PotionEffect(potionEffectType, level, duration), remaining));
                }
                try {
                    PlayerUtils.setCookieDurationTicks(User.this.player, User.this.getLong("cookieDuration", 0));
                } catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
                try {
                    User.this.loadPlayerData();
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
        });
    }

    public void setDataProperty(String key, Object value) {
        this.dataCache.put(key, value);
    }

    public List<String> getStringList(String key, List<String> def) {
        Object value = this.get(key, def);
        if (value instanceof List) {
            return new ArrayList<String>((List)value);
        }
        ArrayList<String> list = new ArrayList<String>();
        list.add(value.toString());
        return list;
    }

    public Object get(String key, Object def) {
        if (this.dataDocument != null && this.dataDocument.get(key) != null) {
            return this.dataDocument.get(key);
        }
        return def;
    }

    public String getString(String key, Object def) {
        return this.get(key, def).toString();
    }

    public int getInt(String key, Object def) {
        return (Integer)this.get(key, def);
    }

    public boolean getBoolean(String key, Object def) {
        return Boolean.parseBoolean(this.get(key, def).toString());
    }

    public double getDouble(String key, Object def) {
        return Double.parseDouble(this.getString(key, def));
    }

    public long getLong(String key, Object def) {
        if (def.equals(0.0)) {
            def = 0L;
        }
        return Long.parseLong(this.getString(key, def));
    }

    public void loadDocument() {
        Document query = new Document("_id", this.uuid.toString());
        MongoCollection<Document> collection = DatabaseManager.getCollection("users").join();
        Document found = (Document)collection.find(query).first();
        if (found == null) {
            this.save();
            found = (Document)collection.find(query).first();
        }
        this.dataDocument = found;
    }

    public void sendMessage(Object message) {
        if (message instanceof String) {
            this.player.sendMessage(message.toString().replace('&', '\u00a7'));
        } else if (message instanceof TextComponent) {
            this.player.spigot().sendMessage(new BaseComponent[]{(BaseComponent)message});
        }
    }

    public void sendMessages(Object ... messages) {
        for (Object message : messages) {
            this.sendMessage(message);
        }
    }

    public boolean addBits(long value) {
        if (value > 0L) {
            this.bits += value;
            return true;
        }
        return false;
    }

    public boolean subBits(long value) {
        if (this.bits > value && value > 0L) {
            this.bits -= value;
            return true;
        }
        return false;
    }

    public int getSkillLevel(SkillType type) {
        Skill skill = type.getSkill();
        double xp = this.getSkillXP(skill);
        return Skill.getLevel(xp, skill.hasSixtyLevels());
    }

    public void addCoins(long coins) {
        this.coins += coins;
    }

    public void subCoins(long coins) {
        this.coins -= coins;
    }

    public void addBankCoins(long bankCoins) {
        this.bankCoins += bankCoins;
    }

    public void subBankCoins(long bankCoins) {
        this.bankCoins -= bankCoins;
    }

    public void addBCollection(int a2) {
        this.sadancollections += (long)a2;
    }

    public void setBCollection(int a2) {
        this.sadancollections = a2;
    }

    public void subBCollection(int a2) {
        this.sadancollections -= (long)a2;
    }

    public long getBCollection() {
        return this.sadancollections;
    }

    public void addBRun6(int a2) {
        this.totalfloor6run += (long)a2;
    }

    public void subBRun6(int a2) {
        this.totalfloor6run -= (long)a2;
    }

    public long getBRun6() {
        return this.totalfloor6run;
    }

    public void addToCollection(ItemCollection collection, int amount) {
        int prevTier = collection.getTier(this.getCollection(collection));
        int i2 = this.collections.getOrDefault(collection, 0);
        this.collections.put(collection, i2 + amount);
        this.updateCollection(collection, prevTier);
    }

    public void addToCollection(ItemCollection collection) {
        this.addToCollection(collection, 1);
    }

    public void setCollection(ItemCollection collection, int amount) {
        int prevTier = collection.getTier(this.getCollection(collection));
        this.collections.put(collection, amount);
        this.updateCollection(collection, prevTier);
    }

    public void zeroCollection(ItemCollection collection) {
        int prevTier = collection.getTier(this.getCollection(collection));
        this.collections.put(collection, 0);
        this.updateCollection(collection, prevTier);
    }

    private void updateCollection(ItemCollection collection, int prevTier) {
        int tier = collection.getTier(this.getCollection(collection));
        if (prevTier != tier) {
            Player player = Bukkit.getPlayer((UUID)this.uuid);
            if (player != null) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------\n");
            builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append("  COLLECTION LEVEL UP ").append(ChatColor.RESET).append(ChatColor.YELLOW).append(collection.getName()).append(" ");
            if (prevTier != 0) {
                builder.append(ChatColor.DARK_GRAY).append(SUtil.toRomanNumeral(prevTier)).append("\u279c");
            }
            builder.append(ChatColor.YELLOW).append(SUtil.toRomanNumeral(tier)).append("\n");
            ItemCollectionRewards rewards = collection.getRewardsFor(tier);
            if (rewards != null && rewards.size() != 0) {
                builder.append(" \n");
                builder.append(ChatColor.GREEN).append(ChatColor.BOLD).append("  REWARD");
                if (rewards.size() != 1) {
                    builder.append("S");
                }
                builder.append(ChatColor.RESET);
                for (ItemCollectionReward reward : rewards) {
                    reward.onAchieve(player);
                    builder.append("\n    ").append(reward.toRewardString());
                }
            }
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------");
            this.send(builder.toString());
        }
    }

    public int getCollection(ItemCollection collection) {
        return this.collections.get(collection);
    }

    public boolean hasCollection(ItemCollection collection, int tier) {
        return collection.getTier(this.getCollection(collection)) >= tier;
    }

    public void addToQuiver(SMaterial material, int amount) {
        int i2 = this.quiver.getOrDefault((Object)material, 0);
        this.setQuiver(material, i2 + amount);
    }

    public void addToQuiver(SMaterial material) {
        this.addToQuiver(material, 1);
    }

    public void setQuiver(SMaterial material, int amount) {
        if (amount == 0) {
            this.quiver.remove((Object)material);
            return;
        }
        this.quiver.put(material, amount);
    }

    public int getQuiver(SMaterial material) {
        return this.quiver.get((Object)material);
    }

    public void subFromQuiver(SMaterial material, int amount) {
        if (!this.quiver.containsKey((Object)material)) {
            return;
        }
        this.setQuiver(material, this.quiver.get((Object)material) - amount);
    }

    public void addtoQuiver(SMaterial material, int amount) {
        if (!this.quiver.containsKey((Object)material)) {
            return;
        }
        this.setQuiver(material, this.quiver.get((Object)material) + amount);
    }

    public void subFromQuiver(SMaterial material) {
        this.subFromQuiver(material, 1);
    }

    public void addtoQuiver(SMaterial material) {
        this.addtoQuiver(material, 1);
    }

    public boolean hasQuiverItem(SMaterial material) {
        return this.quiver.containsKey((Object)material);
    }

    public void clearQuiver() {
        this.quiver.clear();
    }

    public void addPet(SItem item) {
        this.pets.add(new Pet.PetItem(item.getType(), item.getRarity(), item.getData().getDouble("xp")));
    }

    public void equipPet(Pet.PetItem pet) {
        for (Pet.PetItem p2 : this.pets) {
            if (!p2.isActive()) continue;
            p2.setActive(false);
            break;
        }
        pet.setActive(true);
    }

    public void removePet(Pet.PetItem pet) {
        Iterator<Pet.PetItem> iter = this.pets.iterator();
        while (iter.hasNext()) {
            Pet.PetItem p2 = iter.next();
            if (!pet.equals(p2)) continue;
            iter.remove();
            break;
        }
    }

    public Pet.PetItem getActivePet() {
        for (Pet.PetItem pet : this.pets) {
            if (!pet.isActive()) continue;
            return pet;
        }
        return null;
    }

    public Pet getActivePetClass() {
        Pet.PetItem item = this.getActivePet();
        if (item == null) {
            return null;
        }
        return (Pet)item.getType().getGenericInstance();
    }

    public double getSkillXP(Skill skill) {
        if (skill instanceof FarmingSkill) {
            return this.farmingXP;
        }
        if (skill instanceof MiningSkill) {
            return this.miningXP;
        }
        if (skill instanceof CombatSkill) {
            return this.combatXP;
        }
        if (skill instanceof ForagingSkill) {
            return this.foragingXP;
        }
        if (skill instanceof EnchantingSkill) {
            return this.enchantXP;
        }
        if (skill instanceof CatacombsSkill) {
            return this.cataXP;
        }
        if (skill instanceof ArcherSkill) {
            return this.archerXP;
        }
        if (skill instanceof TankSkill) {
            return this.tankXP;
        }
        if (skill instanceof HealerSkill) {
            return this.healerXP;
        }
        if (skill instanceof MageSkill) {
            return this.mageXP;
        }
        if (skill instanceof BerserkSkill) {
            return this.berserkXP;
        }
        return 0.0;
    }

    public void setSkillXP(Skill skill, double xp) {
        double prev = 0.0;
        if (skill instanceof FarmingSkill) {
            prev = this.farmingXP;
            this.farmingXP = xp;
        }
        if (skill instanceof MiningSkill) {
            prev = this.miningXP;
            this.miningXP = xp;
        }
        if (skill instanceof CombatSkill) {
            prev = this.combatXP;
            this.combatXP = xp;
        }
        if (skill instanceof ForagingSkill) {
            prev = this.foragingXP;
            this.foragingXP = xp;
        }
        if (skill instanceof EnchantingSkill) {
            prev = this.enchantXP;
            this.enchantXP = xp;
        }
        if (skill instanceof CatacombsSkill) {
            prev = this.cataXP;
            this.cataXP = xp;
        }
        if (skill instanceof TankSkill) {
            prev = this.tankXP;
            this.tankXP = xp;
        }
        if (skill instanceof ArcherSkill) {
            prev = this.archerXP;
            this.archerXP = xp;
        }
        if (skill instanceof BerserkSkill) {
            prev = this.berserkXP;
            this.berserkXP = xp;
        }
        if (skill instanceof MageSkill) {
            prev = this.mageXP;
            this.mageXP = xp;
        }
        if (skill instanceof HealerSkill) {
            prev = this.healerXP;
            this.healerXP = xp;
        }
        skill.onSkillUpdate(this, prev);
    }

    public void addSkillXP(Skill skill, double xp) {
        this.setSkillXP(skill, this.getSkillXP(skill) + xp);
    }

    public int getHighestRevenantHorror() {
        return this.highestSlayers[0];
    }

    public void setHighestRevenantHorror(int tier) {
        this.highestSlayers[0] = tier;
    }

    public int getHighestTarantulaBroodfather() {
        return this.highestSlayers[1];
    }

    public void setHighestTarantulaBroodfather(int tier) {
        this.highestSlayers[1] = tier;
    }

    public int getHighestSvenPackmaster() {
        return this.highestSlayers[2];
    }

    public void setHighestSvenPackmaster(int tier) {
        this.highestSlayers[2] = tier;
    }

    public int getHighestVoidgloomSeraph() {
        return this.highestSlayers[3];
    }

    public void setHighestVoidgloomSeraph(int tier) {
        this.highestSlayers[3] = tier;
    }

    public int getZombieSlayerXP() {
        return this.slayerXP[0];
    }

    public void setZombieSlayerXP(int xp) {
        this.slayerXP[0] = xp;
    }

    public int getSpiderSlayerXP() {
        return this.slayerXP[1];
    }

    public void setSpiderSlayerXP(int xp) {
        this.slayerXP[1] = xp;
    }

    public int getWolfSlayerXP() {
        return this.slayerXP[2];
    }

    public void setWolfSlayerXP(int xp) {
        this.slayerXP[2] = xp;
    }

    public int getEndermanSlayerXP() {
        return this.slayerXP[3];
    }

    public void setEndermanSlayerXP(int xp) {
        this.slayerXP[3] = xp;
    }

    public void setSlayerXP(SlayerBossType.SlayerMobType type, int xp) {
        this.slayerXP[type.ordinal()] = xp;
    }

    public int getSlayerXP(SlayerBossType.SlayerMobType type) {
        return this.slayerXP[type.ordinal()];
    }

    public int getCrystalLVL(int i2) {
        if (i2 > 7) {
            SLog.severe("Out of bound on action taking data from database!");
            return 0;
        }
        return this.crystalLVL[i2];
    }

    public void setCrystalLVL(int i2, int a2) {
        if (i2 > 7) {
            SLog.severe("Out of bound on action taking data from database!");
            return;
        }
        this.crystalLVL[i2] = a2;
    }

    public int getSlayerCombatXPBuff() {
        int buff = 0;
        for (int highest : this.highestSlayers) {
            buff += highest == 4 ? 5 : highest;
        }
        return buff;
    }

    public void startSlayerQuest(SlayerBossType type) {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        this.slayerQuest = new SlayerQuest(type, System.currentTimeMillis());
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 2.0f);
        player.sendMessage("  " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "SLAYER QUEST STARTED!");
        player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "\u00bb " + ChatColor.GRAY + "Slay " + ChatColor.RED + SUtil.commaify(type.getSpawnXP()) + " Combat XP" + ChatColor.GRAY + " worth of " + type.getType().getPluralName() + ".");
    }

    public void failSlayerQuest() {
        if (this.slayerQuest == null) {
            return;
        }
        if (this.slayerQuest.getDied() != 0L) {
            return;
        }
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        this.slayerQuest.setDied(System.currentTimeMillis());
        if (this.slayerQuest.getEntity() != null) {
            this.slayerQuest.getEntity().remove();
            this.slayerQuest.getEntity().getFunction().onDeath(this.slayerQuest.getEntity(), (Entity)this.slayerQuest.getEntity().getEntity(), (Entity)player);
        }
        SUtil.delay(() -> {
            this.removeAllSlayerBosses();
            player.sendMessage("  " + ChatColor.RED + ChatColor.BOLD + "SLAYER QUEST FAILED!");
            player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "\u00bb " + ChatColor.GRAY + "You need to learn how to play this game first!");
        }, 2L);
    }

    public void removeAllSlayerBosses() {
        for (Entity e2 : Bukkit.getPlayer((UUID)this.uuid).getWorld().getEntities()) {
            if (!e2.hasMetadata("BOSS_OWNER_" + this.uuid.toString()) || !e2.hasMetadata("SlayerBoss")) continue;
            e2.remove();
        }
    }

    public void send(String message) {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        player.sendMessage(Sputnik.trans(message));
    }

    public void damageEntity(Damageable entity1, double damageBase) {
        SItem sitem;
        if (entity1.isDead()) {
            return;
        }
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        double damage = damageBase;
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put((Entity)entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity1)) {
                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity1);
                if (defensepercent > 100) {
                    defensepercent = 100;
                }
                damage -= damage * (double)defensepercent / 100.0;
            }
            PlayerUtils.handleSpecEntity((Entity)entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            double formula = damage / entity1.getMaxHealth() * 100.0;
            damage = formula < 10.0 ? (damage *= 1.0) : (formula > 10.0 && formula < 15.0 ? (damage -= damage * 90.0 / 100.0) : (formula > 15.0 && formula < 20.0 ? (damage -= damage * 99.0 / 100.0) : (formula > 20.0 && formula <= 25.0 ? (damage -= damage * 99.9 / 100.0) : (formula > 25.0 ? (damage *= 0.0) : (damage *= 1.0)))));
            PlayerUtils.handleSpecEntity((Entity)entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        entity1.damage(1.0E-5);
        if (player.getItemInHand() != null && (sitem = SItem.find(player.getItemInHand())) != null && sitem.getEnchantment(EnchantmentType.VAMPIRISM) != null) {
            double lvl = sitem.getEnchantment(EnchantmentType.VAMPIRISM).getLevel();
            if (lvl > 100.0) {
                lvl = 100.0;
            }
            double aB2 = player.getHealth() + lvl / 100.0 * (player.getMaxHealth() - player.getHealth());
            double aC2 = Math.min(player.getMaxHealth(), aB2);
            player.setHealth(aC2);
        }
    }

    public void damageEntityIgnoreShield(Damageable entity1, double damageBase) {
        SItem sitem;
        if (entity1.isDead()) {
            return;
        }
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        double damage = damageBase;
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put((Entity)entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            PlayerUtils.handleSpecEntity((Entity)entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            double formula = damage / entity1.getMaxHealth() * 100.0;
            damage = formula < 10.0 ? (damage *= 1.0) : (formula > 10.0 && formula < 15.0 ? (damage -= damage * 90.0 / 100.0) : (formula > 15.0 && formula < 20.0 ? (damage -= damage * 99.0 / 100.0) : (formula > 20.0 && formula <= 25.0 ? (damage -= damage * 99.9 / 100.0) : (formula > 25.0 ? (damage *= 0.0) : (damage *= 1.0)))));
            PlayerUtils.handleSpecEntity((Entity)entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        entity1.damage(1.0E-5);
        if (player.getItemInHand() != null && (sitem = SItem.find(player.getItemInHand())) != null && sitem.getEnchantment(EnchantmentType.VAMPIRISM) != null) {
            double lvl = sitem.getEnchantment(EnchantmentType.VAMPIRISM).getLevel();
            if (lvl > 100.0) {
                lvl = 100.0;
            }
            double aB2 = player.getHealth() + lvl / 100.0 * (player.getMaxHealth() - player.getHealth());
            double aC2 = Math.min(player.getMaxHealth(), aB2);
            player.setHealth(aC2);
        }
    }

    public static File getDataDirectory() {
        return USER_FOLDER;
    }

    public void damageEntityBowEman(Damageable entity1, double damage, Player player, Arrow a2) {
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put((Entity)entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity1)) {
                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity1);
                if (defensepercent > 100) {
                    defensepercent = 100;
                }
                damage -= damage * (double)defensepercent / 100.0;
            }
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            double formula = damage / entity1.getMaxHealth() * 100.0;
            damage = formula < 10.0 ? (damage *= 1.0) : (formula > 10.0 && formula < 15.0 ? (damage -= damage * 90.0 / 100.0) : (formula > 15.0 && formula < 20.0 ? (damage -= damage * 99.0 / 100.0) : (formula > 20.0 && formula <= 25.0 ? (damage -= damage * 99.9 / 100.0) : (formula > 25.0 ? (damage *= 0.0) : (damage *= 1.0)))));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        PlayerUtils.handleSpecEntity((Entity)entity1, player, new AtomicDouble(damage));
        a2.remove();
    }

    public void damageEntity(LivingEntity entity) {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        entity.damage(0.0, (Entity)player);
    }

    public void damage(double d2, EntityDamageEvent.DamageCause cause, Entity entity) {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        EntityHuman human = ((CraftHumanEntity)player).getHandle();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        double trueDefense = statistics.getTrueDefense().addAll();
        double health = statistics.getMaxHealth().addAll();
        d2 -= d2 * (trueDefense / (trueDefense + 100.0));
        if (player.getHealth() + (double)SputnikPlayer.getCustomAbsorptionHP(player).intValue() - d2 <= 0.0) {
            this.kill(cause, entity);
            return;
        }
        float ab2 = (float)Math.max(0.0, (double)SputnikPlayer.getCustomAbsorptionHP(player).intValue() - d2);
        double actual = Math.max(0.0, d2 - (double)SputnikPlayer.getCustomAbsorptionHP(player).intValue());
        SputnikPlayer.setCustomAbsorptionHP(player, ab2);
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK || cause == EntityDamageEvent.DamageCause.LAVA) {
            boolean damage = false;
            if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK && cause != EntityDamageEvent.DamageCause.LAVA) {
                actual = 5.0;
            } else if (cause == EntityDamageEvent.DamageCause.LAVA) {
                actual = 20.0;
            }
        }
        if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            actual = health * 5.0 / 100.0;
        }
        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            actual = health * 5.0 / 100.0;
        }
        if (cause == EntityDamageEvent.DamageCause.CONTACT) {
            actual = 5.0;
        }
        if (cause == EntityDamageEvent.DamageCause.WITHER || cause == EntityDamageEvent.DamageCause.POISON) {
            actual = 20.0;
        }
        player.setHealth(Math.max(0.0, player.getHealth() - actual));
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK || cause == EntityDamageEvent.DamageCause.LAVA) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.GOLD);
        } else if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.CONTACT || cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.GRAY);
        } else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.DARK_AQUA);
        } else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.RED);
        } else if (cause == EntityDamageEvent.DamageCause.POISON) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.DARK_GREEN);
        } else if (cause == EntityDamageEvent.DamageCause.WITHER) {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.BLACK);
        } else {
            PlayerListener.spawnSpecialDamageInd((Entity)player, actual, ChatColor.WHITE);
        }
        if (player.getHealth() <= 0.0) {
            player.setFireTicks(0);
            this.kill(cause, entity);
        }
    }

    public void damage(double d2) {
        this.damage(d2, EntityDamageEvent.DamageCause.CUSTOM, null);
    }

    public void kill(EntityDamageEvent.DamageCause cause, Entity entity) {
        Object user;
        SlayerQuest quest;
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        player.setHealth(player.getMaxHealth());
        for (int i2 = 0; i2 < player.getInventory().getSize(); ++i2) {
            ItemStack stack = player.getInventory().getItem(i2);
            SItem sItem = SItem.find(stack);
            if (sItem != null) continue;
        }
        player.setFireTicks(0);
        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0.0f);
        SputnikPlayer.AbsHP.put(player.getUniqueId(), 0);
        if (!player.getWorld().getName().contains("f6")) {
            this.sendToSpawn();
            if (!PlayerUtils.cookieBuffActive(player)) {
                this.clearPotionEffects();
            }
        } else {
            final World w2 = player.getWorld();
            for (Entity en : player.getWorld().getEntities()) {
                if (en instanceof HumanEntity) continue;
                en.remove();
            }
            SadanFunction.sendReMsg(false, w2);
            SadanHuman.IsMusicPlaying.put(w2.getUID(), false);
            SadanHuman.BossRun.put(w2.getUID(), false);
            SadanFunction.endRoom2(w2);
            final BukkitTask bkt = SadanHuman.playHBS(w2);
            new BukkitRunnable(){

                public void run() {
                    if (w2 == null || w2.getPlayers().size() == 0) {
                        bkt.cancel();
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
            SUtil.sendTitle(player, ChatColor.YELLOW + "You became a ghost!");
            SUtil.sendSubtitle(player, ChatColor.GRAY + "Hopefully your teammates can revive you.");
            if (cause == EntityDamageEvent.DamageCause.VOID) {
                this.sendToSpawn();
            }
        }
        String name = null;
        if (entity != null) {
            SEntity sEntity = SEntity.findSEntity(entity);
            name = sEntity != null ? sEntity.getStatistics().getEntityName() : entity.getCustomName();
        }
        String message = "You died";
        String out = "%s died";
        switch (cause) {
            case VOID: {
                message = "You fell into the void";
                out = "%s fell into the void";
                break;
            }
            case FALL: {
                message = "You fell to your death";
                out = "%s fell to their death";
                break;
            }
            case ENTITY_ATTACK: {
                message = "You were killed by " + name + ChatColor.GRAY;
                out = "%s was killed by " + name + ChatColor.GRAY;
                break;
            }
            case ENTITY_EXPLOSION: {
                message = "You were killed by " + name + ChatColor.GRAY + "'s explosion";
                out = "%s was killed by " + name + ChatColor.GRAY + "'s explosion";
                break;
            }
            case FIRE: 
            case FIRE_TICK: 
            case LAVA: {
                message = "You burned to death";
                out = "%s burned to death";
                break;
            }
            case MAGIC: {
                message = "You died by magic";
                out = "%s was killed by magic";
                break;
            }
            case POISON: {
                message = "You died by poisoning";
                out = "%s was killed by poisoning";
                break;
            }
            case LIGHTNING: {
                message = "You were struck by lightning and died";
                out = "%s was struck by lightning and killed";
                break;
            }
            case DROWNING: {
                message = "You drowned";
                out = "%s drowned";
                break;
            }
            case SUFFOCATION: {
                message = "You suffocated";
                out = "%s suffocated";
            }
        }
        if (this.slayerQuest != null && this.slayerQuest.getKilled() == 0L && (quest = ((User)(user = User.getUser(player.getUniqueId()))).getSlayerQuest()).getXp() >= (double)quest.getType().getSpawnXP()) {
            this.failSlayerQuest();
        }
        player.playSound(player.getLocation(), Sound.HURT_FLESH, 1.0f, 1.0f);
        if (!player.getWorld().getName().equalsIgnoreCase("limbo") && !player.getWorld().getName().contains("f6")) {
            player.sendMessage(ChatColor.RED + " \u2620 " + ChatColor.GRAY + message + ChatColor.GRAY + ".");
            SUtil.broadcastExcept(ChatColor.RED + " \u2620 " + ChatColor.GRAY + String.format(out, player.getName()) + ChatColor.GRAY + ".", player);
        }
        if (player.getWorld().getName().contains("f6")) {
            player.playSound(player.getLocation(), Sound.HURT_FLESH, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + " \u2620 " + ChatColor.GRAY + message + ChatColor.GRAY + " and became a ghost.");
            SUtil.broadcastExcept(ChatColor.RED + " \u2620 " + ChatColor.GRAY + String.format(out, player.getName()) + ChatColor.GRAY + " and became a ghost.", player);
        }
        for (Entity e2 : player.getWorld().getEntities()) {
            if (!e2.hasMetadata("owner") || !((MetadataValue)e2.getMetadata("owner").get(0)).asString().equals(player.getUniqueId().toString())) continue;
            e2.remove();
            player.sendMessage(ChatColor.RED + "\u2620 Your Voidling's Warden Boss has been despawned since you died!");
            StaticWardenManager.endFight();
        }
        if (PlayerUtils.cookieBuffActive(player)) {
            player.sendMessage(ChatColor.RED + "You died!");
            return;
        }
        if (this.isOnIsland() && cause == EntityDamageEvent.DamageCause.VOID || this.permanentCoins || player.getWorld().getName().equalsIgnoreCase("limbo") || player.getWorld().getName().contains("f6")) {
            return;
        }
        int piggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.PIGGY_BANK);
        if (piggyIndex != -1 && this.coins >= 20000L) {
            SItem cracked = SItem.of(SMaterial.CRACKED_PIGGY_BANK);
            SItem piggy = SItem.find(player.getInventory().getItem(piggyIndex));
            if (piggy.getReforge() != null) {
                cracked.setReforge(piggy.getReforge());
            }
            player.getInventory().setItem(piggyIndex, cracked.getStack());
            player.sendMessage(ChatColor.RED + "You died and your piggy bank cracked!");
            return;
        }
        player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1.0f, 2.0f);
        int crackedPiggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.CRACKED_PIGGY_BANK);
        if (crackedPiggyIndex != -1 && this.coins >= 20000L) {
            SItem broken = SItem.of(SMaterial.BROKEN_PIGGY_BANK);
            SItem crackedPiggy = SItem.find(player.getInventory().getItem(crackedPiggyIndex));
            if (crackedPiggy.getReforge() != null) {
                broken.setReforge(crackedPiggy.getReforge());
            }
            player.getInventory().setItem(crackedPiggyIndex, broken.getStack());
            long sub = (long)((double)this.coins * 0.25);
            player.sendMessage(ChatColor.RED + "You died, lost " + SUtil.commaify(sub) + " coins, and your piggy bank broke!");
            this.coins -= sub;
            if (SkyBlock.getPlugin().config.getBoolean("Config")) {
                this.configsave();
            } else {
                this.save();
            }
            return;
        }
        long sub2 = this.coins / 2L;
        player.sendMessage(ChatColor.RED + "You died and lost " + SUtil.commaify(sub2) + " coins!");
        this.coins -= sub2;
        this.save();
    }

    public void setIslandLocation(double x2, double z2) {
        this.islandX = x2;
        this.islandZ = z2;
    }

    public void addPotionEffect(PotionEffect effect) {
        this.effects.add(new ActivePotionEffect(effect, effect.getDuration()));
    }

    public void removePotionEffect(PotionEffectType type) {
        for (ActivePotionEffect effect : this.effects) {
            if (effect.getEffect().getType() != type) continue;
            effect.setRemaining(0L);
        }
    }

    public ActivePotionEffect getPotionEffect(PotionEffectType type) {
        for (ActivePotionEffect effect : this.effects) {
            if (effect.getEffect().getType() != type) continue;
            return effect;
        }
        return null;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return this.effects.stream().filter(effect -> effect.getEffect().getType() == type).toArray().length != 0;
    }

    public void clearPotionEffects() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player != null) {
            for (org.bukkit.potion.PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
        for (ActivePotionEffect effect2 : this.effects) {
            effect2.setRemaining(0L);
        }
    }

    public boolean isOnIsland() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        return player != null && this.isOnIsland(player.getLocation());
    }

    public boolean isOnIsland(Block block) {
        return this.isOnIsland(block.getLocation());
    }

    public boolean isOnIsland(Location location) {
        World world = Bukkit.getWorld((String)"islands");
        if (world == null) {
            return false;
        }
        double x2 = location.getX();
        double z2 = location.getZ();
        return world.getUID().equals(location.getWorld().getUID()) && x2 >= this.islandX - 125.0 && x2 <= this.islandX + 125.0 && z2 >= this.islandZ - 125.0 && z2 <= this.islandZ + 125.0;
    }

    public boolean isOnUserIsland() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return false;
        }
        World world = Bukkit.getWorld((String)"islands");
        if (world == null) {
            return false;
        }
        double x2 = player.getLocation().getX();
        double z2 = player.getLocation().getZ();
        return world.getUID().equals(player.getWorld().getUID()) && x2 < this.islandX - 125.0 && x2 > this.islandX + 125.0 && z2 < this.islandZ - 125.0 && z2 > this.islandZ + 125.0;
    }

    public List<AuctionItem> getBids() {
        return AuctionItem.getAuctions().stream().filter(item -> {
            for (AuctionBid bid : item.getBids()) {
                if (!bid.getBidder().equals(this.uuid) || !item.getParticipants().contains(this.uuid)) continue;
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<AuctionItem> getAuctions() {
        return AuctionItem.getAuctions().stream().filter(item -> item.getOwner().getUuid().equals(this.uuid) && item.getParticipants().contains(this.uuid)).collect(Collectors.toList());
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer((UUID)this.uuid);
    }

    public EntityPlayer toNMSPlayer() {
        return ((CraftPlayer)Bukkit.getPlayer((UUID)this.uuid)).getHandle();
    }

    public void sendToSpawn() {
        Player player = Bukkit.getPlayer((UUID)this.uuid);
        if (player == null) {
            return;
        }
        if (this.isOnIsland()) {
            PlayerUtils.sendToIsland(player);
        } else if (this.lastRegion != null) {
            switch (this.lastRegion.getType()) {
                case BANK: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case FARM: 
                case RUINS: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case FOREST: 
                case LIBRARY: 
                case COAL_MINE: 
                case COAL_MINE_CAVES: 
                case MOUNTAIN: 
                case VILLAGE: {
                    Location l2 = new Location(Bukkit.getWorld((String)"world"), -2.5, 70.0, -68.5, 180.0f, 0.0f);
                    player.teleport(l2);
                }
                case HIGH_LEVEL: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case BLACKSMITH: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case AUCTION_HOUSE: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case WILDERNESS: 
                case BAZAAR_ALLEY: 
                case COLOSSEUM: 
                case GRAVEYARD: {
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
                }
                case SPIDERS_DEN: {
                    player.teleport(player.getWorld().getSpawnLocation());
                }
                case SPIDERS_DEN_HIVE: {
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
                }
                default: {
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
                }
            }
        } else {
            Location l3 = new Location(Bukkit.getWorld((String)"world"), -2.5, 70.0, -68.5, 180.0f, 0.0f);
            player.teleport(l3);
        }
    }

    public static String generateRandom() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = SUtil.random(7, 7);
        Random random = new Random();
        String generatedString = random.ints(97, 123).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }

    public static User getUser(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (USER_CACHE.containsKey(uuid)) {
            return USER_CACHE.get(uuid);
        }
        return new User(uuid);
    }

    public static User getUser(Player player) {
        if (player == null) {
            return null;
        }
        return User.getUser(player.getUniqueId());
    }

    public void sendPacket(Packet<?> packet) {
        this.toNMSPlayer().playerConnection.sendPacket(packet);
    }

    public static Collection<User> getCachedUsers() {
        return USER_CACHE.values();
    }

    public boolean hasPermission(String permission) {
        return true;
    }

    public static Map<UUID, User> getHash() {
        return USER_CACHE;
    }

    public ItemStack updateItemBoost(SItem sitem) {
        if (sitem.getDataBoolean("dungeons_item") && sitem.getType().getStatistics().getType() != GenericItemType.ITEM && sitem.getType().getStatistics().getType() != GenericItemType.PET && sitem.getType().getStatistics().getType() != GenericItemType.BLOCK && sitem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
            int itemstar = sitem.getDataInt("itemStar");
            double hpbboostweapons = 0.0;
            double hpbboosthp = 0.0;
            double hpbboostdef = 0.0;
            PlayerBoostStatistics hs = sitem.getType().getBoostStatistics();
            ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
            Reforge reforge = sitem.getReforge() == null ? Reforge.blank() : sitem.getReforge();
            double bonusEn = 0.0;
            if (sitem.getType().getStatistics().getType() == GenericItemType.WEAPON && sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null) {
                Enchantment e2 = sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL);
                bonusEn = hs.getBaseDamage() * (e2.getLevel() * 210) / 100;
            }
            if (sitem.getType().getStatistics().getType() == GenericItemType.WEAPON || sitem.getType().getStatistics().getType() == GenericItemType.RANGED_WEAPON) {
                hpbboostweapons = sitem.getDataInt("hpb") * 2;
            } else if (sitem.getType().getStatistics().getType() == GenericItemType.ARMOR) {
                hpbboosthp = sitem.getDataInt("hpb") * 4;
                hpbboostdef = sitem.getDataInt("hpb") * 2;
            }
            is.setDamage(this.getFinal((double)hs.getBaseDamage() + hpbboostweapons + bonusEn, itemstar));
            is.setStrength(this.getFinal(hs.getBaseStrength() + (hpbboostweapons + reforge.getStrength().getForRarity(sitem.getRarity())), itemstar));
            is.setCritchance(this.getFinal(hs.getBaseCritChance() + reforge.getCritChance().getForRarity(sitem.getRarity()), itemstar));
            is.setCritdamage(this.getFinal(hs.getBaseCritDamage() + reforge.getCritDamage().getForRarity(sitem.getRarity()), itemstar));
            is.setIntelligence(this.getFinal(hs.getBaseIntelligence() + reforge.getIntelligence().getForRarity(sitem.getRarity()), itemstar));
            is.setFerocity(this.getFinal(hs.getBaseFerocity() + reforge.getFerocity().getForRarity(sitem.getRarity()), itemstar));
            is.setSpeed(this.getFinal(hs.getBaseSpeed(), itemstar));
            is.setAtkSpeed(this.getFinal(hs.getBaseAttackSpeed() + reforge.getAttackSpeed().getForRarity(sitem.getRarity()), itemstar));
            is.setMagicFind(this.getFinal(hs.getBaseMagicFind(), itemstar));
            double health = hs.getBaseHealth();
            double defense = hs.getBaseDefense();
            if (sitem.isEnchantable()) {
                for (Enchantment enchantment : sitem.getEnchantments()) {
                    if (enchantment.getType() == EnchantmentType.GROWTH) {
                        health += 15.0 * (double)enchantment.getLevel();
                    }
                    if (enchantment.getType() != EnchantmentType.PROTECTION) continue;
                    defense += 3.0 * (double)enchantment.getLevel();
                }
            }
            is.setHealth(this.getFinal(health + hpbboosthp, itemstar));
            is.setDefense(this.getFinal(defense + hpbboostdef, itemstar));
            is.saveTo(sitem);
            return sitem.getStack();
        }
        return sitem.getStack();
    }

    public double getFinal(double stat, int starNum) {
        int cataLVL = Skill.getLevel(this.getCataXP(), false);
        int cataBuffPercentage = cataLVL * 5;
        int percentMstars = (starNum - 5) * 5;
        if (starNum <= 5) {
            percentMstars *= 0;
        }
        double d2 = 1.0 + (double)percentMstars / 100.0;
        return stat * ((double)(1 + percentMstars / 100) * (1.0 + 0.1 * (double)Math.min(5, starNum)) * (double)(1 + cataBuffPercentage / 100) * d2);
    }

    public void sendClickableMessage(String message, TextComponent[] hover, String commandToRun) {
        TextComponent tcp = new TextComponent(Sputnik.trans(message));
        if (hover != null) {
            tcp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (BaseComponent[])hover));
        }
        if (commandToRun != null) {
            tcp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandToRun));
        }
        this.toBukkitPlayer().spigot().sendMessage((BaseComponent)tcp);
    }

    public boolean isCompletedSign() {
        return this.isCompletedSign;
    }

    public void setCompletedSign(boolean isCompletedSign) {
        this.isCompletedSign = isCompletedSign;
    }

    public long getSadancollections() {
        return this.sadancollections;
    }

    public long getTotalfloor6run() {
        return this.totalfloor6run;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Map<ItemCollection, Integer> getCollections() {
        return this.collections;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public long getCoins() {
        return this.coins;
    }

    public long getBits() {
        return this.bits;
    }

    public void setBits(long bits) {
        this.bits = bits;
    }

    public void setBankCoins(long bankCoins) {
        this.bankCoins = bankCoins;
    }

    public long getBankCoins() {
        return this.bankCoins;
    }

    public void setStashedItems(List<ItemStack> stashedItems) {
        this.stashedItems = stashedItems;
    }

    public List<ItemStack> getStashedItems() {
        return this.stashedItems;
    }

    public void setCooldownAltar(int cooldownAltar) {
        this.cooldownAltar = cooldownAltar;
    }

    public int getCooldownAltar() {
        return this.cooldownAltar;
    }

    public void setHeadShot(boolean headShot) {
        this.headShot = headShot;
    }

    public boolean isHeadShot() {
        return this.headShot;
    }

    public void setPlayingSong(boolean playingSong) {
        this.playingSong = playingSong;
    }

    public boolean isPlayingSong() {
        return this.playingSong;
    }

    public void setInDanger(boolean inDanger) {
        this.inDanger = inDanger;
    }

    public boolean isInDanger() {
        return this.inDanger;
    }

    public void setLastRegion(Region lastRegion) {
        this.lastRegion = lastRegion;
    }

    public Region getLastRegion() {
        return this.lastRegion;
    }

    public Map<SMaterial, Integer> getQuiver() {
        return this.quiver;
    }

    public List<ActivePotionEffect> getEffects() {
        return this.effects;
    }

    public List<String> getTalkedNPCs() {
        return this.talkedNPCs;
    }

    public List<String> getTalkedVillagers() {
        return this.talkedVillagers;
    }

    public double getFarmingXP() {
        return this.farmingXP;
    }

    public boolean isBoneToZeroDamage() {
        return this.boneToZeroDamage;
    }

    public void setBoneToZeroDamage(boolean boneToZeroDamage) {
        this.boneToZeroDamage = boneToZeroDamage;
    }

    public boolean isCooldownAPI() {
        return this.cooldownAPI;
    }

    public void setCooldownAPI(boolean cooldownAPI) {
        this.cooldownAPI = cooldownAPI;
    }

    public double getMiningXP() {
        return this.miningXP;
    }

    public double getCombatXP() {
        return this.combatXP;
    }

    public double getEnchantXP() {
        return this.enchantXP;
    }

    public double getArcherXP() {
        return this.archerXP;
    }

    public double getCataXP() {
        return this.cataXP;
    }

    public double getBerserkXP() {
        return this.berserkXP;
    }

    public double getHealerXP() {
        return this.healerXP;
    }

    public double getTankXP() {
        return this.tankXP;
    }

    public double getMageXP() {
        return this.mageXP;
    }

    public double getForagingXP() {
        return this.foragingXP;
    }

    public boolean isSaveable() {
        return this.saveable;
    }

    public void setSaveable(boolean saveable) {
        this.saveable = saveable;
    }

    public int getBonusFerocity() {
        return this.bonusFerocity;
    }

    public void setBonusFerocity(int bonusFerocity) {
        this.bonusFerocity = bonusFerocity;
    }

    public boolean isFatalActive() {
        return this.fatalActive;
    }

    public void setFatalActive(boolean fatalActive) {
        this.fatalActive = fatalActive;
    }

    public boolean isPermanentCoins() {
        return this.permanentCoins;
    }

    public void setPermanentCoins(boolean permanentCoins) {
        this.permanentCoins = permanentCoins;
    }

    public void setSlayerQuest(SlayerQuest slayerQuest) {
        this.slayerQuest = slayerQuest;
    }

    public SlayerQuest getSlayerQuest() {
        return this.slayerQuest;
    }

    public List<Pet.PetItem> getPets() {
        return this.pets;
    }

    public List<String> getUnlockedRecipes() {
        return this.unlockedRecipes;
    }

    public AuctionSettings getAuctionSettings() {
        return this.auctionSettings;
    }

    public boolean isAuctionCreationBIN() {
        return this.auctionCreationBIN;
    }

    public void setAuctionCreationBIN(boolean auctionCreationBIN) {
        this.auctionCreationBIN = auctionCreationBIN;
    }

    public AuctionEscrow getAuctionEscrow() {
        return this.auctionEscrow;
    }

    public void setAuctionEscrow(AuctionEscrow auctionEscrow) {
        this.auctionEscrow = auctionEscrow;
    }

    public boolean isVoidlingWardenActive() {
        return this.voidlingWardenActive;
    }

    public void setVoidlingWardenActive(boolean voidlingWardenActive) {
        this.voidlingWardenActive = voidlingWardenActive;
    }

    public boolean isWaitingForSign() {
        return this.waitingForSign;
    }

    public void setWaitingForSign(boolean waitingForSign) {
        this.waitingForSign = waitingForSign;
    }

    public void setSignContent(String signContent) {
        this.signContent = signContent;
    }

    public String getSignContent() {
        return this.signContent;
    }

    public List<String> getCompletedQuests() {
        return this.completedQuests;
    }

    public void setCompletedQuests(List<String> completedQuests) {
        this.completedQuests = completedQuests;
    }

    public List<String> getCompletedObjectives() {
        return this.completedObjectives;
    }

    public void setCompletedObjectives(List<String> completedObjectives) {
        this.completedObjectives = completedObjectives;
    }

    public Double getIslandX() {
        return this.islandX;
    }

    public void setIslandX(Double islandX) {
        this.islandX = islandX;
    }

    public Double getIslandZ() {
        return this.islandZ;
    }

    public void setIslandZ(Double islandZ) {
        this.islandZ = islandZ;
    }

    public void setRank(PlayerRank rank) {
        this.rank = rank;
    }

    public PlayerRank getRank() {
        return this.rank;
    }

    public boolean isHasIsland() {
        return this.hasIsland;
    }

    static {
        sbc = SkyBlock.getInstance().config;
        USER_CACHE = new HashMap<UUID, User>();
        plugin = SkyBlock.getPlugin();
        USER_FOLDER = new File(plugin.getDataFolder(), "./users");
    }
}

