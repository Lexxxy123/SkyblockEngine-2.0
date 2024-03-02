package net.hypixel.skyblock.entity.dungeons.watcher;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.SkyBlock;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

public class WatcherLivid extends BaseZombie {
    @Override
    public String getEntityName() {
        return Sputnik.trans("&4&lMaster Livid");
    }

    @Override
    public double getEntityMaxHealth() {
        return 9.5E8;
    }

    @Override
    public double getDamageDealt() {
        return 6000000.0;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        HeadsOnWall h = new HeadsOnWall(EnumWatcherType.LIVID);
        PlayerDisguise p = Sputnik.applyPacketNPC(entity, h.value, h.signature, true);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 87);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("WATCHER_E", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        p.setReplaceSounds(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (null != ((CraftZombie) entity).getTarget()) {
                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.LIVID_DAGGER).getStack());
                    p.getWatcher().setRightClicking(true);
                    WatcherLivid.this.throwThickAssBone(entity);
                    SUtil.delay(() -> {
                        Object val$p = p;
                        p.getWatcher().setRightClicking(false);
                    }, 10L);
                    SUtil.delay(() -> {
                        Object val$entity = entity;
                        entity.getEquipment().setItemInHand(SItem.of(SMaterial.SHADOW_FURY).getStack());
                    }, 30L);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 150L, 150L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (int j = 0; 5 > j; ++j) {
                    entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 0.1, 0.0), Effect.WITCH_MAGIC, 0, 1, SUtil.random(-1, 1), SUtil.random(-1, 2), SUtil.random(-1, 1), 0.0f, 1, 20);
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 10L, 10L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (null != ((CraftZombie) entity).getTarget()) {
                    Location lc = ((CraftZombie) entity).getTarget().getLocation();
                    entity.getEquipment().setItemInHand(SItem.of(SMaterial.SHADOW_FURY).getStack());
                    p.getWatcher().setRightClicking(true);
                    SUtil.delay(() -> {
                        Object val$p = p;
                        p.getWatcher().setRightClicking(false);
                    }, 10L);
                    ((CraftZombie) entity).getTarget().getWorld().playSound(((CraftZombie) entity).getTarget().getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    ((CraftZombie) entity).getHandle().setPositionRotation(lc.getX(), lc.getY(), lc.getZ(), lc.getYaw(), lc.getPitch());
                    for (int j = 0; 20 > j; ++j) {
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.LARGE_SMOKE, 0, 1, SUtil.random(-1, 1), SUtil.random(-1, 2), SUtil.random(-1, 1), 0.0f, 1, 20);
                        entity.getWorld().spigot().playEffect(entity.getLocation().clone().add(0.0, 1.75, 0.0), Effect.WITCH_MAGIC, 0, 1, SUtil.random(-1, 1), SUtil.random(-1, 2), SUtil.random(-1, 1), 0.0f, 1, 20);
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 220L, 220L);
        new BukkitRunnable() {
            public void run() {
                EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
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
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 3L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SItem.of(SMaterial.DIAMOND_SWORD).getStack(), null, null, null, null);
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        Entity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e) {
        if (1 == SUtil.random(0, 5)) {
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 5));
        }
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
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
        return 304.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.4;
    }

    @Override
    public int mobLevel() {
        return 540;
    }

    public void throwThickAssBone(Entity e) {
        Vector throwVec = e.getLocation().add(e.getLocation().getDirection().multiply(10)).toVector().subtract(e.getLocation().toVector()).normalize().multiply(1.2);
        Location throwLoc = e.getLocation().add(0.0, 0.5, 0.0);
        ArmorStand armorStand1 = (ArmorStand) e.getWorld().spawnEntity(throwLoc, EntityType.ARMOR_STAND);
        armorStand1.getEquipment().setItemInHand(SItem.of(SMaterial.LIVID_DAGGER).getStack());
        armorStand1.setGravity(false);
        armorStand1.setVisible(false);
        armorStand1.setMarker(true);
        Vector teleportTo = e.getLocation().getDirection().normalize().multiply(1);
        Vector[] previousVector = {throwVec};
        new BukkitRunnable() {
            private int run = -1;

            public void run() {
                int i;
                int ran = i = 0;
                final int num = 90;
                Location loc = null;
                ++this.run;
                if (100 < this.run) {
                    this.cancel();
                    return;
                }
                for (int j = 0; 10 > j; ++j) {
                    armorStand1.getWorld().spigot().playEffect(armorStand1.getLocation().clone().add(0.0, 1.75, 0.0), Effect.CRIT, 0, 1, (float) SUtil.random(-0.5, 0.5), (float) SUtil.random(0.0, 0.5), (float) SUtil.random(-0.5, 0.5), 0.0f, 1, 20);
                }
                Location locof = armorStand1.getLocation();
                locof.setY(locof.getY() + 1.0);
                if (Material.AIR != locof.getBlock().getType()) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                double xPos = armorStand1.getRightArmPose().getX();
                armorStand1.setRightArmPose(new EulerAngle(xPos + 0.7, 0.0, 0.0));
                Vector newVector = new Vector(throwVec.getX(), previousVector[0].getY() - 0.03, throwVec.getZ());
                previousVector[0] = newVector;
                armorStand1.setVelocity(newVector);
                if (13 > i) {
                    int angle = i * 20 + num;
                    final boolean back = false;
                } else {
                    int angle = i * 20 - num;
                    final boolean back = true;
                }
                if (Material.AIR != locof.getBlock().getType() && Material.WATER != locof.getBlock().getType()) {
                    armorStand1.remove();
                    this.cancel();
                    return;
                }
                if (0 == i % 2 && 13 > i) {
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                    armorStand1.teleport(armorStand1.getLocation().add(teleportTo).multiply(1.0));
                } else if (0 == i % 2) {
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                    armorStand1.teleport(armorStand1.getLocation().subtract(loc.getDirection().normalize().multiply(1)));
                }
                for (Entity en : armorStand1.getNearbyEntities(1.0, 1.0, 1.0)) {
                    if (en instanceof Player) {
                        Player p = (Player) en;
                        p.getWorld().playSound(p.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                        User.getUser(p.getUniqueId()).damage(p.getMaxHealth() * 25.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, e);
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
}
