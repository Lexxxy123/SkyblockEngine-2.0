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
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
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
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.EulerAngle
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.dungeons.minibosses;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.ParticleEffect;
import java.util.List;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class FrozenAdv
extends BaseZombie {
    private boolean isEating = false;
    private boolean isBowing = false;
    private boolean EatingCooldown = false;
    private boolean CDDR = false;
    private boolean CDLA = false;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&d&lFrozen Adventurer");
    }

    @Override
    public double getEntityMaxHealth() {
        return 7.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 6000000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        ((CraftZombie)entity).setBaby(false);
        AttributeInstance followRange = ((CraftLivingEntity)entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        PlayerDisguise pl = Sputnik.applyPacketNPC((org.bukkit.entity.Entity)entity, "ewogICJ0aW1lc3RhbXAiIDogMTYwMTUyOTc4ODY1OSwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zZGVlNTAxODFkYmMzMzZkMGQ5MDM1ZjYyMjQ0OGM0M2RhYTdmZGI1ZDdjYWFhOTFmNjdiM2JjNjQ4NmMzMjEwIgogICAgfQogIH0KfQ==", "i0cF74RyF/YAl8m2VathjBpRKlY93rrqnBx/fZPwzaXhL+KLGGhGEJc0SPSzDqpQDXXQKeMO2qKQwDsIbXrNQT0TUMzjFObzPznx5LVNrjZIs9xYpOyh6olmPOxKb8S/5DKKIbtm1ZfejK4KFLuz1OP4idcgPf+xzhoXsPfX8KdbWXoTu192zQ/L6lo0N2dAMzjz6ymELXkpl05gruONtSF01OjcyvVL80lWR5YyecoycxqFPpVXOhxAYYa2PoircLwMg2Vtmkck0/u0gniDt3EEkZkQ44CjT/9bxjf4LEkeHMdnXkt/KYaTk934/eSgr8dL6zlU7v/IyX6Jn3vceQQz9XF04Q+COBsxfjvExc7/Awti+8OJASCvlWoBHL2jqQbXDKXk/OJgjh6F8rPECljiqrdfmEC+W3lM/mc8WBX1KheHtiZiMlyYPOZQ4hCdCJoiHi+jxIhV56UVvu911lhsyRB4ovyb6JWqty/9ztN8spEA4Mxk0xIcK7aVJ3nb8XrfCsMRC17oAwd6W79qSGKxmLJxTg25w+HJ1Sj4JRrLcD4Ix505ptLAdyGdd17xr5oXZ7G4cT3vm19sR1SqPYuyjHV9S1eJBtJAo7kFhFcoAKGBp8MdHXZ4MTZPZZSXdOwPGcYavANN7NA3EPecvfqBUCh9e3IhXJOP70Huv5A=", true);
        final PlayerWatcher skywatch = pl.getWatcher();
        CraftLivingEntity target = ((CraftZombie)entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 87);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("LD", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (FrozenAdv.this.isEating) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    entity.getEquipment().setItemInHand(new ItemStack(Material.GOLDEN_APPLE));
                    Sputnik.sendEatingAnimation(entity);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 4L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i2 = 0; 20 > i2; ++i2) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.FLAME, 0, 1, (float)SUtil.random(-0.5, 0.5), (float)SUtil.random(0.0, 1.5), (float)SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 20L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.getHealth() < entity.getMaxHealth() / 2.0 && !FrozenAdv.this.EatingCooldown && !FrozenAdv.this.isEating) {
                    FrozenAdv.this.EatingCooldown = true;
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    FrozenAdv.this.isBowing = false;
                    SUtil.delay(() -> FrozenAdv.this.isEating = true, 5L);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.GOLDEN_APPLE));
                    new BukkitRunnable(){

                        public void run() {
                            if (entity.isDead()) {
                                return;
                            }
                            entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            entity.getWorld().playSound(entity.getLocation(), Sound.BURP, 1.0f, 1.0f);
                            double healamount = FrozenAdv.this.getEntityMaxHealth() * (double)SUtil.random(40, 60) / 100.0;
                            if (!entity.isDead()) {
                                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + healamount));
                            }
                            FrozenAdv.this.isEating = false;
                            SUtil.delay(() -> {
                                LivingEntity val$entity = entity;
                                if (!FrozenAdv.this.isBowing) {
                                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ICE_WAND).getStack()));
                                } else {
                                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                                }
                            }, 5L);
                            SUtil.delay(() -> FrozenAdv.this.EatingCooldown = false, SUtil.random(600, 800));
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 60L);
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
                CraftLivingEntity target1 = ((CraftZombie)entity).getTarget();
                if (null != target1) {
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation())) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        FrozenAdv.sendHeadRotation((org.bukkit.entity.Entity)entity, entity.getLocation().getYaw(), entity.getLocation().getPitch());
                    }
                    if ((6.0 > target1.getLocation().distance(entity.getLocation()) || 16.0 < target1.getLocation().distance(entity.getLocation())) && !FrozenAdv.this.isEating) {
                        SUtil.delay(() -> {
                            LivingEntity val$entity = entity;
                            entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ICE_WAND).getStack()));
                        }, 0L);
                        FrozenAdv.this.isBowing = false;
                    }
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation()) && !FrozenAdv.this.isBowing && !FrozenAdv.this.isEating) {
                        FrozenAdv.this.isBowing = true;
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
                                if (!FrozenAdv.this.isBowing) {
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    this.cancel();
                                    return;
                                }
                                if (5 == this.t) {
                                    if (!FrozenAdv.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(true);
                                }
                                if (this.t == this.atkCharge) {
                                    if (!FrozenAdv.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(false);
                                }
                                if (this.t >= this.atkCharge + 1) {
                                    if (!FrozenAdv.this.isBowing) {
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
                                    FrozenAdv.this.isBowing = false;
                                }
                            }
                        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
                    }
                    if (10.0 >= target1.getLocation().distance(entity.getLocation()) && !FrozenAdv.this.isBowing && !FrozenAdv.this.isEating && 10 > SUtil.random(0, 100) && !FrozenAdv.this.CDLA) {
                        FrozenAdv.this.CDLA = true;
                        FrozenAdv.this.lightningPlayer((org.bukkit.entity.Entity)entity);
                        SUtil.delay(() -> FrozenAdv.this.CDLA = false, 300L);
                    }
                    if (5.0 >= target1.getLocation().distance(entity.getLocation()) && !FrozenAdv.this.isBowing && !FrozenAdv.this.isEating) {
                        if (30 < SUtil.random(0, 100)) {
                            return;
                        }
                        if (FrozenAdv.this.CDDR) {
                            return;
                        }
                        FrozenAdv.this.CDDR = true;
                        skywatch.setRightClicking(true);
                        FrozenAdv.this.playPar(entity.getEyeLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
                        for (org.bukkit.entity.Entity e2 : target1.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1)), 3.0, 3.0, 3.0)) {
                            if (!(e2 instanceof Player)) continue;
                            final Player player = (Player)e2;
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 20));
                            double b2 = 0.0;
                            for (int i2 = 0; 2 > i2; ++i2) {
                                final int d2 = i2;
                                if (0 == d2) {
                                    b2 = 0.2;
                                } else if (1 == i2) {
                                    b2 = 0.4;
                                } else if (2 == i2) {
                                    b2 = 0.6;
                                }
                                final ArmorStand stands = (ArmorStand)player.getWorld().spawn(player.getLocation().add(0.0, b2 + 1.0, 0.0), ArmorStand.class);
                                stands.setCustomNameVisible(false);
                                stands.setVisible(false);
                                stands.setArms(true);
                                stands.setMarker(true);
                                stands.setGravity(false);
                                stands.setRightArmPose(new EulerAngle(0.0, 0.0, 12.0));
                                stands.getEquipment().setItemInHand(new ItemStack(Material.PACKED_ICE));
                                SUtil.delay(() -> {
                                    stands.remove();
                                    player.removeMetadata("frozen", (Plugin)SkyBlock.getPlugin());
                                }, 100L);
                                new BukkitRunnable(){

                                    public void run() {
                                        double c2 = 0.0;
                                        if (0 == d2) {
                                            c2 = 0.2;
                                        } else if (1 == d2) {
                                            c2 = 0.4;
                                        } else if (2 == d2) {
                                            c2 = 0.6;
                                        }
                                        if (stands.isDead()) {
                                            player.removePotionEffect(PotionEffectType.SLOW);
                                            player.removeMetadata("frozen", (Plugin)SkyBlock.getPlugin());
                                            this.cancel();
                                            return;
                                        }
                                        if (player.isDead() || entity.isDead()) {
                                            stands.remove();
                                            player.removeMetadata("frozen", (Plugin)SkyBlock.getPlugin());
                                        }
                                        stands.teleport(player.getLocation().add(0.0, c2 + 1.0, 0.0));
                                    }
                                }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
                            }
                            PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                            if (null == statistics) {
                                return;
                            }
                            double defense = statistics.getDefense().addAll();
                            int dmglater = (int)Math.round(FrozenAdv.this.getDamageDealt() * 3.0 - FrozenAdv.this.getDamageDealt() * 3.0 * (defense / (defense + 100.0)));
                            User.getUser(player.getUniqueId()).damage(dmglater, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (org.bukkit.entity.Entity)entity);
                            player.setMetadata("frozen", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
                            ((LivingEntity)e2).damage(1.0E-6, null);
                        }
                        SUtil.delay(() -> {
                            PlayerWatcher val$skywatch = skywatch;
                            if (!FrozenAdv.this.isBowing) {
                                skywatch.setRightClicking(false);
                            }
                        }, 20L);
                        SUtil.delay(() -> FrozenAdv.this.CDDR = false, 200L);
                    }
                } else if (!FrozenAdv.this.isEating) {
                    FrozenAdv.this.isBowing = false;
                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ICE_WAND).getStack()));
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
                if (4.0 >= ((CraftZombie)entity).getTarget().getLocation().distance(entity.getLocation()) || FrozenAdv.this.isEating || FrozenAdv.this.isBowing) {
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
                    if (FrozenAdv.this.isEating || FrozenAdv.this.isBowing || !(entities instanceof Player) || GameMode.CREATIVE == (target = (Player)entities).getGameMode() || GameMode.SPECTATOR == target.getGameMode() || target.hasMetadata("NPC") || 7 == target.getNoDamageTicks() || 8 < SUtil.random(0, 10)) continue;
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
        SUtil.delay(() -> FrozenAdv.lambda$onDamage$0((org.bukkit.entity.Entity)en, v2), 1L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(SItem.of(SMaterial.ICE_WAND).getStack()), SUtil.enchant(SUtil.getSkullURLStack("Frozen Blaze Helmet", "55a13bb48e3595b55de8dd6943fc38db5235371278c695bd453e49a0999", 1, "")), SUtil.enchant(FrozenAdv.st(10541807, Material.LEATHER_CHESTPLATE, "Frozen Blaze Chestplate")), SUtil.enchant(FrozenAdv.st(10541807, Material.LEATHER_LEGGINGS, "Frozen Blaze Leggings")), SUtil.enchant(FrozenAdv.st(10541807, Material.LEATHER_BOOTS, "Frozen Blaze Boots")));
    }

    public static ItemStack st(int hexcolor, Material m2, String name) {
        ItemStack stack = SUtil.applyColorToLeatherArmor(new ItemStack(m2), Color.fromRGB((int)hexcolor));
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.spigot().setUnbreakable(true);
        stack.setItemMeta(itemMeta);
        return stack;
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
        return 0.25;
    }

    public void playPar(Location l2) {
        ConeEffect Effect2 = new ConeEffect(SkyBlock.effectManager);
        Effect2.setLocation(l2.clone().add(l2.getDirection().normalize().multiply(-0.25)).add(0.0, -0.1, 0.0));
        Effect2.particle = ParticleEffect.SNOW_SHOVEL;
        Effect2.color = Color.WHITE;
        Effect2.angularVelocity = 0.39269908169872414;
        Effect2.lengthGrow = 0.025f;
        Effect2.particles = 30;
        Effect2.period = 3;
        Effect2.iterations = 5;
        Effect2.start();
    }

    public void lightningPlayer(org.bukkit.entity.Entity en) {
        List a2 = en.getNearbyEntities(10.0, 10.0, 10.0);
        a2.removeIf(entity -> EntityType.PLAYER != entity.getType());
        if (3 > a2.size()) {
            for (int i2 = 0; i2 < SUtil.random(1, 3); ++i2) {
                en.getWorld().strikeLightningEffect(new Location(en.getWorld(), en.getLocation().getX() + (double)SUtil.random(-2, 2), en.getLocation().getY(), en.getLocation().getZ() + (double)SUtil.random(-2, 2), en.getLocation().getYaw(), en.getLocation().getPitch()));
            }
        }
        for (org.bukkit.entity.Entity e2 : en.getNearbyEntities(10.0, 10.0, 10.0)) {
            if (!(e2 instanceof Player)) continue;
            Player p2 = (Player)e2;
            p2.getWorld().strikeLightningEffect(p2.getLocation());
            User.getUser(p2.getUniqueId()).damage(p2.getMaxHealth() * 10.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e2);
        }
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

