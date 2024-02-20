package in.godspunky.skyblock.item.weapon;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.util.EntityManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.listener.PlayerListener;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

import java.util.Set;

public class Excrarion implements ToolStatistics, MaterialFunction, Ability {
    @Override
    public int getBaseDamage() {
        return 300;
    }

    @Override
    public double getBaseStrength() {
        return 140.0;
    }

    @Override
    public double getBaseFerocity() {
        return 10.0;
    }

    @Override
    public double getBaseIntelligence() {
        return 1000.0;
    }

    @Override
    public String getDisplayName() {
        return "Excrarion";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
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
    public String getAbilityName() {
        return "Dimoon Impact";
    }

    @Override
    public String getAbilityDescription() {
        return "Teleports " + ChatColor.GREEN + "10 blocks" + ChatColor.GRAY + " ahead of you. Then implode dealing " + ChatColor.RED + "20,000" + ChatColor.GRAY + " damage to nearby enemies. Also applies the " + ChatColor.GOLD + "ultra" + ChatColor.GOLD + " wither shield" + ChatColor.GRAY + " scroll ability reducing damage taken and granting an " + ChatColor.GOLD + "❤ " + ChatColor.GOLD + "Absorption" + ChatColor.GRAY + " shield for " + ChatColor.YELLOW + "5" + ChatColor.GRAY + " seconds.";
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        double entityTotal = 0.0;
        int j = 0;
        try {
            int f_ = 10;
            for (int range = 1; range < 10; ++range) {
                final Location location = player.getTargetBlock((Set) null, range).getLocation();
                if (location.getBlock().getType() != Material.AIR) {
                    f_ = range;
                    break;
                }
            }
            final Location location2 = player.getTargetBlock((Set) null, f_ - 1).getLocation();
            location2.setYaw(player.getLocation().getYaw());
            location2.setPitch(player.getLocation().getPitch());
            location2.add(0.5, 0.0, 0.5);
            if (f_ != 10) {
                player.sendMessage(ChatColor.RED + "There are blocks in the way!");
            }
            if (f_ > 1) {
                Sputnik.teleport(player, location2);
            } else {
                Sputnik.teleport(player, player.getLocation());
            }
            Sputnik.witherShieldActive2(player);
            for (final Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 6.0, 6.0, 6.0)) {
                if (entity.isDead()) {
                    continue;
                }
                if (!(entity instanceof LivingEntity)) {
                    continue;
                }
                if (entity.hasMetadata("GiantSword")) {
                    continue;
                }
                if (entity.hasMetadata("NoAffect")) {
                    continue;
                }
                if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager) {
                    continue;
                }
                if (entity instanceof ArmorStand) {
                    continue;
                }
                ++j;
                double baseDamage = Sputnik.calMagicDamage(player, 20000, 0.3);
                final User user = User.getUser(player.getUniqueId());
                if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                    int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                    if (defensepercent > 100) {
                        defensepercent = 100;
                    }
                    baseDamage -= baseDamage * defensepercent / 100.0;
                }
                user.damageEntityIgnoreShield((Damageable) entity, (int) baseDamage);
                PlayerListener.spawnDamageInd(entity, baseDamage, false);
                entityTotal += baseDamage;
            }
        } catch (final IllegalStateException ex) {
        }
        player.playSound(player.getLocation(), Sound.EXPLODE, 3.0f, 2.0f);
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ(), 0.0f, 0.0f, 0.0f, 7.0f, 6);
        for (final Player online : player.getWorld().getPlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
        if (j > 0) {
            if (j == 1) {
                player.sendMessage(ChatColor.GRAY + "Your Dimoon Implosion hit " + ChatColor.RED + j + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(entityTotal) + ChatColor.GRAY + " damage.");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your Dimoon Implosion hit " + ChatColor.RED + j + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(entityTotal) + ChatColor.GRAY + " damage.");
            }
        }
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 3;
    }

    @Override
    public int getManaCost() {
        return 600;
    }

    private void createCircle(final Player player, final double radius, final int distance) {
        final Vector dist = player.getEyeLocation().getDirection().multiply(distance);
        final Location mid = player.getEyeLocation().add(dist);
        for (int particles = 18, i = 0; i < particles; ++i) {
            final double angle = 6.283185307179586 * i / particles;
            final double x = Math.cos(angle) * radius;
            final double y = Math.sin(angle) * radius;
            Vector v = this.rotateAroundAxisX(new Vector(x, y, 0.0), player.getEyeLocation().getPitch());
            v = this.rotateAroundAxisY(v, player.getEyeLocation().getYaw());
            final Location temp = mid.clone().add(v);
            player.getWorld().spigot().playEffect(temp, Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        }
    }

    private Vector rotateAroundAxisX(final Vector v, double angle) {
        angle = Math.toRadians(angle);
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double y = v.getY() * cos - v.getZ() * sin;
        final double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private Vector rotateAroundAxisY(final Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double x = v.getX() * cos + v.getZ() * sin;
        final double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }
}
