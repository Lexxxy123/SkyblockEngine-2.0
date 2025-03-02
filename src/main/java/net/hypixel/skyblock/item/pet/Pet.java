/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package net.hypixel.skyblock.item.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.ItemData;
import net.hypixel.skyblock.item.LoreableMaterialStatistics;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.pet.PetAbility;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class Pet
implements SkullStatistics,
LoreableMaterialStatistics,
MaterialFunction,
ItemData {
    protected static List<Integer> COMMON_XP_GOALS = Arrays.asList(0, 100, 210, 330, 460, 605, 765, 940, 1130, 1340, 1570, 1820, 2095, 2395, 2725, 3085, 3485, 3925, 4415, 4955, 5555, 6215, 6945, 7745, 8625, 9585, 10635, 11785, 13045, 14425, 15935, 17585, 19385, 21345, 23475, 25785, 28285, 30985, 33905, 37065, 40485, 44185, 48185, 52535, 57285, 62485, 68185, 74485, 81485, 89285, 97985, 107685, 118485, 130485, 143785, 158485, 174685, 192485, 211985, 233285, 256485, 281685, 309085, 338885, 371285, 406485, 444685, 486085, 530885, 579285, 631485, 687685, 748085, 812885, 882285, 956485, 1035685, 0x111881, 1211085, 1308285, 1412485, 1524185, 1643885, 1772085, 1909285, 2055985, 2212685, 2380385, 2560085, 2752785, 2959485, 3181185, 3418885, 3673585, 3946285, 4237985, 4549685, 4883385, 5241085, 5624785);
    protected static List<Integer> UNCOMMON_XP_GOALS = Arrays.asList(0, 175, 365, 575, 805, 1055, 1330, 1630, 1960, 2320, 2720, 3160, 3650, 4190, 4790, 5450, 6180, 6980, 7860, 8820, 9870, 11020, 12280, 13660, 15170, 16820, 18620, 20580, 22710, 25020, 27520, 30220, 33140, 36300, 39720, 43420, 47420, 51770, 56520, 61720, 67420, 73720, 80720, 88520, 97220, 106920, 117720, 129720, 143020, 157720, 173920, 191720, 211220, 232520, 255720, 280920, 308320, 338120, 370520, 405720, 443920, 485320, 530120, 578520, 630720, 686920, 747320, 812120, 881520, 955720, 1034920, 1119620, 1210320, 1307520, 1411720, 1523420, 1643120, 1771320, 1908520, 2055220, 2211920, 2379620, 2559320, 2752020, 2958720, 3180420, 3418120, 3672820, 3945520, 4237220, 4548920, 4882620, 5240320, 5624020, 6035720, 6477420, 6954120, 7470820, 8032520, 8644220);
    protected static List<Integer> RARE_XP_GOALS = Arrays.asList(0, 275, 575, 905, 1265, 1665, 2105, 2595, 3135, 3735, 4395, 5125, 5925, 6805, 7765, 8815, 9965, 11225, 12605, 14115, 15765, 17565, 19525, 21655, 23965, 26465, 29165, 32085, 35245, 38665, 42365, 46365, 50715, 55465, 60665, 66365, 72665, 79665, 87465, 96165, 105865, 116665, 128665, 141965, 156665, 172865, 190665, 210165, 231465, 254665, 279865, 307265, 337065, 369465, 404665, 442865, 484265, 529065, 577465, 629665, 685865, 746265, 811065, 880465, 954665, 1033865, 0x111165, 1209265, 1306465, 1410665, 1522365, 1642065, 1770265, 1907465, 2054165, 2210865, 2378565, 2558265, 2750965, 2957665, 3179365, 3417065, 3671765, 3944465, 4236165, 4547865, 4881565, 5239265, 0x55CCB5, 6034665, 6476365, 6953065, 7469765, 8031465, 8643165, 9309865, 0x992555, 10828265, 11689965, 12626665);
    protected static List<Integer> EPIC_XP_GOALS = Arrays.asList(0, 440, 930, 1470, 2070, 2730, 3460, 4260, 5140, 6100, 7150, 8300, 9560, 10940, 12450, 14100, 15900, 17860, 19990, 22300, 24800, 27500, 30420, 33580, 37000, 40700, 44700, 49050, 53800, 59000, 64700, 71000, 78000, 85800, 94500, 104200, 115000, 127000, 140300, 155000, 171200, 189000, 208500, 229800, 253000, 278200, 305600, 335400, 367800, 403000, 441200, 482600, 527400, 575800, 628000, 684200, 744600, 809400, 878800, 953000, 1032200, 1116900, 1207600, 1304800, 1409000, 1520700, 1640400, 1768600, 1905800, 2052500, 2209200, 0x2444C4, 2556600, 2749300, 2956000, 3177700, 3415400, 3670100, 3942800, 4234500, 4546200, 4879900, 5237600, 5621300, 6033000, 6474700, 6951400, 7468100, 8029800, 8641500, 9308200, 10034900, 10826600, 11688300, 12625000, 13641700, 14743400, 15935100, 17221800, 18608500);
    protected static List<Integer> LEGENDARY_XP_GOALS = Arrays.asList(0, 660, 1390, 2190, 3070, 4030, 5080, 6230, 7490, 8870, 10380, 12030, 13830, 15790, 17920, 20230, 22730, 25430, 28350, 31510, 34930, 38630, 42630, 46980, 51730, 56930, 62630, 68930, 75930, 83730, 92430, 102130, 112930, 124930, 138230, 152930, 169130, 186930, 206430, 227730, 250930, 276130, 303530, 333330, 365730, 400930, 439130, 480530, 525330, 573730, 625930, 682130, 742530, 807330, 876730, 950930, 1030130, 1114830, 1205530, 1302730, 1406930, 1518630, 1638330, 1766530, 1903730, 2050430, 2207130, 2374830, 2554530, 2747230, 2953930, 3175630, 3413330, 3668030, 3940730, 4232430, 4544130, 4877830, 5235530, 5619230, 6030930, 6472630, 6949330, 7466030, 8027730, 8639430, 9306130, 10032830, 10824530, 11686230, 12622930, 13639630, 14741330, 15933030, 17219730, 18606430, 20103130, 21719830, 0x1661222, 25353230);

    private static List<Integer> getGoalsForRarity(Rarity rarity) {
        List<Integer> goals = null;
        switch (rarity) {
            case COMMON: {
                goals = COMMON_XP_GOALS;
                break;
            }
            case UNCOMMON: {
                goals = UNCOMMON_XP_GOALS;
                break;
            }
            case RARE: {
                goals = RARE_XP_GOALS;
                break;
            }
            case EPIC: {
                goals = EPIC_XP_GOALS;
                break;
            }
            default: {
                goals = LEGENDARY_XP_GOALS;
            }
        }
        return goals;
    }

    public void runAbilities(Consumer<PetAbility> consumer, PetItem item) {
        if (item != null) {
            for (PetAbility ability : this.getPetAbilities(item.toItem())) {
                consumer.accept(ability);
            }
        }
    }

    public static int getLevel(double xp, Rarity rarity) {
        if (xp < 0.0) {
            return -1;
        }
        List<Integer> goals = Pet.getGoalsForRarity(rarity);
        for (int i2 = 0; i2 < goals.size(); ++i2) {
            if (!((double)goals.get(i2).intValue() > xp)) continue;
            return i2;
        }
        return 100;
    }

    private static double getXP(int level, Rarity rarity) {
        if (--level < 0 || level > 99) {
            return -1.0;
        }
        return Pet.getGoalsForRarity(rarity).get(level).intValue();
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setDouble("xp", 0.0);
        compound.setBoolean("equipped", false);
        return compound;
    }

    public abstract List<PetAbility> getPetAbilities(SItem var1);

    public abstract Skill getSkill();

    @Override
    public List<String> getCustomLore(SItem instance) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + this.getSkill().getName() + " Pet");
        if (this.hasStatBoosts()) {
            lore.add(" ");
        }
        int level = Pet.getLevel(instance);
        Pet.addPropertyInt("Magic Find", this.getPerMagicFind() * 100.0, lore, level);
        Pet.addPropertyPercent("Crit Damage", this.getPerCritDamage(), lore, level);
        Pet.addPropertyPercent("Crit Chance", this.getPerCritChance(), lore, level);
        double health = this.getPerHealth();
        if (health > 0.0) {
            lore.add(ChatColor.GRAY + "Health: " + ChatColor.GREEN + "+" + Math.round(health * (double)level) + " HP");
        }
        Pet.addPropertyInt("Strength", this.getPerStrength(), lore, level);
        Pet.addPropertyInt("Defense", this.getPerDefense(), lore, level);
        Pet.addPropertyInt("True Defense", this.getPerTrueDefense(), lore, level);
        Pet.addPropertyPercent("Speed", this.getPerSpeed(), lore, level);
        Pet.addPropertyInt("Intelligence", this.getPerIntelligence(), lore, level);
        Pet.addPropertyInt("Ferocity", this.getPerFerocity(), lore, level);
        Pet.addPropertyInt("Bonus Attack Speed", this.getPerAttackSpeed(), lore, level);
        List<PetAbility> abilities = this.getPetAbilities(instance);
        for (PetAbility ability : abilities) {
            lore.add(" ");
            lore.add(ChatColor.GOLD + ability.getName());
            for (String line : ability.getDescription(instance)) {
                lore.add(ChatColor.GRAY + line);
            }
        }
        if (level != 100) {
            lore.add(" ");
            double xp = instance.getData().getDouble("xp");
            int next = level + 1;
            double progress = xp - Pet.getXP(level, instance.getRarity());
            int goal = (int)(Pet.getXP(next, instance.getRarity()) - Pet.getXP(level, instance.getRarity()));
            lore.add(SUtil.createProgressText("Progress to Level " + next, progress, (double)goal));
            lore.add(SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, progress, (double)goal));
        } else if (level == 100) {
            lore.add("");
            lore.add(String.valueOf(ChatColor.AQUA) + ChatColor.BOLD + "MAX LEVEL");
        }
        if (!instance.getData().getBoolean("equipped")) {
            lore.add("");
            lore.add(ChatColor.YELLOW + "Right-click to add this pet to");
            lore.add(ChatColor.YELLOW + "your pet menu!");
        }
        if (instance.getType().getStatistics().displayRarity() && !instance.getData().getBoolean("equipped")) {
            SpecificItemType type = instance.getType().getStatistics().getSpecificType();
            lore.add(" ");
            lore.add((instance.isRecombobulated() ? instance.getRarity().getBoldedColor() + ChatColor.MAGIC + "D" + ChatColor.RESET + " " : "") + instance.getRarity().getDisplay() + (type != SpecificItemType.NONE ? " " + type.getName() : "") + (instance.isRecombobulated() ? instance.getRarity().getBoldedColor() + " " + ChatColor.MAGIC + "D" + ChatColor.RESET : ""));
        }
        return lore;
    }

    @Override
    public void onInstanceUpdate(SItem instance) {
        instance.setDisplayName(ChatColor.GRAY + "[Lvl " + Pet.getLevel(instance) + "] " + instance.getRarity().getColor() + this.getDisplayName());
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e2) {
        if (e2.getAction() != Action.RIGHT_CLICK_AIR && e2.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = e2.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SItem item = SItem.find(e2.getItem());
        user.addPet(item);
        player.setItemInHand(null);
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        player.sendMessage(ChatColor.GREEN + "Successfully added " + item.getRarity().getColor() + item.getType().getDisplayName(item.getType().getData()) + ChatColor.GREEN + " to your pet menu!");
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    public static int getLevel(SItem instance) {
        double xp = instance.getData().getDouble("xp");
        return Pet.getLevel(xp, instance.getRarity());
    }

    private static void addPropertyInt(String name, double value, List<String> lore, int level) {
        long fin = Math.round(value * (double)level);
        if (value != 0.0) {
            lore.add(ChatColor.GRAY + name + ": " + ChatColor.GREEN + (fin >= 0L ? "+" : "") + fin);
        }
    }

    private static void addPropertyPercent(String name, double value, List<String> lore, int level) {
        long fin = Math.round(value * 100.0 * (double)level);
        if (value != 0.0) {
            lore.add(ChatColor.GRAY + name + ": " + ChatColor.GREEN + (fin >= 0L ? "+" : "") + fin + "%");
        }
    }

    public double getPerHealth() {
        return 0.0;
    }

    public double getPerDefense() {
        return 0.0;
    }

    public double getPerStrength() {
        return 0.0;
    }

    public double getPerIntelligence() {
        return 0.0;
    }

    public double getPerSpeed() {
        return 0.0;
    }

    public double getPerCritChance() {
        return 0.0;
    }

    public double getPerCritDamage() {
        return 0.0;
    }

    public double getPerMagicFind() {
        return 0.0;
    }

    public double getPerTrueDefense() {
        return 0.0;
    }

    public double getPerFerocity() {
        return 0.0;
    }

    public double getPerAttackSpeed() {
        return 0.0;
    }

    public boolean hasStatBoosts() {
        return this.getPerHealth() != 0.0 || this.getPerDefense() != 0.0 || this.getPerStrength() != 0.0 || this.getPerIntelligence() != 0.0 || this.getPerSpeed() != 0.0 || this.getPerCritChance() != 0.0 || this.getPerCritDamage() != 0.0 || this.getPerMagicFind() != 0.0 || this.getPerTrueDefense() != 0.0;
    }

    public void particleBelowA(Player p2, Location l2) {
    }

    public static class PetItem
    implements ConfigurationSerializable {
        private final SMaterial type;
        private Rarity rarity;
        private double xp;
        private boolean active;

        private PetItem(SMaterial type, Rarity rarity, double xp, boolean active) {
            this.type = type;
            this.rarity = rarity;
            this.xp = xp;
            this.active = active;
        }

        public PetItem(SMaterial type, Rarity rarity, double xp) {
            this(type, rarity, xp, false);
        }

        public Map<String, Object> serialize() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("type", this.type.name());
            map.put("rarity", this.rarity.name());
            map.put("xp", this.xp);
            map.put("active", this.active);
            return map;
        }

        public Document toDocument() {
            Document document = new Document();
            document.append("type", this.type.name());
            document.append("rarity", this.rarity.name());
            document.append("xp", this.xp);
            document.append("active", this.active);
            return document;
        }

        public boolean equals(Object o2) {
            if (!(o2 instanceof PetItem)) {
                return false;
            }
            PetItem pet = (PetItem)o2;
            return this.type == pet.type && this.rarity == pet.rarity && this.xp == pet.xp && this.active == pet.active;
        }

        public boolean equalsItem(SItem item) {
            return this.type == item.getType() && this.rarity == item.getRarity() && this.xp == item.getData().getDouble("xp");
        }

        public SItem toItem() {
            SItem sItem = SItem.of(this.type);
            sItem.setRarity(this.rarity);
            sItem.getData().setDouble("xp", this.xp);
            return sItem;
        }

        public static PetItem deserialize(Map<String, Object> map) {
            return new PetItem(SMaterial.getMaterial((String)map.get("type")), Rarity.getRarity((String)map.get("rarity")), (Double)map.get("xp"), (Boolean)map.get("active"));
        }

        public static PetItem fromDocument(Document document) {
            String type = document.getString("type");
            String rarity = document.getString("rarity");
            double xp = document.getDouble("xp");
            boolean active = document.getBoolean("active");
            return new PetItem(SMaterial.getMaterial(type), Rarity.getRarity(rarity), xp, active);
        }

        public SMaterial getType() {
            return this.type;
        }

        public Rarity getRarity() {
            return this.rarity;
        }

        public double getXp() {
            return this.xp;
        }

        public boolean isActive() {
            return this.active;
        }

        public void setRarity(Rarity rarity) {
            this.rarity = rarity;
        }

        public void setXp(double xp) {
            this.xp = xp;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}

