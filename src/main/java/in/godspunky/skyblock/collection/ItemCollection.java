package in.godspunky.skyblock.collection;

import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ItemCollection {
    private static final Map<String, ItemCollection> COLLECTION_MAP = new HashMap<>();

    public static final ItemCollection WHEAT = new ItemCollection("Wheat",
            ItemCollectionCategory.FARMING,
            SMaterial.WHEAT,
            (short) 0,
            new ItemCollectionRewards(50, new ItemCollectionRecipeReward(SMaterial.ENCHANTED_BOOK)),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(15000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000));

    public static final ItemCollection BLAZE_ROD = new ItemCollection("Blaze Rod",
            ItemCollectionCategory.COMBAT,
            SMaterial.BLAZE_ROD,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(15000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000));

    public static final ItemCollection CACTUS = new ItemCollection("Cactus",
            ItemCollectionCategory.FARMING,
            SMaterial.CACTUS,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection ENDER_PEARL = new ItemCollection("Ender Pearl",
            ItemCollectionCategory.COMBAT,
            SMaterial.ENDER_PEARL,
            (short) 0,
            new ItemCollectionRewards(50, new ItemCollectionRecipeReward(SMaterial.ENCHANTED_ENDER_PEARL)),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(15000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000, new ItemCollectionRecipeReward(SMaterial.ASPECT_OF_THE_END)),
            new ItemCollectionRewards(100000));


    public static final ItemCollection CARROT = new ItemCollection("Carrot",
            ItemCollectionCategory.FARMING,
            SMaterial.CARROT_ITEM,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1700),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection FEATHER = new ItemCollection("Feather",
            ItemCollectionCategory.FARMING,
            SMaterial.FEATHER,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(200),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1700),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection LEATHER = new ItemCollection("Leather",
            ItemCollectionCategory.FARMING,
            SMaterial.LEATHER,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(200),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1700),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection NETHER_WART = new ItemCollection("Nether Wart",
            ItemCollectionCategory.FARMING,
            SMaterial.NETHER_WARTS,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(200),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1700),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection POTATO = new ItemCollection("Potato",
            ItemCollectionCategory.FARMING,
            SMaterial.POTATO_ITEM,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(200),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1700),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));


    public static final ItemCollection COCOA = new ItemCollection("Cocoa Beans",
            ItemCollectionCategory.FARMING,
            SMaterial.COCOA_BEANS,
            (short) 3,
            new ItemCollectionRewards(75),
            new ItemCollectionRewards(200),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(2000),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(20000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection SEEDS = new ItemCollection("Seeds",
            ItemCollectionCategory.FARMING,
            SMaterial.SEEDS,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000));

    public static final ItemCollection CANE = new ItemCollection("Sugar Cane",
            ItemCollectionCategory.FARMING,
            SMaterial.SUGAR_CANE,
            (short) 0,
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2000),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(20000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection COAL = new ItemCollection("Coal",
            ItemCollectionCategory.MINING,
            SMaterial.COAL,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(1000000));

    public static final ItemCollection COBBLE = new ItemCollection("Cobblestone",
            ItemCollectionCategory.MINING,
            SMaterial.COBBLESTONE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(40000),
            new ItemCollectionRewards(70000));

    public static final ItemCollection GOLD_INGOT = new ItemCollection("Gold Ingot",
            ItemCollectionCategory.MINING,
            SMaterial.GOLD_INGOT,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection LAPIS_LAZULI = new ItemCollection("Lapis Lazuli",
            ItemCollectionCategory.MINING,
            SMaterial.LAPIS_LAZULI,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection SAND = new ItemCollection("Sand",
            ItemCollectionCategory.MINING,
            SMaterial.SAND,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection END_STONE = new ItemCollection("End Stone",
            ItemCollectionCategory.MINING,
            SMaterial.END_STONE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection GLOWSTONE = new ItemCollection("Glowstone Dust",
            ItemCollectionCategory.MINING,
            SMaterial.GLOWSTONE_DUST,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection GRAVEL = new ItemCollection("Gravel",
            ItemCollectionCategory.MINING,
            SMaterial.GRAVEL,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection ICE = new ItemCollection("Ice",
            ItemCollectionCategory.MINING,
            SMaterial.ICE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection NETHERRACK = new ItemCollection("Netherrack",
            ItemCollectionCategory.MINING,
            SMaterial.NETHERRACK,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection NETHER_QUARTZ = new ItemCollection("Nether Quartz",
            ItemCollectionCategory.MINING,
            SMaterial.NETHER_QUARTZ_ORE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(500),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000));

    public static final ItemCollection IRON_INGOT = new ItemCollection("Iron Ingot",
            ItemCollectionCategory.MINING,
            SMaterial.IRON_INGOT,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000),
            new ItemCollectionRewards(200000),
            new ItemCollectionRewards(400000));

    public static final ItemCollection DIAMOND = new ItemCollection("Diamond",
            ItemCollectionCategory.MINING,
            SMaterial.DIAMOND,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000),
            new ItemCollectionRewards(200000),
            new ItemCollectionRewards(400000),
            new ItemCollectionRewards(800000),
            new ItemCollectionRewards(1600000));

    public static final ItemCollection EMERALD = new ItemCollection("Emerald",
            ItemCollectionCategory.MINING,
            SMaterial.EMERALD,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000),
            new ItemCollectionRewards(200000),
            new ItemCollectionRewards(400000),
            new ItemCollectionRewards(800000),
            new ItemCollectionRewards(1600000));

    public static final ItemCollection REDSTONE = new ItemCollection("Redstone",
            ItemCollectionCategory.MINING,
            SMaterial.REDSTONE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000),
            new ItemCollectionRewards(100000));

    public static final ItemCollection OBSIDIAN = new ItemCollection("Obsidian",
            ItemCollectionCategory.MINING,
            SMaterial.OBSIDIAN,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250, new ItemCollectionUpgradeReward("Quiver", ChatColor.GREEN)),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection OAK_LOG = new ItemCollection("Oak Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.OAK_WOOD,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection DARK_OAK_LOG = new ItemCollection("Dark Oak Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.DARK_OAK_WOOD,
            (short) 1,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection SPRUCE_LOG = new ItemCollection("Spruce Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.SPRUCE_WOOD,
            (short) 1,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection BIRCH_LOG = new ItemCollection("Birch Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.BIRCH_WOOD,
            (short) 2,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection ACACIA_LOG = new ItemCollection("Acacia Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.ACACIA_WOOD,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection JUNGLE_LOG = new ItemCollection("Jungle Wood",
            ItemCollectionCategory.FORAGING,
            SMaterial.JUNGLE_WOOD,
            (short) 3,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection STRING = new ItemCollection("String",
            ItemCollectionCategory.COMBAT,
            SMaterial.STRING,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250, new ItemCollectionUpgradeReward("Quiver", ChatColor.GREEN)),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection BONE = new ItemCollection("Bone",
            ItemCollectionCategory.COMBAT,
            SMaterial.BONE,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection ROTTEN_FLESH = new ItemCollection("Rotten Flesh",
            ItemCollectionCategory.COMBAT,
            SMaterial.ROTTEN_FLESH,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    public static final ItemCollection GUNPOWDER = new ItemCollection("Gunpowder",
            ItemCollectionCategory.COMBAT,
            SMaterial.GUNPOWDER,
            (short) 0,
            new ItemCollectionRewards(50),
            new ItemCollectionRewards(100),
            new ItemCollectionRewards(250),
            new ItemCollectionRewards(1000),
            new ItemCollectionRewards(2500),
            new ItemCollectionRewards(5000),
            new ItemCollectionRewards(10000),
            new ItemCollectionRewards(25000),
            new ItemCollectionRewards(50000));

    private final String name;
    private final String identifier;
    private final ItemCollectionCategory category;
    private final SMaterial material;
    private final short data;
    private final List<ItemCollectionRewards> rewards;
    private final boolean temporary;

    private ItemCollection(final String name, final ItemCollectionCategory category, final SMaterial material, final short data, final ItemCollectionRewards... rewards) {
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll(" ", "_");
        this.category = category;
        this.material = material;
        this.data = data;
        this.rewards = new ArrayList<ItemCollectionRewards>(Arrays.asList(rewards));
        this.temporary = false;
        ItemCollection.COLLECTION_MAP.put(this.identifier, this);
    }

    private ItemCollection(final String name, final ItemCollectionCategory category, final SMaterial material, final short data, final int size) {
        this.name = name;
        this.identifier = name.toLowerCase().replaceAll(" ", "_");
        this.category = category;
        this.material = material;
        this.data = data;
        this.rewards = new ArrayList<ItemCollectionRewards>();
        for (int i = 0; i < size; ++i) {
            this.rewards.add(null);
        }
        this.temporary = true;
        ItemCollection.COLLECTION_MAP.put(this.identifier, this);
    }

    public static String[] getProgress(final Player player, final ItemCollectionCategory category) {
        final User user = User.getUser(player.getUniqueId());
        final AtomicInteger found = new AtomicInteger();
        final AtomicInteger completed = new AtomicInteger();
        final Collection<ItemCollection> collections = (category != null) ? getCategoryCollections(category) : getCollections();
        for (final ItemCollection collection : collections) {
            if (user.getCollection(collection) > 0) {
                found.incrementAndGet();
            }
            if (user.getCollection(collection) >= collection.getMaxAmount()) {
                completed.incrementAndGet();
            }
        }
        String title;
        String progress;
        if (collections.size() == found.get()) {
            title = SUtil.createProgressText("Collection Maxed Out", completed.get(), collections.size());
            progress = SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, completed.get(), collections.size());
        } else {
            title = SUtil.createProgressText("Collection Unlocked", found.get(), collections.size());
            progress = SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, found.get(), collections.size());
        }
        return new String[]{title, progress};
    }

    public static ItemCollection getByIdentifier(final String identifier) {
        return ItemCollection.COLLECTION_MAP.get(identifier.toLowerCase());
    }

    public static ItemCollection getByMaterial(final SMaterial material, final short data) {
        for (final ItemCollection collection : ItemCollection.COLLECTION_MAP.values()) {
            if (collection.material == material && collection.data == data) {
                return collection;
            }
        }
        return null;
    }

    public static Map<ItemCollection, Integer> getDefaultCollections() {
        final Map<ItemCollection, Integer> collections = new HashMap<ItemCollection, Integer>();
        for (final ItemCollection collection : ItemCollection.COLLECTION_MAP.values()) {
            collections.put(collection, 0);
        }
        return collections;
    }

    public static Collection<ItemCollection> getCollections() {
        return ItemCollection.COLLECTION_MAP.values();
    }

    public static List<ItemCollection> getCategoryCollections(final ItemCollectionCategory category) {
        return getCollections().stream().filter(collection -> collection.category == category).collect(Collectors.toList());
    }

    public int getMaxAmount() {
        if (this.rewards.size() == 0 || this.rewards.get(this.rewards.size() - 1) == null) {
            return 0;
        }
        return this.rewards.get(this.rewards.size() - 1).getRequirement();
    }

    public int getTierAmount() {
        return this.rewards.size();
    }

    public int getTier(final int amount) {
        int tier = 0;
        for (final ItemCollectionRewards reward : this.rewards) {
            if (reward == null) {
                continue;
            }
            if (reward.getRequirement() > amount) {
                break;
            }
            ++tier;
        }
        return tier;
    }

    public int getRequirementForTier(int tier) {
        if (--tier < 0 || tier > this.rewards.size() - 1) {
            return -1;
        }
        final ItemCollectionRewards reward = this.rewards.get(tier);
        if (reward == null) {
            return -1;
        }
        return reward.getRequirement();
    }

    public ItemCollectionRewards getRewardsFor(int tier) {
        if (--tier < 0 || tier > this.rewards.size()) {
            return null;
        }
        return this.rewards.get(tier);
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public ItemCollectionCategory getCategory() {
        return this.category;
    }

    public SMaterial getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }

    public List<ItemCollectionRewards> getRewards() {
        return this.rewards;
    }

    public boolean isTemporary() {
        return this.temporary;
    }
}
