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
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.item.armor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.skill.Skill;
import net.hypixel.skyblock.item.Ability;
import net.hypixel.skyblock.item.AbilityActivation;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.item.SkullStatistics;
import net.hypixel.skyblock.item.SpecificItemType;
import net.hypixel.skyblock.item.TickingMaterial;
import net.hypixel.skyblock.item.ToolStatistics;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.DefenseReplacement;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.ManaReplacement;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PrecursorEye
implements MaterialFunction,
SkullStatistics,
ToolStatistics,
Ability,
TickingMaterial {
    public static final Map<UUID, Boolean> PrecursorLaser = new HashMap<UUID, Boolean>();
    public static final Map<UUID, Integer> PrecursorLivingSeconds = new HashMap<UUID, Integer>();
    int boosting;

    @Override
    public double getBaseHealth() {
        return 222.0;
    }

    @Override
    public double getBaseDefense() {
        return 222.0;
    }

    @Override
    public double getBaseIntelligence() {
        return 222.0;
    }

    @Override
    public String getURL() {
        return "72c0683b2019ca3d3947273e394bfca1b4d71b61b67b39474c2d6d73a9b67508";
    }

    @Override
    public String getDisplayName() {
        return "Precursor Eye";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public String getAbilityName() {
        return "Eye Beam";
    }

    @Override
    public String getAbilityDescription() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)"Fire a laser in front of you dealing &c4000 &7damage and costing &b40 &7mana. The damage increases by &c100% &7every second for &b5 &7seconds and the mana cost increases by &d25% &7every second. You can sneak again to de-activate the laser.");
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public boolean displayUsage() {
        return false;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.SNEAK;
    }

    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        if (!PrecursorLaser.containsKey(player.getUniqueId())) {
            PrecursorLaser.put(player.getUniqueId(), false);
        }
        if (!PrecursorLaser.get(player.getUniqueId()).booleanValue()) {
            PrecursorLaser.put(player.getUniqueId(), true);
            player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&dEye Beam &aActivated!"));
        } else {
            if (!PrecursorLaser.containsKey(player.getUniqueId())) {
                PrecursorLaser.put(player.getUniqueId(), false);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&dEye Beam &cDe-Activated!"));
            PrecursorLaser.put(player.getUniqueId(), false);
            PrecursorLivingSeconds.put(player.getUniqueId(), 0);
        }
        new BukkitRunnable(){

            public void run() {
                if (!PrecursorLaser.containsKey(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (!PrecursorLaser.get(player.getUniqueId()).booleanValue()) {
                    this.cancel();
                    return;
                }
                PrecursorEye.this.ticking(sItem, player);
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 15L, 15L);
    }

    public void ticking(SItem item, Player player) {
        SItem helmet = SItem.find(player.getInventory().getHelmet());
        if (helmet == null) {
            return;
        }
        if (helmet.getType() != SMaterial.PRECURSOR_EYE) {
            return;
        }
        String ACT = "true";
        if (!PrecursorLivingSeconds.containsKey(player.getUniqueId())) {
            PrecursorLivingSeconds.put(player.getUniqueId(), 0);
        } else if (PrecursorLaser.get(player.getUniqueId()).booleanValue()) {
            PrecursorLivingSeconds.put(player.getUniqueId(), PrecursorLivingSeconds.get(player.getUniqueId()) + 1);
        }
        if (PrecursorLaser.get(player.getUniqueId()).booleanValue()) {
            Location blockLocation = player.getTargetBlock((Set)null, 30).getLocation();
            Location crystalLocation = player.getEyeLocation();
            Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
            double count = 40.0;
            int manaCost = 0;
            this.boosting = PrecursorLivingSeconds.get(player.getUniqueId());
            int damage = 0;
            switch (this.boosting - 1) {
                case 0: {
                    manaCost = 40;
                    damage = 4000;
                    break;
                }
                case 1: {
                    manaCost = 50;
                    damage = 8000;
                    break;
                }
                case 2: {
                    manaCost = 62;
                    damage = 16000;
                    break;
                }
                case 3: {
                    manaCost = 77;
                    damage = 32000;
                    break;
                }
                case 4: {
                    manaCost = 96;
                    damage = 64000;
                    break;
                }
                default: {
                    manaCost = 120;
                    damage = 128000;
                }
            }
            int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100.0);
            final int cost = PlayerUtils.getFinalManaCost(player, item, manaCost);
            boolean take = PlayerUtils.takeMana(player, cost);
            if (!take) {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                final long c2 = System.currentTimeMillis();
                Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement(){

                    @Override
                    public String getReplacement() {
                        return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                    }

                    @Override
                    public long getEnd() {
                        return c2 + 1500L;
                    }
                });
                player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&dEye Beam &cDe-Activated!"));
                PrecursorLaser.put(player.getUniqueId(), false);
                PrecursorLivingSeconds.put(player.getUniqueId(), 0);
                return;
            }
            final long c3 = System.currentTimeMillis();
            Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement(){

                @Override
                public String getReplacement() {
                    return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + PrecursorEye.this.getAbilityName() + ChatColor.AQUA + ")";
                }

                @Override
                public long getEnd() {
                    return c3 + 2000L;
                }
            });
            for (int i2 = 1; i2 <= 40; ++i2) {
                for (Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 40.0)), 0.5, 0.0, 0.5)) {
                    if (ACT == "false") {
                        return;
                    }
                    if (entity.isDead() || !(entity instanceof LivingEntity) || entity.hasMetadata("GiantSword") || entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand) continue;
                    User user = User.getUser(player.getUniqueId());
                    double damage2 = damage;
                    int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                    double damageMultiplier = 1.0 + (double)combatLevel * 0.04;
                    user.damageEntity((Damageable)entity, (double)((int)damage2) * damageMultiplier);
                    if (PlayerUtils.Debugmsg.debugmsg) {
                        SLog.info("[DEBUG] " + player.getName() + " have dealt " + (double)((float)damage2) * damageMultiplier + " damage! (Eye Beam Ability)");
                    }
                    final ArmorStand stands = (ArmorStand)new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND, new Object[0]).getEntity();
                    int finaldmg = (int)((double)((int)damage2) * damageMultiplier);
                    if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                        int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                        if (defensepercent > 100) {
                            defensepercent = 100;
                        }
                        finaldmg -= finaldmg * defensepercent / 100;
                    }
                    stands.setCustomName("" + ChatColor.GRAY + finaldmg);
                    stands.setCustomNameVisible(true);
                    stands.setGravity(false);
                    stands.setVisible(false);
                    new BukkitRunnable(){

                        public void run() {
                            stands.remove();
                            this.cancel();
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), 30L);
                    ACT = "false";
                }
                player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 40.0)), Effect.COLOURED_DUST, 0, 1, 0.5882353f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
                player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply((double)i2 / 40.0)), Effect.COLOURED_DUST, 0, 1, 0.84313726f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
            }
        }
    }

    @Override
    public int getManaCost() {
        return 0;
    }
}

