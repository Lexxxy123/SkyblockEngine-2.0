package in.godspunky.skyblock.item;

import in.godspunky.skyblock.collection.ItemCollection;
import in.godspunky.skyblock.enchantment.Enchantment;
import in.godspunky.skyblock.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.StaticDragonManager;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.JollyPinkGiant;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanGiant;
import in.godspunky.skyblock.item.armor.Witherborn;
import in.godspunky.skyblock.item.storage.Storage;
import in.godspunky.skyblock.item.weapon.EdibleMace;
import in.godspunky.skyblock.listener.PListener;
import in.godspunky.skyblock.skill.Skill;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.SMongoLoader;
import in.godspunky.skyblock.util.*;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.*;

import java.util.*;

public class ItemListener extends PListener {
    public static final Map<Player, String> Classes;
    public static final Map<Player, Boolean> IsDead;

    @EventHandler
    public void useEtherWarp(final PlayerInteractEvent e) {
        if (!SItem.isSpecItem(e.getItem())) {
            return;
        }
        final SItem sItem = SItem.find(e.getItem());
        if (sItem == null) {
            return;
        }
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        updateStatistics(e.getPlayer());
        final Action action = e.getAction();
        if (sItem.getDataString("etherwarp_trans").equals("true") && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && e.getPlayer().isSneaking()) {
            if (!SItem.isAbleToDoEtherWarpTeleportation(player, sItem)) {
                final Ability ability = sItem.getType().getAbility();
                if (ability != null) {
                    final AbilityActivation activation = ability.getAbilityActivation();
                    if (activation == AbilityActivation.LEFT_CLICK || activation == AbilityActivation.RIGHT_CLICK) {
                        if (activation == AbilityActivation.LEFT_CLICK) {
                            if (action != Action.LEFT_CLICK_AIR) {
                                if (action != Action.LEFT_CLICK_BLOCK) {
                                    return;
                                }
                            }
                        } else if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
                            return;
                        }
                        PlayerUtils.useAbility(player, sItem);
                    }
                }
                return;
            }
            final int mana = Repeater.MANA_MAP.get(uuid);
            final int cost = PlayerUtils.getFinalManaCost(player, sItem, 250);
            final int resMana = mana - cost;
            if (resMana >= 0) {
                Repeater.MANA_MAP.remove(uuid);
                Repeater.MANA_MAP.put(uuid, resMana);
                final long c = System.currentTimeMillis();
                Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement() {
                    @Override
                    public String getReplacement() {
                        return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + "Ether Transmission" + ChatColor.AQUA + ")";
                    }

                    @Override
                    public long getEnd() {
                        return c + 2000L;
                    }
                });
                SItem.etherWarpTeleportation(e.getPlayer(), sItem);
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

    @EventHandler
    public void onPlayerInteracting(final PlayerInteractEvent e) {
    }

    @EventHandler
    public void PotionsSplash(final PotionSplashEvent e) {
        for (final Entity ef : e.getAffectedEntities()) {
            if (ef.hasMetadata("LD")) {
                for (final PotionEffect pe : e.getEntity().getEffects()) {
                    if (pe.getType() == PotionEffectType.HEAL) {
                        e.setCancelled(true);
                    } else {
                        if (pe.getType() != PotionEffectType.HARM) {
                            continue;
                        }
                        e.setCancelled(true);
                        ((LivingEntity) ef).damage(1.0E-4);
                    }
                }
            }
        }
        if (e.getEntity().getShooter() instanceof LivingEntity && ((LivingEntity) e.getEntity().getShooter()).hasMetadata("LD")) {
            for (final Entity ef : e.getAffectedEntities()) {
                if (ef.hasMetadata("LD")) {
                    e.setCancelled(true);
                    if (ef.isDead()) {
                        return;
                    }
                    ((LivingEntity) ef).setHealth(Math.min(((LivingEntity) ef).getMaxHealth(), ((LivingEntity) ef).getHealth() + ((LivingEntity) ef).getMaxHealth() * 10.0 / 100.0));
                } else {
                    if (!(ef instanceof Player)) {
                        continue;
                    }
                    e.setCancelled(true);
                    ((LivingEntity) ef).setHealth(Math.min(((LivingEntity) ef).getMaxHealth(), ((LivingEntity) ef).getHealth() + 500.0));
                    ef.sendMessage(Sputnik.trans("&a&lBUFF! &fYou were splashed with &cHealing V&f!"));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            for (final Player p : e.getPlayer().getWorld().getPlayers()) {
                if (p == e.getPlayer()) {
                    continue;
                }
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) e.getPlayer()).getHandle(), 0));
            }
        }
        if (!SItem.isSpecItem(e.getItem())) {
            return;
        }
        final SItem sItem = SItem.find(e.getItem());
        if (sItem == null) {
            return;
        }
        if (sItem.getType() == SMaterial.HIDDEN_SOUL_WHIP) {
            e.setCancelled(true);
        }
        if ((sItem.getStack().getType() == Material.MONSTER_EGG || sItem.getStack().getType() == Material.MONSTER_EGGS) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
        if (sItem.getType() == SMaterial.HIDDEN_GYRO_EYE || sItem.getType() == SMaterial.HIDDEN_VOID_FRAGMENT) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
        final Action action = e.getAction();
        if (sItem.getType().getStatistics().getSpecificType() == SpecificItemType.HELMET && action == Action.RIGHT_CLICK_AIR && isAir(e.getPlayer().getInventory().getHelmet())) {
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
        }
        final Player player = e.getPlayer();
        final Ability ability = sItem.getType().getAbility();
        Label_0409:
        {
            if (ability != null) {
                final AbilityActivation activation = ability.getAbilityActivation();
                if (activation == AbilityActivation.LEFT_CLICK || activation == AbilityActivation.RIGHT_CLICK) {
                    if (activation == AbilityActivation.LEFT_CLICK) {
                        if (action != Action.LEFT_CLICK_AIR) {
                            if (action != Action.LEFT_CLICK_BLOCK) {
                                break Label_0409;
                            }
                        }
                    } else if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
                        break Label_0409;
                    }
                    if (sItem.getDataString("etherwarp_trans").equals("true")) {
                        if (!player.isSneaking()) {
                            PlayerUtils.useAbility(player, sItem);
                        }
                    } else {
                        PlayerUtils.useAbility(player, sItem);
                    }
                }
            }
        }
        final MaterialFunction function = sItem.getType().getFunction();
        if (function != null) {
            function.onInteraction(e);
        }
    }

    @EventHandler
    public void onPlayerMage(final PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final Action action = e.getAction();
        if (!player.getWorld().getName().equals("dungeon")) {
            return;
        }
        if (!ItemListener.Classes.containsKey(player)) {
            ItemListener.Classes.put(player, "M");
        }
        if (ItemListener.Classes.get(player) == "M" && action == Action.LEFT_CLICK_AIR) {
            String ACT = "true";
            final ItemStack item = player.getInventory().getItemInHand();
            final SItem sitem = SItem.find(item);
            if (sitem == null) {
                return;
            }
            final SMaterial material = sitem.getType();
            final MaterialStatistics statistics = material.getStatistics();
            final SpecificItemType type = statistics.getSpecificType();
            if (type.getName().contains("SWORD")) {
                final Location blockLocation = player.getTargetBlock((Set) null, 30).getLocation();
                final Location crystalLocation = player.getEyeLocation();
                final Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
                final double count = 25.0;
                for (int i = 1; i <= (int) count; ++i) {
                    for (final Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.5, 0.0, 0.5)) {
                        if (ACT == "false") {
                            return;
                        }
                        if (entity.isDead()) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity)) {
                            continue;
                        }
                        if (entity.hasMetadata("GiantSword")) {
                            continue;
                        }
                        if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager) {
                            continue;
                        }
                        if (entity instanceof ArmorStand) {
                            continue;
                        }
                        final User user = User.getUser(player.getUniqueId());
                        final int damage = 0;
                        double enchantBonus = 0.0;
                        final double potionBonus = 0.0;
                        final PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                        double critDamage = statistics2.getCritDamage().addAll();
                        final double speed = statistics2.getSpeed().addAll();
                        final double realSpeed = speed * 100.0 % 25.0;
                        final double realSpeedDIV = speed - realSpeed;
                        final double realSpeedDIVC = realSpeedDIV / 25.0;
                        final PlayerInventory inv = player.getInventory();
                        final SItem helmet = SItem.find(inv.getHelmet());
                        if (helmet != null && helmet.getType() == SMaterial.WARDEN_HELMET) {
                            enchantBonus += (100.0 + 20.0 * realSpeedDIVC) / 100.0;
                        }
                        for (final Enchantment enchantment : sitem.getEnchantments()) {
                            final EnchantmentType type2 = enchantment.getType();
                            final int level = enchantment.getLevel();
                            if (type2 == EnchantmentType.SHARPNESS) {
                                enchantBonus += level * 6 / 100.0;
                            }
                            if (type2 == EnchantmentType.SMITE && Groups.UNDEAD_MOBS.contains(entity.getType())) {
                                enchantBonus += level * 8 / 100.0;
                            }
                            if (type2 == EnchantmentType.ENDER_SLAYER && Groups.ENDERMAN.contains(entity.getType())) {
                                enchantBonus += level * 12 / 100.0;
                            }
                            if (type2 == EnchantmentType.BANE_OF_ARTHROPODS && Groups.ARTHROPODS.contains(entity.getType())) {
                                enchantBonus += level * 8 / 100.0;
                            }
                            if (type2 == EnchantmentType.DRAGON_HUNTER && Groups.ENDERDRAGON.contains(entity.getType())) {
                                enchantBonus += level * 8 / 100.0;
                            }
                            if (type2 == EnchantmentType.CRITICAL) {
                                critDamage += level * 10 / 100.0;
                            }
                        }
                        final PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics) material.getStatistics();
                        final double baseDamage = (5 + playerBoostStatistics.getBaseDamage() + statistics2.getStrength().addAll() / 5.0) * (1.0 + statistics2.getStrength().addAll() / 100.0);
                        final int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                        final double weaponBonus = 0.0;
                        final double armorBonus = 1.0;
                        final int critChanceMul = (int) (statistics2.getCritChance().addAll() * 100.0);
                        final int chance = SUtil.random(0, 100);
                        if (chance > critChanceMul) {
                            critDamage = 0.0;
                        }
                        final double damageMultiplier = 1.0 + combatLevel * 0.04 + enchantBonus + weaponBonus;
                        final double finalCritDamage = critDamage;
                        double finalDamage = baseDamage * damageMultiplier * armorBonus * (1.0 + finalCritDamage);
                        final double finalPotionBonus = potionBonus;
                        if (entity.isDead()) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity)) {
                            continue;
                        }
                        if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity instanceof Item) {
                            continue;
                        }
                        if (entity instanceof ItemFrame) {
                            continue;
                        }
                        if (EdibleMace.edibleMace.containsKey(player.getUniqueId()) && EdibleMace.edibleMace.get(player.getUniqueId())) {
                            finalDamage *= 2.0;
                            EdibleMace.edibleMace.put(player.getUniqueId(), false);
                        }
                        final ArmorStand stand3 = (ArmorStand) new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND).getEntity();
                        if (finalCritDamage == 0.0) {
                            stand3.setCustomName("" + ChatColor.GRAY + (int) finalDamage);
                        } else {
                            stand3.setCustomName(SUtil.rainbowize("✧" + (int) finalDamage + "✧"));
                        }
                        stand3.setCustomNameVisible(true);
                        stand3.setGravity(false);
                        stand3.setVisible(false);
                        user.damageEntity((Damageable) entity, finalDamage);
                        new BukkitRunnable() {
                            public void run() {
                                stand3.remove();
                                this.cancel();
                            }
                        }.runTaskLater(SkySimEngine.getPlugin(), 30L);
                        ACT = "false";
                    }
                    player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.FIREWORKS_SPARK, 24, 1, 0.0f, 0.0f, 0.0f, 1.0f, 0, 64);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        final Player player = (Player) e.getPlayer();
        final Inventory storage = Storage.getCurrentStorageOpened(player);
        if (storage == null) {
            return;
        }
        final Inventory inventory = e.getInventory();
        final SItem hand = SItem.find(player.getItemInHand());
        if (hand == null) {
            return;
        }
        final NBTTagCompound storageData = new NBTTagCompound();
        for (int i = 0; i < inventory.getSize(); ++i) {
            final SItem sItem = SItem.find(inventory.getItem(i));
            if (sItem == null) {
                final SItem equiv = SItem.of(inventory.getItem(i));
                if (equiv != null) {
                    storageData.setByteArray(String.valueOf(i), SUtil.gzipCompress(equiv.toCompound().toString().getBytes()));
                } else {
                    storageData.remove(String.valueOf(i));
                }
            } else {
                storageData.setByteArray(String.valueOf(i), SUtil.gzipCompress(sItem.toCompound().toString().getBytes()));
            }
        }
        hand.getData().set("storage_data", storageData);
        hand.update();
        Storage.closeCurrentStorage(player);
    }

    @EventHandler
    public void onPlayerFlight(final PlayerToggleFlightEvent e) {
        final Player player = e.getPlayer();
        final GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) {
            return;
        }
        for (final ItemStack stack : player.getInventory().getArmorContents()) {
            final SItem sItem = SItem.find(stack);
            if (sItem != null) {
                final Ability ability = sItem.getType().getAbility();
                if (ability != null && e.isFlying() && ability.getAbilityActivation() == AbilityActivation.FLIGHT) {
                    e.setCancelled(true);
                    PlayerUtils.useAbility(player, sItem);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSneak(final PlayerToggleSneakEvent e) {
        final Player player = e.getPlayer();
        final GameMode gameMode = player.getGameMode();
        for (final ItemStack stack : player.getInventory().getArmorContents()) {
            final SItem sItem = SItem.find(stack);
            if (sItem != null) {
                final Ability ability = sItem.getType().getAbility();
                if (ability != null && player.isSneaking() && ability.getAbilityActivation() == AbilityActivation.SNEAK) {
                    PlayerUtils.useAbility(player, sItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getView().getTopInventory().getType() != InventoryType.CRAFTING) {
            return;
        }
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
            return;
        }
        if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            return;
        }
        final Inventory inventory = e.getClickedInventory();
        if (inventory == null) {
            return;
        }
        if (inventory.getType() != InventoryType.PLAYER) {
            return;
        }
        final ItemStack current = e.getCurrentItem();
        if (current == null) {
            return;
        }
        SItem sItem = SItem.find(current);
        if (sItem == null) {
            sItem = SItem.of(current);
        }
        updateStatistics((Player) e.getWhoClicked());
        if (sItem.getType().getStatistics().getSpecificType() == null || sItem.getType().getStatistics().getSpecificType() != SpecificItemType.HELMET) {
            return;
        }
        final PlayerInventory playerInventory = (PlayerInventory) inventory;
        if (!isAir(playerInventory.getHelmet())) {
            return;
        }
        e.setCancelled(true);
        e.setCurrentItem(new ItemStack(Material.AIR));
        playerInventory.setHelmet(current);
    }

    @EventHandler
    public void onArmorChange(final InventoryClickEvent e) {
        final Player player = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType() != InventoryType.PLAYER && e.getClickedInventory().getType() != InventoryType.CRAFTING) {
            return;
        }
        updateStatistics((Player) e.getWhoClicked());
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
    }

    @EventHandler
    public void onArmorChange1(final InventoryCloseEvent e) {
        final Player player = (Player) e.getPlayer();
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
        updateStatistics(player);
    }

    @EventHandler
    public void onArmorChange2(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        ItemStack helmet = player.getInventory().getHelmet();
        SUtil.delay(() -> player.getInventory().setHelmet(helmet), 10L);
        SItem helmet1 = SItem.find(player.getInventory().getHelmet());
        if (helmet1 == null) {
            return;
        }
        TickingMaterial tickingMaterial = helmet1.getType().getTickingInstance();
        if (tickingMaterial != null) {
            statistics.tickItem(39, tickingMaterial.getInterval(), () -> tickingMaterial.tick(helmet1, Bukkit.getPlayer(statistics.getUuid())));
        }
        updateStatistics(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClick(final InventoryClickEvent e) {
        final ItemStack stack = e.getCurrentItem();
        if (stack == null) {
            return;
        }
        final SItem sItem = SItem.find(stack);
        if (sItem == null) {
            return;
        }
        if (sItem.getType().getFunction() == null) {
            return;
        }
        sItem.getType().getFunction().onInventoryClick(sItem, e);
    }

    @EventHandler
    public void onItemMove(final InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        if (e.getSlot() != 8) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        final SItem sItem = SItem.find(e.getItemInHand());
        if (sItem == null) {
            return;
        }
        if (sItem.getType().getStatistics().getSpecificType() == SpecificItemType.HELMET && isAir(e.getPlayer().getInventory().getHelmet())) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
            return;
        }
        if (!sItem.getType().isCraft()) {
            if (sItem.getType().getStatistics().getType() != GenericItemType.BLOCK) {
                e.setCancelled(true);
            } else {
                new SBlock(e.getBlockPlaced().getLocation(), sItem.getType(), sItem.getData()).save();
            }
        }
    }

    @EventHandler
    public void onFrameInteract(final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Player player = e.getPlayer();
        final Block block = e.getClickedBlock();
        final ItemStack hand = e.getItem();
        if (hand == null) {
            return;
        }
        final SItem item = SItem.find(hand);
        if (item == null) {
            return;
        }
        if (block.getType() != Material.ENDER_PORTAL_FRAME) {
            return;
        }
        final SBlock sBlock = SBlock.getBlock(block.getLocation());
        if (sBlock == null) {
            e.setCancelled(true);
            return;
        }
        if (sBlock.getType() != SMaterial.SUMMONING_FRAME) {
            e.setCancelled(true);
            return;
        }
        if (!block.hasMetadata("placer")) {
            if (item.getType() != SMaterial.SUMMONING_EYE) {
                return;
            }
            block.setMetadata("placer", new FixedMetadataValue(this.plugin, player.getUniqueId()));
            final BlockState state = block.getState();
            state.setRawData((byte) 4);
            state.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SLEEPING_EYE).getStack());
            final List<Location> locations = StaticDragonManager.EYES.containsKey(player.getUniqueId()) ? StaticDragonManager.EYES.get(player.getUniqueId()) : new ArrayList<Location>();
            locations.add(block.getLocation());
            StaticDragonManager.EYES.remove(player.getUniqueId());
            StaticDragonManager.EYES.put(player.getUniqueId(), locations);
            int quantity = 0;
            for (final List<Location> ls : StaticDragonManager.EYES.values()) {
                quantity += ls.size();
            }
            for (final Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().getName().equals("dragon")) {
                    p.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.GREEN + player.getName() + ChatColor.LIGHT_PURPLE + " placed a Summoning Eye! " + ((quantity == 8) ? "Brace yourselves! " : "") + ChatColor.GRAY + "(" + ((quantity == 8) ? ChatColor.GREEN : ChatColor.YELLOW) + quantity + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                }
            }
            if (quantity != 8) {
                return;
            }
            final List<UUID> cleared = new ArrayList<UUID>();
            for (final List<Location> ls2 : StaticDragonManager.EYES.values()) {
                for (final Location location : ls2) {
                    final Block b = location.getBlock();
                    final List<MetadataValue> values = b.getMetadata("placer");
                    final Player p2 = Bukkit.getPlayer((UUID) values.get(0).value());
                    if (p2 == null) {
                        continue;
                    }
                    if (cleared.contains(p2.getUniqueId())) {
                        continue;
                    }
                    final PlayerInventory inventory = p2.getInventory();
                    for (int i = 0; i < inventory.getSize(); ++i) {
                        final SItem si = SItem.find(inventory.getItem(i));
                        if (si != null) {
                            if (si.getType() == SMaterial.SLEEPING_EYE) {
                                inventory.setItem(i, SItem.of(SMaterial.REMNANT_OF_THE_EYE).getStack());
                            }
                        }
                    }
                    p2.sendMessage(ChatColor.DARK_PURPLE + "Your Sleeping Eyes have been awoken by the magic of the Dragon. They are now Remnants of the Eye!");
                    cleared.add(p2.getUniqueId());
                }
            }
            StaticDragonManager.ACTIVE = true;
            block.getWorld().playSound(block.getLocation(), Sound.ENDERMAN_STARE, 50.0f, -2.0f);
            new BukkitRunnable() {
                public void run() {
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_DEATH, 50.0f, -2.0f);
                }
            }.runTaskLater(this.plugin, 90L);
            new BukkitRunnable() {
                public void run() {
                    for (int i = 0; i < 3; ++i) {
                        block.getWorld().playSound(block.getLocation(), Sound.EXPLODE, 50.0f, -2.0f);
                    }
                    SEntityType dragonType = SEntityType.PROTECTOR_DRAGON;
                    final int chance = SUtil.random(0, 100);
                    if (chance >= 16) {
                        dragonType = SEntityType.OLD_DRAGON;
                    }
                    if (chance >= 32) {
                        dragonType = SEntityType.WISE_DRAGON;
                    }
                    if (chance >= 48) {
                        dragonType = SEntityType.UNSTABLE_DRAGON;
                    }
                    if (chance >= 64) {
                        dragonType = SEntityType.YOUNG_DRAGON;
                    }
                    if (chance >= 80) {
                        dragonType = SEntityType.STRONG_DRAGON;
                    }
                    if (chance >= 96) {
                        dragonType = SEntityType.SUPERIOR_DRAGON;
                    }
                    final SEntity entity = new SEntity(block.getLocation().clone().add(0.0, 53.0, 0.0), dragonType);
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
                    for (final Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getWorld().getName().equals("dragon")) {
                            continue;
                        }
                        final Vector vector = p.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
                        p.setVelocity(vector.normalize().multiply(40.0).setY(100.0));
                    }
                    StaticDragonManager.DRAGON = entity;
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_GROWL, 50.0f, 1.0f);
                    for (final Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getWorld().getName().equals("dragon")) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "The " + ChatColor.RED + ChatColor.BOLD + entity.getStatistics().getEntityName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " has spawned!");
                        }
                    }
                }
            }.runTaskLater(this.plugin, 180L);
        } else {
            final List<MetadataValue> values2 = block.getMetadata("placer");
            final Player p3 = Bukkit.getPlayer((UUID) values2.get(0).value());
            if (p3 == null) {
                return;
            }
            if (item.getType() != SMaterial.SLEEPING_EYE) {
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
            block.removeMetadata("placer", this.plugin);
            final BlockState state2 = block.getState();
            state2.setRawData((byte) 0);
            state2.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SUMMONING_EYE).getStack());
            StaticDragonManager.EYES.get(p3.getUniqueId()).remove(block.getLocation());
            player.sendMessage(ChatColor.DARK_PURPLE + "You recovered a Summoning Eye!");
        }
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent e) {
        final Item item = e.getItem();
        final Player player = e.getPlayer();
        updateStatistics(player);
        NBTTagCompound compound = CraftItemStack.asNMSCopy(item.getItemStack()).getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        if (!compound.hasKey("type")) {
            item.getItemStack().setItemMeta(SItem.of(item.getItemStack()).getStack().getItemMeta());
        }
        if (item.hasMetadata("owner")) {
            final List<MetadataValue> o = item.getMetadata("owner");
            if (o.size() != 0 && !o.get(0).asString().equals(e.getPlayer().getUniqueId().toString())) {
                e.setCancelled(true);
                return;
            }
        }
        final User user = User.getUser(player.getUniqueId());
        final ItemStack stack = item.getItemStack();
        final SItem sItem = SItem.find(stack);
        if (sItem == null) {
            throw new NullPointerException("Something messed up! Check again");
        }
        if (item.hasMetadata("obtained")) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().getName().equals("dragon")) {
                    if (!sItem.getFullName().equals("§6Ender Dragon") && !sItem.getFullName().equals("§5Ender Dragon")) {
                        p.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + sItem.getFullName() + ChatColor.YELLOW + "!");
                    } else {
                        p.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName() + ChatColor.YELLOW + "!");
                    }
                }
            }
        }
        if (sItem.getOrigin() == ItemOrigin.NATURAL_BLOCK || sItem.getOrigin() == ItemOrigin.MOB) {
            sItem.setOrigin(ItemOrigin.UNKNOWN);
            final ItemCollection collection = ItemCollection.getByMaterial(sItem.getType(), stack.getDurability());
            if (collection != null) {
                final int prev = user.getCollection(collection);
                user.addToCollection(collection, stack.getAmount());
                SkySimEngine.getPlugin().getDataLoader().save(player.getUniqueId());
                if (prev == 0) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  COLLECTION UNLOCKED " + ChatColor.RESET + ChatColor.YELLOW + collection.getName());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent e) {
        final SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (sItem != null && (sItem.getType() == SMaterial.SKYBLOCK_MENU || sItem.getType() == SMaterial.QUIVER_ARROW)) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
    }

    @EventHandler
    public void onItemMove(final PlayerDropItemEvent e) {
        final SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (sItem != null && (sItem.getType() == SMaterial.SKYBLOCK_MENU || sItem.getType() == SMaterial.QUIVER_ARROW)) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
    }

    @EventHandler
    public void onItemDrop1(final PlayerDropItemEvent e) {
        final SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (sItem != null && sItem.getType() == SMaterial.BONEMERANG && e.getItemDrop().getItemStack().toString().contains("GHAST_TEAR")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove1(final PlayerDropItemEvent e) {
        final SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (sItem != null && (sItem.getType() == SMaterial.SKYBLOCK_MENU || sItem.getType() == SMaterial.QUIVER_ARROW)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFishingRodReel(final PlayerFishEvent e) {
        final SItem rod = SItem.find(e.getPlayer().getItemInHand());
        if (rod == null) {
            return;
        }
        e.getHook().setMetadata("owner", new FixedMetadataValue(SkySimEngine.getPlugin(), e.getPlayer()));
        final MaterialFunction function = rod.getType().getFunction();
        if (function == null) {
            return;
        }
        if (function instanceof FishingRodFunction) {
            ((FishingRodFunction) function).onFish(rod, e);
        }
    }

    @EventHandler
    public void onPotionSplash(final PotionSplashEvent e) {
        final SItem item = SItem.find(e.getPotion().getItem());
        if (item == null) {
            return;
        }
        if (!item.isPotion()) {
            return;
        }
        e.setCancelled(true);
        for (final LivingEntity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) {
                continue;
            }
            final User user = User.getUser(entity.getUniqueId());
            if (user == null) {
                continue;
            }
            for (final in.godspunky.skyblock.potion.PotionEffect effect : item.getPotionEffects()) {
                PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
                if (effect.getType().getOnDrink() != null) {
                    effect.getType().getOnDrink().accept(effect, (Player) entity);
                }
                final long ticks = (long) (effect.getDuration() * e.getIntensity(entity));
                if (!user.hasPotionEffect(effect.getType()) || (user.hasPotionEffect(effect.getType()) && ticks > user.getPotionEffect(effect.getType()).getRemaining())) {
                    user.removePotionEffect(effect.getType());
                    user.addPotionEffect(new in.godspunky.skyblock.potion.PotionEffect(effect.getType(), effect.getLevel(), ticks));
                }
                entity.sendMessage((effect.getType().isBuff() ? (ChatColor.GREEN + "" + ChatColor.BOLD + "BUFF!") : (ChatColor.RED + "" + ChatColor.BOLD + "DEBUFF!")) + ChatColor.RESET + ChatColor.WHITE + " You " + (e.getPotion().getShooter().equals(entity) ? "splashed yourself" : "were splashed") + " with " + effect.getDisplayName() + ChatColor.WHITE + "!");
            }
        }
    }

    public static void updateStatistics(final Player player) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack beforeHelmet = inv.getHelmet();
        final ItemStack beforeChestplate = inv.getChestplate();
        final ItemStack beforeLeggings = inv.getLeggings();
        final ItemStack beforeBoots = inv.getBoots();
        new BukkitRunnable() {
            public void run() {
                final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                final ItemStack afterHelmet = inv.getHelmet();
                final ItemStack afterChestplate = inv.getChestplate();
                final ItemStack afterLeggings = inv.getLeggings();
                final ItemStack afterBoots = inv.getBoots();
                final boolean helmetSimilar = similar(beforeHelmet, afterHelmet);
                final boolean chestplateSimilar = similar(beforeChestplate, afterChestplate);
                final boolean leggingsSimilar = similar(beforeLeggings, afterLeggings);
                final boolean bootsSimilar = similar(beforeBoots, afterBoots);
                SItem helmet = null;
                SItem chestplate = null;
                SItem leggings = null;
                SItem boots = null;
                if (!helmetSimilar) {
                    PlayerUtils.updateArmorStatistics(helmet = SItem.find(afterHelmet), statistics, 0);
                }
                if (!chestplateSimilar) {
                    PlayerUtils.updateArmorStatistics(chestplate = SItem.find(afterChestplate), statistics, 1);
                }
                if (!leggingsSimilar) {
                    PlayerUtils.updateArmorStatistics(leggings = SItem.find(afterLeggings), statistics, 2);
                }
                if (!bootsSimilar) {
                    PlayerUtils.updateArmorStatistics(boots = SItem.find(afterBoots), statistics, 3);
                }
                PlayerUtils.updateInventoryStatistics(player, statistics);
                User.getUser(player.getUniqueId()).updateArmorInventory();
                ItemListener.checkCondition(player);
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 1L);
    }

    public static void checkCondition(final Player p) {
        final SItem helm = SItem.find(p.getInventory().getHelmet());
        final SItem chest = SItem.find(p.getInventory().getChestplate());
        final SItem leg = SItem.find(p.getInventory().getLeggings());
        final SItem boots = SItem.find(p.getInventory().getBoots());
        if (helm != null && chest != null && leg != null && boots != null) {
            if (Groups.WITHER_HELMETS.contains(helm.getType()) && Groups.WITHER_CHESTPLATES.contains(chest.getType()) && Groups.WITHER_LEGGINGS.contains(leg.getType()) && Groups.WITHER_BOOTS.contains(boots.getType())) {
                if (Witherborn.WITHER_COOLDOWN.containsKey(p.getUniqueId())) {
                    if (!Witherborn.WITHER_COOLDOWN.get(p.getUniqueId()) && !Witherborn.WITHER_MAP.containsKey(p.getUniqueId())) {
                        final Witherborn w = new Witherborn(p);
                        w.spawnWither();
                    }
                } else if (!Witherborn.WITHER_MAP.containsKey(p.getUniqueId())) {
                    final Witherborn w = new Witherborn(p);
                    w.spawnWither();
                }
            } else Witherborn.WITHER_MAP.remove(p.getUniqueId());
        } else Witherborn.WITHER_MAP.remove(p.getUniqueId());
    }

    public static void updateStatistics1(final Player player) {
        final PlayerInventory inv = player.getInventory();
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        final ItemStack afterHelmet = inv.getHelmet();
        final ItemStack afterChestplate = inv.getChestplate();
        final ItemStack afterLeggings = inv.getLeggings();
        final ItemStack afterBoots = inv.getBoots();
        SItem helmet = null;
        SItem chestplate = null;
        SItem leggings = null;
        SItem boots = null;
        PlayerUtils.updateArmorStatistics(helmet = SItem.find(afterHelmet), statistics, 0);
        PlayerUtils.updateArmorStatistics(chestplate = SItem.find(afterChestplate), statistics, 1);
        PlayerUtils.updateArmorStatistics(leggings = SItem.find(afterLeggings), statistics, 2);
        PlayerUtils.updateArmorStatistics(boots = SItem.find(afterBoots), statistics, 3);
        SUtil.delay(() -> PlayerUtils.updateInventoryStatistics(player, statistics), 1L);
    }

    private static boolean similar(final ItemStack is, final ItemStack is1) {
        return (is == null && is1 == null) || ((is == null || is1 != null) && is != null && is.isSimilar(is1));
    }

    private static boolean isAir(final ItemStack is) {
        return is == null || is.getType() == Material.AIR;
    }

    @EventHandler
    public void aAaB(final ProjectileHitEvent enn) {
        final Entity e = enn.getEntity();
        if (!(e instanceof Arrow)) {
            return;
        }
        if (!(((Arrow) e).getShooter() instanceof Player)) {
            return;
        }
        final Player player = (Player) ((Arrow) e).getShooter();
        boolean ACT = true;
        if (e.isOnGround()) {
            return;
        }
        for (final Entity enderman_1 : e.getWorld().getNearbyEntities(e.getLocation(), 1.5, 1.5, 1.5)) {
            if (enderman_1 instanceof Enderman && ACT && !e.isOnGround() && !enderman_1.isDead()) {
                if (SItem.find(player.getItemInHand()) == null) {
                    e.remove();
                    break;
                }
                if (SItem.find(player.getItemInHand()).getType() != SMaterial.TERMINATOR && SItem.find(player.getItemInHand()).getType() != SMaterial.JUJU_SHORTBOW) {
                    e.remove();
                    return;
                }
                ACT = false;
                final EntityDamageByEntityEvent bl = new EntityDamageByEntityEvent(e, enderman_1, EntityDamageEvent.DamageCause.CUSTOM, 1);
                Bukkit.getPluginManager().callEvent(bl);
                ((LivingEntity) enderman_1).setHealth(((LivingEntity) enderman_1).getHealth() - Math.min(((LivingEntity) enderman_1).getHealth(), bl.getDamage()));
                e.remove();
            }
        }
    }

    @EventHandler
    public void changeBlock(final EntityChangeBlockEvent event) {
        final Entity fallingBlock = event.getEntity();
        if (event.getEntityType() == EntityType.FALLING_BLOCK && fallingBlock.hasMetadata("t")) {
            final Block block = event.getBlock();
            block.setType(Material.AIR);
            event.setCancelled(true);
            final List<Entity> entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (final Entity e : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            final List<Entity> fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (entity.getType() == EntityType.FALLING_BLOCK && entity.hasMetadata("t")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    final Player p = (Player) entity;
                    JollyPinkGiant.damagePlayer(p);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity) entity).damage(0.0);
                }
            });
            fallingBlock.remove();
        }
    }

    @EventHandler
    public void changeBlockF1(final EntityChangeBlockEvent event) {
        final Entity fallingBlock = event.getEntity();
        if (event.getEntityType() == EntityType.FALLING_BLOCK && fallingBlock.hasMetadata("f")) {
            final Block block = event.getBlock();
            block.setType(Material.AIR);
            event.setCancelled(true);
            final List<Entity> entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (final Entity e : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            final List<Entity> fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (entity.getType() == EntityType.FALLING_BLOCK && entity.hasMetadata("f")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    final Player p = (Player) entity;
                    SadanGiant.damagePlayer(p);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity) entity).damage(0.0);
                }
            });
            fallingBlock.remove();
        }
    }

    @EventHandler
    public void onentityded(final EntityDeathEvent e) {
        if (e.getEntity().getWorld().getName().startsWith("f6")) {
            e.setDroppedExp(0);
        }
    }

    static {
        Classes = new HashMap<Player, String>();
        IsDead = new HashMap<Player, Boolean>();
    }
}
