package in.godspunky.skyblock.item.orb;

import in.godspunky.skyblock.item.*;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.SUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PowerOrb implements SkullStatistics, MaterialFunction, Ability, OrbBuff {
    private static final Map<UUID, ArmorStand> USING_POWER_ORB_MAP;
    private static final Map<UUID, PowerOrbInstance> POWER_ORB_MAP;

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
        final Location sloc = player.getLocation().clone().add(player.getLocation().getDirection().multiply(1.5));
        this.destroyArmorStandWithUUID(player.getUniqueId(), player.getWorld());
        this.r(player, sloc);
        if (PowerOrb.POWER_ORB_MAP.containsKey(player.getUniqueId())) {
            final PowerOrbInstance instance = PowerOrb.POWER_ORB_MAP.get(player.getUniqueId());
            final ArmorStand s = instance.getArmorStand();
            s.remove();
            player.sendMessage(ChatColor.YELLOW + "Your previous " + instance.getColoredName() + ChatColor.YELLOW + " was removed!");
        }
        final SEntity sEntity = new SEntity(sloc, SEntityType.VELOCITY_ARMOR_STAND);
        final ArmorStand stand = (ArmorStand) sEntity.getEntity();
        PowerOrb.POWER_ORB_MAP.put(player.getUniqueId(), new PowerOrbInstance() {
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
        final AtomicInteger seconds = new AtomicInteger((int) (this.getOrbLifeTicks() / 20L));
        stand.setCustomName(sItem.getRarity().getColor() + ((this.getCustomOrbName() == null) ? this.getBuffName() : this.getCustomOrbName()) + " " + ChatColor.YELLOW + seconds.get() + "s");
        stand.setCustomNameVisible(true);
        stand.setHelmet(SUtil.getSkull(this.getURL(), null));
        stand.setVelocity(new Vector(0.0, 0.1, 0.0));
        stand.setMetadata(player.getUniqueId().toString() + "_powerorb", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        new BukkitRunnable() {
            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                final Vector velClone = stand.getVelocity().clone();
                stand.setVelocity(new Vector(0.0, (velClone.getY() < 0.0) ? 0.1 : -0.1, 0.0));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 25L, 25L);
        new BukkitRunnable() {
            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                final Location location = stand.getLocation();
                location.setYaw(stand.getLocation().getYaw() + 15.0f);
                stand.teleport(location);
                PowerOrb.this.playEffect(stand.getEyeLocation().clone().add(stand.getLocation().getDirection().divide(new Vector(2, 2, 2))));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 1L);
        new BukkitRunnable() {
            public void run() {
                if (stand.isDead()) {
                    this.cancel();
                    return;
                }
                int c = 0;
                for (final Entity entity : stand.getNearbyEntities(18.0, 18.0, 18.0)) {
                    if (!(entity instanceof Player)) {
                        continue;
                    }
                    final Player p = (Player) entity;
                    if (c >= 5) {
                        break;
                    }
                    ++c;
                    if (PowerOrb.USING_POWER_ORB_MAP.containsKey(p.getUniqueId()) && !PowerOrb.USING_POWER_ORB_MAP.get(p.getUniqueId()).equals(stand)) {
                        continue;
                    }
                    PowerOrb.USING_POWER_ORB_MAP.put(p.getUniqueId(), stand);
                    new BukkitRunnable() {
                        public void run() {
                            PowerOrb.USING_POWER_ORB_MAP.remove(p.getUniqueId());
                        }
                    }.runTaskLater(SkySimEngine.getPlugin(), 20L);
                    PowerOrb.this.buff(p);
                    for (int i = 0; i < 8; ++i) {
                        PowerOrb.this.playEffect(p.getLocation().add(SUtil.random(-0.5, 0.5), 0.1, SUtil.random(-0.5, 0.5)));
                    }
                }
                stand.setCustomName(sItem.getRarity().getColor() + ((PowerOrb.this.getCustomOrbName() == null) ? PowerOrb.this.getBuffName() : PowerOrb.this.getCustomOrbName()) + " " + ChatColor.YELLOW + Math.max(0, seconds.decrementAndGet()) + "s");
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 20L, 20L);
        new BukkitRunnable() {
            public void run() {
                PowerOrb.POWER_ORB_MAP.remove(player.getUniqueId());
                stand.remove();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), this.getOrbLifeTicks() + 15L);
    }

    protected abstract void buff(final Player p0);

    protected abstract long getOrbLifeTicks();

    protected abstract void playEffect(final Location p0);

    public void destroyArmorStandWithUUID(final UUID uuid, final World w) {
        final String uuidString = uuid.toString() + "_powerorb";
        for (final Entity e : w.getEntities()) {
            if (e.hasMetadata(uuidString)) {
                e.remove();
            }
        }
    }

    public void r(final Player player, final Location loc1) {
        final Location loc2 = loc1.clone().add(0.0, 1.0, 0.0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
        player.playEffect(loc2, Effect.POTION_SWIRL, 0);
    }

    static {
        USING_POWER_ORB_MAP = new HashMap<UUID, ArmorStand>();
        POWER_ORB_MAP = new HashMap<UUID, PowerOrbInstance>();
    }

    private interface PowerOrbInstance {
        String getColoredName();

        ArmorStand getArmorStand();
    }
}
