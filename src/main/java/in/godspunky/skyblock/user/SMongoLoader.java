package in.godspunky.skyblock.user;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import de.tr7zw.nbtapi.NBTItem;
import in.godspunky.skyblock.auction.AuctionEscrow;
import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.potion.ActivePotionEffect;
import in.godspunky.skyblock.potion.PotionEffect;
import in.godspunky.skyblock.potion.PotionEffectType;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.region.RegionType;
import in.godspunky.skyblock.slayer.SlayerBossType;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.util.SUtil;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SMongoLoader
{
    public static void load(UUID uuid) {
        User user = User.getUser(uuid);

        Player player = Bukkit.getPlayer(uuid);

        Document base = grabUser(uuid.toString());

        UUID selectedProfileUUID = base != null ? UUID.fromString(getString(base, "selectedProfile", null)) : null;

        if (selectedProfileUUID != null && SUtil.isUUID(selectedProfileUUID.toString())) {
            user.selectedProfile = Profile.get(selectedProfileUUID, uuid);
            if (user.selectedProfile != null) {
                user.profiles = (Map<String, Boolean>) get(base, "profiles", new HashMap<>());
                user.profiles.putIfAbsent(user.selectedProfile.uuid, true);

                User.USER_CACHE.put(uuid, user);
                user.selectedProfile.setSelected(true);
                loadProfile(user.selectedProfile);
                try {
                    user.loadPlayerData(user.selectedProfile);
                    user.loadCookieStatus(user.selectedProfile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
                player.sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + getActiveProfile(selectedProfileUUID));
                return;
            }
        }
        UUID newProfileUUID = UUID.randomUUID();
        String name = SUtil.generateRandomProfileNameFor();
        Profile newProfile = new Profile(newProfileUUID.toString(), uuid, name);
        user.selectedProfile = newProfile;
        user.profiles = new HashMap<>();
        user.profiles.put(newProfile.uuid, true);

        User.USER_CACHE.put(uuid, user);
        Profile.USER_CACHE.put(newProfileUUID.toString(), newProfile);
        grabProfile(newProfileUUID.toString());
        save(uuid);
        player.sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
        player.sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + name);
    }

    public static String getActiveProfile(UUID uuid) {
        Document document = grabProfile(uuid.toString());

        assert document != null;
        return document.getString("name");
    }

    @SneakyThrows
    public static void save(UUID uuid) {
        User user = User.getUser(uuid);
        Profile selectedProfile = user.selectedProfile;
        UserDatabase db = new UserDatabase(uuid.toString(), false);

        if (db.exists()) {
            db.getDocument().forEach(SMongoLoader::setUserProperty);
        }

        setUserProperty("selectedProfile", selectedProfile == null ? null : selectedProfile.getUuid().toString());
        setUserProperty("profiles", user.profiles);

        SUtil.runAsync(() -> saveUserData(uuid));

        if (selectedProfile == null) {
            selectedProfile = new Profile(UUID.randomUUID().toString(), uuid, "$temp");
            user.profiles = new HashMap<>();
            user.profiles.put(selectedProfile.getId().toString(), true);
        }
        selectedProfile = user.selectedProfile;
        if (selectedProfile != null) {
            selectedProfile.setLastRegion(user.getLastRegion());
            selectedProfile.setQuiver(user.getQuiver());
            selectedProfile.setEffects(user.getEffects());
            selectedProfile.setFarmingXP(user.getFarmingXP());
            selectedProfile.setMiningXP(user.getMiningXP());
            selectedProfile.setCombatXP(user.getCombatXP());
            selectedProfile.setForagingXP(user.getForagingXP());
            selectedProfile.setHighestRevenantHorror(user.highestSlayers[0]);
            selectedProfile.setHighestTarantulaBroodfather(user.highestSlayers[1]);
            selectedProfile.setHighestSvenPackmaster(user.highestSlayers[2]);
            selectedProfile.setHighestVoidgloomSeraph(user.highestSlayers[3]);
            selectedProfile.setSlayerXP(SlayerBossType.SlayerMobType.ZOMBIE, user.slayerXP[0]);
            selectedProfile.setSlayerXP(SlayerBossType.SlayerMobType.SPIDER, user.slayerXP[1]);
            selectedProfile.setSlayerXP(SlayerBossType.SlayerMobType.WOLF, user.slayerXP[2]);
            selectedProfile.setSlayerXP(SlayerBossType.SlayerMobType.ENDERMAN, user.slayerXP[3]);
            selectedProfile.setPermanentCoins(user.isPermanentCoins());
            selectedProfile.setSlayerQuest(user.getSlayerQuest());
            selectedProfile.setAuctionSettings(user.getAuctionSettings());
            selectedProfile.setAuctionCreationBIN(user.isAuctionCreationBIN());
            selectedProfile.setAuctionEscrow(user.getAuctionEscrow());
            selectedProfile.setCoins(user.getCoins());
            selectedProfile.setBankCoins(user.getBankCoins());
            selectedProfile.setCoins(user.getCoins());
            selectedProfile.setCollections(user.getCollections());
            selectedProfile.setSelected(true);
        }
        user.saveAllVanillaInstances(selectedProfile);
        user.saveCookie(selectedProfile);

        saveProfile(selectedProfile);

    }

    public static void loadProfile(Profile profile) {
        System.out.println("using message at " + System.currentTimeMillis());
        User owner = User.getUser(profile.getOwner());
        Player player = Bukkit.getPlayer(profile.owner);
        Document base = grabProfile(profile.getUuid().toString());


        SUtil.runAsync(() -> profile.owner = UUID.fromString(getString(base, "owner", UUID.randomUUID().toString())));

        SUtil.runAsync(() -> profile.name = getString(base, "name", SUtil.generateRandomProfileNameFor()));

        SUtil.runAsync(() -> profile.setSelected(getBoolean(base, "selected", false)));

        for (ItemCollection collection : ItemCollection.getCollections()) {
            profile.collections.put(collection, 0);
        }
        Map<String, Integer> coll = (Map<String, Integer>) get(base, "collections", new HashMap<>());
        coll.forEach((key, value) -> {
            owner.getCollections().put(ItemCollection.getByIdentifier(key), value);
        });

        SUtil.runAsync(() -> {
            long coins = getLong(base, "coins", 0);
            profile.setCoins(coins);
            owner.setCoins(coins);
        });

        SUtil.runAsync(() -> {
            long e = getLong(base, "bankCoins", 0);
            profile.setBankCoins(e);
            owner.setBankCoins(e);
        });

        SUtil.runAsync(() -> {
            String name = getString(base, "lastRegion", "none");
            if (name.equals("none")) {
                profile.setLastRegion(null);
                return;
            }
            profile.setLastRegion(Region.get(name) == null ? null : Region.get(getString(base, "lastRegion", "none")));
            owner.setLastRegion(profile.getLastRegion());
        });


        SUtil.runAsync(() -> {
            Map<String, Integer> quiv = (Map<String, Integer>) get(base, "quiver", new HashMap<>());
            quiv.forEach((key, value) -> {
                profile.getQuiver().put(SMaterial.getMaterial(key), value);
            });
            owner.quiver = profile.getQuiver();
        });

        SUtil.runAsync(() -> {
            profile.farmingXP = getDouble(base, "skillFarmingXp", 0.0);
            owner.farmingXP = getDouble(base, "skillFarmingXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.miningXP = getDouble(base, "skillMiningXp", 0.0);
            owner.miningXP = getDouble(base, "skillMiningXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.combatXP = getDouble(base, "skillCombatXp", 0.0);
            owner.combatXP = getDouble(base, "skillCombatXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.foragingXP = getDouble(base, "skillForagingXp", 0.0);
            owner.foragingXP = getDouble(base, "skillForagingXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[0] = getInt(base, "slayerRevenantHorrorHighest", 0);
            owner.highestSlayers[0] = getInt(base, "slayerRevenantHorrorHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[1] = getInt(base, "slayerTarantulaBroodfatherHighest", 0);
            owner.highestSlayers[1] = getInt(base, "slayerTarantulaBroodfatherHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[2] = getInt(base, "slayerSvenPackmasterHighest", 0);
            owner.highestSlayers[2] = getInt(base, "slayerSvenPackmasterHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[3] = getInt(base, "slayerVoidgloomSeraphHighest", 0);
            owner.highestSlayers[3] = getInt(base, "slayerVoidgloomSeraphHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[0] = getInt(base, "xpSlayerRevenantHorror", 0);
            owner.slayerXP[0] = getInt(base, "xpSlayerRevenantHorror", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[1] = getInt(base, "xpSlayerTarantulaBroodfather", 0);
            owner.slayerXP[1] = getInt(base, "xpSlayerTarantulaBroodfather", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[2] = getInt(base, "xpSlayerSvenPackmaster", 0);
            owner.slayerXP[2] = getInt(base, "xpSlayerSvenPackmaster", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[3] = getInt(base, "xpSlayerVoidgloomSeraph", 0);
            owner.slayerXP[3] = getInt(base, "xpSlayerVoidgloomSeraph", 0);
        });

        SUtil.runAsync(() -> {
            profile.setPermanentCoins(getBoolean(base, "permanentCoins", false));
            owner.setPermanentCoins(getBoolean(base, "permanentCoins", false));
        });

        SUtil.runAsync(() -> {
            try {
                SlayerQuest quest = SlayerQuest.deserialize((Map<String, Object>) get(base, "slayerQuest", new HashMap<>()));
                profile.setSlayerQuest(quest);
                owner.setSlayerQuest(quest);
            } catch (Exception ex) {
                profile.setSlayerQuest(null);
            }
        });

        SUtil.runAsync(() -> {
            profile.pets = new ArrayList<>();
            List<Object> listOfPetObjects = new ArrayList<>((List<Object>) get(base, "pets", new ArrayList<>()));
            listOfPetObjects.forEach((item) -> {
                Pet.PetItem pitem = Pet.PetItem.deserialize((Map<String, Object>) item);
                profile.pets.add(pitem);
            });
        });
        SUtil.runAsync(() -> {
            try {
                profile.auctionSettings = AuctionSettings.deserialize((Map<String, Object>) get(base, "auctionSettings", new HashMap<>()));
            } catch (Exception ex) {
                profile.auctionSettings = new AuctionSettings();
            }
        });
        SUtil.runAsync(() -> {
            profile.setAuctionCreationBIN(getBoolean(base, "auctionCreationBIN", false));
        });
        SUtil.runAsync(() -> {
            try {
                profile.setAuctionEscrow(AuctionEscrow.deserialize((Map<String, Object>) get(base, "auctionEscrow", new HashMap<>())));
            } catch (Exception ex) {
                profile.setAuctionEscrow(new AuctionEscrow());
            }
        });
        // Load potion effects
        if (base.containsKey("effects")) {
            List<Document> effectsDocuments = base.getList("effects", Document.class);
            for (Document effectData : effectsDocuments) {
                String key = effectData.getString("key");
                Integer level = effectData.getInteger("level");
                Long duration = effectData.getLong("duration");
                Long remaining = effectData.getLong("remaining");

                if (key != null && level != null && duration != null && remaining != null) {
                    PotionEffectType potionEffectType = PotionEffectType.getByNamespace(key);
                    if (potionEffectType != null) {
                        profile.getEffects().add(new ActivePotionEffect(
                                new PotionEffect(potionEffectType, level, duration),
                                remaining
                        ));
                    }
                }
            }
        }
        SUtil.runAsync(() -> owner.setBankCoins(profile.getBankCoins()));
        SUtil.runAsync(() -> owner.setCoins(profile.getCoins()));
        SUtil.runAsync(() -> owner.setCoins(profile.getCoins()));
        SUtil.runAsync(() -> owner.quiver = profile.getQuiver());
        SUtil.runAsync(() -> owner.effects = profile.getEffects());
        SUtil.runAsync(() -> owner.farmingXP = profile.getFarmingXP());
        SUtil.runAsync(() -> owner.miningXP = profile.getMiningXP());
        SUtil.runAsync(() -> owner.combatXP = profile.getCombatXP());
        SUtil.runAsync(() -> owner.foragingXP = profile.getForagingXP());
        SUtil.runAsync(() -> owner.highestSlayers[0] = profile.getHighestRevenantHorror());
        SUtil.runAsync(() -> owner.highestSlayers[1] = profile.getHighestTarantulaBroodfather());
        SUtil.runAsync(() -> owner.highestSlayers[2] = profile.getHighestSvenPackmaster());
        SUtil.runAsync(() -> owner.highestSlayers[3] = profile.getHighestVoidgloomSeraph());
        SUtil.runAsync(() -> owner.slayerXP[0] = profile.getSlayerXP(SlayerBossType.SlayerMobType.ZOMBIE));
        SUtil.runAsync(() -> owner.slayerXP[1] = profile.getSlayerXP(SlayerBossType.SlayerMobType.SPIDER));
        SUtil.runAsync(() -> owner.slayerXP[2] = profile.getSlayerXP(SlayerBossType.SlayerMobType.WOLF));
        SUtil.runAsync(() -> owner.slayerXP[3] = profile.getSlayerXP(SlayerBossType.SlayerMobType.ENDERMAN));
        SUtil.runAsync(() -> owner.setPermanentCoins(profile.isPermanentCoins()));
        SUtil.runAsync(() -> owner.setSlayerQuest(profile.getSlayerQuest()));
        SUtil.runAsync(() -> owner.pets = profile.getPets());
        try {
            SUtil.runAsync(() -> profile.setSelected(owner.selectedProfile.getUuid().equals(profile.getUuid())));
        } catch (Exception e) { }

        Profile.USER_CACHE.put(profile.uuid, profile);
        User.USER_CACHE.put(owner.getUuid(), owner);
    }

    public void loadOnlyDisplay(Profile profile) {
        Document base = grabProfile(profile.getUuid().toString());

        // profile.created = getLong(base, "created", System.currentTimeMillis());
        profile.name = getString(base, "name", "§cError: SBR 93");

        profile.setCoins(getLong(base, "coins", 0));
        profile.setBankCoins(getLong(base, "bankCoins", 0));
        profile.miningXP = getDouble(base, "miningXP", 0);
        profile.farmingXP = getDouble(base, "farmingXP", 0);
        profile.combatXP = getDouble(base, "combatXP", 0);
        profile.foragingXP = getDouble(base, "foragingXP", 0);
    }

    public static void saveProfile(Profile profile) {
        final long[] i = {0};
        setProfileProperty("owner", profile.owner.toString());

        setProfileProperty("name", profile.name);
        Map<String, Integer> tempColl = new HashMap<>();
        for (ItemCollection collection : ItemCollection.getCollections()) {
            tempColl.put(collection.getIdentifier(), profile.getCollection(collection));
        }
        setProfileProperty("collections", tempColl);
        setProfileProperty("coins", profile.getCoins());
        setProfileProperty("bankCoins", profile.getBankCoins());
        if (profile.getLastRegion() != null)
            setProfileProperty("lastRegion", profile.getLastRegion().getName());
        Map<String, Integer> tempQuiv = new HashMap<>();
        profile.getQuiver().forEach((key, value) -> tempQuiv.put(key.name(), value));
        setProfileProperty("quiver", tempQuiv);
        List<Document> effectsDocuments = new ArrayList<>();
        for (ActivePotionEffect effect : profile.getEffects()) {
            Document effectDocument = new Document()
                    .append("key", effect.getEffect().getType().getNamespace())
                    .append("level", effect.getEffect().getLevel())
                    .append("duration", effect.getEffect().getDuration())
                    .append("remaining", effect.getRemaining());
            effectsDocuments.add(effectDocument);
        }
        setProfileProperty("effects", effectsDocuments);
        setProfileProperty("skillFarmingXp", profile.farmingXP);
        setProfileProperty("skillMiningXp", profile.miningXP);
        setProfileProperty("skillCombatXp", profile.combatXP);
        setProfileProperty("skillForagingXp", profile.foragingXP);
        setProfileProperty("slayerRevenantHorrorHighest", profile.highestSlayers[0]);
        setProfileProperty("slayerTarantulaBroodfatherHighest", profile.highestSlayers[1]);
        setProfileProperty("slayerSvenPackmasterHighest", profile.highestSlayers[2]);
        setProfileProperty("slayerVoidgloomSeraphHighest", profile.highestSlayers[3]);
        setProfileProperty("permanentCoins", profile.isPermanentCoins());
        setProfileProperty("xpSlayerRevenantHorror", profile.slayerXP[0]);
        setProfileProperty("xpSlayerTarantulaBroodfather", profile.slayerXP[1]);
        setProfileProperty("xpSlayerSvenPackmaster", profile.slayerXP[2]);
        setProfileProperty("xpSlayerVoidgloomSeraph", profile.slayerXP[3]);
        if (profile.getSlayerQuest() != null)
            setProfileProperty("slayerQuest", profile.getSlayerQuest().serialize());
        if (!profile.pets.isEmpty()) {
            List<Map<String, Object>> petsSerialized = profile.pets.stream().map(pet -> pet.serialize()).collect(Collectors.toList());
            setProfileProperty("pets", petsSerialized);
        } else {
            setProfileProperty("pets", new ArrayList<Map<String, Object>>());
        }
        setProfileProperty("auctionSettings", profile.auctionSettings.serialize());
        //setProfileProperty("created", profile.created);
        setProfileProperty("auctionCreationBIN", profile.isAuctionCreationBIN());
        setProfileProperty("selected", profile.isSelected());
        SUtil.runAsync(() -> saveProfileData(UUID.fromString(profile.uuid)));
    }

    public static void saveNewProfile(Profile profile) {
        setProfileProperty("name", profile.name);
        Map<String, Integer> tempColl = new HashMap<>();
        profile.collections.forEach((key, value) -> {
            tempColl.put(key.getIdentifier(), value);
        });
        setProfileProperty("collections", tempColl);
        setProfileProperty("coins", profile.getCoins());
        setProfileProperty("bankCoins", profile.getBankCoins());
        if (profile.getLastRegion() != null)
            setProfileProperty("lastRegion", profile.getLastRegion().getName());
        Map<String, Integer> tempQuiv = new HashMap<>();
        profile.getQuiver().forEach((key, value) -> tempQuiv.put(key.name(), value));
        setProfileProperty("quiver", tempQuiv);
        List<Document> effectsDocuments = new ArrayList<>();
        for (ActivePotionEffect effect : profile.getEffects()) {
            Document effectDocument = new Document()
                    .append("key", effect.getEffect().getType().getNamespace())
                    .append("level", effect.getEffect().getLevel())
                    .append("duration", effect.getEffect().getDuration())
                    .append("remaining", effect.getRemaining());
            effectsDocuments.add(effectDocument);
        }
        setProfileProperty("effects", effectsDocuments);
        setProfileProperty("skillFarmingXp", profile.farmingXP);
        setProfileProperty("skillMiningXp", profile.miningXP);
        setProfileProperty("skillCombatXp", profile.combatXP);
        setProfileProperty("skillForagingXp", profile.foragingXP);
        setProfileProperty("slayerRevenantHorrorHighest", profile.highestSlayers[0]);
        setProfileProperty("slayerTarantulaBroodfatherHighest", profile.highestSlayers[1]);
        setProfileProperty("slayerSvenPackmasterHighest", profile.highestSlayers[2]);
        setProfileProperty("slayerVoidgloomSeraphHighest", profile.highestSlayers[3]);
        setProfileProperty("permanentCoins", profile.isPermanentCoins());
        setProfileProperty("xpSlayerRevenantHorror", profile.slayerXP[0]);
        setProfileProperty("xpSlayerTarantulaBroodfather", profile.slayerXP[1]);
        setProfileProperty("xpSlayerSvenPackmaster", profile.slayerXP[2]);
        setProfileProperty("xpSlayerVoidgloomSeraph", profile.slayerXP[3]);
        if (profile.getSlayerQuest() != null)
            setProfileProperty("slayerQuest", profile.getSlayerQuest().serialize());
        if (!profile.pets.isEmpty()) {
            List<Map<String, Object>> petsSerialized = profile.pets.stream().map(pet -> pet.serialize()).collect(Collectors.toList());
            setProfileProperty("pets", petsSerialized);
        } else {
            setProfileProperty("pets", new ArrayList<Map<String, Object>>());
        }
        setProfileProperty("unlockedRecipes", profile.getUnlockedRecipes());
        setProfileProperty("talked_npcs", profile.getTalked_npcs());
        setProfileProperty("auctionSettings", profile.auctionSettings.serialize());
        setProfileProperty("auctionCreationBIN", profile.isAuctionCreationBIN());
        setProfileProperty("auctionEscrow", profile.getAuctionEscrow().serialize());
        setProfileProperty("selected", profile.isSelected());

        SUtil.runAsync(() -> saveProfileData(profile.owner));
    }

    private static Map<String, Object> profileCache = new ConcurrentHashMap<>();
    private static Map<String, Object> userCache = new ConcurrentHashMap<>();
    private static final Object profileCacheLock = new Object();
    private static final Object userCacheLock = new Object();

    public SMongoLoader() {
        profileCache = new HashMap<>();
        userCache = new HashMap<>();
    }

    public static void setProfileProperty(String key, Object value) {
        synchronized (profileCacheLock) {
            if (value != null) {
                profileCache.put(key, value);
            }
        }
    }

    public static void setUserProperty(String key, Object value) {
        synchronized (userCacheLock) {
            if (value != null) {
                userCache.put(key, value);
            }
        }
    }

    public static Document grabUser(String id) {
        Document query = new Document("_id", id);
        Document foundOrNot = DatabaseManager.getCollection("users").find(query).first();
        if (foundOrNot == null) {
            SUtil.runAsync(() -> DatabaseManager.getCollection("users").insertOne(new Document("_id", id)));
        }
        System.out.println("USER BLAH BLAH " + id);
        return foundOrNot;
    }

    public static Document grabProfile(String id) {
        Document query = new Document("_id", id);
        Document foundOrNot = DatabaseManager.getCollection("profiles").find(query).first();
        if (foundOrNot == null) {
            SUtil.runAsync(() -> DatabaseManager.getCollection("profiles").insertOne(new Document("_id", id)));
        }
        System.out.println("PROFILE BLAH BLAH " + id);
        return foundOrNot;
    }

    public static Object get(Document base, String key, Object def) {
        if (base.get(key) != null) {
            return base.get(key);
        }
        return def;
    }

    public static String getString(Document base, String key, Object def) {
        return get(base, key, def).toString();
    }

    public static int getInt(Document base, String key, Object def) {
        return (int) get(base, key, def);
    }

    public static boolean getBoolean(Document base, String key, Object def) {
        return Boolean.parseBoolean(get(base, key, def).toString());
    }

    public static double getDouble(Document base, String key, Object def) {
        return Double.parseDouble(getString(base, key, def));
    }

    public static long getLong(Document base, String key, Object def) {
        if (def.equals(0.0)) {
            def = 0L;
        }
        return Long.parseLong(getString(base, key, def));
    }

    public static void saveProfileData(UUID uuid) {
        synchronized (profileCacheLock) {
            Document found = grabProfile(uuid.toString());

            if (found != null) {
                Document updated = new Document(found);
                profileCache.forEach(updated::append);

                assert found != null;
                DatabaseManager.getCollection("profiles").replaceOne(found, updated);
            }
        }
    }

    public static void saveUserData(UUID uuid) {
        synchronized (userCacheLock) {
            Document found = grabUser(uuid.toString());

            if (found != null) {
                Document updated = new Document();
                userCache.forEach(updated::append);

                assert found != null;
                DatabaseManager.getCollection("users").replaceOne(found, updated);
            }
        }
    }
}