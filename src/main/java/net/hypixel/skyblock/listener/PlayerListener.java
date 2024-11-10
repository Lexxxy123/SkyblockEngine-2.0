/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.BlockPosition
 *  net.minecraft.server.v1_8_R3.Blocks
 *  net.minecraft.server.v1_8_R3.ChatComponentText
 *  net.minecraft.server.v1_8_R3.EntityArmorStand
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.FishHook
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDamageEvent$DamageModifier
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.EntityTeleportEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerArmorStandManipulateEvent
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerLoginEvent
 *  org.bukkit.event.player.PlayerLoginEvent$Result
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.world.ChunkLoadEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.listener;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.beam.Beam;
import net.hypixel.skyblock.api.hologram.HologramManager;
import net.hypixel.skyblock.command.RebootServerCommand;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.dungeons.watcher.Watcher;
import net.hypixel.skyblock.entity.nms.VoidgloomSeraph;
import net.hypixel.skyblock.features.dungeons.blessing.BlessingChest;
import net.hypixel.skyblock.features.dungeons.blessing.Blessings;
import net.hypixel.skyblock.features.dungeons.chest.ItemChest;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.gui.PetsGUI;
import net.hypixel.skyblock.gui.ProfileViewerGUI;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.SBlock;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.accessory.AccessoryFunction;
import net.hypixel.skyblock.item.armor.PrecursorEye;
import net.hypixel.skyblock.item.armor.VoidlingsWardenHelmet;
import net.hypixel.skyblock.item.bow.BowFunction;
import net.hypixel.skyblock.item.pet.Pet;
import net.hypixel.skyblock.item.pet.PetAbility;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.nms.packetevents.PacketReader;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.TemporaryStats;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.user.UserStash;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.FerocityCalculation;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.hypixel.skyblock.util.SputnikPlayer;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerListener
extends PListener {
    private static final Map<UUID, BowShooting> BOW_MAP = new HashMap<UUID, BowShooting>();
    private static final Map<UUID, CombatAction> COMBAT_MAP = new HashMap<UUID, CombatAction>();
    private final Map<UUID, Boolean> isNotLoaded = new HashMap<UUID, Boolean>();
    public static final Map<Player, Double> LAST_DAMAGE_DEALT = new HashMap<Player, Double>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        if (to == null || from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            SBlock block = SBlock.getBlock(player.getLocation().clone().subtract(0.0, 0.3, 0.0));
            if (player.getGameMode() != GameMode.SPECTATOR && e.getTo().getY() <= -25.0) {
                User.getUser(player.getUniqueId()).kill(EntityDamageEvent.DamageCause.VOID, null);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        e.setJoinMessage(null);
        this.getIsNotLoaded().put(player.getUniqueId(), true);
        SUtil.delay(() -> {
            PlayerUtils.USER_SESSION_ID.put(player.getUniqueId(), UUID.randomUUID());
            PlayerUtils.COOKIE_DURATION_CACHE.remove(player.getUniqueId());
            PlayerUtils.AUTO_SLAYER.remove(player.getUniqueId());
            PetsGUI.PET_SHOWN.remove(player.getUniqueId());
            User.getHash().remove(player.getUniqueId());
            PrecursorEye.PrecursorLaser.put(player.getUniqueId(), false);
            User user = User.getUser(player.getUniqueId());
            user.load();
            if (!PlayerUtils.STATISTICS_CACHE.containsKey(player.getUniqueId())) {
                PlayerUtils.STATISTICS_CACHE.put(player.getUniqueId(), PlayerUtils.getStatistics(player));
            }
            for (Skill skill : Skill.getSkills()) {
                skill.onSkillUpdate(user, user.getSkillXP(skill));
            }
            user.loadStatic();
            SLog.info("player uuid on join : " + player.getUniqueId());
            SputnikPlayer.BonemerangFix(player);
            SputnikPlayer.KatanasFix(player);
            SlayerQuest quest = user.getSlayerQuest();
            if (quest != null) {
                quest.setLastKilled(null);
            }
            final int[] counters = new int[]{0, 0};
            final ArrayList<AtomicInteger> counters2 = new ArrayList<AtomicInteger>(5);
            counters2.add(new AtomicInteger());
            new BukkitRunnable(){

                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }
                    UserStash.getStash(player.getUniqueId()).sendNotificationMessage();
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 600L, 550L);
            new BukkitRunnable(){

                public void run() {
                    ItemStack helm_vanilla;
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }
                    if (player.getWorld().getName().contains("arena") && !player.isOp() && player.isFlying()) {
                        player.setFlying(false);
                    }
                    if ((helm_vanilla = player.getInventory().getHelmet()) == null) {
                        return;
                    }
                    SItem helm = SItem.find(helm_vanilla);
                    if (helm != null && helm.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
                        ItemStack smStack = helm.getStack();
                        SkullMeta meta = (SkullMeta)smStack.getItemMeta();
                        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                        byte[] ed = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", VoidlingsWardenHelmet.getTexture()).getBytes());
                        profile.getProperties().put((Object)"textures", (Object)new Property("textures", new String(ed)));
                        try {
                            Field f = meta.getClass().getDeclaredField("profile");
                            f.setAccessible(true);
                            f.set(meta, profile);
                        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException exception) {
                            // empty catch block
                        }
                        smStack.setItemMeta((ItemMeta)meta);
                        player.getInventory().setHelmet(smStack);
                    }
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
            new BukkitRunnable(){

                public void run() {
                    User u;
                    if (!player.isOnline()) {
                        if (TemporaryStats.getFromPlayer(player) != null) {
                            TemporaryStats.getFromPlayer(player).cleanUp();
                        }
                        this.cancel();
                        return;
                    }
                    SUtil.runSync(() -> Repeater.runPlayerTask(player, counters, counters2));
                    if (TemporaryStats.getFromPlayer(player) != null) {
                        TemporaryStats.getFromPlayer(player).update();
                    }
                    if (player.getWorld().getName().contains("arena") && (u = User.getUser(player.getUniqueId())) != null) {
                        if (u.hasPotionEffect(net.hypixel.skyblock.features.potion.PotionEffectType.JUMP_BOOST) || u.hasPotionEffect(net.hypixel.skyblock.features.potion.PotionEffectType.RABBIT)) {
                            u.removePotionEffect(net.hypixel.skyblock.features.potion.PotionEffectType.JUMP_BOOST);
                            u.removePotionEffect(net.hypixel.skyblock.features.potion.PotionEffectType.RABBIT);
                            u.toBukkitPlayer().removePotionEffect(PotionEffectType.JUMP);
                            u.send("&cYour Jump Boost potion effect has been taken away by an unknown magicial force!");
                        }
                        if (u.toBukkitPlayer().hasPotionEffect(PotionEffectType.JUMP)) {
                            u.toBukkitPlayer().removePotionEffect(PotionEffectType.JUMP);
                        }
                    }
                    counters[0] = counters[0] + 1;
                    counters[1] = counters[1] + 1;
                    if (counters[0] == 3) {
                        counters[0] = 1;
                    }
                    if (counters[1] == 5) {
                        counters[1] = 1;
                    }
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L);
            Pet.PetItem pet = User.getUser(player.getUniqueId()).getActivePet();
            Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
            if (pet != null && petclass != null) {
                PetsGUI.spawnFlyingHeads(player, petclass, pet.toItem().getStack());
            }
            SUtil.delay(() -> player.setHealth(player.getMaxHealth()), 1L);
            SUtil.delay(() -> this.getIsNotLoaded().remove(player.getUniqueId()), 20L);
            SUtil.delay(() -> UserStash.getStash(player.getUniqueId()).sendNotificationMessage(), 30L);
        }, 3L);
        SUtil.delay(() -> {
            Location l = new Location(Bukkit.getWorld((String)"world"), -2.5, 70.0, -68.5, 180.0f, 0.0f);
            player.teleport(l);
        }, 1L);
        player.sendMessage(Sputnik.trans("&eWelcome to &aFunpixel SkyBlock"));
        player.sendMessage(Sputnik.trans("&ePlease report bugs at https://discord.gg/funpixel"));
        new BukkitRunnable(){

            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }
                User user = User.getUser(player.getUniqueId());
                try {
                    if (user != null) {
                        PlayerRank rank = user.rank;
                        player.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)rank.getPrefix()) + " " + player.getName());
                        player.setPlayerListName(ChatColor.translateAlternateColorCodes((char)'&', (String)rank.getPrefix()) + " " + player.getName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean hasActiveEffects = user.getEffects().size() > 0;
                boolean hasActiveBuff = PlayerUtils.cookieBuffActive(player);
                String cookieDuration = PlayerUtils.getCookieDurationDisplayGUI(player);
                ChatComponentText header = new ChatComponentText(ChatColor.AQUA + "You are" + ChatColor.RESET + " " + ChatColor.AQUA + "playing on " + ChatColor.YELLOW + "" + ChatColor.BOLD + "MC.FUNPIXEL.XYZ\n");
                ChatComponentText footer = new ChatComponentText("\n" + ChatColor.GREEN + "" + ChatColor.BOLD + "Active Effects\n" + (hasActiveEffects ? ChatColor.GRAY + "You have " + ChatColor.YELLOW + user.getEffects().size() + ChatColor.GRAY + " active effects. Use\n" + ChatColor.GRAY + "\"" + ChatColor.GOLD + "/effects" + ChatColor.GRAY + "\" to see them!\n\n" : ChatColor.GRAY + "No effects active. Drink potions or splash\n" + ChatColor.GRAY + "them on the ground to buff yourself!\n\n") + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Cookie Buff\n" + (hasActiveBuff ? ChatColor.WHITE + cookieDuration + "\n" : ChatColor.GRAY + "Not active! Obtain booster cookies from the\ncommunity shop in the hub") + "\n\n" + ChatColor.GREEN + "Ranks, Boosters, & MORE!" + ChatColor.RESET + " " + ChatColor.RED + "" + ChatColor.BOLD + "STORE.FUNPIXEL.XYZ");
                PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
                try {
                    Field headerField = packet.getClass().getDeclaredField("a");
                    Field footerField = packet.getClass().getDeclaredField("b");
                    headerField.setAccessible(true);
                    footerField.setAccessible(true);
                    headerField.set(packet, header);
                    footerField.set(packet, footer);
                    headerField.setAccessible(!headerField.isAccessible());
                    footerField.setAccessible(!footerField.isAccessible());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ((CraftPlayer)user.toBukkitPlayer()).getHandle().playerConnection.sendPacket((Packet)packet);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L);
        new PacketReader().injectPlayer(player);
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent e) {
        for (Entity enn : e.getChunk().getEntities()) {
            if (enn instanceof Player || enn.hasMetadata("pets") || enn.hasMetadata("specEntityObject")) continue;
            enn.remove();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SlayerQuest quest = user.getSlayerQuest();
        user.removeAllSlayerBosses();
        if (quest != null && quest.getXp() >= (double)quest.getType().getSpawnXP()) {
            user.failSlayerQuest();
            player.sendMessage("  " + ChatColor.RED + ChatColor.BOLD + "SLAYER QUEST FAILED!");
            player.sendMessage("   " + ChatColor.DARK_PURPLE + ChatColor.BOLD + "\u00bb " + ChatColor.GRAY + "You need to learn how to play this game first!");
        }
        user.save();
        Blessings.STAT_MAP.remove(player.getUniqueId());
        PrecursorEye.PrecursorLaser.remove(player.getUniqueId());
        PlayerUtils.COOKIE_DURATION_CACHE.remove(player.getUniqueId());
        PlayerUtils.AUTO_SLAYER.remove(player.getUniqueId());
        PetsGUI.PET_SHOWN.remove(player.getUniqueId());
        PlayerUtils.COOLDOWN_MAP.remove(player.getUniqueId());
        PlayerUtils.USER_SESSION_ID.remove(player.getUniqueId());
        PlayerUtils.SOUL_EATER_MAP.remove(player.getUniqueId());
        PlayerUtils.LAST_KILLED_MAPPING.remove(player.getUniqueId());
        Repeater.PTN_CACHE.remove(user.getUuid());
        PlayerUtils.STATISTICS_CACHE.remove(user.getUuid());
        user.unload();
    }

    @EventHandler
    public void RegionChange(PlayerMoveEvent e) {
        User player = User.getUser(e.getPlayer().getUniqueId());
        if (player.getRegion() == null) {
            return;
        }
        List<String> discoveredZonesList = player.getdiscoveredzones();
        if (discoveredZonesList.contains(player.getRegion().getType().getName())) {
            return;
        }
        RegionType type = player.getRegion().getType();
        player.addnewzone(player.getRegion().getType().getName());
        if (type == RegionType.VILLAGE) {
            this.onNewZone(player, RegionType.VILLAGE, "Purchase items at the Market.", "Visit the Auction House.", "Manage your Coins in the Bank.", "Enchant items at the Library.");
        } else if (type == RegionType.AUCTION_HOUSE) {
            this.onNewZone(player, RegionType.AUCTION_HOUSE, "Auction off your special items.", "Bid on other player's items.");
        } else if (type == RegionType.BANK) {
            this.onNewZone(player, RegionType.BANK, "Talk to the Banker.", "Store your coins to keep them safe.", "Earn interest on your coins.");
        } else if (type == RegionType.GOLD_MINE) {
            this.onNewZone(player, RegionType.GOLD_MINE, "Talk to the Lazy Miner.", "Find the hidden gold mine.");
        } else if (type == RegionType.COAL_MINE) {
            this.onNewZone(player, RegionType.COAL_MINE, "Mine coal.", "Travel to the Gold Mine.");
        } else if (type == RegionType.FARM) {
            this.onNewZone(player, RegionType.FARM, "Talk to the farmer.", "Travel to The Barn.");
        } else if (type == RegionType.BIRCH_PARK) {
            this.onNewZone(player, RegionType.BIRCH_PARK, "Chop down trees.", "Collect all Log types.");
        } else if (type == RegionType.FOREST) {
            this.onNewZone(player, RegionType.FOREST, "Visit the Lumberjack.", "Chop down trees.", "Travel to the Birch Park.");
        } else if (type == RegionType.GRAVEYARD) {
            this.onNewZone(player, RegionType.GRAVEYARD, "Fight Zombies.", "Travel to the Spider's Den.", "Talk to Pat.", "Investigate the Catacombs.");
        } else if (type == RegionType.BAZAAR_ALLEY) {
            this.onNewZone(player, RegionType.BAZAAR_ALLEY, "Buy and sell materials in bulk in the Bazaar.");
        } else if (type == RegionType.WILDERNESS) {
            this.onNewZone(player, RegionType.WILDERNESS, "Fish.", "Visit the Fisherman's Hut.", "Visit the fairy at the Fairy Pond.", "Discover hidden secrets.");
        } else if (type == RegionType.RUINS) {
            this.onNewZone(player, RegionType.RUINS, "Explore the ancient ruins.", "Watch out for the guard dogs!");
        } else if (type == RegionType.THE_END) {
            this.onNewZone(player, RegionType.THE_END, "Talk to the Pearl Dealer.", "Explore the End Shop.", "Kill Endermen.", "Fight Dragons!");
        }
    }

    public void onNewZone(User p, RegionType r, String ... messages) {
        p.onNewZone(r, messages);
    }

    @EventHandler
    public void onPlayerQuit2(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SputnikPlayer.BonemerangFix(player);
        SputnikPlayer.KatanasFix(player);
    }

    @EventHandler
    public void onMoveWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        SputnikPlayer.BonemerangFix(player);
        SputnikPlayer.KatanasFix(player);
        Pet.PetItem pet = User.getUser(player.getUniqueId()).getActivePet();
        Pet petclass = User.getUser(player.getUniqueId()).getActivePetClass();
        SUtil.delay(() -> UserStash.getStash(player.getUniqueId()).sendNotificationMessage(), 20L);
        if (pet != null && petclass != null) {
            PetsGUI.spawnFlyingHeads(player, petclass, pet.toItem().getStack());
        }
    }

    @EventHandler
    public void onEndermanMove(EntityTeleportEvent e) {
        if (e.getEntityType() == EntityType.ENDERMAN) {
            e.setCancelled(true);
            Location locbf = e.getFrom();
            e.getEntity().teleport(locbf);
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (e instanceof EntityDamageByEntityEvent) {
            return;
        }
        Player player = (Player)e.getEntity();
        User user = User.getUser(player.getUniqueId());
        if (player.getHealth() + (double)SputnikPlayer.getCustomAbsorptionHP(player).intValue() - e.getDamage() <= 0.0) {
            e.setCancelled(true);
            user.kill(e.getCause(), null);
            return;
        }
        user.damage(e.getDamage(), e.getCause(), null);
        e.setDamage(0.0);
    }

    @EventHandler
    public void listen(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().hasMetadata("NPC")) {
            return;
        }
        Player performer = e.getPlayer();
        if (e.getRightClicked() instanceof Player) {
            Player clicked = (Player)e.getRightClicked();
            if (performer.isSneaking()) {
                Sputnik.tradeIntitize(clicked, performer);
            } else if (!performer.getWorld().getName().contains("f6")) {
                new ProfileViewerGUI(clicked).open(performer);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamage_(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            return;
        }
        if (e.getEntity() instanceof ArmorStand) {
            e.setCancelled(true);
            return;
        }
        LivingEntity en = (LivingEntity)e.getEntity();
        if (!(e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK && e.getCause() != EntityDamageEvent.DamageCause.FIRE || en.hasMetadata("SlayerBoss"))) {
            e.setCancelled(true);
            return;
        }
        if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING && !en.hasMetadata("SlayerBoss")) {
            e.setDamage(en.getMaxHealth() * 5.0 / 100.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.DARK_AQUA);
        } else if (!(e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK || en.hasMetadata("SlayerBoss"))) {
            e.setDamage(5.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.GOLD);
        } else if (e.getCause() == EntityDamageEvent.DamageCause.LAVA && !en.hasMetadata("SlayerBoss")) {
            e.setDamage(20.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.GOLD);
        } else if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT && !en.hasMetadata("SlayerBoss")) {
            e.setDamage(5.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.GRAY);
        } else if (e.getCause() == EntityDamageEvent.DamageCause.POISON) {
            e.setDamage(5.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.DARK_GREEN);
        } else if (e.getCause() == EntityDamageEvent.DamageCause.WITHER) {
            e.setDamage(5.0);
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.BLACK);
        } else if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.GRAY);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamage_2(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            return;
        }
        LivingEntity en = (LivingEntity)e.getEntity();
        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (e.getDamager() instanceof Player) {
                return;
            }
            if (e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Player) {
                return;
            }
            PlayerListener.spawnSpecialDamageInd((Entity)en, e.getDamage(), ChatColor.GRAY);
        }
    }

    public boolean checkArrowSource(Entity damager) {
        return !(damager instanceof Arrow) || !(((Arrow)damager).getShooter() instanceof Player);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onTeleport(PlayerTeleportEvent event) {
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Arrow arrow;
        SItem sItem = null;
        ItemStack weapon = null;
        Player player = null;
        Entity damaged = e.getEntity();
        if (!(damaged instanceof Player) && e.getDamager() instanceof Player) {
            Ability ability2;
            SItem sitem;
            ItemStack st;
            PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(e.getDamager().getUniqueId());
            double atkSpeed = Math.min(100L, Math.round(statistics.getAttackSpeed().addAll()));
            if (damaged instanceof LivingEntity) {
                ((LivingEntity)damaged).setNoDamageTicks((int)Math.round(15.0 / (1.0 + atkSpeed / 100.0)));
                ((LivingEntity)damaged).setMaximumNoDamageTicks((int)Math.round(15.0 / (1.0 + atkSpeed / 100.0)));
            }
            if ((st = ((Player)e.getDamager()).getItemInHand()) != null && (sitem = SItem.find(st)) != null && (ability2 = sitem.getType().getAbility()) != null && ability2.requirementsUse((Player)e.getDamager(), sitem)) {
                e.getDamager().sendMessage(Sputnik.trans(ability2.getAbilityReq()));
                e.getEntity().getWorld().spigot().playEffect(e.getEntity().getLocation().clone().add(0.0, 1.5, 0.0).add(e.getEntity().getLocation().getDirection().normalize().multiply(0.2)), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getEntity().getWorld().spigot().playEffect(e.getEntity().getLocation().clone().add(0.0, 1.5, 0.0).add(e.getEntity().getLocation().getDirection().normalize().multiply(0.2)), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getEntity().getWorld().spigot().playEffect(e.getEntity().getLocation().clone().add(0.0, 1.5, 0.0).add(e.getEntity().getLocation().getDirection().normalize().multiply(0.2)), Effect.CRIT, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.setCancelled(true);
                return;
            }
        }
        if (VoidgloomSeraph.HIT_SHIELD.containsKey(damaged)) {
            VoidgloomSeraph.HIT_SHIELD.put(damaged, VoidgloomSeraph.HIT_SHIELD.get(damaged) - 1);
            damaged.getWorld().playSound(damaged.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        }
        if (damaged instanceof ArmorStand) {
            e.setCancelled(true);
            return;
        }
        Entity damager = e.getDamager();
        if (damaged instanceof LivingEntity && damager instanceof FishHook && damager.hasMetadata("owner")) {
            User.getUser(((Player)((MetadataValue)damager.getMetadata("owner").get(0)).value()).getUniqueId()).damageEntity((LivingEntity)damaged);
            return;
        }
        SEntity sEntity = null;
        if (!(damager instanceof Player)) {
            WitherSkull arrow22;
            ProjectileSource shooter;
            Entity in = damager;
            if (in instanceof Arrow && (shooter = (arrow = (Arrow)in).getShooter()) instanceof Entity) {
                in = (Entity)shooter;
            }
            if (in instanceof WitherSkull && (shooter = (arrow22 = (WitherSkull)in).getShooter()) instanceof Entity) {
                in = (Entity)shooter;
            }
            if ((sEntity = SEntity.findSEntity(in)) != null) {
                sEntity.getFunction().onAttack(e);
                e.setDamage(sEntity.getStatistics().getDamageDealt());
                try {
                    e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0);
                } catch (UnsupportedOperationException arrow22) {
                    // empty catch block
                }
            }
        }
        if (damaged instanceof Player) {
            User user;
            if (damaged.hasMetadata("NPC")) {
                return;
            }
            Player damagedPlayer = (Player)damaged;
            if (VoidlingsWardenHelmet.VOIDLING_SHIELD.containsKey(damagedPlayer.getUniqueId()) && User.getUser(damagedPlayer.getUniqueId()).isVoidlingWardenActive() && !(damager instanceof Player) && this.checkArrowSource(damager)) {
                if (VoidlingsWardenHelmet.VOIDLING_SHIELD.get(damagedPlayer.getUniqueId()) - 1 > 0) {
                    e.setDamage(0.0);
                    e.setCancelled(true);
                    damaged.getWorld().playSound(damaged.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
                    VoidlingsWardenHelmet.VOIDLING_SHIELD.put(damagedPlayer.getUniqueId(), VoidlingsWardenHelmet.VOIDLING_SHIELD.get(damagedPlayer.getUniqueId()) - 1);
                } else {
                    User.getUser(damagedPlayer.getUniqueId()).setVoidlingWardenActive(false);
                }
            }
            if (!Sputnik.HaveDMGReduction.containsKey(damagedPlayer.getUniqueId())) {
                Sputnik.HaveDMGReduction.put(damagedPlayer.getUniqueId(), false);
            }
            if (damagedPlayer.hasMetadata("frozen")) {
                Player en = damagedPlayer;
                Vector v = new Vector(0, 0, 0);
                SUtil.delay(() -> PlayerListener.lambda$onEntityDamage$6((Entity)en, v), 0L);
            }
            if ((user = User.getUser(damagedPlayer.getUniqueId())) == null) {
                return;
            }
            PlayerStatistics statistics2 = PlayerUtils.STATISTICS_CACHE.get(damagedPlayer.getUniqueId());
            if (statistics2 == null) {
                return;
            }
            double defense = statistics2.getDefense().addAll();
            double trueDefense = statistics2.getTrueDefense().addAll();
            if (sEntity != null && sEntity.getStatistics().dealsTrueDamage()) {
                e.setDamage(e.getDamage() - e.getDamage() * (trueDefense / (trueDefense + 100.0)));
            } else {
                e.setDamage(e.getDamage() - e.getDamage() * (defense / (defense + 100.0)));
            }
            if (Sputnik.HaveDMGReduction.get(damagedPlayer.getUniqueId()).booleanValue()) {
                e.setDamage(e.getDamage() - e.getDamage() * 10.0 / 100.0);
            }
            if (!(e.getDamager() instanceof Player)) {
                PlayerListener.spawnSpecialDamageInd(damaged, e.getDamage(), ChatColor.GRAY);
                if (SputnikPlayer.getCustomAbsorptionHP(damagedPlayer) > 0) {
                    if (e.getDamage() > (double)SputnikPlayer.getCustomAbsorptionHP(damagedPlayer).intValue()) {
                        e.setDamage(e.getDamage() - (double)SputnikPlayer.getCustomAbsorptionHP(damagedPlayer).intValue());
                        SputnikPlayer.setCustomAbsorptionHP(damagedPlayer, 0.0f);
                    } else {
                        SputnikPlayer.minusCustomAbsorptionHP(damagedPlayer, (float)e.getDamage());
                        e.setDamage(0.0);
                    }
                }
            }
            EntityDamageEvent.DamageCause cause = e.getCause();
            if (damager instanceof Projectile && ((Projectile)damager).getShooter() instanceof Entity) {
                damager = (Entity)((Projectile)damager).getShooter();
                cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            }
            Pet.PetItem item = user.getActivePet();
            Pet pet = user.getActivePetClass();
            if (item != null && pet != null) {
                for (PetAbility ability2 : pet.getPetAbilities(item.toItem())) {
                    ability2.onHurt(e, damager);
                }
            }
            try {
                e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0);
            } catch (UnsupportedOperationException unsupportedOperationException) {
                // empty catch block
            }
            if (damagedPlayer.getHealth() - e.getDamage() <= 0.0) {
                e.setCancelled(true);
                User.getUser(damagedPlayer.getUniqueId()).kill(cause, damager);
            }
            COMBAT_MAP.put(damagedPlayer.getUniqueId(), PlayerListener.createCombatAction(false, e.getDamage(), e.getDamager() instanceof Arrow, System.currentTimeMillis()));
        }
        if (!(damager instanceof Player) && !(damager instanceof Arrow)) {
            return;
        }
        float bowForceReducer = 1.0f;
        if (damager instanceof Arrow) {
            arrow = (Arrow)damager;
            ProjectileSource shooter = arrow.getShooter();
            if (!(shooter instanceof Player)) {
                return;
            }
            player = (Player)shooter;
            if (!BOW_MAP.containsKey(player.getUniqueId())) {
                return;
            }
            BowShooting shooting = BOW_MAP.get(player.getUniqueId());
            weapon = shooting.getBow();
            bowForceReducer = shooting.getForce();
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
        } else if (e.getDamager() instanceof Player) {
            player = (Player)e.getDamager();
            weapon = player.getInventory().getItemInHand();
        } else if (e.getDamager() instanceof CraftArrow) {
            CraftArrow craftArrow = (CraftArrow)e.getDamager();
            ProjectileSource shooter = craftArrow.getShooter();
            if (!(shooter instanceof Player)) {
                return;
            }
            player = (Player)shooter;
            if (!BOW_MAP.containsKey(player.getUniqueId())) {
                return;
            }
            BowShooting shooting = BOW_MAP.get(player.getUniqueId());
            weapon = shooting.getBow();
            bowForceReducer = shooting.getForce();
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
        }
        PlayerUtils.DamageResult result = PlayerUtils.getDamageDealt(weapon, player, damaged, damager instanceof Arrow);
        AtomicDouble finalDamage = new AtomicDouble(result.getFinalDamage() * (double)bowForceReducer);
        e.setDamage(finalDamage.get());
        if (e.getEntity().getType() == EntityType.ENDER_DRAGON) {
            double formula = finalDamage.get() / ((EnderDragon)e.getEntity()).getMaxHealth() * 100.0;
            if (formula < 10.0) {
                e.setDamage(finalDamage.get());
            } else if (formula > 10.0 && formula < 15.0) {
                e.setDamage(finalDamage.get() * 10.0 / 100.0);
            } else if (formula > 15.0 && formula < 20.0) {
                e.setDamage(finalDamage.get() / 100.0);
            } else if (formula > 20.0 && formula <= 25.0) {
                e.setDamage(finalDamage.get() * 0.01 / 100.0);
            } else if (formula > 25.0) {
                e.setDamage(finalDamage.get() * 0.0);
            } else {
                e.setDamage(finalDamage.get());
            }
        }
        if (EntityManager.DEFENSE_PERCENTAGE.containsKey(e.getEntity())) {
            int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(e.getEntity());
            if (defensepercent > 100) {
                defensepercent = 100;
            }
            e.setDamage(finalDamage.get() - finalDamage.get() * (double)defensepercent / 100.0);
        }
        if (e.getEntity().hasMetadata("frozen")) {
            e.setDamage(e.getDamage() + e.getDamage() * 10.0 / 100.0);
        }
        if ((sItem = SItem.find(weapon)) != null) {
            if (sItem.getType().getFunction() != null) {
                sItem.getType().getFunction().onDamage(damaged, player, finalDamage, sItem);
            }
            if (sItem.getType().getFunction() instanceof BowFunction && e.getDamager() instanceof Arrow) {
                ((BowFunction)sItem.getType().getFunction()).onBowHit(damaged, player, (Arrow)e.getDamager(), sItem, finalDamage);
            }
        }
        for (SItem accessory : PlayerUtils.getAccessories(player)) {
            if (!(accessory.getType().getFunction() instanceof AccessoryFunction)) continue;
            ((AccessoryFunction)accessory.getType().getFunction()).onDamageInInventory(sItem, player, damaged, accessory, finalDamage);
        }
        User user = User.getUser(player.getUniqueId());
        Pet pet = user.getActivePetClass();
        if (pet != null) {
            pet.runAbilities(ability -> ability.onDamage(e), user.getActivePet());
        }
        try {
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 0.0);
        } catch (UnsupportedOperationException trueDefense) {
            // empty catch block
        }
        SEntity s = SEntity.findSEntity(damaged);
        if (s != null) {
            s.getFunction().onDamage(s, damager, e, finalDamage);
        }
        if (e.isCancelled()) {
            return;
        }
        LAST_DAMAGE_DEALT.put(player, e.getDamage());
        FerocityCalculation.activeFerocityTimes(player, (LivingEntity)damaged, (int)e.getDamage(), result.didCritDamage());
        PlayerUtils.handleSpecEntity(damaged, player, new AtomicDouble(e.getDamage()));
        COMBAT_MAP.put(player.getUniqueId(), PlayerListener.createCombatAction(true, e.getDamage(), e.getDamager() instanceof Arrow, System.currentTimeMillis()));
        Location l_ = damaged.getLocation().clone();
        PlayerListener.spawnDamageInd(damaged, e.getDamage(), result.didCritDamage());
    }

    public static void eshortBowActive(Player p, Entity damaged, Arrow a) {
        SItem sItem;
        if (p == null) {
            return;
        }
        ItemStack weapon = p.getInventory().getItemInHand();
        PlayerUtils.DamageResult result = PlayerUtils.getDamageDealt(weapon, p, damaged, true);
        AtomicDouble finalDamage_ = new AtomicDouble(result.getFinalDamage());
        double finalDamage = finalDamage_.get();
        Boolean crit = result.didCritDamage();
        LivingEntity le = (LivingEntity)damaged;
        if (EntityManager.DEFENSE_PERCENTAGE.containsKey(damaged)) {
            int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(damaged);
            if (defensepercent > 100) {
                defensepercent = 100;
            }
            finalDamage -= finalDamage * (double)defensepercent / 100.0;
        }
        if ((sItem = SItem.find(weapon)) != null) {
            if (sItem.getType().getFunction() != null) {
                sItem.getType().getFunction().onDamage(damaged, p, finalDamage_, sItem);
            }
            if (sItem.getType().getFunction() instanceof BowFunction && a instanceof Arrow) {
                ((BowFunction)sItem.getType().getFunction()).onBowHit(damaged, p, a, sItem, finalDamage_);
            }
        }
        for (SItem accessory : PlayerUtils.getAccessories(p)) {
            if (!(accessory.getType().getFunction() instanceof AccessoryFunction)) continue;
            ((AccessoryFunction)accessory.getType().getFunction()).onDamageInInventory(sItem, p, damaged, accessory, finalDamage_);
        }
        FerocityCalculation.activeFerocityTimes(p, (LivingEntity)damaged, (int)finalDamage, result.didCritDamage());
        User user = User.getUser(p.getUniqueId());
        user.damageEntityBowEman((Damageable)le, finalDamage, p, a);
        PlayerListener.spawnDamageInd(damaged, finalDamage, crit);
    }

    public static void ferocityActive(int times, final Player p, final double finalDMG, final Entity damaged, final Boolean crit) {
        LivingEntity le;
        if (times > 0) {
            p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 0.4f, 0.0f);
        }
        for (int i = 0; i < times && !(le = (LivingEntity)damaged).isDead(); ++i) {
            if (le.isDead()) {
                return;
            }
            final User user = User.getUser(p.getUniqueId());
            new BukkitRunnable(){

                public void run() {
                    if (damaged.isDead()) {
                        return;
                    }
                    p.playSound(p.getLocation(), Sound.FIRE_IGNITE, 0.4f, 0.0f);
                    SUtil.delay(() -> {
                        Entity val$damaged = damaged;
                        Double val$finalDMG = finalDMG;
                        Player val$p = p;
                        Boolean val$crit = crit;
                        User val$user = user;
                        if (!damaged.isDead()) {
                            double finalDamage = finalDMG;
                            if (EntityManager.DEFENSE_PERCENTAGE.containsKey(damaged)) {
                                int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(damaged);
                                if (defensepercent > 100) {
                                    defensepercent = 100;
                                }
                                finalDamage -= finalDamage * (double)defensepercent / 100.0;
                            }
                            if (VoidgloomSeraph.HIT_SHIELD.containsKey(damaged)) {
                                VoidgloomSeraph.HIT_SHIELD.put(damaged, VoidgloomSeraph.HIT_SHIELD.get(damaged) - 1);
                                damaged.getWorld().playSound(damaged.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
                            }
                            Sputnik.ferocityParticle((LivingEntity)damaged);
                            PlayerListener.spawnDamageInd(damaged, finalDamage, crit);
                            p.playSound(p.getLocation(), Sound.IRONGOLEM_THROW, 1.0f, 1.35f);
                            user.damageEntityIgnoreShield((Damageable)damaged, finalDamage);
                        }
                    }, 10L);
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 4L * (long)i);
        }
    }

    @EventHandler
    public void onBoatPlace(PlayerInteractEvent event) {
        Player player;
        if (!(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK || (player = event.getPlayer()).getItemInHand().getType() != Material.BOAT && player.getItemInHand().getType() != Material.MINECART && player.getItemInHand().getType() != Material.EYE_OF_ENDER || player.isOp())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBowLaunch(PlayerInteractEvent e) {
        final SItem id = SItem.find(e.getPlayer().getItemInHand());
        if (id != null && (id.getType() == SMaterial.TERMINATOR || id.getType() == SMaterial.JUJU_SHORTBOW)) {
            BOW_MAP.put(e.getPlayer().getUniqueId(), new BowShooting(){

                @Override
                public ItemStack getBow() {
                    return id.getStack();
                }

                @Override
                public float getForce() {
                    return 1.0f;
                }
            });
            final User user = User.getUser(e.getPlayer().getUniqueId());
            final Player player = e.getPlayer();
            SItem arrows = SItem.find(player.getInventory().getItem(8));
            if (arrows != null && arrows.getType() == SMaterial.QUIVER_ARROW) {
                final int save = arrows.getStack().getAmount();
                new BukkitRunnable(){

                    public void run() {
                        ItemStack last = player.getInventory().getItem(8);
                        if (last == null) {
                            user.subFromQuiver(SMaterial.ARROW);
                            player.getInventory().setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());
                            return;
                        }
                        if (save == last.getAmount()) {
                            return;
                        }
                        user.subFromQuiver(SMaterial.ARROW);
                        player.getInventory().setItem(8, SUtil.setStackAmount(SItem.of(SMaterial.QUIVER_ARROW).getStack(), Math.min(64, user.getQuiver(SMaterial.ARROW))));
                    }
                }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
            }
        }
    }

    @EventHandler
    public void onBowShoot(final EntityShootBowEvent e) {
        SItem sItem;
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        BOW_MAP.put(e.getEntity().getUniqueId(), new BowShooting(){

            @Override
            public ItemStack getBow() {
                return e.getBow();
            }

            @Override
            public float getForce() {
                return e.getForce();
            }
        });
        final User user = User.getUser(e.getEntity().getUniqueId());
        final Player player = (Player)e.getEntity();
        SItem arrows = SItem.find(player.getInventory().getItem(8));
        if (arrows != null && arrows.getType() == SMaterial.QUIVER_ARROW) {
            final int save = arrows.getStack().getAmount();
            new BukkitRunnable(){

                public void run() {
                    ItemStack last = player.getInventory().getItem(8);
                    if (last == null) {
                        user.subFromQuiver(SMaterial.ARROW);
                        player.getInventory().setItem(8, SItem.of(SMaterial.SKYBLOCK_MENU).getStack());
                        return;
                    }
                    if (save == last.getAmount()) {
                        return;
                    }
                    user.subFromQuiver(SMaterial.ARROW);
                    player.getInventory().setItem(8, SUtil.setStackAmount(SItem.of(SMaterial.QUIVER_ARROW).getStack(), Math.min(64, user.getQuiver(SMaterial.ARROW))));
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
        }
        if ((sItem = SItem.find(e.getBow())) != null) {
            Enchantment aiming = sItem.getEnchantment(EnchantmentType.AIMING);
            SUtil.markAimingArrow((Projectile)e.getProjectile(), aiming);
            if (sItem.getType().getFunction() instanceof BowFunction) {
                ((BowFunction)sItem.getType().getFunction()).onBowShoot(sItem, e);
            }
        }
    }

    @EventHandler
    public void onArmorStandChange(PlayerArmorStandManipulateEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerChangedWorldEvent e) {
        SlayerQuest quest;
        User user = User.getUser(e.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        TemporaryStats ts = null;
        ts = TemporaryStats.getFromPlayer(user.toBukkitPlayer()) != null ? TemporaryStats.getFromPlayer(user.toBukkitPlayer()) : new TemporaryStats(User.getUser(user.toBukkitPlayer().getUniqueId()));
        if (TemporaryStats.getFromPlayer(user.toBukkitPlayer()) != null) {
            ts.cleanStats();
        }
        if ((quest = user.getSlayerQuest()) != null && quest.getXp() >= (double)quest.getType().getSpawnXP() && quest.getKilled() == 0L) {
            User.getUser(e.getPlayer().getUniqueId()).failSlayerQuest();
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player plr = e.getPlayer();
        Set recipients = e.getRecipients();
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld().getName().equals(plr.getWorld().getName())) continue;
            recipients.remove(p);
        }
        if (e.getMessage().toLowerCase().contains("${")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "This message is blocked!");
            e.getPlayer().sendMessage(ChatColor.RED + "Listen kiddo, it wont work anymore.");
        }
    }

    @EventHandler
    public void placeBlockEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        org.bukkit.block.Block b = e.getClickedBlock();
        Map<org.bukkit.block.Block, BlessingChest> map = BlessingChest.CHEST_CACHE;
        if (b == null) {
            return;
        }
        if (b.getType() != Material.CHEST) {
            return;
        }
        if (!map.containsKey(b)) {
            return;
        }
        if (player.isSneaking() && e.getAction() == Action.LEFT_CLICK_BLOCK && player.isOp()) {
            e.setCancelled(true);
            map.get(b).destroy();
            player.sendMessage("removed");
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        BlessingChest be = map.get(b);
        if (!be.isOpened() && !be.isLocked()) {
            Location chestLocation = b.getLocation();
            BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ());
            PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, (Block)Blocks.CHEST, 1, 1);
            chestLocation.getWorld().playSound(chestLocation, Sound.CHEST_OPEN, 1.0f, 1.0f);
            for (Player p : chestLocation.getWorld().getPlayers()) {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
            }
        }
        be.open(player);
    }

    @EventHandler
    public void placeBlockEventTwo(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        org.bukkit.block.Block b = e.getClickedBlock();
        Map<org.bukkit.block.Block, ItemChest> map = ItemChest.ITEM_CHEST_CACHE;
        if (b == null) {
            return;
        }
        if (b.getType() != Material.CHEST) {
            return;
        }
        if (!map.containsKey(b)) {
            return;
        }
        if (player.isSneaking() && e.getAction() == Action.LEFT_CLICK_BLOCK && player.isOp()) {
            e.setCancelled(true);
            map.get(b).destroy();
            player.sendMessage("removed");
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        ItemChest be = map.get(b);
        if (!be.isOpened() && !be.isLocked()) {
            Location chestLocation = b.getLocation();
            BlockPosition pos = new BlockPosition(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ());
            PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, (Block)Blocks.CHEST, 1, 1);
            chestLocation.getWorld().playSound(chestLocation, Sound.CHEST_OPEN, 1.0f, 1.0f);
            for (Player p : chestLocation.getWorld().getPlayers()) {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
            }
        }
        be.open(player);
    }

    @EventHandler
    public void onPlayerAsyncCommand(PlayerCommandPreprocessEvent e) {
        if (this.isNotLoaded.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Your instance haven't been intitized yet, please wait!");
        }
        if (RebootServerCommand.secondMap.containsKey(Bukkit.getServer()) && (e.getMessage().contains("pv") || e.getMessage().contains("auction"))) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "The server is restarting! You cannot do this!");
        }
    }

    public static String replaceChatColors(String s) {
        for (ChatColor c : ChatColor.values()) {
            s = s.replaceAll("&" + c.getChar(), s + ChatColor.getByChar((char)c.getChar()));
        }
        return s;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onJoin(PlayerLoginEvent event) {
        if (!event.getPlayer().isWhitelisted() && Bukkit.hasWhitelist()) {
            String message = "&cWe are sorry but SkyBlock Network BETA is currently down for maintenance.\n\n&cFor more information: &bhttps://discord.skysim.sbs\n&c";
            message = ChatColor.translateAlternateColorCodes((char)'&', (String)message);
            message = message.replaceAll("&p", event.getPlayer().getName());
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, message);
        }
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent e) {
        SItem sItem = SItem.find(e.getItem());
        if (sItem == null) {
            return;
        }
        if (sItem.getType() != SMaterial.WATER_BOTTLE) {
            return;
        }
        e.setCancelled(true);
        List<PotionEffect> effects = sItem.getPotionEffects();
        User user = User.getUser(e.getPlayer().getUniqueId());
        for (PotionEffect effect : effects) {
            user.removePotionEffect(effect.getType());
            PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
            if (effect.getType().getOnDrink() != null) {
                effect.getType().getOnDrink().accept(effect, e.getPlayer());
            }
            user.addPotionEffect(effect);
            e.getPlayer().sendMessage((effect.getType().isBuff() ? ChatColor.GREEN + "" + ChatColor.BOLD + "BUFF!" : ChatColor.RED + "" + ChatColor.BOLD + "DEBUFF!") + ChatColor.RESET + ChatColor.WHITE + " You have gained " + effect.getDisplayName() + ChatColor.WHITE + "!");
        }
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            e.getPlayer().setItemInHand(SItem.of(SMaterial.GLASS_BOTTLE).getStack());
        }
    }

    @EventHandler
    public void onMilkDrink(PlayerItemConsumeEvent e) {
        SItem sItem = SItem.find(e.getItem());
        if (sItem == null) {
            return;
        }
        if (sItem.getType() != SMaterial.MILK_BUCKET) {
            return;
        }
        e.setCancelled(true);
        User user = User.getUser(e.getPlayer().getUniqueId());
        user.clearPotionEffects();
        e.getPlayer().sendMessage(ChatColor.GREEN + "You have cleared all your active effects.");
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            e.getPlayer().setItemInHand(SItem.of(SMaterial.BUCKET).getStack());
        }
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            new BukkitRunnable(){

                public void run() {
                    e.getEntity().remove();
                }
            }.runTaskLater((Plugin)SkyBlock.getPlugin(), 10L);
            return;
        }
        if (e.getEntity() instanceof Fireball && (e.getEntity().hasMetadata("dragon") || e.getEntity().hasMetadata("magma"))) {
            String type = e.getEntity().hasMetadata("dragon") ? "dragon" : "magma";
            SEntity sEntity = (SEntity)((MetadataValue)e.getEntity().getMetadata(type).get(0)).value();
            e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_HUGE, (Object)Effect.EXPLOSION_HUGE.getData());
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 5.0f, 0.0f);
            for (Entity entity : e.getEntity().getNearbyEntities(2.0, 2.0, 2.0)) {
                int d;
                if (!(entity instanceof LivingEntity)) continue;
                int n = d = type.equals("dragon") ? SUtil.random(292, 713) : 125;
                if (entity instanceof Player) {
                    User.getUser(entity.getUniqueId()).damage(d, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)sEntity.getEntity());
                } else {
                    ((LivingEntity)entity).damage((double)d);
                }
                if (!type.equals("dragon")) continue;
                entity.sendMessage(ChatColor.DARK_PURPLE + "\u262c " + ChatColor.RED + sEntity.getStatistics().getEntityName() + ChatColor.LIGHT_PURPLE + " used " + ChatColor.YELLOW + "Fireball" + ChatColor.LIGHT_PURPLE + " on you for " + ChatColor.RED + d + " damage.");
            }
        }
    }

    public static CombatAction getLastCombatAction(Player player) {
        return COMBAT_MAP.get(player.getUniqueId());
    }

    private static CombatAction createCombatAction(final boolean attacked, final double damage, final boolean bowShot, final long timestamp) {
        return new CombatAction(){

            @Override
            public boolean attacked() {
                return attacked;
            }

            @Override
            public double getDamageDealt() {
                return damage;
            }

            @Override
            public boolean isBowShot() {
                return bowShot;
            }

            @Override
            public long getTimeStamp() {
                return timestamp;
            }
        };
    }

    @EventHandler
    public void onEntityDamage1(EntityDamageByEntityEvent event) {
        if (event.getEntity().hasMetadata("GiantSword")) {
            event.setCancelled(true);
        }
    }

    public static Double COGCalculation(Integer baseDmg, Player player) {
        User user = User.getUser(player.getUniqueId());
        long coin = user.getCoins();
        if (coin > (long)baseDmg.intValue()) {
            user.subCoins(baseDmg.intValue());
            Sputnik.CoinsTakenOut.put(player.getUniqueId(), baseDmg);
            if (Sputnik.CoinsTakenOut.containsKey(player.getUniqueId())) {
                SUtil.delay(() -> Sputnik.CoinsTakenOut.remove(player.getUniqueId()), 35L);
            }
            return (double)baseDmg.intValue() * 25.0 / 100.0;
        }
        return 0.0;
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) {
            return;
        }
        event.setCancelled(true);
        EnderDragon drag = (EnderDragon)event.getEntity();
        int addamount = SUtil.random(15000, 35000);
        if (drag.getHealth() + (double)addamount < drag.getMaxHealth()) {
            drag.setHealth(drag.getHealth() + (double)addamount);
        } else {
            drag.setHealth(drag.getHealth() + (drag.getMaxHealth() - drag.getHealth()));
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ENDER_CRYSTAL) {
            e.setCancelled(true);
        }
    }

    public void onCrystalDMG(World world, Player player) {
        for (Entity e : world.getEntities()) {
            if (e.getType() != EntityType.ENDER_DRAGON) continue;
            User user = User.getUser(player.getUniqueId());
            user.damageEntity((Damageable)e, 2000.0);
        }
    }

    @EventHandler
    void onHit(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Skeleton && e.getEntity().hasMetadata("SkeletonMaster")) {
            e.setCancelled(true);
            WitherSkull skull = (WitherSkull)e.getEntity().launchProjectile(WitherSkull.class);
            skull.setShooter((ProjectileSource)e.getEntity());
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.WITHER_SHOOT, 1.0f, 1.0f);
        }
    }

    @EventHandler
    void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            if (!e.getEntity().getWorld().getName().toLowerCase().contains("dragon")) {
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            Location loc = e.getEntity().getLocation();
            e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 3);
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
            SUtil.broadcastExcept(ChatColor.translateAlternateColorCodes((char)'&', (String)("&5\u262c &a" + e.getDamager().getName() + " &dhas destroyed an &5Ender Crystal&d!")), (Player)e.getDamager());
            e.getDamager().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&5\u262c &dYou destroyed an &5Ender Crystal&d!"));
            e.getEntity().remove();
            ItemStack cf = SItem.of(SMaterial.CRYSTAL_FRAGMENT).getStack();
            this.onCrystalDMG(e.getDamager().getWorld(), (Player)e.getDamager());
            SItem sitem = SItem.find(((Player)e.getDamager()).getItemInHand());
            if (sitem != null) {
                if (sitem.getEnchantment(EnchantmentType.TELEKINESIS) != null && !Sputnik.isFullInv((Player)e.getDamager()) && sitem.getType() != SMaterial.ENCHANTED_BOOK) {
                    Sputnik.GiveItem(cf, (Player)e.getDamager());
                } else {
                    e.getEntity().getWorld().dropItem(loc, cf);
                }
            } else {
                e.getEntity().getWorld().dropItem(loc, cf);
            }
        } else if (e.getDamager() instanceof Arrow && e.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
            Projectile p = (Projectile)e.getDamager();
            if (!(p.getShooter() instanceof Player)) {
                return;
            }
            e.setCancelled(true);
            Location loc2 = e.getEntity().getLocation();
            e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_HUGE, 3);
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
            SUtil.broadcastExcept(ChatColor.translateAlternateColorCodes((char)'&', (String)("&5\u262c &a" + ((Player)p.getShooter()).getName() + " &dhas destroyed an &5Ender Crystal&d!")), (Player)p.getShooter());
            ((Player)p.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&5\u262c &dYou destroyed an &5Ender Crystal&d!"));
            e.getEntity().remove();
            ItemStack cf2 = SItem.of(SMaterial.CRYSTAL_FRAGMENT).getStack();
            this.onCrystalDMG(e.getEntity().getWorld(), (Player)p.getShooter());
            SItem sitem2 = SItem.find(((Player)p.getShooter()).getItemInHand());
            if (sitem2 != null) {
                if (sitem2.getEnchantment(EnchantmentType.TELEKINESIS) != null && !Sputnik.isFullInv((Player)p.getShooter()) && sitem2.getType() != SMaterial.ENCHANTED_BOOK) {
                    Sputnik.GiveItem(cf2, (Player)p.getShooter());
                } else {
                    e.getEntity().getWorld().dropItem(loc2, cf2);
                }
            } else {
                e.getEntity().getWorld().dropItem(loc2, cf2);
            }
        }
    }

    public static void spawnDamageInd(final Entity damaged, double damage, boolean isCrit) {
        if (damaged.hasMetadata("Dimoon")) {
            return;
        }
        Location l_ = damaged.getLocation().clone();
        l_.add(SUtil.random(-1.5, 1.5), 2.0, SUtil.random(-1.5, 1.5));
        final EntityArmorStand stand = new EntityArmorStand((net.minecraft.server.v1_8_R3.World)((CraftWorld)damaged.getWorld()).getHandle());
        stand.setLocation(l_.getX(), l_.getY(), l_.getZ(), 0.0f, 0.0f);
        ((ArmorStand)stand.getBukkitEntity()).setMarker(true);
        stand.setCustomName(isCrit ? SUtil.rainbowize("\u2727" + (int)damage + "\u2727") : "" + ChatColor.GRAY + (int)damage);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        final ArrayList prp = new ArrayList();
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)stand);
                for (Entity e : damaged.getNearbyEntities(12.0, 12.0, 12.0)) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)packet);
                    prp.add((Player)e);
                }
            }
        }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(new int[]{stand.getId()});
                for (Player e : prp) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)pa);
                }
            }
        }.runTaskLaterAsynchronously((Plugin)SkyBlock.getPlugin(), 30L);
    }

    public static void spawnSpecialDamageInd(final Entity damaged, double damage, ChatColor c) {
        Location l_ = damaged.getLocation().clone();
        l_.add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5));
        final EntityArmorStand stand = new EntityArmorStand((net.minecraft.server.v1_8_R3.World)((CraftWorld)damaged.getWorld()).getHandle());
        stand.setLocation(l_.getX(), l_.getY(), l_.getZ(), 0.0f, 0.0f);
        ((ArmorStand)stand.getBukkitEntity()).setMarker(true);
        stand.setCustomName(c + String.valueOf((int)Math.round(damage)));
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        final ArrayList prp = new ArrayList();
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)stand);
                for (Entity e : damaged.getNearbyEntities(12.0, 12.0, 12.0)) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)packet);
                    prp.add((Player)e);
                }
            }
        }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(new int[]{stand.getId()});
                for (Player e : prp) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)pa);
                }
            }
        }.runTaskLaterAsynchronously((Plugin)SkyBlock.getPlugin(), 30L);
    }

    public static void customDMGIND(final Entity damaged, String text) {
        Location l_ = damaged.getLocation().clone();
        l_.add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5));
        final EntityArmorStand stand = new EntityArmorStand((net.minecraft.server.v1_8_R3.World)((CraftWorld)damaged.getWorld()).getHandle());
        stand.setLocation(l_.getX(), l_.getY(), l_.getZ(), 0.0f, 0.0f);
        ((ArmorStand)stand.getBukkitEntity()).setMarker(true);
        stand.setCustomName(text);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        final ArrayList prp = new ArrayList();
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)stand);
                for (Entity e : damaged.getNearbyEntities(12.0, 12.0, 12.0)) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)packet);
                    prp.add((Player)e);
                }
            }
        }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
        new BukkitRunnable(){

            public void run() {
                PacketPlayOutEntityDestroy pa = new PacketPlayOutEntityDestroy(new int[]{stand.getId()});
                for (Player e : prp) {
                    if (!(e instanceof Player)) continue;
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket((Packet)pa);
                }
            }
        }.runTaskLaterAsynchronously((Plugin)SkyBlock.getPlugin(), 30L);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        for (Entity en : e.getChunk().getEntities()) {
            if (!en.hasMetadata("pets")) continue;
            en.remove();
        }
    }

    @EventHandler
    public void watcherMobsDamage(EntityDamageEvent ev) {
        if (!ev.getEntity().hasMetadata("WATCHER_E")) {
            return;
        }
        if (((LivingEntity)ev.getEntity()).getHealth() - ev.getFinalDamage() <= 0.0) {
            Watcher watcher = Watcher.getWatcher(ev.getEntity().getWorld());
            if (watcher == null) {
                return;
            }
            Watcher watcher2 = watcher;
            --watcher2.currentMobsCount;
            if (SUtil.random(0, 1) == 0) {
                watcher.sd(watcher.mobDeathConvs[SUtil.random(0, watcher.mobDeathConvs.length - 1)], 0, 50, true);
            }
        }
    }

    @EventHandler
    public void watcherDamage(final EntityDamageByEntityEvent ev) {
        Watcher watcher = Watcher.getWatcher(ev.getEntity().getWorld());
        if (watcher == null) {
            return;
        }
        if (watcher.welcomeParticles) {
            return;
        }
        if (!ev.getEntity().hasMetadata("WATCHER_M")) {
            return;
        }
        ev.setCancelled(true);
        watcher.sd(watcher.watcherAttack[SUtil.random(0, watcher.watcherAttack.length - 1)], 0, 50, true);
        if (!Watcher.getWatcher((World)ev.getEntity().getWorld()).isResting) {
            return;
        }
        if (Watcher.random(0, 2) == 1 && (ev.getDamager() instanceof Player || ev.getDamager() instanceof Arrow)) {
            Player p = null;
            p = ev.getDamager() instanceof Player ? (Player)ev.getDamager() : (Player)((Arrow)ev.getDamager()).getShooter();
            User.getUser(p.getUniqueId()).damage(p.getMaxHealth() / 10.0, EntityDamageEvent.DamageCause.CUSTOM, ev.getEntity());
            final Beam beam = new Beam(ev.getEntity().getLocation().clone().add(0.0, 1.3, 0.0), p.getLocation().clone().add(0.0, 1.0, 0.0));
            p.damage(1.0E-5);
            beam.start();
            final Player p2 = p;
            new BukkitRunnable(){
                int i = 0;

                public void run() {
                    if (this.i >= 20 || !Watcher.getWatcher((World)ev.getEntity().getWorld()).isResting) {
                        beam.stop();
                        this.cancel();
                        return;
                    }
                    ++this.i;
                    beam.setStartingPosition(ev.getEntity().getLocation().clone().add(0.0, 1.3, 0.0));
                    beam.setEndingPosition(p2.getLocation().clone().add(0.0, 1.0, 0.0));
                    beam.update();
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        }
    }

    @EventHandler
    public void ans(BlockPlaceEvent e) {
        if (e.getPlayer().getWorld().getName().contains("arena") && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (SkyblockNPC skyblockNPC : SkyblockNPCManager.getNPCS()) {
            if (!skyblockNPC.isShown(player)) continue;
            skyblockNPC.hideFrom(player);
        }
        HologramManager.getHolograms().forEach(hologram -> {
            if (hologram.isShown(player)) {
                hologram.hide(player);
            }
        });
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        World fromWorld;
        Player player = event.getPlayer();
        World toWorld = event.getTo().getWorld();
        if (!toWorld.equals(fromWorld = event.getFrom().getWorld())) {
            for (SkyblockNPC npc : SkyblockNPCManager.getNPCS()) {
                if (npc.getWorld().equals(toWorld)) {
                    SUtil.delay(() -> npc.showTo(player), 20L);
                    continue;
                }
                if (!npc.isShown(player) || !npc.getWorld().equals(fromWorld)) continue;
                npc.hideFrom(player);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location to;
        Player player = event.getPlayer();
        Location from = event.getFrom();
        if (!this.isSignificantMove(from, to = event.getTo())) {
            return;
        }
        this.updateEntitiesInRange(player);
    }

    private boolean isSignificantMove(Location from, Location to) {
        return to != null && (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ());
    }

    private void updateEntitiesInRange(Player player) {
        this.updateNPCs(player);
        this.updateHolograms(player);
    }

    private void updateNPCs(Player player) {
        SkyblockNPCManager.getNPCS().forEach(npc -> {
            if (!npc.isShown(player) && npc.inRangeOf(player)) {
                npc.showTo(player);
            } else if (npc.isShown(player) && !npc.inRangeOf(player)) {
                npc.hideFrom(player);
            }
        });
    }

    private void updateHolograms(Player player) {
        HologramManager.getHolograms().forEach(hologram -> {
            if (!hologram.isShown(player) && hologram.inRangeOf(player)) {
                hologram.show(player);
            } else if (hologram.isShown(player) && !hologram.inRangeOf(player)) {
                hologram.hide(player);
            }
        });
    }

    @EventHandler
    public void swingSword(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (User.getUser(p.getUniqueId()) == null) {
            return;
        }
        if (SItem.find(p.getItemInHand()) == null) {
            return;
        }
        if (SItem.find(p.getItemInHand()).getType() != SMaterial.HIDDEN_REVANTUS_SWORD) {
            return;
        }
        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            Location playerLocation = e.getPlayer().getEyeLocation();
            Location entityTargetLocation = e.getPlayer().getEyeLocation().clone().add(playerLocation.getDirection().normalize().multiply(7));
            Vector vector = playerLocation.clone().toVector().subtract(entityTargetLocation.clone().toVector());
            int count = 60;
            for (int i = 1; i <= count; ++i) {
                Location nl = entityTargetLocation.clone().add(vector.clone().multiply((double)i / (double)count));
                Collection el = nl.getWorld().getNearbyEntities(nl, 0.15, 0.15, 0.15);
                Entity[] clt = el.toArray(new Entity[el.size()]);
                for (int j = 0; j < clt.length; ++j) {
                    Entity en = clt[j];
                    if (en instanceof Player || !(en instanceof LivingEntity) || en.isDead() || en.hasMetadata("GiantSword") || en.hasMetadata("NPC") || en instanceof ArmorStand || !(en.getLocation().distance(e.getPlayer().getLocation()) > 3.0)) continue;
                    Object[] atp = Sputnik.calculateDamage(p, p, p.getItemInHand(), (LivingEntity)en, false);
                    double finalDamage1 = ((Float)atp[0]).floatValue();
                    PlayerListener.spawnDamageInd(en, ((Float)atp[2]).floatValue(), (Boolean)atp[1]);
                    FerocityCalculation.activeFerocityTimes(p, (LivingEntity)en, (int)finalDamage1, (Boolean)atp[1]);
                    User.getUser(p.getUniqueId()).damageEntity((Damageable)en, finalDamage1);
                    return;
                }
            }
        }
    }

    public Map<UUID, Boolean> getIsNotLoaded() {
        return this.isNotLoaded;
    }

    private static /* synthetic */ void lambda$onEntityDamage$6(Entity en, Vector v) {
        en.setVelocity(v);
    }

    private static interface BowShooting {
        public ItemStack getBow();

        public float getForce();
    }

    public static interface CombatAction {
        public boolean attacked();

        public double getDamageDealt();

        public boolean isBowShot();

        public long getTimeStamp();
    }
}

