/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.AttributeInstance
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.GenericAttributes
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.block.BlockFallAPI;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanFunction;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanHuman;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BigfootGiant
extends BaseZombie {
    private static LivingEntity e;
    private boolean shockWave = false;
    private boolean shockWaveCD = true;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&c&lBigfoot");
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 30000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        if (entity.getWorld().getPlayers().size() == 0) {
            return;
        }
        e = entity;
        ((CraftZombie)entity).setBaby(false);
        Player p2 = (Player)entity.getWorld().getPlayers().get(SUtil.random(0, entity.getWorld().getPlayers().size() - 1));
        if (p2 != null && p2.getGameMode() != GameMode.SPECTATOR && p2.getGameMode() != GameMode.CREATIVE) {
            ((CraftZombie)entity).setTarget((LivingEntity)p2);
        }
        AttributeInstance followRange = ((CraftLivingEntity)entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(500.0);
        SUtil.delay(() -> {
            this.shockWaveCD = false;
        }, 150L);
        Sputnik.applyPacketGiant((Entity)entity);
        EntityManager.DEFENSE_PERCENTAGE.put((Entity)entity, 25);
        entity.setMetadata("Giant_", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("highername", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (!BigfootGiant.this.shockWave && !BigfootGiant.this.shockWaveCD && SUtil.random(1, 100) <= 5) {
                    BigfootGiant.this.shockWaveCD = true;
                    BigfootGiant.this.shockWave = true;
                    Vector vec = new Vector(0, 0, 0);
                    vec.setY(2);
                    e.setVelocity(vec);
                    SUtil.delay(() -> {
                        LivingEntity val$entity = entity;
                        BigfootGiant.this.jumpAni(entity);
                    }, 10L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                EntityLiving nms = ((CraftLivingEntity)entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    Player target;
                    if (!(entities instanceof Player) || (target = (Player)entities).getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR || target.hasMetadata("NPC")) continue;
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().toVector().subtract(target.getLocation().toVector())));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer)players).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((net.minecraft.server.v1_8_R3.Entity)((CraftLivingEntity)entity).getHandle(), 0));
                    }
                    nms.r((net.minecraft.server.v1_8_R3.Entity)((CraftPlayer)target).getHandle());
                    break;
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 8L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(null, null, null, null, BigfootGiant.b(8991025, Material.LEATHER_BOOTS));
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        Sputnik.zero(killed);
        if (SadanHuman.SadanGiantsCount.containsKey(killed.getWorld().getUID())) {
            SadanHuman.SadanGiantsCount.put(killed.getWorld().getUID(), SadanHuman.SadanGiantsCount.get(killed.getWorld().getUID()) - 1);
        }
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
    public boolean isBaby() {
        return false;
    }

    @Override
    public double getXPDropped() {
        return 0.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.35;
    }

    public void jumpAni(final LivingEntity e2) {
        new BukkitRunnable(){

            public void run() {
                if (e2.isDead()) {
                    this.cancel();
                    return;
                }
                if (!BigfootGiant.this.shockWave) {
                    this.cancel();
                    return;
                }
                if (e2.isOnGround()) {
                    BigfootGiant.this.shockWave = false;
                    SUtil.delay(() -> BigfootGiant.this.shockWaveCD = false, 400L);
                    e2.getWorld().playEffect(e2.getLocation().add(0.0, 0.5, 0.0), Effect.EXPLOSION_HUGE, 3);
                    e2.getWorld().playEffect(e2.getLocation(), Effect.EXPLOSION_HUGE, 3);
                    e2.getWorld().playSound(e2.getLocation(), Sound.EXPLODE, 3.0f, 0.0f);
                    SUtil.delay(() -> {
                        LivingEntity val$e = e2;
                        e2.getWorld().playSound(e2.getLocation(), Sound.EXPLODE, 10.0f, 0.0f);
                    }, 5L);
                    for (Entity entities : e2.getNearbyEntities(15.0, 10.0, 15.0)) {
                        Player p2;
                        Vector vec = new Vector(0, 0, 0);
                        if (entities.hasMetadata("NPC") || entities instanceof ArmorStand || entities.hasMetadata("highername") || !(entities instanceof Player)) continue;
                        if (entities.getLocation().distance(e2.getLocation()) <= 5.0) {
                            p2 = (Player)entities;
                            p2.sendMessage(Sputnik.trans("&c&lBigfoot &chit you with &eStomp &cfor " + SUtil.commaify(Math.round(SadanFunction.dmgc(40000, p2, (Entity)e2).intValue())) + " &cdamage."));
                            continue;
                        }
                        p2 = (Player)entities;
                        p2.sendMessage(Sputnik.trans("&c&lBigfoot &chit you with &eStomp &cfor " + SUtil.commaify(Math.round(SadanFunction.dmgc(35000, p2, (Entity)e2).intValue())) + " &cdamage."));
                    }
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public static ItemStack buildColorStack(int hexcolor) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(Material.LEATHER_HELMET), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static ItemStack b(int hexcolor, Material m2) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m2), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static ItemStack c(Material m2) {
        ItemStack stack = new ItemStack(m2);
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
    }

    public static void applyEffect(PotionEffectType e2, Entity en, int ticks, int amp) {
        ((LivingEntity)en).addPotionEffect(new PotionEffect(e2, ticks, amp));
    }

    public static void createBlockTornado(Entity e2, Material mat, byte id) {
        for (int i2 = 0; i2 <= 30; ++i2) {
            int random = SUtil.random(0, 3);
            double range = 0.0;
            Location loc = e2.getLocation().clone();
            loc.setYaw((float)SUtil.random(0, 360));
            if (random == 1) {
                range = 0.6;
            }
            if (random == 2) {
                range = 0.7;
            }
            if (random == 3) {
                range = 0.8;
            }
            Vector vec = loc.getDirection().normalize().multiply(range);
            vec.setY(1.1);
            BlockFallAPI.sendVelocityBlock(e2.getLocation(), mat, id, e2.getWorld(), 70, vec);
        }
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e2, AtomicDouble damage) {
        e2.getEntity().getWorld().playSound(e2.getEntity().getLocation(), Sound.ZOMBIE_HURT, 1.0f, 0.0f);
    }
}

