package vn.giakhanhvn.skysim.item.weapon;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import org.bukkit.entity.Damageable;
import vn.giakhanhvn.skysim.util.Sputnik;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.Villager;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Effect;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.item.SMaterial;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.entity.Entity;
import vn.giakhanhvn.skysim.util.EntityManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

import java.util.Set;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.Ability;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class GiantSword implements ToolStatistics, MaterialFunction, Ability {
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
    public void onAbilityUse(final Player player, final SItem sItem) {
        int i = 0;
        double j = 0.0;
        final Location location = player.getTargetBlock((Set) null, 6).getLocation();
        final Giant sword = (Giant) player.getWorld().spawnEntity(location, EntityType.GIANT);
        sword.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
        EntityManager.noAI(sword);
        EntityManager.noHit(sword);
        EntityManager.shutTheFuckUp(sword);
        sword.setCustomName("Dinnerbone");
        sword.setMetadata("GiantSword", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        sword.setMetadata("NoAffect", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        final ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(true);
        stand.setPassenger(sword);
        sword.getEquipment().setItemInHand(SItem.of(SMaterial.IRON_SWORD).getStack());
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 10.0f, 0.0f);
        player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 0.9f, 0.35f);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        player.getWorld().playEffect(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)).add(0.0, -1.0, 0.0), Effect.EXPLOSION_HUGE, 1);
        for (final Entity entity : sword.getWorld().getNearbyEntities(sword.getLocation().add(sword.getLocation().getDirection().multiply(3.0)), 6.0, 6.0, 6.0)) {
            if (entity.isDead()) {
                continue;
            }
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            if (entity.hasMetadata("GiantSword")) {
                continue;
            }
            if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager) {
                continue;
            }
            if (entity instanceof ArmorStand) {
                continue;
            }
            final User user = User.getUser(player.getUniqueId());
            final double baseDamage = Sputnik.calculateMagicDamage(entity, player, 100000, 0.05);
            user.damageEntityIgnoreShield((Damageable) entity, baseDamage);
            ++i;
            j += baseDamage;
            PlayerListener.spawnDamageInd(entity, baseDamage, false);
        }
        if (i > 0) {
            if (i == 1) {
                player.sendMessage(ChatColor.GRAY + "Your Implosion hit " + ChatColor.RED + i + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(j) + ChatColor.GRAY + " damage.");
            } else {
                player.sendMessage(ChatColor.GRAY + "Your Implosion hit " + ChatColor.RED + i + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(j) + ChatColor.GRAY + " damage.");
            }
        }
        new BukkitRunnable() {
            public void run() {
                sword.remove();
                stand.remove();
                this.cancel();
            }
        }.runTaskLater(SkySimEngine.getPlugin(), 135L);
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
