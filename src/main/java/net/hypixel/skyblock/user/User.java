package net.hypixel.skyblock.user;

import com.google.common.util.concurrent.AtomicDouble;
import com.mongodb.client.MongoCollection;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.features.auction.AuctionBid;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.auction.AuctionItem;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.collection.ItemCollectionReward;
import net.hypixel.skyblock.features.collection.ItemCollectionRewards;
import net.hypixel.skyblock.features.dungeons.stats.ItemSerial;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanFunction;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.hypixel.skyblock.features.reforge.Reforge;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.skill.*;
import net.hypixel.skyblock.features.slayer.SlayerBossType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.util.*;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.gui.PetsGUI;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.listener.PlayerListener;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class User {
    public static final int ISLAND_SIZE = 125;
    public static final Map<UUID, User> USER_CACHE;
    private static final SkyBlock plugin;

    public final Map<String, Object> dataCache;

    @Getter
    public long sadancollections;
    @Getter
    public long totalfloor6run;
    @Getter
    private UUID uuid;

    private Document dataDocument;

    @Getter
    @Setter
    public final Map<ItemCollection, Integer> collections;
    @Setter
    @Getter
    private long coins;
    @Getter @Setter
    private long bits;
    @Setter
    @Getter
    private long bankCoins;
    @Setter
    @Getter
    private List<ItemStack> stashedItems;
    @Setter
    @Getter
    private int cooldownAltar;
    @Setter
    @Getter
    private boolean headShot;
    private static final boolean multiServer = false;
    @Setter
    @Getter
    private boolean playingSong;
    @Setter
    @Getter
    private boolean inDanger;
    @Setter
    @Getter
    private Region lastRegion;
    @Getter
    private final Map<SMaterial, Integer> quiver;
    @Getter
    private final List<ActivePotionEffect> effects;

    @Getter
    private List<String> talkedNPCs;
    @Getter
    double farmingXP;
    @Getter
    @Setter
    private boolean boneToZeroDamage;
    @Getter
    @Setter
    private boolean cooldownAPI;
    @Getter
    double miningXP;
    @Getter
    double combatXP;
    @Getter
    private double enchantXP;
    @Getter
    private double archerXP;
    @Getter
    private double cataXP;
    @Getter
    private double berserkXP;
    @Getter
    private double healerXP;
    @Getter
    private double tankXP;
    @Getter
    private double mageXP;
    @Getter
    double foragingXP;
    final int[] highestSlayers;
    final int[] slayerXP;
    private final int[] crystalLVL;
    @Getter
    @Setter
    private boolean saveable;

    @Getter
    @Setter
    private int bonusFerocity;
    @Getter
    @Setter
    private boolean fatalActive;
    @Getter
    @Setter
    private boolean permanentCoins;
    @Setter
    @Getter
    private SlayerQuest slayerQuest;
    @Getter
    List<Pet.PetItem> pets;
    @Getter
    private List<String> unlockedRecipes;
    @Getter
    AuctionSettings auctionSettings;
    @Getter
    @Setter
    private boolean auctionCreationBIN;
    @Getter
    @Setter
    private AuctionEscrow auctionEscrow;
    @Getter
    @Setter
    private boolean voidlingWardenActive;
    @Getter
    @Setter
    private boolean waitingForSign;
    @Setter
    @Getter
    private String signContent;
    private boolean isCompletedSign;

    @Getter
    @Setter
    public List<String> completedQuests;

    @Getter
    @Setter
    public List<String> completedObjectives;
    @Getter
    @Setter
    private Double islandX;
    @Getter
    @Setter
    private Double islandZ;



    @Setter
    @Getter
    public PlayerRank rank;

    public Player player;


    public List<String> foundzone;

    private User(final UUID uuid) {
        this.stashedItems = new ArrayList<ItemStack>();
        this.cooldownAltar = 0;
        this.dataCache = new HashMap<>();
        this.headShot = false;
        this.playingSong = false;
        this.inDanger = false;
        this.player = Bukkit.getPlayer(uuid);
        this.boneToZeroDamage = false;
        this.rank = PlayerRank.DEFAULT;
        this.cooldownAPI = false;
        this.saveable = true;
        this.waitingForSign = false;
        this.signContent = null;
        this.isCompletedSign = false;
        this.uuid = uuid;
        this.foundzone = new ArrayList<>();
        this.collections = ItemCollection.getDefaultCollections();
        this.totalfloor6run = 0L;
        this.coins = 0L;
        this.bits = 0L;
        this.bankCoins = 0L;
        this.sadancollections = 0L;
        this.lastRegion = null;
        this.talkedNPCs = new CopyOnWriteArrayList<>();
        this.quiver = new HashMap<>();
        this.effects = new ArrayList<>();
        this.unlockedRecipes = new ArrayList<>();
        this.completedQuests = new ArrayList<>();
        this.completedObjectives = new ArrayList<>();
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
        User.USER_CACHE.put(uuid, this);
        load();
    }

    public void unload() {
        User.USER_CACHE.remove(this.uuid);
    }


    public synchronized void addTalkedNPC(String name) {
        if (!talkedNPCs.contains(name)) {
            talkedNPCs.add(name);
        }
    }

    public List<String> getdiscoveredzones(){return this.foundzone;}


    public void  addnewzone(String q){this.foundzone.add(q);}


    public void addCompletedQuest(String questName) {
        this.completedQuests.add(questName);
    }

    public void addCompletedObjectives(String questName) {
        this.completedObjectives.add(questName);
    }

    public QuestLine getQuestLine() {
        return SkyBlock.getPlugin().getQuestLineHandler().getFromPlayer(this);
    }

    public void send(String message, Object... args) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.sendMessage(Sputnik.trans(String.format(message, args)));
    }

    public void onNewZone(RegionType zone, String... features) {
        send("");
        send("§6§l NEW AREA DISCOVERED!");
        send("§7  ⏣ " + zone.getColor() + zone.getName());
        send("");
        if (features.length > 0) {
            for (String feature : features) {
                send("§7   ⬛ §f%s", feature);
            }
        } else {
            send("§7   ⬛ §cNot much yet!");
        }
        send("");

        Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.LEVEL_UP, 1f, 0f);
        Player player = Bukkit.getPlayer(uuid);
        SUtil.sendTypedTitle(player, zone.getColor() + zone.getName(), PacketPlayOutTitle.EnumTitleAction.TITLE);
        SUtil.sendTypedTitle(player,  "§6§lNEW AREA DISCOVERED!", PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
    }

    public Region getRegion() {
        if (isOnIsland() || isOnUserIsland()) {
            return Region.getIslandRegion();
        }
        String worldName = player.getWorld().getName();
        if (worldName.startsWith("f6")) {
            return Region.get("Catacombs (F6)");
        }

        return Region.getRegionOfEntity(Bukkit.getPlayer(this.uuid));
    }


    public void asyncSavingData() {
        save();
    }


    public void loadStatic() {
        Player player = Bukkit.getPlayer(this.uuid);
        User user = getUser(this.uuid);
        // todo : fix it
        PlayerUtils.AUTO_SLAYER.put(player.getUniqueId(), true);
        PetsGUI.setShowPets(player, true);
    }

    public void kick(){
        if (toBukkitPlayer().isOnline()){
            toBukkitPlayer().kickPlayer("server restart...");
        }
    }



    public CompletableFuture<Void> save() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        SUtil.runAsync(new Runnable() {
            @Override
            public void run() {
                setDataProperty("uuid" , uuid.toString());
                setDataProperty("coins" , coins);
                setDataProperty("bits" , bits);
                setDataProperty("bankCoins" , bankCoins);
                setDataProperty("rank" , rank.toString());
                setDataProperty("islandX", islandX);
                setDataProperty("islandZ", islandZ);
                Map<String, Integer> collectionsData = new HashMap<>();
                for (ItemCollection collection : ItemCollection.getCollections()) {
                    collectionsData.put(collection.getIdentifier(), getCollection(collection));
                }
                setDataProperty("collections", collectionsData);
                if (lastRegion != null){
                    setDataProperty("lastRegion", getLastRegion().getName());
                }
                Map<String, Integer> quiverData = new HashMap<>();
                getQuiver().forEach((key, value) -> quiverData.put(key.name(), value));
                setDataProperty("quiver", quiverData);
                List<Document> effects = new ArrayList<>();
                for (ActivePotionEffect effect : getEffects()) {
                    Document effectDocument = new Document()
                            .append("key", effect.getEffect().getType().getNamespace())
                            .append("level", effect.getEffect().getLevel())
                            .append("duration", effect.getEffect().getDuration())
                            .append("remaining", effect.getRemaining());
                    effects.add(effectDocument);
                }
                setDataProperty("totalfloor6run" , totalfloor6run);
                setDataProperty("sadanCollections" , sadancollections);
                setDataProperty("effects", effects);
                setDataProperty("skillFarmingXp", farmingXP);
                setDataProperty("skillMiningXp", miningXP);
                setDataProperty("skillCombatXp", combatXP);
                setDataProperty("skillForagingXp", foragingXP);
                setDataProperty("skillEnchantXp" , enchantXP);
                setDataProperty("slayerRevenantHorrorHighest", highestSlayers[0]);
                setDataProperty("slayerTarantulaBroodfatherHighest", highestSlayers[1]);
                setDataProperty("slayerSvenPackmasterHighest", highestSlayers[2]);
                setDataProperty("slayerVoidgloomSeraphHighest", highestSlayers[3]);
                setDataProperty("permanentCoins", isPermanentCoins());
                setDataProperty("xpSlayerRevenantHorror", slayerXP[0]);
                setDataProperty("xpSlayerTarantulaBroodfather", slayerXP[1]);
                setDataProperty("xpSlayerSvenPackmaster", slayerXP[2]);
                setDataProperty("xpSlayerVoidgloomSeraph", slayerXP[3]);

                setDataProperty("completedQuests" , completedQuests);
                setDataProperty("completedObjectives" , completedObjectives);

                if (slayerQuest != null){
                    setDataProperty("slayerQuest", getSlayerQuest().serialize());
                }
                if (!pets.isEmpty()) {
                    List<Map<String, Object>> petsSerialized = pets.stream().map(Pet.PetItem::serialize).collect(Collectors.toList());
                    setDataProperty("pets", petsSerialized);
                } else {
                    setDataProperty("pets", new ArrayList<Map<String, Object>>());
                }
                if (auctionSettings != null){
                    setDataProperty("auctionSettings", auctionSettings.serialize());
                }
                setDataProperty("auctionCreationBIN", isAuctionCreationBIN());

                if (auctionEscrow != null){
                    setDataProperty("auctionEscrow" , auctionEscrow.serialize());
                }

                setDataProperty("unlockedRecipes" , unlockedRecipes);
                setDataProperty("talkedNPCs" , talkedNPCs);

                setDataProperty("foundZone" , foundzone);


                if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()) {
                    setDataProperty("showPets", PetsGUI.getShowPet(Bukkit.getPlayer(uuid)));
                    setDataProperty("autoSlayer", PlayerUtils.isAutoSlayer(Bukkit.getPlayer(uuid)));
                    if (PlayerUtils.COOKIE_DURATION_CACHE.containsKey(uuid)) {
                        setDataProperty("cookieDuration", PlayerUtils.getCookieDurationTicks(Bukkit.getPlayer(uuid)));
                    }

                }

                Document query = new Document("_id", uuid.toString());

                UserDatabase.collectionFuture.thenApply(userCollection -> {
                    Document found = userCollection.find(query).first();

                    if (found != null) {
                        Document updated = new Document(found);
                        dataCache.forEach(updated::append);
                        userCollection.replaceOne(found, updated);
                    } else {
                        Document newDocument = new Document("_id", uuid.toString());
                        dataCache.forEach(newDocument::append);
                        userCollection.insertOne(newDocument);
                    }
                    future.complete(null);
                   return future;
                });
            }
        });

        return future;
    }




    public void load() {

        SUtil.runAsync(new Runnable() {
            @Override
            public void run() {
                loadDocument();
                uuid = UUID.fromString(getString("uuid", null));
                coins = getLong("coins", 0);
                bits = getLong("bits", 0);
                bankCoins = getLong("bankCoins", 0);
                Map<String, Integer> coll = (Map<String, Integer>) get("collections", new HashMap<>());
                coll.forEach((key, value) -> {
                    collections.put(ItemCollection.getByIdentifier(key), value);
                });
                setRank(PlayerRank.valueOf(getString("rank", "DEFAULT")));
                islandX = getDouble("islandX", 0);
                islandZ = getDouble("islandZ", 0);
                if (getString("lastRegion", "none").equals("none")) {
                    setLastRegion(null);
                } else {
                    setLastRegion(Region.get(getString("lastRegion", "none")));
                }

                totalfloor6run = getLong("totalfloor6run", 0);
                sadancollections = getLong("sadanCollections", 0);

                farmingXP = getDouble("skillFarmingXp", 0.0);
                miningXP = getDouble("skillMiningXp", 0.0);

                combatXP = getDouble("skillCombatXp", 0.0);
                foragingXP = getDouble("skillForagingXp", 0.0);

                enchantXP = getDouble("skillEnchantXp", 0.0);

                highestSlayers[0] = getInt("slayerRevenantHorrorHighest", 0);
                highestSlayers[1] = getInt("slayerTarantulaBroodfatherHighest", 0);

                highestSlayers[2] = getInt("slayerSvenPackmasterHighest", 0);
                highestSlayers[3] = getInt("slayerVoidgloomSeraphHighest", 0);

                slayerXP[0] = getInt("xpSlayerRevenantHorror", 0);
                slayerXP[1] = getInt("xpSlayerTarantulaBroodfather", 0);
                slayerXP[2] = getInt("xpSlayerSvenPackmaster", 0);
                slayerXP[3] = getInt("xpSlayerVoidgloomSeraph", 0);

                talkedNPCs = getStringList("talkedNPCs" , new ArrayList<>());
                unlockedRecipes = getStringList("unlockedRecipes" , new ArrayList<>());
                foundzone = getStringList("foundZone" , new ArrayList<>());

                setPermanentCoins(getBoolean("permanentCoins", false));

                Map<String, Integer> quiv = (Map<String, Integer>) get("quiver", new HashMap<>());
                quiv.forEach((key, value) -> {
                    quiver.put(SMaterial.getMaterial(key), value);
                });

                try {
                    SlayerQuest quest = SlayerQuest.deserialize((Map<String, Object>) get("slayerQuest", new HashMap<>()));
                    setSlayerQuest(quest);
                } catch (Exception ignored) {
                    setSlayerQuest(null);
                }

                List<Object> listOfPetObjects = new ArrayList<>((List<Object>) get("pets", new ArrayList<>()));
                listOfPetObjects.forEach((item) -> {
                    Pet.PetItem petitem = Pet.PetItem.deserialize((Map<String, Object>) item);
                    pets.add(petitem);
                });

                try {
                    auctionSettings = AuctionSettings.deserialize((Map<String, Object>) get("auctionSettings", new HashMap<>()));
                } catch (Exception ignored) {
                   auctionSettings = new AuctionSettings();
                }

                try {
                    setAuctionEscrow(AuctionEscrow.deserialize((Map<String, Object>) get("auctionEscrow", new HashMap<>())));
                } catch (Exception ignored) {
                    setAuctionEscrow(new AuctionEscrow());
                }

                completedQuests = getStringList("completedQuests" , new ArrayList<>());
                completedObjectives = getStringList("completedObjectives" , new ArrayList<>());


                setCompletedQuests(completedQuests);
                setCompletedObjectives(completedObjectives);


                setAuctionCreationBIN(getBoolean("auctionCreationBIN", false));

                List<Document> effectsDocuments = dataDocument.getList("effects", Document.class);
                for (Document effectData : effectsDocuments) {
                    String key = effectData.getString("key");
                    Integer level = effectData.getInteger("level");
                    Long duration = effectData.getLong("duration");
                    Long remaining = effectData.getLong("remaining");

                    if (key != null && level != null && duration != null && remaining != null) {
                        PotionEffectType potionEffectType = PotionEffectType.getByNamespace(key);
                        if (potionEffectType != null) {
                            getEffects().add(new ActivePotionEffect(
                                    new PotionEffect(potionEffectType, level, duration),
                                    remaining
                            ));
                        }
                    }
                }
                try {
                    PlayerUtils.setCookieDurationTicks(player, getLong("cookieDuration" , 0));
                } catch (NullPointerException ignored) {}
            }
        });

    }


    public void setDataProperty(String key, Object value)
    {
        dataCache.put(key, value);
    }

    public List<String> getStringList(String key, List<String> def) {
        Object value = get(key, def);
        if (value instanceof List<?>) {
            return new ArrayList<>((List<String>) value);
        } else {
            List<String> list = new ArrayList<>();
            list.add(value.toString());
            return list;
        }
    }

    public Object get(String key, Object def) {
        if (dataDocument != null && dataDocument.get(key) != null) {
            return dataDocument.get(key);
        }
        return def;
    }


    public String getString( String key, Object def) {
        return get(key, def).toString();
    }

    public int getInt(String key, Object def) {
        return (int) get(key, def);
    }

    public boolean getBoolean(String key, Object def) {
        return Boolean.parseBoolean(get(key, def).toString());
    }

    public double getDouble(String key, Object def) {
        return Double.parseDouble(getString( key, def));
    }

    public long getLong(String key, Object def) {
        if (def.equals(0.0)) {
            def = 0L;
        }
        return Long.parseLong(getString(key, def));
    }

    public void loadDocument() {
        Document query = new Document("_id", uuid.toString());

        MongoCollection<Document> collection = DatabaseManager.getCollection("users").join();

        Document found = collection.find(query).first();


        if (found == null) {
            save();
            found = collection.find(query).first();
        }

        dataDocument = found;
    }



    public void sendMessage(Object message) {
        if (message instanceof String) {
            this.player.sendMessage(message.toString().replace('&', ChatColor.COLOR_CHAR));
        } else if (message instanceof TextComponent) {
            player.spigot().sendMessage(new BaseComponent[]{(BaseComponent) message});
        }
    }

    public void sendMessages(Object... messages) {
        for (Object message : messages) {
            sendMessage(message);
        }
    }


    public boolean addBits(long value){
        if (value > 0){
            this.bits += value;
            return true;
        }
        return false;
    }
    public boolean subBits(long value){
        if (bits > value && value > 0){
            bits -= value;
            return true;
        }
        return false;
    }



    public void addCoins(final long coins) {
        this.coins += coins;
    }

    public void subCoins(final long coins) {
        this.coins -= coins;
    }

    public void addBankCoins(final long bankCoins) {
        this.bankCoins += bankCoins;
    }

    public void subBankCoins(final long bankCoins) {
        this.bankCoins -= bankCoins;
    }

    public void addBCollection(final int a) {
        this.sadancollections += a;
    }

    public void setBCollection(final int a) {
        this.sadancollections = a;
    }

    public void subBCollection(final int a) {
        this.sadancollections -= a;
    }

    public long getBCollection() {
        return this.sadancollections;
    }

    public void addBRun6(final int a) {
        this.totalfloor6run += a;
    }

    public void subBRun6(final int a) {
        this.totalfloor6run -= a;
    }

    public long getBRun6() {
        return this.totalfloor6run;
    }

    public void addToCollection(final ItemCollection collection, final int amount) {
        final int prevTier = collection.getTier(this.getCollection(collection));
        final int i = this.collections.getOrDefault(collection, 0);
        this.collections.put(collection, i + amount);
        this.updateCollection(collection, prevTier);
    }

    public void addToCollection(final ItemCollection collection) {
        this.addToCollection(collection, 1);
    }

    public void setCollection(final ItemCollection collection, final int amount) {
        final int prevTier = collection.getTier(this.getCollection(collection));
        this.collections.put(collection, amount);
        this.updateCollection(collection, prevTier);
    }

    public void zeroCollection(final ItemCollection collection) {
        final int prevTier = collection.getTier(this.getCollection(collection));
        this.collections.put(collection, 0);
        this.updateCollection(collection, prevTier);
    }

    private void updateCollection(final ItemCollection collection, final int prevTier) {
        final int tier = collection.getTier(this.getCollection(collection));
        if (prevTier != tier) {
            final Player player = Bukkit.getPlayer(this.uuid);
            if (player != null) {
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
            }
            final StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------\n");
            builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append("  COLLECTION LEVEL UP ").append(ChatColor.RESET).append(ChatColor.YELLOW).append(collection.getName()).append(" ");
            if (prevTier != 0) {
                builder.append(ChatColor.DARK_GRAY).append(SUtil.toRomanNumeral(prevTier)).append("➜");
            }
            builder.append(ChatColor.YELLOW).append(SUtil.toRomanNumeral(tier)).append("\n");
            final ItemCollectionRewards rewards = collection.getRewardsFor(tier);
            if (rewards != null && rewards.size() != 0) {
                builder.append(" \n");
                builder.append(ChatColor.GREEN).append(ChatColor.BOLD).append("  REWARD");
                if (rewards.size() != 1) {
                    builder.append("S");
                }
                builder.append(ChatColor.RESET);
                for (final ItemCollectionReward reward : rewards) {
                    reward.onAchieve(player);
                    builder.append("\n    ").append(reward.toRewardString());
                }
            }
            builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("------------------------------------------");
            this.send(builder.toString());
        }
    }

    public int getCollection(final ItemCollection collection) {
        return this.collections.get(collection);
    }

    public boolean hasCollection(final ItemCollection collection, final int tier) {
        return collection.getTier(this.getCollection(collection)) >= tier;
    }

    public void addToQuiver(final SMaterial material, final int amount) {
        final int i = this.quiver.getOrDefault(material, 0);
        this.setQuiver(material, i + amount);
    }

    public void addToQuiver(final SMaterial material) {
        this.addToQuiver(material, 1);
    }

    public void setQuiver(final SMaterial material, final int amount) {
        if (amount == 0) {
            this.quiver.remove(material);
            return;
        }
        this.quiver.put(material, amount);
    }

    public int getQuiver(final SMaterial material) {
        return this.quiver.get(material);
    }

    public void subFromQuiver(final SMaterial material, final int amount) {
        if (!this.quiver.containsKey(material)) {
            return;
        }
        this.setQuiver(material, this.quiver.get(material) - amount);
    }

    public void addtoQuiver(final SMaterial material, final int amount) {
        if (!this.quiver.containsKey(material)) {
            return;
        }
        this.setQuiver(material, this.quiver.get(material) + amount);
    }

    public void subFromQuiver(final SMaterial material) {
        this.subFromQuiver(material, 1);
    }

    public void addtoQuiver(final SMaterial material) {
        this.addtoQuiver(material, 1);
    }

    public boolean hasQuiverItem(final SMaterial material) {
        return this.quiver.containsKey(material);
    }

    public void clearQuiver() {
        this.quiver.clear();
    }

    public void addPet(final SItem item) {
        this.pets.add(new Pet.PetItem(item.getType(), item.getRarity(), item.getData().getDouble("xp")));
    }

    public void equipPet(final Pet.PetItem pet) {
        for (final Pet.PetItem p : this.pets) {
            if (p.isActive()) {
                p.setActive(false);
                break;
            }
        }
        pet.setActive(true);
    }

    public void removePet(final Pet.PetItem pet) {
        final Iterator<Pet.PetItem> iter = this.pets.iterator();
        while (iter.hasNext()) {
            final Pet.PetItem p = iter.next();
            if (pet.equals(p)) {
                iter.remove();
                break;
            }
        }
    }

    public Pet.PetItem getActivePet() {
        for (final Pet.PetItem pet : this.pets) {
            if (pet.isActive()) {
                return pet;
            }
        }
        return null;
    }

    public Pet getActivePetClass() {
        final Pet.PetItem item = this.getActivePet();
        if (item == null) {
            return null;
        }
        return (Pet) item.getType().getGenericInstance();
    }

    public double getSkillXP(final Skill skill) {
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

    public void setSkillXP(final Skill skill, final double xp) {
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

    public void addSkillXP(final Skill skill, final double xp) {
        this.setSkillXP(skill, this.getSkillXP(skill) + xp);
    }

    public int getHighestRevenantHorror() {
        return this.highestSlayers[0];
    }

    public void setHighestRevenantHorror(final int tier) {
        this.highestSlayers[0] = tier;
    }

    public int getHighestTarantulaBroodfather() {
        return this.highestSlayers[1];
    }

    public void setHighestTarantulaBroodfather(final int tier) {
        this.highestSlayers[1] = tier;
    }

    public int getHighestSvenPackmaster() {
        return this.highestSlayers[2];
    }

    public void setHighestSvenPackmaster(final int tier) {
        this.highestSlayers[2] = tier;
    }

    public int getHighestVoidgloomSeraph() {
        return this.highestSlayers[3];
    }

    public void setHighestVoidgloomSeraph(final int tier) {
        this.highestSlayers[3] = tier;
    }

    public int getZombieSlayerXP() {
        return this.slayerXP[0];
    }

    public void setZombieSlayerXP(final int xp) {
        this.slayerXP[0] = xp;
    }

    public int getSpiderSlayerXP() {
        return this.slayerXP[1];
    }

    public void setSpiderSlayerXP(final int xp) {
        this.slayerXP[1] = xp;
    }

    public int getWolfSlayerXP() {
        return this.slayerXP[2];
    }

    public void setWolfSlayerXP(final int xp) {
        this.slayerXP[2] = xp;
    }

    public int getEndermanSlayerXP() {
        return this.slayerXP[3];
    }

    public void setEndermanSlayerXP(final int xp) {
        this.slayerXP[3] = xp;
    }

    public void setSlayerXP(final SlayerBossType.SlayerMobType type, final int xp) {
        this.slayerXP[type.ordinal()] = xp;
    }

    public int getSlayerXP(final SlayerBossType.SlayerMobType type) {
        return this.slayerXP[type.ordinal()];
    }

    public int getCrystalLVL(final int i) {
        if (i > 7) {
            SLog.severe("Out of bound on action taking data from database!");
            return 0;
        }
        return this.crystalLVL[i];
    }

    public void setCrystalLVL(final int i, final int a) {
        if (i > 7) {
            SLog.severe("Out of bound on action taking data from database!");
            return;
        }
        this.crystalLVL[i] = a;
    }

    public int getSlayerCombatXPBuff() {
        int buff = 0;
        for (final int highest : this.highestSlayers) {
            buff += ((highest == 4) ? 5 : highest);
        }
        return buff;
    }

    public void startSlayerQuest(final SlayerBossType type) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        this.slayerQuest = new SlayerQuest(type, System.currentTimeMillis());
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 2.0f);
        player.sendMessage("  " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "SLAYER QUEST STARTED!");
        player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "» " + ChatColor.GRAY + "Slay " + ChatColor.RED + SUtil.commaify(type.getSpawnXP()) + " Combat XP" + ChatColor.GRAY + " worth of " + type.getType().getPluralName() + ".");
    }

    public void failSlayerQuest() {
        if (this.slayerQuest == null) {
            return;
        }
        if (this.slayerQuest.getDied() != 0L) {
            return;
        }
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        this.slayerQuest.setDied(System.currentTimeMillis());
        if (this.slayerQuest.getEntity() != null) {
            this.slayerQuest.getEntity().remove();
            this.slayerQuest.getEntity().getFunction().onDeath(this.slayerQuest.getEntity(), this.slayerQuest.getEntity().getEntity(), player);
        }
        SUtil.delay(() -> {
            this.removeAllSlayerBosses();
            player.sendMessage("  " + ChatColor.RED + ChatColor.BOLD + "SLAYER QUEST FAILED!");
            player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "» " + ChatColor.GRAY + "You need to learn how to play this game first!");
        }, 2L);
    }

    public void removeAllSlayerBosses() {
        for (final Entity e : Bukkit.getPlayer(this.uuid).getWorld().getEntities()) {
            if (e.hasMetadata("BOSS_OWNER_" + this.uuid.toString()) && e.hasMetadata("SlayerBoss")) {
                e.remove();
            }
        }
    }

    public void send(final String message) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        player.sendMessage(Sputnik.trans(message));
    }



    public void damageEntity(final Damageable entity1, final double damageBase) {
        if (entity1.isDead()) {
            return;
        }
        final Player player = Bukkit.getPlayer(this.uuid);
        double damage = damageBase;
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put(entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity1)) {
                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity1);
                if (defensepercent > 100) {
                    defensepercent = 100;
                }
                damage -= damage * defensepercent / 100.0;
            }
            PlayerUtils.handleSpecEntity(entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            final double formula = damage / entity1.getMaxHealth() * 100.0;
            if (formula < 10.0) {
                damage *= 1.0;
            } else if (formula > 10.0 && formula < 15.0) {
                damage -= damage * 90.0 / 100.0;
            } else if (formula > 15.0 && formula < 20.0) {
                damage -= damage * 99.0 / 100.0;
            } else if (formula > 20.0 && formula <= 25.0) {
                damage -= damage * 99.9 / 100.0;
            } else if (formula > 25.0) {
                damage *= 0.0;
            } else {
                damage *= 1.0;
            }
            PlayerUtils.handleSpecEntity(entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        final double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        entity1.damage(1.0E-5);
        if (player.getItemInHand() != null) {
            final SItem sitem = SItem.find(player.getItemInHand());
            if (sitem != null && sitem.getEnchantment(EnchantmentType.VAMPIRISM) != null) {
                double lvl = sitem.getEnchantment(EnchantmentType.VAMPIRISM).getLevel();
                if (lvl > 100.0) {
                    lvl = 100.0;
                }
                final double aB = player.getHealth() + lvl / 100.0 * (player.getMaxHealth() - player.getHealth());
                final double aC = Math.min(player.getMaxHealth(), aB);
                player.setHealth(aC);
            }
        }
    }

    public void damageEntityIgnoreShield(final Damageable entity1, final double damageBase) {
        if (entity1.isDead()) {
            return;
        }
        final Player player = Bukkit.getPlayer(this.uuid);
        double damage = damageBase;
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put(entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            PlayerUtils.handleSpecEntity(entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            final double formula = damage / entity1.getMaxHealth() * 100.0;
            if (formula < 10.0) {
                damage *= 1.0;
            } else if (formula > 10.0 && formula < 15.0) {
                damage -= damage * 90.0 / 100.0;
            } else if (formula > 15.0 && formula < 20.0) {
                damage -= damage * 99.0 / 100.0;
            } else if (formula > 20.0 && formula <= 25.0) {
                damage -= damage * 99.9 / 100.0;
            } else if (formula > 25.0) {
                damage *= 0.0;
            } else {
                damage *= 1.0;
            }
            PlayerUtils.handleSpecEntity(entity1, player, new AtomicDouble(damage));
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        final double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        entity1.damage(1.0E-5);
        if (player.getItemInHand() != null) {
            final SItem sitem = SItem.find(player.getItemInHand());
            if (sitem != null && sitem.getEnchantment(EnchantmentType.VAMPIRISM) != null) {
                double lvl = sitem.getEnchantment(EnchantmentType.VAMPIRISM).getLevel();
                if (lvl > 100.0) {
                    lvl = 100.0;
                }
                final double aB = player.getHealth() + lvl / 100.0 * (player.getMaxHealth() - player.getHealth());
                final double aC = Math.min(player.getMaxHealth(), aB);
                player.setHealth(aC);
            }
        }
    }

    public void damageEntityBowEman(final Damageable entity1, double damage, final Player player, final Arrow a) {
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(entity1)) {
            VoidgloomSeraph.HIT_SHIELD.put(entity1, VoidgloomSeraph.HIT_SHIELD.get(entity1) - 1);
            entity1.getWorld().playSound(entity1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        final double health = entity1.getMaxHealth();
        if (player == null) {
            return;
        }
        if (entity1.getType() != EntityType.ENDER_DRAGON) {
            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity1)) {
                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity1);
                if (defensepercent > 100) {
                    defensepercent = 100;
                }
                damage -= damage * defensepercent / 100.0;
            }
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        } else {
            final double formula = damage / entity1.getMaxHealth() * 100.0;
            if (formula < 10.0) {
                damage *= 1.0;
            } else if (formula > 10.0 && formula < 15.0) {
                damage -= damage * 90.0 / 100.0;
            } else if (formula > 15.0 && formula < 20.0) {
                damage -= damage * 99.0 / 100.0;
            } else if (formula > 20.0 && formula <= 25.0) {
                damage -= damage * 99.9 / 100.0;
            } else if (formula > 25.0) {
                damage *= 0.0;
            } else {
                damage *= 1.0;
            }
            entity1.setHealth(Math.max(0.0, entity1.getHealth() - damage));
        }
        PlayerUtils.handleSpecEntity(entity1, player, new AtomicDouble(damage));
        a.remove();
    }

    public void damageEntity(final LivingEntity entity) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        entity.damage(0.0, player);
    }

    public void damage(double d, final EntityDamageEvent.DamageCause cause, final Entity entity) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        final EntityHuman human = ((CraftHumanEntity) player).getHandle();
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        final double trueDefense = statistics.getTrueDefense().addAll();
        final double health = statistics.getMaxHealth().addAll();
        d -= d * (trueDefense / (trueDefense + 100.0));
        if (player.getHealth() + SputnikPlayer.getCustomAbsorptionHP(player) - d <= 0.0) {
            this.kill(cause, entity);
            return;
        }
        final float ab = (float) Math.max(0.0, SputnikPlayer.getCustomAbsorptionHP(player) - d);
        double actual = Math.max(0.0, d - SputnikPlayer.getCustomAbsorptionHP(player));
        SputnikPlayer.setCustomAbsorptionHP(player, ab);
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK || cause == EntityDamageEvent.DamageCause.LAVA) {
            final int damage = 0;
            if (cause == EntityDamageEvent.DamageCause.FIRE || (cause == EntityDamageEvent.DamageCause.FIRE_TICK && cause != EntityDamageEvent.DamageCause.LAVA)) {
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
        if ((cause == EntityDamageEvent.DamageCause.WITHER || cause == EntityDamageEvent.DamageCause.POISON)) {
            actual = 20.0;
        }
        player.setHealth(Math.max(0.0, player.getHealth() - actual));
        if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK || cause == EntityDamageEvent.DamageCause.LAVA) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.GOLD);
        } else if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.CONTACT || cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.GRAY);
        } else if (cause == EntityDamageEvent.DamageCause.DROWNING) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.DARK_AQUA);
        } else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.RED);
        } else if (cause == EntityDamageEvent.DamageCause.POISON) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.DARK_GREEN);
        } else if (cause == EntityDamageEvent.DamageCause.WITHER) {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.BLACK);
        } else {
            PlayerListener.spawnSpecialDamageInd(player, actual, ChatColor.WHITE);
        }
        if (player.getHealth() <= 0.0) {
            player.setFireTicks(0);
            this.kill(cause, entity);
        }
    }

    public void damage(final double d) {
        this.damage(d, EntityDamageEvent.DamageCause.CUSTOM, null);
    }

    public void kill(final EntityDamageEvent.DamageCause cause, final Entity entity) {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        player.setHealth(player.getMaxHealth());
        for (int i = 0; i < player.getInventory().getSize(); ++i) {
            final ItemStack stack = player.getInventory().getItem(i);
            final SItem sItem = SItem.find(stack);
            if (sItem == null) {
            }
        }
        player.setFireTicks(0);
        player.setVelocity(new Vector(0, 0, 0));
        player.setFallDistance(0.0f);
        SputnikPlayer.AbsHP.put(player, 0);
        if (!player.getWorld().getName().contains("f6")) {
            this.sendToSpawn();
            if (!PlayerUtils.cookieBuffActive(player)) {
                this.clearPotionEffects();
            }
        } else {
            final World w = player.getWorld();
            for (final Entity en : player.getWorld().getEntities()) {
                if (en instanceof HumanEntity) {
                    continue;
                }
                en.remove();
            }
            SadanFunction.sendReMsg(false, w);
            SadanHuman.IsMusicPlaying.put(w.getUID(), false);
            SadanHuman.BossRun.put(w.getUID(), false);
            SadanFunction.endRoom2(w);
            final BukkitTask bkt = SadanHuman.playHBS(w);
            new BukkitRunnable() {
                public void run() {
                    if (w == null || w.getPlayers().size() == 0) {
                        bkt.cancel();
                        this.cancel();
                    }
                }
            }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
            SUtil.sendTitle(player, ChatColor.YELLOW + "You became a ghost!");
            SUtil.sendSubtitle(player, ChatColor.GRAY + "Hopefully your teammates can revive you.");
            if (cause == EntityDamageEvent.DamageCause.VOID) {
                this.sendToSpawn();
            }
        }
        String name = null;
        if (entity != null) {
            final SEntity sEntity = SEntity.findSEntity(entity);
            name = ((sEntity != null) ? sEntity.getStatistics().getEntityName() : entity.getCustomName());
        }
        String message = "You died";
        String out = "%s died";
        switch (cause) {
            case VOID:
                message = "You fell into the void";
                out = "%s fell into the void";
                break;
            case FALL:
                message = "You fell to your death";
                out = "%s fell to their death";
                break;
            case ENTITY_ATTACK:
                message = "You were killed by " + name + ChatColor.GRAY;
                out = "%s was killed by " + name + ChatColor.GRAY;
                break;
            case ENTITY_EXPLOSION:
                message = "You were killed by " + name + ChatColor.GRAY + "'s explosion";
                out = "%s was killed by " + name + ChatColor.GRAY + "'s explosion";
                break;
            case FIRE:
            case FIRE_TICK:
            case LAVA:
                message = "You burned to death";
                out = "%s burned to death";
                break;
            case MAGIC:
                message = "You died by magic";
                out = "%s was killed by magic";
                break;
            case POISON:
                message = "You died by poisoning";
                out = "%s was killed by poisoning";
                break;
            case LIGHTNING:
                message = "You were struck by lightning and died";
                out = "%s was struck by lightning and killed";
                break;
            case DROWNING:
                message = "You drowned";
                out = "%s drowned";
                break;
            case SUFFOCATION:
                message = "You suffocated";
                out = "%s suffocated";
                break;
        }
        if (this.slayerQuest != null && this.slayerQuest.getKilled() == 0L) {
            final User user = getUser(player.getUniqueId());
            final SlayerQuest quest = user.getSlayerQuest();
            if (quest.getXp() >= quest.getType().getSpawnXP()) {
                this.failSlayerQuest();
            }
        }
        player.playSound(player.getLocation(), Sound.HURT_FLESH, 1.0f, 1.0f);
        if (!player.getWorld().getName().equalsIgnoreCase("limbo") && !player.getWorld().getName().contains("f6")) {
            player.sendMessage(ChatColor.RED + " ☠ " + ChatColor.GRAY + message + ChatColor.GRAY + ".");
            SUtil.broadcastExcept(ChatColor.RED + " ☠ " + ChatColor.GRAY + String.format(out, player.getName()) + ChatColor.GRAY + ".", player);
        }
        if (player.getWorld().getName().contains("f6")) {
            player.playSound(player.getLocation(), Sound.HURT_FLESH, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + " ☠ " + ChatColor.GRAY + message + ChatColor.GRAY + " and became a ghost.");
            SUtil.broadcastExcept(ChatColor.RED + " ☠ " + ChatColor.GRAY + String.format(out, player.getName()) + ChatColor.GRAY + " and became a ghost.", player);
        }
        for (final Entity e : player.getWorld().getEntities()) {
            if (e.hasMetadata("owner") && e.getMetadata("owner").get(0).asString().equals(player.getUniqueId().toString())) {
                e.remove();
                player.sendMessage(ChatColor.RED + "☠ Your Voidling's Warden Boss has been despawned since you died!");
            }
        }
        if (PlayerUtils.cookieBuffActive(player)) {
            player.sendMessage(ChatColor.RED + "You died!");
            return;
        }
        if ((this.isOnIsland() && cause == EntityDamageEvent.DamageCause.VOID) || this.permanentCoins || player.getWorld().getName().equalsIgnoreCase("limbo") || player.getWorld().getName().contains("f6")) {
            return;
        }
        final int piggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.PIGGY_BANK);
        if (piggyIndex != -1 && this.coins >= 20000L) {
            final SItem cracked = SItem.of(SMaterial.CRACKED_PIGGY_BANK);
            final SItem piggy = SItem.find(player.getInventory().getItem(piggyIndex));
            if (piggy.getReforge() != null) {
                cracked.setReforge(piggy.getReforge());
            }
            player.getInventory().setItem(piggyIndex, cracked.getStack());
            player.sendMessage(ChatColor.RED + "You died and your piggy bank cracked!");
            return;
        }
        player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1.0f, 2.0f);
        final int crackedPiggyIndex = PlayerUtils.getSpecItemIndex(player, SMaterial.CRACKED_PIGGY_BANK);
        if (crackedPiggyIndex != -1 && this.coins >= 20000L) {
            final SItem broken = SItem.of(SMaterial.BROKEN_PIGGY_BANK);
            final SItem crackedPiggy = SItem.find(player.getInventory().getItem(crackedPiggyIndex));
            if (crackedPiggy.getReforge() != null) {
                broken.setReforge(crackedPiggy.getReforge());
            }
            player.getInventory().setItem(crackedPiggyIndex, broken.getStack());
            final long sub = (long) (this.coins * 0.25);
            player.sendMessage(ChatColor.RED + "You died, lost " + SUtil.commaify(sub) + " coins, and your piggy bank broke!");
            this.coins -= sub;
            this.save();
            return;
        }
        final long sub2 = this.coins / 2L;
        player.sendMessage(ChatColor.RED + "You died and lost " + SUtil.commaify(sub2) + " coins!");
        this.coins -= sub2;
        this.save();
    }

    public void setIslandLocation(final double x, final double z) {
        this.islandX = x;
        this.islandZ = z;
    }

    public void addPotionEffect(final PotionEffect effect) {
        this.effects.add(new ActivePotionEffect(effect, effect.getDuration()));
    }

    public void removePotionEffect(final PotionEffectType type) {
        for (final ActivePotionEffect effect : this.effects) {
            if (effect.getEffect().getType() == type) {
                effect.setRemaining(0L);
            }
        }
    }

    public ActivePotionEffect getPotionEffect(final PotionEffectType type) {
        for (final ActivePotionEffect effect : this.effects) {
            if (effect.getEffect().getType() == type) {
                return effect;
            }
        }
        return null;
    }

    public boolean hasPotionEffect(final PotionEffectType type) {
        return this.effects.stream().filter(effect -> effect.getEffect().getType() == type).toArray().length != 0;
    }

    public void clearPotionEffects() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            for (final org.bukkit.potion.PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        }
        for (final ActivePotionEffect effect2 : this.effects) {
            effect2.setRemaining(0L);
        }
    }

    public boolean isOnIsland() {
        final Player player = Bukkit.getPlayer(this.uuid);
        return player != null && this.isOnIsland(player.getLocation());
    }

    public boolean isOnIsland(final Block block) {
        return this.isOnIsland(block.getLocation());
    }

    public boolean isOnIsland(final Location location) {
        final World world = Bukkit.getWorld("islands");
        if (world == null) {
            return false;
        }
        final double x = location.getX();
        final double z = location.getZ();
        return world.getUID().equals(location.getWorld().getUID()) && x >= this.islandX - 125.0 && x <= this.islandX + 125.0 && z >= this.islandZ - 125.0 && z <= this.islandZ + 125.0;
    }

    public boolean isOnUserIsland() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return false;
        }
        final World world = Bukkit.getWorld("islands");
        if (world == null) {
            return false;
        }
        final double x = player.getLocation().getX();
        final double z = player.getLocation().getZ();
        return world.getUID().equals(player.getWorld().getUID()) && x < this.islandX - 125.0 && x > this.islandX + 125.0 && z < this.islandZ - 125.0 && z > this.islandZ + 125.0;
    }


    public List<AuctionItem> getBids() {
        return AuctionItem.getAuctions().stream().filter(item -> {
            for (AuctionBid bid : item.getBids()) {
                if (bid.getBidder().equals(this.uuid) && item.getParticipants().contains(this.uuid)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<AuctionItem> getAuctions() {
        return AuctionItem.getAuctions().stream().filter(item -> item.getOwner().getUuid().equals(this.uuid) && item.getParticipants().contains(this.uuid)).collect(Collectors.toList());
    }

    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public EntityPlayer toNMSPlayer() {
        return ((CraftPlayer) Bukkit.getPlayer(this.uuid)).getHandle();
    }

    public void sendToSpawn() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return;
        }
        if (this.isOnIsland()) {
            PlayerUtils.sendToIsland(player);
        } else if (this.lastRegion != null) {
            switch (this.lastRegion.getType()) {
                case BANK:
                    player.teleport(player.getWorld().getSpawnLocation());
                case FARM:
                case RUINS:
                    player.teleport(player.getWorld().getSpawnLocation());
                case FOREST:
                case LIBRARY:
                case COAL_MINE:
                case COAL_MINE_CAVES:
                case MOUNTAIN:
                case VILLAGE:
                    player.teleport(player.getWorld().getSpawnLocation());
                case HIGH_LEVEL:
                    player.teleport(player.getWorld().getSpawnLocation());
                case BLACKSMITH:
                    player.teleport(player.getWorld().getSpawnLocation());
                case AUCTION_HOUSE:
                    player.teleport(player.getWorld().getSpawnLocation());
                case WILDERNESS:
                case BAZAAR_ALLEY:
                case COLOSSEUM:
                case GRAVEYARD:
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
                case SPIDERS_DEN:
                    player.teleport(player.getWorld().getSpawnLocation());
                case SPIDERS_DEN_HIVE:
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
                default:
                    player.teleport(player.getWorld().getSpawnLocation());
                    break;
            }
        } else {
            player.teleport(player.getWorld().getSpawnLocation());
        }
    }

    public static String generateRandom() {
        final int leftLimit = 97;
        final int rightLimit = 122;
        final int targetStringLength = SUtil.random(7, 7);
        final Random random = new Random();
        final String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return generatedString;
    }


    public static User getUser(final UUID uuid) {
        if (uuid == null) {
            return null;
        }
        if (User.USER_CACHE.containsKey(uuid)) {
            return User.USER_CACHE.get(uuid);
        }
        return new User(uuid);
    }

    public static User getUser(Player player){
        if (player == null) return null;
        return getUser(player.getUniqueId());
    }

    public void sendPacket(Packet<?> packet){
        toNMSPlayer().playerConnection.sendPacket(packet);
    }

    public static Collection<User> getCachedUsers() {
        return User.USER_CACHE.values();
    }

    public boolean hasPermission(final String permission) {
        return true;
    }

    public static Map<UUID, User> getHash() {
        return User.USER_CACHE;
    }

    public ItemStack updateItemBoost(final SItem sitem) {
        if (sitem.getDataBoolean("dungeons_item") && sitem.getType().getStatistics().getType() != GenericItemType.ITEM && sitem.getType().getStatistics().getType() != GenericItemType.PET && sitem.getType().getStatistics().getType() != GenericItemType.BLOCK && sitem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
            final int itemstar = sitem.getDataInt("itemStar");
            double hpbboostweapons = 0.0;
            double hpbboosthp = 0.0;
            double hpbboostdef = 0.0;
            final PlayerBoostStatistics hs = sitem.getType().getBoostStatistics();
            final ItemSerial is = ItemSerial.getItemBoostStatistics(sitem);
            final Reforge reforge = (sitem.getReforge() == null) ? Reforge.blank() : sitem.getReforge();
            double bonusEn = 0.0;
            if (sitem.getType().getStatistics().getType() == GenericItemType.WEAPON && sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null) {
                final Enchantment e = sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL);
                bonusEn = hs.getBaseDamage() * (e.getLevel() * 210) / 100;
            }
            if (sitem.getType().getStatistics().getType() == GenericItemType.WEAPON || sitem.getType().getStatistics().getType() == GenericItemType.RANGED_WEAPON) {
                hpbboostweapons = sitem.getDataInt("hpb") * 2;
            } else if (sitem.getType().getStatistics().getType() == GenericItemType.ARMOR) {
                hpbboosthp = sitem.getDataInt("hpb") * 4;
                hpbboostdef = sitem.getDataInt("hpb") * 2;
            }
            is.setDamage(this.getFinal(hs.getBaseDamage() + hpbboostweapons + bonusEn, itemstar));
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
                for (final Enchantment enchantment : sitem.getEnchantments()) {
                    if (enchantment.getType() == EnchantmentType.GROWTH) {
                        health += 15.0 * enchantment.getLevel();
                    }
                    if (enchantment.getType() == EnchantmentType.PROTECTION) {
                        defense += 3.0 * enchantment.getLevel();
                    }
                }
            }
            is.setHealth(this.getFinal(health + hpbboosthp, itemstar));
            is.setDefense(this.getFinal(defense + hpbboostdef, itemstar));
            is.saveTo(sitem);
            return sitem.getStack();
        }
        return sitem.getStack();
    }

    public double getFinal(final double stat, final int starNum) {
        final int cataLVL = Skill.getLevel(this.getCataXP(), false);
        final int cataBuffPercentage = cataLVL * 5;
        int percentMstars = (starNum - 5) * 5;
        if (starNum <= 5) {
            percentMstars *= 0;
        }
        final double d = 1.0 + percentMstars / 100.0;
        return stat * ((1 + percentMstars / 100) * (1.0 + 0.1 * Math.min(5, starNum)) * (1 + cataBuffPercentage / 100) * d);
    }

    public void sendClickableMessage(final String message, final TextComponent[] hover, final String commandToRun) {
        final TextComponent tcp = new TextComponent(Sputnik.trans(message));
        if (hover != null) {
            tcp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover));
        }
        if (commandToRun != null) {
            tcp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandToRun));
        }
        this.toBukkitPlayer().spigot().sendMessage(tcp);
    }


    public boolean isCompletedSign() {
        return this.isCompletedSign;
    }

    public void setCompletedSign(final boolean isCompletedSign) {
        this.isCompletedSign = isCompletedSign;
    }

    static {
        USER_CACHE = new HashMap<UUID, User>();
        plugin = SkyBlock.getPlugin();
    }
}
