package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.auction.AuctionEscrow;
import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.potion.ActivePotionEffect;
import in.godspunky.skyblock.potion.PotionEffect;
import in.godspunky.skyblock.potion.PotionEffectType;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.slayer.SlayerBossType;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.util.BukkitSerializeClass;
import in.godspunky.skyblock.util.SLog;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.SaveQueue;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class SMongoLoader {
    public static SaveQueue<SaveInfo> savingQueue = new SaveQueue<>();
    public Map<String, Object> profileCache;
    public Map<String, Object> userCache;
    public String cachedUserId;
    public String cachedProfileId;


    public SMongoLoader(UUID id) {
        profileCache = new HashMap<>();
        userCache = new HashMap<>();
        cachedUserId = id.toString();
    }


    public SMongoLoader() {
        profileCache = new HashMap<>();
        userCache = new HashMap<>();
    }

    public static void queue(SaveInfo i) {
        savingQueue.add(i);
    }

    public static void queue(String uuid, boolean soft) {
        savingQueue.add(new SaveInfo(uuid, soft));
    }

    public static void startQueueTask() {
        Skyblock.getPlugin().getServer().getScheduler().runTaskTimer(Skyblock.getPlugin(), () -> {
            if (!savingQueue.isEmpty()) {
                SaveInfo currentSave = savingQueue.dequeue();
                SLog.info("Saving data for :" + currentSave.getUuid());
                Skyblock.getPlugin().dataLoader.save(UUID.fromString(currentSave.getUuid()));
            }
        }, 1L, 1L);
    }

    public void load(UUID uuid) {
        User user = User.getUser(uuid);
        cachedUserId = uuid.toString();

        Document base = grabUser(uuid.toString());
        UUID selectedProfileUUID = base != null ? UUID.fromString(getString(base, "selectedProfile", null)) : null;

        if (selectedProfileUUID != null && SUtil.isUUID(selectedProfileUUID.toString())) {

            user.selectedProfile = Profile.get(selectedProfileUUID, uuid);

            if (!user.profiles.containsKey(user.selectedProfile.uuid)) {
                user.profiles.clear();
                user.profiles.put(user.selectedProfile.uuid, true);
            }

            User.USER_CACHE.put(uuid, user);
            user.selectedProfile.setSelected(true);

            if (user.selectedProfile == null) {
                createAndSaveNewProfile(uuid);
                Bukkit.getScheduler().runTaskLater(Skyblock.getPlugin(), () -> {
                    SkyblockIsland.getIsland(uuid).send();
                }, 5 * 20L); // 20 ticks per second, so 5 seconds = 5 * 20 ticks
                return;
            }

            if (user.profiles.isEmpty()) {
                user.profiles.put(user.selectedProfile.uuid, true);
            }
            loadProfile(user.selectedProfile);
            user.toBukkitPlayer().sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
            user.toBukkitPlayer().sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + getActiveProfile(UUID.fromString(user.selectedProfile.uuid)));
        } else {
            createAndSaveNewProfile(uuid);
            Bukkit.getScheduler().runTaskLater(Skyblock.getPlugin(), () -> {
                SkyblockIsland.getIsland(uuid).send();
            }, 5 * 20L);
        }
    }

    public void createAndSaveNewProfile(UUID uuid) {
        User user = User.getUser(uuid);
        UUID id = UUID.randomUUID();
        String name = SUtil.generateRandomProfileNameFor();
        Profile profile = new Profile(id.toString(), uuid, name);
        user.selectedProfile = profile;
        user.profiles = Collections.singletonMap(profile.uuid, true);

        User.USER_CACHE.put(uuid, user);
        Profile.USER_CACHE.put(profile.uuid, user.selectedProfile);

        grabProfile(id.toString());
        queue(uuid.toString(), true);
        user.toBukkitPlayer().sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
        user.toBukkitPlayer().sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + name);
    }

    public String getActiveProfile(UUID uuid) {
        Document document = grabProfile(uuid.toString());

        assert document != null;
        return document.getString("name");
    }

    @SneakyThrows
    public void save(UUID uuid) {
        User user = User.getUser(uuid);
        if (user == null) return;
        Profile selectedProfile = user.selectedProfile;
        UserDatabase db = new UserDatabase(uuid.toString(), false);

        if (db.exists()) db.getDocument().forEach(this::setUserProperty);


        setUserProperty("selectedProfile", selectedProfile == null ? null : selectedProfile.getUuid().toString());
        setUserProperty("profiles", user.profiles);

        SUtil.runAsync(() -> saveUserData(db));
        Document base = grabUser(uuid.toString());
        if (base != null && user.selectedProfile == null) {
            if (getString(base, "selectedProfile", null) != null) {
                user.selectedProfile = Profile.get(UUID.fromString(getString(base, "selectedProfile", null)), uuid);
            }
        }
        if (user.selectedProfile == null) {
            user.selectedProfile = new Profile(UUID.randomUUID().toString(), uuid, "$temp");
            user.profiles = new HashMap<>();
            user.profiles.put(user.selectedProfile.getId().toString(), true);
        }
        selectedProfile = user.selectedProfile;

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

        saveProfile(selectedProfile);

    }

    public void loadProfile(Profile profile) {
        System.out.println("using message at " + System.currentTimeMillis());
        User owner = User.getUser(profile.getOwner());
        Document base = grabProfile(profile.getUuid().toString());


        SUtil.runAsync(() -> {
            ItemStack[] inv;
            ItemStack[] arm;
            try {
                inv = BukkitSerializeClass.itemStackArrayFromBase64(getString(base, "inventory", ""));
                arm = BukkitSerializeClass.itemStackArrayFromBase64(getString(base, "armor", ""));
            } catch (Exception ex) {
                SLog.info("Something went wrong while loading Inventory and armor for : " + profile.uuid);
                inv = new ItemStack[0];
                arm = new ItemStack[0];
            }

            profile.inventory = Arrays.asList(inv);
            profile.armor = Arrays.asList(arm);
        });

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
            profile.setCompletedObjectives(getStringList(base, "completedObjectives", new ArrayList<>()));
            profile.setCompletedQuests(getStringList(base, "completedQuests", new ArrayList<>()));
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

        SUtil.runAsync(() -> {
            Profile.updateProfileName(profile.uuid);
            profile.setName(Profile.getProfileNames().get(profile.uuid));
        });

        owner.loadCookieStatus(profile);


        try {
            SUtil.runAsync(() -> profile.selected = owner.selectedProfile.getUuid().equals(profile.getUuid()));
        } catch (Exception e) {
        }

        Profile.USER_CACHE.put(profile.uuid, profile);
        User.USER_CACHE.put(owner.getUuid(), owner);
    }

    public void saveProfile(Profile profile) {
        ProfileDatabase db = new ProfileDatabase(profile.uuid, true);

        List<ItemStack> invList = profile.inventory.stream().map(e -> (ItemStack) e).collect(Collectors.toList());
        List<ItemStack> armList = profile.armor.stream().map(e -> (ItemStack) e).collect(Collectors.toList());
        ItemStack[] inv = invList.toArray(new ItemStack[0]);
        ItemStack[] arm = armList.toArray(new ItemStack[0]);

        setProfileProperty("owner", profile.owner.toString());
        setProfileProperty("inventory", BukkitSerializeClass.itemStackArrayToBase64(inv));
        setProfileProperty("armor", BukkitSerializeClass.itemStackArrayToBase64(arm));
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
        setProfileProperty("completedObjectives", profile.getCompletedObjectives());
        setProfileProperty("completedQuests", profile.getCompletedQuests());
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
        User.getUser(profile.owner).saveCookie(profile);
        setProfileProperty("effects", effectsDocuments);
        setProfileProperty("completedObjectives", new HashMap<>());
        setProfileProperty("completedQuests", new HashMap<>());
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
        SUtil.runAsync(() -> saveProfileData(db));
        Profile.USER_CACHE.remove(profile.uuid);
        User.USER_CACHE.remove(profile.owner);
    }

    public void setProfileProperty(String key, Object valye) {
        profileCache.put(key, valye);
    }

    public void setUserProperty(String key, Object value) {
        userCache.put(key, value);
    }

    public Document grabUser(String id) {
        Document query = new Document("_id", id);
        return UserDatabase.collection.find(query).first();
    }

    public Document grabProfile(String id) {
        Document query = new Document("_id", id);
        MongoCollection<Document> collection = DatabaseManager.getCollection("profiles");

        Document foundOrNot = collection.find(query).first();

        if (foundOrNot == null) {
            collection.insertOne(new Document("_id", id));
            foundOrNot = collection.find(query).first();
        }

        return foundOrNot;
    }

    public List<String> getStringList(Document base, String key, List<String> def) {
        Object value = get(base, key, def);
        if (value instanceof List) {
            return (List<String>) value;
        }
        return def;
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

    public void saveProfileData(ProfileDatabase db) {
        Document query = new Document("_id", db.id);
        Document found = ProfileDatabase.collection.find(query).first();
        if (found != null) {
            SLog.info("Cache size : " + profileCache.size());
            Document updated = new Document(found);
            profileCache.forEach(updated::append);
            for (Map.Entry<String, Object> entry : profileCache.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            ProfileDatabase.collection.replaceOne(found, updated);
            System.out.println("updating old profile data");
            return;
        }
        Document New = new Document("_id", db.id);
        profileCache.forEach(New::append);
        System.out.println("saving new profile data!");
        ProfileDatabase.collection.insertOne(New);


    }

    public void saveUserData(UserDatabase db) {
        if (db.exists()) {
            Document query = new Document("_id", db.id);
            Document found = UserDatabase.collection.find(query).first();

            Document updated = new Document();
            userCache.forEach(updated::append);

            assert found != null;
            UserDatabase.collection.replaceOne(found, updated);
            return;
        }
        Document query = new Document("_id", db.id);
        Document found = UserDatabase.collection.find(query).first();
        Document New = new Document("_id", db.id);
        userCache.forEach(New::append);
        if (found == null) {
            UserDatabase.collection.insertOne(New);
        }
    }
}