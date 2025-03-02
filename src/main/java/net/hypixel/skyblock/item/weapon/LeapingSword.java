/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.item.weapon;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.user.PlayerStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LeapingSword
implements ToolStatistics,
MaterialFunction,
Ability {
    @Override
    public int getBaseDamage() {
        return 150;
    }

    @Override
    public double getBaseStrength() {
        return 100.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.25;
    }

    @Override
    public String getDisplayName() {
        return "Leaping Sword";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
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
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Leap";
    }

    @Override
    public String getAbilityDescription() {
        return "Leap into the air and deal " + ChatColor.RED + "350 " + ChatColor.GRAY + "damage to any nearby enemies upon landing on the ground. Damaged enemies will also be frozen for 1 second.";
    }

    @Override
    public void onAbilityUse(final Player player, SItem sItem) {
        if (!Sputnik.tpAbilUsable(player)) {
            return;
        }
        player.setVelocity(player.getLocation().getDirection().multiply(3).setY(2));
        new BukkitRunnable(){

            public void run() {
                if (player.getLocation().subtract(0.0, 0.5, 0.0).getBlock().getType() == Material.AIR) {
                    return;
                }
                player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 2.0f, 1.0f);
                player.playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, (Object)Effect.EXPLOSION_LARGE.getData());
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                int manaPool = SUtil.blackMagic(100.0 + statistics.getIntelligence().addAll());
                for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation().add(player.getLocation().getDirection().multiply(3.0)), 5.0, 5.0, 5.0)) {
                    if (!(entity instanceof LivingEntity) || entity instanceof Player || entity instanceof EnderDragon || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity.hasMetadata("GiantSword")) continue;
                    User user = User.getUser(player.getUniqueId());
                    int baseMagicDmg = 350;
                    PlayerInventory inv = player.getInventory();
                    SItem helmet = SItem.find(inv.getHelmet());
                    if (helmet != null) {
                        if (helmet.getType() == SMaterial.DARK_GOGGLES) {
                            baseMagicDmg += baseMagicDmg * 25 / 100;
                        } else if (helmet.getType() == SMaterial.SHADOW_GOGGLES) {
                            baseMagicDmg += baseMagicDmg * 35 / 100;
                        } else if (helmet.getType() == SMaterial.WITHER_GOGGLES) {
                            baseMagicDmg += baseMagicDmg * 45 / 100;
                        }
                    }
                    double baseDamage = baseMagicDmg * (manaPool / 100 + 1);
                    final ArmorStand stands = (ArmorStand)new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]).getEntity();
                    stands.setCustomName("" + ChatColor.GRAY + (int)baseDamage);
                    stands.setCustomNameVisible(true);
                    stands.setGravity(false);
                    stands.setVisible(false);
                    new BukkitRunnable(){

                        public void run() {
                            stands.remove();
                            this.cancel();
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 30L);
                    user.damageEntity((Damageable)entity, baseDamage);
                }
                this.cancel();
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 10L, 2L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 20;
    }

    @Override
    public int getManaCost() {
        return 50;
    }
}

