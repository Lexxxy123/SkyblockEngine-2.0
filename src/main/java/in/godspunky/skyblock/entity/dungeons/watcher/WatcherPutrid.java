package in.godspunky.skyblock.entity.dungeons.watcher;

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
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

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
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        final HeadsOnWall h = new HeadsOnWall(EnumWatcherType.PUTRID);
        final PlayerDisguise p = Sputnik.applyPacketNPC(entity, h.value, h.signature, true);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 99);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("WATCHER_E", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        p.setReplaceSounds(false);
        new BukkitRunnable() {
            public void run() {
                final EntityLiving nms = ((CraftLivingEntity) entity).getHandle();
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                for (final Entity entities : entity.getWorld().getNearbyEntities(entity.getLocation().add(entity.getLocation().getDirection().multiply(1.0)), 1.5, 1.5, 1.5)) {
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
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 3L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(SItem.of(SMaterial.GOLD_SWORD).getStack(), null, SItem.of(SMaterial.IRON_CHESTPLATE).getStack(), SItem.of(SMaterial.IRON_LEGGINGS).getStack(), null);
    }

    @Override
    public void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
        final Entity en = sEntity.getEntity();
        final Vector v = new Vector(0, 0, 0);
        SUtil.delay(() -> en.setVelocity(v), 1L);
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e) {
        final LivingEntity killed = (LivingEntity) e.getEntity();
        if (SUtil.random(0, 20) == 1) {
            killed.getLocation().getWorld().playEffect(killed.getLocation(), Effect.EXPLOSION_HUGE, 0);
            killed.getWorld().playSound(killed.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
            killed.getNearbyEntities(5.0, 5.0, 5.0).forEach(entity -> {
                if (entity instanceof Player) {
                    final Player p = (Player) entity;
                    p.damage(1.0E-5);
                    User.getUser(p.getUniqueId()).damage(p.getMaxHealth() * 10.0 / 100.0, EntityDamageEvent.DamageCause.ENTITY_ATTACK, killed);
                }
            });
        }
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
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
