/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityFallingBlock
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Chicken
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.RebootServerCommand;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanBossManager;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.calendar.SkyBlockCalendar;
import net.hypixel.skyblock.features.dungeons.blessing.Blessings;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.potion.ActivePotionEffect;
import net.hypixel.skyblock.features.quest.QuestLine;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.scoreboard.Sidebar;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemListener;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.armor.TickingSet;
import net.hypixel.skyblock.item.bow.Terminator;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.DefenseReplacement;
import net.hypixel.skyblock.util.ManaReplacement;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.hypixel.skyblock.util.SputnikPlayer;
import net.hypixel.skyblock.util.ZSHash;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Repeater {
    public static final Map<UUID, Integer> MANA_MAP = new HashMap<UUID, Integer>();
    public static final Map<UUID, Boolean> MANA_REGEN_DEC = new HashMap<UUID, Boolean>();
    public static final Map<UUID, DefenseReplacement> DEFENSE_REPLACEMENT_MAP = new HashMap<UUID, DefenseReplacement>();
    public static final Map<UUID, ManaReplacement> MANA_REPLACEMENT_MAP = new HashMap<UUID, ManaReplacement>();
    public static final Map<UUID, Entity> BEACON_THROW2 = new HashMap<UUID, Entity>();
    public static final Map<Entity, Player> BEACON_OWNER = new HashMap<Entity, Player>();
    public static final Map<Entity, EntityFallingBlock> BEACON = new HashMap<Entity, EntityFallingBlock>();
    public static final Map<UUID, Integer> PTN_CACHE = new HashMap<UUID, Integer>();
    private final List<BukkitTask> tasks = new ArrayList<BukkitTask>();
    public static int EFFECT_COUNTING = 0;
    public static final Map<UUID, Integer> FloorLivingSec = new HashMap<UUID, Integer>();
    public static Config config = SkyBlock.getPlugin().config;

    public static void cRun(User u2) {
        List<ActivePotionEffect> apt = u2.getEffects();
        if (PTN_CACHE.containsKey(u2.getUuid())) {
            if (PTN_CACHE.get(u2.getUuid()) >= apt.size()) {
                PTN_CACHE.put(u2.getUuid(), 0);
            }
        } else {
            PTN_CACHE.put(u2.getUuid(), 0);
        }
    }

    public Repeater() {
        this.tasks.add(new BukkitRunnable(){

            public void run() {
                SLog.info("[SYSTEM] Auto-Saving players data and world data...");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isOnline()) {
                        return;
                    }
                    User user = User.getUser(player.getUniqueId());
                    user.asyncSavingData();
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 3000L, 3000L));
        this.tasks.add(new BukkitRunnable(){

            public void run() {
                for (User user : User.getCachedUsers()) {
                    if (Bukkit.getPlayer((UUID)user.getUuid()) == null || Bukkit.getPlayer((UUID)user.getUuid()).isOnline()) continue;
                    User.getHash().remove(user.getUuid());
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 1L, 1L));
        this.tasks.add(new BukkitRunnable(){

            public void run() {
                Blessings.update();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location blockLocation = player.getEyeLocation();
                    try {
                        blockLocation = player.getTargetBlock((Set)null, 30).getLocation();
                    } catch (IllegalStateException illegalStateException) {
                        // empty catch block
                    }
                    Location crystalLocation = player.getEyeLocation();
                    Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
                    double count = 10.0;
                    double length = 0.0;
                    for (int i2 = 1; i2 <= 10; ++i2) {
                        for (Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 10.0)), 0.25, 0.2, 0.25)) {
                            if (!entity.hasMetadata("Nukekubi") || !VoidgloomSeraph.NUKEKUBI_TARGET.containsKey(entity) || VoidgloomSeraph.NUKEKUBI_TARGET.get(entity) != player) continue;
                            entity.remove();
                            player.playSound(player.getLocation(), Sound.SILVERFISH_HIT, 1.0f, 0.0f);
                            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION, 1);
                            entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION, 1);
                            if (!VoidgloomSeraph.LivingSkulls.containsKey(VoidgloomSeraph.NUKEKUBI_TARGET.get(entity).getUniqueId())) continue;
                            VoidgloomSeraph.LivingSkulls.get(VoidgloomSeraph.NUKEKUBI_TARGET.get(entity).getUniqueId()).remove(entity);
                        }
                    }
                }
                for (World world : Bukkit.getWorlds()) {
                    if (!world.getName().toLowerCase().contains("f6")) continue;
                    for (Entity e2 : world.getEntities()) {
                        if (e2.hasMetadata("Giant_")) {
                            for (Entity entity2 : e2.getNearbyEntities(3.0, 11.0, 3.0)) {
                                if (!(entity2 instanceof Arrow)) continue;
                                Projectile arr = (Projectile)entity2;
                                if (!(arr.getShooter() instanceof Player)) {
                                    return;
                                }
                                SUtil.giantsHitboxFix(arr);
                            }
                        }
                        if (e2 instanceof Item) {
                            if (((Item)e2).getItemStack().getType() == Material.FLOWER_POT_ITEM) {
                                e2.remove();
                            }
                            if (((Item)e2).getItemStack().getType() == Material.IRON_TRAPDOOR) {
                                e2.remove();
                            }
                        }
                        if (!(e2 instanceof Chicken)) continue;
                        e2.remove();
                    }
                    for (Entity e2 : world.getNearbyEntities(new Location(world, 191.0, 54.0, 266.0), 4.0, 11.0, 4.0)) {
                        if (!(e2 instanceof Player)) continue;
                        Player player2 = (Player)e2;
                        User.getUser(player2.getUniqueId()).kill(EntityDamageEvent.DamageCause.VOID, null);
                    }
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L));
        this.tasks.add(new BukkitRunnable(){

            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (PTN_CACHE.containsKey(User.getUser(player.getUniqueId()).getUuid())) {
                        PTN_CACHE.put(User.getUser(player.getUniqueId()).getUuid(), PTN_CACHE.get(User.getUser(player.getUniqueId()).getUuid()) + 1);
                        continue;
                    }
                    PTN_CACHE.put(User.getUser(player.getUniqueId()).getUuid(), 0);
                }
                for (World w2 : Bukkit.getWorlds()) {
                    if (!w2.getName().contains("f6") || w2.getName().equals("f6") || w2.getPlayers().size() != 0) continue;
                    SadanBossManager.endFloor(w2);
                    SLog.warn("[WORLD CLEARER] Cleared F6 Bossroom world " + w2.getName() + ". Reason: No player inside");
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 45L));
        this.tasks.add(new BukkitRunnable(){

            public void run() {
                SkyBlockCalendar.ELAPSED += 20L;
                for (World w2 : Bukkit.getWorlds()) {
                    if (!SadanHuman.BossRun.containsKey(w2.getUID()) || !w2.getName().contains("f6") || !SadanHuman.BossRun.containsKey(w2.getUID()) || !SadanHuman.BossRun.get(w2.getUID()).booleanValue()) continue;
                    FloorLivingSec.put(w2.getUID(), FloorLivingSec.get(w2.getUID()) + 1);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L));
    }

    public void stop() {
        for (BukkitTask task : this.tasks) {
            task.cancel();
        }
    }

    public int runningTasks() {
        return this.tasks.size();
    }

    public static void runPlayerTask(Player player, int[] counters, List<AtomicInteger> counters2) {
        int mana;
        Repeater.cRun(User.getUser(player.getUniqueId()));
        if (User.getUser(player.getUniqueId()).getActivePet() != null && User.getUser(player.getUniqueId()).getActivePet().getType() == SMaterial.HIDDEN_USSR_T34_TANK_PET) {
            player.getWorld().playSound(player.getLocation(), Sound.MINECART_INSIDE, 0.09f, 0.3f);
        }
        if (!ZSHash.Charges.containsKey(player.getUniqueId())) {
            ZSHash.Charges.put(player.getUniqueId(), 5);
        }
        if (ZSHash.Charges.get(player.getUniqueId()) < 5) {
            if (!ZSHash.Cooldown.containsKey(player.getUniqueId())) {
                ZSHash.Cooldown.put(player.getUniqueId(), 15);
            }
            ZSHash.Cooldown.put(player.getUniqueId(), ZSHash.Cooldown.get(player.getUniqueId()) - 1);
            if (ZSHash.Cooldown.get(player.getUniqueId()) <= 0) {
                ZSHash.Cooldown.put(player.getUniqueId(), 15);
                ZSHash.Charges.put(player.getUniqueId(), ZSHash.Charges.get(player.getUniqueId()) + 1);
            }
        }
        PlayerInventory inv1 = player.getInventory();
        SItem helm = SItem.find(inv1.getHelmet());
        SItem chest = SItem.find(inv1.getChestplate());
        SItem legs = SItem.find(inv1.getLeggings());
        SItem boot = SItem.find(inv1.getBoots());
        if (helm != null && chest != null && legs != null && boot != null && helm.getType() == SMaterial.SORROW_HELMET && chest.getType() == SMaterial.SORROW_CHESTPLATE && legs.getType() == SMaterial.SORROW_LEGGINGS && boot.getType() == SMaterial.SORROW_BOOTS) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        if (helm != null && (helm.getType() == SMaterial.HIDDEN_USSR_HELMET || helm.getType() == SMaterial.HIDDEN_DONATOR_HELMET)) {
            ItemStack SSRstack = helm.getStack();
            SSRstack.setDurability((short)SUtil.random(0, 15));
            player.getInventory().setHelmet(SSRstack);
        }
        PlayerInventory inventory = player.getInventory();
        SItem sitem = SItem.find(player.getItemInHand());
        if (sitem != null) {
            Object enchL;
            if (sitem.getType() != SMaterial.ENCHANTED_BOOK && sitem.getEnchantments() != null && (enchL = Enchantment.ultimateEnchantsListFromList(sitem.getEnchantments())).size() > 1) {
                Iterator<Enchantment> iterator = enchL.iterator();
                while (iterator.hasNext()) {
                    Enchantment ench = iterator.next();
                    sitem.removeEnchantment(ench.getType());
                }
                player.sendMessage(ChatColor.RED + "Your item have multiple legacy ultimate enchantments so we need to remove all of them, sorry! You can always get a new one, also just to remind you, only 1 ultimate enchantment is allowed per weapon.");
            }
            if (sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null && sitem.getType() != SMaterial.ENCHANTED_BOOK && sitem.getType().getStatistics().getType() == GenericItemType.WEAPON) {
                for (Enchantment enchantment : sitem.getEnchantments()) {
                    if (enchantment.getType() == EnchantmentType.TELEKINESIS || enchantment.getType() == EnchantmentType.ONE_FOR_ALL) continue;
                    sitem.removeEnchantment(enchantment.getType());
                }
            }
        }
        PlayerStatistics statistics1 = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        for (int i2 = 0; i2 <= inventory.getSize(); ++i2) {
            ItemStack stack = inventory.getItem(i2);
            SItem sItem = SItem.find(stack);
            int slot = 15 + i2;
            if (sItem != null && sItem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
                statistics1.zeroAll(slot);
            }
            if (stack != null) continue;
            statistics1.zeroAll(slot);
        }
        player.setSaturation(1000.0f);
        player.setFoodLevel(20);
        UUID uuid = player.getUniqueId();
        if (!PlayerUtils.STATISTICS_CACHE.containsKey(uuid)) {
            PlayerUtils.STATISTICS_CACHE.put(uuid, PlayerUtils.getStatistics(player));
        }
        PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(uuid);
        int manaPool = SUtil.blackMagic(100.0 + statistics2.getIntelligence().addAll());
        SItem hand = SItem.find(inventory.getItemInHand());
        if (hand == null && (hand = SItem.of(inventory.getItemInHand())) != null) {
            if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().contains("minion")) {
                return;
            }
            player.setItemInHand(hand.getStack());
        }
        PlayerUtils.updateHandStatistics(hand, statistics2);
        PlayerUtils.updatePetStatistics(statistics2);
        PlayerUtils.updateInventoryStatistics(player, statistics2);
        ItemListener.updateStatistics1(player);
        User user = User.getUser(player.getUniqueId());
        for (ActivePotionEffect effect : user.getEffects()) {
            effect.setRemaining(effect.getRemaining() - 20L);
        }
        PlayerUtils.subtractDurationCookie(player, 20L);
        PlayerUtils.updatePotionEffects(user, statistics2);
        if (!player.getWorld().getName().equalsIgnoreCase("limbo")) {
            if (hand != null) {
                if (hand.getType().getStatistics().getType() != GenericItemType.BLOCK && hand.getType().getStatistics().getType() != GenericItemType.ITEM && hand.getType().getStatistics().getType() != GenericItemType.PET) {
                    hand.getData().setString("owner", player.getUniqueId().toString());
                }
                hand.update();
                if (hand.getType().getStatistics().getType() != GenericItemType.BLOCK && hand.getType().getStatistics().getType() != GenericItemType.ITEM && hand.getType().getStatistics().getType() != GenericItemType.PET) {
                    player.getItemInHand().setItemMeta(user.updateItemBoost(hand).getItemMeta());
                }
                SItem last = SItem.find(inventory.getItem(8));
                if (hand.getType().getStatistics().getSpecificType() == SpecificItemType.BOW && user.hasQuiverItem(SMaterial.ARROW) && (last == null || last.getType() != SMaterial.QUIVER_ARROW)) {
                    inventory.setItem(8, SUtil.setStackAmount(SItem.of(SMaterial.QUIVER_ARROW).getStack(), Math.min(64, user.getQuiver(SMaterial.ARROW))));
                }
                if (hand.getType().getStatistics().getSpecificType() != SpecificItemType.BOW) {
                    inventory.setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());
                }
            } else {
                inventory.setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());
            }
        }
        if (!MANA_MAP.containsKey(uuid)) {
            MANA_MAP.put(uuid, manaPool);
        }
        if (counters[0] == 2 && (mana = MANA_MAP.get(uuid).intValue()) <= manaPool) {
            int reg = Math.min(manaPool, Math.min(manaPool, manaPool / 50 + (int)((double)(manaPool / 50) * statistics2.getManaRegenerationPercentBonus())));
            reg = MANA_REGEN_DEC.containsKey(uuid) ? (MANA_REGEN_DEC.get(uuid).booleanValue() ? mana + Math.round(reg / 10) : (reg += mana)) : (reg += mana);
            MANA_MAP.remove(uuid);
            MANA_MAP.put(uuid, Math.min(manaPool, reg));
        }
        if (counters[1] == 4 && player.getHealth() <= player.getMaxHealth()) {
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 1.5 + (double)((int)player.getMaxHealth()) * 0.01 + (1.5 + (double)((int)player.getMaxHealth()) * 0.01) * statistics2.getHealthRegenerationPercentBonus()));
        }
        PlayerUtils.updateSetStatistics(player, SItem.find(inventory.getHelmet()), SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), statistics2);
        double health = statistics2.getMaxHealth().addAll();
        player.setHealthScale(Math.min(40.0, 20.0 + (health - 100.0) / 25.0));
        SputnikPlayer.updateScaledAHP(player);
        SUtil.runSync(() -> player.setWalkSpeed(Math.min((float)(statistics2.getSpeed().addAll() / 5.0), 1.0f)));
        int defense = SUtil.blackMagic(statistics2.getDefense().addAll());
        float absorption = SputnikPlayer.getCustomAbsorptionHP(player).intValue();
        ChatColor color = absorption > 0.0f ? ChatColor.GOLD : ChatColor.RED;
        DefenseReplacement replacement = DEFENSE_REPLACEMENT_MAP.get(player.getUniqueId());
        ManaReplacement replacement2 = MANA_REPLACEMENT_MAP.get(player.getUniqueId());
        if (replacement != null && System.currentTimeMillis() >= replacement.getEnd()) {
            DEFENSE_REPLACEMENT_MAP.remove(player.getUniqueId());
            replacement = null;
        }
        if (replacement2 != null && System.currentTimeMillis() >= replacement2.getEnd()) {
            MANA_REPLACEMENT_MAP.remove(player.getUniqueId());
            replacement2 = null;
        }
        String ZSActionBar = "";
        String ABTerminator = "";
        if (!ZSHash.Charges.containsKey(player.getUniqueId())) {
            ZSHash.Charges.put(player.getUniqueId(), 5);
        }
        if (ZSHash.Charges.get(player.getUniqueId()) == 5) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &e&l\u24e9\u24e9\u24e9\u24e9\u24e9");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 4) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &e&l\u24e9\u24e9\u24e9\u24e9&6&l\u24c4");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 3) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &e&l\u24e9\u24e9\u24e9&6&l\u24c4\u24c4");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 2) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &e&l\u24e9\u24e9&6&l\u24c4\u24c4\u24c4");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 1) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &e&l\u24e9&6&l\u24c4\u24c4\u24c4\u24c4");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 0) {
            ZSActionBar = ChatColor.translateAlternateColorCodes((char)'&', (String)"     &6&l\u24c4\u24c4\u24c4\u24c4\u24c4");
        }
        if (!Terminator.CountTerm.containsKey(player.getUniqueId())) {
            Terminator.CountTerm.put(player.getUniqueId(), 0);
        }
        if (Terminator.CountTerm.get(player.getUniqueId()) == 0) {
            ABTerminator = "";
        } else if (Terminator.CountTerm.get(player.getUniqueId()) == 1) {
            ABTerminator = Sputnik.trans("  &6T1");
        } else if (Terminator.CountTerm.get(player.getUniqueId()) == 2) {
            ABTerminator = Sputnik.trans("  &6T2");
        } else if (Terminator.CountTerm.get(player.getUniqueId()) >= 3) {
            ABTerminator = Sputnik.trans("  &a&lT3!");
        }
        if (SItem.find(player.getItemInHand()) != null) {
            if (SItem.find(player.getItemInHand()).getType() != SMaterial.ZOMBIE_SWORD_T2 && SItem.find(player.getItemInHand()).getType() != SMaterial.ZOMBIE_SWORD_T3) {
                ZSActionBar = "";
            }
            if (SItem.find(player.getItemInHand()).getType() != SMaterial.TERMINATOR) {
                ABTerminator = "";
            }
        } else {
            ZSActionBar = "";
            ABTerminator = "";
        }
        if (!player.getWorld().getName().equalsIgnoreCase("limbo")) {
            SUtil.sendActionBar(player, color + "" + Math.round(player.getHealth() + (double)absorption) + "/" + SUtil.blackMagic(statistics2.getMaxHealth().addAll()) + "\u2764" + Repeater.get(player) + "     " + (replacement == null ? (defense != 0 ? "" + ChatColor.GREEN + defense + "\u2748 Defense" + ABTerminator + "     " : "") : replacement.getReplacement() + "     ") + (replacement2 == null ? (manaPool >= 0 ? "" + ChatColor.AQUA + MANA_MAP.get(player.getUniqueId()) + "/" + manaPool + "\u270e Mana" : "") : replacement2.getReplacement()) + ZSActionBar);
        } else {
            SUtil.sendActionBar(player, color + "" + ChatColor.RED + ChatColor.BOLD + "YOU ARE CURRENTLY IN THE LIMBO SERVER, USE " + ChatColor.GOLD + ChatColor.BOLD + "/HUB " + ChatColor.RED + ChatColor.BOLD + "TO WARP OUT");
        }
        statistics2.zeroAll(8);
        ArmorSet set = SMaterial.getIncompleteArmorSet(inventory);
        if (set instanceof TickingSet) {
            ((TickingSet)set).tick(player, SItem.find(inventory.getHelmet()), SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), counters2);
        }
        SUtil.runSync(() -> {
            Sidebar sidebar = new Sidebar(Sputnik.trans("&e&lSKYBLOCK &c&lSANDBOX"), "SKYBLOCK");
            SUtil.runAsync(() -> {
                String strd = SUtil.getDate();
                if (RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
                    strd = RebootServerCommand.secondMap.get(Bukkit.getServer()) >= 10 ? ChatColor.RED + "Server closing: 00:" + RebootServerCommand.secondMap.get(Bukkit.getServer()) : ChatColor.RED + "Server closing: 00:0" + RebootServerCommand.secondMap.get(Bukkit.getServer());
                }
                sidebar.add(ChatColor.GRAY + strd + " " + ChatColor.DARK_GRAY + SkyBlock.getPlugin().getServerName());
                sidebar.add("  ");
                sidebar.add(" " + SkyBlockCalendar.getMonthName() + " " + SUtil.ntify(SkyBlockCalendar.getDay()));
                int hours = 12;
                int minutes = 31;
                sidebar.add(ChatColor.GRAY + " " + hours + ":" + SUtil.zeroed(minutes) + "pm " + ChatColor.YELLOW + "\u2600");
                String location = ChatColor.GRAY + "None";
                Region region = Region.getRegionOfEntity((Entity)player);
                if (region != null) {
                    user.setLastRegion(region);
                    if (region.getType().getName() != null) {
                        location = region.getType().getColor() + region.getType().getName();
                    }
                }
                if (user.isOnIsland()) {
                    location = ChatColor.GREEN + "Your Island";
                }
                if (user.isOnUserIsland()) {
                    location = ChatColor.AQUA + "Others Island";
                } else if (player.getWorld().getName().contains("f6")) {
                    sidebar.add(ChatColor.GRAY + " \u23e3 " + ChatColor.RED + "Catacombs" + ChatColor.GRAY + " (F6)");
                } else if (player.getWorld().getName().contains("arena")) {
                    sidebar.add(ChatColor.GRAY + " \u23e3 " + ChatColor.RED + "Withering Ruins");
                } else if (player.getWorld().getName().contains("gisland")) {
                    sidebar.add(ChatColor.GRAY + " \u23e3 " + ChatColor.YELLOW + "Giant's Island");
                } else {
                    sidebar.add(ChatColor.GRAY + " \u23e3 " + location);
                }
                if (!player.getWorld().getName().startsWith("f6") && !player.getWorld().getName().equalsIgnoreCase("arena")) {
                    sidebar.add(" ");
                    StringBuilder coinsDisplay = new StringBuilder();
                    if (user.isPermanentCoins()) {
                        coinsDisplay.append("Purse: ");
                    } else if (PlayerUtils.hasItem(player, SMaterial.PIGGY_BANK) || PlayerUtils.hasItem(player, SMaterial.CRACKED_PIGGY_BANK)) {
                        coinsDisplay.append("Piggy: ");
                    } else {
                        coinsDisplay.append("Purse: ");
                    }
                    sidebar.add(coinsDisplay.append(ChatColor.GOLD).append(SUtil.commaify(user.getCoins())) + ".0" + ChatColor.YELLOW);
                    sidebar.add("Bits: " + ChatColor.AQUA + user.getBits());
                    sidebar.add("   ");
                    SlayerQuest quest = user.getSlayerQuest();
                    QuestLine line = user.getQuestLine();
                    if (!(StaticDragonManager.ACTIVE && StaticDragonManager.DRAGON != null || quest != null || line == null)) {
                        sidebar.add(ChatColor.WHITE + "Objective");
                        sidebar.add(ChatColor.YELLOW + line.getObjective(user).getDisplay());
                        if (line.getObjective(user).hasSuffix(user)) {
                            sidebar.add(line.getObjective(user).getSuffix(user));
                        }
                        sidebar.add("      ");
                    }
                    if (!(StaticDragonManager.ACTIVE && StaticDragonManager.DRAGON != null || quest == null || quest.getDied() != 0L && quest.getKilled() == 0L)) {
                        sidebar.add("Slayer Quest");
                        sidebar.add(quest.getType().getDisplayName());
                        if (quest.getKilled() != 0L) {
                            sidebar.add(ChatColor.GREEN + "Boss slain!");
                        } else if (quest.getXp() >= (double)quest.getType().getSpawnXP()) {
                            sidebar.add(ChatColor.YELLOW + "Slay the boss!");
                        } else if (quest.getLastKilled() != null) {
                            double xpDropped = quest.getLastKilled().getStatistics().getXPDropped();
                            if (PlayerUtils.getCookieDurationTicks(player) > 0L) {
                                xpDropped += quest.getLastKilled().getStatistics().getXPDropped() * 35.0 / 100.0;
                            }
                            sidebar.add(ChatColor.YELLOW + " " + SUtil.commaify((int)(quest.getXp() / xpDropped)) + ChatColor.GRAY + "/" + ChatColor.RED + SUtil.commaify((int)((double)quest.getType().getSpawnXP() / xpDropped)) + ChatColor.GRAY + " Kills");
                        } else {
                            sidebar.add(ChatColor.GRAY + " (" + ChatColor.YELLOW + SUtil.commaify((int)quest.getXp()) + ChatColor.GRAY + "/" + ChatColor.RED + Sputnik.formatFull(quest.getType().getSpawnXP()) + ChatColor.GRAY + ") Combat XP");
                        }
                        sidebar.add("    ");
                    }
                    if (StaticDragonManager.ACTIVE && StaticDragonManager.DRAGON != null) {
                        int dmgdealt;
                        sidebar.add("Dragon HP: " + ChatColor.GREEN + SUtil.commaify((int)StaticDragonManager.DRAGON.getEntity().getHealth()) + ChatColor.RED + " \u2764");
                        if (StaticDragonManager.DRAGON.getDamageDealt().containsKey(uuid)) {
                            double dealt = StaticDragonManager.DRAGON.getDamageDealt().get(uuid);
                            dmgdealt = (int)Math.round(dealt);
                            if (dmgdealt < 0) {
                                dmgdealt = Integer.MAX_VALUE;
                            }
                        } else {
                            dmgdealt = 0;
                        }
                        sidebar.add("Your Damage: " + ChatColor.RED + SUtil.commaify(dmgdealt));
                        sidebar.add("     ");
                    }
                } else if (player.getWorld().getName().startsWith("f6") && !player.getWorld().getName().equals("f6")) {
                    sidebar.add(ChatColor.RED + " ");
                    if (FloorLivingSec.containsKey(player.getWorld().getUID())) {
                        sidebar.add(ChatColor.translateAlternateColorCodes((char)'&', (String)("&fTime Elapsed: &a" + Sputnik.formatTime(FloorLivingSec.get(player.getWorld().getUID())))));
                    } else {
                        sidebar.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&fTime Elapsed: &a00m 00s"));
                    }
                    sidebar.add(ChatColor.translateAlternateColorCodes((char)'&', (String)"&fDungeon Cleared: &cN/A%"));
                    sidebar.add(ChatColor.RED + "  ");
                    String playerName = player.getName();
                    if (player.getWorld().getPlayers().size() > 1) {
                        for (Player dungeonMate : player.getWorld().getPlayers()) {
                            String backend = Repeater.getFormatted(dungeonMate);
                            if (Objects.equals(dungeonMate.getName(), playerName)) continue;
                            sidebar.add(ChatColor.translateAlternateColorCodes((char)'&', (String)("&e[N/A&e] &b" + dungeonMate.getName() + backend)));
                        }
                    } else if (player.getWorld().getPlayers().size() == 1) {
                        sidebar.add(ChatColor.DARK_GRAY + "SOLO");
                    } else if (player.getWorld().getName().startsWith("f6") && player.getWorld().getPlayers().size() > 5) {
                        sidebar.add(ChatColor.RED + "Cannot display more than 5 players at once!");
                    }
                    QuestLine line = user.getQuestLine();
                    if (line != null) {
                        sidebar.add(" ");
                        sidebar.add(ChatColor.WHITE + "Objective");
                        sidebar.add(ChatColor.YELLOW + line.getObjective(user).getDisplay());
                        if (line.getObjective(user).hasSuffix(user)) {
                            sidebar.add(line.getObjective(user).getSuffix(user));
                        }
                        sidebar.add("      ");
                    }
                    sidebar.add(ChatColor.AQUA + "     ");
                }
                sidebar.add(ChatColor.YELLOW + config.getString("ip"));
                if (!player.getWorld().getName().equalsIgnoreCase("dungeon")) {
                    sidebar.apply(player);
                }
            });
        });
    }

    @NotNull
    private static String getFormatted(Player dungeonMate) {
        String colorCode = dungeonMate.getHealth() <= dungeonMate.getMaxHealth() / 2.0 && dungeonMate.getHealth() > dungeonMate.getMaxHealth() * 25.0 / 100.0 ? "e" : (dungeonMate.getHealth() <= dungeonMate.getMaxHealth() * 25.0 / 100.0 ? "c" : "a");
        return " &" + colorCode + (int)dungeonMate.getHealth() + "&c\u2764";
    }

    public static String get(Player p2) {
        return "";
    }
}

