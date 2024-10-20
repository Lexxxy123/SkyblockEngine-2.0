/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.item.orb;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.orb.OrbBuff;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public abstract class PowerOrb
implements SkullStatistics,
MaterialFunction,
Ability,
OrbBuff {
    private static final Map<UUID, ArmorStand> USING_POWER_ORB_MAP = new HashMap<UUID, ArmorStand>();
    private static final Map<UUID, PowerOrbInstance> POWER_ORB_MAP = new HashMap<UUID, PowerOrbInstance>();

    @Override
    public String getAbilityName() {
        return "Deploy";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 5;
    }

    @Override
    public boolean displayCooldown() {
        return false;
    }

    @Override
    public boolean displayUsage() {
        return false;
    }

    @Override
    public int getManaCost() {
        return -2;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        Location sloc = player.getLocation().clone().add(player.getLocation().getDirection().multiply(1.5));
        this.destroyArmorStandWithUUID(player.getUniqueId(), player.getWorld());
        this.r(player, sloc);
        if (POWER_ORB_MAP.containsKey(player.getUniqueId())) {
            PowerOrbInstance instance = POWER_ORB_MAP.get(player.getUniqueId());
            ArmorStand s2 = instance.getArmorStand();
            s2.remove();
            player.sendMessage(ChatColor.YELLOW + "Your previous " + instance.getColoredName() + ChatColor.YELLOW + " was removed!");
        }
        SEntity sEntity = new SEntity(sloc, SEntityType.VELOCITY_ARMOR_STAND, new Object[0]);
        final ArmorStand stand = (ArmorStand)sEntity.getEntity();
        POWER_ORB_MAP.put(player.getUniqueId(), new PowerOrbInstance(){

            @Override
            public String getColoredName() {
                return sItem.getRarity().getColor() + sItem.getType().getDisplayName(sItem.getVariant());
            }

            @Override
            public ArmorStand getArmorStand() {
                return stand;
            }
        });
        stand.setVisible(false);
        final AtomicInteger seconds = new AtomicInteger((int)(this.getOrbLifeTicks() / 20L));
        stand.setCustomName(sItem.getRarity().getColor() + (this.getCustomOrbName() == null ? this.getBuffName() : this.getCustomOrbName()) + " " + ChatColor.YELLOW + seconds.get() + "s");
        stand.setCustomNameVisible(true);
        stand.setHelmet(SUtil.getSkull(this.getURL(), null));
        stand.setVelocity(new Vector(0.0, 0.1, 0.0));
        stand.setMetadata(player.getUniqueId().toString() + "_powerorb", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        new BukkitRunnable(){

            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                Vector velClone = stand.getVelocity().clone();
                stand.setVelocity(new Vector(0.0, velClone.getY() < 0.0 ? 0.1 : -0.1, 0.0));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 25L, 25L);
        new BukkitRunnable(){

            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                Location location = stand.getLocation();
                location.setYaw(stand.getLocation().getYaw() + 15.0f);
                stand.teleport(location);
                PowerOrb.this.playEffect(stand.getEyeLocation().clone().add(stand.getLocation().getDirection().divide(new Vector(2, 2, 2))));
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
        new BukkitRunnable(){

            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                int c2 = 0;
                for (Entity entity : stand.getNearbyEntities(18.0, 18.0, 18.0)) {
                    if (!(entity instanceof Player)) continue;
                    final Player p2 = (Player)entity;
                    if (c2 >= 5) break;
                    ++c2;
                    if (USING_POWER_ORB_MAP.containsKey(p2.getUniqueId()) && !((ArmorStand)USING_POWER_ORB_MAP.get(p2.getUniqueId())).equals(stand)) continue;
                    USING_POWER_ORB_MAP.put(p2.getUniqueId(), stand);
                    new BukkitRunnable(){

                        public void run() {
                            USING_POWER_ORB_MAP.remove(p2.getUniqueId());
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 20L);
                    PowerOrb.this.buff(p2);
                    for (int i2 = 0; i2 < 8; ++i2) {
                        PowerOrb.this.playEffect(p2.getLocation().add(SUtil.random(-0.5, 0.5), 0.1, SUtil.random(-0.5, 0.5)));
                    }
                }
                stand.setCustomName(sItem.getRarity().getColor() + (PowerOrb.this.getCustomOrbName() == null ? PowerOrb.this.getBuffName() : PowerOrb.this.getCustomOrbName()) + " " + ChatColor.YELLOW + Math.max(0, seconds.decrementAndGet()) + "s");
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 20L, 20L);
        new BukkitRunnable(){

            public void run() {
                POWER_ORB_MAP.remove(player.getUniqueId());
                stand.remove();
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), this.getOrbLifeTicks() + 15L);
    }

    protected abstract void buff(Player var1);

    protected abstract long getOrbLifeTicks();

    protected abstract void playEffect(Location var1);

    public void destroyArmorStandWithUUID(UUID uuid, World w2) {
        String uuidString = uuid.toString() + "_powerorb";
        for (Entity e2 : w2.getEntities()) {
            if (!e2.hasMetadata(uuidString)) continue;
            e2.remove();
        }
    }

    public void r(Player player, Location loc1) {
        Location loc2 = loc1.clone().add(0.0, 1.0, 0.0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
    }

    private static interface PowerOrbInstance {
        public String getColoredName();

        public ArmorStand getArmorStand();
    }
}

