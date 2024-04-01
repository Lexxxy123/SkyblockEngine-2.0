package net.hypixel.skyblock.api.protocol;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.entity.StaticWardenManager;
import net.hypixel.skyblock.entity.end.VoidlingsWardenMob;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.slayer.SlayerQuest;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PacketInvoker {
    public static void dropChest(final Player owner, final Location loc) {
        final boolean isExplosive = SUtil.random(0, 3) == 1;
        owner.sendMessage(Sputnik.trans("&e&oYou dropped a Loot Chest!"));
        final ArmorStand drop = (ArmorStand) owner.getWorld().spawn(loc.clone().add(0.0, -1.4, 0.0), (Class) ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setCustomName(Sputnik.trans("&6&lLOOT CHEST"));
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkyBlock.getPlugin(), true));
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
        new BukkitRunnable() {
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
                PacketInvoker.invokeChannel(drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.HAPPY_VILLAGER, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (final Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (e instanceof Player && e == owner) {
                        drop.teleport(owner);
                        owner.playSound(owner.getLocation(), Sound.CHEST_OPEN, 1.0f, 1.0f);
                        if (!isExplosive) {
                            final double r = SUtil.random(0.0, 1.0);
                            final double c = 5.0E-4;
                            final double r2 = SUtil.random(0.0, 1.0);
                            final double c2 = 0.001;
                            if (r < c) {
                                owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.4f);
                                owner.sendMessage(Sputnik.trans("&5&lLUCKY! &fYou found a &5Luckiness VII &fbook inside the loot chest!"));
                                final SItem Book = SItem.of(SMaterial.ENCHANTED_BOOK);
                                Book.addEnchantment(EnchantmentType.LUCKINESS, 7);
                                final Item item = SUtil.spawnPersonalItem(Book.getStack(), drop.getLocation(), owner);
                                item.setCustomNameVisible(true);
                                item.setCustomName(Sputnik.trans("&e&ka&r &f1x &5Luckiness VII &e&ka"));
                            } else if (r2 < c2) {
                                owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.0f);
                                owner.sendMessage(Sputnik.trans("&6&lLUCKY! &fYou found a &7[Lvl 1] &6Enderman &finside the loot chest!"));
                                final SItem eman = SItem.of(SMaterial.ENDERMAN_PET);
                                eman.setRarity(Rarity.LEGENDARY);
                                final Item item = SUtil.spawnPersonalItem(eman.getStack(), drop.getLocation(), owner);
                                item.setCustomNameVisible(true);
                                item.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Enderman &e&ka"));
                            } else {
                                final int bitsReward = SUtil.random(80, 400);
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
                            User.getUser(owner.getUniqueId()).damage(owner.getMaxHealth() * 10.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, drop);
                            owner.damage(1.0E-5);
                        }
                        drop.remove();
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropVoidSpawner(final Player owner, final Location loc) {
        for (final Entity e : owner.getWorld().getEntities()) {
            if (e.hasMetadata("owner") && e.getMetadata("owner").get(0).asString().equals(owner.getUniqueId().toString())) {
                return;
            }
        }
        owner.sendMessage(Sputnik.trans("&5&lYou challenged the Voidling's Warden Boss! It is spawning..."));
        SlayerQuest.playBossSpawn(loc.add(0.0, 2.0, 0.0), null);
        SUtil.delay(() -> {
            final SEntity e2 = new SEntity(loc, SEntityType.VOIDLINGS_WARDEN);
            activeGyroAbi(e2.getEntity());
            e2.getEntity().setMetadata("owner", new FixedMetadataValue(SkyBlock.getPlugin(), owner.getUniqueId()));
            StaticWardenManager.WARDEN = e2;
            ((CraftZombie) e2.getEntity()).setTarget(owner);
        }, 30L);
    }

    public static void activeGyroAbi(final Entity e) {
        final Location loc = e.getLocation().clone().add(0.0, -1.0, 0.0);
        gyroWand(loc, 8, 6);
        SUtil.delay(() -> gyroWand(loc, 8, 6), 5L);
        SUtil.delay(() -> gyroWand(loc, 6, 4), 10L);
        SUtil.delay(() -> gyroWand(loc, 4, 2), 15L);
        SUtil.delay(() -> gyroWand(loc, 3, 1), 20L);
        SUtil.delay(() -> gyroWand(loc, 2, 1), 25L);
        SUtil.delay(() -> gyroWand(loc, 1, 0), 30L);
        SUtil.delay(() -> cylinderReset(loc, 10), 32L);
        for (int i = 0; i < 40; ++i) {
            a(loc.clone().add(0.0, 0.3, 0.0), (float) (i * 12));
        }
    }

    public static void gyroWand(final Location l, final int arg0, final int arg1) {
        final Material[] mat = {Material.OBSIDIAN, Material.AIR, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.AIR};
        final Material[] mat_r = {Material.OBSIDIAN, Material.STAINED_GLASS, Material.STAINED_CLAY};
        final List<Block> a = cylinder(l, arg0);
        final List<Block> b = cylinder(l, arg1);
        a.removeIf(b::contains);
        final List<Location> aA = new ArrayList<Location>();
        for (final Block bl2 : a) {
            aA.add(bl2.getLocation().add(0.5, 0.0, 0.5));
        }
        if (arg1 != 0) {
            for (final Location loc : aA) {
                byte data = 0;
                final int r = SUtil.random(0, 4);
                final Material mats = mat[r];
                if (mats == Material.STAINED_GLASS) {
                    data = 2;
                } else if (mats == Material.STAINED_CLAY) {
                    data = 11;
                }
                BlockFallAPI.sendVelocityBlock(loc, mats, data, loc.getWorld(), 10, new Vector(0.0, 0.225, 0.0));
            }
        } else {
            for (final Location loc : aA) {
                byte data = 0;
                final int r = SUtil.random(0, 2);
                final Material mats = mat_r[r];
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
        new BukkitRunnable() {
            float cout = startYaw;
            int b = 0;
            double c = 8.0;

            public void run() {
                if (this.b >= 22) {
                    this.cancel();
                    return;
                }
                final Location loc = location.clone();
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
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public static List<Block> cylinder(final Location loc, final int r) {
        final List<Block> bl = new ArrayList<Block>();
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        final World w = loc.getWorld();
        final int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    final Location l = new Location(w, x, cy, z);
                    bl.add(l.getBlock());
                }
            }
        }
        return bl;
    }

    public static void cylinderReset(final Location loc, final int r) {
        final int cx = loc.getBlockX();
        final int cy = loc.getBlockY();
        final int cz = loc.getBlockZ();
        final World w = loc.getWorld();
        final int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; ++x) {
            for (int z = cz - r; z <= cz + r; ++z) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    final Location l = new Location(w, x, cy, z);
                    l.getBlock().getState().update();
                }
            }
        }
    }

    public static void dropT34Pet(final Player owner, final Location loc) {
        owner.sendMessage(Sputnik.trans("&7&oYou dropped something, check around you..."));
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        final ArmorStand drop = (ArmorStand) owner.getWorld().spawn(loc.clone().add(0.0, -0.5, 0.0), (Class) ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.HIDDEN_USSR_T34_TANK_PET).getStack());
        drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Mini T-34 &e&ka"));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                owner.sendMessage(ChatColor.RED + "Oh no, you didn't picked up your T-34 pet, it despawned, good luck next time, F.");
                drop.remove();
            }
        }, 6000L);
        new BukkitRunnable() {
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
                final Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel(drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (final Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (e instanceof Player && e == owner) {
                        if (!Sputnik.isFullInv(owner)) {
                            drop.teleport(owner);
                            drop.remove();
                            SUtil.globalBroadcast(Sputnik.trans(""));
                            SUtil.globalBroadcast(Sputnik.trans("&c>&e>&b> &6&lWOW! &e" + owner.getDisplayName() + " &ehas obtained &7[Lvl 1] &6Mini T-34&e! &b<&e<&c<"));
                            SUtil.globalBroadcast(Sputnik.trans(""));
                            owner.sendMessage(Sputnik.trans("&6&lPET DROP! &eYou have obtained &7[Lvl 1] &6Mini T-34&e! &d&lGG&e!"));
                            owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1.0f, 0.1f);
                            owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                            owner.getInventory().addItem(SItem.of(SMaterial.HIDDEN_USSR_T34_TANK_PET).getStack());
                        } else {
                            SUtil.delay(() -> {
                                final Object val$drop = drop;
                                final Object val$owner = owner;
                                if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                                    owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                                    this.noticeable = false;
                                }
                            }, 20L);
                        }
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropEye(final Player owner, final Location loc, final int amount) {
        final ArmorStand drop = (ArmorStand) owner.getWorld().spawn(loc.clone().add(0.0, -0.7, 0.0), (Class) ArmorStand.class);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.REMNANT_OF_THE_EYE).getStack());
        drop.setCustomName(Sputnik.trans("&7" + amount + "x &9Ender's Fragment"));
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                drop.remove();
            }
        }, 2000L);
        new BukkitRunnable() {
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
                final Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel(drop, drop.getWorld(), owner);
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                for (final Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (e instanceof Player && e == owner) {
                        if (!Sputnik.isFullInv(owner)) {
                            drop.teleport(owner);
                            drop.remove();
                            owner.playSound(owner.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
                            owner.getWorld().playEffect(drop.getLocation(), Effect.LAVA_POP, 1);
                            for (int i = 0; i < amount; ++i) {
                                final SItem sitem = SItem.of(SMaterial.HIDDEN_VOID_FRAGMENT);
                                owner.getInventory().addItem(sitem.getStack());
                            }
                        } else {
                            SUtil.delay(() -> {
                                final Object val$drop = drop;
                                final Object val$owner = owner;
                                if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                                    owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                                    this.noticeable = false;
                                }
                            }, 20L);
                        }
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void dropGoldenTigerPet(final Player owner, final Location loc, final boolean isMythic) {
        owner.sendMessage(Sputnik.trans("&7&oYou dropped something, check around you..."));
        SUtil.sendTitle(owner, Sputnik.trans(""));
        SUtil.sendSubtitle(owner, Sputnik.trans("&6&oYou dropped something, check around!"));
        new BukkitRunnable() {
            public void run() {
                System.out.println("**A TIGER PET DROPPED!** Player `" + owner.getName() + "` dropped a Golden Tiger Pet at `" + owner.getWorld().getName() + "`, Mythic: " + isMythic);
            }
        }.runTaskAsynchronously(SkyBlock.getPlugin());
        owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 2.0f);
        final ArmorStand drop = (ArmorStand) owner.getWorld().spawn(loc.clone().add(0.0, -0.5, 0.0), (Class) ArmorStand.class);
        drop.getWorld().playEffect(drop.getLocation(), Effect.EXPLOSION_HUGE, 1);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(true);
        drop.setMetadata("ss_drop", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        drop.getEquipment().setHelmet(SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022).getStack());
        if (!isMythic) {
            drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &6Golden Tiger &e&ka"));
        } else {
            drop.setCustomName(Sputnik.trans("&e&ka&r &f1x &7[Lvl 1] &dGolden Tiger &e&ka"));
        }
        SUtil.delay(() -> {
            if (!drop.isDead()) {
                new BukkitRunnable() {

                    public void run() {
                        System.out.println("**PET DESPAWNED! ** Player `" + owner.getName() + "` let a Golden Tiger Pet at `" + owner.getWorld().getName() + "` despawned, Mythic: " + isMythic);
                    }
                }.runTaskAsynchronously(SkyBlock.getPlugin());
                owner.sendMessage(ChatColor.RED + "Oh no, you didn't picked up your Golden Tiger pet, it despawned, good luck next time, F.");
                drop.remove();
            }
        }, 6000L);
        new BukkitRunnable() {
            boolean noticeable = true;

            public void run() {
                if (owner.getWorld() != drop.getWorld()) {
                    new BukkitRunnable() {
                        public void run() {
                            System.out.println("**PET DESPAWNED! ** Player `" + owner.getName() + "` let a Golden Tiger Pet at `" + owner.getWorld().getName() + "` despawned, Mythic: " + isMythic);
                        }
                    }.runTaskAsynchronously(SkyBlock.getPlugin());
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
                final Location l = drop.getLocation();
                l.setYaw(l.getYaw() + 3.0f);
                drop.teleport(l);
                PacketInvoker.invokeChannel(drop, drop.getWorld(), owner);
                owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                if (isMythic) {
                    owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.FLYING_GLYPH, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                    owner.spigot().playEffect(drop.getLocation().add(0.0, 1.0, 0.0), Effect.WITCH_MAGIC, 0, 1, (float) SUtil.random(-1.0, 1.0), 1.0f, (float) SUtil.random(-1.0, 1.0), 0.0f, 1, 100);
                }
                if (!drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                    this.noticeable = true;
                }
                for (final Entity e : drop.getNearbyEntities(0.07, 0.07, 0.07)) {
                    if (e instanceof Player && e == owner) {
                        if (!Sputnik.isFullInv(owner)) {
                            drop.teleport(owner);
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
                            new BukkitRunnable() {
                                public void run() {
                                    System.out.println("**PET PICKUP!** Player `" + owner.getName() + "` picked up a Golden Tiger Pet at `" + owner.getWorld().getName() + "`, Mythic: " + isMythic);
                                }
                            }.runTaskAsynchronously(SkyBlock.getPlugin());
                            if (!isMythic) {
                                owner.getInventory().addItem(SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022).getStack());
                            } else {
                                final SItem sitem = SItem.of(SMaterial.HIDDEN_GOLDEN_TIGER_2022);
                                sitem.setRarity(Rarity.MYTHIC);
                                owner.getInventory().addItem(sitem.getStack());
                            }
                        } else {
                            SUtil.delay(() -> {
                                final Object val$drop = drop;
                                final Object val$owner = owner;
                                if (this.noticeable && drop.getNearbyEntities(0.07, 0.07, 0.07).contains(owner)) {
                                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                                    owner.sendMessage(ChatColor.RED + "Your inventory is full, clean it up!");
                                    this.noticeable = false;
                                }
                            }, 20L);
                        }
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public static void invokeChannel(final Entity as1, final World w, final Player owner) {
        final net.minecraft.server.v1_8_R3.Entity el = ((CraftEntity) as1).getHandle();
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(el.getId());
        for (final Entity e : w.getNearbyEntities(owner.getLocation(), 35.0, 35.0, 35.0)) {
            if (e instanceof Player) {
                final Player p = (Player) e;
                if (p == owner) {
                    continue;
                }
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
