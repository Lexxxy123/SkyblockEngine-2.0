package net.hypixel.skyblock.user;

import com.mongodb.client.MongoCollection;
import lombok.SneakyThrows;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.serializer.BukkitSerializeClass;
import net.hypixel.skyblock.features.auction.AuctionEscrow;
import net.hypixel.skyblock.features.collection.ItemCollection;

import net.hypixel.skyblock.features.island.SkyblockIsland;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.sql.DatabaseManager;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.SaveQueue;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SMongoLoader {
    public static SaveQueue<SaveInfo> savingQueue = new SaveQueue<>();
    public Map<String, Object> userCache;
    public String cachedUserId;
    public String cachedProfileId;


    public SMongoLoader(UUID id) {
        userCache = new HashMap<>();
        cachedUserId = id.toString();
    }


    public SMongoLoader() {
        userCache = new HashMap<>();
    }

    public static void queue(SaveInfo i) {
        savingQueue.add(i);
    }

    public static void queue(String uuid, boolean soft) {
        savingQueue.add(new SaveInfo(uuid, soft));
    }

    public static void startQueueTask() {
        SkyBlock.getPlugin().getServer().getScheduler().runTaskTimer(SkyBlock.getPlugin(), () -> {
            if (!savingQueue.isEmpty()) {
                SaveInfo currentSave = savingQueue.dequeue();
                SLog.info("Saving data for :" + currentSave.getUuid());
                SkyBlock.getPlugin().dataLoader.save(UUID.fromString(currentSave.getUuid()));
            }
        }, 1L, 1L);
    }

    public void load(UUID uuid) {
        User user = User.getUser(uuid);
        cachedUserId = uuid.toString();

        Document query = new Document("_id", uuid.toString());
        MongoCollection<Document> collection = DatabaseManager.getCollection("users");

        Document foundOrNot = collection.find(query).first();

        if (foundOrNot == null) {

        }

        if (foundOrNot != null) {
            User.USER_CACHE.put(uuid, user);
            loadProfile(user);
            user.toBukkitPlayer().sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky SkyBlock!");
        } else {
            collection.insertOne(new Document("_id", uuid.toString()));
            createAndSaveNewProfile(uuid);
           /* Bukkit.getScheduler().runTaskLater(SkyBlock.getPlugin(), () -> {
                SkyblockIsland.getIsland(uuid).send();
            }, 5 * 20L);*/
        }
    }

    public void createAndSaveNewProfile(UUID uuid) {
        User user = User.getUser(uuid);
        User.USER_CACHE.put(uuid, user);
        savenewProfile(user);
        user.toBukkitPlayer().sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky SkyBlock!");
    }


    @SneakyThrows
    public void save(UUID uuid) {
        User user = User.getUser(uuid);
        saveProfile(user);
    }

    public void loadProfile(User profile) {
        System.out.println("using message at " + System.currentTimeMillis());
        Document base = grabProfile(profile.getUuid().toString());
        
        Map<String, Integer> coll = (Map<String, Integer>) get(base, "collections", new HashMap<>());
        coll.forEach((key, value) -> {
            profile.getCollections().put(ItemCollection.getByIdentifier(key), value);
        });

        SUtil.runAsync(() -> {
            String rank = getString(base, "rank", "DEFAULT");
            profile.setRank(PlayerRank.valueOf(rank));
        });

        SUtil.runAsync(() -> {
            long coins = getLong(base, "coins", 0);
            profile.setCoins(coins);
        });

        SUtil.runAsync(() -> {
            long bits = getLong(base, "bits", 0);
            profile.setBits(bits);
        });

        SUtil.runAsync(() -> {
            Document data = (Document) get(base, "data" , new Document());
            List<String> foundzone = data.getList("foundzone", String.class , new ArrayList<>());
            long runs = data.getLong("floor6");
            long sadan = data.getLong("sadan");
            List<String> talkednpc = data.getList("talkednpc" , String.class , new ArrayList<>());
            profile.addTalkedNPC(talkednpc.toString());
            profile.totalfloor6run = runs;
            profile.sadancollections = sadan;
            profile.addnewzone(foundzone.toString());
        });

        SUtil.runAsync(() -> {
            long e = getLong(base, "bankCoins", 0);
            profile.setBankCoins(e);
        });

        SUtil.runAsync(() -> {
            String name = getString(base, "lastRegion", "none");
            if (name.equals("none")) {
                profile.setLastRegion(null);
                return;
            }
            profile.setLastRegion(Region.get(name) == null ? null : Region.get(getString(base, "lastRegion", "none")));
        });


        SUtil.runAsync(() -> {
            Map<String, Integer> quiv = (Map<String, Integer>) get(base, "quiver", new HashMap<>());
            quiv.forEach((key, value) -> {
                profile.getQuiver().put(SMaterial.getMaterial(key), value);
            });
        });



        SUtil.runAsync(() -> {
            profile.farmingXP = getDouble(base, "skillFarmingXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.miningXP = getDouble(base, "skillMiningXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.combatXP = getDouble(base, "skillCombatXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.foragingXP = getDouble(base, "skillForagingXp", 0.0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[0] = getInt(base, "slayerRevenantHorrorHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[1] = getInt(base, "slayerTarantulaBroodfatherHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[2] = getInt(base, "slayerSvenPackmasterHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.highestSlayers[3] = getInt(base, "slayerVoidgloomSeraphHighest", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[0] = getInt(base, "xpSlayerRevenantHorror", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[1] = getInt(base, "xpSlayerTarantulaBroodfather", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[2] = getInt(base, "xpSlayerSvenPackmaster", 0);
        });

        SUtil.runAsync(() -> {
            profile.slayerXP[3] = getInt(base, "xpSlayerVoidgloomSeraph", 0);
        });

        SUtil.runAsync(() -> {
            profile.setPermanentCoins(getBoolean(base, "permanentCoins", false));
        });

        SUtil.runAsync(() -> {
            try {
                SlayerQuest quest = SlayerQuest.deserialize((Map<String, Object>) get(base, "slayerQuest", new HashMap<>()));
                profile.setSlayerQuest(quest);
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

        SUtil.runAsync(() -> {
            Document questData = (Document) get(base, "quests", new Document());
            List<String> completedQuests = questData.getList("completedQuests", String.class, new ArrayList<>());
            List<String> completedObjectives = questData.getList("completedObjectives", String.class, new ArrayList<>());

            profile.setCompletedQuests(completedQuests);
            profile.setCompletedObjectives(completedObjectives);

        });

        profile.loadCookieStatus();

        User.USER_CACHE.put(profile.getUuid(), profile);
    }

    public void saveProfile(User profile) {
        UserDatabase db = new UserDatabase(profile.getUuid().toString(), true);

        //profile.saveAllVanillaInstances();
        Map<String, Integer> tempColl = new HashMap<>();
        for (ItemCollection collection : ItemCollection.getCollections()) {
            tempColl.put(collection.getIdentifier(), profile.getCollection(collection));
        }
        setUserProperty("collections", tempColl);
        setUserProperty("coins", profile.getCoins());
        setUserProperty("bits", profile.getBits());
        setUserProperty("rank", String.valueOf(profile.getRank()));
        setUserProperty("bankCoins", profile.getBankCoins());
        if (profile.getLastRegion() != null)
            setUserProperty("lastRegion", profile.getLastRegion().getName());
        Map<String, Object> data = new HashMap<>();
        data.put("foundzone", profile.getdiscoveredzones());
        data.put("talkednpc", profile.getTalked_npcs());
        data.put("floor6", profile.getTotalfloor6run());
        data.put("sadan", profile.getSadancollections());
        setUserProperty("data", data);
        Map<String, Object> questData = new HashMap<>();
        questData.put("completedQuests", profile.getCompletedQuests());
        questData.put("completedObjectives", profile.getCompletedObjectives());
        setUserProperty("quests", questData);
        Map<String, Integer> tempQuiv = new HashMap<>();
        profile.getQuiver().forEach((key, value) -> tempQuiv.put(key.name(), value));
        setUserProperty("quiver", tempQuiv);
        List<Document> effectsDocuments = new ArrayList<>();
        for (ActivePotionEffect effect : profile.getEffects()) {
            Document effectDocument = new Document()
                    .append("key", effect.getEffect().getType().getNamespace())
                    .append("level", effect.getEffect().getLevel())
                    .append("duration", effect.getEffect().getDuration())
                    .append("remaining", effect.getRemaining());
            effectsDocuments.add(effectDocument);
        }
        profile.saveCookie();
        setUserProperty("effects", effectsDocuments);
        setUserProperty("skillFarmingXp", profile.farmingXP);
        setUserProperty("skillMiningXp", profile.miningXP);
        setUserProperty("skillCombatXp", profile.combatXP);
        setUserProperty("skillForagingXp", profile.foragingXP);
        setUserProperty("slayerRevenantHorrorHighest", profile.highestSlayers[0]);
        setUserProperty("slayerTarantulaBroodfatherHighest", profile.highestSlayers[1]);
        setUserProperty("slayerSvenPackmasterHighest", profile.highestSlayers[2]);
        setUserProperty("slayerVoidgloomSeraphHighest", profile.highestSlayers[3]);
        setUserProperty("permanentCoins", profile.isPermanentCoins());
        setUserProperty("xpSlayerRevenantHorror", profile.slayerXP[0]);
        setUserProperty("xpSlayerTarantulaBroodfather", profile.slayerXP[1]);
        setUserProperty("xpSlayerSvenPackmaster", profile.slayerXP[2]);
        setUserProperty("xpSlayerVoidgloomSeraph", profile.slayerXP[3]);
        if (profile.getSlayerQuest() != null)
            setUserProperty("slayerQuest", profile.getSlayerQuest().serialize());
        if (!profile.pets.isEmpty()) {
            List<Map<String, Object>> petsSerialized = profile.pets.stream().map(pet -> pet.serialize()).collect(Collectors.toList());
            setUserProperty("pets", petsSerialized);
        } else {
            setUserProperty("pets", new ArrayList<Map<String, Object>>());
        }
        setUserProperty("auctionSettings", profile.auctionSettings.serialize());

        //setUserProperty("created", profile.created);
        setUserProperty("auctionCreationBIN", profile.isAuctionCreationBIN());
        SUtil.runAsync(() -> saveProfileData(db));
        User.USER_CACHE.remove(profile.getUuid());
    }

    public void savenewProfile(User profile) {
        UserDatabase db = new UserDatabase(profile.getUuid().toString(), true);

        //profile.saveAllVanillaInstances();
        Map<String, Integer> tempColl = new HashMap<>();
        for (ItemCollection collection : ItemCollection.getCollections()) {
            tempColl.put(collection.getIdentifier(), profile.getCollection(collection));
        }
        setUserProperty("collections", tempColl);
        setUserProperty("coins", profile.getCoins());
        setUserProperty("bits", profile.getBits());
        setUserProperty("rank", String.valueOf(profile.getRank()));
        setUserProperty("bankCoins", profile.getBankCoins());
        if (profile.getLastRegion() != null)
            setUserProperty("lastRegion", profile.getLastRegion().getName());
        Map<String, Object> data = new HashMap<>();
        data.put("foundzone", profile.getdiscoveredzones());
        data.put("talkednpc", profile.getTalked_npcs());
        data.put("floor6", profile.getTotalfloor6run());
        data.put("sadan", profile.getSadancollections());
        setUserProperty("data", data);
        Map<String, Object> questData = new HashMap<>();
        questData.put("completedQuests", profile.getCompletedQuests());
        questData.put("completedObjectives", profile.getCompletedObjectives());
        setUserProperty("quests", questData);
        Map<String, Integer> tempQuiv = new HashMap<>();
        profile.getQuiver().forEach((key, value) -> tempQuiv.put(key.name(), value));
        setUserProperty("quiver", tempQuiv);
        List<Document> effectsDocuments = new ArrayList<>();
        for (ActivePotionEffect effect : profile.getEffects()) {
            Document effectDocument = new Document()
                    .append("key", effect.getEffect().getType().getNamespace())
                    .append("level", effect.getEffect().getLevel())
                    .append("duration", effect.getEffect().getDuration())
                    .append("remaining", effect.getRemaining());
            effectsDocuments.add(effectDocument);
        }

        profile.saveCookie();
        setUserProperty("effects", effectsDocuments);
        setUserProperty("skillFarmingXp", profile.farmingXP);
        setUserProperty("skillMiningXp", profile.miningXP);
        setUserProperty("skillCombatXp", profile.combatXP);
        setUserProperty("skillForagingXp", profile.foragingXP);
        setUserProperty("slayerRevenantHorrorHighest", profile.highestSlayers[0]);
        setUserProperty("slayerTarantulaBroodfatherHighest", profile.highestSlayers[1]);
        setUserProperty("slayerSvenPackmasterHighest", profile.highestSlayers[2]);
        setUserProperty("slayerVoidgloomSeraphHighest", profile.highestSlayers[3]);
        setUserProperty("permanentCoins", profile.isPermanentCoins());
        setUserProperty("xpSlayerRevenantHorror", profile.slayerXP[0]);
        setUserProperty("xpSlayerTarantulaBroodfather", profile.slayerXP[1]);
        setUserProperty("xpSlayerSvenPackmaster", profile.slayerXP[2]);
        setUserProperty("xpSlayerVoidgloomSeraph", profile.slayerXP[3]);
        if (profile.getSlayerQuest() != null)
            setUserProperty("slayerQuest", profile.getSlayerQuest().serialize());
        if (!profile.pets.isEmpty()) {
            List<Map<String, Object>> petsSerialized = profile.pets.stream().map(pet -> pet.serialize()).collect(Collectors.toList());
            setUserProperty("pets", petsSerialized);
        } else {
            setUserProperty("pets", new ArrayList<Map<String, Object>>());
        }
        setUserProperty("auctionSettings", profile.auctionSettings.serialize());

        //setUserProperty("created", profile.created);
        setUserProperty("auctionCreationBIN", profile.isAuctionCreationBIN());
        SUtil.runAsync(() -> saveProfileData(db));
       // User.USER_CACHE.remove(profile.getUuid());
    }

    public void setUserProperty(String key, Object value) {
        userCache.put(key, value);
    }

    public Document grabProfile(String id) {
        Document query = new Document("_id", id);
        MongoCollection<Document> collection = DatabaseManager.getCollection("users");

        Document foundOrNot = collection.find(query).first();

        if (foundOrNot == null) {
            collection.insertOne(new Document("_id", id));
            foundOrNot = collection.find(query).first();
        }

        return foundOrNot;
    }

    public List<String> getStringList(Document base, String key, List<String> def) {
        return Collections.singletonList(get(base, key, def).toString());
    }

    public Object get(Document base, String key, Object def) {
        if (base.get(key) != null) {
            return base.get(key);
        }
        return def;
    }

    public String getString(Document base, String key, Object def) {
        return get(base, key, def).toString();
    }

    public int getInt(Document base, String key, Object def) {
        return (int) get(base, key, def);
    }

    public boolean getBoolean(Document base, String key, Object def) {
        return Boolean.parseBoolean(get(base, key, def).toString());
    }

    public double getDouble(Document base, String key, Object def) {
        return Double.parseDouble(getString(base, key, def));
    }

    public long getLong(Document base, String key, Object def) {
        if (def.equals(0.0)) {
            def = 0L;
        }
        return Long.parseLong(getString(base, key, def));
    }

    public void saveProfileData(UserDatabase db) {
        Document query = new Document("_id", db.id);
        Document found = UserDatabase.collection.find(query).first();
        if (found != null) {
            SLog.info("Cache size : " + userCache.size());
            Document updated = new Document(found);
            userCache.forEach(updated::append);
            for (Map.Entry<String, Object> entry : userCache.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            UserDatabase.collection.replaceOne(found, updated);
            System.out.println("updating old profile data");
            return;
        }
        Document New = new Document("_id", db.id);
        userCache.forEach(New::append);
        System.out.println("saving new profile data!");
        UserDatabase.collection.insertOne(New);


    }
}