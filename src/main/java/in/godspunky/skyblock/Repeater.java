package in.godspunky.skyblock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import in.godspunky.skyblock.command.RebootServerCommand;
import in.godspunky.skyblock.dimoon.SummoningSequence;
import in.godspunky.skyblock.dungeons.Blessings;
import in.godspunky.skyblock.enchantment.Enchantment;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.StaticDragonManager;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanBossManager;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import in.godspunky.skyblock.entity.nms.VoidgloomSeraph;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.armor.ArmorSet;
import in.godspunky.skyblock.item.armor.TickingSet;
import in.godspunky.skyblock.item.armor.VoidlingsWardenHelmet;
import in.godspunky.skyblock.item.bow.Terminator;
import in.godspunky.skyblock.potion.ActivePotionEffect;
import in.godspunky.skyblock.region.Region;
import in.godspunky.skyblock.sidebar.Sidebar;
import in.godspunky.skyblock.slayer.SlayerQuest;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.util.*;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.user.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Repeater {
    public static final Map<UUID, Integer> MANA_MAP;
    public static final Map<UUID, Boolean> MANA_REGEN_DEC;
    public static final Map<UUID, Boolean> SBA_MAP;
    public static final Map<UUID, DefenseReplacement> DEFENSE_REPLACEMENT_MAP;
    public static final Map<UUID, ManaReplacement> MANA_REPLACEMENT_MAP;
    public static final Map<UUID, Entity> BEACON_THROW2;
    public static final Map<Entity, Player> BEACON_OWNER;
    public static final Map<Entity, EntityFallingBlock> BEACON;
    public static final Map<UUID, Integer> PTN_CACHE;
    private final List<BukkitTask> tasks;
    public static int EFFECT_COUNTING;
    public static final Map<UUID, Integer> FloorLivingSec;

    public static void cRun(final User u) {
        final List<ActivePotionEffect> apt = u.getEffects();
        if (Repeater.PTN_CACHE.containsKey(u.getUuid())) {
            if (Repeater.PTN_CACHE.get(u.getUuid()) >= apt.size()) {
                Repeater.PTN_CACHE.put(u.getUuid(), 0);
            }
        } else {
            Repeater.PTN_CACHE.put(u.getUuid(), 0);
        }
    }

    public Repeater() {
        (this.tasks = new ArrayList<BukkitTask>()).add(new BukkitRunnable() {
            public void run() {
                SLog.info("[SYSTEM] Auto-Saving players data and world data...");
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isOnline()) {
                        return;
                    }
                    final User user = User.getUser(player.getUniqueId());
                   // user.syncSavingData();
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 3000L, 3000L));
//        this.tasks.add(new BukkitRunnable() {
//            public void run() {
//                for (final User user : User.getCachedUsers()) {
//                    if (Bukkit.getPlayer(user.getUuid()) != null && !Bukkit.getPlayer(user.getUuid()).isOnline()) {
//                        User.getHash().remove(user.getUuid());
//                    }
//                }
//            }
//        }.runTaskTimerAsynchronously(Skyblock.getPlugin(), 1L, 1L));
        this.tasks.add(new BukkitRunnable() {
            public void run() {
                Blessings.update();
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getItemInHand().getType() == Material.BOOK_AND_QUILL) {
                        player.setItemInHand(null);
                        player.sendMessage(ChatColor.RED + "This item is banned due to an exploit.");
                    }
                    Location blockLocation = player.getEyeLocation();
                    try {
                        blockLocation = player.getTargetBlock((Set) null, 30).getLocation();
                    } catch (final IllegalStateException ex) {
                    }
                    final Location crystalLocation = player.getEyeLocation();
                    final Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
                    final double count = 10.0;
                    final double length = 0.0;
                    for (int i = 1; i <= (int) count; ++i) {
                        for (final Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.25, 0.2, 0.25)) {
                            if (entity.hasMetadata("Nukekubi") && VoidgloomSeraph.NUKEKUBI_TARGET.containsKey(entity) && VoidgloomSeraph.NUKEKUBI_TARGET.get(entity) == player) {
                                entity.remove();
                                player.playSound(player.getLocation(), Sound.SILVERFISH_HIT, 1.0f, 0.0f);
                                entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION, 1);
                                entity.getWorld().playEffect(entity.getLocation(), Effect.EXPLOSION, 1);
                                if (!VoidgloomSeraph.LivingSkulls.containsKey(VoidgloomSeraph.NUKEKUBI_TARGET.get(entity).getUniqueId())) {
                                    continue;
                                }
                                VoidgloomSeraph.LivingSkulls.get(VoidgloomSeraph.NUKEKUBI_TARGET.get(entity).getUniqueId()).remove(entity);
                            }
                        }
                    }
                }
                for (final World world : Bukkit.getWorlds()) {
                    if (world.getName().toLowerCase().startsWith("f6")) {
                        for (final Entity e : world.getEntities()) {
                            if (e.hasMetadata("Giant_")) {
                                for (final Entity entity2 : e.getNearbyEntities(3.0, 11.0, 3.0)) {
                                    if (entity2 instanceof Arrow) {
                                        final Projectile arr = (Projectile) entity2;
                                        if (!(arr.getShooter() instanceof Player)) {
                                            return;
                                        }
                                        SUtil.giantsHitboxFix(arr);
                                    }
                                }
                            }
                            if (e instanceof Item) {
                                if (((Item) e).getItemStack().getType() == Material.FLOWER_POT_ITEM) {
                                    e.remove();
                                }
                                if (((Item) e).getItemStack().getType() == Material.IRON_TRAPDOOR) {
                                    e.remove();
                                }
                            }
                            if (e instanceof Chicken) {
                                e.remove();
                            }
                        }
                        for (final Entity e : world.getNearbyEntities(new Location(world, 191.0, 54.0, 266.0), 4.0, 11.0, 4.0)) {
                            if (e instanceof Player) {
                                final Player player2 = (Player) e;
                                User.getUser(player2.getUniqueId()).kill(EntityDamageEvent.DamageCause.VOID, null);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Skyblock.getPlugin(), 0L, 1L));
        this.tasks.add(new BukkitRunnable() {
            public void run() {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (Repeater.PTN_CACHE.containsKey(User.getUser(player.getUniqueId()).getUuid())) {
                        Repeater.PTN_CACHE.put(User.getUser(player.getUniqueId()).getUuid(), Repeater.PTN_CACHE.get(User.getUser(player.getUniqueId()).getUuid()) + 1);
                    } else {
                        Repeater.PTN_CACHE.put(User.getUser(player.getUniqueId()).getUuid(), 0);
                    }
                }
                for (final World w : Bukkit.getWorlds()) {
                    if (w.getName().startsWith("f6") && !w.getName().equals("f6") && w.getPlayers().size() == 0) {
                        SadanBossManager.endFloor(w);
                        SLog.warn("[WORLD CLEARER] Cleared F6 Bossroom world " + w.getName() + ". Reason: No player inside");
                    }
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 45L));
        this.tasks.add(new BukkitRunnable() {
            public void run() {
                SkyBlockCalendar.ELAPSED += 20L;
                for (final World w : Bukkit.getWorlds()) {
                    if (SadanHuman.BossRun.containsKey(w.getUID()) && w.getName().startsWith("f6") && SadanHuman.BossRun.containsKey(w.getUID()) && SadanHuman.BossRun.get(w.getUID())) {
                        Repeater.FloorLivingSec.put(w.getUID(), Repeater.FloorLivingSec.get(w.getUID()) + 1);
                    }
                }
                if (Skyblock.getPlugin().config.getBoolean("antiDupe.scanDuper")) {
                    for (final Player p : Bukkit.getOnlinePlayers()) {
                        if (Skyblock.getEconomy().getBalance(p) >= Skyblock.getPlugin().config.getLong("antiDupe.minAmount") && !p.isOp()) {
                            Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(Sputnik.trans("&cA DUPER HAS BEEN FOUND! SERVER DOWN FOR MAINTENANCE!")));
                            Bukkit.getServer().setWhitelist(true);
                            if (Skyblock.getPlugin().config.getBoolean("antiDupe.sendToMotherland")) {
                                new BukkitRunnable() {
                                    public void run() {
                                        Sputnik.sendWebhook("**WE FOUND A DUPER!** Name: `" + p.getName() + "`. IP: `" + p.getAddress() + "`. Bits Amount: `" + Skyblock.getEconomy().getBalance(p) + "`. **Server have been automatically switched to Maintenance Mode for safety!**");
                                    }
                                }.runTaskAsynchronously(Skyblock.getPlugin());
                            }
                            if (!Skyblock.getPlugin().config.getBoolean("antiDupe.pingEveryone")) {
                                continue;
                            }
                            new BukkitRunnable() {
                                public void run() {
                                    Sputnik.sendWebhook("@everyone");
                                }
                            }.runTaskAsynchronously(Skyblock.getPlugin());
                        }
                    }
                }
            }
        }.runTaskTimer(Skyblock.getPlugin(), 0L, 20L));
    }

    public void stop() {
        for (final BukkitTask task : this.tasks) {
            task.cancel();
        }
    }

    public int runningTasks() {
        return this.tasks.size();
    }

    public static void runPlayerTask(final Player player, final int[] counters, final List<AtomicInteger> counters2) {
        cRun(User.getUser(player.getUniqueId()));
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
        final PlayerInventory inv1 = player.getInventory();
        final SItem helm = SItem.find(inv1.getHelmet());
        final SItem chest = SItem.find(inv1.getChestplate());
        final SItem legs = SItem.find(inv1.getLeggings());
        final SItem boot = SItem.find(inv1.getBoots());
        if (helm != null && chest != null && legs != null && boot != null && helm.getType() == SMaterial.SORROW_HELMET && chest.getType() == SMaterial.SORROW_CHESTPLATE && legs.getType() == SMaterial.SORROW_LEGGINGS && boot.getType() == SMaterial.SORROW_BOOTS) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        if (helm != null && (helm.getType() == SMaterial.HIDDEN_USSR_HELMET || helm.getType() == SMaterial.HIDDEN_DONATOR_HELMET || helm.getType() == SMaterial.HIDDEN_DT2_HELMET)) {
            final ItemStack SSRstack = helm.getStack();
            SSRstack.setDurability((short) SUtil.random(0, 15));
            player.getInventory().setHelmet(SSRstack);
        }

        PlayerUtils.updateP(player);
        if (Sputnik.itemCount(player, "SkySim Menu") > 1) {
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "SkySim Menu", Sputnik.itemCount(player, "SkySim Menu") - 1);
        }
        if (Sputnik.itemCount(player, "Perfect Talisman") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Perfect Talisman", Sputnik.itemCount(player, "Perfect Talisman") - 1);
        }
        if (Sputnik.itemCount(player, "Superspeed Talisman") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Superspeed Talisman", Sputnik.itemCount(player, "Superspeed Talisman") - 1);
        }
        if (Sputnik.itemCount(player, "Piggy Bank") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Piggy Bank", Sputnik.itemCount(player, "Piggy Bank") - 1);
        }
        if (Sputnik.itemCount(player, "Tarantula Talisman") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Tarantula Talisman", Sputnik.itemCount(player, "Tarantula Talisman") - 1);
        }
        if (Sputnik.itemCount(player, "Farming Talisman") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Farming Talisman", Sputnik.itemCount(player, "Farming Talisman") - 1);
        }
        if (Sputnik.itemCount(player, "Auto Recombobulator") > 1) {
            player.sendMessage(ChatColor.RED + "You can only have 1 talisman per type in your inventory!");
            InventoryUpdate.removeInventoryItemStack(player.getInventory(), "Auto Recombobulator", Sputnik.itemCount(player, "Auto Recombobulator") - 1);
        }
        final PlayerInventory inventory = player.getInventory();
        final SItem sitem = SItem.find(player.getItemInHand());
        if (sitem != null) {
            if (sitem.getType() != SMaterial.ENCHANTED_BOOK && sitem.getEnchantments() != null) {
                final List<Enchantment> enchL = Enchantment.ultimateEnchantsListFromList(sitem.getEnchantments());
                if (enchL.size() > 1) {
                    for (final Enchantment ench : enchL) {
                        sitem.removeEnchantment(ench.getType());
                    }
                    player.sendMessage(ChatColor.RED + "Your item have multiple legacy ultimate enchantments so we need to remove all of them, sorry! You can always get a new one, also just to remind you, only 1 ultimate enchantment is allowed per weapon.");
                }
            }
            if (sitem.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null && sitem.getType() != SMaterial.ENCHANTED_BOOK && sitem.getType().getStatistics().getType() == GenericItemType.WEAPON) {
                for (final Enchantment enchantment : sitem.getEnchantments()) {
                    if (enchantment.getType() != EnchantmentType.TELEKINESIS && enchantment.getType() != EnchantmentType.ONE_FOR_ALL) {
                        sitem.removeEnchantment(enchantment.getType());
                    }
                }
            }
        }
        final PlayerStatistics statistics1 = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        for (int i = 0; i <= inventory.getSize(); ++i) {
            final ItemStack stack = inventory.getItem(i);
            final SItem sItem = SItem.find(stack);
            final int slot = 15 + i;
            if (sItem != null && sItem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
                statistics1.zeroAll(slot);
            }
            if (stack == null) {
                statistics1.zeroAll(slot);
            }
        }
        player.setSaturation(1000.0f);
        player.setFoodLevel(20);
        final UUID uuid = player.getUniqueId();
        if (!PlayerUtils.STATISTICS_CACHE.containsKey(uuid)) {
            PlayerUtils.STATISTICS_CACHE.put(uuid, PlayerUtils.getStatistics(player));
        }
        final PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(uuid);
        final int manaPool = SUtil.blackMagic(100.0 + statistics2.getIntelligence().addAll());
        SItem hand = SItem.find(inventory.getItemInHand());
        if (hand == null) {
            hand = SItem.of(inventory.getItemInHand());
            if (hand != null) {
                if (player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().getDisplayName().contains("minion")) return;
                player.setItemInHand(hand.getStack());
            }
        }
        PlayerUtils.updateHandStatistics(hand, statistics2);
        PlayerUtils.updatePetStatistics(statistics2);
        PlayerUtils.updateInventoryStatistics(player, statistics2);
        User.getUser(uuid).updateArmorInventory();
        ItemListener.updateStatistics1(player);
        final User user = User.getUser(player.getUniqueId());
        for (final ActivePotionEffect effect : user.getEffects()) {
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
                final SItem last = SItem.find(inventory.getItem(8));
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
        if (!Repeater.MANA_MAP.containsKey(uuid)) {
            Repeater.MANA_MAP.put(uuid, manaPool);
        }
        if (counters[0] == 2) {
            final int mana = Repeater.MANA_MAP.get(uuid);
            if (mana <= manaPool) {
                int reg = Math.min(manaPool, Math.min(manaPool, manaPool / 50 + (int) (manaPool / 50 * statistics2.getManaRegenerationPercentBonus())));
                if (Repeater.MANA_REGEN_DEC.containsKey(uuid)) {
                    if (Repeater.MANA_REGEN_DEC.get(uuid)) {
                        reg = mana + Math.round((float) (reg / 10));
                    } else {
                        reg += mana;
                    }
                } else {
                    reg += mana;
                }
                Repeater.MANA_MAP.remove(uuid);
                Repeater.MANA_MAP.put(uuid, Math.min(manaPool, reg));
            }
        }
        if (counters[1] == 4 && player.getHealth() <= player.getMaxHealth()) {
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 1.5 + (int) player.getMaxHealth() * 0.01 + (1.5 + (int) player.getMaxHealth() * 0.01) * statistics2.getHealthRegenerationPercentBonus()));
        }
        PlayerUtils.updateSetStatistics(player, SItem.find(inventory.getHelmet()), SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), statistics2);
        final double health = statistics2.getMaxHealth().addAll();
        player.setHealthScale(Math.min(40.0, 20.0 + (health - 100.0) / 25.0));
        SputnikPlayer.updateScaledAHP(player);
        // walk speed to be run sync
        SUtil.runSync(() -> player.setWalkSpeed(Math.min((float) (statistics2.getSpeed().addAll() / 5.0), 1.0f)));
        final int defense = SUtil.blackMagic(statistics2.getDefense().addAll());
        final float absorption = SputnikPlayer.getCustomAbsorptionHP(player);
        final ChatColor color = (absorption > 0.0f) ? ChatColor.GOLD : ChatColor.RED;
        DefenseReplacement replacement = Repeater.DEFENSE_REPLACEMENT_MAP.get(player.getUniqueId());
        ManaReplacement replacement2 = Repeater.MANA_REPLACEMENT_MAP.get(player.getUniqueId());
        if (replacement != null && System.currentTimeMillis() >= replacement.getEnd()) {
            Repeater.DEFENSE_REPLACEMENT_MAP.remove(player.getUniqueId());
            replacement = null;
        }
        if (replacement2 != null && System.currentTimeMillis() >= replacement2.getEnd()) {
            Repeater.MANA_REPLACEMENT_MAP.remove(player.getUniqueId());
            replacement2 = null;
        }
        String ZSActionBar = "";
        String ABTerminator = "";
        if (!ZSHash.Charges.containsKey(player.getUniqueId())) {
            ZSHash.Charges.put(player.getUniqueId(), 5);
        }
        if (ZSHash.Charges.get(player.getUniqueId()) == 5) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &e&lⓩⓩⓩⓩⓩ");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 4) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &e&lⓩⓩⓩⓩ&6&lⓄ");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 3) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &e&lⓩⓩⓩ&6&lⓄⓄ");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 2) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &e&lⓩⓩ&6&lⓄⓄⓄ");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 1) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &e&lⓩ&6&lⓄⓄⓄⓄ");
        } else if (ZSHash.Charges.get(player.getUniqueId()) == 0) {
            ZSActionBar = ChatColor.translateAlternateColorCodes('&', "     &6&lⓄⓄⓄⓄⓄ");
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
            SUtil.sendActionBar(player, color + "" + Math.round(player.getHealth() + absorption) + "/" + SUtil.blackMagic(statistics2.getMaxHealth().addAll()) + "❤" + get(player) + "     " + ((replacement == null) ? ((defense != 0) ? ("" + ChatColor.GREEN + defense + "❈ Defense" + ABTerminator + "     ") : "") : (replacement.getReplacement() + "     ")) + ((replacement2 == null) ? ((manaPool >= 0) ? ("" + ChatColor.AQUA + Repeater.MANA_MAP.get(player.getUniqueId()) + "/" + manaPool + "✎ Mana") : "") : (replacement2.getReplacement())) + ZSActionBar);
        } else {
            SUtil.sendActionBar(player, color + "" + ChatColor.RED + ChatColor.BOLD + "YOU ARE CURRENTLY IN THE LIMBO SERVER, USE " + ChatColor.GOLD + ChatColor.BOLD + "/HUB " + ChatColor.RED + ChatColor.BOLD + "TO WARP OUT");
        }
        statistics2.zeroAll(8);
        final ArmorSet set = SMaterial.getIncompleteArmorSet(inventory);
        if (set instanceof TickingSet) {
            ((TickingSet) set).tick(player, SItem.find(inventory.getHelmet()), SItem.find(inventory.getChestplate()), SItem.find(inventory.getLeggings()), SItem.find(inventory.getBoots()), counters2);
        }
        String skysim = Sputnik.trans("&e&lSKYBLOCK");
        final Boolean isNotCracked = Bukkit.getServer().getOnlineMode();
        if (Repeater.SBA_MAP.containsKey(uuid)) {
            if (Repeater.SBA_MAP.get(uuid)) {
                skysim = ChatColor.translateAlternateColorCodes('&', "&e&lSKYBLOCK");
            } else if (isNotCracked) {
                skysim = ChatColor.translateAlternateColorCodes('&', "&e&lSKYBLOCK &b&lBETA");
            } else {
                skysim = ChatColor.translateAlternateColorCodes('&', "&e&lSKYBLOCK &b&lBETA");
            }
        } else if (isNotCracked) {
            skysim = ChatColor.translateAlternateColorCodes('&', "&e&lSKYBLOCK &b&lBETA");
        } else {
            skysim = ChatColor.translateAlternateColorCodes('&', "&e&lSKYBLOCK &b&lBETA");
        }
        String finalSkysim = skysim;
        SUtil.runSync(()->{
            Sidebar sidebar = new Sidebar(finalSkysim, "SKYBLOCK");
            SUtil.runAsync(()->{
                String strd = SUtil.getDate();
                if (RebootServerCommand.secondMap.containsKey(Bukkit.getServer())) {
                    if (RebootServerCommand.secondMap.get(Bukkit.getServer()) >= 10) {
                        strd = ChatColor.RED + "Server closing: 00:" + RebootServerCommand.secondMap.get(Bukkit.getServer());
                    } else {
                        strd = ChatColor.RED + "Server closing: 00:0" + RebootServerCommand.secondMap.get(Bukkit.getServer());
                    }
                }
                sidebar.add(ChatColor.GRAY + strd + " " + ChatColor.DARK_GRAY + Skyblock.getPlugin().getServerName());
                sidebar.add("  ");
                sidebar.add(" " + SkyBlockCalendar.getMonthName() + " " + SUtil.ntify(SkyBlockCalendar.getDay()));
                boolean day = true;
                int time = (int) (SkyBlockCalendar.ELAPSED % 24000L - 6000L);
                if (time < 0) {
                    time += 24000;
                }
                int hours = 6 + time / 1000;
                int minutes = (int) (time % ((hours - 6) * 1000.0) / 16.66666);
                final String sMin = String.valueOf(minutes);
                minutes -= Integer.parseInt(sMin.substring(sMin.length() - 1));
                if (hours >= 24) {
                    hours -= 24;
                }
                if (hours <= 6 || hours >= 20) {
                    day = false;
                }
                sidebar.add(ChatColor.GRAY + " " + ((hours > 12) ? (hours - 12) : ((hours == 0) ? 12 : hours)) + ":" + SUtil.zeroed(minutes) + ((hours >= 12) ? "pm" : "am") + " " + (day ? (ChatColor.YELLOW + "☀") : (ChatColor.AQUA + "☽")));

                String location = ChatColor.GRAY + "None";
                final Region region = Region.getRegionOfEntity(player);
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
                }
                if (player.getWorld().getName().equalsIgnoreCase("dragon")) {
                    sidebar.add(ChatColor.GRAY + " ⏣ " + ChatColor.DARK_PURPLE + "Dragon's Nest");
                } else if (player.getWorld().getName().startsWith("f6")) {
                    sidebar.add(ChatColor.GRAY + " ⏣ " + ChatColor.RED + "Catacombs Demo" + ChatColor.GRAY + " (F6)");
                } else if (player.getWorld().getName().contains("arena")) {
                    sidebar.add(ChatColor.GRAY + " ⏣ " + ChatColor.RED + "Withering Ruins");
                } else if (player.getWorld().getName().contains("gisland")) {
                    sidebar.add(ChatColor.GRAY + " ⏣ " + ChatColor.YELLOW + "Giant's Island");
                } else {
                    sidebar.add(ChatColor.GRAY + " ⏣ " + location);
                }
                if (!player.getWorld().getName().startsWith("f6") && !player.getWorld().getName().equalsIgnoreCase("arena")) {
                    sidebar.add(" ");
                    final StringBuilder coinsDisplay = new StringBuilder();
                    if (user.isPermanentCoins()) {
                        coinsDisplay.append("Purse: ");
                    } else if (PlayerUtils.hasItem(player, SMaterial.PIGGY_BANK) || PlayerUtils.hasItem(player, SMaterial.CRACKED_PIGGY_BANK)) {
                        coinsDisplay.append("Piggy: ");
                    } else {
                        coinsDisplay.append("Purse: ");
                    }
                    sidebar.add(coinsDisplay.append(ChatColor.GOLD).append(SUtil.commaify(user.getCoins())) + ".0" + ChatColor.YELLOW);
                  //  final String bits = PlaceholderAPI.setPlaceholders(player, "%royaleeconomy_balance_purse%") + " " + PlaceholderAPI.setPlaceholders(player, "%royaleeconomy_dynamic_coins%");
                    String bits = "0";
                    sidebar.add("Bits: " + ChatColor.AQUA + bits);
                    sidebar.add("   ");
                    final SlayerQuest quest = user.getSlayerQuest();
                    if ((!StaticDragonManager.ACTIVE || StaticDragonManager.DRAGON == null || !player.getWorld().getName().equalsIgnoreCase("dragon")) && quest != null && (quest.getDied() == 0L || quest.getKilled() != 0L)) {
                        sidebar.add("Slayer Quest");
                        sidebar.add(quest.getType().getDisplayName());
                        if (quest.getKilled() != 0L) {
                            sidebar.add(ChatColor.GREEN + "Boss slain!");
                        } else if (quest.getXp() >= quest.getType().getSpawnXP()) {
                            sidebar.add(ChatColor.YELLOW + "Slay the boss!");
                        } else if (quest.getLastKilled() != null) {
                            double xpDropped = quest.getLastKilled().getStatistics().getXPDropped();
                            if (PlayerUtils.getCookieDurationTicks(player) > 0L) {
                                xpDropped += quest.getLastKilled().getStatistics().getXPDropped() * 35.0 / 100.0;
                            }
                            sidebar.add(ChatColor.YELLOW + " " + SUtil.commaify((int) (quest.getXp() / xpDropped)) + ChatColor.GRAY + "/" + ChatColor.RED + SUtil.commaify((int) (quest.getType().getSpawnXP() / xpDropped)) + ChatColor.GRAY + " Kills");
                        } else {
                            sidebar.add(ChatColor.GRAY + " (" + ChatColor.YELLOW + SUtil.commaify((int) quest.getXp()) + ChatColor.GRAY + "/" + ChatColor.RED + Sputnik.formatFull((float) quest.getType().getSpawnXP()) + ChatColor.GRAY + ") Combat XP");
                        }
                        sidebar.add("    ");
                    }
                    if (StaticDragonManager.ACTIVE && StaticDragonManager.DRAGON != null && player.getWorld().getName().equalsIgnoreCase("dragon")) {
                        sidebar.add("Dragon HP: " + ChatColor.GREEN + SUtil.commaify((int) StaticDragonManager.DRAGON.getEntity().getHealth()) + ChatColor.RED + " ❤");
                        int dmgdealt;
                        if (StaticDragonManager.DRAGON.getDamageDealt().containsKey(uuid)) {
                            final double dealt = StaticDragonManager.DRAGON.getDamageDealt().get(uuid);
                            dmgdealt = (int) Math.round(dealt);
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
                    if (Repeater.FloorLivingSec.containsKey(player.getWorld().getUID())) {
                        sidebar.add(ChatColor.translateAlternateColorCodes('&', "&fTime Elapsed: &a" + Sputnik.formatTime(Repeater.FloorLivingSec.get(player.getWorld().getUID()))));
                    } else {
                        sidebar.add(ChatColor.translateAlternateColorCodes('&', "&fTime Elapsed: &a00m 00s"));
                    }
                    sidebar.add(ChatColor.translateAlternateColorCodes('&', "&fDungeon Cleared: &cN/A%"));
                    sidebar.add(ChatColor.RED + "  ");
                    final String nameofplayer = player.getName();
                    if (player.getWorld().getPlayers().size() > 1) {
                        for (final Player dungeonmate : player.getWorld().getPlayers()) {
                            String colorcode;
                            if (dungeonmate.getHealth() <= dungeonmate.getMaxHealth() / 2.0 && dungeonmate.getHealth() > dungeonmate.getMaxHealth() * 25.0 / 100.0) {
                                colorcode = "e";
                            } else if (dungeonmate.getHealth() <= dungeonmate.getMaxHealth() * 25.0 / 100.0) {
                                colorcode = "c";
                            } else {
                                colorcode = "a";
                            }
                            final String backend = " &" + colorcode + (int) dungeonmate.getHealth() + "&c❤";
                            if (dungeonmate.getName() == nameofplayer) {
                                continue;
                            }
                            sidebar.add(ChatColor.translateAlternateColorCodes('&', "&e[N/A&e] &b" + dungeonmate.getName() + backend));
                        }
                    } else if (player.getWorld().getPlayers().size() == 1) {
                        sidebar.add(ChatColor.DARK_GRAY + "No Teammates");
                    } else if (player.getWorld().getPlayers().size() > 5) {
                        sidebar.add(ChatColor.RED + "Something went wrong!");
                    }
                    sidebar.add(ChatColor.AQUA + "     ");
                }
                if (player.getWorld().getName().contains("arena") && Skyblock.getPlugin().dimoon == null && Skyblock.getPlugin().sq != null) {
                    final SummoningSequence sq = Skyblock.getPlugin().sq;
                    sidebar.add(Sputnik.trans("&l"));
                    sidebar.add(Sputnik.trans("&aCatalysts &fPlaced &7(&e" + sq.catalystInTheAltar() + "&7/&a8&7)"));
                    sidebar.add(Sputnik.trans("&6Crystal &fStatus " + (sq.isAcD() ? "&b✬" : "&7✬") + (sq.isAcR() ? "&c✬" : "&7✬") + (sq.isAcG() ? "&e✬" : "&7✬") + (sq.isAcE() ? "&a✬" : "&7✬")));
                    if (sq.isBossSpawning()) {
                        sidebar.add(Sputnik.trans("&d"));
                        sidebar.add(Sputnik.trans("&cThe Boss is Spawning..."));
                    }
                    sidebar.add(Sputnik.trans("&c"));
                }
                if (player.getWorld().getName().equalsIgnoreCase("arena") && Skyblock.getPlugin().dimoon != null) {
                    final Set<Integer> damageSet = ((HashMultimap) Multimaps.invertFrom((Multimap) Skyblock.getPlugin().dimoon.getDamages(), (Multimap) HashMultimap.create())).get(player.getName());
                    final int damageDealt = damageSet.iterator().hasNext() ? damageSet.iterator().next() : 0;
                    sidebar.add(Sputnik.trans("&b"));
                    sidebar.add(Sputnik.trans("Dimoon Boss HP: &a" + SUtil.commaify(Skyblock.getPlugin().dimoon.getHealth()) + " &c❤"));
                    sidebar.add(Sputnik.trans("Boss Stunned: " + (Skyblock.getPlugin().dimoon.stunned ? "&a&lYES" : "&c&lNO")));
                    sidebar.add(Sputnik.trans("Parkours Completed: &e" + Skyblock.getPlugin().dimoon.getParkoursCompleted()));
                    sidebar.add(Sputnik.trans("&c&l"));
                    sidebar.add(Sputnik.trans("Your Status: " + (user.isInDanger() ? "&c&lDANGER!" : "&a&lSAFE")));
                    sidebar.add(Sputnik.trans("Your Damage: &c" + SUtil.commaify(damageDealt)));
                    sidebar.add(Sputnik.trans("&a"));
                }
                if (Repeater.SBA_MAP.containsKey(uuid)) {
                    if (Repeater.SBA_MAP.get(uuid)) {
                        sidebar.add(ChatColor.YELLOW + "www.hypixel.net");
                    } else {
                        sidebar.add(ChatColor.YELLOW + "mc.skysim.sbs");
                    }
                } else {
                    sidebar.add(ChatColor.YELLOW + "mc.skysim.sbs");
                }



            if (!player.getWorld().getName().equalsIgnoreCase("limbo") && !player.getWorld().getName().equalsIgnoreCase("dungeon")) {
                sidebar.apply(player);


            } else if (player.getWorld().getName().equalsIgnoreCase("limbo")) {
                final Sidebar sidebar2 = new Sidebar("" + ChatColor.YELLOW + ChatColor.BOLD + "SKYSIM " + ChatColor.RED + ChatColor.BOLD + "LIMBO", "SKYSIM1");
                sidebar2.add(ChatColor.GRAY + SUtil.getDate() + " " + ChatColor.DARK_GRAY + "mini12D7");
                sidebar2.add("     ");
                sidebar2.add("You are currently in" + ChatColor.RED);
                sidebar2.add("the " + ChatColor.RED + "Limbo " + ChatColor.WHITE + "server.");
                sidebar2.add("Use " + ChatColor.GOLD + "/hub " + ChatColor.WHITE + "to");
                sidebar2.add("continue playing.");
                sidebar2.add("If your connection is");
                sidebar2.add("unstable, stay here!");
                sidebar2.add(ChatColor.AQUA + "     ");
                sidebar2.add(ChatColor.YELLOW + "skysim.sbs");

                sidebar2.apply(player);
            }
           });
        });
    }

    public static String get(final Player p) {
        if (VoidlingsWardenHelmet.getDisplay(p) != "") {
            return " " + VoidlingsWardenHelmet.getDisplay(p);
        }
        return "";
    }

    static {
        MANA_MAP = new HashMap<UUID, Integer>();
        MANA_REGEN_DEC = new HashMap<UUID, Boolean>();
        SBA_MAP = new HashMap<UUID, Boolean>();
        DEFENSE_REPLACEMENT_MAP = new HashMap<UUID, DefenseReplacement>();
        MANA_REPLACEMENT_MAP = new HashMap<UUID, ManaReplacement>();
        BEACON_THROW2 = new HashMap<UUID, Entity>();
        BEACON_OWNER = new HashMap<Entity, Player>();
        BEACON = new HashMap<Entity, EntityFallingBlock>();
        PTN_CACHE = new HashMap<UUID, Integer>();
        Repeater.EFFECT_COUNTING = 0;
        FloorLivingSec = new HashMap<UUID, Integer>();
    }
}
