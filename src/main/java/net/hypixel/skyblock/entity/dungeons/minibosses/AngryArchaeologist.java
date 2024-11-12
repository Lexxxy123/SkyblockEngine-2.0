/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.slikey.effectlib.effect.ConeEffect
 *  de.slikey.effectlib.util.ParticleEffect
 *  me.libraryaddict.disguise.disguisetypes.PlayerDisguise
 *  me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher
 *  net.minecraft.server.v1_8_R3.AttributeInstance
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityZombie
 *  net.minecraft.server.v1_8_R3.GenericAttributes
 *  net.minecraft.server.v1_8_R3.MathHelper
 *  net.minecraft.server.v1_8_R3.MobEffectList
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport
 *  org.bukkit.Bukkit
 *  org.bukkit.Effect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.ThrownPotion
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.dungeons.minibosses;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.ParticleEffect;
import java.util.Random;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class AngryArchaeologist
extends BaseZombie {
    private boolean isSplashing = false;
    private boolean isBowing = false;
    private boolean startedBattle = false;
    private boolean SplashCooldown = false;
    private boolean s = false;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&d&lAngry Archaeologist");
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.5E8;
    }

    @Override
    public double getDamageDealt() {
        return 5300000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        ((CraftZombie)entity).setBaby(false);
        AttributeInstance followRange = ((CraftLivingEntity)entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        PlayerDisguise pl = Sputnik.applyPacketNPC((org.bukkit.entity.Entity)entity, "ewogICJ0aW1lc3RhbXAiIDogMTYxMjAzMDY5NDA5OSwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jNDhjNzgzNDU4ZTRjZjg1MThlOGFiNTg2M2ZiYzRjYjk0OGY5MDU2OGVlYjlhNjBkMTZjNGZkZTJiOTZjMDMzIgogICAgfQogIH0KfQ==", "h0EcebQKYgqarHvlkbkkkRN798ir/crHJD4PUtLWNgohxOCk0WbtPu5YxQpmCL75Y6I2Y0vVQvic7x2r4vfMUu5z0O5dfjUXwpXQ6zWYdmHIbeg796EqUsdr1VJlPHMY/PVYle+NoYflwssIXYqLOWqswaBB4cz6qfyinujYoU6wVhGbONns7h/mpCM1r+gyua0hP9g+kjgslGebpDtkQRtv/kZpJ5+19cM5KT12KmjBGlTwsmiP+RfEINt5oGv2p12wqwv0CC5TFqB+/SM1yjYcEdWXQfzmsnC9nzIfgEHpNquKiX2pcGfVPvKgjkkLkO23nnQ0e2KOfIhLKHlyHcESd/lwGP9Ea/i+JVtZMEMUmuU3lQU+ywDMCQiGNEnB9MFlDdA6LBc2mwZKYShyQNgEveXxV2V1j8dt5ctKe7ANBMrCKXRjIO0TcHv2q/PJ9GwEuSfRNwdZp88gkbb79VV+7R4nkzAzEmNpRUxpB4P0qYDpMNCaC+NYEjHrzUr3hiD3tyHQzWHqvOJYYkor5kxGBoE19lZNxfVOEv9K6dIiSAPAtyYbRc9PVL9DUwlshfQO+kYwymSfZqVqW8CUafIA2NtIpIshsuOigqPMYIJv/p1HfGZVSGZ2B1Zmb/DQ9QoTLPqv+ExZ/zMMAqLQB+aB1DL5qfMABNOYhyfSQ2c=", true);
        final PlayerWatcher skywatch = pl.getWatcher();
        CraftLivingEntity target = ((CraftZombie)entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 95);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("LD", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i2 = 0; 20 > i2; ++i2) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.MAGIC_CRIT, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 0.6), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 15L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (null != ((CraftZombie)entity).getTarget() && !AngryArchaeologist.this.s) {
                    final Random random = new Random();
                    AngryArchaeologist.this.s = true;
                    entity.getEquipment().setItemInHand(AngryArchaeologist.getStrPot());
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    BukkitTask bkt = new BukkitRunnable(){

                        public void run() {
                            entity.getEquipment().setItemInHand(AngryArchaeologist.getStrPot());
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                            entity.getWorld().playSound(entity.getLocation(), Sound.DRINK, 0.5f + 0.5f * (float)random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
                        }
                    }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 4L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity = entity;
                        entity.getEquipment().setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));
                        bkt.cancel();
                    }, 60L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity2 = entity;
                        entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                        entity.removePotionEffect(PotionEffectType.SLOW);
                        AngryArchaeologist.this.startedBattle = true;
                    }, 65L);
                }
                if (AngryArchaeologist.this.isSplashing) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 6));
                    entity.getEquipment().setItemInHand(AngryArchaeologist.getPot());
                    Location h2 = entity.getLocation();
                    h2.setPitch(90.0f);
                    entity.teleport(h2);
                    AngryArchaeologist.sendHeadRotation((org.bukkit.entity.Entity)entity, entity.getLocation().getYaw(), 90.0f);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.getHealth() < entity.getMaxHealth() / 2.0 && !AngryArchaeologist.this.SplashCooldown && !AngryArchaeologist.this.isSplashing) {
                    final float perv = entity.getLocation().getPitch();
                    AngryArchaeologist.this.SplashCooldown = true;
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 6));
                    AngryArchaeologist.this.isBowing = false;
                    Location h2 = entity.getLocation();
                    h2.setPitch(90.0f);
                    entity.teleport(h2);
                    SUtil.delay(() -> AngryArchaeologist.this.isSplashing = true, 5L);
                    entity.getEquipment().setItemInHand(AngryArchaeologist.getPot());
                    Location loc = entity.getEyeLocation();
                    SUtil.delay(() -> {
                        LivingEntity val$entity = entity;
                        if (!entity.isDead()) {
                            AngryArchaeologist.spawnHealthPotion(entity.getEyeLocation(), (org.bukkit.entity.Entity)entity);
                        }
                    }, 5L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity2 = entity;
                        if (!entity.isDead()) {
                            AngryArchaeologist.spawnHealthPotion(entity.getEyeLocation(), (org.bukkit.entity.Entity)entity);
                        }
                    }, 10L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity3 = entity;
                        if (!entity.isDead()) {
                            AngryArchaeologist.spawnHealthPotion(entity.getEyeLocation(), (org.bukkit.entity.Entity)entity);
                        }
                    }, 15L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity4 = entity;
                        if (!entity.isDead()) {
                            AngryArchaeologist.spawnHealthPotion(entity.getEyeLocation(), (org.bukkit.entity.Entity)entity);
                        }
                    }, 20L);
                    SUtil.delay(() -> {
                        LivingEntity val$entity5 = entity;
                        if (!entity.isDead()) {
                            AngryArchaeologist.spawnHealthPotion(entity.getEyeLocation(), (org.bukkit.entity.Entity)entity);
                        }
                    }, 25L);
                    new BukkitRunnable(){

                        public void run() {
                            if (entity.isDead()) {
                                return;
                            }
                            entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            AngryArchaeologist.this.isSplashing = false;
                            AngryArchaeologist.sendHeadRotation((org.bukkit.entity.Entity)entity, entity.getLocation().getYaw(), perv);
                            Location h2 = entity.getLocation();
                            h2.setPitch(perv);
                            entity.teleport(h2);
                            SUtil.delay(() -> {
                                LivingEntity val$entity = entity;
                                if (!AngryArchaeologist.this.isBowing) {
                                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                                } else {
                                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                                }
                            }, 3L);
                            SUtil.delay(() -> AngryArchaeologist.this.SplashCooldown = false, SUtil.random(500, 900));
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 40L);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 10L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    Sputnik.zero((org.bukkit.entity.Entity)entity);
                    this.cancel();
                    return;
                }
                if (!AngryArchaeologist.this.startedBattle) {
                    return;
                }
                CraftLivingEntity target1 = ((CraftZombie)entity).getTarget();
                if (null != target1) {
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation()) && !AngryArchaeologist.this.isSplashing) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        Sputnik.sendHeadRotation((org.bukkit.entity.Entity)entity, entity.getLocation().getYaw(), entity.getLocation().getPitch());
                    }
                    if ((6.0 > target1.getLocation().distance(entity.getLocation()) || 16.0 < target1.getLocation().distance(entity.getLocation())) && !AngryArchaeologist.this.isSplashing) {
                        SUtil.delay(() -> {
                            LivingEntity val$entity = entity;
                            entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                        }, 0L);
                        AngryArchaeologist.this.isBowing = false;
                    }
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation()) && !AngryArchaeologist.this.isBowing && !AngryArchaeologist.this.isSplashing) {
                        AngryArchaeologist.this.isBowing = true;
                        skywatch.setRightClicking(false);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 4));
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        new BukkitRunnable((LivingEntity)target1){
                            int t = 0;
                            int atkCharge = 20;
                            double bowPower = 2.2;
                            boolean crit = true;
                            final /* synthetic */ LivingEntity val$target1;
                            {
                                this.val$target1 = livingEntity;
                            }

                            public void run() {
                                if (10.0 >= this.val$target1.getLocation().distance(entity.getLocation())) {
                                    this.atkCharge = 10;
                                    this.bowPower = 1.1;
                                    this.crit = false;
                                }
                                ++this.t;
                                if (!AngryArchaeologist.this.isBowing) {
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    this.cancel();
                                    return;
                                }
                                if (5 == this.t) {
                                    if (!AngryArchaeologist.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(true);
                                }
                                if (this.t == this.atkCharge) {
                                    if (!AngryArchaeologist.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(false);
                                }
                                if (this.t >= this.atkCharge + 1) {
                                    if (!AngryArchaeologist.this.isBowing) {
                                        return;
                                    }
                                    Location location = entity.getEyeLocation().add(entity.getEyeLocation().getDirection().toLocation(entity.getWorld()));
                                    Location l2 = location.clone();
                                    l2.setYaw(location.getYaw());
                                    Arrow arr = entity.getWorld().spawnArrow(l2, l2.getDirection(), (float)this.bowPower, 1.6f);
                                    arr.setShooter((ProjectileSource)entity);
                                    if (!this.crit) {
                                        arr.setCritical(1 == SUtil.random(0, 1));
                                    } else {
                                        arr.setCritical(true);
                                    }
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    AngryArchaeologist.this.isBowing = false;
                                }
                            }
                        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
                    }
                } else if (!AngryArchaeologist.this.isSplashing) {
                    AngryArchaeologist.this.isBowing = false;
                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 2L);
        new BukkitRunnable(){
            Location loc;
            final EntityLiving nms;
            {
                this.loc = entity.getLocation();
                this.nms = ((CraftLivingEntity)entity).getHandle();
            }

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                this.loc.setY(0.0);
                this.nms.setSprinting(false);
                Location loc2 = entity.getLocation();
                loc2.setY(0.0);
                if (entity.hasMetadata("frozen")) {
                    return;
                }
                if (null == ((CraftZombie)entity).getTarget()) {
                    return;
                }
                if (((CraftZombie)entity).getTarget().getWorld() != entity.getWorld()) {
                    return;
                }
                if (4.0 >= ((CraftZombie)entity).getTarget().getLocation().distance(entity.getLocation()) || AngryArchaeologist.this.isSplashing || AngryArchaeologist.this.isBowing || !AngryArchaeologist.this.startedBattle) {
                    return;
                }
                if (0.2 <= this.loc.distance(loc2)) {
                    this.nms.setSprinting(true);
                    if (entity.isOnGround() && 0.5 <= this.loc.distance(loc2)) {
                        double motY = 0.4199999868869782;
                        double motX = 0.0;
                        double motZ = 0.0;
                        if (this.nms.hasEffect(MobEffectList.JUMP)) {
                            motY += (double)((float)(this.nms.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.2f);
                        }
                        if (this.nms.isSprinting()) {
                            float f2 = this.nms.yaw * 0.01745329f;
                            motX -= (double)(MathHelper.sin((float)f2) * 0.9f);
                            motZ += (double)(MathHelper.cos((float)f2) * 0.9f);
                        }
                        entity.setVelocity(new Vector(motX, motY, motZ));
                    }
                    this.loc = entity.getLocation().clone();
                    return;
                }
                this.nms.setSprinting(false);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 7L);
        new BukkitRunnable(){

            public void run() {
                EntityLiving nms = ((CraftLivingEntity)entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (org.bukkit.entity.Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    Player target;
                    if (AngryArchaeologist.this.isSplashing || AngryArchaeologist.this.isBowing || !AngryArchaeologist.this.startedBattle || !(entities instanceof Player) || GameMode.CREATIVE == (target = (Player)entities).getGameMode() || GameMode.SPECTATOR == target.getGameMode() || target.hasMetadata("NPC") || 7 == target.getNoDamageTicks() || 8 < SUtil.random(0, 10)) continue;
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().subtract(entities.getLocation()).toVector()));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer)players).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutAnimation((Entity)((CraftLivingEntity)entity).getHandle(), 0));
                    }
                    nms.r((Entity)((CraftPlayer)target).getHandle());
                    break;
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public void onDamage(SEntity sEntity, org.bukkit.entity.Entity damager, EntityDamageByEntityEvent e2, AtomicDouble damage) {
        LivingEntity en = sEntity.getEntity();
        Vector v2 = new Vector(0, 0, 0);
        SUtil.delay(() -> AngryArchaeologist.lambda$onDamage$0((org.bukkit.entity.Entity)en, v2), 1L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()), SUtil.enchant(SItem.of(SMaterial.DIAMOND_HELMET).getStack()), SUtil.enchant(SItem.of(SMaterial.DIAMOND_CHESTPLATE).getStack()), SUtil.enchant(SItem.of(SMaterial.DIAMOND_LEGGINGS).getStack()), SUtil.enchant(SItem.of(SMaterial.DIAMOND_BOOTS).getStack()));
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
    public double getXPDropped() {
        return 0.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.35;
    }

    public void playPar(Location l2) {
        ConeEffect Effect2 = new ConeEffect(SkyBlock.effectManager);
        Effect2.setLocation(l2.clone().add(l2.getDirection().normalize().multiply(-0.25)).add(0.0, -0.1, 0.0));
        Effect2.particle = ParticleEffect.FLAME;
        Effect2.angularVelocity = 0.39269908169872414;
        Effect2.lengthGrow = 0.025f;
        Effect2.particles = 30;
        Effect2.period = 3;
        Effect2.iterations = 5;
        Effect2.start();
    }

    public static void spawnHealthPotion(Location location, org.bukkit.entity.Entity en) {
        World world = location.getWorld();
        ItemStack item = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setSplash(true);
        pot.apply(item);
        ThrownPotion thrownPotion = (ThrownPotion)world.spawnEntity(location.clone().add(0.0, -0.5, 0.0), EntityType.SPLASH_POTION);
        thrownPotion.setShooter((ProjectileSource)en);
        thrownPotion.setItem(item);
    }

    public static ItemStack getPot() {
        ItemStack item = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setSplash(true);
        pot.apply(item);
        return item;
    }

    public static ItemStack getStrPot() {
        ItemStack item = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setType(PotionType.STRENGTH);
        pot.setSplash(false);
        pot.apply(item);
        return item;
    }

    public static void sendHeadRotation(org.bukkit.entity.Entity e2, float yaw, float pitch) {
        EntityZombie pl = ((CraftZombie)e2).getHandle();
        pl.setLocation(e2.getLocation().getX(), e2.getLocation().getY(), e2.getLocation().getZ(), yaw, pitch);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport((Entity)pl);
        Sputnik.sendPacket(e2.getWorld(), (Packet)packet);
    }

    private static /* synthetic */ void lambda$onDamage$0(org.bukkit.entity.Entity en, Vector v2) {
        en.setVelocity(v2);
    }
}

