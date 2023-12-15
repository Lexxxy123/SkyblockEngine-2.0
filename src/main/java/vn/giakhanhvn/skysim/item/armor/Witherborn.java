package vn.giakhanhvn.skysim.item.armor;

import java.util.HashMap;
import java.util.Iterator;

import net.md_5.bungee.api.ChatColor;

import java.math.RoundingMode;
import java.math.BigDecimal;

import org.bukkit.entity.Damageable;
import vn.giakhanhvn.skysim.listener.PlayerListener;
import vn.giakhanhvn.skysim.skill.Skill;
import vn.giakhanhvn.skysim.user.User;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.List;

import vn.giakhanhvn.skysim.util.SUtil;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.util.EntityManager;
import vn.giakhanhvn.skysim.util.Groups;
import vn.giakhanhvn.skysim.item.SItem;

import java.util.UUID;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Player;

public class Witherborn {
    private final Player p;
    private final int size;
    private Wither w;
    private boolean isTargetting;
    public Entity withersTarget;
    public static final Map<UUID, Witherborn> WITHER_MAP;
    public static final Map<UUID, Boolean> WITHER_COOLDOWN;

    public Witherborn(final Player p) {
        this.size = 790;
        this.isTargetting = false;
        this.withersTarget = null;
        this.p = p;
        this.isTargetting = false;
        Witherborn.WITHER_MAP.put(p.getUniqueId(), this);
    }

    public boolean checkCondition() {
        final SItem helm = SItem.find(this.p.getInventory().getHelmet());
        final SItem chest = SItem.find(this.p.getInventory().getChestplate());
        final SItem leg = SItem.find(this.p.getInventory().getLeggings());
        final SItem boots = SItem.find(this.p.getInventory().getBoots());
        if (helm == null || chest == null || leg == null || boots == null) {
            Witherborn.WITHER_MAP.remove(this.p.getUniqueId());
            this.w.remove();
            return false;
        }
        if (Groups.WITHER_HELMETS.contains(helm.getType()) && Groups.WITHER_CHESTPLATES.contains(chest.getType()) && Groups.WITHER_LEGGINGS.contains(leg.getType()) && Groups.WITHER_BOOTS.contains(boots.getType())) {
            return true;
        }
        Witherborn.WITHER_MAP.remove(this.p.getUniqueId());
        this.w.remove();
        return false;
    }

    public void spawnWither() {
        final Wither w = (Wither) this.p.getWorld().spawn(this.p.getLocation(), (Class) Wither.class);
        EntityManager.noAI(w);
        EntityManager.setNBTTag(w, "Invul", this.size);
        EntityManager.noHit(w);
        EntityManager.shutTheFuckUp(w);
        w.setMetadata("GiantSword", new FixedMetadataValue(SkySimEngine.getPlugin(), 0));
        w.setMetadata("NoAffect", new FixedMetadataValue(SkySimEngine.getPlugin(), 0));
        w.setMetadata("Ire", new FixedMetadataValue(SkySimEngine.getPlugin(), 0));
        this.w = w;
        new BukkitRunnable() {
            public void run() {
                if (!Witherborn.this.p.isOnline() || w.isDead() || Witherborn.getWitherbornInstance(Witherborn.this.p) == null) {
                    this.cancel();
                    return;
                }
                if (Witherborn.this.withersTarget == null) {
                    final List<Entity> er = w.getNearbyEntities(10.0, 10.0, 10.0);
                    er.removeIf(en -> en.hasMetadata("GiantSword") || en.hasMetadata("NPC"));
                    er.removeIf(en -> !(en instanceof LivingEntity));
                    er.removeIf(en -> en instanceof Player);
                    er.removeIf(en -> en instanceof ArmorStand);
                    er.removeIf(en -> en instanceof Villager);
                    er.removeIf(en -> en.isDead());
                    if (er.size() > 0) {
                        final LivingEntity le = (LivingEntity) er.get(SUtil.random(0, er.size() - 1));
                        Witherborn.this.setWithersTarget(le);
                    }
                }
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 100L, 100L);
        new BukkitRunnable() {
            float cout = Witherborn.this.p.getLocation().getYaw();

            public void run() {
                if (!Witherborn.this.p.isOnline() || w.isDead() || Witherborn.getWitherbornInstance(Witherborn.this.p) == null) {
                    w.remove();
                    Witherborn.this.p.playSound(Witherborn.this.p.getLocation(), Sound.WITHER_DEATH, 0.5f, 1.0f);
                    this.cancel();
                    return;
                }
                Witherborn.this.checkCondition();
                if (Witherborn.this.withersTarget != null) {
                    if (Witherborn.this.withersTarget instanceof LivingEntity) {
                        if (!Witherborn.this.isTargetting) {
                            final LivingEntity r1 = (LivingEntity) Witherborn.this.withersTarget;
                            Witherborn.this.selfSacrificeHeroAction(w, r1);
                            Witherborn.this.isTargetting = true;
                        }
                    } else {
                        Witherborn.this.withersTarget = null;
                    }
                }
                final Location loc = Witherborn.this.p.getLocation();
                loc.setYaw(this.cout);
                loc.setPitch(0.0f);
                loc.add(loc.getDirection().normalize().multiply(1.45));
                if (Witherborn.this.withersTarget == null && !Witherborn.this.isTargetting) {
                    w.teleport(loc);
                }
                this.cout += 7.0f;
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 2L, 2L);
    }

    public void selfSacrificeHeroAction(final Wither w, final Entity e) {
        new BukkitRunnable() {
            public void run() {
                if (!Witherborn.this.p.isOnline() || w.isDead()) {
                    w.remove();
                    w.getWorld().playEffect(w.getLocation(), Effect.EXPLOSION_LARGE, Witherborn.this.size);
                    w.getWorld().playEffect(w.getLocation(), Effect.EXPLOSION_LARGE, Witherborn.this.size);
                    w.getWorld().playSound(w.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                    w.getWorld().playSound(w.getLocation(), Sound.WITHER_DEATH, 0.3f, 1.0f);
                    this.cancel();
                    return;
                }
                if (e.isDead()) {
                    Witherborn.this.isTargetting = false;
                    Witherborn.this.withersTarget = null;
                    this.cancel();
                    return;
                }
                if (w.getNearbyEntities(0.2, 0.2, 0.2).contains(e)) {
                    Witherborn.WITHER_COOLDOWN.put(Witherborn.this.p.getUniqueId(), true);
                    SUtil.delay(() -> {
                        if (Witherborn.this.checkCondition()) {
                            Witherborn.WITHER_MAP.remove(Witherborn.this.p.getUniqueId());
                            final Witherborn a = new Witherborn(Witherborn.this.p);
                            a.spawnWither();
                        }
                        Witherborn.WITHER_COOLDOWN.put(Witherborn.this.p.getUniqueId(), false);
                    }, 600L);
                    w.remove();
                    int j = 0;
                    double d = 0.0;
                    for (final Entity entity : w.getNearbyEntities(3.0, 3.0, 3.0)) {
                        if (entity.isDead()) {
                            continue;
                        }
                        if (!(entity instanceof LivingEntity)) {
                            continue;
                        }
                        if (entity.hasMetadata("GiantSword")) {
                            continue;
                        }
                        if (entity.hasMetadata("NoAffect")) {
                            continue;
                        }
                        if (entity instanceof Player || entity instanceof EnderDragonPart || entity instanceof Villager) {
                            continue;
                        }
                        if (entity instanceof ArmorStand) {
                            continue;
                        }
                        ++j;
                        final double damage = 1000000.0;
                        double find = 0.0;
                        final int combatLevel = Skill.getLevel(User.getUser(Witherborn.this.p.getUniqueId()).getCombatXP(), false);
                        final double damageMultiplier = 1.0 + combatLevel * 0.04;
                        find = damage * damageMultiplier;
                        if (EntityManager.DEFENSE_PERCENTAGE.containsKey(entity)) {
                            int defensepercent = EntityManager.DEFENSE_PERCENTAGE.get(entity);
                            if (defensepercent > 100) {
                                defensepercent = 100;
                            }
                            find -= find * defensepercent / 100.0;
                        }
                        PlayerListener.spawnDamageInd(entity, find, false);
                        User.getUser(Witherborn.this.p.getUniqueId()).damageEntityIgnoreShield((Damageable) entity, find);
                        d += find;
                    }
                    d = new BigDecimal(d).setScale(1, RoundingMode.HALF_EVEN).doubleValue();
                    if (j > 0) {
                        if (j == 1) {
                            Witherborn.this.p.sendMessage(ChatColor.GRAY + "Your Witherborn hit " + ChatColor.RED + j + ChatColor.GRAY + " enemy for " + ChatColor.RED + SUtil.commaify(d) + ChatColor.GRAY + " damage.");
                        } else {
                            Witherborn.this.p.sendMessage(ChatColor.GRAY + "Your Witherborn hit " + ChatColor.RED + j + ChatColor.GRAY + " enemies for " + ChatColor.RED + SUtil.commaify(d) + ChatColor.GRAY + " damage.");
                        }
                    }
                }
                Witherborn.this.withersTarget = e;
                final Location r = w.getLocation().setDirection(e.getLocation().toVector().subtract(w.getLocation().toVector()));
                w.teleport(r);
                w.teleport(w.getLocation().add(w.getLocation().getDirection().normalize().multiply(0.3)));
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 2L, 1L);
    }

    public static Witherborn getWitherbornInstance(final Player p) {
        return Witherborn.WITHER_MAP.get(p.getUniqueId());
    }

    public Entity getWithersTarget() {
        return this.withersTarget;
    }

    public void setWithersTarget(final Entity withersTarget) {
        this.withersTarget = withersTarget;
    }

    static {
        WITHER_MAP = new HashMap<UUID, Witherborn>();
        WITHER_COOLDOWN = new HashMap<UUID, Boolean>();
    }
}
