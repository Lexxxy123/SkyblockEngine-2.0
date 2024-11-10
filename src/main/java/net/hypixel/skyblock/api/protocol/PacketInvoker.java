/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.api.protocol;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.StaticWardenManager;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PacketInvoker {
    public static void dropChest(final Player owner, Location loc) {
        final boolean isExplosive = SUtil.random(0, 3) == 1;
        owner.sendMessage(Sputnik.trans("&e&oYou dropped a Loot Chest!"));
        final ArmorStand drop = (ArmorStand)owner.getWorld().spawn(loc.clone().add(0.0, -1.4, 0.0), ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setCustomName(Sputnik.trans("&6&lLOOT CHEST"));
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        if (!isExplosive) {
            drop.getEquipment().setHelmet(SItem.of(SMaterial.CHEST).getStack());
        } else {
            drop.getEquipment().setHelmet(SItem.of(SMaterial.TRAPPED_CHEST).getStack());
        }
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                drop.remove();
            }
        }, 1000L);
        new BukkitRunnable(){
            boolean noticeable = true;

            public void run() {
                if (drop.isDead()) {
                    this.cancel();
                    return;
                }
                if (owner.getWorld() != drop.getWorld()) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                if (drop.isDead()) {
                    this.cancel();
                    return;
                }
                if (!owner.getWorld().getEntities().contains(drop)) {
                    this.cancel();
                    return;
                }
                if (!owner.isOnline()) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                PacketInvoker.invokeChannel((Entity)drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.HAPPY_VILLAGER, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (!(e instanceof Player) || e != owner) continue;
                    drop.teleport((Entity)owner);
                    owner.playSound(owner.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
                    if (!isExplosive) {
                        Item item;
                        double r = SUtil.random(0.0, 1.0);
                        double c = 5.0E-4;
                        double r2 = SUtil.random(0.0, 1.0);
                        double c2 = 0.001;
                        if (r < 5.0E-4) {
                            owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.4f);
                            owner.sendMessage(Sputnik.trans("&5&lLUCKY! &fYou found a &5Luckiness VII &fbook inside the loot chest!"));
                            SItem Book = SItem.of(SMaterial.ENCHANTED_BOOK);
                            Book.addEnchantment(EnchantmentType.LUCKINESS, 7);
                            item = SUtil.spawnPersonalItem(Book.getStack(), drop.getLocation(), owner);
                            item.setCustomNameVisible(true);
                            item.setCustomName(Sputnik.trans("&e&ka&r &f1x &5Luckiness VII &e&ka"));
                        } else if (r2 < 0.001) {
                            owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
                            owner.sendMessage(Sputnik.trans("&6&lLUCKY! &fYou found a &7[Lvl 1] &6Enderman &finside the loot chest!"));
                            SItem eman = SItem.of(SMaterial.ENDERMAN_PET);
                            eman.setRarity(Rarity.LEGENDARY);
                            item = SUtil.spawnPersonalItem(eman.getStack(), drop.getLocation(), owner);
                            item.setCustomNameVisible(true);
                            item.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Enderman &e&ka"));
                        } else {
                            int bitsReward = SUtil.random(80, 400);
                            User.getUser(owner.getUniqueId()).addBits(bitsReward);
                            owner.sendMessage(Sputnik.trans("&b&lBITS! &fYou found &b" + SUtil.commaify(bitsReward) + " Bits&f inside the loot chest!"));
                        }
                    } else {
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playEffect(owner.getLocation(), Effect.EXPLOSION_HUGE, 1);
                        owner.getWorld().playSound(owner.getLocation(), Sound.EXPLODE, 1.5f, 0.0f);
                        owner.getWorld().playSound(owner.getLocation(), Sound.AMBIENCE_THUNDER, 1.5f, 0.0f);
                        User.getUser(owner.getUniqueId()).damage(owner.getMaxHealth() * 10.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (Entity)drop);
                        owner.damage(1.0E-5);
                    }
                    drop.remove();
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropVoidSpawner(Player owner, Location loc) {
        for (Entity e : owner.getWorld().getEntities()) {
            if (!e.hasMetadata("owner") || !((MetadataValue)e.getMetadata("owner").get(0)).asString().equals(owner.getUniqueId().toString())) continue;
            return;
        }
        owner.sendMessage(Sputnik.trans("&5&lYou challenged the Voidling's Warden Boss! It is spawning..."));
        SlayerQuest.playBossSpawn(loc.add(0.0, 2.0, 0.0), null);
        SUtil.delay(() -> {
            SEntity e2 = new SEntity(loc, SEntityType.VOIDLINGS_WARDEN, new Object[0]);
            PacketInvoker.activeGyroAbi((Entity)e2.getEntity());
            e2.getEntity().setMetadata("owner", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)owner.getUniqueId()));
            StaticWardenManager.WARDEN = e2;
            ((CraftZombie)e2.getEntity()).setTarget((LivingEntity)owner);
        }, 30L);
    }

    public static void activeGyroAbi(Entity e) {
        Location loc = e.getLocation().clone().add(0.0, -1.0, 0.0);
        PacketInvoker.gyroWand(loc, 8, 6);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 8, 6), 5L);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 6, 4), 10L);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 4, 2), 15L);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 3, 1), 20L);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 2, 1), 25L);
        SUtil.delay(() -> PacketInvoker.gyroWand(loc, 1, 0), 30L);
        SUtil.delay(() -> PacketInvoker.cylinderReset(loc, 10), 32L);
        for (int i = 0; i < 40; ++i) {
            PacketInvoker.a(loc.clone().add(0.0, 0.3, 0.0), i * 12);
        }
    }

    public static void gyroWand(Location l, int arg0, int arg1) {
        Material[] mat = new Material[]{Material.OBSIDIAN, Material.AIR, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.AIR};
        Material[] mat_r = new Material[]{Material.OBSIDIAN, Material.STAINED_GLASS, Material.STAINED_CLAY};
        List<Block> a = PacketInvoker.cylinder(l, arg0);
        List<Block> b = PacketInvoker.cylinder(l, arg1);
        a.removeIf(b::contains);
        ArrayList<Location> aA = new ArrayList<Location>();
        for (Block bl2 : a) {
            aA.add(bl2.getLocation().add(0.5, 0.0, 0.5));
        }
        if (arg1 != 0) {
            for (Location loc : aA) {
                byte data = 0;
                int r = SUtil.random(0, 4);
                Material mats = mat[r];
                if (mats == Material.STAINED_GLASS) {
                    data = 2;
                } else if (mats == Material.STAINED_CLAY) {
                    data = 11;
                }
                BlockFallAPI.sendVelocityBlock(loc, mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
            }
        } else {
            for (Location loc : aA) {
                byte data = 0;
                int r = SUtil.random(0, 2);
                Material mats = mat_r[r];
                if (mats == Material.STAINED_GLASS) {
                    data = 2;
                } else if (mats == Material.STAINED_CLAY) {
                    data = 11;
                }
                BlockFallAPI.sendVelocityBlock(loc, mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
                BlockFallAPI.sendVelocityBlock(l.getBlock().getLocation().add(0.5, 0.0, 0.5), mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
            }
        }
    }

    public static void a(final Location location, final float startYaw) {
        new BukkitRunnable(){
            float cout;
            int b;
            double c;
            {
                this.cout = startYaw;
                this.b = 0;
                this.c = 8.0;
            }

            public void run() {
                if (this.b >= 22) {
                    this.cancel();
                    return;
                }
                Location loc = location.clone();
                ++this.b;
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                if (this.c > 0.0) {
                    this.c -= 0.3;
                }
                loc.add(loc.getDirection().normalize().multiply(this.c));
                location.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                location.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 10.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static List<Block> cylinder(Location loc, int r) {
        ArrayList<Block> bl = new ArrayList<Block>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) > rSquared) continue;
                Location l = new Location(w, (double)x, (double)cy, (double)z);
                bl.add(l.getBlock());
            }
        }
        return bl;
    }

    public static void cylinderReset(Location loc, int r) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) > rSquared) continue;
                Location l = new Location(w, (double)x, (double)cy, (double)z);
                l.getBlock().getState().update();
            }
        }
    }

    public static void dropT34Pet(final Player owner, Location loc) {
        owner.sendMessage(Sputnik.trans("&7&oYou dropped something, check around you..."));
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        final ArmorStand drop = (ArmorStand)owner.getWorld().spawn(loc.clone().add(0.0, -0.5, 0.0), ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.HIDDEN_USSR_T34_TANK_PET).getStack());
        drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Mini T-34 &e&ka"));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                owner.sendMessage(ChatColor.RED + "Oh no, you didn't picked up your T-34 pet, it despawned, good luck next time, F.");
                drop.remove();
            }
        }, 6000L);
        new BukkitRunnable(){
            boolean noticeable = true;

            public void run() {
                if (drop.isDead()) {
                    this.cancel();
                    return;
                }
                if (!owner.getWorld().getEntities().contains(drop)) {
                    this.cancel();
                    return;
                }
                if (!owner.isOnline()) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel((Entity)drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (!(e instanceof Player) || e != owner) continue;
                    if (!Sputnik.isFullInv(owner)) {
                        drop.teleport((Entity)owner);
                        drop.remove();
                        SUtil.globalBroadcast(Sputnik.trans(""));
                        SUtil.globalBroadcast(Sputnik.trans("&c>&e>&b> &6&lWOW! &e" + owner.getDisplayName() + " &ehas obtained &7[Lvl 1] &6Mini T-34&e! &b<&e<&c<"));
                        SUtil.globalBroadcast(Sputnik.trans(""));
                        owner.sendMessage(Sputnik.trans("&6&lPET DROP! &eYou have obtained &7[Lvl 1] &6Mini T-34&e! &d&lGG&e!"));
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.1f);
                        owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                        owner.getInventory().addItem(new ItemStack[]{SItem.of(SMaterial.HIDDEN_USSR_T34_TANK_PET).getStack()});
                        continue;
                    }
                    SUtil.delay(() -> {
                        ArmorStand val$drop = drop;
                        Player val$owner = owner;
                        if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                            owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                            owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                            this.noticeable = false;
                        }
                    }, 20L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropEye(final Player owner, Location loc, final int amount) {
        final ArmorStand drop = (ArmorStand)owner.getWorld().spawn(loc.clone().add(0.0, -0.7, 0.0), ArmorStand.class);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.REMNANT_OF_THE_EYE).getStack());
        drop.setCustomName(Sputnik.trans("&7" + amount + "x &9Ender's Fragment"));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                drop.remove();
            }
        }, 2000L);
        new BukkitRunnable(){
            boolean noticeable = true;

            public void run() {
                if (owner.getWorld() != drop.getWorld()) {
                    drop.remove();
                }
                if (drop.isDead()) {
                    this.cancel();
                    return;
                }
                if (!owner.getWorld().getEntities().contains(drop)) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                if (!owner.isOnline()) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel((Entity)drop, drop.getWorld(), owner);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                for (Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (!(e instanceof Player) || e != owner) continue;
                    if (!Sputnik.isFullInv(owner)) {
                        drop.teleport((Entity)owner);
                        drop.remove();
                        owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                        owner.getWorld().playEffect(drop.getLocation(), Effect.LAVA_POP, 1);
                        for (int i = 0; i < amount; ++i) {
                            SItem sitem = SItem.of(SMaterial.HIDDEN_VOID_FRAGMENT);
                            owner.getInventory().addItem(new ItemStack[]{sitem.getStack()});
                        }
                        continue;
                    }
                    SUtil.delay(() -> {
                        ArmorStand val$drop = drop;
                        Player val$owner = owner;
                        if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                            owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                            owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                            this.noticeable = false;
                        }
                    }, 20L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropGoldenTigerPet(final Player owner, Location loc, final boolean isMythic) {
        owner.sendMessage(Sputnik.trans("&7&oYou dropped something, check around you..."));
        SUtil.sendTitle(owner, Sputnik.trans(""));
        SUtil.sendSubtitle(owner, Sputnik.trans("&6&oYou dropped something, check around!"));
        new BukkitRunnable(){

            public void run() {
                System.out.println("**A TIGER PET DROPPED!** Player `" + owner.getName() + "` dropped a Golden Tiger Pet at `" + owner.getWorld().getName() + "`, Mythic: " + isMythic);
            }
        }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        final ArmorStand drop = (ArmorStand)owner.getWorld().spawn(loc.clone().add(0.0, -0.5, 0.0), ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022).getStack());
        if (!isMythic) {
            drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Golden Tiger &e&ka"));
        } else {
            drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &dGolden Tiger &e&ka"));
        }
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                new BukkitRunnable(){

                    public void run() {
                        System.out.println("**PET DESPAWNED! ** Player `" + owner.getName() + "` let a Golden Tiger Pet at `" + owner.getWorld().getName() + "` despawned, Mythic: " + isMythic);
                    }
                }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
                owner.sendMessage(ChatColor.RED + "Oh no, you didn't picked up your Golden Tiger pet, it despawned, good luck next time, F.");
                drop.remove();
            }
        }, 6000L);
        new BukkitRunnable(){
            boolean noticeable = true;

            public void run() {
                if (owner.getWorld() != drop.getWorld()) {
                    new BukkitRunnable(){

                        public void run() {
                            System.out.println("**PET DESPAWNED! ** Player `" + owner.getName() + "` let a Golden Tiger Pet at `" + owner.getWorld().getName() + "` despawned, Mythic: " + isMythic);
                        }
                    }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
                    owner.sendMessage(ChatColor.RED + "Oh no, you didn't picked up your Golden Tiger pet, it despawned, good luck next time, F.");
                    drop.remove();
                }
                if (drop.isDead()) {
                    this.cancel();
                    return;
                }
                if (!owner.getWorld().getEntities().contains(drop)) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                if (!owner.isOnline()) {
                    drop.remove();
                    this.cancel();
                    return;
                }
                Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel((Entity)drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (isMythic) {
                    owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                    owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0, 1, (float)SUtil.random(-1.0, 1.0), 1.0f, (float)SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                }
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (!(e instanceof Player) || e != owner) continue;
                    if (!Sputnik.isFullInv(owner)) {
                        drop.teleport((Entity)owner);
                        drop.remove();
                        SUtil.globalBroadcast(Sputnik.trans(""));
                        if (!isMythic) {
                            SUtil.globalBroadcast(Sputnik.trans("&c>&e>&b> &6&lWOW! &e" + owner.getDisplayName() + " &ehas obtained &7[Lvl 1] &6Golden Tiger&e! &b<&e<&c<"));
                        } else {
                            SUtil.globalBroadcast(Sputnik.trans("&c>&e>&b> &6&lNICE! &e" + owner.getDisplayName() + " &ehas obtained &7[Lvl 1] &dGolden Tiger&e! &b<&e<&c<"));
                        }
                        SUtil.globalBroadcast(Sputnik.trans(""));
                        if (!isMythic) {
                            owner.sendMessage(Sputnik.trans("&6&lPET DROP! &eYou have obtained &7[Lvl 1] &6Golden Tiger&e! &d&lGG&e!"));
                        } else {
                            owner.sendMessage(Sputnik.trans("&6&lSUPER RARE PET DROP! &eYou have obtained &7[Lvl 1] &dGolden Tiger&e! &d&lWOW&e!"));
                        }
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.1f);
                        owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                        owner.getWorld().playEffect(drop.getLocation(), Effect.LAVA_POP, 1);
                        new BukkitRunnable(){

                            public void run() {
                                System.out.println("**PET PICKUP!** Player `" + owner.getName() + "` picked up a Golden Tiger Pet at `" + owner.getWorld().getName() + "`, Mythic: " + isMythic);
                            }
                        }.runTaskAsynchronously((Plugin)SkyBlock.getPlugin());
                        if (!isMythic) {
                            owner.getInventory().addItem(new ItemStack[]{SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022).getStack()});
                            continue;
                        }
                        SItem sitem = SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022);
                        sitem.setRarity(Rarity.MYTHIC);
                        owner.getInventory().addItem(new ItemStack[]{sitem.getStack()});
                        continue;
                    }
                    SUtil.delay(() -> {
                        ArmorStand val$drop = drop;
                        Player val$owner = owner;
                        if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                            owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                            owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                            this.noticeable = false;
                        }
                    }, 20L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void invokeChannel(Entity as1, World w, Player owner) {
        net.minecraft.server.v1_8_R3.Entity el = ((CraftEntity)as1).getHandle();
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{el.getId()});
        for (Entity e : w.getNearbyEntities(owner.getLocation(), 35.0, 35.0, 35.0)) {
            Player p;
            if (!(e instanceof Player) || (p = (Player)e) == owner) continue;
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }
}

