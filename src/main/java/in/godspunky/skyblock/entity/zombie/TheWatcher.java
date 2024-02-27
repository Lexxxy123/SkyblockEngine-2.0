package in.godspunky.skyblock.entity.zombie;

import com.google.common.util.concurrent.AtomicDouble;
import in.godspunky.skyblock.SkyBlock;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.api.beam.Beam;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.HashMap;
import java.util.Map;

public class TheWatcher extends BaseZombie {
    public static final Map<Entity, String> DIALOUGE_BOSS;

    @Override
    public String getEntityName() {
        return "";
    }

    @Override
    public double getEntityMaxHealth() {
        return 250.0;
    }

    @Override
    public double getDamageDealt() {
        return 200.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 1));
        final net.minecraft.server.v1_8_R3.Entity e = ((CraftEntity) entity).getHandle();
        final double height;
        final double height_ = height = e.getBoundingBox().e - e.getBoundingBox().b;
        final ArmorStand hologram = (ArmorStand) entity.getWorld().spawn(entity.getLocation().add(0.0, height, 0.0), (Class) ArmorStand.class);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setSmall(false);
        hologram.setMarker(true);
        hologram.setBasePlate(false);
        hologram.setCustomNameVisible(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    new BukkitRunnable() {
                        public void run() {
                            hologram.remove();
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 20L);
                }
                if (hologram.isDead()) {
                    this.cancel();
                    return;
                }
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram.setCustomName(Sputnik.trans("&e﴾ &c&lThe Watcher &e﴿"));
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 0L);
        final ArmorStand hologram_d = (ArmorStand) entity.getWorld().spawn(entity.getLocation().add(0.0, height + 0.3, 0.0), (Class) ArmorStand.class);
        hologram_d.setVisible(false);
        hologram_d.setGravity(false);
        hologram_d.setSmall(false);
        hologram_d.setMarker(true);
        hologram_d.setBasePlate(false);
        hologram_d.setCustomNameVisible(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    new BukkitRunnable() {
                        public void run() {
                            hologram_d.remove();
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 20L);
                }
                if (hologram.isDead()) {
                    this.cancel();
                    return;
                }
                if (TheWatcher.DIALOUGE_BOSS.containsKey(entity)) {
                    hologram_d.setCustomNameVisible(true);
                    hologram_d.setCustomName(Sputnik.trans(TheWatcher.DIALOUGE_BOSS.get(entity)));
                } else {
                    hologram_d.setCustomNameVisible(false);
                    hologram_d.setCustomName("");
                }
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.3, 0.0));
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.3, 0.0));
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.3, 0.0));
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
        EntityManager.noAI(entity);
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("notDisplay", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.shutTheFuckUp(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        final ArmorStand stand = (ArmorStand) entity.getWorld().spawn(entity.getLocation(), (Class) ArmorStand.class);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setMarker(true);
        stand.getEquipment().setHelmet(SItem.of(SMaterial.JERRY_HEAD).getStack());
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    new BukkitRunnable() {
                        public void run() {
                            stand.remove();
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 0L);
                }
                for (final Entity target : stand.getNearbyEntities(20.0, 20.0, 20.0)) {
                    if (target instanceof Player) {
                        entity.teleport(entity.getLocation().setDirection(target.getLocation().toVector().subtract(target.getLocation().toVector())));
                    }
                }
                if (hologram.isDead()) {
                    this.cancel();
                    return;
                }
                stand.teleport(entity.getLocation().clone().add(0.0, 0.0, 0.0));
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
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
        if (damager instanceof Player) {
            final Location loc = e.getEntity().getLocation().add(0.0, 1.2, 0.0);
            final Beam beam = new Beam(loc, damager.getLocation().add(0.0, 1.0, 0.0));
            beam.start();
            User.getUser(damager.getUniqueId()).damage(SUtil.random(600, 1000), EntityDamageEvent.DamageCause.ENTITY_ATTACK, e.getEntity());
            TheWatcher.DIALOUGE_BOSS.put(e.getEntity(), "&f&lI'm not your goal.");
            ((LivingEntity) damager).damage(0.1, e.getEntity());
            SUtil.delay(() -> beam.stop(), 30L);
            SUtil.delay(() -> TheWatcher.DIALOUGE_BOSS.remove(e.getEntity()), 40L);
        }
    }

    @Override
    public double getXPDropped() {
        return 20.0;
    }

    static {
        DIALOUGE_BOSS = new HashMap<Entity, String>();
    }
}
