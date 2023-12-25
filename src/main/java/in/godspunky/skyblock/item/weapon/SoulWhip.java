package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.FerocityCalculation;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.Sputnik;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SoulWhip implements ToolStatistics, MaterialFunction, Ability, Ownable {
    public static final Map<UUID, Boolean> cd;
    public static final Map<Integer, Boolean> hit;

    @Override
    public String getAbilityName() {
        return "Flay";
    }

    @Override
    public String getAbilityDescription() {
        return "Flay your whip in an arc, dealing your melee damage to all enemies in its path";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        final Player p = player;
        if (!SoulWhip.cd.containsKey(p.getUniqueId())) {
            SoulWhip.cd.put(p.getUniqueId(), true);
            final Vector v = p.getLocation().getDirection().normalize().multiply(0.15);
            final Location loc = p.getLocation().clone().add(0.0, 1.55, 0.0);
            final World world = loc.getWorld();
            p.playSound(loc, Sound.CAT_HISS, 0.2f, 10.0f);
            new BukkitRunnable() {
                int count = 0;

                public void run() {
                    for (final Entity e : world.getNearbyEntities(loc, 1.0, 1.0, 1.0)) {
                        if (e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof ExperienceOrb) && !(e instanceof ArmorStand) && !(e instanceof Villager) && !e.isDead() && !e.hasMetadata("NPC") && !e.hasMetadata("GiantSword") && !SoulWhip.hit.containsKey(e.getEntityId())) {
                            SoulWhip.hit.put(e.getEntityId(), true);
                            new BukkitRunnable() {
                                public void run() {
                                    final User user = User.getUser(player.getUniqueId());
                                    final Object[] atp = Sputnik.calculateDamage(player, player, sItem.getStack(), (LivingEntity) e, false);
                                    final double finalDamage1 = (float) atp[0];
                                    PlayerListener.spawnDamageInd(e, (float) atp[2], (boolean) atp[1]);
                                    FerocityCalculation.activeFerocityTimes(player, (LivingEntity) e, (int) finalDamage1, (boolean) atp[1]);
                                    user.damageEntity((Damageable) e, finalDamage1);
                                }
                            }.runTaskLater(Skyblock.getPlugin(), 0L);
                            new BukkitRunnable() {
                                public void run() {
                                    SoulWhip.hit.remove(e.getEntityId());
                                }
                            }.runTaskLater(Skyblock.getPlugin(), 10L);
                        }
                    }
                    for (int i = 0; i < 10; ++i) {
                        loc.add(v);
                        loc.setY(loc.getY() + (50 - this.count) / 1000.0);
                        final Iterator<Entity> packetx = null;
                        PacketPlayOutWorldParticles packet;
                        if (this.count % 2 == 0) {
                            packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 3.9215687E-5f, 0.0f, 0.0f, 1.0f, 0);
                        } else {
                            packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), -0.9f, 0.2f, 0.2f, 1.0f, 0);
                        }
                        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
                        for (final Entity player : p.getNearbyEntities(10.0, 10.0, 10.0)) {
                            if (player instanceof Player) {
                                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                            }
                        }
                        ++this.count;
                    }
                    if (this.count >= 100) {
                        this.cancel();
                    }
                    if (world.getBlockAt((int) (loc.getX() - 0.5), loc.getBlockY(), (int) (loc.getZ() - 0.5)).getType() != Material.AIR) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(Skyblock.getPlugin(), 1L, 1L);
            new BukkitRunnable() {
                public void run() {
                    SoulWhip.cd.remove(p.getUniqueId());
                }
            }.runTaskLater(Skyblock.getPlugin(), 10L);
        }
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public String getDisplayName() {
        return "Soul Whip";
    }

    @Override
    public int getBaseDamage() {
        return 145;
    }

    @Override
    public double getBaseStrength() {
        return 175.0;
    }

    @Override
    public boolean displayUsage() {
        return false;
    }

    @Override
    public boolean requirementsUse(final Player player, final SItem sItem) {
        return User.getUser(player.getUniqueId()).getBCollection() < 25L;
    }

    @Override
    public String getAbilityReq() {
        return "&cYou do not have requirement to use this item!\n&cYou need at least &525 Sadan Kills &cto use it!";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }

    @Override
    public NBTTagCompound getData() {
        return Ownable.super.getData();
    }

    static {
        cd = new HashMap<UUID, Boolean>();
        hit = new HashMap<Integer, Boolean>();
    }
}
