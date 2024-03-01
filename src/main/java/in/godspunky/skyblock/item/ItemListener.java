package in.godspunky.skyblock.item;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.features.collection.ItemCollection;
import in.godspunky.skyblock.features.enchantment.Enchantment;
import in.godspunky.skyblock.features.enchantment.EnchantmentType;
import in.godspunky.skyblock.entity.StaticDragonManager;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.JollyPinkGiant;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanGiant;
import in.godspunky.skyblock.item.armor.Witherborn;
import in.godspunky.skyblock.item.storage.Storage;
import in.godspunky.skyblock.item.weapon.EdibleMace;
import in.godspunky.skyblock.listener.PListener;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.user.PlayerStatistics;
import in.godspunky.skyblock.user.PlayerUtils;
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
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.user.User;

import java.util.*;

public class ItemListener extends PListener {
    public static final Map<Player, String> Classes;
    public static final Map<Player, Boolean> IsDead;

    @EventHandler
    public void useEtherWarp(PlayerInteractEvent e) {
        if (!SItem.isSpecItem(e.getItem())) {
            return;
        }
        SItem sItem = SItem.find(e.getItem());
        if (null == sItem) {
            return;
        }
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        updateStatistics(e.getPlayer());
        Action action = e.getAction();
        if (sItem.getDataString("etherwarp_trans").equals("true") && (Action.RIGHT_CLICK_AIR == action || Action.RIGHT_CLICK_BLOCK == action) && e.getPlayer().isSneaking()) {
            if (!SItem.isAbleToDoEtherWarpTeleportation(player, sItem)) {
                Ability ability = sItem.getType().getAbility();
                if (null != ability) {
                    AbilityActivation activation = ability.getAbilityActivation();
                    if (AbilityActivation.LEFT_CLICK == activation || AbilityActivation.RIGHT_CLICK == activation) {
                        if (AbilityActivation.LEFT_CLICK == activation) {
                            if (Action.LEFT_CLICK_AIR != action) {
                                if (Action.LEFT_CLICK_BLOCK != action) {
                                    return;
                                }
                            }
                        } else if (Action.RIGHT_CLICK_AIR != action && Action.RIGHT_CLICK_BLOCK != action) {
                            return;
                        }
                        PlayerUtils.useAbility(player, sItem);
                    }
                }
                return;
            }
            int mana = Repeater.MANA_MAP.get(uuid);
            int cost = PlayerUtils.getFinalManaCost(player, sItem, 250);
            int resMana = mana - cost;
            if (0 <= resMana) {
                Repeater.MANA_MAP.remove(uuid);
                Repeater.MANA_MAP.put(uuid, resMana);
                long c = System.currentTimeMillis();
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
                long c = System.currentTimeMillis();
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
    public void onPlayerInteracting(PlayerInteractEvent e) {
    }

    @EventHandler
    public void PotionsSplash(PotionSplashEvent e) {
        for (Entity ef : e.getAffectedEntities()) {
            if (ef.hasMetadata("LD")) {
                for (PotionEffect pe : e.getEntity().getEffects()) {
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
            for (Entity ef : e.getAffectedEntities()) {
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
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (Action.RIGHT_CLICK_AIR != e.getAction()) {
            for (Player p : e.getPlayer().getWorld().getPlayers()) {
                if (p == e.getPlayer()) {
                    continue;
                }
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) e.getPlayer()).getHandle(), 0));
            }
        }
        if (!SItem.isSpecItem(e.getItem())) {
            return;
        }
        SItem sItem = SItem.find(e.getItem());
        if (null == sItem) {
            return;
        }
        if (SMaterial.HIDDEN_SOUL_WHIP == sItem.getType()) {
            e.setCancelled(true);
        }
        if ((Material.MONSTER_EGG == sItem.getStack().getType() || Material.MONSTER_EGGS == sItem.getStack().getType()) && !e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
        if (SMaterial.HIDDEN_GYRO_EYE == sItem.getType() || SMaterial.HIDDEN_VOID_FRAGMENT == sItem.getType()) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
        Action action = e.getAction();
        if (SpecificItemType.HELMET == sItem.getType().getStatistics().getSpecificType() && Action.RIGHT_CLICK_AIR == action && isAir(e.getPlayer().getInventory().getHelmet())) {
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
        }
        Player player = e.getPlayer();
        Ability ability = sItem.getType().getAbility();
        Label_0409:
        {
            if (null != ability) {
                AbilityActivation activation = ability.getAbilityActivation();
                if (AbilityActivation.LEFT_CLICK == activation || AbilityActivation.RIGHT_CLICK == activation) {
                    if (AbilityActivation.LEFT_CLICK == activation) {
                        if (Action.LEFT_CLICK_AIR != action) {
                            if (Action.LEFT_CLICK_BLOCK != action) {
                                break Label_0409;
                            }
                        }
                    } else if (Action.RIGHT_CLICK_AIR != action && Action.RIGHT_CLICK_BLOCK != action) {
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
        MaterialFunction function = sItem.getType().getFunction();
        if (null != function) {
            function.onInteraction(e);
        }
    }

    @EventHandler
    public void onPlayerMage(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        if (!player.getWorld().getName().equals("dungeon")) {
            return;
        }
        if (!Classes.containsKey(player)) {
            Classes.put(player, "M");
        }
        if ("M" == ItemListener.Classes.get(player) && Action.LEFT_CLICK_AIR == action) {
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
                Location blockLocation = player.getTargetBlock((Set) null, 30).getLocation();
                Location crystalLocation = player.getEyeLocation();
                Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
                final double count = 25.0;
                for (int i = 1; (int) count >= i; ++i) {
                    for (Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.5, 0.0, 0.5)) {
                        if ("false" == ACT) {
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
                        User user = User.getUser(player.getUniqueId());
                        final int damage = 0;
                        double enchantBonus = 0.0;
                        final double potionBonus = 0.0;
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
                        PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics) material.getStatistics();
                        double baseDamage = (5 + playerBoostStatistics.getBaseDamage() + statistics2.getStrength().addAll() / 5.0) * (1.0 + statistics2.getStrength().addAll() / 100.0);
                        int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                        final double weaponBonus = 0.0;
                        final double armorBonus = 1.0;
                        int critChanceMul = (int) (statistics2.getCritChance().addAll() * 100.0);
                        int chance = SUtil.random(0, 100);
                        if (chance > critChanceMul) {
                            critDamage = 0.0;
                        }
                        double damageMultiplier = 1.0 + combatLevel * 0.04 + enchantBonus + weaponBonus;
                        double finalCritDamage = critDamage;
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
                        ArmorStand stand3 = (ArmorStand) new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND).getEntity();
                        if (0.0 == finalCritDamage) {
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
                        }.runTaskLater(SkyBlock.getPlugin(), 30L);
                        ACT = "false";
                    }
                    player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.FIREWORKS_SPARK, 24, 1, 0.0f, 0.0f, 0.0f, 1.0f, 0, 64);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getPlayer();
        Inventory storage = Storage.getCurrentStorageOpened(player);
        if (null == storage) {
            return;
        }
        Inventory inventory = e.getInventory();
        SItem hand = SItem.find(player.getItemInHand());
        if (null == hand) {
            return;
        }
        NBTTagCompound storageData = new NBTTagCompound();
        for (int i = 0; i < inventory.getSize(); ++i) {
            SItem sItem = SItem.find(inventory.getItem(i));
            if (null == sItem) {
                SItem equiv = SItem.of(inventory.getItem(i));
                if (null != equiv) {
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
    public void onPlayerFlight(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (GameMode.CREATIVE == gameMode || GameMode.SPECTATOR == gameMode) {
            return;
        }
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            SItem sItem = SItem.find(stack);
            if (null != sItem) {
                Ability ability = sItem.getType().getAbility();
                if (null != ability && e.isFlying() && AbilityActivation.FLIGHT == ability.getAbilityActivation()) {
                    e.setCancelled(true);
                    PlayerUtils.useAbility(player, sItem);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        GameMode gameMode = player.getGameMode();
        for (ItemStack stack : player.getInventory().getArmorContents()) {
            SItem sItem = SItem.find(stack);
            if (null != sItem) {
                Ability ability = sItem.getType().getAbility();
                if (null != ability && player.isSneaking() && AbilityActivation.SNEAK == ability.getAbilityActivation()) {
                    PlayerUtils.useAbility(player, sItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (InventoryType.CRAFTING != e.getView().getTopInventory().getType()) {
            return;
        }
        if (InventoryType.SlotType.CONTAINER != e.getSlotType() && InventoryType.SlotType.QUICKBAR != e.getSlotType()) {
            return;
        }
        if (InventoryAction.MOVE_TO_OTHER_INVENTORY != e.getAction()) {
            return;
        }
        Inventory inventory = e.getClickedInventory();
        if (null == inventory) {
            return;
        }
        if (InventoryType.PLAYER != inventory.getType()) {
            return;
        }
        ItemStack current = e.getCurrentItem();
        if (null == current) {
            return;
        }
        SItem sItem = SItem.find(current);
        if (null == sItem) {
            sItem = SItem.of(current);
        }
        updateStatistics((Player) e.getWhoClicked());
        if (null == sItem.getType().getStatistics().getSpecificType() || SpecificItemType.HELMET != sItem.getType().getStatistics().getSpecificType()) {
            return;
        }
        PlayerInventory playerInventory = (PlayerInventory) inventory;
        if (!isAir(playerInventory.getHelmet())) {
            return;
        }
        e.setCancelled(true);
        e.setCurrentItem(new ItemStack(Material.AIR));
        playerInventory.setHelmet(current);
    }

    @EventHandler
    public void onArmorChange(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (null == e.getClickedInventory()) {
            return;
        }
        if (InventoryType.PLAYER != e.getClickedInventory().getType() && InventoryType.CRAFTING != e.getClickedInventory().getType()) {
            return;
        }
        updateStatistics((Player) e.getWhoClicked());
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
    }

    @EventHandler
    public void onArmorChange1(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        player.getInventory().setHelmet(player.getInventory().getHelmet());
        player.getInventory().setChestplate(player.getInventory().getChestplate());
        player.getInventory().setLeggings(player.getInventory().getLeggings());
        player.getInventory().setBoots(player.getInventory().getBoots());
        updateStatistics(player);
    }

    @EventHandler
    public void onArmorChange2(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        final ItemStack helmet = player.getInventory().getHelmet();
        SUtil.delay(() -> player.getInventory().setHelmet(helmet), 10L);
        final SItem helmet1 = SItem.find(player.getInventory().getHelmet());
        if (null == helmet1) {
            return;
        }
        final TickingMaterial tickingMaterial = helmet1.getType().getTickingInstance();
        if (null != tickingMaterial) {
            statistics.tickItem(39, tickingMaterial.getInterval(), () -> tickingMaterial.tick(helmet1, Bukkit.getPlayer(statistics.getUuid())));
        }
        updateStatistics(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClick(InventoryClickEvent e) {
        ItemStack stack = e.getCurrentItem();
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
        sItem.getType().getFunction().onInventoryClick(sItem, e);
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent e) {
        if (null == e.getClickedInventory()) {
            return;
        }
        if (InventoryType.PLAYER != e.getClickedInventory().getType()) {
            return;
        }
        if (8 != e.getSlot()) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        SItem sItem = SItem.find(e.getItemInHand());
        if (null == sItem) {
            return;
        }
        if (SpecificItemType.HELMET == sItem.getType().getStatistics().getSpecificType() && isAir(e.getPlayer().getInventory().getHelmet())) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
            return;
        }
        if (!sItem.getType().isCraft()) {
            if (GenericItemType.BLOCK != sItem.getType().getStatistics().getType()) {
                e.setCancelled(true);
            } else {
                new SBlock(e.getBlockPlaced().getLocation(), sItem.getType(), sItem.getData()).save();
            }
        }
    }

    @EventHandler
    public void onFrameInteract(PlayerInteractEvent e) {
        if (Action.RIGHT_CLICK_BLOCK != e.getAction()) {
            return;
        }
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack hand = e.getItem();
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
            e.setCancelled(true);
            return;
        }
        if (SMaterial.SUMMONING_FRAME != sBlock.getType()) {
            e.setCancelled(true);
            return;
        }
        if (!block.hasMetadata("placer")) {
            if (SMaterial.SUMMONING_EYE != item.getType()) {
                return;
            }
            block.setMetadata("placer", new FixedMetadataValue(this.plugin, player.getUniqueId()));
            BlockState state = block.getState();
            state.setRawData((byte) 4);
            state.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SLEEPING_EYE).getStack());
            List<Location> locations = StaticDragonManager.EYES.containsKey(player.getUniqueId()) ? StaticDragonManager.EYES.get(player.getUniqueId()) : new ArrayList<Location>();
            locations.add(block.getLocation());
            StaticDragonManager.EYES.remove(player.getUniqueId());
            StaticDragonManager.EYES.put(player.getUniqueId(), locations);
            int quantity = 0;
            for (List<Location> ls : StaticDragonManager.EYES.values()) {
                quantity += ls.size();
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().getName().equals("dragon")) {
                    p.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.GREEN + player.getName() + ChatColor.LIGHT_PURPLE + " placed a Summoning Eye! " + ((8 == quantity) ? "Brace yourselves! " : "") + ChatColor.GRAY + "(" + ((8 == quantity) ? ChatColor.GREEN : ChatColor.YELLOW) + quantity + ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
                }
            }
            if (8 != quantity) {
                return;
            }
            List<UUID> cleared = new ArrayList<UUID>();
            for (List<Location> ls2 : StaticDragonManager.EYES.values()) {
                for (Location location : ls2) {
                    Block b = location.getBlock();
                    List<MetadataValue> values = b.getMetadata("placer");
                    Player p2 = Bukkit.getPlayer((UUID) values.get(0).value());
                    if (null == p2) {
                        continue;
                    }
                    if (cleared.contains(p2.getUniqueId())) {
                        continue;
                    }
                    PlayerInventory inventory = p2.getInventory();
                    for (int i = 0; i < inventory.getSize(); ++i) {
                        SItem si = SItem.find(inventory.getItem(i));
                        if (null != si) {
                            if (SMaterial.SLEEPING_EYE == si.getType()) {
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
                    for (int i = 0; 3 > i; ++i) {
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
                    SEntity entity = new SEntity(block.getLocation().clone().add(0.0, 53.0, 0.0), dragonType);
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
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getWorld().getName().equals("dragon")) {
                            continue;
                        }
                        Vector vector = p.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
                        p.setVelocity(vector.normalize().multiply(40.0).setY(100.0));
                    }
                    StaticDragonManager.DRAGON = entity;
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_GROWL, 50.0f, 1.0f);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getWorld().getName().equals("dragon")) {
                            p.sendMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "The " + ChatColor.RED + ChatColor.BOLD + entity.getStatistics().getEntityName() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " has spawned!");
                        }
                    }
                }
            }.runTaskLater(this.plugin, 180L);
        } else {
            List<MetadataValue> values2 = block.getMetadata("placer");
            Player p3 = Bukkit.getPlayer((UUID) values2.get(0).value());
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
            block.removeMetadata("placer", this.plugin);
            BlockState state2 = block.getState();
            state2.setRawData((byte) 0);
            state2.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SUMMONING_EYE).getStack());
            StaticDragonManager.EYES.get(p3.getUniqueId()).remove(block.getLocation());
            player.sendMessage(ChatColor.DARK_PURPLE + "You recovered a Summoning Eye!");
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        Item item = e.getItem();
        Player player = e.getPlayer();
        updateStatistics(player);
        NBTTagCompound compound = CraftItemStack.asNMSCopy(item.getItemStack()).getTag();
        if (null == compound) {
            compound = new NBTTagCompound();
        }
        if (!compound.hasKey("type")) {
            item.getItemStack().setItemMeta(SItem.of(item.getItemStack()).getStack().getItemMeta());
        }
        if (item.hasMetadata("owner")) {
            List<MetadataValue> o = item.getMetadata("owner");
            if (0 != o.size() && !o.get(0).asString().equals(e.getPlayer().getUniqueId().toString())) {
                e.setCancelled(true);
                return;
            }
        }
        User user = User.getUser(player.getUniqueId());
        ItemStack stack = item.getItemStack();
        SItem sItem = SItem.find(stack);
        if (null == sItem) {
            throw new NullPointerException("Something messed up! Check again");
        }
        if (item.hasMetadata("obtained")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getWorld().getName().equals("dragon")) {
                    if (!sItem.getFullName().equals("§6Ender Dragon") && !sItem.getFullName().equals("§5Ender Dragon")) {
                        p.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + sItem.getFullName() + ChatColor.YELLOW + "!");
                    } else {
                        p.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName() + ChatColor.YELLOW + "!");
                    }
                }
            }
        }
        if (ItemOrigin.NATURAL_BLOCK == sItem.getOrigin() || ItemOrigin.MOB == sItem.getOrigin()) {
            sItem.setOrigin(ItemOrigin.UNKNOWN);
            ItemCollection collection = ItemCollection.getByMaterial(sItem.getType(), stack.getDurability());
            if (null != collection) {
                int prev = user.getCollection(collection);
                user.addToCollection(collection, stack.getAmount());
                user.save();
                if (0 == prev) {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  COLLECTION UNLOCKED " + ChatColor.RESET + ChatColor.YELLOW + collection.getName());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 2.0f);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
    }

    @EventHandler
    public void onItemMove(PlayerDropItemEvent e) {
        SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e.setCancelled(true);
        }
        updateStatistics(e.getPlayer());
    }

    @EventHandler
    public void onItemDrop1(PlayerDropItemEvent e) {
        SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (null != sItem && SMaterial.BONEMERANG == sItem.getType() && e.getItemDrop().getItemStack().toString().contains("GHAST_TEAR")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove1(PlayerDropItemEvent e) {
        SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (null != sItem && (SMaterial.SKYBLOCK_MENU == sItem.getType() || SMaterial.QUIVER_ARROW == sItem.getType())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFishingRodReel(PlayerFishEvent e) {
        SItem rod = SItem.find(e.getPlayer().getItemInHand());
        if (null == rod) {
            return;
        }
        e.getHook().setMetadata("owner", new FixedMetadataValue(SkyBlock.getPlugin(), e.getPlayer()));
        MaterialFunction function = rod.getType().getFunction();
        if (null == function) {
            return;
        }
        if (function instanceof FishingRodFunction) {
            ((FishingRodFunction) function).onFish(rod, e);
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        SItem item = SItem.find(e.getPotion().getItem());
        if (null == item) {
            return;
        }
        if (!item.isPotion()) {
            return;
        }
        e.setCancelled(true);
        for (LivingEntity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player)) {
                continue;
            }
            User user = User.getUser(entity.getUniqueId());
            if (null == user) {
                continue;
            }
            for (in.godspunky.skyblock.features.potion.PotionEffect effect : item.getPotionEffects()) {
                PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
                if (null != effect.getType().getOnDrink()) {
                    effect.getType().getOnDrink().accept(effect, (Player) entity);
                }
                long ticks = (long) (effect.getDuration() * e.getIntensity(entity));
                if (!user.hasPotionEffect(effect.getType()) || (user.hasPotionEffect(effect.getType()) && ticks > user.getPotionEffect(effect.getType()).getRemaining())) {
                    user.removePotionEffect(effect.getType());
                    user.addPotionEffect(new in.godspunky.skyblock.features.potion.PotionEffect(effect.getType(), effect.getLevel(), ticks));
                }
                entity.sendMessage((effect.getType().isBuff() ? (ChatColor.GREEN + "" + ChatColor.BOLD + "BUFF!") : (ChatColor.RED + "" + ChatColor.BOLD + "DEBUFF!")) + ChatColor.RESET + ChatColor.WHITE + " You " + (e.getPotion().getShooter().equals(entity) ? "splashed yourself" : "were splashed") + " with " + effect.getDisplayName() + ChatColor.WHITE + "!");
            }
        }
    }

    public static void updateStatistics(Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack beforeHelmet = inv.getHelmet();
        ItemStack beforeChestplate = inv.getChestplate();
        ItemStack beforeLeggings = inv.getLeggings();
        ItemStack beforeBoots = inv.getBoots();
        new BukkitRunnable() {
            public void run() {
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                ItemStack afterHelmet = inv.getHelmet();
                ItemStack afterChestplate = inv.getChestplate();
                ItemStack afterLeggings = inv.getLeggings();
                ItemStack afterBoots = inv.getBoots();
                boolean helmetSimilar = similar(beforeHelmet, afterHelmet);
                boolean chestplateSimilar = similar(beforeChestplate, afterChestplate);
                boolean leggingsSimilar = similar(beforeLeggings, afterLeggings);
                boolean bootsSimilar = similar(beforeBoots, afterBoots);
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
                checkCondition(player);
            }
        }.runTaskLater(SkyBlock.getPlugin(), 1L);
    }

    public static void checkCondition(Player p) {
        SItem helm = SItem.find(p.getInventory().getHelmet());
        SItem chest = SItem.find(p.getInventory().getChestplate());
        SItem leg = SItem.find(p.getInventory().getLeggings());
        SItem boots = SItem.find(p.getInventory().getBoots());
        if (null != helm && null != chest && null != leg && null != boots) {
            if (Groups.WITHER_HELMETS.contains(helm.getType()) && Groups.WITHER_CHESTPLATES.contains(chest.getType()) && Groups.WITHER_LEGGINGS.contains(leg.getType()) && Groups.WITHER_BOOTS.contains(boots.getType())) {
                if (Witherborn.WITHER_COOLDOWN.containsKey(p.getUniqueId())) {
                    if (!Witherborn.WITHER_COOLDOWN.get(p.getUniqueId()) && !Witherborn.WITHER_MAP.containsKey(p.getUniqueId())) {
                        Witherborn w = new Witherborn(p);
                        w.spawnWither();
                    }
                } else if (!Witherborn.WITHER_MAP.containsKey(p.getUniqueId())) {
                    Witherborn w = new Witherborn(p);
                    w.spawnWither();
                }
            } else Witherborn.WITHER_MAP.remove(p.getUniqueId());
        } else Witherborn.WITHER_MAP.remove(p.getUniqueId());
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
        PlayerUtils.updateArmorStatistics(helmet = SItem.find(afterHelmet), statistics, 0);
        PlayerUtils.updateArmorStatistics(chestplate = SItem.find(afterChestplate), statistics, 1);
        PlayerUtils.updateArmorStatistics(leggings = SItem.find(afterLeggings), statistics, 2);
        PlayerUtils.updateArmorStatistics(boots = SItem.find(afterBoots), statistics, 3);
        SUtil.delay(() -> PlayerUtils.updateInventoryStatistics(player, statistics), 1L);
    }

    private static boolean similar(ItemStack is, ItemStack is1) {
        return (null == is && null == is1) || ((null == is || null != is1) && null != is && is.isSimilar(is1));
    }

    private static boolean isAir(ItemStack is) {
        return null == is || Material.AIR == is.getType();
    }

    @EventHandler
    public void aAaB(ProjectileHitEvent enn) {
        Entity e = enn.getEntity();
        if (!(e instanceof Arrow)) {
            return;
        }
        if (!(((Arrow) e).getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) ((Arrow) e).getShooter();
        boolean ACT = true;
        if (e.isOnGround()) {
            return;
        }
        for (Entity enderman_1 : e.getWorld().getNearbyEntities(e.getLocation(), 1.5, 1.5, 1.5)) {
            if (enderman_1 instanceof Enderman && ACT && !e.isOnGround() && !enderman_1.isDead()) {
                if (null == SItem.find(player.getItemInHand())) {
                    e.remove();
                    break;
                }
                if (SMaterial.TERMINATOR != SItem.find(player.getItemInHand()).getType() && SMaterial.JUJU_SHORTBOW != SItem.find(player.getItemInHand()).getType()) {
                    e.remove();
                    return;
                }
                ACT = false;
                EntityDamageByEntityEvent bl = new EntityDamageByEntityEvent(e, enderman_1, EntityDamageEvent.DamageCause.CUSTOM, 1);
                Bukkit.getPluginManager().callEvent(bl);
                ((LivingEntity) enderman_1).setHealth(((LivingEntity) enderman_1).getHealth() - Math.min(((LivingEntity) enderman_1).getHealth(), bl.getDamage()));
                e.remove();
            }
        }
    }

    @EventHandler
    public void changeBlock(EntityChangeBlockEvent event) {
        Entity fallingBlock = event.getEntity();
        if (EntityType.FALLING_BLOCK == event.getEntityType() && fallingBlock.hasMetadata("t")) {
            Block block = event.getBlock();
            block.setType(Material.AIR);
            event.setCancelled(true);
            List<Entity> entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (Entity e : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            List<Entity> fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (EntityType.FALLING_BLOCK == entity.getType() && entity.hasMetadata("t")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    Player p = (Player) entity;
                    JollyPinkGiant.damagePlayer(p);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity) entity).damage(0.0);
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
            List<Entity> entityList = fallingBlock.getNearbyEntities(3.0, 3.0, 3.0);
            fallingBlock.getWorld().playSound(fallingBlock.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
            fallingBlock.getWorld().playEffect(fallingBlock.getLocation(), Effect.EXPLOSION_HUGE, 0);
            for (Entity e : fallingBlock.getNearbyEntities(7.0, 7.0, 7.0)) {
                if (e instanceof Item) {
                    e.remove();
                }
            }
            fallingBlock.setVelocity(new Vector(0, 0, 0));
            List<Entity> fallingBlockList = fallingBlock.getNearbyEntities(7.0, 7.0, 7.0);
            fallingBlockList.forEach(entity -> {
                if (EntityType.FALLING_BLOCK == entity.getType() && entity.hasMetadata("f")) {
                    entity.remove();
                }
            });
            entityList.forEach(entity -> {
                if (entity instanceof Player) {
                    Player p = (Player) entity;
                    SadanGiant.damagePlayer(p);
                } else if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                    ((LivingEntity) entity).damage(0.0);
                }
            });
            fallingBlock.remove();
        }
    }

    @EventHandler
    public void onentityded(EntityDeathEvent e) {
        if (e.getEntity().getWorld().getName().contains("f6")) {
            e.setDroppedExp(0);
        }
    }

    static {
        Classes = new HashMap<Player, String>();
        IsDead = new HashMap<Player, Boolean>();
    }
}
