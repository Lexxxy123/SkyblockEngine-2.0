package net.hypixel.skyblock.entity.dungeons.watcher;

import com.google.common.util.concurrent.AtomicDouble;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

public class WatcherPutrid extends BaseZombie {
    @Override
    public String getEntityName() {
        return Sputnik.trans("&4&lMaster Putrid");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 5000000.0;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        HeadsOnWall h = new HeadsOnWall(EnumWatcherType.PUTRID);
        PlayerDisguise p = Sputnik.applyPacketNPC(entity, h.value, h.signature, true);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 99);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("WATCHER_E", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        p.setReplaceSounds(false);
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
        return new SEntityEquipment(SItem.of(SMaterial.GOLD_SWORD).getStack(), null, SItem.of(SMaterial.IRON_CHESTPLATE).getStack(), SItem.of(SMaterial.IRON_LEGGINGS).getStack(), null);
    }

    @Override
    public void onDamage(SEntity sEntity, Entity damager, EntityDamageByEntityEvent e, AtomicDouble damage) {
        Entity en = sEntity.getEntity();
        Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public void onAttack(EntityDamageByEntityEvent e) {
        LivingEntity killed = (LivingEntity) e.getEntity();
        if (1 == SUtil.random(0, 20)) {
            killed.getLocation().getWorld().playEffect(killed.getLocation(), Effect.EXPLOSION_HUGE, 0);
            killed.getWorld().playSound(killed.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
            killed.getNearbyEntities(5.0, 5.0, 5.0).forEach(entity -> {
                if (entity instanceof Player) {
                    Player p = (Player) entity;
                    p.damage(1.0E-5);
                    User.getUser(p.getUniqueId()).damage(p.getMaxHealth() * 10.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, killed);
                }
            });
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
        return 155.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.3;
    }

    @Override
    public int mobLevel() {
        return 540;
    }
}
