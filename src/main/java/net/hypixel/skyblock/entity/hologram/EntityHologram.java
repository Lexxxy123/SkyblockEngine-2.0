package net.hypixel.skyblock.entity.hologram;

import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.hologram.Hologram;
import net.hypixel.skyblock.entity.EntityStatistics;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EntityHologram {
    @Getter
    private final static Set<EntityHologram> ENTITY_HOLOGRAMS = new HashSet<>();
    @Getter
    private List<Hologram> holograms;

    private final static Map<Entity, ArrayList<UUID>> FIRST_STRIKE_MAP = new HashMap<Entity, ArrayList<UUID>>();

    private final LivingEntity entity;
    private final SEntityType specType;
    private final Object[] params;

   public EntityHologram(LivingEntity entity , final SEntityType specType, final Object... params){
        this.entity = entity;
        this.specType = specType;
        this.params = params;
        this.holograms = new ArrayList<>();
    }

    public void start() {
        ENTITY_HOLOGRAMS.add(this);
        Hologram hologram1 = new Hologram(entity.getLocation());
        hologram1.setSmall(true);
        hologram1.setBasePlate(true);
        hologram1.setCustomNameVisible(false);
        addHologram(hologram1);

        hologram1.mount(((CraftEntity) entity).getHandle());
        if (!entity.hasMetadata("LD")) {
            hologram1.remove();
            removeHologram(hologram1);
        }
        FIRST_STRIKE_MAP.put(entity, new ArrayList<>());
        if (entity.hasMetadata("notDisplay")) {
            return;
        }
        if (entity.getType() == EntityType.ARMOR_STAND) {
            return;
        }
        if (entity.getType() == EntityType.ENDER_DRAGON) {
            return;
        }
        if (entity.getType() == EntityType.ENDER_CRYSTAL) {
            return;
        }

        final net.minecraft.server.v1_8_R3.Entity e = ((CraftEntity) entity).getHandle();
        double height_ = e.getBoundingBox().e - e.getBoundingBox().b;
        final Object instance = specType.instance(params);
        final EntityStatistics statistics = (EntityStatistics) instance;
        if (entity.getType() == EntityType.SKELETON || entity.hasMetadata("SKEL")) {
            height_ += 0.2;
        }
        if (entity.getType() == EntityType.SKELETON && ((CraftSkeleton) entity).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
            height_ -= 0.2;
        }
        if (entity.hasMetadata("LD")) {
            height_ -= 0.144;
        }
        if (entity.hasMetadata("highername")) {
            height_ += 10.5;
        }
        if (entity.hasMetadata("NNP")) {
            height_ += 0.8;
        }
        if (entity.hasMetadata("NNPS")) {
            height_ += 0.8;
        }
        final double height = height_;

        Hologram hologram2 = new Hologram(entity.getLocation().add(0.0, height, 0.0));
        addHologram(hologram2);

        boolean hideLVL = entity.hasMetadata("WATCHER_E");

        new BukkitRunnable() {
            @Override
            public void run() {
                hologram2.setCustomNameVisible(!entity.hasPotionEffect(PotionEffectType.INVISIBILITY));

                if (entity.isDead()) {
                    if (entity.hasMetadata("NNP")) {
                        hologram2.remove();
                        removeHologram(hologram2);
                    }
                    if (entity.hasMetadata("h_sadan")) {
                        hologram2.remove();
                        removeHologram(hologram2);
                    }
                    FIRST_STRIKE_MAP.remove(entity);
                    hologram2.setText(Sputnik.trans(Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), hideLVL))));
                    new BukkitRunnable() {
                        public void run() {
                            hologram2.remove();
                            hologram1.remove();
                            removeHologram(hologram1);
                            removeHologram(hologram2);
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 20L);
                }
                if (!hologram2.isAlive()) {
                    this.cancel();
                    return;
                }
                if (entity.hasMetadata("RMHN")) {
                    hologram1.remove();
                    removeHologram(hologram1);
                } else {
                    entity.setPassenger(hologram1.getBukkitEntity());
                }
                hologram2.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                hologram2.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                if (!entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    if (entity.hasMetadata("Boss")) {
                        hologram2.setText(Sputnik.trans("&e﴾ " + Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), true)) + " &e﴿"));
                    } else if (entity.hasMetadata("NNP")) {
                        hologram2.setText(statistics.getEntityName());
                    } else {
                        hologram2.setText(Sputnik.trans(Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), hideLVL))));
                    }
                }
            }
        }.runTaskTimerAsynchronously(SkyBlock.getPlugin(), 0L, 0L);
    }


    private void addHologram(Hologram hologram){
       holograms.add(hologram);
    }

    private void removeHologram(Hologram hologram){
       if (!holograms.contains(hologram)) return;
       holograms.remove(hologram);
    }


    public boolean inRangeOf(Player player) {
        if (player == null) return false;
        if (!player.getWorld().equals(entity.getLocation().getWorld())) {
            // No need to continue our checks, they are in different worlds.
            return false;
        }

        // If Bukkit doesn't track the entity anymore, bypass the hiding distance variable.
        double hideDistance = 20;
        double distanceSquared = player.getLocation().distanceSquared(entity.getLocation());
        double bukkitRange = Bukkit.getViewDistance() << 4;

        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }


}
