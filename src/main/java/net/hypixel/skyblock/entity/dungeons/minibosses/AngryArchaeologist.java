package net.hypixel.skyblock.entity.dungeons.minibosses;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.ParticleEffect;
import net.hypixel.skyblock.SkyBlock;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.Random;

public class AngryArchaeologist extends BaseZombie {
    private boolean isSplashing;
    private boolean isBowing;
    private boolean startedBattle;
    private boolean SplashCooldown;
    private boolean s;

    public AngryArchaeologist() {
        this.isSplashing = false;
        this.isBowing = false;
        this.startedBattle = false;
        this.SplashCooldown = false;
        this.s = false;
    }

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
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        ((CraftZombie) entity).setBaby(false);
        AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "ewogICJ0aW1lc3RhbXAiIDogMTYxMjAzMDY5NDA5OSwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jNDhjNzgzNDU4ZTRjZjg1MThlOGFiNTg2M2ZiYzRjYjk0OGY5MDU2OGVlYjlhNjBkMTZjNGZkZTJiOTZjMDMzIgogICAgfQogIH0KfQ==", "h0EcebQKYgqarHvlkbkkkRN798ir/crHJD4PUtLWNgohxOCk0WbtPu5YxQpmCL75Y6I2Y0vVQvic7x2r4vfMUu5z0O5dfjUXwpXQ6zWYdmHIbeg796EqUsdr1VJlPHMY/PVYle+NoYflwssIXYqLOWqswaBB4cz6qfyinujYoU6wVhGbONns7h/mpCM1r+gyua0hP9g+kjgslGebpDtkQRtv/kZpJ5+19cM5KT12KmjBGlTwsmiP+RfEINt5oGv2p12wqwv0CC5TFqB+/SM1yjYcEdWXQfzmsnC9nzIfgEHpNquKiX2pcGfVPvKgjkkLkO23nnQ0e2KOfIhLKHlyHcESd/lwGP9Ea/i+JVtZMEMUmuU3lQU+ywDMCQiGNEnB9MFlDdA6LBc2mwZKYShyQNgEveXxV2V1j8dt5ctKe7ANBMrCKXRjIO0TcHv2q/PJ9GwEuSfRNwdZp88gkbb79VV+7R4nkzAzEmNpRUxpB4P0qYDpMNCaC+NYEjHrzUr3hiD3tyHQzWHqvOJYYkor5kxGBoE19lZNxfVOEv9K6dIiSAPAtyYbRc9PVL9DUwlshfQO+kYwymSfZqVqW8CUafIA2NtIpIshsuOigqPMYIJv/p1HfGZVSGZ2B1Zmb/DQ9QoTLPqv+ExZ/zMMAqLQB+aB1DL5qfMABNOYhyfSQ2c=", true);
        PlayerWatcher skywatch = pl.getWatcher();
        LivingEntity target = ((CraftZombie) entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 95);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; 20 > i; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.MAGIC_CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.6), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 15L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (null != ((CraftZombie) entity).getTarget() && !AngryArchaeologist.this.s) {
                    Random random = new Random();
                    AngryArchaeologist.this.s = true;
                    entity.getEquipment().setItemInHand(getStrPot());
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                    BukkitTask bkt = new BukkitRunnable() {
                        public void run() {
                            entity.getEquipment().setItemInHand(getStrPot());
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 65, 4));
                            entity.getWorld().playSound(entity.getLocation(), Sound.DRINK, 0.5f + 0.5f * random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
                        }
                    }.runTaskTimer(SkyBlock.getPlugin(), 0L, 4L);
                    SUtil.delay(() -> {
                        Object val$entity = entity;
                        entity.getEquipment().setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));
                        bkt.cancel();
                    }, 60L);
                    SUtil.delay(() -> {
                        Object val$entity2 = entity;
                        entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                        entity.removePotionEffect(PotionEffectType.SLOW);
                        AngryArchaeologist.this.startedBattle = true;
                    }, 65L);
                }
                if (AngryArchaeologist.this.isSplashing) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 6));
                    entity.getEquipment().setItemInHand(getPot());
                    Location h = entity.getLocation();
                    h.setPitch(90.0f);
                    entity.teleport(h);
                    sendHeadRotation(entity, entity.getLocation().getYaw(), 90.0f);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (entity.getHealth() < entity.getMaxHealth() / 2.0 && !AngryArchaeologist.this.SplashCooldown && !AngryArchaeologist.this.isSplashing) {
                    float perv = entity.getLocation().getPitch();
                    AngryArchaeologist.this.SplashCooldown = true;
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 35, 6));
                    AngryArchaeologist.this.isBowing = false;
                    Location h = entity.getLocation();
                    h.setPitch(90.0f);
                    entity.teleport(h);
                    SUtil.delay(() -> AngryArchaeologist.this.isSplashing = true, 5L);
                    entity.getEquipment().setItemInHand(getPot());
                    Location loc = entity.getEyeLocation();
                    SUtil.delay(() -> {
                        Object val$entity = entity;
                        if (entity.isDead()) {
                        } else {
                            spawnHealthPotion(entity.getEyeLocation(), entity);
                        }
                    }, 5L);
                    SUtil.delay(() -> {
                        Object val$entity2 = entity;
                        if (entity.isDead()) {
                        } else {
                            spawnHealthPotion(entity.getEyeLocation(), entity);
                        }
                    }, 10L);
                    SUtil.delay(() -> {
                        Object val$entity3 = entity;
                        if (entity.isDead()) {
                        } else {
                            spawnHealthPotion(entity.getEyeLocation(), entity);
                        }
                    }, 15L);
                    SUtil.delay(() -> {
                        Object val$entity4 = entity;
                        if (entity.isDead()) {
                        } else {
                            spawnHealthPotion(entity.getEyeLocation(), entity);
                        }
                    }, 20L);
                    SUtil.delay(() -> {
                        Object val$entity5 = entity;
                        if (entity.isDead()) {
                        } else {
                            spawnHealthPotion(entity.getEyeLocation(), entity);
                        }
                    }, 25L);
                    new BukkitRunnable() {
                        public void run() {
                            if (entity.isDead()) {
                                return;
                            }
                            entity.getEquipment().setItemInHand(new ItemStack(Material.AIR));
                            AngryArchaeologist.this.isSplashing = false;
                            sendHeadRotation(entity, entity.getLocation().getYaw(), perv);
                            Location h = entity.getLocation();
                            h.setPitch(perv);
                            entity.teleport(h);
                            SUtil.delay(() -> {
                                Object val$entity = entity;
                                if (!AngryArchaeologist.this.isBowing) {
                                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                                } else {
                                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                                }
                            }, 3L);
                            SUtil.delay(() -> AngryArchaeologist.this.SplashCooldown = false, SUtil.random(500, 900));
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 40L);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 10L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    Sputnik.zero(entity);
                    this.cancel();
                    return;
                }
                if (!AngryArchaeologist.this.startedBattle) {
                    return;
                }
                LivingEntity target1 = ((CraftZombie) entity).getTarget();
                if (null != target1) {
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation()) && !AngryArchaeologist.this.isSplashing) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        Sputnik.sendHeadRotation(entity, entity.getLocation().getYaw(), entity.getLocation().getPitch());
                    }
                    if ((6.0 > target1.getLocation().distance(entity.getLocation()) || 16.0 < target1.getLocation().distance(entity.getLocation())) && !AngryArchaeologist.this.isSplashing) {
                        SUtil.delay(() -> {
                            Object val$entity = entity;
                            entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                        }, 0L);
                        AngryArchaeologist.this.isBowing = false;
                    }
                    if (6.0 <= target1.getLocation().distance(entity.getLocation()) && 16.0 > target1.getLocation().distance(entity.getLocation()) && !AngryArchaeologist.this.isBowing && !AngryArchaeologist.this.isSplashing) {
                        AngryArchaeologist.this.isBowing = true;
                        skywatch.setRightClicking(false);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 4));
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        new BukkitRunnable() {
                            int t = 0;
                            int atkCharge = 20;
                            double bowPower = 2.2;
                            boolean crit = true;

                            public void run() {
                                if (10.0 >= target1.getLocation().distance(entity.getLocation())) {
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
                                    Location l = location.clone();
                                    l.setYaw(location.getYaw());
                                    Arrow arr = entity.getWorld().spawnArrow(l, l.getDirection(), (float) this.bowPower, 1.6f);
                                    arr.setShooter(entity);
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
                        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
                    }
                } else if (!AngryArchaeologist.this.isSplashing) {
                    AngryArchaeologist.this.isBowing = false;
                    entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
        new BukkitRunnable() {
            Location loc = entity.getLocation();
            final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();

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
                if (null == ((CraftZombie) entity).getTarget()) {
                    return;
                }
                if (((CraftZombie) entity).getTarget().getWorld() != entity.getWorld()) {
                    return;
                }
                if (4.0 >= ((CraftZombie) entity).getTarget().getLocation().distance(entity.getLocation()) || AngryArchaeologist.this.isSplashing || AngryArchaeologist.this.isBowing || !AngryArchaeologist.this.startedBattle) {
                    return;
                }
                if (0.2 <= this.loc.distance(loc2)) {
                    this.nms.setSprinting(true);
                    if (entity.isOnGround() && 0.5 <= this.loc.distance(loc2)) {
                        double motY = 0.4199999868869782;
                        double motX = 0.0;
                        double motZ = 0.0;
                        if (this.nms.hasEffect(MobEffectList.JUMP)) {
                            motY += (this.nms.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.2f;
                        }
                        if (this.nms.isSprinting()) {
                            float f = this.nms.yaw * 0.01745329f;
                            motX -= MathHelper.sin(f) * 0.9f;
                            motZ += MathHelper.cos(f) * 0.9f;
                        }
                        entity.setVelocity(new Vector(motX, motY, motZ));
                    }
                    this.loc = entity.getLocation().clone();
                    return;
                }
                this.nms.setSprinting(false);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 7L);
        new BukkitRunnable() {
            public void run() {
                EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (AngryArchaeologist.this.isSplashing) {
                        continue;
                    }
                    if (AngryArchaeologist.this.isBowing) {
                        continue;
                    }
                    if (!AngryArchaeologist.this.startedBattle) {
                        continue;
                    }
                    if (!(entities instanceof Player)) {
                        continue;
                    }
                    Player target = (Player) entities;
                    if (GameMode.CREATIVE == target.getGameMode()) {
                        continue;
                    }
                    if (GameMode.SPECTATOR == target.getGameMode()) {
                        continue;
                    }
                    if (target.hasMetadata("NPC")) {
                        continue;
                    }
                    if (7 == target.getNoDamageTicks()) {
                        continue;
                    }
                    if (8 < SUtil.random(0, 10)) {
                        continue;
                    }
                    entity.teleport(entity.getLocation().setDirection(target.getLocation().subtract(entities.getLocation()).toVector()));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) players).getHandle().playerConnection.sendPacket(new PacketPlayOutAnimation(((CraftLivingEntity) entity).getHandle(), 0));
                    }
                    nms.r(((CraftPlayer) target).getHandle());
                    break;
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        Entity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
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

    public void playPar(Location l) {
        ConeEffect Effect = new ConeEffect(SkyBlock.effectManager);
        Effect.setLocation(l.clone().add(l.getDirection().normalize().multiply(-0.25)).add(0.0, -0.1, 0.0));
        Effect.particle = ParticleEffect.FLAME;
        Effect.angularVelocity = 0.39269908169872414;
        Effect.lengthGrow = 0.025f;
        Effect.particles = 30;
        Effect.period = 3;
        Effect.iterations = 5;
        Effect.start();
    }

    public static void spawnHealthPotion(Location location, Entity en) {
        World world = location.getWorld();
        ItemStack item = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setSplash(true);
        pot.apply(item);
        ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location.clone().add(0.0, -0.5, 0.0), EntityType.SPLASH_POTION);
        thrownPotion.setShooter((ProjectileSource) en);
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

    public static void sendHeadRotation(Entity e, float yaw, float pitch) {
        net.minecraft.server.v1_8_R3.Entity pl = ((CraftZombie) e).getHandle();
        pl.setLocation(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), yaw, pitch);
        PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(pl);
        Sputnik.sendPacket(e.getWorld(), packet);
    }
}
