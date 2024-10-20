/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.minecraft.server.v1_8_R3.EnumParticle
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.item.weapon;

import java.util.Set;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

public class Excrarion
implements ToolStatistics,
MaterialFunction,
Ability {
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
        return "Teleports " + ChatColor.GREEN + "10 blocks" + ChatColor.GRAY + " ahead of you. Then implode dealing " + ChatColor.RED + "20,000" + ChatColor.GRAY + " damage to nearby enemies. Also applies the " + ChatColor.GOLD + "ultra" + ChatColor.GOLD + " wither shield" + ChatColor.GRAY + " scroll ability reducing damage taken and granting an " + ChatColor.GOLD + "\u2764 " + ChatColor.GOLD + "Absorption" + ChatColor.GRAY + " shield for " + ChatColor.YELLOW + "5" + ChatColor.GRAY + " seconds.";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        double entityTotal = 0.0;
        int j2 = 0;
        try {
            int f_ = 10;
            for (int range = 1; range < 10; ++range) {
                Location location = player.getTargetBlock((Set)null, range).getLocation();
                if (location.getBlock().getType() == Material.AIR) continue;
                f_ = range;
                break;
            }
            Location location2 = player.getTargetBlock((Set)null, f_ - 1).getLocation();
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
            for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 6.0, 6.0, 6.0)) {
                if (entity.isDead() || !(entity instanceof LivingEntity) || entity.hasMetadata("GiantSword") || entity.hasMetadata("NoAffect") || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand) continue;
                ++j2;
                double baseDamage = Sputnik.calMagicDamage(player, 20000, 0.3);
                User user = User.getUser(player.getUniqueId());
                if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                    int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                    if (defensepercent > 100) {
                        defensepercent = 100;
                    }
                    baseDamage -= baseDamage * (double)defensepercent / 100.0;
                }
                user.damageEntityIgnoreShield((Damageable)entity, (int)baseDamage);
                PlayerListener.spawnDamageInd(entity, baseDamage, false);
                entityTotal += baseDamage;
            }
        } catch (IllegalStateException f_) {
            // empty catch block
        }
        player.playSound(player.getLocation(), Sound.EXPLODE, 3.0f, 2.0f);
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float)player.getLocation().getX(), (float)player.getLocation().getY(), (float)player.getLocation().getZ(), 0.0f, 0.0f, 0.0f, 7.0f, 6, new int[0]);
        for (Player online : player.getWorld().getPlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket((Packet)packet);
        }
        if (j2 > 0) {
            if (j2 == 1) {
                player.sendMessage(ChatColor.GRAY + "Your Dimoon Implosion hit " + ChatColor.RED + j2 + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(entityTotal) + ChatColor.GRAY + " damage.");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your Dimoon Implosion hit " + ChatColor.RED + j2 + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(entityTotal) + ChatColor.GRAY + " damage.");
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

    private void createCircle(Player player, double radius, int distance) {
        Vector dist = player.getEyeLocation().getDirection().multiply(distance);
        Location mid = player.getEyeLocation().add(dist);
        int particles = 18;
        for (int i2 = 0; i2 < particles; ++i2) {
            double angle = Math.PI * 2 * (double)i2 / (double)particles;
            double x2 = Math.cos(angle) * radius;
            double y2 = Math.sin(angle) * radius;
            Vector v2 = this.rotateAroundAxisX(new Vector(x2, y2, 0.0), player.getEyeLocation().getPitch());
            v2 = this.rotateAroundAxisY(v2, player.getEyeLocation().getYaw());
            Location temp = mid.clone().add(v2);
            player.getWorld().spigot().playEffect(temp, Effect.FLAME, 0, 1, 1.0f, 1.0f, 1.0f, 0.0f, 0, 64);
        }
    }

    private Vector rotateAroundAxisX(Vector v2, double angle) {
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y2 = v2.getY() * cos - v2.getZ() * sin;
        double z2 = v2.getY() * sin + v2.getZ() * cos;
        return v2.setY(y2).setZ(z2);
    }

    private Vector rotateAroundAxisY(Vector v2, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x2 = v2.getX() * cos + v2.getZ() * sin;
        double z2 = v2.getX() * -sin + v2.getZ() * cos;
        return v2.setX(x2).setZ(z2);
    }
}

