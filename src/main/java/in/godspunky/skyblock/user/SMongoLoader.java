package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import in.godspunky.skyblock.auction.AuctionEscrow;
import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.item.pet.Pet;
import in.godspunky.skyblock.potion.ActivePotionEffect;
import in.godspunky.skyblock.potion.PotionEffect;
import in.godspunky.skyblock.potion.PotionEffectType;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.slayer.SlayerBossType;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.util.SLog;
import in.godspunky.skyblock.util.SUtil;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SMongoLoader
{
    public void load(UUID uuid) {
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
        user.setSelectedProfile(newProfile);
        user.profiles = new HashMap<>();
        user.profiles.put(newProfile.uuid, true);
        User.USER_CACHE.put(uuid, user);
        Profile.USER_CACHE.put(newProfileUUID.toString(), newProfile);
        grabProfile(newProfileUUID.toString());
        save(uuid);
        player.sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
        player.sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + name);
    }
    public void create(UUID uuid) {
        User user = User.getUser(uuid);
        Player player = Bukkit.getPlayer(uuid);
        List<Profile> profiles = new ArrayList<>();
        if (!user.profiles.isEmpty()){
            for (Profile oldProfile : user.getProfiles()){
                if (!profiles.contains(oldProfile))
                    profiles.add(oldProfile);
            }
        }
        UUID newProfileUUID = UUID.randomUUID();
        String name = SUtil.generateRandomProfileNameFor();
        Profile newProfile = new Profile(newProfileUUID.toString(), uuid, name);
        user.setSelectedProfile(newProfile);
        user.profiles = new HashMap<>();
        user.profiles.put(newProfile.uuid, true);
        User.USER_CACHE.put(uuid, user);
        Profile.USER_CACHE.put(newProfileUUID.toString(), newProfile);
        for (Profile profile : profiles){
            user.addProfile(profile , false);
        }
        user.addProfile(newProfile ,true);
        grabProfile(newProfileUUID.toString());
        save(uuid);
        loadProfile(user.selectedProfile);
        try {
            user.loadPlayerData(user.selectedProfile);
            user.loadCookieStatus(user.selectedProfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.sendMessage(ChatColor.YELLOW + "Welcome to " + ChatColor.GREEN + "Godspunky Skyblock!");
        player.sendMessage(ChatColor.AQUA + "You are playing on profile: " + ChatColor.YELLOW + name);
    }


    public String getActiveProfile(UUID uuid) {
        Document document = grabProfile(uuid.toString());

        assert document != null;
        return document.getString("name");
    }

    @SneakyThrows
    public  void save(UUID uuid) {
        User user = User.getUser(uuid);
        Profile selectedProfile = user.selectedProfile;
        UserDatabase db = new UserDatabase(uuid.toString(), false);

        // todo fix it
        if (db.exists()) {
            SMongoLoader loader = new SMongoLoader(uuid.toString());
            db.getDocument().forEach(loader::setUserProperty);
        }


        setUserProperty(uuid.toString(),"selectedProfile", selectedProfile == null ? null : selectedProfile.getUuid().toString());
        setUserProperty(uuid.toString(),"profiles", user.profiles);

        SUtil.runAsync(() -> saveUserData(uuid));

        if (selectedProfile == null) {
            selectedProfile = new Profile(UUID.randomUUID().toString(), uuid, "$temp");
            user.profiles = new HashMap<>();
            user.profiles.put(selectedProfile.getId().toString(), true);
        }

        selectedProfile = user.selectedProfile;
        if (selectedProfile == null){
            SLog.info("Something went wrong while saving data for : " + uuid);
            return;
        }
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
        user.saveAllVanillaInstances(selectedProfile);
        user.saveCookie(selectedProfile);
        saveProfile(selectedProfile);



    }



    public  void loadProfile(Profile profile) {
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

    public void saveProfile(Profile profile) {
        SLog.info("Save Profile is getting called");
        String uuid = profile.owner.toString();
        setProfileProperty(uuid ,"owner", profile.owner.toString());

        setProfileProperty(uuid, "name", profile.name);
        Map<String, Integer> tempColl = new HashMap<>();
        for (ItemCollection collection : ItemCollection.getCollections()) {
            tempColl.put(collection.getIdentifier(), profile.getCollection(collection));
        }
        setProfileProperty(uuid ,"collections", tempColl);
        setProfileProperty(uuid,"coins", profile.getCoins());
        setProfileProperty(uuid,"bankCoins", profile.getBankCoins());
        if (profile.getLastRegion() != null)
            setProfileProperty(uuid,"lastRegion", profile.getLastRegion().getName());
        Map<String, Integer> tempQuiv = new HashMap<>();
        profile.getQuiver().forEach((key, value) -> tempQuiv.put(key.name(), value));
        setProfileProperty(uuid,"quiver", tempQuiv);
        List<Document> effectsDocuments = new ArrayList<>();
        for (ActivePotionEffect effect : profile.getEffects()) {
            Document effectDocument = new Document()
                    .append("key", effect.getEffect().getType().getNamespace())
                    .append("level", effect.getEffect().getLevel())
                    .append("duration", effect.getEffect().getDuration())
                    .append("remaining", effect.getRemaining());
            effectsDocuments.add(effectDocument);
        }
        setProfileProperty(uuid,"effects", effectsDocuments);
        setProfileProperty(uuid,"skillFarmingXp", profile.farmingXP);
        setProfileProperty(uuid,"skillMiningXp", profile.miningXP);
        setProfileProperty(uuid,"skillCombatXp", profile.combatXP);
        setProfileProperty(uuid,"skillForagingXp", profile.foragingXP);
        setProfileProperty(uuid,"slayerRevenantHorrorHighest", profile.highestSlayers[0]);
        setProfileProperty(uuid,"slayerTarantulaBroodfatherHighest", profile.highestSlayers[1]);
        setProfileProperty(uuid,"slayerSvenPackmasterHighest", profile.highestSlayers[2]);
        setProfileProperty(uuid,"slayerVoidgloomSeraphHighest", profile.highestSlayers[3]);
        setProfileProperty(uuid,"permanentCoins", profile.isPermanentCoins());
        setProfileProperty(uuid,"xpSlayerRevenantHorror", profile.slayerXP[0]);
        setProfileProperty(uuid,"xpSlayerTarantulaBroodfather", profile.slayerXP[1]);
        setProfileProperty(uuid,"xpSlayerSvenPackmaster", profile.slayerXP[2]);
        setProfileProperty(uuid,"xpSlayerVoidgloomSeraph", profile.slayerXP[3]);
        if (profile.getSlayerQuest() != null)
            setProfileProperty(uuid,"slayerQuest", profile.getSlayerQuest().serialize());
        if (!profile.pets.isEmpty()) {
            List<Map<String, Object>> petsSerialized = profile.pets.stream().map(pet -> pet.serialize()).collect(Collectors.toList());
            setProfileProperty(uuid,"pets", petsSerialized);
        } else {
            setProfileProperty(uuid,"pets", new ArrayList<Map<String, Object>>());
        }
        setProfileProperty(uuid,"auctionSettings", profile.auctionSettings.serialize());
        //setProfileProperty("created", profile.created);
        setProfileProperty(uuid,"auctionCreationBIN", profile.isAuctionCreationBIN());
        setProfileProperty(uuid,"selected", profile.isSelected());
        SUtil.runAsync(() -> saveProfileData(UUID.fromString(profile.uuid) , profile.owner.toString()));
    }



    public static Map<String, ProfileDataHolder> profileCache = new HashMap<>();
    public static Map<String, UserDataHolder> userCache = new HashMap<>();
    public String cachedUserId;
    public String cachedProfileId;

    public SMongoLoader() {

    }
    public SMongoLoader(String uuid){
        this.cachedUserId = uuid;
    }
    public void setUserProperty(String key , Object value){
        setUserProperty(cachedUserId , key , value);
    }

    public void setProfileProperty(String uuid ,String key, Object value) {
            if (value != null) {
                if (!profileCache.containsKey(uuid)){
                    profileCache.put(uuid , ProfileDataHolder.getDataHolder(uuid));
                }
                profileCache.get(uuid).addData(key , value);
            }
        }

    public void setUserProperty(String uuid,String key, Object value) {
            if (value != null) {
                if (!userCache.containsKey(uuid)){
                    userCache.put(uuid , UserDataHolder.getDataHolder(uuid));
                }
               userCache.get(uuid).addData(key , value);
            }
        }


    public Document grabUser(String id) {
        Document query = new Document("_id", id);
        Document foundOrNot = DatabaseManager.getCollection("users").find(query).first();
        if (foundOrNot == null) {
            SUtil.runAsync(() -> DatabaseManager.getCollection("users").insertOne(new Document("_id", id)));
        }
        return foundOrNot;
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

    public  void saveProfileData(UUID uuid , String owner) {
        if (owner == null) return;
        if (uuid == null) return;
            Document found = grabProfile(uuid.toString());
            if (found != null) {
                Document updated = new Document(found);
                for (Map.Entry<String, ProfileDataHolder> data : profileCache.entrySet()) {
                    if (data.getKey().equals(owner)) {
                        data.getValue().getDATA_CACHE().forEach(updated::append);
                        System.out.println("cache size : " + data.getValue().getDATA_CACHE().size());
                        DatabaseManager.getCollection("profiles").replaceOne(found, updated);
                        System.out.println("saving data for : " + owner);
                        profileCache.remove(owner);
                    }
                }
                SLog.info("Updating profile data : " + owner);
            }
        }


    public  void saveUserData(UUID uuid) {

            Document found = grabUser(uuid.toString());
            Document updated = new Document();
            if (found != null) {
                for (Map.Entry<String , UserDataHolder> data : userCache.entrySet()){
                    if (data.getKey().equals(uuid.toString())){
                        data.getValue().getDATA_CACHE().forEach(updated::append);
                        break;
                    }
                }
                DatabaseManager.getCollection("users").replaceOne(found, updated);
                userCache.remove(uuid.toString());
            }
        }
}