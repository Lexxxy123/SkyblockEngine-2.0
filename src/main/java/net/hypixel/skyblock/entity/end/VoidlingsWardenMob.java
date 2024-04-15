package net.hypixel.skyblock.entity.end;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.*;
import net.hypixel.skyblock.entity.dragon.Dragon;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.api.protocol.PacketInvoker;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.*;

public class VoidlingsWardenMob extends BaseZombie {
    private ArmorStand tb;
    private long end;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&5☠ &d&lVoidlings Warden");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E9;
    }

    @Override
    public double getDamageDealt() {
        return 2200000.0;
    }

    public static ItemStack b(final int hexcolor, final Material m) {
        final ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m), Color.fromRGB(hexcolor));
        final ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        this.end = System.currentTimeMillis() + 240000L;
        ((CraftZombie) entity).setBaby(false);
        this.tb = Sputnik.spawnDialougeBox(entity, 2.1);
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "eyJ0aW1lc3RhbXAiOjE1ODY3NzE2NDQzMDksInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YTUxOWY2ZmNiNTlkNDBjN2Y4ODgwOTI5NWIxMzlmYjMwMDI5NWFkNDUwZWY2ZThhNGUwZjBhY2NhMGRmYmRkIn19fQ==", "rqcOQovxdPBFIBxVHLmGk46I+tOKZ/KQYKdfgYy0+di1Do1Hz65iUwHkmhTSxq8rvYJSvhpT93nmT16n8/gaVI+AGFd4rxGoj3Q0u7AuR+E+y0XRn4VYIwAmkK6oA/cR5hdG5dHKeWS59ieKGKp44GH1LkW7MwhIqakKvlJT4zMh7mqlbm56wHBLrKZ12VONGcIdUG69Nfc+OqEnREzCe4yF8iOsW365m04NiyHL0vRzPZ+VLN4SqHjLwVcRhRDSosjzg0c+QV77tyexaSqdArPpgAkjDNOjgyKoUUuuj8GQGstd/ogAmTLK/pfKiI00Pb1giGvl1t5NadIGFw3sdCYNywUEToAsWEuq73Odiim2Gdz6pfhNTYdmDyG0lZjOPxk2VhtbCuA9WIg0jna1Kx9YJuzbe8EPbucM+zcPK7jITVHdOwtQT/nAmy+xaKqORtp2TGlxDvKY+YWO6ZSe/0Vw3ZWZfyE77rPvkK70phRRfB+iTV31UVrdZBZLe0i9VSTtZp4zs7dtgFSFJdoOmDTQik1DwEhA+Cmw4gLnolMU6hKXpcSKREBF7u7njW/PpmKBxWTVH4Z2nCU5VlXCugumvjfg9cKIBijkZlj4lGR6cYMiOJRU9gEEpcTEH0xrS+DMSAdE+sU4KZzLslLz6cqwlnZtkrW6jHQH+VnOCfQ=", true);
        pl.getWatcher().setRightClicking(false);
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("VWE", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 85);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (final Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (!(entities instanceof Player)) {
                        continue;
                    }
                    final Player target = (Player) entities;
                    if (target.getGameMode() == GameMode.CREATIVE) {
                        continue;
                    }
                    if (target.getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }
                    if (target.hasMetadata("NPC")) {
                        continue;
                    }
                    if (target.getNoDamageTicks() == 7) {
                        continue;
                    }
                    if (SUtil.random(0, 10) > 8) {
                        continue;
                    }
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().subtract(entities.getLocation()).toVector()));
                    for (final Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) players).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) entity).getHandle(), 0));
                    }
                    nms.r(((CraftPlayer) target).getHandle());
                    break;
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 4L);
        final Entity e = entity;
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.FLAME, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 1.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 30L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (!entity.hasMetadata("owner")) {
                    this.cancel();
                    return;
                }
                if (Bukkit.getPlayer(UUID.fromString(entity.getMetadata("owner").get(0).asString())) != null) {
                    Bukkit.getPlayer(UUID.fromString(entity.getMetadata("owner").get(0).asString())).damage(VoidlingsWardenMob.this.getDamageDealt(), entity);
                    return;
                }
                this.cancel();
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 20L, 20L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.FLYING_GLYPH, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 1.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.MAGIC_CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 1.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 10L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (((CraftZombie) entity).getTarget() != null) {
                    VoidlingsWardenMob.this.throwAxe(entity);
                    SUtil.delay(() -> {
                        final Object val$entity = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 20L);
                    SUtil.delay(() -> {
                        final Object val$entity2 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 40L);
                    SUtil.delay(() -> {
                        final Object val$entity3 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 60L);
                    SUtil.delay(() -> {
                        final Object val$entity4 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 80L);
                    SUtil.delay(() -> {
                        final Object val$entity5 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 100L);
                    SUtil.delay(() -> {
                        final Object val$entity6 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 120L);
                    SUtil.delay(() -> {
                        final Object val$entity7 = entity;
                        VoidlingsWardenMob.this.throwAxe(entity);
                    }, 140L);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 300L, 300L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 10));
                for (int i = 0; i < 100; ++i) {
                    entity.getWorld().playEffect(entity.getLocation(), Effect.FLYING_GLYPH, 0);
                }
                VoidlingsWardenMob.this.activeGyroAbi(entity);
                SUtil.delay(() -> {
                    final Iterator<Entity> iterator = entity.getNearbyEntities(2.0, 2.0, 2.0).iterator();
                    while (iterator.hasNext()) {
                        final Entity e = iterator.next();
                        if (!(e instanceof Player)) {
                            continue;
                        } else {
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                            entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                            entity.getWorld().playSound(entity.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
                            final Player p = (Player) e;
                            User.getUser(p.getUniqueId()).damage(p.getHealth() * 50.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e);
                            p.damage(1.0E-5);
                            User.getUser(p.getUniqueId()).send("&dVoidlings Warden&7's Gyrokinetic Explosion have hit you for &c" + SUtil.commaify(p.getHealth() * 50.0 / 100.0) + " &7true damage.");
                        }
                    }
                }, 35L);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 400L, 400L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (((CraftZombie) entity).getTarget() != null) {
                    final Location lc = ((CraftZombie) entity).getTarget().getLocation();
                    ((CraftZombie) entity).getTarget().getWorld().playSound(((CraftZombie) entity).getTarget().getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    ((CraftZombie) entity).getHandle().setPositionRotation(lc.getX(), lc.getY(), lc.getZ(), lc.getYaw(), lc.getPitch());
                    for (int j = 0; j < 20; ++j) {
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.LARGE_SMOKE, 0, 1, (float) SUtil.random(-1, 1), (float) SUtil.random(-1, 2), (float) SUtil.random(-1, 1), 0.0f, 1, 20);
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.WITCH_MAGIC, 0, 1, (float) SUtil.random(-1, 1), (float) SUtil.random(-1, 2), (float) SUtil.random(-1, 1), 0.0f, 1, 20);
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 150L, 150L);
        new BukkitRunnable() {
            float cout = e.getLocation().getYaw();

            public void run() {
                if (e.isDead()) {
                    VoidlingsWardenMob.this.tb.remove();
                    this.cancel();
                    return;
                }
                final Location loc = e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.6));
                final int hitshield = 100;
                final int hitshieldmax = 100;
                int stage = 3;
                if (hitshield <= hitshieldmax / 2 && hitshield > hitshieldmax * 25 / 100) {
                    stage = 2;
                } else if (hitshield <= hitshieldmax * 25 / 100 && hitshield != 1) {
                    stage = 1;
                } else if (hitshield == 1) {
                    stage = 1;
                }
                e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                if (stage >= 2) {
                    e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                }
                if (stage == 3) {
                    e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                }
                this.cout += 18.0f;
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            float cout = e.getLocation().getYaw();

            public void run() {
                if (e.isDead()) {
                    this.cancel();
                    return;
                }
                if (System.currentTimeMillis() > VoidlingsWardenMob.this.end) {
                    entity.remove();
                    this.cancel();
                    return;
                }
                final Location loc = e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(3));
                final int hitshield = 100;
                final int hitshieldmax = 100;
                int stage = 3;
                if (hitshield <= hitshieldmax / 2 && hitshield > hitshieldmax * 25 / 100) {
                    stage = 2;
                } else if (hitshield <= hitshieldmax * 25 / 100 && hitshield != 1) {
                    stage = 1;
                } else if (hitshield == 1) {
                    stage = 1;
                }
                e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.4, 0.0), Effect.FLYING_GLYPH, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 8.0f;
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 1L, 1L);
    }

    public void cylinderReset(final Location loc, final int r) {
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

    public void activeGyroAbi(final Entity e) {
        final Location loc = e.getLocation().clone().add(0.0, -1.0, 0.0);
        this.gyroWand(loc, 8, 6);
        SUtil.delay(() -> this.gyroWand(loc, 8, 6), 5L);
        SUtil.delay(() -> this.gyroWand(loc, 6, 4), 10L);
        SUtil.delay(() -> this.gyroWand(loc, 4, 2), 15L);
        SUtil.delay(() -> this.gyroWand(loc, 3, 1), 20L);
        SUtil.delay(() -> this.gyroWand(loc, 2, 1), 25L);
        SUtil.delay(() -> this.gyroWand(loc, 1, 0), 30L);
        SUtil.delay(() -> this.cylinderReset(loc, 10), 32L);
        for (int i = 0; i < 40; ++i) {
            this.a(loc.clone().add(0.0, 0.3, 0.0), (float) (i * 12));
        }
        this.pullingPlayersTo(loc.clone().add(0.0, 1.0, 0.0));
    }

    public void pullingPlayersTo(final Location l) {
        for (final Entity entity : l.getWorld().getNearbyEntities(l, 8.0, 8.0, 8.0)) {
            if (!(entity instanceof Player)) {
                continue;
            }
            new BukkitRunnable() {
                Location look;
                int t;

                public void run() {
                    if (this.t >= 25) {
                        this.cancel();
                        return;
                    }
                    (this.look = entity.getLocation().setDirection(l.toVector().subtract(entity.getLocation().toVector()))).add(this.look.getDirection().normalize().multiply(0.5));
                    if (entity.getLocation().distance(l) > 1.0) {
                        entity.getWorld().playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 2.0f);
                        final Location nl = new Location(this.look.getWorld(), this.look.getX(), this.look.getY(), this.look.getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                        entity.teleport(nl);
                    }
                    ++this.t;
                }
            }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
        }
    }

    public void gyroWand(final Location l, final int arg0, final int arg1) {
        final Material[] mat = {Material.OBSIDIAN, Material.AIR, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.AIR};
        final Material[] mat_r = {Material.OBSIDIAN, Material.STAINED_GLASS, Material.STAINED_CLAY};
        final List<Block> a = this.cylinder(l, arg0);
        final List<Block> b = this.cylinder(l, arg1);
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

    public void a(final Location location, final float startYaw) {
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

    public List<Block> cylinder(final Location loc, final int r) {
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

    public void throwAxe(final Entity e) {
        final Vector throwVec = e.getLocation().add(e.getLocation().getDirection().multiply(10)).toVector().subtract(e.getLocation().toVector()).normalize().multiply(1.2);
        final Location throwLoc = e.getLocation().add(0.0, 0.5, 0.0);
        final ArmorStand armorStand1 = (ArmorStand) e.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setItemInHand(SItem.of(SMaterial.AXE_OF_THE_SHREDDED).getStack());
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        armorStand1.setMarker(true);
        final Vector teleportTo = e.getLocation().getDirection().normalize().multiply(1);
        final Vector[] previousVector = {throwVec};
        new BukkitRunnable() {
            private int run = -1;

            public void run() {
                final int i;
                final int ran = i = 0;
                final int num = 90;
                final Location loc = null;
                ++this.run;
                if (this.run > 100) {
                    this.cancel();
                    return;
                }
                for (int j = 0; j < 7; ++j) {
                    armorStand1.getWorld().spigot().playEffect(armorStand1.getLocation().clone().add(0.0, 1.75, 0.0), Effect.MAGIC_CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 100);
                }
                final Location locof = armorStand1.getLocation();
                locof.setY(locof.getY() + 1.0);
                if (locof.getBlock().getType() != Material.AIR) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                final double xPos = armorStand1.getRightArmPose().getX();
                armorStand1.setRightArmPose(new EulerAngle(xPos + 0.7, 0.0, 0.0));
                final Vector newVector = new Vector(throwVec.getX(), previousVector[0].getY() - 0.03, throwVec.getZ());
                previousVector[0] = newVector;
                armorStand1.setVelocity(newVector);
                if (i < 13) {
                    final int angle = i * 20 + num;
                    final boolean back = false;
                } else {
                    final int angle = i * 20 - num;
                    final boolean back = true;
                }
                if (locof.getBlock().getType() != Material.AIR && locof.getBlock().getType() != Material.WATER) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (i % 2 == 0 && i < 13) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (i % 2 == 0) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                for (final Entity en : armorStand1.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (en instanceof Player) {
                        final Player p = (Player) en;
                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                        User.getUser(p.getUniqueId()).damage(p.getMaxHealth() * 8.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e);
                        User.getUser(p.getUniqueId()).send("&dVoidlings Warden&7's Axe have hit you for &c" + SUtil.commaify(p.getMaxHealth() * 8.0 / 100.0) + " &7true damage.");
                        p.damage(1.0E-5);
                        armorStand1.remove();
                        this.cancel();
                        break;
                    }
                }
            }

            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SItem.of(SMaterial.HIDDEN_ETHERWARP_TRANSCODER).getStack(), null, null, null, null);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean hasNameTag() {
        return false;
    }

    @Override
    public boolean isVillager() {
        return false;
    }

    @Override
    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
        final Entity en = sEntity.getEntity();
        final Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public double getXPDropped() {
        return 6942.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.25;
    }

    public void say(final String str) {
        if (str == null) {
            this.tb.setCustomNameVisible(false);
            return;
        }
        this.tb.setCustomNameVisible(true);
        this.tb.setCustomName(Sputnik.trans(str));
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        Map<UUID, List<Location>> eyes = new HashMap<UUID, List<Location>>(StaticWardenManager.EYES);
        SUtil.delay(() -> StaticWardenManager.endFight(), 500L);
        StringBuilder message = new StringBuilder();
        message.append(org.bukkit.ChatColor.GREEN).append(org.bukkit.ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n");
        message.append(org.bukkit.ChatColor.GOLD).append(org.bukkit.ChatColor.BOLD).append("                 ").append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        List<Map.Entry<UUID, Double>> damageDealt = new ArrayList<Map.Entry<UUID, Double>>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort((Comparator<? super Map.Entry<UUID, Double>>) Map.Entry.<Object, Comparable>comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player) {
            name = damager.getName();
        }
        if (damager instanceof Arrow && ((Arrow) damager).getShooter() instanceof Player) {
            name = ((Player) ((Arrow) damager).getShooter()).getName();
        }
        if (null != name) {
            message.append("            ").append(org.bukkit.ChatColor.GREEN).append(name).append(org.bukkit.ChatColor.GRAY).append(" dealt the final blow.\n \n");
        }
        for (int i = 0; i < Math.min(3, damageDealt.size()); ++i) {
            message.append("\n");
            Map.Entry<UUID, Double> d = damageDealt.get(i);
            int place = i + 1;
            switch (place) {
                case 1:
                    message.append("        ").append(org.bukkit.ChatColor.YELLOW);
                    break;
                case 2:
                    message.append("        ").append(org.bukkit.ChatColor.GOLD);
                    break;
                case 3:
                    message.append("        ").append(org.bukkit.ChatColor.RED);
                    break;
            }
            message.append(org.bukkit.ChatColor.BOLD).append(place);
            switch (place) {
                case 1:
                    message.append("st");
                    break;
                case 2:
                    message.append("nd");
                    break;
                case 3:
                    message.append("rd");
                    break;
            }
            message.append(" Damager").append(org.bukkit.ChatColor.RESET).append(org.bukkit.ChatColor.GRAY).append(" - ").append(org.bukkit.ChatColor.GREEN).append(Bukkit.getOfflinePlayer(d.getKey()).getName()).append(org.bukkit.ChatColor.GRAY).append(" - ").append(org.bukkit.ChatColor.YELLOW).append(SUtil.commaify(d.getValue().intValue()));
        }
        message.append("\n \n").append("         ").append(org.bukkit.ChatColor.RESET).append(org.bukkit.ChatColor.YELLOW).append("Your Damage: ").append("%s").append(org.bukkit.ChatColor.RESET).append("\n").append("             ").append(org.bukkit.ChatColor.YELLOW).append("Runecrafting Experience: ").append(org.bukkit.ChatColor.LIGHT_PURPLE).append("N/A").append(org.bukkit.ChatColor.RESET).append("\n \n");
        message.append(org.bukkit.ChatColor.GREEN).append(org.bukkit.ChatColor.BOLD).append("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        for (Player player : Bukkit.getOnlinePlayers()) {
            int place = -1;
            int damage = 0;
            for (int j = 0; j < damageDealt.size(); ++j) {
                Map.Entry<UUID, Double> d2 = damageDealt.get(j);
                if (d2.getKey().equals(player.getUniqueId())) {
                    place = j + 1;
                    damage = d2.getValue().intValue();
                }
            }
            if (player.getWorld().getName().equals("world")) {
                player.sendMessage(String.format(message.toString(), (-1 != place) ? (org.bukkit.ChatColor.GREEN + SUtil.commaify(damage) + org.bukkit.ChatColor.GRAY + " (Position #" + place + ")") : (org.bukkit.ChatColor.RED + "N/A" + org.bukkit.ChatColor.GRAY + " (Position #N/A)")));
            }
        }
        new BukkitRunnable() {
            public void run() {
                for (int i = 0; i < damageDealt.size(); ++i) {
                    Map.Entry<UUID, Double> d = damageDealt.get(i);
                    Player player = Bukkit.getPlayer(d.getKey());
                    if (null != player) {
                        int weight = 0;
                        if (eyes.containsKey(player.getUniqueId())) {
                            weight += Math.min(400, eyes.get(player.getUniqueId()).size() * 100);
                        }
                        if (0 == i) {
                            weight += 500;
                        }
                        if (1 == i) {
                            weight += 450;
                        }
                        if (2 == i) {
                            weight += 500;
                        }
                        if (3 <= i && 6 >= i) {
                            weight += 325;
                        }
                        if (7 <= i && 14 >= i) {
                            weight += 200;
                        }
                        if (15 <= i) {
                            weight += 175;
                        }
                        List<VoidlingsWardenMob.Drop> possibleMajorDrops = new ArrayList<VoidlingsWardenMob.Drop>();
                        SEntityType type = sEntity.getSpecType();
                        SUtil.addIf(new VoidlingsWardenMob.Drop(SMaterial.HIDDEN_VOIDLINGS_PET, 500), possibleMajorDrops, 500 <= weight);
                        SUtil.addIf(new VoidlingsWardenMob.Drop(SMaterial.HIDDEN_ETHERWARP_CONDUIT, 250), possibleMajorDrops, 250 <= weight);
                        SUtil.addIf(new VoidlingsWardenMob.Drop(SMaterial.JUDGEMENT_CORE, 350), possibleMajorDrops, 350 <= weight);
                        SUtil.addIf(new VoidlingsWardenMob.Drop(SMaterial.HIDDEN_QUANTUMFLUX_POWER_ORB, 450), possibleMajorDrops, 450 <= weight);
                        int remainingWeight = weight;
                        if (0 < possibleMajorDrops.size()) {
                            VoidlingsWardenMob.Drop drop = possibleMajorDrops.get(SUtil.random(0, possibleMajorDrops.size() - 1));
                            SMaterial majorDrop = drop.getMaterial();
                            remainingWeight -= drop.getWeight();
                            if (null != majorDrop) {
                                SItem sItem = SItem.of(majorDrop);
                                if (!sItem.getFullName().equals("§6Voidling Destroyer")) {
                                    Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(SkyBlock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                                } else {
                                    Item item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                    item.setMetadata("obtained", new FixedMetadataValue(SkyBlock.getPlugin(), true));
                                    item.setCustomNameVisible(true);
                                    item.setCustomName(item.getItemStack().getAmount() + "x " + org.bukkit.ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName());
                                }
                            }
                        }
                        List<VoidlingsWardenMob.Drop> minorDrops = new ArrayList<VoidlingsWardenMob.Drop>(Arrays.asList(new VoidlingsWardenMob.Drop(SMaterial.ENDER_PEARL, 0), new VoidlingsWardenMob.Drop(SMaterial.ENCHANTED_ENDER_PEARL, 0)));
                        SUtil.addIf(new VoidlingsWardenMob.Drop(SMaterial.HIDDEN_DEMONS_PEARL, 22), minorDrops, 22 <= weight);
                    }
                }
            }
        }.runTaskLater(SkyBlock.getPlugin(), 200L);
    }

    public List<EntityDrop> drops() {
        List<EntityDrop> drops = new ArrayList<EntityDrop>();
        int revFlesh = SUtil.random(50, 64);
        drops.add(new EntityDrop(SUtil.setStackAmount(SItem.of(SMaterial.NULL_SPHERE).getStack(), revFlesh), EntityDropType.GUARANTEED, 1.0));
            SItem endBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            endBook.addEnchantment(EnchantmentType.ENDER_SLAYER, 15);
            drops.add(new EntityDrop(endBook.getStack(), EntityDropType.CRAZY_RARE, 0.002));
            SItem legiBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            legiBook.addEnchantment(EnchantmentType.LEGION, SUtil.random(1, 2));
            drops.add(new EntityDrop(legiBook.getStack(), EntityDropType.INSANE_RARE, 0.00125));
            SItem fatalBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            fatalBook.addEnchantment(EnchantmentType.FATAL_TEMPO, 1);
            drops.add(new EntityDrop(fatalBook.getStack(), EntityDropType.CRAZY_RARE, 4.0E-4));
            drops.add(new EntityDrop(SMaterial.HIDDEN_DEMONS_PEARL, EntityDropType.RARE, 0.5));
            drops.add(new EntityDrop(SMaterial.HIDDEN_SHARD_VOID,EntityDropType.RARE, 1));
        return drops;
    }

    private static class Drop {
        private final SMaterial material;
        private final SMaterial.VagueEntityMaterial vagueEntityMaterial;
        private final int weight;

        public Drop(SMaterial material, int weight) {
            this.material = material;
            this.vagueEntityMaterial = null;
            this.weight = weight;
        }

        public Drop(SMaterial.VagueEntityMaterial material, int weight, SEntityType type) {
            this.material = material.getEntityArmorPiece(type);
            this.vagueEntityMaterial = material;
            this.weight = weight;
        }

        public SMaterial getMaterial() {
            return this.material;
        }

        public SMaterial.VagueEntityMaterial getVagueEntityMaterial() {
            return this.vagueEntityMaterial;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
