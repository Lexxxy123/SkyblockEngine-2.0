/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.hypixel.skyblock.item.bow;

import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.item.bow.BowFunction;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.projectiles.ProjectileSource;

public class RunaansBow
implements ToolStatistics,
BowFunction,
Ability {
    @Override
    public String getAbilityName() {
        return "Triple-shot";
    }

    @Override
    public String getAbilityDescription() {
        return "Shoots 3 arrows at a time! The 2 extra arrows deal " + ChatColor.GREEN + "40% " + ChatColor.GRAY + "percent of the damage and home to targets.";
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
        return "Runaan's Bow";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
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
        return 160;
    }

    @Override
    public double getBaseStrength() {
        return 50.0;
    }

    @Override
    public void onBowShoot(SItem bow, EntityShootBowEvent e) {
        Player shooter = (Player)e.getEntity();
        Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
        float speed = e.getForce() * 3.0f;
        Location l = location.clone();
        l.setYaw(location.getYaw() - 30.0f);
        shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
        l.setYaw(location.getYaw() + 30.0f);
        shooter.getWorld().spawnArrow(l, l.getDirection(), speed, 1.0f).setShooter((ProjectileSource)shooter);
    }
}

