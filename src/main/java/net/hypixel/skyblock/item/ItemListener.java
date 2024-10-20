/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Enderman
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Villager
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.PotionSplashEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerToggleFlightEvent
 *  org.bukkit.event.player.PlayerToggleSneakEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.JollyPinkGiant;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanGiant;
import net.hypixel.skyblock.features.collection.ItemCollection;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.FishingRodFunction;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemOrigin;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.SBlock;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.TickingMaterial;
import net.hypixel.skyblock.item.armor.Witherborn;
import net.hypixel.skyblock.item.storage.Storage;
import net.hypixel.skyblock.item.weapon.EdibleMace;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.DefenseReplacement;
import net.hypixel.skyblock.util.Groups;
import net.hypixel.skyblock.util.ManaReplacement;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ItemListener
extends PListener {
    public static final Map<Player, String> Classes = new HashMap<Player, String>();
    public static final Map<Player, Boolean> IsDead = new HashMap<Player, Boolean>();

    @EventHandler
    public void useEtherWarp(PlayerInteractEvent e2) {
        if (!SItem.isSpecItem(e2.getItem())) {
            return;
        }
        SItem sItem = SItem.find(e2.getItem());
        if (null == sItem) {
            return;
        }
        Player player = e2.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemListener.updateStatistics(e2.getPlayer());
        Action action = e2.getAction();
        if (sItem.getDataString("etherwarp_trans").equals("true") && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action) && e2.getPlayer().isSneaking()) {
            int cost;
            if (!SItem.isAbleToDoEtherWarpTeleportation(player, sItem)) {
                AbilityActivation activation;
                Ability ability = sItem.getType().getAbility();
                if (null != ability && (AbilityActivation.LEFT_CLICK == (activation = ability.getAbilityActivation()) || AbilityActivation.RIGHT_CLICK == activation)) {
                    if (AbilityActivation.LEFT_CLICK == activation ? Action.LEFT_CLICK_AIR != action && Action.LEFT_CLICK_BLOCK != action : Action.RIGHT_CLICK_AIR != action && Action.RIGHT_CLICK_BLOCK != action) {
                        return;
                    }
                    PlayerUtils.useAbility(player, sItem);
                }
                return;
            }
            int mana = Repeater.MANA_MAP.get(uuid);
            int resMana = mana - (cost = PlayerUtils.getFinalManaCost(player, sItem, 250));
            if (0 <= resMana) {
                Repeater.MANA_MAP.remove(uuid);
                Repeater.MANA_MAP.put(uuid, resMana);
                final long c2 = System.currentTimeMillis();
                Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement(){

                    @Override
                    public String getReplacement() {
                        return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + "Ether Transmission" + ChatColor.AQUA + ")";
                    }

                    @Override
                    public long getEnd() {
                        return c2 + 2000L;
                    }
                });
                SItem.etherWarpTeleportation(e2.getPlayer(), sItem);
            } else {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                final long c3 = System.currentTimeMillis();
                Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement(){

                    @Override
                    public String getReplacement() {
                        return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                    }

                    @Override
                    public long getEnd() {
                        return c3 + 1500L;
                    }
                });
            }
        }
    }

    @EventHandler
    public void onPlayerInteracting(PlayerInteractEvent e2) {
    }

    @EventHandler
    public void PotionsSplash(PotionSplashEvent e2) {
        for (Entity ef : e2.getAffectedEntities()) {
            if (!ef.hasMetadata("LD")) continue;
            for (org.bukkit.potion.PotionEffect pe : e2.getEntity().getEffects()) {
                if (pe.getType() == PotionEffectType.HEAL) {
                    e2.setCancelled(true);
                    continue;
                }
                if (pe.getType() != PotionEffectType.HARM) continue;
                e2.setCancelled(true);
                ((LivingEntity)ef).damage(1.0E-4);
            }
        }
        if (e2.getEntity().getShooter() instanceof LivingEntity && ((LivingEntity)e2.getEntity().getShooter()).hasMetadata("LD")) {
            for (Entity ef : e2.getAffectedEntities()) {
                if (ef.hasMetadata("LD")) {
                    e2.setCancelled(true);
                    if (ef.isDead()) {
                        return;
                    }
                    ((LivingEntity)ef).setHealth(Math.min(((LivingEntity)ef).getMaxHealth(), ((LivingEntity)ef).getHealth() + ((LivingEntity)ef).getMaxHealth() * 10.0 / 100.0));
                    continue;
                }
                if (!(ef instanceof Player)) continue;
                e2.setCancelled(true);
                ((LivingEntity)ef).setHealth(Math.min(((LivingEntity)ef).getMaxHealth(), ((LivingEntity)ef).getHealth() + 500.0));
                ef.sendMessage(Sputnik.trans("&a&lBUFF! &fYou were splashed with &cHealing V&f!"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e2) {
        MaterialFunction function;
        AbilityActivation activation;
        if (Action.RIGHT_CLICK_AIR != e2.getAction()) {
            for (Player p2 : e2.getPlayer().getWorld().getPlayers()) {
                if (p2 == e2.getPlayer()) continue;
                ((CraftPlayer)p2).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((net.minecraft.server.v1_8_R3.Entity)((CraftLivingEntity)e2.getPlayer()).getHandle(), 0));
            }
        }
        if (!SItem.isSpecItem(e2.getItem())) {
            return;
        }
        SItem sItem = SItem.find(e2.getItem());
        if (null == sItem) {
            return;
        }
        if (!(Material.MONSTER_EGG != sItem.getStack().getType() && Material.MONSTER_EGGS != sItem.getStack().getType() || e2.getPlayer().isOp())) {
            e2.setCancelled(true);
        }
        ItemListener.updateStatistics(e2.getPlayer());
        Action action = e2.getAction();
        if (SpecificItemType.HELMET == sItem.getType().getStatistics().getSpecificType() && Action.RIGHT_CLICK_AIR == action && ItemListener.isAir(e2.getPlayer().getInventory().getHelmet())) {
            e2.getPlayer().getInventory().setHelmet(sItem.getStack());
            e2.getPlayer().setItemInHand(null);
        }
        Player player = e2.getPlayer();
        Ability ability = sItem.getType().getAbility();
        if (!(null == ability || AbilityActivation.LEFT_CLICK != (activation = ability.getAbilityActivation()) && AbilityActivation.RIGHT_CLICK != activation || (AbilityActivation.LEFT_CLICK != activation ? Action.RIGHT_CLICK_AIR != action && Action.RIGHT_CLICK_BLOCK != action : Action.LEFT_CLICK_AIR != action && Action.LEFT_CLICK_BLOCK != action))) {
            if (sItem.getDataString("etherwarp_trans").equals("true")) {
                if (!player.isSneaking()) {
                    PlayerUtils.useAbility(player, sItem);
                }
            } else {
                PlayerUtils.useAbility(player, sItem);
            }
        }
        if (null != (function = sItem.getType().getFunction())) {
            function.onInteraction(e2);
        }
    }

    @EventHandler
    public void onPlayerMage(PlayerInteractEvent e2) {
        Player player = e2.getPlayer();
        Action action = e2.getAction();
        if (!player.getWorld().getName().equals("dungeon")) {
            return;
        }
        if (!Classes.containsKey(player)) {
            Classes.put(player, "M");
        }
        if ("M" == Classes.get(player) && Action.LEFT_CLICK_AIR == action) {
            String ACT = "true";
            ItemStack item = player.getInventory().getItemInHand();
            SItem sitem = SItem.find(item);
            if (null == sitem) {
                return;
            }
            SMaterial material = sitem.getType();
            MaterialStatistics statistics = material.getStatistics();
            SpecificItemType type = statistics.getSpecificType();
            if (type.getName().contains("SWORD")) {
                Location blockLocation = player.getTargetBlock((Set)null, 30).getLocation();
                Location crystalLocation = player.getEyeLocation();
                Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
                double count = 25.0;
                for (int i2 = 1; 25 >= i2; ++i2) {
                    for (Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 25.0)), 0.5, 0.0, 0.5)) {
                        if ("false" == ACT) {
                            return;
                        }
                        if (entity.isDead() || !(entity instanceof LivingEntity) || entity.hasMetadata("GiantSword") || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand) continue;
                        User user = User.getUser(player.getUniqueId());
                        boolean damage = false;
                        double enchantBonus = 0.0;
                        double potionBonus = 0.0;
                        PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                        double critDamage = statistics2.getCritDamage().addAll();
                        double speed = statistics2.getSpeed().addAll();
                        double realSpeed = speed * 100.0 % 25.0;
                        double realSpeedDIV = speed - realSpeed;
                        double realSpeedDIVC = realSpeedDIV / 25.0;
                        PlayerInventory inv = player.getInventory();
                        SItem helmet = SItem.find(inv.getHelmet());
                        if (null != helmet && SMaterial.WARDEN_HELMET == helmet.getType()) {
                            enchantBonus += (100.0 + 20.0 * realSpeedDIVC) / 100.0;
                        }
                        for (Enchantment enchantment : sitem.getEnchantments()) {
                            EnchantmentType type2 = enchantment.getType();
                            int level = enchantment.getLevel();
                            if (type2 == EnchantmentType.SHARPNESS) {
                                enchantBonus += (double)(level * 6) / 100.0;
                            }
                            if (type2 == EnchantmentType.SMITE && Groups.UNDEAD_MOBS.contains(entity.getType())) {
                                enchantBonus += (double)(level * 8) / 100.0;
                            }
                            if (type2 == EnchantmentType.ENDER_SLAYER && Groups.ENDERMAN.contains(entity.getType())) {
                                enchantBonus += (double)(level * 12) / 100.0;
                            }
                            if (type2 == EnchantmentType.BANE_OF_ARTHROPODS && Groups.ARTHROPODS.contains(entity.getType())) {
                                enchantBonus += (double)(level * 8) / 100.0;
                            }
                            if (type2 == EnchantmentType.DRAGON_HUNTER && Groups.ENDERDRAGON.contains(entity.getType())) {
                                enchantBonus += (double)(level * 8) / 100.0;
                            }
                            if (type2 != EnchantmentType.CRITICAL) continue;
                            critDamage += (double)(level * 10) / 100.0;
                        }
                        PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics)material.getStatistics();
                        double baseDamage = ((double)(5 + playerBoostStatistics.getBaseDamage()) + statistics2.getStrength().addAll() / 5.0) * (1.0 + statistics2.getStrength().addAll() / 100.0);
                        int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                        double weaponBonus = 0.0;
                        double armorBonus = 1.0;
                        int critChanceMul = (int)(statistics2.getCritChance().addAll() * 100.0);
                        int chance = SUtil.random(0, 100);
                        if (chance > critChanceMul) {
                            critDamage = 0.0;
                        }
                        double damageMultiplier = 1.0 + (double)combatLevel * 0.04 + enchantBonus + 0.0;
                        double finalCritDamage = critDamage;
                        double finalDamage = baseDamage * damageMultiplier * 1.0 * (1.0 + finalCritDamage);
                        double finalPotionBonus = 0.0;
                        if (entity.isDead() || !(entity instanceof LivingEntity) || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity instanceof Item || entity instanceof ItemFrame) continue;
                        if (EdibleMace.edibleMace.containsKey(player.getUniqueId()) && EdibleMace.edibleMace.get(player.getUniqueId()).booleanValue()) {
                            finalDamage *= 2.0;
                            EdibleMace.edibleMace.put(player.getUniqueId(), false);
                        }
                        final ArmorStand stand3 = (ArmorStand)new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]).getEntity();
                        if (0.0 == finalCritDamage) {
                            stand3.setCustomName("" + ChatColor.GRAY + (int)finalDamage);
                        } else {
                            stand3.setCustomName(SUtil.rainbowize("\u2727" + (int)finalDamage + "\u2727"));
                        }
                        stand3.setCustomNameVisible(true);
                        stand3.setGravity(false);
                        stand3.setVisible(false);
                        user.damageEntity((Damageable)entity, finalDamage);
                        new BukkitRunnable(){

                            public void run() {
                                stand3.remove();
                                this.cancel();
                            }
                        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 30L);
                        ACT = "false";
                    }
                    player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 25.0)), Effect.FIREWORKS_SPARK, 24, 1, 0.0f, 0.0f, 0.0f, 1.0f, 0, 64);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e2) {
        if (!(e2.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player)e2.getPlayer();
        Inventory storage = Storage.getCurrentStorageOpened(player);
        if (null == storage) {
            return;
        }
        Inventory inventory = e2.getInventory();
        SItem hand = SItem.find(player.getItemInHand());
        if (null == hand) {
            return;
        }
        NBTTagCompound storageData = new NBTTagCompound();
        for (int i2 = 0; i2 < inventory.getSize(); ++i2) {
            SItem sItem = SItem.find(inventory.getItem(i2));
            if (null == sItem) {
                SItem equiv = SItem.of(inventory.getItem(i2));
                if (null != equiv) {
                    storageData.setByteArray(String.valueOf(i2), SUtil.gzipCompress(equiv.toCompound().toString().getBytes()));
                    continue;
                }
                storageData.remove(String.valueOf(i2));
                continue;
            }
            storageData.setByteArray(String.valueOf(i2), SUtil.gzipCompress(sItem.toCompound().toString().getBytes()));
        }
        hand.getData().set("storage_data", (NBTBase)storageData);
        hand.update();
        Storage.closeCurrentStorage(player);
    }

    @EventHandler
    public void onPlayerFlight(PlayerToggleFlightEvent e2) {
        Player player = e2.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (GameMode.CREATIVE == gameMode || GameMode.SPECTATOR == gameMode) {
            return;
        }
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            Ability ability;
            SItem sItem = SItem.find(stack);
            if (null == sItem || null == (ability = sItem.getType().getAbility()) || !e2.isFlying() || AbilityActivation.FLIGHT != ability.getAbilityActivation()) continue;
            e2.setCancelled(true);
            PlayerUtils.useAbility(player, sItem);
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e2) {
        Player player = e2.getPlayer();
        GameMode gameMode = player.getGameMode();
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            Ability ability;
            SItem sItem = SItem.find(stack);
            if (null == sItem || null == (ability = sItem.getType().getAbility()) || !player.isSneaking() || AbilityActivation.SNEAK != ability.getAbilityActivation()) continue;
            PlayerUtils.useAbility(player, sItem);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e2) {
        if (InventoryType.CRAFTING != e2.getView().getTopInventory().getType()) {
            return;
        }
        if (InventoryType.SlotType.CONTAINER != e2.getSlotType() && InventoryType.SlotType.QUICKBAR != e2.getSlotType()) {
            return;
        }
        if (InventoryAction.MOVE_TO_OTHER_INVENTORY != e2.getAction()) {
            return;
        }
        Inventory inventory = e2.getClickedInventory();
        if (null == inventory) {
            return;
        }
        if (InventoryType.PLAYER != inventory.getType()) {
            return;
        }
        ItemStack current = e2.getCurrentItem();
        if (null == current) {
            return;
        }
        SItem sItem = SItem.find(current);
        if (null == sItem) {
            sItem = SItem.of(current);
        }
        ItemListener.updateStatistics((Player)e2.getWhoClicked());
        if (null == sItem.getType().getStatistics().getSpecificType() || SpecificItemType.HELMET != sItem.getType().getStatistics().getSpecificType()) {
            return;
        }
        PlayerInventory playerInventory = (PlayerInventory)inventory;
        if (!ItemListener.isAir(playerInventory.getHelmet())) {
            return;
        }
        e2.setCancelled(true);
        e2.setCurrentItem(new ItemStack(Material.AIR));
        playerInventory.setHelmet(current);
    }

    @EventHandler
    public void onArmorChange(InventoryClickEvent e2) {
        Player player = (Player)e2.getWhoClicked();
        if (null == e2.getClickedInventory()) {
            return;
        }
        if (InventoryType.PLAYER != e2.getClickedInventory().getType() && InventoryType.CRAFTING != e2.getClickedInventory().getType()) {
            return;
        }
        ItemListener.updateStatistics((Player)e2.getWhoClicked());
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
    }

    @EventHandler
    public void onArmorChange1(InventoryCloseEvent e2) {
        Player player = (Player)e2.getPlayer();
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
        ItemListener.updateStatistics(player);
    }

    @EventHandler
    public void onArmorChange2(PlayerJoinEvent e2) {
        Player player = e2.getPlayer();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        ItemStack helmet = player.getInventory().getHelmet();
        SUtil.delay(() -> player.getInventory().setHelmet(helmet), 10L);
        SItem helmet1 = SItem.find(player.getInventory().getHelmet());
        if (null == helmet1) {
            return;
        }
        TickingMaterial tickingMaterial = helmet1.getType().getTickingInstance();
        if (null != tickingMaterial) {
            statistics.tickItem(39, tickingMaterial.getInterval(), () -> tickingMaterial.tick(helmet1, Bukkit.getPlayer((UUID)statistics.getUuid())));
        }
        ItemListener.updateStatistics(player);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onItemClick(InventoryClickEvent e2) {
        ItemStack stack = e2.getCurrentItem();
        if (null == stack) {
            return;
        }
        SItem sItem = SItem.find(stack);
        if (null == sItem) {
            return;
        }
        if (null == sItem.getType().getFunction()) {
            return;
        }
        sItem.getType().getFunction().onInventoryClick(sItem, e2);
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent e2) {
        if (null == e2.getClickedInventory()) {
            return;
        }
        if (InventoryType.PLAYER != e2.getClickedInventory().getType()) {
            return;
        }
        if (8 != e2.getSlot()) {
            return;
        }
        e2.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e2) {
        SItem sItem = SItem.find(e2.getItemInHand());
        if (null == sItem) {
            return;
        }
        if (SpecificItemType.HELMET == sItem.getType().getStatistics().getSpecificType() && ItemListener.isAir(e2.getPlayer().getInventory().getHelmet())) {
            e2.setCancelled(true);
            e2.getPlayer().getInventory().setHelmet(sItem.getStack());
            e2.getPlayer().setItemInHand(null);
            return;
        }
        if (!sItem.getType().isCraft()) {
            if (GenericItemType.BLOCK != sItem.getType().getStatistics().getType()) {
                e2.setCancelled(true);
            } else {
                new SBlock(e2.getBlockPlaced().getLocation(), sItem.getType(), sItem.getData()).save();
            }
        }
    }

    @EventHandler
    public void onFrameInteract(PlayerInteractEvent e2) {
        if (Action.RIGHT_CLICK_BLOCK != e2.getAction()) {
            return;
        }
        Player player = e2.getPlayer();
        final Block block = e2.getClickedBlock();
        ItemStack hand = e2.getItem();
        if (null == hand) {
            return;
        }
        SItem item = SItem.find(hand);
        if (null == item) {
            return;
        }
        if (Material.ENDER_PORTAL_FRAME != block.getType()) {
            return;
        }
        SBlock sBlock = SBlock.getBlock(block.getLocation());
        if (null == sBlock) {
            e2.setCancelled(true);
            return;
        }
        if (SMaterial.SUMMONING_FRAME != sBlock.getType()) {
            e2.setCancelled(true);
            return;
        }
        if (!block.hasMetadata("placer")) {
            if (SMaterial.SUMMONING_EYE != item.getType()) {
                return;
            }
            block.setMetadata("placer", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)player.getUniqueId()));
            BlockState state = block.getState();
            state.setRawData((byte)4);
            state.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SLEEPING_EYE).getStack());
            ArrayList<Location> locations = StaticDragonManager.EYES.containsKey(player.getUniqueId()) ? StaticDragonManager.EYES.get(player.getUniqueId()) : new ArrayList<Location>();
            locations.add(block.getLocation());
            StaticDragonManager.EYES.remove(player.getUniqueId());
            StaticDragonManager.EYES.put(player.getUniqueId(), locations);
            int quantity = 0;
            for (List<Location> ls : StaticDragonManager.EYES.values()) {
                quantity += ls.size();
            }
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (!p2.getWorld().getName().equals("world")) continue;
                p2.sendMessage(ChatColor.DARK_PURPLE + "\u262c " + ChatColor.GREEN + player.getName() + ChatColor.LIGHT_PURPLE + " placed a Summoning Eye! " + (8 == quantity ? "Brace yourselves! " : "") + ChatColor.GRAY + "(" + (8 == quantity ? ChatColor.GREEN : ChatColor.YELLOW) + quantity + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
            }
            if (8 != quantity) {
                return;
            }
            ArrayList<UUID> cleared = new ArrayList<UUID>();
            for (List<Location> ls2 : StaticDragonManager.EYES.values()) {
                for (Location location : ls2) {
                    Block b2 = location.getBlock();
                    List values = b2.getMetadata("placer");
                    Player p2 = Bukkit.getPlayer((UUID)((UUID)((MetadataValue)values.get(0)).value()));
                    if (null == p2 || cleared.contains(p2.getUniqueId())) continue;
                    PlayerInventory inventory = p2.getInventory();
                    for (int i2 = 0; i2 < inventory.getSize(); ++i2) {
                        SItem si = SItem.find(inventory.getItem(i2));
                        if (null == si || SMaterial.SLEEPING_EYE != si.getType()) continue;
                        inventory.setItem(i2, SItem.of(SMaterial.REMNANT_OF_THE_EYE).getStack());
                    }
                    p2.sendMessage(ChatColor.DARK_PURPLE + "Your Sleeping Eyes have been awoken by the magic of the Dragon. They are now Remnants of the Eye!");
                    cleared.add(p2.getUniqueId());
                }
            }
            StaticDragonManager.ACTIVE = true;
            block.getWorld().playSound(block.getLocation(), Sound.ENDERMAN_STARE, 50.0f, -2.0f);
            new BukkitRunnable(){

                public void run() {
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_DEATH, 50.0f, -2.0f);
                }
            }.runTaskLater((Plugin)this.plugin, 90L);
            new BukkitRunnable(){

                public void run() {
                    for (int i2 = 0; 3 > i2; ++i2) {
                        block.getWorld().playSound(block.getLocation(), Sound.EXPLODE, 50.0f, -2.0f);
                    }
                    SEntityType dragonType = SEntityType.PROTECTOR_DRAGON;
                    int chance = SUtil.random(0, 100);
                    if (16 <= chance) {
                        dragonType = SEntityType.OLD_DRAGON;
                    }
                    if (32 <= chance) {
                        dragonType = SEntityType.WISE_DRAGON;
                    }
                    if (48 <= chance) {
                        dragonType = SEntityType.UNSTABLE_DRAGON;
                    }
                    if (64 <= chance) {
                        dragonType = SEntityType.YOUNG_DRAGON;
                    }
                    if (80 <= chance) {
                        dragonType = SEntityType.STRONG_DRAGON;
                    }
                    if (96 <= chance) {
                        dragonType = SEntityType.SUPERIOR_DRAGON;
                    }
                    SEntity entity = new SEntity(block.getLocation().clone().add(0.0, 53.0, 0.0), dragonType, new Object[0]);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-642, 36, -246).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-635, 51, -233).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-717, 39, -255).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-715, 44, -299).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-682, 58, -323).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-638, 51, -318).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-623, 58, -293).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-678, 31, -287).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-697, 35, -249).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    block.getWorld().spawnEntity(block.getWorld().getBlockAt(-638, 40, -309).getLocation().add(-0.5, 0.7, -0.5), EntityType.ENDER_CRYSTAL);
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        if (!p2.getWorld().getName().equals("dragon")) continue;
                        Vector vector = p2.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
                        p2.setVelocity(vector.normalize().multiply(40.0).setY(100.0));
                    }
                    StaticDragonManager.DRAGON = entity;
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_GROWL, 50.0f, 1.0f);
                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                        p2.sendMessage(ChatColor.DARK_PURPLE + "\u262c " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "The " + ChatColor.RED + ChatColor.BOLD + entity.getStatistics().getEntityName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " has spawned!");
                    }
                }
            }.runTaskLater((Plugin)this.plugin, 180L);
        } else {
            List values2 = block.getMetadata("placer");
            Player p3 = Bukkit.getPlayer((UUID)((UUID)((MetadataValue)values2.get(0)).value()));
            if (null == p3) {
                return;
            }
            if (SMaterial.SLEEPING_EYE != item.getType()) {
                return;
            }
            if (!p3.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You can only recover Summoning Eyes that you placed!");
                return;
            }
            if (StaticDragonManager.ACTIVE) {
                player.sendMessage(ChatColor.RED + "You cannot recover Summoning Eyes after the dragon has been summoned!");
                return;
            }
            block.removeMetadata("placer", (Plugin)this.plugin);
            BlockState state2 = block.getState();
            state2.setRawData((byte)0);
            state2.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SUMMONING_EYE).getStack());
            StaticDragonManager.EYES.get(p3.getUniqueId()).remove(block.getLocation());
            player.sendMessage(ChatColor.DARK_PURPLE + "You recovered a Summoning Eye!");
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e2) {
        List o2;
        Item item = e2.getItem();
        Player player = e2.getPlayer();
        ItemListener.updateStatistics(player);
        NBTTagCompound compound = CraftItemStack.asNMSCopy((ItemStack)item.getItemStack()).getTag();
        if (null == compound) {
            compound = new NBTTagCompound();
        }
        if (!compound.hasKey("type")) {
            item.getItemStack().setItemMeta(SItem.of(item.getItemStack()).getStack().getItemMeta());
        }
        if (item.hasMetadata("owner") && 0 != (o2 = item.getMetadata("owner")).size() && !((MetadataValue)o2.get(0)).asString().equals(e2.getPlayer().getUniqueId().toString())) {
            e2.setCancelled(true);
            return;
        }
        User user = User.getUser(player.getUniqueId());
        ItemStack stack = item.getItemStack();
        SItem sItem = SItem.find(stack);
        if (null == sItem) {
            throw new NullPointerException("Something messed up! Check again");
        }
        if (item.hasMetadata("obtained")) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (!p2.getWorld().getName().equals("world")) continue;
                if (!sItem.getFullName().equals("\u00a76Ender Dragon") && !sItem.getFullName().equals("\u00a75Ender Dragon") || !sItem.getFullName().equals("\u00a76Voidling Destroyer")) {
                    p2.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + sItem.getFullName() + ChatColor.YELLOW + "!");
                    continue;
                }
                p2.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName() + ChatColor.YELLOW + "!");
            }
        }
        if (ItemOrigin.NATURAL_BLOCK == sItem.getOrigin() || ItemOrigin.MOB == sItem.getOrigin()) {
            sItem.setOrigin(ItemOrigin.UNKNOWN);
            ItemCollection collection = ItemCollection.getByMaterial(sItem.getType(), stack.getDurability());
            if (null != collection) {
                int prev = user.getCollection(collection);
                user.addToCollection(collection, stack.getAmount());
                if (SkyBlock.getPlugin().config.getBoolean("Config")) {
                    user.configsave();
                } else {
                    user.save();
                }
                if (0 == prev) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  COLLECTION UNLOCKED " + ChatColor.RESET + ChatColor.YELLOW + collection.getName());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e2) {
        SItem sItem = SItem.find(e2.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e2.setCancelled(true);
        }
        ItemListener.updateStatistics(e2.getPlayer());
    }

    @EventHandler
    public void onItemMove(PlayerDropItemEvent e2) {
        SItem sItem = SItem.find(e2.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e2.setCancelled(true);
        }
        ItemListener.updateStatistics(e2.getPlayer());
    }

    @EventHandler
    public void onItemDrop1(PlayerDropItemEvent e2) {
        SItem sItem = SItem.find(e2.getItemDrop().getItemStack());
        if (null != sItem && SMaterial.BONEMERANG == sItem.getType() && e2.getItemDrop().getItemStack().toString().contains("GHAST_TEAR")) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove1(PlayerDropItemEvent e2) {
        SItem sItem = SItem.find(e2.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e2.setCancelled(true);
        }
    }

    @EventHandler
    public void onFishingRodReel(PlayerFishEvent e2) {
        SItem rod = SItem.find(e2.getPlayer().getItemInHand());
        if (null == rod) {
            return;
        }
        e2.getHook().setMetadata("owner", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)e2.getPlayer()));
        MaterialFunction function = rod.getType().getFunction();
        if (null == function) {
            return;
        }
        if (function instanceof FishingRodFunction) {
            ((FishingRodFunction)function).onFish(rod, e2);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e2) {
        SItem item = SItem.find(e2.getPotion().getItem());
        if (null == item) {
            return;
        }
        if (!item.isPotion()) {
            return;
        }
        e2.setCancelled(true);
        for (LivingEntity entity : e2.getAffectedEntities()) {
            User user;
            if (!(entity instanceof Player) || null == (user = User.getUser(entity.getUniqueId()))) continue;
            for (PotionEffect effect : item.getPotionEffects()) {
                PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
                if (null != effect.getType().getOnDrink()) {
                    effect.getType().getOnDrink().accept(effect, (Player)entity);
                }
                long ticks = (long)((double)effect.getDuration() * e2.getIntensity(entity));
                if (!user.hasPotionEffect(effect.getType()) || user.hasPotionEffect(effect.getType()) && ticks > user.getPotionEffect(effect.getType()).getRemaining()) {
                    user.removePotionEffect(effect.getType());
                    user.addPotionEffect(new PotionEffect(effect.getType(), effect.getLevel(), ticks));
                }
                entity.sendMessage((effect.getType().isBuff() ? ChatColor.GREEN + "" + ChatColor.BOLD + "BUFF!" : ChatColor.RED + "" + ChatColor.BOLD + "DEBUFF!") + ChatColor.RESET + ChatColor.WHITE + " You " + (e2.getPotion().getShooter().equals(entity) ? "splashed yourself" : "were splashed") + " with " + effect.getDisplayName() + ChatColor.WHITE + "!");
            }
        }
    }

    public static void updateStatistics(final Player player) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack beforeHelmet = inv.getHelmet();
        final ItemStack beforeChestplate = inv.getChestplate();
        final ItemStack beforeLeggings = inv.getLeggings();
        final ItemStack beforeBoots = inv.getBoots();
        new BukkitRunnable(){

            public void run() {
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                ItemStack afterHelmet = inv.getHelmet();
                ItemStack afterChestplate = inv.getChestplate();
                ItemStack afterLeggings = inv.getLeggings();
                ItemStack afterBoots = inv.getBoots();
                boolean helmetSimilar = ItemListener.similar(beforeHelmet, afterHelmet);
                boolean chestplateSimilar = ItemListener.similar(beforeChestplate, afterChestplate);
                boolean leggingsSimilar = ItemListener.similar(beforeLeggings, afterLeggings);
                boolean bootsSimilar = ItemListener.similar(beforeBoots, afterBoots);
                SItem helmet = null;
                SItem chestplate = null;
                SItem leggings = null;
                SItem boots = null;
                if (!helmetSimilar) {
                    helmet = SItem.find(afterHelmet);
                    PlayerUtils.updateArmorStatistics(helmet, statistics, 0);
                }
                if (!chestplateSimilar) {
                    chestplate = SItem.find(afterChestplate);
                    PlayerUtils.updateArmorStatistics(chestplate, statistics, 1);
                }
                if (!leggingsSimilar) {
                    leggings = SItem.find(afterLeggings);
                    PlayerUtils.updateArmorStatistics(leggings, statistics, 2);
                }
                if (!bootsSimilar) {
                    boots = SItem.find(afterBoots);
                    PlayerUtils.updateArmorStatistics(boots, statistics, 3);
                }
                PlayerUtils.updateInventoryStatistics(player, statistics);
                ItemListener.checkCondition(player);
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
    }

    public static void checkCondition(Player p2) {
        SItem helm = SItem.find(p2.getInventory().getHelmet());
        SItem chest = SItem.find(p2.getInventory().getChestplate());
        SItem leg = SItem.find(p2.getInventory().getLeggings());
        SItem boots = SItem.find(p2.getInventory().getBoots());
        if (null != helm && null != chest && null != leg && null != boots) {
            if (Groups.WITHER_HELMETS.contains((Object)helm.getType()) && Groups.WITHER_CHESTPLATES.contains((Object)chest.getType()) && Groups.WITHER_LEGGINGS.contains((Object)leg.getType()) && Groups.WITHER_BOOTS.contains((Object)boots.getType())) {
                if (Witherborn.WITHER_COOLDOWN.containsKey(p2.getUniqueId())) {
                    if (!Witherborn.WITHER_COOLDOWN.get(p2.getUniqueId()).booleanValue() && !Witherborn.WITHER_MAP.containsKey(p2.getUniqueId())) {
                        Witherborn w2 = new Witherborn(p2);
                        w2.spawnWither();
                    }
                } else if (!Witherborn.WITHER_MAP.containsKey(p2.getUniqueId())) {
                    Witherborn w3 = new Witherborn(p2);
                    w3.spawnWither();
                }
            } else {
                Witherborn.WITHER_MAP.remove(p2.getUniqueId());
            }
        } else {
            Witherborn.WITHER_MAP.remove(p2.getUniqueId());
        }
    }

    public static void updateStatistics1(Player player) {
        PlayerInventory inv = player.getInventory();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        ItemStack afterHelmet = inv.getHelmet();
        ItemStack afterChestplate = inv.getChestplate();
        ItemStack afterLeggings = inv.getLeggings();
        ItemStack afterBoots = inv.getBoots();
        SItem helmet = null;
        SItem chestplate = null;
        SItem leggings = null;
        SItem boots = null;
        helmet = SItem.find(afterHelmet);
        PlayerUtils.updateArmorStatistics(helmet, statistics, 0);
        chestplate = SItem.find(afterChestplate);
        PlayerUtils.updateArmorStatistics(chestplate, statistics, 1);
        leggings = SItem.find(afterLeggings);
        PlayerUtils.updateArmorStatistics(leggings, statistics, 2);
        boots = SItem.find(afterBoots);
        PlayerUtils.updateArmorStatistics(boots, statistics, 3);
        SUtil.delay(() -> PlayerUtils.updateInventoryStatistics(player, statistics), 1L);
    }

    private static boolean similar(ItemStack is, ItemStack is1) {
        return null == is && null == is1 || (null == is || null != is1) && null != is && is.isSimilar(is1);
    }

    private static boolean isAir(ItemStack is) {
        return null == is || Material.AIR == is.getType();
    }

    @EventHandler
    public void aAaB(ProjectileHitEvent enn) {
        Projectile e2 = enn.getEntity();
        if (!(e2 instanceof Arrow)) {
            return;
        }
        if (!(((Arrow)e2).getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)((Arrow)e2).getShooter();
        boolean ACT = true;
        if (e2.isOnGround()) {
            return;
        }
        for (Entity enderman_1 : e2.getWorld().getNearbyEntities(e2.getLocation(), 1.5, 1.5, 1.5)) {
            if (!(enderman_1 instanceof Enderman) || !ACT || e2.isOnGround() || enderman_1.isDead()) continue;
            if (null == SItem.find(player.getItemInHand())) {
                e2.remove();
                break;
            }
            if (SMaterial.TERMINATOR != SItem.find(player.getItemInHand()).getType() && SMaterial.JUJU_SHORTBOW != SItem.find(player.getItemInHand()).getType()) {
                e2.remove();
                return;
            }
            ACT = false;
            EntityDamageByEntityEvent bl2 = new EntityDamageByEntityEvent((Entity)e2, enderman_1, EntityDamageEvent.DamageCause.CUSTOM, 1);
            Bukkit.getPluginManager().callEvent((Event)bl2);
            ((LivingEntity)enderman_1).setHealth(((LivingEntity)enderman_1).getHealth() - Math.min(((LivingEntity)enderman_1).getHealth(), bl2.getDamage()));
            e2.remove();
        }
    }

    @EventHandler
    public void changeBlock(EntityChangeBlockEvent event) {
        Entity fallingBlock = event.getEntity();
        if (EntityType.FALLING_BLOCK == event.getEntityType() && fallingBlock.hasMetadata("t")) {
            Block block = event.getBlock();
            block.setType(Material.AIR);
            event.setCancelled(true);
            List entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (Entity e2 : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (!(e2 instanceof Item)) continue;
                e2.remove();
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            List fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (EntityType.FALLING_BLOCK == entity.getType() && entity.hasMetadata("t")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    Player p2 = (Player)entity;
                    JollyPinkGiant.damagePlayer(p2);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity)entity).damage(0.0);
                }
            });
            fallingBlock.remove();
        }
    }

    @EventHandler
    public void changeBlockF1(EntityChangeBlockEvent event) {
        Entity fallingBlock = event.getEntity();
        if (EntityType.FALLING_BLOCK == event.getEntityType() && fallingBlock.hasMetadata("f")) {
            Block block = event.getBlock();
            block.setType(Material.AIR);
            event.setCancelled(true);
            List entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (Entity e2 : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (!(e2 instanceof Item)) continue;
                e2.remove();
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            List fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (EntityType.FALLING_BLOCK == entity.getType() && entity.hasMetadata("f")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    Player p2 = (Player)entity;
                    SadanGiant.damagePlayer(p2);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity)entity).damage(0.0);
                }
            });
            fallingBlock.remove();
        }
    }

    @EventHandler
    public void onentityded(EntityDeathEvent e2) {
        if (e2.getEntity().getWorld().getName().contains("f6")) {
            e2.setDroppedExp(0);
        }
    }
}

