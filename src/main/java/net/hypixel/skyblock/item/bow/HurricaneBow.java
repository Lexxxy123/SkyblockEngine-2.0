/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.hypixel.skyblock.item.bow;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.item.bow.BowFunction;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.projectiles.ProjectileSource;

public class HurricaneBow
implements ToolStatistics,
BowFunction,
Ability {
    @Override
    public String getAbilityName() {
        return "Tempest";
    }

    @Override
    public String getAbilityDescription() {
        return "The more kills you get using this bow the more powerful it becomes! Reach 250 kills to unlock its full potential.";
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.NO_ACTIVATION;
    }

    @Override
    public String getDisplayName() {
        return "Hurricane Bow";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.RANGED_WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.BOW;
    }

    @Override
    public int getBaseDamage() {
        return 120;
    }

    @Override
    public double getBaseStrength() {
        return 50.0;
    }

    @Override
    public boolean displayKills() {
        return true;
    }

    @Override
    public void onBowShoot(SItem bow, EntityShootBowEvent e2) {
        Location l2;
        Player shooter = (Player)e2.getEntity();
        int kills = bow.getDataInt("kills");
        Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
        float speed = e2.getForce() * 3.0f;
        if (kills >= 20) {
            l2 = location.clone();
            l2.setYaw(location.getYaw() - 15.0f);
            shooter.getWorld().spawnArrow(l2, l2.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
        }
        if (kills >= 50) {
            l2 = location.clone();
            l2.setYaw(location.getYaw() + 15.0f);
            shooter.getWorld().spawnArrow(l2, l2.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
        }
        if (kills >= 100) {
            l2 = location.clone();
            l2.setYaw(location.getYaw() - 30.0f);
            shooter.getWorld().spawnArrow(l2, l2.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
        }
        if (kills >= 250) {
            l2 = location.clone();
            l2.setYaw(location.getYaw() + 30.0f);
            shooter.getWorld().spawnArrow(l2, l2.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
        }
    }

    @Override
    public void onBowHit(Entity hit, Player shooter, Arrow arrow, SItem weapon, AtomicDouble finalDamage) {
        if (!(hit instanceof LivingEntity)) {
            return;
        }
        if (hit instanceof Villager) {
            return;
        }
        if (((LivingEntity)hit).getHealth() - finalDamage.get() <= 0.0) {
            weapon.setKills(weapon.getDataInt("kills") + 1);
        }
    }
}

