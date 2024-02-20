package in.godspunky.skyblock.entity.dungeons.regularentity;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.SMaterial;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

public class ZombieKnight extends BaseZombie {
    @Override
    public String getEntityName() {
        return "Zombie Knight";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 2050000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 61);
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
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
        return new SEntityEquipment(null, SItem.of(SMaterial.GOLDEN_HELMET).getStack(), SItem.of(SMaterial.GOLDEN_CHESTPLATE).getStack(), SItem.of(SMaterial.GOLDEN_LEGGINGS).getStack(), SItem.of(SMaterial.GOLDEN_BOOTS).getStack());
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e) {
        ((LivingEntity) e.getDamager()).setHealth(Math.min(((LivingEntity) e.getDamager()).getMaxHealth(), ((LivingEntity) e.getDamager()).getHealth() + ((LivingEntity) e.getDamager()).getMaxHealth() / 100.0));
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
        return 0.25;
    }
}
