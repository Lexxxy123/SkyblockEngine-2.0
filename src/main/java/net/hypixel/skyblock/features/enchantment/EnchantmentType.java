// 
// Decompiled by Procyon v0.5.36
// 

package net.hypixel.skyblock.features.enchantment;


import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

import java.util.*;


public class EnchantmentType {

    public static final Map<String, EnchantmentType> ENCHANTMENT_TYPE_CACHE;

    public static List<EnchantmentType> ENCHANTMENT_TYPE_LIST = new ArrayList<>();
    public static EnchantmentType SHARPNESS;
    public static EnchantmentType LIFE_STEAL;
    public static EnchantmentType EXECUTE;
    public static EnchantmentType FIRE_ASPECT;
    public static EnchantmentType PROTECTION;
    public static EnchantmentType GROWTH;
    public static EnchantmentType AIMING;
    public static EnchantmentType POWER;
    public static EnchantmentType FLAME;
    public static EnchantmentType ENDER_SLAYER;
    public static EnchantmentType DRAGON_HUNTER;
    public static EnchantmentType EFFICIENCY;
    public static EnchantmentType KNOCKBACK;
    public static EnchantmentType AQUA_INFINITY;
    public static EnchantmentType VAMPIRISM;
    public static EnchantmentType FIRST_STRIKE;
    public static EnchantmentType VICIOUS;
    public static EnchantmentType SMITE;
    public static EnchantmentType BANE_OF_ARTHROPODS;
    public static EnchantmentType CRITICAL;
    public static EnchantmentType FATAL_TEMPO;
    public static EnchantmentType HARVESTING;
    public static EnchantmentType TELEKINESIS;
    public static EnchantmentType ULTIMATE_WISE;
    public static EnchantmentType LUCKINESS;
    public static EnchantmentType SOUL_EATER;
    public static EnchantmentType CHIMERA;
    public static EnchantmentType LEGION;
    public static EnchantmentType ONE_FOR_ALL;
    private final String name;
    private final String namespace;
    private final String description;
    private final boolean ultimate;
    private final Enchantment vanilla;
    private final List<SpecificItemType> compatibleTypes;
    public final int maxLvl;


    public EnchantmentType(String name, String namespace, String description, boolean ultimate, Enchantment vanilla, int maxLvl, SpecificItemType... compatibleTypes) {
        this.name = name;
        this.namespace = namespace;
        this.description = description;
        this.ultimate = ultimate;
        this.vanilla = vanilla;
        this.maxLvl = maxLvl;
        this.compatibleTypes = new ArrayList<>(Arrays.asList(compatibleTypes));
        ENCHANTMENT_TYPE_CACHE.put(namespace, this);
        ENCHANTMENT_TYPE_LIST.add(this);
    }

    public EnchantmentType(String name, String namespace, String description, boolean ultimate, int maxLvl, SpecificItemType... compatibleTypes) {
        this(name, namespace, description, ultimate, null, maxLvl, compatibleTypes);
    }

    public EnchantmentType(String name, String namespace, String description, Enchantment vanilla, int maxLvl, SpecificItemType... compatibleTypes) {
        this(name, namespace, description, false, vanilla, maxLvl, compatibleTypes);
    }

    public EnchantmentType(String name, String namespace, String description, int maxLvl, SpecificItemType... compatibleTypes) {
        this(name, namespace, description, false, maxLvl, compatibleTypes);
    }

    public static EnchantmentType getByNamespace(String namespace) {
        return EnchantmentType.ENCHANTMENT_TYPE_CACHE.get(namespace.toLowerCase());
    }

    public String getDescription(Object... objects) {
        String description = this.description;
        for (Object object : objects) {
            description = description.replaceFirst("%s", String.valueOf(object));
        }
        return description;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EnchantmentType && ((EnchantmentType) o).namespace.equals(this.namespace);
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public boolean isUltimate() {
        return this.ultimate;
    }

    public Enchantment getVanilla() {
        return this.vanilla;
    }

    public List<SpecificItemType> getCompatibleTypes() {
        return this.compatibleTypes;
    }


    static {
        ENCHANTMENT_TYPE_CACHE = new HashMap<String, EnchantmentType>();
        SHARPNESS = new EnchantmentType("Sharpness", "sharpness", "Increases damage dealt by " + ChatColor.GREEN + "%s%", 400, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE); // you do gui till i am doing maxLvl
        LIFE_STEAL = new EnchantmentType("Life Steal", "life_steal", "Heals for " + ChatColor.GREEN + "%s%" + ChatColor.GRAY + " of your max health each time you hit a mob.", 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        EXECUTE = new EnchantmentType("Execute", "execute", "Increases damage by " + ChatColor.GREEN + "%s%" + Sputnik.trans(" &7for each percent of Health missing on your target. "), 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        FIRE_ASPECT = new EnchantmentType("Fire Aspect", "fire_aspect", "Gives whoever this weapon hits %s seconds of fire.", 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        PROTECTION = new EnchantmentType("Protection", "protection", "Grants " + ChatColor.GREEN + "+%s \u2748 Defense" + ChatColor.GRAY + ".", 1500, SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS, SpecificItemType.BOOTS);
        GROWTH = new EnchantmentType("Growth", "growth", "Grants " + ChatColor.GREEN + "+%s " + ChatColor.RED + "\u2764 " + ChatColor.RED + "Health" + ChatColor.GRAY + ".", 1500, SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS, SpecificItemType.BOOTS);
        AIMING = new EnchantmentType("Aiming", "aiming", "Arrows home towards nearby mobs if they are within %s blocks.", 30, SpecificItemType.BOW);
        POWER = new EnchantmentType("Power", "power", "Increases bow damage by " + ChatColor.GREEN + "%s%", 320, SpecificItemType.BOW);
        FLAME = new EnchantmentType("Flame", "flame", "Arrow ignites target for 3 seconds, dealing 5 damage every second.", 30, SpecificItemType.BOW);
        ENDER_SLAYER = new EnchantmentType("Ender Slayer", "ender_slayer", "Increases damage dealt to Ender Dragons and Endermen by " + ChatColor.GREEN + "%s% " + ChatColor.GRAY, 250, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        DRAGON_HUNTER = new EnchantmentType("Dragon Hunter", "dragon_hunter", "Increases damage dealt to Ender Dragons by " + ChatColor.GREEN + "%s% " + ChatColor.GRAY, 250, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE, SpecificItemType.BOW);
        EFFICIENCY = new EnchantmentType("Efficiency", "efficiency", "Reduces the time in takes to mine.", Enchantment.DIG_SPEED, 5, SpecificItemType.AXE, SpecificItemType.PICKAXE, SpecificItemType.SHOVEL);
        KNOCKBACK = new EnchantmentType("Knockback", "knockback", Sputnik.trans("Increases knockback by &a%s&7 blocks."), Enchantment.KNOCKBACK, 2, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        AQUA_INFINITY = new EnchantmentType("Aqua Infinity", "aqua_infinity", Sputnik.trans("Increases underwater mining rate to normal level mining rate."), Enchantment.WATER_WORKER, 5, SpecificItemType.HELMET);
        VAMPIRISM = new EnchantmentType("Vampirism", "vampirism", Sputnik.trans("Heals for &a%s% &7of your missing Health per level whenever you kill an enemy."), 10, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        FIRST_STRIKE = new EnchantmentType("First Strike", "first_strike", Sputnik.trans("Increases the first melee damage dealt to a mob by &a%s%"), 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        VICIOUS = new EnchantmentType("Vicious", "vicious", Sputnik.trans("Grant &c+%s\u2afd Ferocity"), 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE, SpecificItemType.BOW);
        SMITE = new EnchantmentType("Smite", "smite", "Increases damage dealt to Zombies, Zombie Pigmen, Withers, Wither Skeletons, and Skeletons by " + ChatColor.GREEN + "%s% " + ChatColor.GRAY, 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        BANE_OF_ARTHROPODS = new EnchantmentType("Bane of Arthropods", "bane_of_arthropods", "Increases damage dealt to Cave Spiders, Spiders, and Silverfish by " + ChatColor.GREEN + "%s% " + ChatColor.GRAY, 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        CRITICAL = new EnchantmentType("Critical", "critical", "Increases " + ChatColor.BLUE + "\u2620 Crit Damage " + ChatColor.GRAY + "by " + ChatColor.GREEN + "%s%" + ChatColor.GRAY + ".", 30, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        FATAL_TEMPO = new EnchantmentType("Fatal Tempo", "fatal_tempo", Sputnik.trans("&7Attack increases your &c\u2afd &cFerocity &7by &c%s% &7per hit, capped at &c200% &7for 3 seconds after your &efirst &eattack &7that triggers the enchantment."), true, 10, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
        HARVESTING = new EnchantmentType("Harvesting", "harvesting", "Increases the chance for crops to drop double the amount of items by " + ChatColor.GREEN + "%s%" + ChatColor.GRAY + ".", 5, SpecificItemType.HOE);
        TELEKINESIS = new EnchantmentType("Telekinesis", "telekinesis", "Blocks and mob drops go directly into your inventory.", 1, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.BOW, SpecificItemType.AXE);
        ULTIMATE_WISE = new EnchantmentType("Ultimate Wise", "ultimate_wise", "Reduces the Mana Cost of this item's ability by " + ChatColor.GREEN + "%s%" + ChatColor.GRAY + ".", true, 20, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.SHOVEL, SpecificItemType.SHEARS, SpecificItemType.PICKAXE, SpecificItemType.BOW, SpecificItemType.AXE, SpecificItemType.ROD, SpecificItemType.HOE, SpecificItemType.WAND);
        LUCKINESS = new EnchantmentType("Luckiness", "luckiness", Sputnik.trans("&7Grant &b+%s \u272f Magic Find"), 10, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.SHOVEL, SpecificItemType.SHEARS, SpecificItemType.PICKAXE, SpecificItemType.BOW, SpecificItemType.AXE, SpecificItemType.ROD, SpecificItemType.HOE, SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS, SpecificItemType.BOOTS);
        SOUL_EATER = new EnchantmentType("Soul Eater", "soul_eater", Sputnik.trans("Your weapon gains &c%sx&7 damage of the latest monster killed and applies it on your next hit."), true, 20, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE, SpecificItemType.BOW);
        CHIMERA = new EnchantmentType("Chimera", "chimera", Sputnik.trans("Copies &a%s% &7of your active pet's stats."), true, 20, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE, SpecificItemType.BOW);
        LEGION = new EnchantmentType("Legion", "legion", Sputnik.trans("Increases most of your player stats by &e+%s% &7per player per level within &b30 &7blocks of you, up to &a20 &7players."), true, 20, SpecificItemType.HELMET, SpecificItemType.CHESTPLATE, SpecificItemType.LEGGINGS, SpecificItemType.BOOTS);
        ONE_FOR_ALL = new EnchantmentType("One for All", "one_for_all", Sputnik.trans("Removes all other enchants but increases your weapon damage by &a%s%"), true, 20, SpecificItemType.SWORD, SpecificItemType.LONGSWORD, SpecificItemType.AXE);
    }
}