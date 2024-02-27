package in.godspunky.skyblock.item.armor;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.features.skill.Skill;
import in.godspunky.skyblock.util.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.user.PlayerUtils;
import in.godspunky.skyblock.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PrecursorEye implements MaterialFunction, SkullStatistics, ToolStatistics, Ability, TickingMaterial {
    public static final Map<UUID, Boolean> PrecursorLaser;
    public static final Map<UUID, Integer> PrecursorLivingSeconds;
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
        return ChatColor.translateAlternateColorCodes('&', "Fire a laser in front of you dealing &c4000 &7damage and costing &b40 &7mana. The damage increases by &c100% &7every second for &b5 &7seconds and the mana cost increases by &d25% &7every second. You can sneak again to de-activate the laser.");
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
        if (!PrecursorEye.PrecursorLaser.containsKey(player.getUniqueId())) {
            PrecursorEye.PrecursorLaser.put(player.getUniqueId(), false);
        }
        if (!PrecursorEye.PrecursorLaser.get(player.getUniqueId())) {
            PrecursorEye.PrecursorLaser.put(player.getUniqueId(), true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dEye Beam &aActivated!"));
        } else {
            if (!PrecursorEye.PrecursorLaser.containsKey(player.getUniqueId())) {
                PrecursorEye.PrecursorLaser.put(player.getUniqueId(), false);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dEye Beam &cDe-Activated!"));
            PrecursorEye.PrecursorLaser.put(player.getUniqueId(), false);
            PrecursorEye.PrecursorLivingSeconds.put(player.getUniqueId(), 0);
        }
        new BukkitRunnable() {
            public void run() {
                if (!PrecursorEye.PrecursorLaser.containsKey(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (!PrecursorEye.PrecursorLaser.get(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                PrecursorEye.this.ticking(sItem, player);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 15L, 15L);
    }

    public void ticking(final SItem item, final Player player) {
        final SItem helmet = SItem.find(player.getInventory().getHelmet());
        if (helmet == null) {
            return;
        }
        if (helmet.getType() != SMaterial.PRECURSOR_EYE) {
            return;
        }
        String ACT = "true";
        if (!PrecursorEye.PrecursorLivingSeconds.containsKey(player.getUniqueId())) {
            PrecursorEye.PrecursorLivingSeconds.put(player.getUniqueId(), 0);
        } else if (PrecursorEye.PrecursorLaser.get(player.getUniqueId())) {
            PrecursorEye.PrecursorLivingSeconds.put(player.getUniqueId(), PrecursorEye.PrecursorLivingSeconds.get(player.getUniqueId()) + 1);
        }
        if (PrecursorEye.PrecursorLaser.get(player.getUniqueId())) {
            final Location blockLocation = player.getTargetBlock((Set) null, 30).getLocation();
            final Location crystalLocation = player.getEyeLocation();
            final Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
            final double count = 40.0;
            int manaCost = 0;
            this.boosting = PrecursorEye.PrecursorLivingSeconds.get(player.getUniqueId());
            int damage = 0;
            switch (this.boosting - 1) {
                case 0:
                    manaCost = 40;
                    damage = 4000;
                    break;
                case 1:
                    manaCost = 50;
                    damage = 8000;
                    break;
                case 2:
                    manaCost = 62;
                    damage = 16000;
                    break;
                case 3:
                    manaCost = 77;
                    damage = 32000;
                    break;
                case 4:
                    manaCost = 96;
                    damage = 64000;
                    break;
                default:
                    manaCost = 120;
                    damage = 128000;
                    break;
            }
            final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100.0);
            final int cost = PlayerUtils.getFinalManaCost(player, item, manaCost);
            final boolean take = PlayerUtils.takeMana(player, cost);
            if (!take) {
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
                final long c = System.currentTimeMillis();
                Repeater.MANA_REPLACEMENT_MAP.put(player.getUniqueId(), new ManaReplacement() {
                    @Override
                    public String getReplacement() {
                        return "" + ChatColor.RED + ChatColor.BOLD + "NOT ENOUGH MANA";
                    }

                    @Override
                    public long getEnd() {
                        return c + 1500L;
                    }
                });
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dEye Beam &cDe-Activated!"));
                PrecursorEye.PrecursorLaser.put(player.getUniqueId(), false);
                PrecursorEye.PrecursorLivingSeconds.put(player.getUniqueId(), 0);
                return;
            }
            final long c = System.currentTimeMillis();
            Repeater.DEFENSE_REPLACEMENT_MAP.put(player.getUniqueId(), new DefenseReplacement() {
                @Override
                public String getReplacement() {
                    return ChatColor.AQUA + "-" + cost + " Mana (" + ChatColor.GOLD + PrecursorEye.this.getAbilityName() + ChatColor.AQUA + ")";
                }

                @Override
                public long getEnd() {
                    return c + 2000L;
                }
            });
            for (int i = 1; i <= (int) count; ++i) {
                for (final Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.5, 0.0, 0.5)) {
                    if (ACT == "false") {
                        return;
                    }
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
                    final double damage2 = damage;
                    final int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                    final double damageMultiplier = 1.0 + combatLevel * 0.04;
                    user.damageEntity((Damageable) entity, (int) damage2 * damageMultiplier);
                    if (PlayerUtils.Debugmsg.debugmsg) {
                        SLog.info("[DEBUG] " + player.getName() + " have dealt " + (float) damage2 * damageMultiplier + " damage! (Eye Beam Ability)");
                    }
                    final ArmorStand stands = (ArmorStand) new SEntity(entity.getLocation().clone().add(SUtil.random(-1.5, 1.5), 1.0, SUtil.random(-1.5, 1.5)), SEntityType.UNCOLLIDABLE_ARMOR_STAND).getEntity();
                    int finaldmg = (int) ((int) damage2 * damageMultiplier);
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
                    new BukkitRunnable() {
                        public void run() {
                            stands.remove();
                            this.cancel();
                        }
                    }.runTaskLater(SkyBlock.getPlugin(), 30L);
                    ACT = "false";
                }
                player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.COLOURED_DUST, 0, 1, 0.5882353f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
                player.getWorld().spigot().playEffect(crystalLocation.clone().add(vector.clone().multiply(i / count)), Effect.COLOURED_DUST, 0, 1, 0.84313726f, 0.03529412f, 0.007843138f, 1.0f, 0, 64);
            }
        }
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    static {
        PrecursorLaser = new HashMap<UUID, Boolean>();
        PrecursorLivingSeconds = new HashMap<UUID, Integer>();
    }
}
