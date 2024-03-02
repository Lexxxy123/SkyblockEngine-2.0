package net.hypixel.skyblock.entity.nms;

import com.google.common.util.concurrent.AtomicDouble;
import de.slikey.effectlib.effect.ConeEffect;
import de.slikey.effectlib.util.ParticleEffect;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

import java.util.HashMap;
import java.util.Map;

public class BorisYeltsin extends BaseZombie {
    public static final Map<Entity, String> DIALOUGE_BOSS;
    private final boolean isEating;
    private final boolean isBowing;
    private final boolean EatingCooldown;
    private final boolean CDDR;

    public BorisYeltsin() {
        this.isEating = false;
        this.isBowing = false;
        this.EatingCooldown = false;
        this.CDDR = false;
    }

    @Override
    public String getEntityName() {
        return Sputnik.trans("&4☠ &c&lBoris Yeltsin");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E9;
    }

    @Override
    public double getDamageDealt() {
        return 450000.0;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        net.minecraft.server.v1_8_R3.Entity e = ((CraftEntity) entity).getHandle();
        double height;
        double height_ = height = e.getBoundingBox().e - e.getBoundingBox().b;
        ArmorStand hologram_d = (ArmorStand) entity.getWorld().spawn(entity.getLocation().add(0.0, height + 0.22, 0.0), (Class) ArmorStand.class);
        hologram_d.setVisible(false);
        hologram_d.setGravity(false);
        hologram_d.setSmall(false);
        hologram_d.setMarker(true);
        hologram_d.setBasePlate(false);
        hologram_d.setCustomNameVisible(true);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    hologram_d.remove();
                    this.cancel();
                    return;
                }
                if (DIALOUGE_BOSS.containsKey(entity)) {
                    hologram_d.setCustomNameVisible(true);
                    hologram_d.setCustomName(Sputnik.trans(DIALOUGE_BOSS.get(entity)));
                } else {
                    hologram_d.setCustomNameVisible(false);
                    hologram_d.setCustomName("");
                }
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.22, 0.0));
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.22, 0.0));
                hologram_d.teleport(entity.getLocation().clone().add(0.0, height + 0.22, 0.0));
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
        PlayerDisguise pl = Sputnik.applyPacketNPC(entity, "eyJ0aW1lc3RhbXAiOjE1NzI3MDIwMjY2MzEsInByb2ZpbGVJZCI6ImZkNjBmMzZmNTg2MTRmMTJiM2NkNDdjMmQ4NTUyOTlhIiwicHJvZmlsZU5hbWUiOiJSZWFkIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lZWZlMWM0MmU4ZDk1NGExNzA3ZGU4ZTdkYmIxZmQwODFmNDkxMTFhZDM3M2E3YzYxNGQ2NzBmYWQ2NzM5ZTBhIn19fQ==", "B8V2ZUnrBe1u7/p4AB2XLndD4zfazlkkdTtUzehv8Sxz2EzNCdHOFMc2BVP4dOi/E9keh+temKYZO9Wgz/yeZ2WnE79UaXSMjzL8g79LL438x7zRxXgFcnEMDMWCyyFbVDauBXe/MP2zSwCZx/r+gc+qvDluIZ2DSCKGsKzmv2w+sIkA1U6xDhbMRRF3ByIrPwZ2GEblhn2ddtIZ11gBPuYkLfbTal4Iq3aR2+FMR6BsT1b6n0d0LKGV4z6Qz9pQXSNhKwPx9yMMLH/60s7O5vZXnvku3J/J0NuPYVKtaNAbDVI+byoirqli3wy7Ui4cljmusCqijULkHgPQ8sndsrJW4LZTSyEC/yWqeTS1ewsPjyMewPPGYUBqG2YoiV9le77Ufj3fyFdTTSAlaie+6ZnDHcKDE1aLqhHahPsfcp58tyXLDIkuFgjR3IyVkaN4xvGY1WonEB6xaJaTWrVi03HZhQW16BxFgxaA9j/SGcwnEoUVRaeVNfcou1FfnrbE8SlvzxHtqP4qq2gwE6QcRXqD7ef3yEsaDDvW3JajgCpXJtTKnEWbOKs7mZYFXQJ3a+kXpdN+KF8+k9iwA6xqVGCtzING3Zq8TrrfukCOywu0kGEg1x5gFdmVwbm9JoDfJd2r4Red36G+Hu8JeBn/ZYiGxBAAdvozolcH32ZrIgc=", true);
        PlayerWatcher skywatch = pl.getWatcher();
        LivingEntity target = ((CraftZombie) entity).getTarget();
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 60);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
                    if (BorisYeltsin.this.isEating) {
                        continue;
                    }
                    if (BorisYeltsin.this.isBowing) {
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
                    if (3 == target.getNoDamageTicks()) {
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
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        Entity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(null, null, null, null, null);
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

    public void sd_(String message, Entity e, int delay) {
        if (e.isDead()) {
            return;
        }
        SUtil.delay(() -> this.say(message, e), delay);
    }

    public void say(String message, Entity e) {
        if (e.isDead()) {
            return;
        }
        DIALOUGE_BOSS.put(e, message);
        for (Player p : e.getWorld().getPlayers()) {
            p.sendMessage(Sputnik.trans("&4[BOSS] Boris Yeltsin&f: " + message));
        }
    }

    public void cd_(Entity e) {
        DIALOUGE_BOSS.remove(e);
    }

    static {
        DIALOUGE_BOSS = new HashMap<Entity, String>();
    }
}
