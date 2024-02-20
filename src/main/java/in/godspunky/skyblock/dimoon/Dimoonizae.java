package in.godspunky.skyblock.dimoon;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.ParticleEffect;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.EntityDrop;
import in.godspunky.skyblock.entity.EntityDropType;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
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
import org.bukkit.util.Vector;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.Collections;
import java.util.List;

public class Dimoonizae extends BaseZombie {
    private boolean isBowing;
    private final boolean s;
    private Location spawnLoc;

    public Dimoonizae() {
        this.isBowing = false;
        this.s = false;
        this.spawnLoc = null;
    }

    @Override
    public String getEntityName() {
        return Sputnik.trans("&e&lDimooniaze");
    }

    @Override
    public double getEntityMaxHealth() {
        return 5.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 2500000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        this.spawnLoc = entity.getLocation();
        ((CraftZombie) entity).setBaby(false);
        final AttributeInstance followRange = ((CraftLivingEntity) entity).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        followRange.setValue(40.0);
        final PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "ewogICJ0aW1lc3RhbXAiIDogMTYwNzI2OTE0ODI0NSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lMzgxYTE4NzVlZmViMmEyNjBkMzU3OTg0YzNlNzVhN2RmYmM3YTIyYjU4MmJmZTk5OTA0MjFjYmI0NDVhY2MiCiAgICB9CiAgfQp9", "IPtgN21aEt2XOntNnSueh3eJHFJOEwIad/ttKraizcl4ffFSymZQo3NhgUPbRnSy2lFNHR/kTl6fczwnRtwxBNmG/YeA7kyr225kgROEAnze8owdQSDkFhvcnP5QRn5oBW9Aa11ZZRlcHuMEUYyZptYkUo8VZ4EV6NSkxz6n5Vjph8KJc2qTLsFtSZIi9lvMAvZRuR4IaKZT85Pnf04Lqmsj1mXkV22pjG3t5MQECsAEmV6beLAVkSjpxQqEkr86+tCbeYRx77fZxxhswHhCZsTB50I5EpDJ3cx5pvvmz8LW2/KoEppXt+NAW/3P4aHuj34KFMp1+qJjSHStl9yKHEdCzTKUy2bh3b6cq6hr0WfCAOes1X0uObindLA6kP+y0UbtvPHGnuyslvQ47X5NdkT54oATW8wBWh85gPUEPLsBXO3VBvfK5omSgaSrC3D1xZbuFjdDYETg4BmTlJOnhoDHUTAvl/a9gQTnItyJkLvkoa/WNWlSk6lcNqz09X7VipnerY5XYad1A1T5XngOg6+TUBwW+XXcxxh5FDH3kMGLomDCbZjaiKm2QvPzVrHVtZ0DhdvQ3jzGa/oSyCbQCWWgusptxeMcOvgxtrjtb4ZxnFiR3H8XYPYWsSxl2zZUlq7Ias9MbKILu8CulOX90i3Gha27lSIaf+ksUO4vrII=", true);
        final PlayerWatcher skywatch = pl.getWatcher();
        final LivingEntity target = ((CraftZombie) entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 70);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 20; ++i) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.25, 0.0), Effect.WITCH_MAGIC, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.6), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 15L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    Sputnik.zero(entity);
                    this.cancel();
                    return;
                }
                final LivingEntity target1 = ((CraftZombie) entity).getTarget();
                if (entity.getFireTicks() > 0) {
                    entity.teleport(Dimoonizae.this.spawnLoc);
                }
                if (target1 != null) {
                    if (target1.getLocation().distance(entity.getLocation()) >= 6.0 && target1.getLocation().distance(entity.getLocation()) < 16.0) {
                        entity.teleport(entity.getLocation().setDirection(target1.getLocation().toVector().subtract(entity.getLocation().toVector())));
                        Sputnik.sendHeadRotation(entity, entity.getLocation().getYaw(), entity.getLocation().getPitch());
                    }
                    if (target1.getLocation().distance(entity.getLocation()) < 6.0 || target1.getLocation().distance(entity.getLocation()) > 16.0) {
                        SUtil.delay(() -> {
                            final Object val$entity = entity;
                            entity.getEquipment().setItemInHand(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()));
                        }, 0L);
                        Dimoonizae.this.isBowing = false;
                    }
                    if (target1.getLocation().distance(entity.getLocation()) >= 6.0 && target1.getLocation().distance(entity.getLocation()) < 16.0 && !Dimoonizae.this.isBowing) {
                        Dimoonizae.this.isBowing = true;
                        skywatch.setRightClicking(false);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 4));
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.BOW).getStack());
                        new BukkitRunnable() {
                            int t = 0;
                            int atkCharge = 20;
                            double bowPower = 2.2;
                            boolean crit = true;

                            public void run() {
                                if (target1.getLocation().distance(entity.getLocation()) <= 10.0) {
                                    this.atkCharge = 10;
                                    this.bowPower = 1.1;
                                    this.crit = false;
                                }
                                ++this.t;
                                if (!Dimoonizae.this.isBowing) {
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    this.cancel();
                                    return;
                                }
                                if (this.t == 5) {
                                    if (!Dimoonizae.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(true);
                                }
                                if (this.t == this.atkCharge) {
                                    if (!Dimoonizae.this.isBowing) {
                                        return;
                                    }
                                    skywatch.setRightClicking(false);
                                }
                                if (this.t >= this.atkCharge + 1) {
                                    if (!Dimoonizae.this.isBowing) {
                                        return;
                                    }
                                    final Location location = entity.getEyeLocation().add(entity.getEyeLocation().getDirection().toLocation(entity.getWorld()));
                                    final Location l = location.clone();
                                    l.setYaw(location.getYaw());
                                    final Arrow arr = entity.getWorld().spawnArrow(l, l.getDirection(), (float) this.bowPower, 1.6f);
                                    arr.setShooter(entity);
                                    if (!this.crit) {
                                        arr.setCritical(SUtil.random(0, 1) == 1);
                                    } else {
                                        arr.setCritical(true);
                                    }
                                    skywatch.setRightClicking(false);
                                    entity.removePotionEffect(PotionEffectType.SLOW);
                                    Dimoonizae.this.isBowing = false;
                                }
                            }
                        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
                    }
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
                final Location loc2 = entity.getLocation();
                loc2.setY(0.0);
                if (entity.hasMetadata("frozen")) {
                    return;
                }
                if (((CraftZombie) entity).getTarget() == null) {
                    return;
                }
                if (((CraftZombie) entity).getTarget().getWorld() != entity.getWorld()) {
                    return;
                }
                if (((CraftZombie) entity).getTarget().getLocation().distance(entity.getLocation()) <= 4.0 || Dimoonizae.this.isBowing) {
                    return;
                }
                if (this.loc.distance(loc2) >= 0.2) {
                    this.nms.setSprinting(true);
                    if (entity.isOnGround() && this.loc.distance(loc2) >= 0.5) {
                        double motY = 0.4199999868869782;
                        double motX = 0.0;
                        double motZ = 0.0;
                        if (this.nms.hasEffect(MobEffectList.JUMP)) {
                            motY += (this.nms.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.2f;
                        }
                        if (this.nms.isSprinting()) {
                            final float f = this.nms.yaw * 0.01745329f;
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
                final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (final Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (Dimoonizae.this.isBowing) {
                        continue;
                    }
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
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 2L);
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        if (SkyBlock.getPlugin().dimoon == null) {
            return;
        }
        if (!(damager instanceof Player)) {
            return;
        }
        killed.getWorld().playEffect(killed.getLocation().add(0.0, 0.7, 0.0), Effect.EXPLOSION_HUGE, 0);
        killed.getWorld().playSound(killed.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
        SkyBlock.getPlugin().dimoon.func((Player) damager);
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.HIDDEN_DIMOON_FRAG, EntityDropType.OCCASIONAL, 1.0));
    }

    @Override
    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
        final Entity en = sEntity.getEntity();
        final Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SUtil.enchant(SItem.of(SMaterial.DIAMOND_SWORD).getStack()), null, null, null, null);
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
        return 200.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.3;
    }

    public void playPar(final Location l) {
        final ConeEffect Effect = new ConeEffect(SkyBlock.effectManager);
        Effect.setLocation(l.clone().add(l.getDirection().normalize().multiply(-0.25)).add(0.0, -0.1, 0.0));
        Effect.particle = ParticleEffect.FLAME;
        Effect.angularVelocity = 0.39269908169872414;
        Effect.lengthGrow = 0.025f;
        Effect.particles = 30;
        Effect.period = 3;
        Effect.iterations = 5;
        Effect.start();
    }

    public static void spawnHealthPotion(final Location location, final Entity en) {
        final World world = location.getWorld();
        final ItemStack item = new ItemStack(Material.POTION, 1);
        final Potion pot = new Potion(1);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setSplash(true);
        pot.apply(item);
        final ThrownPotion thrownPotion = (ThrownPotion) world.spawnEntity(location.clone().add(0.0, -0.5, 0.0), EntityType.SPLASH_POTION);
        thrownPotion.setShooter((ProjectileSource) en);
        thrownPotion.setItem(item);
    }

    public static ItemStack getPot() {
        final ItemStack item = new ItemStack(Material.POTION, 1);
        final Potion pot = new Potion(1);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setSplash(true);
        pot.apply(item);
        return item;
    }

    public static ItemStack getStrPot() {
        final ItemStack item = new ItemStack(Material.POTION, 1);
        final Potion pot = new Potion(1);
        pot.setType(PotionType.STRENGTH);
        pot.setSplash(false);
        pot.apply(item);
        return item;
    }

    public static void sendHeadRotation(final Entity e, final float yaw, final float pitch) {
        final net.minecraft.server.v1_8_R3.Entity pl = ((CraftZombie) e).getHandle();
        pl.setLocation(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), yaw, pitch);
        final PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(pl);
        Sputnik.sendPacket(e.getWorld(), packet);
    }
}
