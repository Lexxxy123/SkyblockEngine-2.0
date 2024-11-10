/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.libraryaddict.disguise.disguisetypes.PlayerDisguise
 *  net.minecraft.server.v1_8_R3.AttributeInstance
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.GenericAttributes
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.end;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.StaticWardenManager;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class VoidlingsWardenMob
extends BaseZombie {
    private ArmorStand tb;
    private long end;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&5\u2620 &d&lVoidlings Warden");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E9;
    }

    @Override
    public double getDamageDealt() {
        return 2200000.0;
    }

    public static ItemStack b(int hexcolor, Material m) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        this.end = System.currentTimeMillis() + 240000L;
        ((CraftZombie)entity).setBaby(false);
        this.tb = Sputnik.spawnDialougeBox((org.bukkit.entity.Entity)entity, 2.1);
        AttributeInstance followRange = ((CraftLivingEntity)entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        PlayerDisguise pl = Sputnik.applyPacketNPC((org.bukkit.entity.Entity)entity, "eyJ0aW1lc3RhbXAiOjE1ODY3NzE2NDQzMDksInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85YTUxOWY2ZmNiNTlkNDBjN2Y4ODgwOTI5NWIxMzlmYjMwMDI5NWFkNDUwZWY2ZThhNGUwZjBhY2NhMGRmYmRkIn19fQ==", "rqcOQovxdPBFIBxVHLmGk46I+tOKZ/KQYKdfgYy0+di1Do1Hz65iUwHkmhTSxq8rvYJSvhpT93nmT16n8/gaVI+AGFd4rxGoj3Q0u7AuR+E+y0XRn4VYIwAmkK6oA/cR5hdG5dHKeWS59ieKGKp44GH1LkW7MwhIqakKvlJT4zMh7mqlbm56wHBLrKZ12VONGcIdUG69Nfc+OqEnREzCe4yF8iOsW365m04NiyHL0vRzPZ+VLN4SqHjLwVcRhRDSosjzg0c+QV77tyexaSqdArPpgAkjDNOjgyKoUUuuj8GQGstd/ogAmTLK/pfKiI00Pb1giGvl1t5NadIGFw3sdCYNywUEToAsWEuq73Odiim2Gdz6pfhNTYdmDyG0lZjOPxk2VhtbCuA9WIg0jna1Kx9YJuzbe8EPbucM+zcPK7jITVHdOwtQT/nAmy+xaKqORtp2TGlxDvKY+YWO6ZSe/0Vw3ZWZfyE77rPvkK70phRRfB+iTV31UVrdZBZLe0i9VSTtZp4zs7dtgFSFJdoOmDTQik1DwEhA+Cmw4gLnolMU6hKXpcSKREBF7u7njW/PpmKBxWTVH4Z2nCU5VlXCugumvjfg9cKIBijkZlj4lGR6cYMiOJRU9gEEpcTEH0xrS+DMSAdE+sU4KZzLslLz6cqwlnZtkrW6jHQH+VnOCfQ=", true);
        pl.getWatcher().setRightClicking(false);
        entity.setMetadata("NoAffect", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("LD", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("VWE", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 85);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                EntityLiving nms = ((CraftLivingEntity)entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (org.bukkit.entity.Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    Player target;
                    if (!(entities instanceof Player) || (target = (Player)entities).getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR || target.hasMetadata("NPC") || target.getNoDamageTicks() == 7 || SUtil.random(0, 10) > 8) continue;
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().subtract(entities.getLocation()).toVector()));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer)players).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((Entity)((CraftLivingEntity)entity).getHandle(), 0));
                    }
                    nms.r((Entity)((CraftPlayer)target).getHandle());
                    break;
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 4L);
        LivingEntity e = entity;
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.FLAME, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 1.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 30L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (!entity.hasMetadata("owner")) {
                    this.cancel();
                    return;
                }
                if (Bukkit.getPlayer((UUID)UUID.fromString(((MetadataValue)entity.getMetadata("owner").get(0)).asString())) != null) {
                    Bukkit.getPlayer((UUID)UUID.fromString(((MetadataValue)entity.getMetadata("owner").get(0)).asString())).damage(VoidlingsWardenMob.this.getDamageDealt(), (org.bukkit.entity.Entity)entity);
                    return;
                }
                this.cancel();
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 20L, 20L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.FLYING_GLYPH, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 1.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.MAGIC_CRIT, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 1.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 10L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (((CraftZombie)entity).getTarget() != null) {
                    VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    SUtil.delay(() -> {
                        LivingEntity val$entity = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 20L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity2 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 40L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity3 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 60L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity4 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 80L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity5 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 100L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity6 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 120L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity7 = entity;
                        VoidlingsWardenMob.this.throwAxe((org.bukkit.entity.Entity)entity);
                    }, 140L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 300L, 300L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 10));
                for (int i = 0; i < 100; ++i) {
                    entity.getWorld().playEffect(entity.getLocation(), Effect.FLYING_GLYPH, 0);
                }
                VoidlingsWardenMob.this.activeGyroAbi((org.bukkit.entity.Entity)entity);
                SUtil.delay(() -> {
                    for (org.bukkit.entity.Entity e : entity.getNearbyEntities(2.0, 2.0, 2.0)) {
                        if (!(e instanceof Player)) continue;
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION_HUGE, 0);
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                        entity.getWorld().playEffect(entity.getLocation().clone().add(0.0, 0.6, 0.0), Effect.EXPLOSION, 0);
                        entity.getWorld().playSound(entity.getLocation(), Sound.EXPLODE, 2.0f, 0.0f);
                        Player p = (Player)e;
                        User.getUser(p.getUniqueId()).damage(p.getHealth() * 50.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e);
                        p.damage(1.0E-5);
                        User.getUser(p.getUniqueId()).send("&dVoidlings Warden&7's Gyrokinetic Explosion have hit you for &c" + SUtil.commaify(p.getHealth() * 50.0 / 100.0) + " &7true damage.");
                    }
                }, 35L);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 400L, 400L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (((CraftZombie)entity).getTarget() != null) {
                    Location lc = ((CraftZombie)entity).getTarget().getLocation();
                    ((CraftZombie)entity).getTarget().getWorld().playSound(((CraftZombie)entity).getTarget().getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    ((CraftZombie)entity).getHandle().setPositionRotation(lc.getX(), lc.getY(), lc.getZ(), lc.getYaw(), lc.getPitch());
                    for (int j = 0; j < 20; ++j) {
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.LARGE_SMOKE, 0, 1, (float)SUtil.random(-1, 1), (float)SUtil.random(-1, 2), (float)SUtil.random(-1, 1), 0.0f, 1, 20);
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.WITCH_MAGIC, 0, 1, (float)SUtil.random(-1, 1), (float)SUtil.random(-1, 2), (float)SUtil.random(-1, 1), 0.0f, 1, 20);
                    }
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 150L, 150L);
        new BukkitRunnable((org.bukkit.entity.Entity)e){
            float cout;
            final /* synthetic */ org.bukkit.entity.Entity val$e;
            {
                this.val$e = entity;
                this.cout = this.val$e.getLocation().getYaw();
            }

            public void run() {
                if (this.val$e.isDead()) {
                    VoidlingsWardenMob.this.tb.remove();
                    this.cancel();
                    return;
                }
                Location loc = this.val$e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(0.6));
                int hitshield = 100;
                int hitshieldmax = 100;
                int stage = 3;
                this.val$e.getWorld().spigot().playEffect(loc, Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.val$e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.6, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                if (stage >= 2) {
                    this.val$e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.2, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                }
                if (stage == 3) {
                    this.val$e.getWorld().spigot().playEffect(loc.clone().add(0.0, 1.8, 0.0), Effect.WITCH_MAGIC, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                }
                this.cout += 18.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable((org.bukkit.entity.Entity)e, entity){
            float cout;
            final /* synthetic */ org.bukkit.entity.Entity val$e;
            final /* synthetic */ LivingEntity val$entity;
            {
                this.val$e = entity;
                this.val$entity = livingEntity;
                this.cout = this.val$e.getLocation().getYaw();
            }

            public void run() {
                if (this.val$e.isDead()) {
                    this.cancel();
                    return;
                }
                if (System.currentTimeMillis() > VoidlingsWardenMob.this.end) {
                    this.val$entity.remove();
                    this.cancel();
                    return;
                }
                Location loc = this.val$e.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(3));
                int hitshield = 100;
                int hitshieldmax = 100;
                int stage = 3;
                this.val$e.getWorld().spigot().playEffect(loc.clone().add(0.0, 0.4, 0.0), Effect.FLYING_GLYPH, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
                this.cout += 8.0f;
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 1L, 1L);
    }

    public void cylinderReset(Location loc, int r) {
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

    public void activeGyroAbi(org.bukkit.entity.Entity e) {
        Location loc = e.getLocation().clone().add(0.0, -1.0, 0.0);
        this.gyroWand(loc, 8, 6);
        SUtil.delay(() -> this.gyroWand(loc, 8, 6), 5L);
        SUtil.delay(() -> this.gyroWand(loc, 6, 4), 10L);
        SUtil.delay(() -> this.gyroWand(loc, 4, 2), 15L);
        SUtil.delay(() -> this.gyroWand(loc, 3, 1), 20L);
        SUtil.delay(() -> this.gyroWand(loc, 2, 1), 25L);
        SUtil.delay(() -> this.gyroWand(loc, 1, 0), 30L);
        SUtil.delay(() -> this.cylinderReset(loc, 10), 32L);
        for (int i = 0; i < 40; ++i) {
            this.a(loc.clone().add(0.0, 0.3, 0.0), i * 12);
        }
        this.pullingPlayersTo(loc.clone().add(0.0, 1.0, 0.0));
    }

    public void pullingPlayersTo(final Location l) {
        for (final org.bukkit.entity.Entity entity : l.getWorld().getNearbyEntities(l, 8.0, 8.0, 8.0)) {
            if (!(entity instanceof Player)) continue;
            new BukkitRunnable(){
                Location look;
                int t;

                public void run() {
                    if (this.t >= 25) {
                        this.cancel();
                        return;
                    }
                    this.look = entity.getLocation().setDirection(l.toVector().subtract(entity.getLocation().toVector()));
                    this.look.add(this.look.getDirection().normalize().multiply(0.5));
                    if (entity.getLocation().distance(l) > 1.0) {
                        entity.getWorld().playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 2.0f);
                        Location nl = new Location(this.look.getWorld(), this.look.getX(), this.look.getY(), this.look.getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                        entity.teleport(nl);
                    }
                    ++this.t;
                }
            }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        }
    }

    public void gyroWand(Location l, int arg0, int arg1) {
        Material[] mat = new Material[]{Material.OBSIDIAN, Material.AIR, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.AIR};
        Material[] mat_r = new Material[]{Material.OBSIDIAN, Material.STAINED_GLASS, Material.STAINED_CLAY};
        List<Block> a = this.cylinder(l, arg0);
        List<Block> b = this.cylinder(l, arg1);
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

    public void a(final Location location, final float startYaw) {
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

    public List<Block> cylinder(Location loc, int r) {
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

    public void throwAxe(final org.bukkit.entity.Entity e) {
        final Vector throwVec = e.getLocation().add(e.getLocation().getDirection().multiply(10)).toVector().subtract(e.getLocation().toVector()).normalize().multiply(1.2);
        Location throwLoc = e.getLocation().add(0.0, 0.5, 0.0);
        final ArmorStand armorStand1 = (ArmorStand)e.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setItemInHand(SItem.of(SMaterial.AXE_OF_THE_SHREDDED).getStack());
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        armorStand1.setMarker(true);
        final Vector teleportTo = e.getLocation().getDirection().normalize().multiply(1);
        final Vector[] previousVector = new Vector[]{throwVec};
        new BukkitRunnable(){
            private int run = -1;

            public void run() {
                boolean bl;
                int angle;
                Vector newVector;
                int i = 0;
                int ran = 0;
                int num = 90;
                Object loc = null;
                ++this.run;
                if (this.run > 100) {
                    this.cancel();
                    return;
                }
                for (int j = 0; j < 7; ++j) {
                    armorStand1.getWorld().spigot().playEffect(armorStand1.getLocation().clone().add(0.0, 1.75, 0.0), Effect.MAGIC_CRIT, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 0.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 100);
                }
                Location locof = armorStand1.getLocation();
                locof.setY(locof.getY() + 1.0);
                if (locof.getBlock().getType() != Material.AIR) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                double xPos = armorStand1.getRightArmPose().getX();
                armorStand1.setRightArmPose(new EulerAngle(xPos + 0.7, 0.0, 0.0));
                previousVector[0] = newVector = new Vector(throwVec.getX(), previousVector[0].getY() - 0.03, throwVec.getZ());
                armorStand1.setVelocity(newVector);
                if (i < 13) {
                    angle = i * 20 + 90;
                    bl = false;
                } else {
                    angle = i * 20 - 90;
                    bl = true;
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
                for (org.bukkit.entity.Entity en : armorStand1.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (!(en instanceof Player)) continue;
                    Player p = (Player)en;
                    p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                    User.getUser(p.getUniqueId()).damage(p.getMaxHealth() * 8.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e);
                    User.getUser(p.getUniqueId()).send("&dVoidlings Warden&7's Axe have hit you for &c" + SUtil.commaify(p.getMaxHealth() * 8.0 / 100.0) + " &7true damage.");
                    p.damage(1.0E-5);
                    armorStand1.remove();
                    this.cancel();
                    break;
                }
            }

            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 2L);
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
    public void onDamage(SEntity sEntity, org.bukkit.entity.Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        LivingEntity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> VoidlingsWardenMob.lambda$onDamage$7((org.bukkit.entity.Entity)en, v), 1L);
    }

    @Override
    public double getXPDropped() {
        return 6942.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.25;
    }

    public void say(String str) {
        if (str == null) {
            this.tb.setCustomNameVisible(false);
            return;
        }
        this.tb.setCustomNameVisible(true);
        this.tb.setCustomName(Sputnik.trans(str));
    }

    @Override
    public void onDeath(final SEntity sEntity, final org.bukkit.entity.Entity killed, org.bukkit.entity.Entity damager) {
        int place;
        final HashMap<UUID, List<Location>> eyes = new HashMap<UUID, List<Location>>(StaticWardenManager.EYES);
        SUtil.delay(() -> StaticWardenManager.endFight(), 500L);
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\n");
        message.append(ChatColor.GOLD).append(ChatColor.BOLD).append("                 ").append(sEntity.getStatistics().getEntityName().toUpperCase()).append(" DOWN!\n \n");
        final ArrayList<Map.Entry<UUID, Double>> damageDealt = new ArrayList<Map.Entry<UUID, Double>>(sEntity.getDamageDealt().entrySet());
        damageDealt.sort(Map.Entry.comparingByValue());
        Collections.reverse(damageDealt);
        String name = null;
        if (damager instanceof Player) {
            name = damager.getName();
        }
        if (damager instanceof Arrow && ((Arrow)damager).getShooter() instanceof Player) {
            name = ((Player)((Arrow)damager).getShooter()).getName();
        }
        if (null != name) {
            message.append("            ").append(ChatColor.GREEN).append(name).append(ChatColor.GRAY).append(" dealt the final blow.\n \n");
        }
        for (int i = 0; i < Math.min(3, damageDealt.size()); ++i) {
            message.append("\n");
            Map.Entry d = (Map.Entry)damageDealt.get(i);
            place = i + 1;
            switch (place) {
                case 1: {
                    message.append("        ").append(ChatColor.YELLOW);
                    break;
                }
                case 2: {
                    message.append("        ").append(ChatColor.GOLD);
                    break;
                }
                case 3: {
                    message.append("        ").append(ChatColor.RED);
                }
            }
            message.append(ChatColor.BOLD).append(place);
            switch (place) {
                case 1: {
                    message.append("st");
                    break;
                }
                case 2: {
                    message.append("nd");
                    break;
                }
                case 3: {
                    message.append("rd");
                }
            }
            message.append(" Damager").append(ChatColor.RESET).append(ChatColor.GRAY).append(" - ").append(ChatColor.GREEN).append(Bukkit.getOfflinePlayer((UUID)((UUID)d.getKey())).getName()).append(ChatColor.GRAY).append(" - ").append(ChatColor.YELLOW).append(SUtil.commaify(((Double)d.getValue()).intValue()));
        }
        message.append("\n \n").append("         ").append(ChatColor.RESET).append(ChatColor.YELLOW).append("Your Damage: ").append("%s").append(ChatColor.RESET).append("\n").append("             ").append(ChatColor.YELLOW).append("Runecrafting Experience: ").append(ChatColor.LIGHT_PURPLE).append("N/A").append(ChatColor.RESET).append("\n \n");
        message.append(ChatColor.GREEN).append(ChatColor.BOLD).append("\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac");
        for (Player player : Bukkit.getOnlinePlayers()) {
            place = -1;
            int damage = 0;
            for (int j = 0; j < damageDealt.size(); ++j) {
                Map.Entry d2 = (Map.Entry)damageDealt.get(j);
                if (!((UUID)d2.getKey()).equals(player.getUniqueId())) continue;
                place = j + 1;
                damage = ((Double)d2.getValue()).intValue();
            }
            if (!player.getWorld().getName().equals("world")) continue;
            player.sendMessage(String.format(message.toString(), -1 != place ? ChatColor.GREEN + SUtil.commaify(damage) + ChatColor.GRAY + " (Position #" + place + ")" : ChatColor.RED + "N/A" + ChatColor.GRAY + " (Position #N/A)"));
        }
        new BukkitRunnable(){

            public void run() {
                for (int i = 0; i < damageDealt.size(); ++i) {
                    Map.Entry d = (Map.Entry)damageDealt.get(i);
                    Player player = Bukkit.getPlayer((UUID)((UUID)d.getKey()));
                    if (null == player) continue;
                    int weight = 0;
                    if (eyes.containsKey(player.getUniqueId())) {
                        weight += Math.min(400, ((List)eyes.get(player.getUniqueId())).size() * 100);
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
                    ArrayList possibleMajorDrops = new ArrayList();
                    SEntityType type = sEntity.getSpecType();
                    SUtil.addIf(new Drop(SMaterial.HIDDEN_VOIDLINGS_PET, 500), possibleMajorDrops, 500 <= weight);
                    SUtil.addIf(new Drop(SMaterial.HIDDEN_ETHERWARP_CONDUIT, 250), possibleMajorDrops, 250 <= weight);
                    SUtil.addIf(new Drop(SMaterial.JUDGEMENT_CORE, 350), possibleMajorDrops, 350 <= weight);
                    SUtil.addIf(new Drop(SMaterial.HIDDEN_QUANTUMFLUX_POWER_ORB, 450), possibleMajorDrops, 450 <= weight);
                    int remainingWeight = weight;
                    if (0 < possibleMajorDrops.size()) {
                        Drop drop = (Drop)possibleMajorDrops.get(SUtil.random(0, possibleMajorDrops.size() - 1));
                        SMaterial majorDrop = drop.getMaterial();
                        remainingWeight -= drop.getWeight();
                        if (null != majorDrop) {
                            Item item;
                            SItem sItem = SItem.of(majorDrop);
                            if (!sItem.getFullName().equals("\u00a76Voidling Destroyer")) {
                                item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                item.setMetadata("obtained", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
                                item.setCustomNameVisible(true);
                                item.setCustomName(item.getItemStack().getAmount() + "x " + sItem.getFullName());
                            } else {
                                item = SUtil.spawnPersonalItem(sItem.getStack(), killed.getLocation(), player);
                                item.setMetadata("obtained", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
                                item.setCustomNameVisible(true);
                                item.setCustomName(item.getItemStack().getAmount() + "x " + ChatColor.GRAY + "[Lvl 1] " + sItem.getFullName());
                            }
                        }
                    }
                    ArrayList<Drop> minorDrops = new ArrayList<Drop>(Arrays.asList(new Drop(SMaterial.ENDER_PEARL, 0), new Drop(SMaterial.ENCHANTED_ENDER_PEARL, 0)));
                    SUtil.addIf(new Drop(SMaterial.HIDDEN_DEMONS_PEARL, 22), minorDrops, 22 <= weight);
                }
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 200L);
    }

    @Override
    public List<EntityDrop> drops() {
        ArrayList<EntityDrop> drops = new ArrayList<EntityDrop>();
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
        drops.add(new EntityDrop(SMaterial.HIDDEN_SHARD_VOID, EntityDropType.RARE, 1.0));
        return drops;
    }

    private static /* synthetic */ void lambda$onDamage$7(org.bukkit.entity.Entity en, Vector v) {
        en.setVelocity(v);
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

