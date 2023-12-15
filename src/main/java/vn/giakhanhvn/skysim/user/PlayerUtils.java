package vn.giakhanhvn.skysim.user;

import java.util.*;

import vn.giakhanhvn.skysim.gui.SlayerGUI;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.MaterialStatistics;
import vn.giakhanhvn.skysim.extra.protocol.PacketInvoker;
import vn.giakhanhvn.skysim.slayer.SlayerBossType;

import vn.giakhanhvn.skysim.entity.EntityDropType;
import vn.giakhanhvn.skysim.util.SLog;
import vn.giakhanhvn.skysim.entity.EntityDrop;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.slayer.SlayerQuest;
import vn.giakhanhvn.skysim.entity.nms.SlayerBoss;
import vn.giakhanhvn.skysim.skill.CombatSkill;
import vn.giakhanhvn.skysim.entity.dungeons.watcher.Watcher;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.potion.PotionEffect;
import vn.giakhanhvn.skysim.config.Config;
import org.bukkit.World;
import org.bukkit.Location;

import java.io.File;

import vn.giakhanhvn.skysim.util.BlankWorldCreator;

import vn.giakhanhvn.skysim.util.ManaReplacement;

import vn.giakhanhvn.skysim.item.Ability;
import vn.giakhanhvn.skysim.util.DefenseReplacement;
import vn.giakhanhvn.skysim.Repeater;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.item.AbilityActivation;
import org.bukkit.Effect;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.item.armor.VoidlingsWardenHelmet;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.item.weapon.EdibleMace;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.potion.PotionEffectType;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import vn.giakhanhvn.skysim.util.Groups;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.potion.ActivePotionEffect;
import org.bukkit.inventory.ItemStack;
import vn.giakhanhvn.skysim.item.accessory.AccessoryFunction;

import vn.giakhanhvn.skysim.item.armor.ArmorSet;
import vn.giakhanhvn.skysim.item.armor.minichad.MinichadSet;
import vn.giakhanhvn.skysim.item.armor.gigachad.GigachadSet;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.TickingMaterial;
import org.bukkit.entity.Entity;

import org.bukkit.entity.EntityType;
import vn.giakhanhvn.skysim.enchantment.Enchantment;
import vn.giakhanhvn.skysim.item.PlayerBoostStatistics;
import vn.giakhanhvn.skysim.dungeons.ItemSerial;
import vn.giakhanhvn.skysim.enchantment.EnchantmentType;
import vn.giakhanhvn.skysim.reforge.Reforge;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.pet.Pet;
import org.bukkit.Bukkit;
import org.bukkit.inventory.PlayerInventory;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.item.SMaterial;

public final class PlayerUtils {
    public static final Map<UUID, Boolean> AUTO_SLAYER;
    public static final Map<UUID, UUID> USER_SESSION_ID;
    public static final Map<UUID, PlayerStatistics> STATISTICS_CACHE;
    public static final Map<UUID, List<SMaterial>> COOLDOWN_MAP;
    public static final Map<UUID, SEntity> SOUL_EATER_MAP;
    public static final Map<UUID, Long> COOKIE_DURATION_CACHE;
    public static final Map<UUID, Integer> LAST_KILLED_MAPPING;

    public static PlayerStatistics getStatistics(final Player player) {
        final PlayerInventory inv = player.getInventory();
        final SItem helmet = SItem.find(inv.getHelmet());
        final SItem chestplate = SItem.find(inv.getChestplate());
        final SItem leggings = SItem.find(inv.getLeggings());
        final SItem boots = SItem.find(inv.getBoots());
        final SItem hand = SItem.find(inv.getItemInHand());
        final List<SItem> items = Arrays.asList(helmet, chestplate, leggings, boots);
        final PlayerStatistics statistics = PlayerStatistics.blank(player.getUniqueId());
        for (int i = 0; i < items.size(); ++i) {
            final SItem sItem = items.get(i);
            updateArmorStatistics(sItem, statistics, i);
        }
        updateSetStatistics(player, helmet, chestplate, leggings, boots, statistics);
        updateHandStatistics(hand, statistics);
        updatePetStatistics(statistics);
        updateInventoryStatistics(player, statistics);
        return statistics;
    }

    public static PlayerStatistics updateHandStatistics(final SItem hand, final PlayerStatistics statistics) {
        double hpbwea = 0.0;
        double ferogrant = 0.0;
        double mfgrant = 0.0;
        double a = 0.0;
        final Player player = Bukkit.getPlayer(statistics.getUuid());
        final User user = User.getUser(player.getUniqueId());
        final Pet.PetItem active = user.getActivePet();
        int level = 0;
        Pet pet = (Pet) SMaterial.GUNGA_PET.getGenericInstance();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic trueDefense = statistics.getTrueDefense();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        statistics.zeroAll(4);
        statistics.getFerocity().set(153, Double.valueOf(user.getBonusFerocity()));
        if (hand != null && hand.getType().getStatistics().getType() != GenericItemType.ARMOR && hand.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
            final Reforge reforge = (hand.getReforge() == null) ? Reforge.blank() : hand.getReforge();
            strength.set(4, reforge.getStrength().getForRarity(hand.getRarity()));
            critDamage.set(4, reforge.getCritDamage().getForRarity(hand.getRarity()));
            critChance.set(4, reforge.getCritChance().getForRarity(hand.getRarity()));
            intelligence.set(4, reforge.getIntelligence().getForRarity(hand.getRarity()));
            ferocity.set(4, reforge.getFerocity().getForRarity(hand.getRarity()));
            atkSpeed.set(4, reforge.getAttackSpeed().getForRarity(hand.getRarity()));
            final PlayerBoostStatistics handStatistics = hand.getType().getBoostStatistics();
            if (handStatistics != null) {
                strength.add(4, Double.valueOf(handStatistics.getBaseStrength()));
                critDamage.add(4, Double.valueOf(handStatistics.getBaseCritDamage()));
                critChance.add(4, Double.valueOf(handStatistics.getBaseCritChance()));
                intelligence.add(4, Double.valueOf(handStatistics.getBaseIntelligence()));
                ferocity.add(4, Double.valueOf(handStatistics.getBaseFerocity()));
                atkSpeed.add(4, Double.valueOf(handStatistics.getBaseAttackSpeed()));
            }
        } else {
            strength.zero(4);
            intelligence.zero(4);
            critChance.zero(4);
            critDamage.zero(4);
            intelligence.zero(4);
            speed.zero(4);
            ferocity.zero(4);
            atkSpeed.zero(4);
        }
        if (hand != null && hand.getType() == SMaterial.TERMINATOR) {
            critChance.sub(4, Double.valueOf(statistics.getCritChance().addAll() - statistics.getCritChance().addAll() / 4.0));
        }
        if (hand != null) {
            if (hand.getEnchantment(EnchantmentType.VICIOUS) != null) {
                ferogrant = hand.getEnchantment(EnchantmentType.VICIOUS).getLevel();
            }
            if (hand.getEnchantment(EnchantmentType.LUCKINESS) != null) {
                mfgrant = hand.getEnchantment(EnchantmentType.LUCKINESS).getLevel() * 0.02;
            }
        }
        if (hand != null && hand.getEnchantment(EnchantmentType.CHIMERA) != null) {
            final double lvl = hand.getEnchantment(EnchantmentType.CHIMERA).getLevel();
            if (active != null) {
                level = Pet.getLevel(active.getXp(), active.getRarity());
                pet = (Pet) active.getType().getGenericInstance();
                a = 20.0 * lvl / 100.0;
            }
        }
        if (hand != null && hand.getDataInt("hpb") > 0) {
            hpbwea = hand.getDataInt("hpb") * 2;
        }
        ferocity.add(4, Double.valueOf(ferogrant));
        magicFind.add(4, Double.valueOf(mfgrant));
        strength.add(4, Double.valueOf(hpbwea));
        if (hand != null && hand.getType().getStatistics().getType() != GenericItemType.ARMOR && (user.toBukkitPlayer().getWorld().getName().contains("f6") || user.toBukkitPlayer().getWorld().getName().contains("dungeon"))) {
            final ItemSerial is = ItemSerial.getItemBoostStatistics(hand);
            defense.add(4, Double.valueOf(is.getDefense()));
            strength.add(4, Double.valueOf(is.getStrength()));
            intelligence.add(4, Double.valueOf(is.getIntelligence()));
            critChance.add(4, Double.valueOf(is.getCritchance()));
            critDamage.add(4, Double.valueOf(is.getCritdamage()));
            speed.add(4, Double.valueOf(is.getSpeed()));
            magicFind.add(4, Double.valueOf(is.getMagicFind()));
            atkSpeed.add(4, Double.valueOf(is.getAtkSpeed()));
            ferocity.add(4, Double.valueOf(is.getFerocity()));
        }
        defense.add(4, Double.valueOf(a * (pet.getPerDefense() * level)));
        strength.add(4, Double.valueOf(a * (pet.getPerStrength() * level)));
        intelligence.add(4, Double.valueOf(a * (pet.getPerIntelligence() * level)));
        speed.add(4, Double.valueOf(a * (pet.getPerSpeed() * level)));
        critChance.add(4, Double.valueOf(a * (pet.getPerCritChance() * level)));
        critDamage.add(4, Double.valueOf(a * (pet.getPerCritDamage() * level)));
        magicFind.add(4, Double.valueOf(a * (pet.getPerMagicFind() * level)));
        trueDefense.add(4, Double.valueOf(a * (pet.getPerTrueDefense() * level)));
        ferocity.add(4, Double.valueOf(a * (pet.getPerFerocity() * level)));
        atkSpeed.add(4, Double.valueOf(a * (pet.getPerAttackSpeed() * level)));
        updateHealth(Bukkit.getPlayer(statistics.getUuid()), statistics);
        return statistics;
    }

    public static PlayerStatistics updateArmorStatistics(final SItem piece, final PlayerStatistics statistics, final int slot) {
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic trueDefense = statistics.getTrueDefense();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        statistics.zeroAll(slot);
        if (piece != null) {
            if (piece.getType().getStatistics().getType() != GenericItemType.ARMOR) {
                return statistics;
            }
            final Reforge reforge = (piece.getReforge() == null) ? Reforge.blank() : piece.getReforge();
            strength.set(slot, reforge.getStrength().getForRarity(piece.getRarity()));
            critDamage.set(slot, reforge.getCritDamage().getForRarity(piece.getRarity()));
            critChance.set(slot, reforge.getCritChance().getForRarity(piece.getRarity()));
            intelligence.set(slot, reforge.getIntelligence().getForRarity(piece.getRarity()));
            ferocity.set(slot, reforge.getFerocity().getForRarity(piece.getRarity()));
            atkSpeed.set(slot, reforge.getAttackSpeed().getForRarity(piece.getRarity()));
            final PlayerBoostStatistics pieceStatistics = piece.getType().getBoostStatistics();
            if (pieceStatistics != null) {
                addBoostStatistics(statistics, slot, pieceStatistics);
            }
            final Player player = Bukkit.getPlayer(statistics.getUuid());
            final PlayerInventory inv = player.getInventory();
            final SItem helm = SItem.find(inv.getHelmet());
            final SItem chest = SItem.find(inv.getChestplate());
            final SItem legs = SItem.find(inv.getLeggings());
            final SItem boot = SItem.find(inv.getBoots());
            if (piece.getDataInt("hpb") > 0) {
                final double hphpb = piece.getDataInt("hpb") * 4;
                final double defhpb = piece.getDataInt("hpb") * 2;
                health.add(slot, Double.valueOf(hphpb));
                defense.add(slot, Double.valueOf(defhpb));
            }
            if (piece.isEnchantable()) {
                for (final Enchantment enchantment : piece.getEnchantments()) {
                    if (enchantment.getType() == EnchantmentType.GROWTH) {
                        health.add(slot, Double.valueOf(15.0 * enchantment.getLevel()));
                    }
                    if (enchantment.getType() == EnchantmentType.PROTECTION) {
                        defense.add(slot, Double.valueOf(3.0 * enchantment.getLevel()));
                    }
                    if (enchantment.getType() == EnchantmentType.LUCKINESS) {
                        magicFind.add(slot, Double.valueOf(0.02 * enchantment.getLevel()));
                    }
                    if (enchantment.getType() == EnchantmentType.LEGION) {
                        final int level = enchantment.getLevel();
                        final List<Entity> nearbyplayer = player.getNearbyEntities(30.0, 30.0, 30.0);
                        nearbyplayer.removeIf(entity -> entity.getType() != EntityType.PLAYER || entity.hasMetadata("NPC"));
                        final double nbps = Math.min(20, nearbyplayer.size());
                        double multiper = level * 0.07 * nbps;
                        multiper = Math.max(0.0, multiper);
                        multiper /= 100.0;
                        defense.add(slot, Double.valueOf(multiper * statistics.getDefense().addAll()));
                        strength.add(slot, Double.valueOf(multiper * statistics.getStrength().addAll()));
                        intelligence.add(slot, Double.valueOf(multiper * statistics.getIntelligence().addAll()));
                        speed.add(slot, Double.valueOf(multiper * statistics.getSpeed().addAll()));
                        critChance.add(slot, Double.valueOf(multiper * statistics.getCritChance().addAll()));
                        critDamage.add(slot, Double.valueOf(multiper * statistics.getCritDamage().addAll()));
                        magicFind.add(slot, Double.valueOf(multiper * statistics.getMagicFind().addAll()));
                        trueDefense.add(slot, Double.valueOf(multiper * statistics.getTrueDefense().addAll()));
                        ferocity.add(slot, Double.valueOf(multiper * statistics.getFerocity().addAll()));
                        atkSpeed.add(slot, Double.valueOf(multiper * statistics.getAttackSpeed().addAll()));
                    }
                }
            }
            if (piece != null && (player.getWorld().getName().contains("f6") || player.getWorld().getName().contains("dungeon"))) {
                final ItemSerial is = ItemSerial.getItemBoostStatistics(piece);
                health.add(slot, Double.valueOf(is.getHealth()));
                defense.add(slot, Double.valueOf(is.getDefense()));
                strength.add(slot, Double.valueOf(is.getStrength()));
                intelligence.add(slot, Double.valueOf(is.getIntelligence()));
                critChance.add(slot, Double.valueOf(is.getCritchance()));
                critDamage.add(slot, Double.valueOf(is.getCritdamage()));
                speed.add(slot, Double.valueOf(is.getSpeed()));
                magicFind.add(slot, Double.valueOf(is.getMagicFind()));
                atkSpeed.add(slot, Double.valueOf(is.getAtkSpeed()));
                ferocity.add(slot, Double.valueOf(is.getFerocity()));
            }
            if (piece.getType() == SMaterial.WARDEN_HELMET || piece.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
                speed.sub(slot, Double.valueOf(statistics.getSpeed().addAll() / 2.0));
            }
            final TickingMaterial tickingMaterial = piece.getType().getTickingInstance();
            if (tickingMaterial != null) {
                statistics.tickItem(slot, tickingMaterial.getInterval(), () -> tickingMaterial.tick(piece, Bukkit.getPlayer(statistics.getUuid())));
            }
        }
        updateHealth(Bukkit.getPlayer(statistics.getUuid()), statistics);
        return statistics;
    }

    public static PlayerStatistics updatePetStatistics(final PlayerStatistics statistics) {
        final Player player = Bukkit.getPlayer(statistics.getUuid());
        final User user = User.getUser(player.getUniqueId());
        final Pet.PetItem active = user.getActivePet();
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic trueDefense = statistics.getTrueDefense();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        final DoublePlayerStatistic abilityDamage = statistics.getAbilityDamage();
        if (active != null) {
            final int level = Pet.getLevel(active.getXp(), active.getRarity());
            final Pet pet = (Pet) active.getType().getGenericInstance();
            health.set(7, Double.valueOf(pet.getPerHealth() * level));
            defense.set(7, Double.valueOf(pet.getPerDefense() * level));
            strength.set(7, Double.valueOf(pet.getPerStrength() * level));
            intelligence.set(7, Double.valueOf(pet.getPerIntelligence() * level));
            speed.set(7, Double.valueOf(pet.getPerSpeed() * level));
            critChance.set(7, Double.valueOf(pet.getPerCritChance() * level));
            critDamage.set(7, Double.valueOf(pet.getPerCritDamage() * level));
            magicFind.set(7, Double.valueOf(pet.getPerMagicFind() * level));
            trueDefense.set(7, Double.valueOf(pet.getPerTrueDefense() * level));
            ferocity.set(7, Double.valueOf(pet.getPerFerocity() * level));
            atkSpeed.set(7, Double.valueOf(pet.getPerAttackSpeed() * level));
            abilityDamage.set(7, Double.valueOf(0.0));
        }
        final double defense2 = statistics.getDefense().addAll();
        final double strength2 = statistics.getStrength().addAll();
        final double intelligence2 = statistics.getIntelligence().addAll();
        final double speed2 = statistics.getSpeed().addAll();
        final double critChance2 = statistics.getCritChance().addAll();
        final double critDamage2 = statistics.getCritDamage().addAll();
        final double magicFind2 = statistics.getMagicFind().addAll();
        final double trueDefense2 = statistics.getTrueDefense().addAll();
        final double ferocity2 = statistics.getFerocity().addAll() - statistics.getFerocity().getFor(153);
        final double atkSpeed2 = statistics.getAttackSpeed().addAll();
        if (active != null) {
            final int level2 = Pet.getLevel(active.getXp(), active.getRarity());
            final Pet pet2 = (Pet) active.getType().getGenericInstance();
            if ((pet2.getDisplayName().equals("Ender Dragon") || pet2.getDisplayName().equals("Golden Tiger")) && active.getRarity().isAtLeast(Rarity.LEGENDARY)) {
                final double LevelMul = level2 * 0.1;
                defense.add(7, Double.valueOf(defense2 * LevelMul / 100.0));
                strength.add(7, Double.valueOf(strength2 * LevelMul / 100.0));
                intelligence.add(7, Double.valueOf(intelligence2 * LevelMul / 100.0));
                speed.add(7, Double.valueOf(speed2 * LevelMul / 100.0));
                critChance.add(7, Double.valueOf(critChance2 * LevelMul / 100.0));
                critDamage.add(7, Double.valueOf(critDamage2 * LevelMul / 100.0));
                magicFind.add(7, Double.valueOf(magicFind2 * LevelMul / 100.0));
                trueDefense.add(7, Double.valueOf(trueDefense2 * LevelMul / 100.0));
                ferocity.add(7, Double.valueOf(ferocity2 * LevelMul / 100.0));
                atkSpeed.add(7, Double.valueOf(atkSpeed2 * LevelMul / 100.0));
            }
            if (pet2.getDisplayName().equals("Mini T-34")) {
                final double LevelMul = level2 * 0.2;
                defense.add(7, Double.valueOf(defense2 * LevelMul / 100.0));
                strength.add(7, Double.valueOf(strength2 * LevelMul / 100.0));
                intelligence.add(7, Double.valueOf(intelligence2 * LevelMul / 100.0));
                speed.add(7, Double.valueOf(speed2 * LevelMul / 100.0));
                critChance.add(7, Double.valueOf(critChance2 * LevelMul / 100.0));
                critDamage.add(7, Double.valueOf(critDamage2 * LevelMul / 100.0));
                magicFind.add(7, Double.valueOf(magicFind2 * LevelMul / 100.0));
                trueDefense.add(7, Double.valueOf(trueDefense2 * LevelMul / 100.0));
                ferocity.add(7, Double.valueOf(ferocity2 * LevelMul / 100.0));
                atkSpeed.add(7, Double.valueOf(atkSpeed2 * LevelMul / 100.0));
            } else if (pet2.getDisplayName().equals("Black Cat")) {
                magicFind.add(7, Double.valueOf(0.0015 * level2));
                speed.add(7, Double.valueOf(0.01 * level2));
            } else if (pet2.getDisplayName().equals("Baby Yeti")) {
                defense.add(7, Double.valueOf(strength2 * level2 / 100.0));
            } else if (pet2.getDisplayName().equals("Golden Tiger")) {
                magicFind.add(7, Double.valueOf(strength2 / 100.0 / 100.0));
                if (active.getRarity().isAtLeast(Rarity.MYTHIC)) {
                    final Economy e = SkySimEngine.getEconomy();
                    int count = 0;
                    for (long num = (long) e.getBalance(player); num != 0L; num /= 10L, ++count) {
                    }
                    ferocity.add(7, Double.valueOf(count * (level2 * 0.1)));
                    magicFind.add(7, Double.valueOf(count * (level2 * 0.05) / 100.0));
                }
            } else if (pet2.getDisplayName().equals("Magicivy")) {
                abilityDamage.add(7, Double.valueOf(level2));
            }
        } else {
            statistics.zeroAll(7);
        }
        updateHealth(player, statistics);
        return statistics;
    }

    public static PlayerStatistics updateSetStatistics(final Player player, final SItem helmet, final SItem chestplate, final SItem leggings, final SItem boots, final PlayerStatistics statistics) {
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        final DoublePlayerStatistic trueDefense = statistics.getTrueDefense();
        if (helmet != null && chestplate != null && leggings != null && boots != null) {
            final ArmorSet set = SMaterial.findArmorSet(helmet.getType(), chestplate.getType(), leggings.getType(), boots.getType());
            statistics.setArmorSet(set);
            statistics.zeroAll(5);
            if (set != null) {
                final PlayerBoostStatistics boost = set.whileHasFullSet(player);
                if (boost != null) {
                    health.set(5, Double.valueOf(boost.getBaseHealth()));
                    defense.set(5, Double.valueOf(boost.getBaseDefense()));
                    strength.set(5, Double.valueOf(boost.getBaseStrength()));
                    intelligence.set(5, Double.valueOf(boost.getBaseIntelligence()));
                    speed.set(5, Double.valueOf(boost.getBaseSpeed()));
                    critChance.set(5, Double.valueOf(boost.getBaseCritChance()));
                    critDamage.set(5, Double.valueOf(boost.getBaseCritDamage()));
                    magicFind.set(5, Double.valueOf(boost.getBaseMagicFind()));
                    ferocity.set(5, Double.valueOf(boost.getBaseFerocity()));
                    atkSpeed.set(5, Double.valueOf(boost.getBaseAttackSpeed()));
                }
                if (set instanceof GigachadSet) {
                    final double defense2 = statistics.getDefense().addAll();
                    final double strength2 = statistics.getStrength().addAll();
                    final double intelligence2 = statistics.getIntelligence().addAll();
                    final double speed2 = statistics.getSpeed().addAll();
                    final double critChance2 = statistics.getCritChance().addAll();
                    final double critDamage2 = statistics.getCritDamage().addAll();
                    final double magicFind2 = statistics.getMagicFind().addAll();
                    final double trueDefense2 = statistics.getTrueDefense().addAll();
                    final double ferocity2 = statistics.getFerocity().addAll() - statistics.getFerocity().getFor(153);
                    final double atkSpeed2 = statistics.getAttackSpeed().addAll();
                    final double LevelMul = 20.0;
                    defense.add(5, Double.valueOf(defense2 * LevelMul / 100.0));
                    strength.add(5, Double.valueOf(strength2 * LevelMul / 100.0));
                    intelligence.add(5, Double.valueOf(intelligence2 * LevelMul / 100.0));
                    speed.add(5, Double.valueOf(speed2 * LevelMul / 100.0));
                    critChance.add(5, Double.valueOf(critChance2 * LevelMul / 100.0));
                    critDamage.add(5, Double.valueOf(critDamage2 * LevelMul / 100.0));
                    magicFind.add(5, Double.valueOf(magicFind2 * LevelMul / 100.0));
                    trueDefense.add(5, Double.valueOf(trueDefense2 * LevelMul / 100.0));
                    ferocity.add(5, Double.valueOf(ferocity2 * LevelMul / 100.0));
                    atkSpeed.add(5, Double.valueOf(atkSpeed2 * LevelMul / 100.0));
                } else if (set instanceof MinichadSet) {
                    final double defense2 = statistics.getDefense().addAll();
                    final double strength2 = statistics.getStrength().addAll();
                    final double intelligence2 = statistics.getIntelligence().addAll();
                    final double speed2 = statistics.getSpeed().addAll();
                    final double critChance2 = statistics.getCritChance().addAll();
                    final double critDamage2 = statistics.getCritDamage().addAll();
                    final double magicFind2 = statistics.getMagicFind().addAll();
                    final double trueDefense2 = statistics.getTrueDefense().addAll();
                    final double ferocity2 = statistics.getFerocity().addAll() - statistics.getFerocity().getFor(153);
                    final double atkSpeed2 = statistics.getAttackSpeed().addAll();
                    final double LevelMul = 10.0;
                    defense.add(5, Double.valueOf(defense2 * LevelMul / 100.0));
                    strength.add(5, Double.valueOf(strength2 * LevelMul / 100.0));
                    intelligence.add(5, Double.valueOf(intelligence2 * LevelMul / 100.0));
                    speed.add(5, Double.valueOf(speed2 * LevelMul / 100.0));
                    critChance.add(5, Double.valueOf(critChance2 * LevelMul / 100.0));
                    critDamage.add(5, Double.valueOf(critDamage2 * LevelMul / 100.0));
                    magicFind.add(5, Double.valueOf(magicFind2 * LevelMul / 100.0));
                    trueDefense.add(5, Double.valueOf(trueDefense2 * LevelMul / 100.0));
                    ferocity.add(5, Double.valueOf(ferocity2 * LevelMul / 100.0));
                    atkSpeed.add(5, Double.valueOf(atkSpeed2 * LevelMul / 100.0));
                }
            }
        } else {
            statistics.setArmorSet(null);
            health.zero(5);
            defense.zero(5);
            strength.zero(5);
            intelligence.zero(5);
            speed.zero(5);
            critChance.zero(5);
            critDamage.zero(5);
            magicFind.zero(5);
            ferocity.zero(5);
            atkSpeed.zero(5);
        }
        updateHealth(player, statistics);
        return statistics;
    }

    public static PlayerStatistics updateInventoryStatistics(final Player player, final PlayerStatistics statistics) {
        if (player == null) {
            return null;
        }
        if (statistics == null) {
            return null;
        }
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        final PlayerInventory inventory = player.getInventory();
        final List<SMaterial> materials = new ArrayList<SMaterial>();
        for (int i = 0; i <= inventory.getSize(); ++i) {
            final ItemStack stack = inventory.getItem(i);
            final SItem sItem = SItem.find(stack);
            final int slot = 15 + i;
            if (sItem != null) {
                if (materials.contains(sItem.getType())) {
                    continue;
                }
                if (sItem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
                    continue;
                }
                materials.add(sItem.getType());
                if (sItem.getType().getFunction() instanceof AccessoryFunction) {
                    ((AccessoryFunction) sItem.getType().getFunction()).update(sItem, player, slot);
                }
            }
            statistics.zeroAll(slot);
            if (sItem != null) {
                final Reforge reforge = (sItem.getReforge() == null) ? Reforge.blank() : sItem.getReforge();
                strength.set(slot, reforge.getStrength().getForRarity(sItem.getRarity()));
                critDamage.set(slot, reforge.getCritDamage().getForRarity(sItem.getRarity()));
                critChance.set(slot, reforge.getCritChance().getForRarity(sItem.getRarity()));
                intelligence.set(slot, reforge.getIntelligence().getForRarity(sItem.getRarity()));
                ferocity.set(slot, reforge.getFerocity().getForRarity(sItem.getRarity()));
                atkSpeed.set(slot, reforge.getAttackSpeed().getForRarity(sItem.getRarity()));
                final PlayerBoostStatistics sItemStatistics = sItem.getType().getBoostStatistics();
                if (sItemStatistics != null && sItem.getType().getStatistics().getType() == GenericItemType.ACCESSORY) {
                    addBoostStatistics(statistics, slot, sItemStatistics);
                } else {
                    statistics.zeroAll(slot);
                }
            }
        }
        updateHealth(player, statistics);
        return statistics;
    }

    public static void updateP(final Player p) {
    }

    public static PlayerStatistics updatePotionEffects(User user, PlayerStatistics statistics) {
        DoublePlayerStatistic health = statistics.getMaxHealth();
        DoublePlayerStatistic defense = statistics.getDefense();
        DoublePlayerStatistic strength = statistics.getStrength();
        DoublePlayerStatistic intelligence = statistics.getIntelligence();
        DoublePlayerStatistic ferocity = statistics.getFerocity();
        DoublePlayerStatistic speed = statistics.getSpeed();
        DoublePlayerStatistic critChance = statistics.getCritChance();
        DoublePlayerStatistic critDamage = statistics.getCritDamage();
        DoublePlayerStatistic magicFind = statistics.getMagicFind();
        DoublePlayerStatistic trueDefense = statistics.getTrueDefense();
        DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        for (int i = 0; i < user.getEffects().size(); ++i) {
            ActivePotionEffect effect2 = user.getEffects().get(i);
            int slot = 52 + i;
            health.zero(slot);
            defense.zero(slot);
            strength.zero(slot);
            intelligence.zero(slot);
            speed.zero(slot);
            critChance.zero(slot);
            critDamage.zero(slot);
            magicFind.zero(slot);
            ferocity.zero(slot);
            atkSpeed.zero(slot);
            trueDefense.zero(slot);
            if (!effect2.getEffect().getType().isInstant()) {
                if (effect2.getRemaining() <= 0L || effect2.getEffect().getType().getStatsUpdate() == null) continue;
                effect2.getEffect().getType().getStatsUpdate().accept(statistics, slot, effect2.getEffect().getLevel());
                continue;
            }
            effect2.setRemaining(0L);
        }
        user.getEffects().removeIf(effect -> effect.getRemaining() <= 0L);
        return statistics;
    }

    public static PlayerStatistics boostPlayer(final PlayerStatistics statistics, final PlayerBoostStatistics boostStatistics, final long ticks) {
        if (statistics == null) {
            return null;
        }
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        health.add(6, Double.valueOf(boostStatistics.getBaseHealth()));
        defense.add(6, Double.valueOf(boostStatistics.getBaseDefense()));
        strength.add(6, Double.valueOf(boostStatistics.getBaseStrength()));
        intelligence.add(6, Double.valueOf(boostStatistics.getBaseIntelligence()));
        speed.add(6, Double.valueOf(boostStatistics.getBaseSpeed()));
        critChance.add(6, Double.valueOf(boostStatistics.getBaseCritChance()));
        critDamage.add(6, Double.valueOf(boostStatistics.getBaseCritDamage()));
        magicFind.add(6, Double.valueOf(boostStatistics.getBaseMagicFind()));
        ferocity.add(6, Double.valueOf(boostStatistics.getBaseFerocity()));
        atkSpeed.add(6, Double.valueOf(boostStatistics.getBaseAttackSpeed()));
        updateHealth(Bukkit.getPlayer(statistics.getUuid()), statistics);
        new BukkitRunnable() {
            public void run() {
                health.sub(6, Double.valueOf(boostStatistics.getBaseHealth()));
                defense.sub(6, Double.valueOf(boostStatistics.getBaseDefense()));
                strength.sub(6, Double.valueOf(boostStatistics.getBaseStrength()));
                intelligence.sub(6, Double.valueOf(boostStatistics.getBaseIntelligence()));
                speed.sub(6, Double.valueOf(boostStatistics.getBaseSpeed()));
                critChance.sub(6, Double.valueOf(boostStatistics.getBaseCritChance()));
                critDamage.sub(6, Double.valueOf(boostStatistics.getBaseCritDamage()));
                magicFind.sub(6, Double.valueOf(boostStatistics.getBaseMagicFind()));
                ferocity.sub(6, Double.valueOf(boostStatistics.getBaseFerocity()));
                atkSpeed.sub(6, Double.valueOf(boostStatistics.getBaseAttackSpeed()));
                PlayerUtils.updateHealth(Bukkit.getPlayer(statistics.getUuid()), statistics);
            }
        }.runTaskLater(SkySimEngine.getPlugin(), ticks);
        return statistics;
    }

    public static DamageResult getDamageDealt(final ItemStack weapon, final Player player, final Entity damaged, final boolean arrowHit) {
        final SItem sItem = SItem.find(weapon);
        double rot = (player.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rot < 0.0) {
            rot += 360.0;
        }
        String facingDirection = "null";
        if (0.0 <= rot && rot < 22.5) {
            facingDirection = "North";
        } else if (22.5 <= rot && rot < 67.5) {
            facingDirection = "Northeast";
        } else if (67.5 <= rot && rot < 112.5) {
            facingDirection = "East";
        } else if (112.5 <= rot && rot < 157.5) {
            facingDirection = "Southeast";
        } else if (157.5 <= rot && rot < 202.5) {
            facingDirection = "South";
        } else if (202.5 <= rot && rot < 247.5) {
            facingDirection = "Southwest";
        } else if (247.5 <= rot && rot < 292.5) {
            facingDirection = "West";
        } else if (292.5 <= rot && rot < 337.5) {
            facingDirection = "Northwest";
        } else if (337.5 <= rot && rot < 360.0) {
            facingDirection = "North";
        } else {
            facingDirection = "null";
        }
        double rot2 = (damaged.getLocation().getYaw() - 90.0f) % 360.0f;
        if (rot2 < 0.0) {
            rot2 += 360.0;
        }
        String facingDirection2 = "null";
        if (0.0 <= rot2 && rot2 < 22.5) {
            facingDirection2 = "North";
        } else if (22.5 <= rot2 && rot2 < 67.5) {
            facingDirection2 = "Northeast";
        } else if (67.5 <= rot2 && rot2 < 112.5) {
            facingDirection2 = "East";
        } else if (112.5 <= rot2 && rot2 < 157.5) {
            facingDirection2 = "Southeast";
        } else if (157.5 <= rot2 && rot2 < 202.5) {
            facingDirection2 = "South";
        } else if (202.5 <= rot2 && rot2 < 247.5) {
            facingDirection2 = "Southwest";
        } else if (247.5 <= rot2 && rot2 < 292.5) {
            facingDirection2 = "West";
        } else if (292.5 <= rot2 && rot2 < 337.5) {
            facingDirection2 = "Northwest";
        } else if (337.5 <= rot2 && rot2 < 360.0) {
            facingDirection2 = "North";
        } else {
            facingDirection2 = facingDirection;
        }
        int damage = 0;
        double enchantBonus = 0.0;
        double potionBonus = 0.0;
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        int critChanceMul = (int) (statistics.getCritChance().addAll() * 100.0);
        double critDamage = statistics.getCritDamage().addAll();
        double hpbwea = 0.0;
        final long cap = 35000000000L;
        final double d1 = Math.pow((double) Math.min(cap, User.getUser(player.getUniqueId()).getCoins()), 0.25);
        final double finald = 2.5 * d1;
        final int fd2 = (int) finald;
        double bonusDamage = 0.0;
        final double bonusEn = 0.0;
        double strength = statistics.getStrength().addAll();
        final double strength2 = strength % 10.0;
        final double real = strength - strength2;
        final double critgive = real / 10.0;
        final double critreal = critgive / 100.0;
        final double speed = Math.min(500.0, statistics.getSpeed().addAll() * 100.0);
        final double realSpeed = Math.min(500.0, speed * 100.0) % 25.0;
        final double realSpeedDIV = speed - realSpeed;
        final double realSpeedDIVC = realSpeedDIV / 25.0;
        long leftpercent = 0L;
        double addDmg = 0.0;
        if (damaged instanceof LivingEntity) {
            leftpercent = Math.round(100.0 - ((LivingEntity) damaged).getHealth() / ((LivingEntity) damaged).getMaxHealth() * 100.0);
        }
        final PlayerInventory inv = player.getInventory();
        final SItem helmet = SItem.find(inv.getHelmet());
        if (helmet != null && helmet.getType() == SMaterial.TARANTULA_HELMET) {
            critDamage += critreal;
        }
        if (sItem != null && sItem.getDataInt("hpb") > 0) {
            hpbwea = sItem.getDataInt("hpb") * 2;
        }
        final SItem helm = SItem.find(inv.getHelmet());
        final SItem chest = SItem.find(inv.getChestplate());
        final SItem legs = SItem.find(inv.getLeggings());
        final SItem boot = SItem.find(inv.getBoots());
        final User user = User.getUser(player.getUniqueId());
        final Pet pet = user.getActivePetClass();
        final Pet.PetItem active1 = user.getActivePet();
        if (active1 != null) {
            if (pet.getDisplayName().equals("Ender Dragon")) {
                final Pet.PetItem active2 = user.getActivePet();
                final int level = Pet.getLevel(active2.getXp(), active2.getRarity());
                if (Groups.ENDERMAN.contains(damaged.getType())) {
                    enchantBonus += 0.25 * level / 100.0;
                }
                if (sItem != null && sItem.getType() == SMaterial.ASPECT_OF_THE_DRAGONS) {
                    damage += (int) (level * 0.5);
                    strength += 0.3 * level;
                }
            } else if (pet.getDisplayName().equals("Baby Yeti")) {
                final Pet.PetItem active2 = user.getActivePet();
                final int level = Pet.getLevel(active2.getXp(), active2.getRarity());
                if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SNOW || player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK) {
                    strength += 0.5 * level;
                    critDamage += 0.005 * level;
                }
            } else if (pet.getDisplayName().equals("Golden Tiger")) {
                final Pet.PetItem active2 = user.getActivePet();
                final int level = Pet.getLevel(active2.getXp(), active2.getRarity());
                final long ferocity = Math.round(statistics.getFerocity().addAll());
                addDmg += (int) ferocity / 30 * (level * 0.1);
            } else if (pet.getDisplayName().equals("Archivy") && user.isHeadShot()) {
                critChanceMul = 100;
            }
        }
        if (sItem != null && (sItem.getType().getStatistics().getType() != GenericItemType.RANGED_WEAPON || arrowHit)) {
            final PlayerBoostStatistics playerBoostStatistics = sItem.getType().getBoostStatistics();
            if (playerBoostStatistics != null) {
                damage = playerBoostStatistics.getBaseDamage();
            }
            if (sItem.getType() == SMaterial.EMERALD_BLADE) {
                damage += (int) finald;
            }
            if (sItem.getType().getStatistics().getType() == GenericItemType.WEAPON && sItem.getEnchantment(EnchantmentType.ONE_FOR_ALL) != null) {
                final Enchantment e = sItem.getEnchantment(EnchantmentType.ONE_FOR_ALL);
                damage += damage * (e.getLevel() * 210) / 100;
            }
            if (helmet != null) {
                if (helmet.getType() == SMaterial.WARDEN_HELMET) {
                    damage += (int) (20.0 * realSpeedDIVC / 100.0 * damage);
                } else if (helmet.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
                    damage += (int) (35.0 * realSpeedDIVC / 100.0 * damage);
                }
            }
            damage += (int) bonusEn;
            damage += (int) hpbwea;
            strength += hpbwea;
            damage += (int) addDmg;
            if (helmet != null && helmet.getType() == SMaterial.CROWN_OF_GREED) {
                damage += (int) (Object) PlayerListener.COGCalculation(damage, player);
            }
            if (sItem.getType() == SMaterial.POOCH_SWORD && EntityType.WOLF.equals(damaged.getType())) {
                strength += 150.0;
            }
            if (user.toBukkitPlayer().getWorld().getName().contains("f6") || user.toBukkitPlayer().getWorld().getName().contains("dungeon")) {
                damage += (int) ItemSerial.getItemBoostStatistics(sItem).getDamage();
            }
            if ((sItem.getType() == SMaterial.PRISMARINE_BLADE && player.getLocation().getBlock().getType() == Material.WATER) || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                enchantBonus += 3.0;
            }
            if (sItem.getType() == SMaterial.AXE_OF_THE_SHREDDED && Groups.UNDEAD_MOBS.contains(damaged.getType())) {
                enchantBonus += 3.5;
            }
            if (sItem.getType() == SMaterial.DEATH_BOW && Groups.UNDEAD_MOBS.contains(damaged.getType()) && arrowHit) {
                enchantBonus += 2.0;
            }
            if (sItem.getType() == SMaterial.AXE_OF_THE_SHREDDED) {
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 50.0));
            }
            if (sItem.getType() == SMaterial.REAPER_FALCHION && Groups.UNDEAD_MOBS.contains(damaged.getType())) {
                enchantBonus += 3.0;
            }
            if (sItem.getType() == SMaterial.REAPER_FALCHION) {
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 10.0));
            }
            if (sItem.getType() == SMaterial.VOIDWALKER_KATANA && Groups.ENDERMAN.contains(damaged.getType())) {
                enchantBonus += 2.0;
            }
            if (sItem.getType() == SMaterial.VOIDEDGE_KATANA && Groups.ENDERMAN.contains(damaged.getType())) {
                enchantBonus += 2.5;
            }
            if (sItem.getType() == SMaterial.VORPAL_KATANA && Groups.ENDERMAN.contains(damaged.getType())) {
                enchantBonus += 3.0;
            }
            if (sItem.getType() == SMaterial.ATOMSPLIT_KATANA && Groups.ENDERMAN.contains(damaged.getType())) {
                enchantBonus += 3.5;
            }
            if (sItem.getType() == SMaterial.LIVID_DAGGER && facingDirection == facingDirection2) {
                enchantBonus += 2.0;
            }
            if (sItem.getType() != SMaterial.ENCHANTED_BOOK && sItem.isEnchantable()) {
                for (final Enchantment enchantment : sItem.getEnchantments()) {
                    final EnchantmentType type = enchantment.getType();
                    final int level2 = enchantment.getLevel();
                    if (type == EnchantmentType.SHARPNESS && !arrowHit) {
                        enchantBonus += level2 * 5 / 100.0;
                    }
                    if (type == EnchantmentType.EXECUTE && !arrowHit) {
                        enchantBonus += level2 * 0.2 * leftpercent / 100.0;
                    }
                    if (type == EnchantmentType.FIRE_ASPECT && sItem.getType().getStatistics().getType() == GenericItemType.WEAPON) {
                        damaged.setFireTicks(0);
                    }
                    if (type == EnchantmentType.POWER && arrowHit) {
                        enchantBonus += level2 * 8 / 100.0;
                    }
                    if (type == EnchantmentType.FLAME && arrowHit) {
                        damaged.setFireTicks(0);
                    }
                    if (type == EnchantmentType.ENDER_SLAYER && !arrowHit && Groups.ENDERMAN.contains(damaged.getType())) {
                        enchantBonus += level2 * 12 / 100.0;
                    }
                    if (type == EnchantmentType.DRAGON_HUNTER && Groups.ENDERDRAGON.contains(damaged.getType())) {
                        enchantBonus += level2 * 8 / 100.0;
                    }
                    if (type == EnchantmentType.SMITE && !arrowHit && Groups.UNDEAD_MOBS.contains(damaged.getType())) {
                        enchantBonus += level2 * 8 / 100.0;
                    }
                    if (type == EnchantmentType.BANE_OF_ARTHROPODS && !arrowHit && Groups.ARTHROPODS.contains(damaged.getType())) {
                        enchantBonus += level2 * 8 / 100.0;
                    }
                    if (type == EnchantmentType.CRITICAL) {
                        critDamage += level2 * 10 / 100.0;
                    }
                    if (type == EnchantmentType.SOUL_EATER && PlayerUtils.SOUL_EATER_MAP.containsKey(player.getUniqueId()) && PlayerUtils.SOUL_EATER_MAP.get(player.getUniqueId()) != null) {
                        bonusDamage += PlayerUtils.SOUL_EATER_MAP.get(player.getUniqueId()).getStatistics().getDamageDealt() * (level2 * 2);
                        PlayerUtils.SOUL_EATER_MAP.put(player.getUniqueId(), null);
                    }
                    if (type == EnchantmentType.LIFE_STEAL && level2 > 0) {
                        player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + level2 * 0.5 / 100.0 * player.getMaxHealth()));
                    }
                }
            }
            for (final ActivePotionEffect effect : User.getUser(player.getUniqueId()).getEffects()) {
                final PotionEffectType type2 = effect.getEffect().getType();
                final int level2 = effect.getEffect().getLevel();
                if (type2 == PotionEffectType.ARCHERY && arrowHit) {
                    potionBonus += SUtil.<Double>getOrDefault(Arrays.asList(12.5, 25.0, 50.0, 75.0), level2 - 1, level2 * 25.0 - 25.0) / 100.0;
                }
            }
            if (sItem.getEnchantment(EnchantmentType.FATAL_TEMPO) != null) {
                final int lvl = sItem.getEnchantment(EnchantmentType.FATAL_TEMPO).getLevel();
                final double ferocity2 = statistics.getFerocity().addAll() - statistics.getFerocity().getFor(153);
                user.setBonusFerocity((int) Math.min(ferocity2 * 200.0 / 100.0, user.getBonusFerocity() + ferocity2 * (lvl * 5.0 / 100.0)));
                if (!user.isFatalActive()) {
                    SUtil.delay(() -> {
                        user.setBonusFerocity(0);
                        user.setFatalActive(false);
                    }, 60L);
                }
                user.setFatalActive(true);
            }
            if (player.getItemInHand() != null) {
                final SItem sitem = SItem.of(player.getItemInHand());
                if (sitem != null && sitem.getEnchantment(EnchantmentType.VAMPIRISM) != null) {
                    double lvl2 = sitem.getEnchantment(EnchantmentType.VAMPIRISM).getLevel();
                    if (lvl2 > 100.0) {
                        lvl2 = 100.0;
                    }
                    final double aB = player.getHealth() + lvl2 / 100.0 * (player.getMaxHealth() - player.getHealth());
                    final double aC = Math.min(player.getMaxHealth(), aB);
                    player.setHealth(aC);
                }
            }
        }
        final int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
        final double weaponBonus = 0.0;
        final double armorBonus = 1.0;
        final int chance = SUtil.random(0, 100);
        if (chance > critChanceMul) {
            critDamage = 0.0;
        }
        final double baseDamage = (5 + damage) * (1.0 + strength / 100.0);
        final double damageMultiplier = 1.0 + combatLevel * 0.04 + enchantBonus + weaponBonus;
        final double finalCritDamage = critDamage;
        double finalDamage = baseDamage * damageMultiplier * armorBonus * (1.0 + finalCritDamage) + bonusDamage;
        double finalPotionBonus = potionBonus;
        if (EdibleMace.edibleMace.containsKey(player.getUniqueId()) && EdibleMace.edibleMace.get(player.getUniqueId())) {
            finalDamage *= 2.0;
            finalPotionBonus *= 2.0;
            EdibleMace.edibleMace.put(player.getUniqueId(), false);
        }
        if (sItem != null) {
            if (sItem.getType() == SMaterial.POOCH_SWORD) {
                double health1 = statistics.getMaxHealth().addAll();
                health1 = Math.min(300000.0, health1);
                final double dmggive = health1 % 50.0;
                final double health2 = health1 - dmggive;
                finalDamage += health2 / 50.0;
            }
            if (sItem.getType().getStatistics().getType() == GenericItemType.WEAPON && sItem.getEnchantment(EnchantmentType.FIRST_STRIKE) != null && EntityFunction.FIRST_STRIKE_MAP.containsKey(damaged) && !EntityFunction.FIRST_STRIKE_MAP.get(damaged).contains(player.getUniqueId())) {
                finalDamage += finalDamage * (sItem.getEnchantment(EnchantmentType.FIRST_STRIKE).getLevel() * 25.0 / 100.0);
                EntityFunction.FIRST_STRIKE_MAP.get(damaged).add(player.getUniqueId());
            }
        }
        final double FinalDMG = finalDamage;
        final double finalPot = finalPotionBonus;
        double fds = (FinalDMG + FinalDMG * finalPot) * (VoidlingsWardenHelmet.VOIDLING_WARDEN_BUFF.containsKey(user.getUuid()) ? 1.5 : 1.0);
        if (active1 != null && pet.getDisplayName().equals("Archivy") && arrowHit) {
            final Pet.PetItem active3 = user.getActivePet();
            final int level3 = Pet.getLevel(active3.getXp(), active3.getRarity());
            fds += fds * (level3 / 2.0f) / 100.0;
            if (user.isHeadShot()) {
                fds += fds * (level3 * 0.75f) / 100.0;
                user.toBukkitPlayer().playSound(user.toBukkitPlayer().getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                for (int i = 0; i < 50; ++i) {
                    damaged.getWorld().spigot().playEffect(damaged.getLocation().clone().add(0.0, 1.5, 0.0), Effect.MAGIC_CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 100);
                }
            }
        }
        user.setHeadShot(false);
        final double fdsfinal = fds;
        return new DamageResult() {
            @Override
            public double getFinalDamage() {
                return fdsfinal;
            }

            @Override
            public boolean didCritDamage() {
                return finalCritDamage != 0.0;
            }
        };
    }

    public static void useAbility(final Player player, final SItem sItem) {
        final Ability ability = sItem.getType().getAbility();
        if (ability != null) {
            final AbilityActivation activation = ability.getAbilityActivation();
            if (activation != AbilityActivation.NO_ACTIVATION) {
                final UUID uuid = player.getUniqueId();
                if (ability.requirementsUse(player, sItem)) {
                    player.sendMessage(Sputnik.trans(ability.getAbilityReq()));
                    return;
                }
                if (PlayerUtils.COOLDOWN_MAP.containsKey(uuid) && PlayerUtils.COOLDOWN_MAP.get(uuid).contains(sItem.getType())) {
                    if (ability.displayCooldown()) {
                        player.sendMessage(ChatColor.RED + "You currently have a cooldown for this ability!");
                    }
                } else {
                    final int mana = Repeater.MANA_MAP.get(uuid);
                    final int cost = getFinalManaCost(player, sItem, ability.getManaCost());
                    final int resMana = mana - cost;
                    if (resMana >= 0) {
                        Repeater.MANA_MAP.remove(uuid);
                        Repeater.MANA_MAP.put(uuid, resMana);
                        try {
                            ability.onAbilityUse(player, sItem);
                        } catch (final Exception ex) {
                            ex.printStackTrace();
                        }
                        if (ability.displayUsage() && sItem.getType() != SMaterial.AXE_OF_THE_SHREDDED && sItem.getType() != SMaterial.BONEMERANG && sItem.getType() != SMaterial.SHADOW_FURY && sItem.getType() != SMaterial.ASPECT_OF_THE_JERRY && sItem.getType() != SMaterial.FLOWER_OF_TRUTH && sItem.getType() != SMaterial.EDIBLE_MACE) {
                            final long c = System.currentTimeMillis();
                            Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement() {
                                @Override
                                public String getReplacement() {
                                    return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + ability.getAbilityName() + ChatColor.AQUA + ")";
                                }

                                @Override
                                public long getEnd() {
                                    return c + 2000L;
                                }
                            });
                        }
                        if (ability.getAbilityCooldownTicks() != 0) {
                            if (PlayerUtils.COOLDOWN_MAP.containsKey(uuid)) {
                                PlayerUtils.COOLDOWN_MAP.get(uuid).add(sItem.getType());
                            } else {
                                PlayerUtils.COOLDOWN_MAP.put(uuid, new ArrayList<SMaterial>(Collections.singletonList(sItem.getType())));
                            }
                            new BukkitRunnable() {
                                public void run() {
                                    PlayerUtils.COOLDOWN_MAP.get(uuid).remove(sItem.getType());
                                    if (PlayerUtils.COOLDOWN_MAP.get(uuid).size() == 0) {
                                        PlayerUtils.COOLDOWN_MAP.remove(uuid);
                                    }
                                }
                            }.runTaskLater(SkySimEngine.getPlugin(), ability.getAbilityCooldownTicks());
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                        final long c = System.currentTimeMillis();
                        Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement() {
                            @Override
                            public String getReplacement() {
                                return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                            }

                            @Override
                            public long getEnd() {
                                return c + 1500L;
                            }
                        });
                    }
                }
            }
        }
    }

    public static void updateHealth(final Player player, final PlayerStatistics statistics) {
        if (player == null) {
            return;
        }
        final boolean fill = player.getHealth() == player.getMaxHealth();
        if (player.getMaxHealth() != statistics.getMaxHealth().addAll()) {
            player.setMaxHealth(statistics.getMaxHealth().addAll());
        }
        if (fill) {
            player.setHealth(player.getMaxHealth());
        }
    }

    public static List<SItem> getAccessories(final Player player) {
        final List<SItem> accessories = new ArrayList<SItem>();
        final List<SMaterial> types = new ArrayList<SMaterial>();
        for (final ItemStack stack : player.getInventory()) {
            final SItem sItem = SItem.find(stack);
            if (sItem == null) {
                continue;
            }
            if (sItem.getType().getStatistics().getType() != GenericItemType.ACCESSORY) {
                continue;
            }
            if (types.contains(sItem.getType())) {
                continue;
            }
            accessories.add(sItem);
            types.add(sItem.getType());
        }
        return accessories;
    }

    public static boolean hasItem(final Player player, final SMaterial material) {
        for (final ItemStack stack : player.getInventory()) {
            final SItem sItem = SItem.find(stack);
            if (sItem == null) {
                continue;
            }
            if (sItem.getType() == material) {
                return true;
            }
        }
        return false;
    }

    public static void sendToIsland(final Player player) {
        World world = Bukkit.getWorld("islands");
        if (world == null) {
            world = new BlankWorldCreator("islands").createWorld();
        }
        final User user = User.getUser(player.getUniqueId());
        if (user.getIslandX() == null) {
            final Config config = SkySimEngine.getPlugin().config;
            double xOffset = config.getDouble("islands.x");
            double zOffset = config.getDouble("islands.z");
            if (xOffset < -2.5E7 || xOffset > 2.5E7) {
                zOffset += 250.0;
            }
            final File file = new File(config.getString("islands.schematic"));
            SUtil.pasteSchematic(file, new Location(world, 7.0 + xOffset, 100.0, 7.0 + zOffset), true);
            SUtil.setBlocks(new Location(world, 7.0 + xOffset, 104.0, 44.0 + zOffset), new Location(world, 5.0 + xOffset, 100.0, 44.0 + zOffset), Material.PORTAL, false);
            user.setIslandLocation(7.5 + xOffset, 7.5 + zOffset);
            user.save();
            if (xOffset > 0.0) {
                xOffset *= -1.0;
            } else if (xOffset <= 0.0) {
                if (xOffset != 0.0) {
                    xOffset *= -1.0;
                }
                xOffset += 250.0;
            }
            config.set("islands.x", xOffset);
            config.set("islands.z", zOffset);
            config.save();
        }
        final World finalWorld = world;
        SUtil.delay(() -> player.teleport(finalWorld.getHighestBlockAt(SUtil.blackMagic(user.getIslandX()), SUtil.blackMagic(user.getIslandZ())).getLocation().add(0.5, 1.0, 0.5)), 10L);
    }

    public static PotionEffect getPotionEffect(final Player player, final org.bukkit.potion.PotionEffectType type) {
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().getName().equals(type.getName())) {
                return effect;
            }
        }
        return null;
    }

    public static void replacePotionEffect(final Player player, final PotionEffect add) {
        final PotionEffect effect = getPotionEffect(player, add.getType());
        if (effect != null && effect.getAmplifier() > add.getAmplifier()) {
            return;
        }
        player.removePotionEffect(add.getType());
        player.addPotionEffect(add);
    }

    public static void handleSpecEntity(final Entity entity, final Player damager, final AtomicDouble finalDamage) {
        if (entity.hasMetadata("isDead")) {
            return;
        }
        final SEntity sEntity = SEntity.findSEntity(entity);
        if (sEntity != null) {
            final EntityFunction function = sEntity.getFunction();
            if (damager != null) {
                sEntity.addDamageFor(damager, finalDamage.get());
            }
            if (((LivingEntity) entity).getHealth() - finalDamage.get() <= 0.0) {
                function.onDeath(sEntity, entity, damager);
                if (entity.hasMetadata("LD")) {
                    Sputnik.zero(entity);
                }
                if (entity.hasMetadata("WATCHER_E")) {
                    final Watcher watcher = Watcher.getWatcher(entity.getWorld());
                    if (watcher != null) {
                        final Watcher watcher2 = watcher;
                        --watcher2.currentMobsCount;
                        if (SUtil.random(0, 1) == 0) {
                            watcher.sd(watcher.mobDeathConvs[SUtil.random(0, watcher.mobDeathConvs.length - 1)], 0, 50, true);
                        }
                    }
                }
                if (damager != null) {
                    damager.playSound(damager.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
                }
                final SItem sitem1 = SItem.find(damager.getItemInHand());
                if (sitem1 != null && sitem1.getEnchantment(EnchantmentType.SOUL_EATER) != null && sitem1.getType() != SMaterial.ENCHANTED_BOOK) {
                    PlayerUtils.SOUL_EATER_MAP.put(damager.getUniqueId(), sEntity);
                }
                if (sitem1 != null && sitem1.getEnchantment(EnchantmentType.TURBO_GEM) != null && sitem1.getType() != SMaterial.ENCHANTED_BOOK) {
                    SkySimEngine.getEconomy().depositPlayer(damager, sitem1.getEnchantment(EnchantmentType.TURBO_GEM).getLevel());
                }
                final User user = User.getUser(damager.getUniqueId());
                double xpDropped = sEntity.getStatistics().getXPDropped();
                if (getCookieDurationTicks(damager) > 0L) {
                    xpDropped += sEntity.getStatistics().getXPDropped() * 35.0 / 100.0;
                }
                Skill.reward(CombatSkill.INSTANCE, xpDropped, damager);
                final SlayerQuest quest = user.getSlayerQuest();
                if (sEntity.getGenericInstance() instanceof SlayerBoss && !((SlayerBoss) sEntity.getGenericInstance()).getSpawnerUUID().equals(damager.getUniqueId())) {
                    finishSlayerQuest(((SlayerBoss) sEntity.getGenericInstance()).getSpawnerUUID());
                }
                if (quest != null && sEntity.getSpecType().getCraftType() == quest.getType().getType().getEntityType() && !damager.getWorld().getName().contains("f6")) {
                    if (sEntity.getGenericInstance() instanceof SlayerBoss && ((SlayerBoss) sEntity.getGenericInstance()).getSpawnerUUID().equals(damager.getUniqueId())) {
                        finishSlayerQuest(damager.getUniqueId());
                    } else {
                        double xpDropped2 = sEntity.getStatistics().getXPDropped();
                        if (getCookieDurationTicks(damager) > 0L) {
                            xpDropped2 += sEntity.getStatistics().getXPDropped() * 35.0 / 100.0;
                        }
                        quest.setXp(quest.getXp() + xpDropped2);
                        if (!(sEntity.getGenericInstance() instanceof SlayerBoss)) {
                            quest.setLastKilled(sEntity.getSpecType());
                        }
                        if (quest.getXp() >= quest.getType().getSpawnXP() && quest.getSpawned() == 0L) {
                            final Location location = entity.getLocation().clone().add(0.0, 1.0, 0.0);
                            quest.setSpawned(System.currentTimeMillis());
                            SlayerQuest.playBossSpawn(location, damager);
                            SUtil.delay(() -> quest.setEntity(new SEntity(location, quest.getType().getSpecType(), quest.getType().getTier(), damager.getUniqueId())), 28L);
                        }
                    }
                }
                entity.setMetadata("isDead", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
                boolean rare = false;
                for (final EntityDrop drop : SUtil.shuffle(function.drops())) {
                    final EntityDropType type = drop.getType();
                    final double magicFind = PlayerUtils.STATISTICS_CACHE.get(damager.getUniqueId()).getMagicFind().addAll() / 100.0;
                    double sp = 100.0 * (drop.getDropChance() * (1.0 + magicFind * 10000.0 / 100.0));
                    if (!SkySimEngine.getPlugin().config.getBoolean("disableDebug")) {
                        SLog.info("-------------------------------");
                        SLog.info("Final SP " + sp);
                        SLog.info("Drop chance " + drop.getDropChance());
                        SLog.info("RM " + Math.round(100.0 / sp));
                        SLog.info("Mob Killed " + sEntity.getStatistics().getEntityName());
                        if (drop.getDropChance() >= 0.05) {
                            SLog.info("Canc! Item is " + type.getDisplay());
                        }
                        SLog.info("-------------------------------");
                    }
                    if (drop.getDropChance() >= 0.25) {
                        sp = 100.0 * (drop.getDropChance());
                    }
                    final int r = SUtil.random(1, (int) Math.round(100.0 / sp));
                    if (r != 1) {
                        continue;
                    }
                    if (rare && type != EntityDropType.GUARANTEED) {
                        continue;
                    }
                    final ItemStack stack = drop.getStack();
                    SItem sItem = SItem.find(stack);
                    if (sItem == null) {
                        sItem = SItem.of(stack);
                    }
                    final MaterialStatistics s = sItem.getType().getStatistics();
                    final String name = sItem.getRarity().getColor() + sItem.getType().getDisplayName(sItem.getVariant());
                    final MaterialFunction f = sItem.getType().getFunction();
                    if (f != null && s.getType() != GenericItemType.ACCESSORY) {
                        f.onKill(entity, damager, sItem);
                    }
                    if (damager != null) {
                        for (final SItem accessory : getAccessories(damager)) {
                            if (accessory.getType().getStatistics().getType() == GenericItemType.ACCESSORY) {
                                accessory.getType().getFunction().onKill(entity, damager, sItem);
                            }
                        }
                    }
                    if (type != EntityDropType.GUARANTEED && type != EntityDropType.COMMON && damager != null) {
                        damager.sendMessage(type.getColor() + "" + ChatColor.BOLD + ((type == EntityDropType.CRAZY_RARE) ? "CRAZY " : ((type == EntityDropType.INSANE_RARE) ? "INSANE " : "")) + "RARE DROP! " + ChatColor.GRAY + "(" + name + ChatColor.GRAY + ")" + ((magicFind != 0.0) ? (ChatColor.AQUA + " (+" + (int) (magicFind * 10000.0) + "% Magic Find!)") : ""));
                    }
                    if (sEntity.getStatistics().mobLevel() >= 25 && SUtil.random(1, 25000) == 1 && Skill.getLevel(User.getUser(damager.getUniqueId()).getCombatXP(), false) >= 20) {
                        final int chance = (int) (Skill.getLevel(User.getUser(damager.getUniqueId()).getCombatXP(), false) * 1.5);
                        final Random rnd = new Random();
                        rnd.nextInt(100);
                    }
                    if (sEntity.getEntity().getType() == EntityType.ENDERMAN && SUtil.random(1, 40) == 1 && sEntity.getStatistics().mobLevel() >= 90 && SlayerBossType.SlayerMobType.ENDERMAN.getLevelForXP(User.getUser(damager.getUniqueId()).getEndermanSlayerXP()) >= 6 && User.getUser(damager.getUniqueId()).getActivePet() != null && User.getUser(damager.getUniqueId()).getActivePet().getType() == SMaterial.HIDDEN_VOIDLINGS_PET) {
                        PacketInvoker.dropVoidSpawner(damager, sEntity.getEntity().getLocation());
                    }
                    final SItem sitem2 = SItem.find(damager.getItemInHand());
                    if (sitem2 != null) {
                        if (drop.getOwner() == null) {
                            if (sitem2.getEnchantment(EnchantmentType.TELEKINESIS) != null && !Sputnik.isFullInv(damager) && sitem2.getType() != SMaterial.ENCHANTED_BOOK) {
                                Sputnik.GiveItem(sItem.getStack(), damager);
                            } else {
                                entity.getWorld().dropItem(entity.getLocation(), stack);
                            }
                        } else if (sitem2.getEnchantment(EnchantmentType.TELEKINESIS) != null && !Sputnik.isFullInv(damager) && sitem2.getType() != SMaterial.ENCHANTED_BOOK) {
                            Sputnik.GiveItem(sItem.getStack(), damager);
                        } else {
                            SUtil.spawnPersonalItem(stack, entity.getLocation(), drop.getOwner());
                        }
                    } else {
                        entity.getWorld().dropItem(entity.getLocation(), stack);
                    }
                    if (type == EntityDropType.GUARANTEED) {
                        continue;
                    }
                    rare = true;
                }
            }
        }
    }

    public static void finishSlayerQuest(final UUID uuid) {
        final Player damager = Bukkit.getPlayer(uuid);
        final User user = User.getUser(uuid);
        final SlayerQuest quest = user.getSlayerQuest();
        if (quest != null && quest.getDied() == 0L && quest.getKilled() == 0L) {
            quest.setKilled(System.currentTimeMillis());
            damager.playSound(damager.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
            damager.sendMessage("  " + ChatColor.GOLD + ChatColor.BOLD + "NICE! SLAYER BOSS SLAIN!");
            damager.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "» " + ChatColor.GRAY + "Talk to Maddox to claim your " + quest.getType().getType().getName() + " Slayer XP!");
            if (isAutoSlayer(damager)) {
                SlayerGUI.claimReward(damager);
                final StringBuilder sb = new StringBuilder();
                final String bossType = quest.getType().getType().getName();
                if (bossType.toLowerCase().contains("zombie")) {
                    sb.append("REVENANT_HORROR_");
                }
                if (bossType.toLowerCase().contains("wolf")) {
                    sb.append("SVEN_PACKMASTER_");
                }
                if (bossType.toLowerCase().contains("spider")) {
                    sb.append("TARANTULA_BROODFATHER_");
                }
                if (bossType.toLowerCase().contains("enderman")) {
                    sb.append("VOIDGLOOM_SERAPH_");
                }
                sb.append(SUtil.toRomanNumeral(quest.getType().getTier()));
                User.getUser(damager.getUniqueId()).startSlayerQuest(SlayerBossType.getByNamespace(sb.toString()));
            }
        }
    }

    public static boolean takeMana(final Player player, final int amount) {
        final int n = Repeater.MANA_MAP.get(player.getUniqueId()) - amount;
        if (n < 0) {
            return false;
        }
        Repeater.MANA_MAP.put(player.getUniqueId(), n);
        return true;
    }

    public static int getFinalManaCost(Player player, SItem sItem, int cost) {
        Enchantment ultimateWise;
        PlayerStatistics statistics = STATISTICS_CACHE.get(player.getUniqueId());
        int manaPool = 100 + SUtil.blackMagic(statistics.getIntelligence().addAll());
        int updated = cost;
        ArmorSet set = STATISTICS_CACHE.get(player.getUniqueId()).getArmorSet();
        if (set != null && set.equals(SMaterial.WISE_DRAGON_SET)) {
            updated = 0;
        }
        if ((ultimateWise = sItem.getEnchantment(EnchantmentType.ULTIMATE_WISE)) != null) {
            updated = Math.max(0, Long.valueOf(Math.round((double) updated - (double) updated * ((double) ultimateWise.getLevel() / 10.0))).intValue());
        }
        if (cost == -1) {
            updated = manaPool;
        }
        if (cost == -2) {
            updated = manaPool / 2;
        }
        return updated;
    }

    public static int getSpecItemIndex(final Player player, final SMaterial type) {
        final PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            final SItem item = SItem.find(inventory.getItem(i));
            if (item != null && item.getType() == type) {
                return i;
            }
        }
        return -1;
    }

    public static void addBoostStatistics(final PlayerStatistics statistics, final int slot, final PlayerBoostStatistics boostStatistics) {
        if (boostStatistics == null) {
            return;
        }
        final DoublePlayerStatistic health = statistics.getMaxHealth();
        final DoublePlayerStatistic defense = statistics.getDefense();
        final DoublePlayerStatistic strength = statistics.getStrength();
        final DoublePlayerStatistic intelligence = statistics.getIntelligence();
        final DoublePlayerStatistic ferocity = statistics.getFerocity();
        final DoublePlayerStatistic speed = statistics.getSpeed();
        final DoublePlayerStatistic critChance = statistics.getCritChance();
        final DoublePlayerStatistic critDamage = statistics.getCritDamage();
        final DoublePlayerStatistic atkSpeed = statistics.getAttackSpeed();
        final DoublePlayerStatistic magicFind = statistics.getMagicFind();
        health.add(slot, Double.valueOf(boostStatistics.getBaseHealth()));
        defense.add(slot, Double.valueOf(boostStatistics.getBaseDefense()));
        strength.add(slot, Double.valueOf(boostStatistics.getBaseStrength()));
        speed.add(slot, Double.valueOf(boostStatistics.getBaseSpeed()));
        intelligence.add(slot, Double.valueOf(boostStatistics.getBaseIntelligence()));
        critDamage.add(slot, Double.valueOf(boostStatistics.getBaseCritDamage()));
        critChance.add(slot, Double.valueOf(boostStatistics.getBaseCritChance()));
        magicFind.add(slot, Double.valueOf(boostStatistics.getBaseMagicFind()));
        ferocity.add(slot, Double.valueOf(boostStatistics.getBaseFerocity()));
        atkSpeed.add(slot, Double.valueOf(boostStatistics.getBaseAttackSpeed()));
    }

    public static boolean isAutoSlayer(final Player p) {
        boolean returnval = PlayerUtils.AUTO_SLAYER.containsKey(p.getUniqueId()) && PlayerUtils.AUTO_SLAYER.get(p.getUniqueId());
        return returnval;
    }

    public static boolean isSBAToggle(final Player pl) {
        final UUID p = pl.getUniqueId();
        boolean returnval = Repeater.SBA_MAP.containsKey(p) && Repeater.SBA_MAP.get(p);
        return returnval;
    }

    public static long getCookieDurationTicks(final Player p) {
        if (PlayerUtils.COOKIE_DURATION_CACHE.containsKey(p.getUniqueId())) {
            return PlayerUtils.COOKIE_DURATION_CACHE.get(p.getUniqueId());
        }
        PlayerUtils.COOKIE_DURATION_CACHE.put(p.getUniqueId(), 0L);
        return 0L;
    }

    public static void setCookieDurationTicks(final Player p, final long ticks) {
        PlayerUtils.COOKIE_DURATION_CACHE.put(p.getUniqueId(), ticks);
    }

    public static String getCookieDurationDisplay(final Player p) {
        if (getCookieDurationTicks(p) > 0L) {
            return SUtil.getFormattedTimeToDay(getCookieDurationTicks(p));
        }
        return Sputnik.trans("&7Not active! Obtain booster cookies from the") + "\n" + Sputnik.trans("&7community shop in the hub.");
    }

    public static String getCookieDurationDisplayGUI(final Player p) {
        if (getCookieDurationTicks(p) > 0L) {
            return ChatColor.GREEN + SUtil.getFormattedTimeToDay(getCookieDurationTicks(p));
        }
        return Sputnik.trans("&cNot active!");
    }

    public static void subtractDurationCookie(final Player p, final long sub) {
        if (getCookieDurationTicks(p) > 0L) {
            setCookieDurationTicks(p, getCookieDurationTicks(p) - sub);
        }
        if (getCookieDurationTicks(p) <= 0L) {
            wipeCookieStatsBuff(p);
        } else {
            loadCookieStatsBuff(p);
        }
    }

    public static boolean cookieBuffActive(final Player p) {
        return getCookieDurationTicks(p) > 0L;
    }

    public static void loadCookieStatsBuff(final Player player) {
        final User user = User.getUser(player.getUniqueId());
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(151);
        statistics.getFerocity().set(151, Double.valueOf(35.0));
        statistics.getDefense().set(151, Double.valueOf(200.0));
        statistics.getCritDamage().set(151, Double.valueOf(0.25));
        statistics.getIntelligence().set(151, Double.valueOf(2000.0));
        statistics.getMagicFind().set(151, Double.valueOf(0.3));
        statistics.getStrength().set(151, Double.valueOf(100.0));
    }

    public static void wipeCookieStatsBuff(final Player player) {
        final User user = User.getUser(player.getUniqueId());
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(user.getUuid());
        statistics.zeroAll(151);
    }

    public static void aBs(final Player p) {
        new BukkitRunnable() {
            final float cout = p.getLocation().getYaw();

            public void run() {
                if (!p.isOnline()) {
                    this.cancel();
                    return;
                }
                final Location loc = p.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.6));
                p.getWorld().spigot().playEffect(loc.clone().add(0.0, 2.2, 0.0), Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
    }

    static {
        AUTO_SLAYER = new HashMap<UUID, Boolean>();
        USER_SESSION_ID = new HashMap<UUID, UUID>();
        STATISTICS_CACHE = new HashMap<UUID, PlayerStatistics>();
        COOLDOWN_MAP = new HashMap<UUID, List<SMaterial>>();
        SOUL_EATER_MAP = new HashMap<UUID, SEntity>();
        COOKIE_DURATION_CACHE = new HashMap<UUID, Long>();
        LAST_KILLED_MAPPING = new HashMap<UUID, Integer>();
    }

    public static class Debugmsg {
        public static boolean debugmsg;
    }

    public interface DamageResult {
        double getFinalDamage();

        boolean didCritDamage();
    }
}
