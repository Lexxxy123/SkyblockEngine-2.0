/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  de.slikey.effectlib.effect.ConeEffect
 *  de.slikey.effectlib.util.ParticleEffect
 *  me.libraryaddict.disguise.disguisetypes.PlayerDisguise
 *  me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.entity.dungeons;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
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
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.texture.ItemTexture;

public class SuperiorLostAdvNPC
extends BaseZombie {
    private boolean isEating = false;
    private boolean isBowing = false;
    private boolean EatingCooldown = false;
    private boolean CDDR = false;

    @Override
    public String getEntityName() {
        return Sputnik.trans("&d&lLost Adventurer");
    }

    @Override
    public double getEntityMaxHealth() {
        return 3.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 60000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, SEntity sEntity) {
        PlayerDisguise pl = Sputnik.applyPacketNPC((org.bukkit.entity.Entity)entity, "adventuure", null, false);
        final PlayerWatcher skywatch = pl.getWatcher();
        CraftLivingEntity target = ((CraftZombie)entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put((org.bukkit.entity.Entity)entity, 60);
        entity.setMetadata("SlayerBoss", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        entity.setMetadata("LD", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (SuperiorLostAdvNPC.this.isEating) {
                    entity.getWorld().playSound(entity.getLocation(), Sound.EAT, 1.0f, 1.0f);
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 4L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (SuperiorLostAdvNPC.this.isEating) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    Location loc = entity.getLocation();
                    loc.add(0.0, 1.4, 0.0);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.GOLDEN_APPLE));
                    loc.add(entity.getLocation().getDirection().multiply(0.5));
                    new ParticleBuilder(ParticleEffect.ITEM_CRACK, loc).setParticleData(new ItemTexture(new ItemStack(Material.CAULDRON_ITEM))).setOffset(new Vector((double)Sputnik.randomVector(), 0.3, (double)Sputnik.randomVector())).display();
                    new ParticleBuilder(ParticleEffect.ITEM_CRACK, loc).setParticleData(new ItemTexture(new ItemStack(Material.CAULDRON_ITEM))).setOffset(new Vector((double)Sputnik.randomVector(), 0.3, (double)Sputnik.randomVector())).display();
                    new ParticleBuilder(ParticleEffect.ITEM_CRACK, loc).setParticleData(new ItemTexture(new ItemStack(Material.CAULDRON_ITEM))).setOffset(new Vector((double)Sputnik.randomVector(), 0.3, (double)Sputnik.randomVector())).display();
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 3L);
        new BukkitRunnable(){

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.getHealth() < entity.getMaxHealth() * 3.0 / 4.0 && !SuperiorLostAdvNPC.this.EatingCooldown && !SuperiorLostAdvNPC.this.isEating) {
                    SuperiorLostAdvNPC.this.EatingCooldown = true;
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    SUtil.delay(() -> SuperiorLostAdvNPC.this.isEating = true, 5L);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.GOLDEN_APPLE));
                    new BukkitRunnable(){

                        public void run() {
                            entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            entity.getWorld().playSound(entity.getLocation(), Sound.BURP, 1.0f, 1.0f);
                            double healamount = SUtil.random(30000000, 40000000);
                            if (!entity.isDead()) {
                                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + healamount));
                            }
                            SuperiorLostAdvNPC.this.isEating = false;
                            SUtil.delay(() -> {
                                LivingEntity val$entity = entity;
                                if (!SuperiorLostAdvNPC.this.isBowing) {
                                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ASPECT_OF_THE_DRAGONS).getStack()));
                                } else {
                                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                                }
                            }, 5L);
                            SUtil.delay(() -> SuperiorLostAdvNPC.this.EatingCooldown = false, SUtil.random(400, 500));
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
                if (target1 != null) {
                    if (target1.getLocation().distance(entity.getLocation()) <= 5.0 && !SuperiorLostAdvNPC.this.isBowing && !SuperiorLostAdvNPC.this.isEating) {
                        if (SUtil.random(0, 100) > 30) {
                            return;
                        }
                        if (SuperiorLostAdvNPC.this.CDDR) {
                            return;
                        }
                        SuperiorLostAdvNPC.this.CDDR = true;
                        skywatch.setRightClicking(true);
                        SuperiorLostAdvNPC.this.playPar(entity.getEyeLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
                        for (org.bukkit.entity.Entity e2 : target1.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(3.0)), 3.0, 3.0, 3.0)) {
                            if (!(e2 instanceof Player)) continue;
                            Player player = (Player)e2;
                            player.sendMessage(Sputnik.trans("&cLost Adventurer &aused &6Dragon's Breath &aon you!"));
                            player.setVelocity(player.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.0).multiply(6.0));
                            PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                            if (statistics == null) {
                                return;
                            }
                            double defense = statistics.getDefense().addAll();
                            int dmglater = (int)Math.round(105000.0 - 105000.0 * (defense / (defense + 100.0)));
                            User.getUser(player.getUniqueId()).damage(dmglater, EntityDamageEvent.DamageCause.ENTITY_ATTACK, (org.bukkit.entity.Entity)entity);
                            ((LivingEntity)e2).damage(1.0E-6, null);
                        }
                        SUtil.delay(() -> {
                            PlayerWatcher val$skywatch = skywatch;
                            if (!SuperiorLostAdvNPC.this.isBowing) {
                                skywatch.setRightClicking(false);
                            }
                        }, 10L);
                        SUtil.delay(() -> SuperiorLostAdvNPC.this.CDDR = false, 250L);
                    }
                    if (target1.getLocation().distance(entity.getLocation()) >= 7.0 && target1.getLocation().distance(entity.getLocation()) < 15.0) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                    }
                    if (target1.getLocation().distance(entity.getLocation()) >= 7.0 && target1.getLocation().distance(entity.getLocation()) < 15.0 && !SuperiorLostAdvNPC.this.isBowing && !SuperiorLostAdvNPC.this.isEating) {
                        SuperiorLostAdvNPC.this.isBowing = true;
                        skywatch.setRightClicking(false);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 3));
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        SUtil.delay(() -> {
                            PlayerWatcher val$skywatch2 = skywatch;
                            if (SuperiorLostAdvNPC.this.isBowing) {
                                skywatch.setRightClicking(true);
                            }
                        }, 5L);
                        SUtil.delay(() -> {
                            PlayerWatcher val$skywatch3 = skywatch;
                            if (SuperiorLostAdvNPC.this.isBowing) {
                                skywatch.setRightClicking(false);
                            }
                        }, 20L);
                        SUtil.delay(() -> {
                            LivingEntity val$entity = entity;
                            PlayerWatcher val$skywatch4 = skywatch;
                            if (SuperiorLostAdvNPC.this.isBowing) {
                                Location location = entity.getEyeLocation().add(entity.getEyeLocation().getDirection().toLocation(entity.getWorld()));
                                Location l2 = location.clone();
                                l2.setYaw(location.getYaw());
                                entity.getWorld().spawnArrow(l2, l2.getDirection(), 2.2f, 1.6f).setShooter((ProjectileSource)entity);
                                skywatch.setRightClicking(false);
                                SuperiorLostAdvNPC.this.isBowing = false;
                            }
                        }, 21L);
                    } else if ((target1.getLocation().distance(entity.getLocation()) < 7.0 || target1.getLocation().distance(entity.getLocation()) > 15.0) && !SuperiorLostAdvNPC.this.isEating) {
                        SUtil.delay(() -> {
                            LivingEntity val$entity2 = entity;
                            entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ASPECT_OF_THE_DRAGONS).getStack()));
                        }, 0L);
                        SuperiorLostAdvNPC.this.isBowing = false;
                    }
                } else if (!SuperiorLostAdvNPC.this.isEating) {
                    SuperiorLostAdvNPC.this.isBowing = false;
                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.ASPECT_OF_THE_DRAGONS).getStack()));
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 2L);
        new BukkitRunnable(){

            public void run() {
                EntityLiving nms = ((CraftLivingEntity)entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (org.bukkit.entity.Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    Player target;
                    if (SuperiorLostAdvNPC.this.isEating || SuperiorLostAdvNPC.this.isBowing || !(entities instanceof Player) || (target = (Player)entities).getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR || target.hasMetadata("NPC") || target.getNoDamageTicks() == 7 || SUtil.random(0, 10) > 8) continue;
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
        SUtil.delay(() -> SuperiorLostAdvNPC.lambda$onDamage$0((org.bukkit.entity.Entity)en, v2), 1L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(SItem.of(SMaterial.ASPECT_OF_THE_DRAGONS).getStack()), SUtil.enchant(SItem.of(SMaterial.SUPERIOR_DRAGON_HELMET).getStack()), SUtil.enchant(SItem.of(SMaterial.SUPERIOR_DRAGON_CHESTPLATE).getStack()), SUtil.enchant(SItem.of(SMaterial.SUPERIOR_DRAGON_LEGGINGS).getStack()), SUtil.enchant(SItem.of(SMaterial.SUPERIOR_DRAGON_BOOTS).getStack()));
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
        return 5570.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.35;
    }

    public void playPar(Location l2) {
        ConeEffect Effect2 = new ConeEffect(SkyBlock.effectManager);
        Effect2.setLocation(l2.clone().add(l2.getDirection().normalize().multiply(-0.25)).add(0.0, -0.1, 0.0));
        Effect2.particle = de.slikey.effectlib.util.ParticleEffect.FLAME;
        Effect2.angularVelocity = 0.39269908169872414;
        Effect2.lengthGrow = 0.025f;
        Effect2.particles = 30;
        Effect2.period = 3;
        Effect2.iterations = 5;
        Effect2.start();
    }

    private static /* synthetic */ void lambda$onDamage$0(org.bukkit.entity.Entity en, Vector v2) {
        en.setVelocity(v2);
    }
}

