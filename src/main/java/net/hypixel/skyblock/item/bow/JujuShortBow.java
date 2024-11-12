/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.hypixel.skyblock.item.bow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.item.bow.BowFunction;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.util.InventoryUpdate;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

public class JujuShortBow
implements ToolStatistics,
BowFunction {
    public static final Map<UUID, Boolean> USABLE_JUJU = new HashMap<UUID, Boolean>();

    @Override
    public String getDisplayName() {
        return "Juju Shortbow";
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
        return false;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e2) {
        SItem sItem = SItem.find(e2.getPlayer().getItemInHand());
        Enchantment aiming = sItem.getEnchantment(EnchantmentType.AIMING);
        Player shooter = e2.getPlayer();
        if (shooter.getPlayer().getInventory().contains(Material.ARROW, 1) || shooter.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (e2.getAction() == Action.RIGHT_CLICK_AIR || e2.getAction() == Action.RIGHT_CLICK_BLOCK) {
                shooter.updateInventory();
                if (USABLE_JUJU.containsKey(shooter.getUniqueId()) && !USABLE_JUJU.get(shooter.getUniqueId()).booleanValue()) {
                    return;
                }
                if (shooter.getGameMode() != GameMode.CREATIVE) {
                    InventoryUpdate.removeInventoryItems(shooter.getInventory(), Material.ARROW, 1);
                }
                shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
                Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
                Location l2 = location.clone();
                l2.setYaw(location.getYaw());
                Arrow a2 = shooter.getWorld().spawnArrow(l2, l2.getDirection(), 2.1f, 1.5f);
                a2.setShooter((ProjectileSource)shooter);
                USABLE_JUJU.put(shooter.getUniqueId(), false);
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(shooter.getUniqueId());
                double atkSpeed = Math.min(100L, Math.round(statistics.getAttackSpeed().addAll()));
                SUtil.delay(() -> USABLE_JUJU.put(shooter.getUniqueId(), true), (long)(16.0 / (1.0 + atkSpeed / 100.0)));
            } else if (e2.getAction() == Action.LEFT_CLICK_AIR || e2.getAction() == Action.LEFT_CLICK_BLOCK) {
                shooter.updateInventory();
                if (USABLE_JUJU.containsKey(shooter.getUniqueId()) && !USABLE_JUJU.get(shooter.getUniqueId()).booleanValue()) {
                    return;
                }
                if (shooter.getGameMode() != GameMode.CREATIVE) {
                    InventoryUpdate.removeInventoryItems(shooter.getInventory(), Material.ARROW, 1);
                }
                shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
                Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
                Location l3 = location.clone();
                l3.setYaw(location.getYaw());
                Arrow a2 = shooter.getWorld().spawnArrow(l3, l3.getDirection(), 2.2f, 1.6f);
                a2.setShooter((ProjectileSource)shooter);
                USABLE_JUJU.put(shooter.getUniqueId(), false);
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(shooter.getUniqueId());
                double atkSpeed = Math.min(100L, Math.round(statistics.getAttackSpeed().addAll()));
                SUtil.delay(() -> USABLE_JUJU.put(shooter.getUniqueId(), true), (long)(8.0 / (1.0 + atkSpeed / 100.0)));
            } else {
                SLog.severe("[JUJU-SHORTBOW] " + shooter.getUniqueId() + " <- Error Occurred on this user. Something messed up bruh");
            }
        }
    }

    @Override
    public void onBowShoot(SItem bow, EntityShootBowEvent e2) {
        Player player = (Player)e2.getEntity();
        e2.setCancelled(true);
        player.updateInventory();
    }
}

