/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.EnderDragonPart
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Giant
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.item.weapon;

import java.util.Set;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.listener.PlayerListener;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GiantSword
implements ToolStatistics,
MaterialFunction,
Ability {
    @Override
    public int getBaseDamage() {
        return 500;
    }

    @Override
    public String getDisplayName() {
        return "Giant's Sword";
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
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Giant's Slam";
    }

    @Override
    public String getAbilityDescription() {
        return "Slam your sword into the ground dealing " + ChatColor.RED + "100,000 " + ChatColor.GRAY + "damage to nearby enemies.";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
        int i2 = 0;
        double j2 = 0.0;
        Location location = player.getTargetBlock((Set)null, 6).getLocation();
        final Giant sword = (Giant)player.getWorld().spawnEntity(location, EntityType.GIANT);
        sword.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
        EntityManager.noAI((Entity)sword);
        EntityManager.noHit((Entity)sword);
        EntityManager.shutTheFuckUp((Entity)sword);
        sword.setCustomName("Dinnerbone");
        sword.setMetadata("GiantSword", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        sword.setMetadata("NoAffect", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
        final ArmorStand stand = (ArmorStand)player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(true);
        stand.setPassenger((Entity)sword);
        sword.getEquipment().setItemInHand(SItem.of(SMaterial.IRON_SWORD).getStack());
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 10.0f, 0.0f);
        player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 0.9f, 0.35f);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        for (Entity entity : sword.getWorld().getNearbyEntities(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)), 6.0, 6.0, 6.0)) {
            if (entity.isDead() || !(entity instanceof LivingEntity) || entity.hasMetadata("GiantSword") || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand) continue;
            User user = User.getUser(player.getUniqueId());
            double baseDamage = Sputnik.calculateMagicDamage(entity, player, 100000, 0.05);
            user.damageEntityIgnoreShield((Damageable)entity, baseDamage);
            ++i2;
            j2 += baseDamage;
            PlayerListener.spawnDamageInd(entity, baseDamage, false);
        }
        if (i2 > 0) {
            if (i2 == 1) {
                player.sendMessage(ChatColor.GRAY + "Your Implosion hit " + ChatColor.RED + i2 + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(j2) + ChatColor.GRAY + " damage.");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your Implosion hit " + ChatColor.RED + i2 + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(j2) + ChatColor.GRAY + " damage.");
            }
        }
        new BukkitRunnable(){

            public void run() {
                sword.remove();
                stand.remove();
                this.cancel();
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 135L);
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 400;
    }

    @Override
    public int getManaCost() {
        return 100;
    }
}

