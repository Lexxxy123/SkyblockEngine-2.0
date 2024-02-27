package in.godspunky.skyblock.user;

import com.google.common.util.concurrent.AtomicDouble;
import de.tr7zw.nbtapi.NBTItem;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.api.serializer.BukkitSerializeClass;
import in.godspunky.skyblock.features.auction.AuctionBid;
import in.godspunky.skyblock.features.auction.AuctionEscrow;
import in.godspunky.skyblock.features.auction.AuctionItem;
import in.godspunky.skyblock.features.collection.ItemCollection;
import in.godspunky.skyblock.features.collection.ItemCollectionReward;
import in.godspunky.skyblock.features.collection.ItemCollectionRewards;
import in.godspunky.skyblock.config.Config;
import in.godspunky.skyblock.entity.dimoon.Dimoon;
import in.godspunky.skyblock.features.dungeons.stats.ItemSerial;
import in.godspunky.skyblock.features.enchantment.Enchantment;
import in.godspunky.skyblock.features.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanFunction;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import in.godspunky.skyblock.entity.nms.VoidgloomSeraph;
import in.godspunky.skyblock.features.island.SkyblockIsland;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.PlayerBoostStatistics;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.features.potion.ActivePotionEffect;
import in.godspunky.skyblock.features.potion.PotionEffect;
import in.godspunky.skyblock.features.potion.PotionEffectType;
import in.godspunky.skyblock.features.reforge.Reforge;
import in.godspunky.skyblock.features.reforge.ReforgeType;
import in.godspunky.skyblock.features.region.Region;
import in.godspunky.skyblock.features.skill.*;
import in.godspunky.skyblock.features.slayer.SlayerBossType;
import in.godspunky.skyblock.features.slayer.SlayerQuest;
import in.godspunky.skyblock.util.*;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.gui.PetsGUI;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.listener.PlayerListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class User {
    public static final int ISLAND_SIZE = 125;
    private static final Map<UUID, User> USER_CACHE;
    private static final SkyBlock plugin;
    private static final File USER_FOLDER;
    @Getter
    private long sadancollections;
    @Getter
    private long totalfloor6run;
    @Getter
    private UUID uuid;
    private final Config config;
    private final Map<ItemCollection, Integer> collections;
    @Getter
    private long coins;
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
    @Getter
    private Region lastRegion;
    @Getter
    private final Map<SMaterial, Integer> quiver;
    @Getter
    private final List<ActivePotionEffect> effects;

    @Getter
    private List<String> talked_npcs;
    @Getter
    private double farmingXP;
    private boolean boneToZeroDamage;
    private boolean cooldownAPI;
    private double miningXP;
    private double combatXP;
    private double enchantXP;
    private double archerXP;
    private double cataXP;
    private double berserkXP;
    @Getter
    private double healerXP;
    @Getter
    private double tankXP;
    @Getter
    private double mageXP;
    @Getter
    private double foragingXP;
    private final int[] highestSlayers;
    private final int[] slayerXP;
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
    private List<Pet.PetItem> pets;
    @Getter
    private List<String> unlockedRecipes;
    @Getter
    private AuctionSettings auctionSettings;
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

    private User(final UUID uuid) {
        this.stashedItems = new ArrayList<ItemStack>();
        this.cooldownAltar = 0;
        this.headShot = false;
        this.playingSong = false;
        this.inDanger = false;
        this.boneToZeroDamage = false;
        this.cooldownAPI = false;
        this.saveable = true;
        this.waitingForSign = false;
        this.signContent = null;
        this.isCompletedSign = false;
        this.uuid = uuid;
        this.collections = ItemCollection.getDefaultCollections();
        this.totalfloor6run = 0L;
        this.coins = 0L;
        this.bankCoins = 0L;
        this.sadancollections = 0L;
        this.lastRegion = null;
        this.talked_npcs = new ArrayList<>();
        this.quiver = new HashMap<SMaterial, Integer>();
        this.effects = new ArrayList<ActivePotionEffect>();
        this.unlockedRecipes = new ArrayList<>();
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
        if (!User.USER_FOLDER.exists()) {
            User.USER_FOLDER.mkdirs();
        }
        final String path = uuid.toString() + ".yml";
        final File configFile = new File(User.USER_FOLDER, path);
        boolean save = false;
        try {
            if (!configFile.exists()) {
                save = true;
                configFile.createNewFile();
            }
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        this.config = new Config(User.USER_FOLDER, path);
        User.USER_CACHE.put(uuid, this);
        if (save) {
            this.save();
        }
        this.load();
    }

    public void unload() {
        User.USER_CACHE.remove(this.uuid);
    }
    public void addTalkedNPC(String name){
        if (!talked_npcs.contains(name)){
            talked_npcs.add(name);
        }
    }

    public void load() {
        this.uuid = UUID.fromString(this.config.getString("uuid"));
        if (this.config.contains("collections")) {
            for (final String identifier : this.config.getConfigurationSection("collections").getKeys(false)) {
                this.collections.put(ItemCollection.getByIdentifier(identifier), this.config.getInt("collections." + identifier));
            }
        }
        this.coins = this.config.getLong("coins");
        this.bankCoins = this.config.getLong("bankCoins");
        this.lastRegion = ((this.config.getString("lastRegion") != null) ? Region.get(this.config.getString("lastRegion")) : null);
        if (this.config.contains("quiver")) {
            for (final String m : this.config.getConfigurationSection("quiver").getKeys(false)) {
                this.quiver.put(SMaterial.getMaterial(m), this.config.getInt("quiver." + m));
            }
        }
        if (this.config.contains("effects")) {
            for (final String key : this.config.getConfigurationSection("effects").getKeys(false)) {
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
        if (SkyBlock.dimoonEnabled) {
            this.crystalLVL[0] = this.config.getInt("crystals.level.health");
            this.crystalLVL[1] = this.config.getInt("crystals.level.defense");
            this.crystalLVL[2] = this.config.getInt("crystals.level.critdmg");
            this.crystalLVL[3] = this.config.getInt("crystals.level.critchance");
            this.crystalLVL[4] = this.config.getInt("crystals.level.intelligence");
            this.crystalLVL[5] = this.config.getInt("crystals.level.ferocity");
            this.crystalLVL[6] = this.config.getInt("crystals.level.magicfind");
            this.crystalLVL[7] = this.config.getInt("crystals.level.strength");
        }
        this.permanentCoins = this.config.getBoolean("permanentCoins");
        this.slayerQuest = (SlayerQuest) this.config.get("slayer.quest");
        if (this.config.contains("pets")) {
            this.pets = (List<Pet.PetItem>) this.config.getList("pets");
        }
        if (config.contains("unlockedRecipes")) {
            this.unlockedRecipes = (List<String>) this.config.getList("unlockedRecipes");
        }
        if (config.contains("talked_npcs")){
            this.talked_npcs = (List<String>) this.config.getList("talked_npcs");
        }
        this.auctionSettings = (AuctionSettings) this.config.get("auction.settings");
        if (this.auctionSettings == null) {
            this.auctionSettings = new AuctionSettings();
        }
        this.auctionCreationBIN = this.config.getBoolean("auction.creationBIN");
        this.auctionEscrow = (AuctionEscrow) this.config.get("auction.escrow");
        if (this.auctionEscrow == null) {
            this.auctionEscrow = new AuctionEscrow();
        }
    }

    public void asyncSavingData() {
        new BukkitRunnable() {
            public void run() {
                User.this.saveCookie();
                User.this.save();
                User.this.saveAllVanillaInstances();
            }
        }.runTaskAsynchronously(User.plugin);
    }

    public void syncSavingData() {
        new BukkitRunnable() {
            public void run() {
                User.this.saveCookie();
                User.this.save();
                User.this.saveAllVanillaInstances();
            }
        }.runTask(User.plugin);
    }

    public void loadStatic() {
        final Player player = Bukkit.getPlayer(this.uuid);
        final User user = getUser(this.uuid);
        PlayerUtils.AUTO_SLAYER.put(player.getUniqueId(), this.config.getBoolean("configures.autoSlayer"));
        Repeater.SBA_MAP.put(player.getUniqueId(), this.config.getBoolean("configures.sbaToggle"));
        PetsGUI.setShowPets(player, this.config.getBoolean("configures.showPets"));
    }

    public void loadCookieStatus() {
        final Player player = Bukkit.getPlayer(this.uuid);
        PlayerUtils.setCookieDurationTicks(player, this.config.getLong("user.cookieDuration"));
        PlayerUtils.loadCookieStatsBuff(player);
    }

    public void saveCookie() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        if (!Bukkit.getPlayer(this.uuid).isOnline()) {
            return;
        }
        if (!PlayerUtils.COOKIE_DURATION_CACHE.containsKey(this.uuid)) {
            return;
        }
        this.config.set("user.cookieDuration", PlayerUtils.getCookieDurationTicks(Bukkit.getPlayer(this.uuid)));
        this.config.save();
    }

    public void save() {
        this.config.set("uuid", this.uuid.toString());
        this.config.set("collections", null);
        for (final Map.Entry<ItemCollection, Integer> entry : this.collections.entrySet()) {
            this.config.set("collections." + entry.getKey().getIdentifier(), entry.getValue());
        }
        this.config.set("coins", this.coins);
        this.config.set("bankCoins", this.bankCoins);
        if (this.lastRegion != null) {
            this.config.set("lastRegion", this.lastRegion.getName());
        }
        this.config.set("quiver", null);
        for (final Map.Entry<SMaterial, Integer> entry2 : this.quiver.entrySet()) {
            this.config.set("quiver." + entry2.getKey().name().toLowerCase(), entry2.getValue());
        }
        this.config.set("effects", null);
        for (final ActivePotionEffect effect : this.effects) {
            final PotionEffectType type = effect.getEffect().getType();
            this.config.set("effects." + type.getNamespace() + ".level", effect.getEffect().getLevel());
            this.config.set("effects." + type.getNamespace() + ".duration", effect.getEffect().getDuration());
            this.config.set("effects." + type.getNamespace() + ".remaining", effect.getRemaining());
        }
        try {
            this.config.set("user.lastPlayedVersion", SkyBlock.getPlugin().getServerVersion().readableString());
        } catch (final Exception e) {
            e.printStackTrace();
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
        if (SkyBlock.dimoonEnabled) {
            this.config.set("crystals.level.health", this.crystalLVL[0]);
            this.config.set("crystals.level.defense", this.crystalLVL[1]);
            this.config.set("crystals.level.critdmg", this.crystalLVL[2]);
            this.config.set("crystals.level.critchance", this.crystalLVL[3]);
            this.config.set("crystals.level.intelligence", this.crystalLVL[4]);
            this.config.set("crystals.level.ferocity", this.crystalLVL[5]);
            this.config.set("crystals.level.magicfind", this.crystalLVL[6]);
            this.config.set("crystals.level.strength", this.crystalLVL[7]);
        }
        this.config.set("xp.slayer.revenantHorror", this.slayerXP[0]);
        this.config.set("xp.slayer.tarantulaBroodfather", this.slayerXP[1]);
        this.config.set("xp.slayer.svenPackmaster", this.slayerXP[2]);
        this.config.set("xp.slayer.voidgloomSeraph", this.slayerXP[3]);
        this.config.set("permanentCoins", this.permanentCoins);
        this.config.set("slayer.quest", this.slayerQuest);
        this.config.set("pets", this.pets);
        this.config.set("unlockedRecipes", this.unlockedRecipes);
        this.config.set("talked_npcs" , this.talked_npcs);
        this.config.set("auction.settings", this.auctionSettings);
        this.config.set("auction.creationBIN", this.auctionCreationBIN);
        this.config.set("auction.escrow", this.auctionEscrow);
        if (Bukkit.getPlayer(this.uuid) != null && Bukkit.getPlayer(this.uuid).isOnline()) {
            this.config.set("configures.showPets", PetsGUI.getShowPet(Bukkit.getPlayer(this.uuid)));
            this.config.set("configures.autoSlayer", PlayerUtils.isAutoSlayer(Bukkit.getPlayer(this.uuid)));
            this.config.set("configures.sbaToggle", PlayerUtils.isSBAToggle(Bukkit.getPlayer(this.uuid)));
        }
        this.config.save();
    }

    public void saveStorageData(final int page) {
    }


    public void saveInventory() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        Object a = null;
        final PlayerInventory piv = Bukkit.getPlayer(this.uuid).getInventory();
        a = this.getPureListFrom(piv);
        this.config.set("database.inventory", a);
        this.config.save();
    }

    public String getPureListFrom(final Inventory piv) {
        final ItemStack[] ist = piv.getContents();
        final List<ItemStack> arraylist = Arrays.asList(ist);
        for (int i = 0; i < ist.length; ++i) {
            final ItemStack stack = ist[i];
            if (stack != null) {
                final NBTItem nbti = new NBTItem(stack);
                if (nbti.hasKey("dontSaveToProfile")) {
                    arraylist.remove(i);
                }
            }
        }
        final ItemStack[] arrl = (ItemStack[]) arraylist.toArray();
        return BukkitSerializeClass.itemStackArrayToBase64(arrl);
    }

    public void saveArmor() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        Object a = null;
        a = BukkitSerializeClass.itemStackArrayToBase64(Bukkit.getPlayer(this.uuid).getInventory().getArmorContents());
        this.config.set("database.armor", a);
        this.config.save();
    }

    public void saveEnderChest() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        Object a = null;
        final Inventory inv = Bukkit.getPlayer(this.uuid).getEnderChest();
        a = this.getPureListFrom(inv);
        this.config.set("database.enderchest", a);
        this.config.save();
    }

    public void saveExp() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        this.config.set("database.minecraft_xp", Sputnik.getTotalExperience(Bukkit.getPlayer(this.uuid)));
        this.config.save();
    }

    public void loadPlayerData() throws IllegalArgumentException, IOException {
        if (!multiServer) return;
        final Player player = Bukkit.getPlayer(this.uuid);
        if (this.config.getString("database.inventory") != null) {
            player.getInventory().setContents(BukkitSerializeClass.itemStackArrayFromBase64(this.config.getString("database.inventory")));
        } else {
            player.getInventory().setContents(new ItemStack[player.getInventory().getSize()]);
        }
        if (this.config.getString("database.enderchest") != null) {
            player.getEnderChest().setContents(BukkitSerializeClass.itemStackArrayFromBase64(this.config.getString("database.enderchest")));
        } else {
            player.getInventory().setContents(new ItemStack[player.getEnderChest().getSize()]);
        }
        if (this.config.getString("database.armor") != null) {
            player.getInventory().setArmorContents(BukkitSerializeClass.itemStackArrayFromBase64(this.config.getString("database.armor")));
        } else {
            player.getInventory().setContents(new ItemStack[player.getInventory().getArmorContents().length]);
        }
        if (this.config.contains("database.minecraft_xp")) {
            Sputnik.setTotalExperience(player, this.config.getInt("database.minecraft_xp"));
        }
        if (this.config.contains("database.stashed")) {
            final ItemStack[] arr = BukkitSerializeClass.itemStackArrayFromBase64(this.config.getString("database.stashed"));
            this.stashedItems = Arrays.asList(arr);
        } else {
            this.stashedItems = new ArrayList<ItemStack>();
        }
        this.loadEconomy();
        if (this.config.contains("configures.slot_selected")) {
            player.getInventory().setHeldItemSlot(this.config.getInt("configures.slot_selected"));
        }
    }

    public void loadEconomy() {
        final Economy eco = SkyBlock.getEconomy();
        final Player player = Bukkit.getPlayer(this.uuid);
        if (this.config.contains("database.skysim_bits")) {
            eco.withdrawPlayer(player, eco.getBalance(player));
            eco.depositPlayer(player, this.config.getDouble("database.skysim_bits"));
        }
    }

    public void saveBitsAmount() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        this.config.set("database.skysim_bits", SkyBlock.getEconomy().getBalance(Bukkit.getPlayer(this.uuid)));
        this.config.save();
    }

    public void saveLastSlot() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        this.config.set("configures.slot_selected", Bukkit.getPlayer(this.uuid).getInventory().getHeldItemSlot());
        this.config.save();
    }

    public void saveAllVanillaInstances() {
        if (!multiServer) return;
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        this.saveArmor();
        this.saveEnderChest();
        this.saveInventory();
        this.saveExp();
        this.saveBitsAmount();
        this.saveLastSlot();
        this.saveAttributesForAPI();
        this.saveStash();
    }

    public void saveStash() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        if (this.stashedItems == null) {
            return;
        }
        ItemStack[] is = new ItemStack[this.stashedItems.size()];
        is = this.stashedItems.toArray(is);
        this.config.set("database.stashed", BukkitSerializeClass.itemStackArrayToBase64(is));
        this.config.save();
    }

    public void saveAttributesForAPI() {
        if (Bukkit.getPlayer(this.uuid) == null) {
            return;
        }
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(this.getUuid());
        if (statistics == null) {
            return;
        }
        Double visualcap = statistics.getCritChance().addAll() * 100.0;
        if (visualcap > 100.0) {
            visualcap = 100.0;
        }
        this.config.set("apistats.health", statistics.getMaxHealth().addAll().intValue());
        this.config.set("apistats.defense", statistics.getDefense().addAll().intValue());
        this.config.set("apistats.strength", statistics.getStrength().addAll().intValue());
        this.config.set("apistats.crit_chance", visualcap.intValue());
        this.config.set("apistats.speed", statistics.getSpeed().addAll() * 100.0);
        this.config.set("apistats.crit_damage", statistics.getCritDamage().addAll() * 100.0);
        this.config.set("apistats.intelligence", statistics.getIntelligence().addAll().intValue());
        this.config.set("apistats.attack_speed", Math.min(100.0, statistics.getAttackSpeed().addAll()));
        this.config.set("apistats.magic_find", statistics.getMagicFind().addAll() * 100.0);
        this.config.set("apistats.ferocity", statistics.getFerocity().addAll().intValue());
        this.config.set("apistats.ability_damage", statistics.getAbilityDamage().addAll().intValue());
        this.config.save();
    }

    public Config getConfig() {
        return this.config;
    }

    public void setLastRegion(final Region lastRegion) {
        this.lastRegion = lastRegion;
    }

    public void addCoins(final long coins) {
        this.coins += coins;
    }

    public void subCoins(final long coins) {
        this.coins -= coins;
    }

    public void setCoins(final long coins) {
        this.coins = coins;
    }

    public void addBankCoins(final long bankCoins) {
        this.bankCoins += bankCoins;
    }

    public void subBankCoins(final long bankCoins) {
        this.bankCoins -= bankCoins;
    }

    public void setBankCoins(final long bankCoins) {
        this.bankCoins = bankCoins;
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

    public static void dmgDimon(final LivingEntity entity, final Player damager) {
        final int bonusDamage = 0;
        if (damager != null && entity.hasMetadata("Dimoon") && SkyBlock.getPlugin().dimoon != null) {
            final Dimoon dimoon = SkyBlock.getPlugin().dimoon;
            final int damage = 1 + dimoon.getParkoursCompleted() + bonusDamage;
            dimoon.damage(damage, damager.getName());
        }
    }

    public void damageEntity(final Damageable entity1, final double damageBase) {
        if (entity1.isDead()) {
            return;
        }
        final Player player = Bukkit.getPlayer(this.uuid);
        double damage = damageBase;
        dmgDimon((LivingEntity) entity1, player);
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
        if (cause == EntityDamageEvent.DamageCause.DROWNING && cause == EntityDamageEvent.DamageCause.DROWNING) {
            actual = health * 5.0 / 100.0;
        }
        if (cause == EntityDamageEvent.DamageCause.SUFFOCATION && cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
            actual = health * 5.0 / 100.0;
        }
        if (cause == EntityDamageEvent.DamageCause.CONTACT && cause == EntityDamageEvent.DamageCause.CONTACT) {
            actual = 5.0;
        }
        if ((cause == EntityDamageEvent.DamageCause.WITHER || cause == EntityDamageEvent.DamageCause.POISON) && (cause == EntityDamageEvent.DamageCause.WITHER || cause == EntityDamageEvent.DamageCause.POISON)) {
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
        final World world = Bukkit.getWorld(SkyblockIsland.ISLAND_PREFIX + uuid);
        if (world == null) {
            return false;
        }
       return location.getWorld().getName().equalsIgnoreCase(world.getName());
    }

    public boolean isOnUserIsland() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player == null) {
            return false;
        }
        return player.getWorld().getName().startsWith(SkyblockIsland.ISLAND_PREFIX) && !isOnIsland(player.getLocation());
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
            final World world = Bukkit.getWorld(SkyblockIsland.ISLAND_PREFIX + uuid.toString());
            player.teleport(new Location(world , 0 , 100 , 0));
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

    public static void wipeUser(final UUID uuid) {
        final String wipeID = generateRandom();
        SLog.info("Wiping with " + wipeID);
        if (Bukkit.getPlayer(uuid).isOnline()) {
            final Player p = Bukkit.getPlayer(uuid);
            p.kickPlayer(ChatColor.RED + "You have been disconnected");
        }
        final Path source = Paths.get(User.USER_FOLDER + "/" + uuid.toString() + ".yml");
        try {
            Files.move(source, source.resolveSibling("WIPED_" + wipeID + "_" + uuid + ".yml"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
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

    public static Collection<User> getCachedUsers() {
        return User.USER_CACHE.values();
    }

    public boolean hasPermission(final String permission) {
        return true;
    }

    public static Map<UUID, User> getHash() {
        return User.USER_CACHE;
    }

    public void updateArmorInventory() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            this.updateHelmet();
            this.updateChestplate();
            this.updateLeggings();
            this.updateBoots();
        }
    }

    public void updateEnderChest() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            for (int i = 0; i < player.getEnderChest().getContents().length; ++i) {
                final ItemStack is = player.getEnderChest().getItem(i);
                if (is != null) {
                    final SItem sitem = SItem.find(is);
                    if (sitem != null) {
                        if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                            sitem.setReforge(ReforgeType.STRONK.getReforge());
                            player.getEnderChest().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                            sitem.removeEnchantment(EnchantmentType.LEGION);
                            player.sendMessage(ChatColor.RED + "you have illegal enchant in ur echest, that ench got wiped, haha sike u bruh lol sus.");
                            player.getEnderChest().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                            sitem.removeEnchantment(EnchantmentType.LEGION);
                            player.sendMessage(ChatColor.RED + "you have illegal enchant in ur echest, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                            player.getEnderChest().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.SHARPNESS) != null && sitem.getEnchantment(EnchantmentType.SHARPNESS).getLevel() > 400 && !player.isOp()) {
                            sitem.removeEnchantment(EnchantmentType.SHARPNESS);
                            sitem.addEnchantment(EnchantmentType.SHARPNESS, 400);
                            player.getEnderChest().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.POWER) != null && sitem.getEnchantment(EnchantmentType.POWER).getLevel() > 300 && !player.isOp()) {
                            sitem.removeEnchantment(EnchantmentType.POWER);
                            sitem.addEnchantment(EnchantmentType.POWER, 300);
                            player.getEnderChest().setItem(i, sitem.getStack());
                        }
                        sitem.update();
                        player.getEnderChest().setItem(i, this.updateItemBoost(sitem));
                    }
                }
            }
        }
    }

    public void updateInventory() {
        final Player player = Bukkit.getPlayer(this.uuid);
        if (player != null) {
            this.updateHelmet();
            this.updateChestplate();
            this.updateLeggings();
            this.updateBoots();
            this.updateEnderChest();
            for (int i = 0; i < player.getInventory().getContents().length; ++i) {
                final ItemStack is = player.getInventory().getItem(i);
                if (is != null) {
                    final SItem sitem = SItem.find(is);
                    if (sitem != null) {
                        if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                            sitem.setReforge(ReforgeType.STRONK.getReforge());
                            player.getInventory().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                            sitem.removeEnchantment(EnchantmentType.LEGION);
                            player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus.");
                            player.getInventory().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                            sitem.removeEnchantment(EnchantmentType.LEGION);
                            player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                            player.getInventory().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.SHARPNESS) != null && sitem.getEnchantment(EnchantmentType.SHARPNESS).getLevel() > 400 && !player.isOp()) {
                            sitem.removeEnchantment(EnchantmentType.SHARPNESS);
                            sitem.addEnchantment(EnchantmentType.SHARPNESS, 400);
                            player.getInventory().setItem(i, sitem.getStack());
                        }
                        if (sitem.getEnchantment(EnchantmentType.POWER) != null && sitem.getEnchantment(EnchantmentType.POWER).getLevel() > 320 && !player.isOp()) {
                            sitem.removeEnchantment(EnchantmentType.POWER);
                            sitem.addEnchantment(EnchantmentType.POWER, 320);
                            player.getInventory().setItem(i, sitem.getStack());
                        }
                        sitem.update();
                        player.getInventory().setItem(i, this.updateItemBoost(sitem));
                    }
                }
            }
        }
    }

    public void setNMSValSeq(final float arg0) {
        this.toNMSPlayer().aA = arg0;
    }

    public void updateHelmet() {
        final Player player = Bukkit.getPlayer(this.uuid);
        final ItemStack is = player.getInventory().getHelmet();
        if (is != null) {
            final SItem sitem = SItem.find(is);
            if (sitem != null) {
                if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                    sitem.setReforge(ReforgeType.STRONK.getReforge());
                    player.getInventory().setHelmet(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus.");
                    player.getInventory().setHelmet(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                    player.getInventory().setHelmet(sitem.getStack());
                }
                sitem.update();
                player.getInventory().setHelmet(this.updateItemBoost(sitem));
            }
        }
    }

    public void updateChestplate() {
        final Player player = Bukkit.getPlayer(this.uuid);
        final ItemStack is = player.getInventory().getChestplate();
        if (is != null) {
            final SItem sitem = SItem.find(is);
            if (sitem != null) {
                if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                    sitem.setReforge(ReforgeType.STRONK.getReforge());
                    player.getInventory().setChestplate(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus.");
                    player.getInventory().setChestplate(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                    player.getInventory().setChestplate(sitem.getStack());
                }
                sitem.update();
                player.getInventory().setChestplate(this.updateItemBoost(sitem));
            }
        }
    }

    public void updateLeggings() {
        final Player player = Bukkit.getPlayer(this.uuid);
        final ItemStack is = player.getInventory().getLeggings();
        if (is != null) {
            final SItem sitem = SItem.find(is);
            if (sitem != null) {
                if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                    sitem.setReforge(ReforgeType.STRONK.getReforge());
                    player.getInventory().setLeggings(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus.");
                    player.getInventory().setLeggings(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                    player.getInventory().setLeggings(sitem.getStack());
                }
                sitem.update();
                player.getInventory().setLeggings(this.updateItemBoost(sitem));
            }
        }
    }

    public void updateBoots() {
        final Player player = Bukkit.getPlayer(this.uuid);
        final ItemStack is = player.getInventory().getBoots();
        if (is != null) {
            final SItem sitem = SItem.find(is);
            if (sitem != null) {
                if (sitem.getReforge() != null && !player.isOp() && (sitem.getReforge().toString().toUpperCase().contains("OVERPOWERED") | sitem.getReforge().toString().toUpperCase().contains("SUPERGENIUS"))) {
                    sitem.setReforge(ReforgeType.STRONK.getReforge());
                    player.getInventory().setBoots(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && !player.isOp() && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 5) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus.");
                    player.getInventory().setBoots(sitem.getStack());
                }
                if (sitem.getEnchantment(EnchantmentType.LEGION) != null && sitem.getEnchantment(EnchantmentType.LEGION).getLevel() > 10) {
                    sitem.removeEnchantment(EnchantmentType.LEGION);
                    player.sendMessage(ChatColor.RED + "you have illegal enchant in ur inv, that ench got wiped, haha sike u bruh lol sus, epik hole ze cum amogus hahaha bruh lol amogus.");
                    player.getInventory().setBoots(sitem.getStack());
                }
                sitem.update();
                player.getInventory().setBoots(this.updateItemBoost(sitem));
            }
        }
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


    public boolean isBoneToZeroDamage() {
        return this.boneToZeroDamage;
    }

    public void setBoneToZeroDamage(final boolean boneToZeroDamage) {
        this.boneToZeroDamage = boneToZeroDamage;
    }

    public boolean isCooldownAPI() {
        return this.cooldownAPI;
    }

    public void setCooldownAPI(final boolean cooldownAPI) {
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

    public boolean isCompletedSign() {
        return this.isCompletedSign;
    }

    public void setCompletedSign(final boolean isCompletedSign) {
        this.isCompletedSign = isCompletedSign;
    }

    static {
        USER_CACHE = new HashMap<UUID, User>();
        plugin = SkyBlock.getPlugin();
        USER_FOLDER = new File(new File("/rootshare/skysim"), "./users");
    }
}
