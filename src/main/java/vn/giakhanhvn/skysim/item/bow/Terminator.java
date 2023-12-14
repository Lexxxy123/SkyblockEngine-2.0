package vn.giakhanhvn.skysim.item.bow;

import java.util.HashMap;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.inventory.PlayerInventory;
import java.util.Iterator;
import org.bukkit.util.Vector;
import org.bukkit.Effect;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import org.bukkit.entity.Damageable;
import vn.giakhanhvn.skysim.util.EntityManager;
import vn.giakhanhvn.skysim.item.weapon.EdibleMace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Item;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.item.PlayerBoostStatistics;
import vn.giakhanhvn.skysim.entity.SEntity;
import vn.giakhanhvn.skysim.util.Groups;
import vn.giakhanhvn.skysim.enchantment.EnchantmentType;
import vn.giakhanhvn.skysim.enchantment.Enchantment;
import vn.giakhanhvn.skysim.item.SMaterial;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Villager;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.util.ManaReplacement;
import vn.giakhanhvn.skysim.Repeater;
import java.util.Set;
import vn.giakhanhvn.skysim.util.Sputnik;
import org.bukkit.event.entity.EntityShootBowEvent;
import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Arrow;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.util.SUtil;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import vn.giakhanhvn.skysim.user.PlayerStatistics;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.Sound;
import vn.giakhanhvn.skysim.util.InventoryUpdate;
import org.bukkit.event.block.Action;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import java.util.UUID;
import java.util.Map;
import vn.giakhanhvn.skysim.item.Ability;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class Terminator implements ToolStatistics, BowFunction, Ability
{
    public static final Map<UUID, Integer> CountTerm;
    public static final Map<UUID, Boolean> USABLE_TERM;
    
    @Override
    public String getDisplayName() {
        return "Terminator";
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
        return 310;
    }
    
    @Override
    public double getBaseCritDamage() {
        return 2.5;
    }
    
    @Override
    public double getBaseStrength() {
        return 50.0;
    }
    
    @Override
    public double getBaseAttackSpeed() {
        return 40.0;
    }
    
    @Override
    public boolean displayKills() {
        return false;
    }
    
    @Override
    public void onInteraction(final PlayerInteractEvent e) {
        final Player shooter = e.getPlayer();
        if (!Terminator.CountTerm.containsKey(shooter.getUniqueId())) {
            Terminator.CountTerm.put(shooter.getUniqueId(), 0);
        }
        if (shooter.getPlayer().getInventory().contains(Material.ARROW) || shooter.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && Terminator.CountTerm.get(shooter.getUniqueId()) < 3) {
                shooter.updateInventory();
                if (Terminator.USABLE_TERM.containsKey(shooter.getUniqueId()) && !Terminator.USABLE_TERM.get(shooter.getUniqueId())) {
                    return;
                }
                if (shooter.getGameMode() != GameMode.CREATIVE) {
                    InventoryUpdate.removeInventoryItems(shooter.getInventory(), Material.ARROW, 1);
                }
                shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
                final Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
                final Location l = location.clone();
                l.setYaw(location.getYaw());
                final Arrow a = shooter.getWorld().spawnArrow(l, l.getDirection(), 2.1f, 1.6f);
                a.setShooter((ProjectileSource)shooter);
                l.setYaw(location.getYaw() - 13.5f);
                shooter.getWorld().spawnArrow(l, l.getDirection(), 2.1f, 1.6f).setShooter((ProjectileSource)shooter);
                l.setYaw(location.getYaw() + 13.5f);
                shooter.getWorld().spawnArrow(l, l.getDirection(), 2.1f, 1.6f).setShooter((ProjectileSource)shooter);
                Terminator.USABLE_TERM.put(shooter.getUniqueId(), false);
                final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(shooter.getUniqueId());
                final double atkSpeed = (double)Math.min(100L, Math.round(statistics.getAttackSpeed().addAll()));
                SUtil.delay(() -> Terminator.USABLE_TERM.put(shooter.getUniqueId(), true), (long)(14.0 / (1.0 + atkSpeed / 100.0)));
            }
            else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                shooter.updateInventory();
                if (Terminator.USABLE_TERM.containsKey(shooter.getUniqueId()) && !Terminator.USABLE_TERM.get(shooter.getUniqueId())) {
                    return;
                }
                if (shooter.getGameMode() != GameMode.CREATIVE) {
                    InventoryUpdate.removeInventoryItems(shooter.getInventory(), Material.ARROW, 1);
                }
                shooter.playSound(shooter.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
                final Location location = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().toLocation(shooter.getWorld()));
                final Location l = location.clone();
                l.setYaw(location.getYaw());
                final Arrow a2 = shooter.getWorld().spawnArrow(l, l.getDirection(), 2.2f, 1.7f);
                a2.setShooter((ProjectileSource)shooter);
                l.setYaw(location.getYaw() - 13.5f);
                shooter.getWorld().spawnArrow(l, l.getDirection(), 2.2f, 1.7f).setShooter((ProjectileSource)shooter);
                l.setYaw(location.getYaw() + 13.5f);
                shooter.getWorld().spawnArrow(l, l.getDirection(), 2.2f, 1.7f).setShooter((ProjectileSource)shooter);
                Terminator.USABLE_TERM.put(shooter.getUniqueId(), false);
                final PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(shooter.getUniqueId());
                final double atkSpeed = (double)Math.min(100L, Math.round(statistics.getAttackSpeed().addAll()));
                SUtil.delay(() -> Terminator.USABLE_TERM.put(shooter.getUniqueId(), true), (long)(14.0 / (1.0 + atkSpeed / 100.0)));
            }
        }
    }
    
    @Override
    public void onBowShoot(final SItem bow, final EntityShootBowEvent e) {
        final Player player = (Player)e.getEntity();
        e.setCancelled(true);
        player.updateInventory();
    }
    
    @Override
    public String getAbilityName() {
        return "Salvation";
    }
    
    @Override
    public String getAbilityDescription() {
        return Sputnik.trans("Can be casted after landing &63 &7hits. &7Shoot a beam, penetrating up to &e5 &7foes and dealing &c2x &7the damage an arrow would. &7The beam always crits.");
    }
    
    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }
    
    @Override
    public void onAbilityUse(final Player player, final SItem sItem) {
        if (!Terminator.CountTerm.containsKey(player.getUniqueId())) {
            Terminator.CountTerm.put(player.getUniqueId(), 0);
        }
        String ACT = "true";
        if (Terminator.CountTerm.get(player.getUniqueId()) >= 3) {
            final Location blockLocation = player.getTargetBlock((Set)null, 30).getLocation();
            final Location crystalLocation = player.getEyeLocation().add(0.0, -0.1, 0.0);
            final Vector vector = blockLocation.clone().add(0.1, 0.0, 0.1).toVector().subtract(crystalLocation.clone().toVector());
            final double count = 40.0;
            final int manaPool = SUtil.blackMagic(PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()).getIntelligence().addAll() + 100.0);
            final int cost = PlayerUtils.getFinalManaCost(player, sItem, 100);
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
                return;
            }
            Terminator.CountTerm.put(player.getUniqueId(), 0);
            player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);
            for (int i = 1; i <= (int)count; ++i) {
                for (final Entity entity : player.getWorld().getNearbyEntities(crystalLocation.clone().add(vector.clone().multiply(i / count)), 0.7, 1.0, 0.7)) {
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
                    double enchantBonus = 0.0;
                    final double potionBonus = 0.0;
                    double bonusDamage = 0.0;
                    final PlayerStatistics statistics1 = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                    double critDamage = statistics1.getCritDamage().addAll();
                    final double speed = statistics1.getSpeed().addAll();
                    final double realSpeed = speed * 100.0 % 25.0;
                    final double realSpeedDIV = speed - realSpeed;
                    final double realSpeedDIVC = realSpeedDIV / 25.0;
                    final PlayerInventory inv = player.getInventory();
                    final SItem helmet = SItem.find(inv.getHelmet());
                    if (helmet != null) {
                        if (helmet.getType() == SMaterial.WARDEN_HELMET) {
                            enchantBonus += (100.0 + 20.0 * realSpeedDIVC) / 100.0;
                        }
                        else if (helmet.getType() == SMaterial.HIDDEN_VOIDLINGS_WARDEN_HELMET) {
                            enchantBonus += (100.0 + 30.0 * realSpeedDIVC) / 100.0;
                        }
                    }
                    for (final Enchantment enchantment : sItem.getEnchantments()) {
                        final EnchantmentType type1 = enchantment.getType();
                        final int level = enchantment.getLevel();
                        if (type1 == EnchantmentType.POWER) {
                            enchantBonus += level * 8 / 100.0;
                        }
                        if (type1 == EnchantmentType.SMITE && Groups.UNDEAD_MOBS.contains(entity.getType())) {
                            enchantBonus += level * 8 / 100.0;
                        }
                        if (type1 == EnchantmentType.ENDER_SLAYER && Groups.ENDERMAN.contains(entity.getType())) {
                            enchantBonus += level * 12 / 100.0;
                        }
                        if (type1 == EnchantmentType.BANE_OF_ARTHROPODS && Groups.ARTHROPODS.contains(entity.getType())) {
                            enchantBonus += level * 8 / 100.0;
                        }
                        if (type1 == EnchantmentType.DRAGON_HUNTER && Groups.ENDERDRAGON.contains(entity.getType())) {
                            enchantBonus += level * 8 / 100.0;
                        }
                        if (type1 == EnchantmentType.CRITICAL) {
                            critDamage += level * 10 / 100.0;
                        }
                        if (type1 == EnchantmentType.SOUL_EATER && PlayerUtils.SOUL_EATER_MAP.containsKey(player.getUniqueId()) && PlayerUtils.SOUL_EATER_MAP.get(player.getUniqueId()) != null) {
                            bonusDamage += PlayerUtils.SOUL_EATER_MAP.get(player.getUniqueId()).getStatistics().getDamageDealt() * (level * 2);
                            PlayerUtils.SOUL_EATER_MAP.put(player.getUniqueId(), null);
                        }
                    }
                    final SMaterial material = sItem.getType();
                    double hpbwea = 0.0;
                    if (sItem.getDataInt("hpb") > 0) {
                        hpbwea = sItem.getDataInt("hpb") * 2;
                    }
                    final PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics)material.getStatistics();
                    final double baseDamage = (5.0 + (playerBoostStatistics.getBaseDamage() + hpbwea)) * (1.0 + statistics1.getStrength().addAll() / 100.0);
                    final int combatLevel = Skill.getLevel(User.getUser(player.getUniqueId()).getCombatXP(), false);
                    final double weaponBonus = 0.0;
                    final double armorBonus = 1.0;
                    final int critChanceMul = 100;
                    final int chance = SUtil.random(0, 100);
                    if (chance > critChanceMul) {
                        critDamage = 0.0;
                    }
                    final double damageMultiplier = 1.0 + combatLevel * 0.04 + enchantBonus + weaponBonus;
                    final double finalCritDamage = critDamage;
                    double finalDamage = baseDamage * damageMultiplier * armorBonus * (1.0 + finalCritDamage) + bonusDamage;
                    final double finalPotionBonus = potionBonus;
                    if (entity.isDead()) {
                        continue;
                    }
                    if (!(entity instanceof LivingEntity)) {
                        continue;
                    }
                    if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager || entity instanceof ArmorStand || entity instanceof Item) {
                        continue;
                    }
                    if (entity instanceof ItemFrame) {
                        continue;
                    }
                    if (EdibleMace.edibleMace.containsKey(player.getUniqueId()) && EdibleMace.edibleMace.get(player.getUniqueId())) {
                        finalDamage = finalDamage;
                        EdibleMace.edibleMace.put(player.getUniqueId(), false);
                    }
                    if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                        int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                        if (defensepercent > 100) {
                            defensepercent = 100;
                        }
                        finalDamage -= finalDamage * defensepercent / 100.0;
                    }
                    user.damageEntity((Damageable)entity, finalDamage * 1.2);
                    player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.0f);
                    PlayerListener.spawnDamageInd(entity, finalDamage * 1.2, true);
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
    
    @Override
    public boolean displayUsage() {
        return false;
    }
    
    @Override
    public void onBowHit(final Entity hit, final Player shooter, final Arrow arrow, final SItem weapon, final AtomicDouble finalDamage) {
        if (hit.isDead()) {
            return;
        }
        if (!(hit instanceof LivingEntity)) {
            return;
        }
        if (hit.hasMetadata("GiantSword")) {
            return;
        }
        if (hit instanceof Player || hit instanceof Villager || hit instanceof ArmorStand) {
            return;
        }
        Terminator.CountTerm.put(shooter.getUniqueId(), Terminator.CountTerm.get(shooter.getUniqueId()) + 1);
    }
    
    @Override
    public boolean isEnchanted() {
        return true;
    }
    
    static {
        CountTerm = new HashMap<UUID, Integer>();
        USABLE_TERM = new HashMap<UUID, Boolean>();
    }
}
